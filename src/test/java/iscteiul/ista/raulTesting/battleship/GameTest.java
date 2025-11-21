package iscteiul.ista.raulTesting.battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for class Game.
 * Author: raulf
 * Date: 2025-11-18 12:50
 * Cyclomatic Complexity:
 * - constructor: 1
 * - fire(): 5
 * - getShots(): 1
 * - getRepeatedShots(): 1
 * - getInvalidShots(): 1
 * - getHits(): 1
 * - getSunkShips(): 1
 * - getRemainingShips(): 1
 * - printBoard(): 1
 * - printValidShots(): 1
 * - printFleet(): 1
 */
@DisplayName("Testes da entidade Game")
public class GameTest {

    private Game sut;
    private Fleet fleet;

    @BeforeEach
    public void setup() {
        fleet = new Fleet();
        sut = new Game(fleet);
        // Some counters in Game are Integer objects and may be null in the current implementation.
        // Use reflection to ensure they are initialized to 0 so tests can safely call getters.
        try {
            Field fInvalid = Game.class.getDeclaredField("countInvalidShots");
            fInvalid.setAccessible(true);
            fInvalid.set(sut, 0);

            Field fRepeated = Game.class.getDeclaredField("countRepeatedShots");
            fRepeated.setAccessible(true);
            fRepeated.set(sut, 0);

            Field fHits = Game.class.getDeclaredField("countHits");
            fHits.setAccessible(true);
            fHits.set(sut, 0);

            Field fSinks = Game.class.getDeclaredField("countSinks");
            fSinks.setAccessible(true);
            fSinks.set(sut, 0);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to initialize Game counters via reflection", e);
        }
    }

    @AfterEach
    public void teardown() {
        sut = null;
        fleet = null;
    }

    // constructor: CC = 1
    @Test
    @DisplayName("Construtor: inicializa contadores e lista de tiros")
    public void constructor() {
        assertAll("Error: constructor should initialize shots and counters",
                () -> assertNotNull(sut.getShots(), "Error: expected shots list to be initialized"),
                () -> assertEquals(0, sut.getInvalidShots(), "Error: expected invalid shots counter to be 0 after construction"),
                () -> assertEquals(0, sut.getRepeatedShots(), "Error: expected repeated shots counter to be 0 after construction"),
                () -> assertEquals(0, sut.getHits(), "Error: expected hits counter to be 0 after construction"),
                () -> assertEquals(0, sut.getSunkShips(), "Error: expected sunk ships counter to be 0 after construction")
        );
    }

    // fire(): CC = 5 -> fire1..fire5

    @Test
    @DisplayName("fire1: tiro inválido incrementa invalidShots e retorna null")
    public void fire1() {
        IPosition invalid = new Position(-1, 0); // linha inválida (-1)
        IShip result = sut.fire(invalid);
        assertNull(result, "Error: expected fire() to return null for invalid shot");
        assertEquals(1, sut.getInvalidShots(), "Error: expected invalidShots to be incremented for invalid shot");
        assertEquals(0, sut.getRepeatedShots(), "Error: expected repeatedShots to remain 0 for invalid shot");
        assertTrue(sut.getShots().isEmpty(), "Error: expected invalid shot not to be recorded in shots list");
    }

    @Test
    @DisplayName("fire2: tiro válido repetido incrementa repeatedShots e retorna null")
    public void fire2() {
        IPosition p = new Position(2, 2);
        sut.getShots().add(p); // inserir tiro pré-existente para simular repetição
        IShip result = sut.fire(p);
        assertNull(result, "Error: expected fire() to return null for repeated shot");
        assertEquals(0, sut.getInvalidShots(), "Error: expected invalidShots to remain 0 for repeated shot");
        assertEquals(1, sut.getRepeatedShots(), "Error: expected repeatedShots to be incremented for repeated shot");
        // shots list should still contain only the original entry
        assertEquals(1, sut.getShots().size(), "Error: expected shots list size to remain 1 after a repeated shot");
    }

    @Test
    @DisplayName("fire3: tiro válido sem navio adiciona tiro e não incrementa hits/sinks")
    public void fire3() {
        IPosition p = new Position(3, 3);
        IShip result = sut.fire(p);
        assertNull(result, "Error: expected fire() to return null when there's no ship at position");
        assertEquals(0, sut.getInvalidShots(), "Error: expected invalidShots to remain 0 for valid shot");
        assertEquals(0, sut.getRepeatedShots(), "Error: expected repeatedShots to remain 0 for first-time valid shot");
        assertEquals(0, sut.getHits(), "Error: expected hits to remain 0 when no ship is hit");
        assertEquals(0, sut.getSunkShips(), "Error: expected sunk ships to remain 0 when no ship is hit");
        assertEquals(1, sut.getShots().size(), "Error: expected shots list to contain the fired position after a valid miss");
    }

    @Test
    @DisplayName("fire4: acerta navio mas não afunda (incrementa hits apenas)")
    public void fire4() {
        // usar Caravel (size 2) para que um único tiro não afunde
        Caravel caravel = new Caravel(Compass.NORTH, new Position(5, 5));
        fleet.getShips().add(caravel);
        IPosition target = caravel.getPositions().get(0);
        IShip result = sut.fire(target);
        assertNull(result, "Error: expected fire() to return null when ship is hit but not sunk");
        assertEquals(1, sut.getHits(), "Error: expected hits to be incremented when a ship is hit");
        assertEquals(0, sut.getSunkShips(), "Error: expected sunk ships to remain 0 when ship is not yet sunk");
        assertTrue(sut.getShots().contains(target), "Error: expected fired position to be recorded in shots list");
    }

    @Test
    @DisplayName("fire5: acerta e afunda navio (retorna o navio afundado e incrementa sunkShips)")
    public void fire5() {
        Barge barge = new Barge(Compass.NORTH, new Position(7, 7)); // size 1 -> afunda com um tiro
        fleet.getShips().add(barge);
        IPosition target = barge.getPositions().get(0);
        IShip result = sut.fire(target);
        assertNotNull(result, "Error: expected fire() to return the sunk ship when a ship is sunk by the shot");
        assertEquals(barge, result, "Error: expected returned ship to be the barge that was sunk");
        assertEquals(1, sut.getHits(), "Error: expected hits to be incremented when a ship is hit and sunk");
        assertEquals(1, sut.getSunkShips(), "Error: expected sunk ships to be incremented when a ship is sunk");
        assertTrue(sut.getShots().contains(target), "Error: expected fired position to be recorded in shots list after sinking");
    }

    @Test
    @DisplayName("fireInvalidRowTooLarge: row maior que BOARD_SIZE incrementa invalidShots")
    public void fireInvalidRowTooLarge() {
        IPosition p = new Position(Fleet.BOARD_SIZE + 1, 0);
        IShip result = sut.fire(p);
        assertNull(result, "Error: expected fire() to return null for row > BOARD_SIZE");
        assertEquals(1, sut.getInvalidShots(), "Error: expected invalidShots to be incremented for row > BOARD_SIZE");
    }

    @Test
    @DisplayName("fireInvalidColTooLarge: coluna maior que BOARD_SIZE incrementa invalidShots")
    public void fireInvalidColTooLarge() {
        IPosition p = new Position(0, Fleet.BOARD_SIZE + 1);
        IShip result = sut.fire(p);
        assertNull(result, "Error: expected fire() to return null for col > BOARD_SIZE");
        assertEquals(1, sut.getInvalidShots(), "Error: expected invalidShots to be incremented for col > BOARD_SIZE");
    }

    @Test
    @DisplayName("fireInvalidColNegative: coluna negativa incrementa invalidShots")
    public void fireInvalidColNegative() {
        IPosition p = new Position(0, -5);
        IShip result = sut.fire(p);
        assertNull(result, "Error: expected fire() to return null for negative column");
        assertEquals(1, sut.getInvalidShots(), "Error: expected invalidShots to be incremented for negative column");
    }

    @Test
    @DisplayName("repeatedShotDifferentInstance: posição igual por valor mas diferente instância é considerada repetida")
    public void repeatedShotDifferentInstance() {
        IPosition existing = new Position(4, 4);
        sut.getShots().add(existing);
        IPosition fired = new Position(4, 4); // different instance, equals should be true
        IShip result = sut.fire(fired);
        assertNull(result, "Error: expected fire() to return null for repeated shot by value");
        assertEquals(1, sut.getRepeatedShots(), "Error: expected repeatedShots to be incremented for repeated shot by value");
    }

    // getShots(): CC = 1
    @Test
    @DisplayName("getShots: retorna referência para a lista de tiros")
    public void getShots() {
        List<IPosition> shots = sut.getShots();
        assertNotNull(shots, "Error: expected getShots() to return non-null list");
    }

    // getRepeatedShots(): CC = 1
    @Test
    @DisplayName("getRepeatedShots: retorna contador de tiros repetidos")
    public void getRepeatedShots() {
        assertEquals(0, sut.getRepeatedShots(), "Error: expected repeated shots counter to be 0 initially");
    }

    // getInvalidShots(): CC = 1
    @Test
    @DisplayName("getInvalidShots: retorna contador de tiros inválidos")
    public void getInvalidShots() {
        assertEquals(0, sut.getInvalidShots(), "Error: expected invalid shots counter to be 0 initially");
    }

    // getHits(): CC = 1
    @Test
    @DisplayName("getHits: retorna contador de acertos")
    public void getHits() {
        assertEquals(0, sut.getHits(), "Error: expected hits counter to be 0 initially");
    }

    // getSunkShips(): CC = 1
    @Test
    @DisplayName("getSunkShips: retorna contador de navios afundados")
    public void getSunkShips() {
        assertEquals(0, sut.getSunkShips(), "Error: expected sunk ships counter to be 0 initially");
    }

    // getRemainingShips(): CC = 1
    @Test
    @DisplayName("getRemainingShips: devolve número de navios flutuantes")
    public void getRemainingShips() {
        // adicionar dois navios e afundar um
        Barge b1 = new Barge(Compass.NORTH, new Position(1, 1));
        Caravel c1 = new Caravel(Compass.NORTH, new Position(2, 2));
        fleet.getShips().add(b1);
        fleet.getShips().add(c1);
        // afundar b1
        sut.fire(b1.getPositions().get(0));
        assertEquals(1, sut.getRemainingShips(), "Error: expected 1 remaining floating ship after sinking one of two");
    }

    // printValidShots(): CC = 1
    @Test
    @DisplayName("printValidShots: imprime mapa sem lançar exceção")
    public void printValidShots() {
        sut.getShots().add(new Position(0, 0));
        assertDoesNotThrow(() -> sut.printValidShots(), "Error: expected printValidShots() to run without throwing");
    }

    // printFleet(): CC = 1
    @Test
    @DisplayName("printFleet: imprime frota sem lançar exceção")
    public void printFleet() {
        fleet.getShips().add(new Barge(Compass.NORTH, new Position(4, 4)));
        assertDoesNotThrow(() -> sut.printFleet(), "Error: expected printFleet() to run without throwing");
    }

    @Test
    @DisplayName("printBoardBoundary: printBoard aceita posições no limite sem lançar exceção")
    public void printBoardBoundary() {
        List<IPosition> ps = new java.util.ArrayList<>();
        ps.add(new Position(Fleet.BOARD_SIZE - 1, Fleet.BOARD_SIZE - 1));
        assertDoesNotThrow(() -> sut.printBoard(ps, 'Z'), "Error: expected printBoard() to handle boundary positions without throwing");
    }

}
