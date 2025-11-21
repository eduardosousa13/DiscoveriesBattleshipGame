package iscteiul.ista.raulTesting.battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for class Fleet (implements IFleet).
 * Author: raulf
 * Date: 2025-11-18 12:45
 * Cyclomatic Complexity:
 * - constructor: 1
 * - getShips(): 1
 * - addShip(): 4
 * - getShipsLike(): 2
 * - getFloatingShips(): 2
 * - shipAt(): 2
 * - printStatus(): 1
 */
@DisplayName("Testes da entidade Fleet / IFleet")
public class FleetTest {

    private Fleet sut;
    private Position origin;

    @BeforeEach
    public void setup() {
        sut = new Fleet();
        origin = new Position(3, 3);
    }

    @AfterEach
    public void teardown() {
        sut = null;
        origin = null;
    }

    // constructor: CC = 1
    @Test
    @DisplayName("Construtor: inicializa lista de ships")
    public void constructor() {
        assertNotNull(sut.getShips(), "Error: expected getShips() to return a non-null list after construction");
        assertEquals(0, sut.getShips().size(), "Error: expected new Fleet to start with 0 ships");
    }

    // getShips(): CC = 1
    @Test
    @DisplayName("getShips: retorna a lista mutável de navios")
    public void getShips() {
        List<IShip> list = sut.getShips();
        assertNotNull(list, "Error: expected getShips() to return a non-null list");
    }

    // addShip(): CC = 4 -> success, exceed fleet size, outside board, collision risk
    @Test
    @DisplayName("addShip: adiciona navio válido dentro do tabuleiro quando não há colisão")
    public void addShip1() {
        Ship s = new Barge(Compass.NORTH, origin);
        boolean added = sut.addShip(s);
        assertTrue(added, "Error: expected addShip() to return true for a valid ship added to an empty fleet");
        assertTrue(sut.getShips().contains(s), "Error: expected the ship to be present in fleet after successful addShip()");
    }

    @Test
    @DisplayName("addShip: falha quando a frota excede o tamanho máximo permitido")
    public void addShip2() {
        // prefill underlying list to exceed FLEET_SIZE
        for (int i = 0; i <= IFleet.FLEET_SIZE; i++) {
            sut.getShips().add(new Barge(Compass.NORTH, new Position(0, i)));
        }
        Ship extra = new Barge(Compass.NORTH, new Position(9, 9));
        boolean added = sut.addShip(extra);
        assertFalse(added, "Error: expected addShip() to return false when fleet size is already greater than FLEET_SIZE");
    }

    @Test
    @DisplayName("addShip: falha quando o navio está fora do tabuleiro (isInsideBoard false)")
    public void addShip3() {
        // place ship far outside board to fail isInsideBoard
        Ship out = new Carrack(Compass.NORTH, new Position(20, 20));
        boolean added = sut.addShip(out);
        assertFalse(added, "Error: expected addShip() to return false for ships outside the board");
    }

    @Test
    @DisplayName("addShip: falha quando existe risco de colisão com ship já presente")
    public void addShip4() {
        // add an existing ship at (5,5)
        Ship first = new Barge(Compass.NORTH, new Position(5, 5));
        sut.getShips().add(first);
        // new ship placed adjacent to first -> tooCloseTo should be true
        Ship second = new Barge(Compass.NORTH, new Position(6, 5));
        boolean added = sut.addShip(second);
        assertFalse(added, "Error: expected addShip() to return false when collision risk exists with existing ships");
    }

    // getShipsLike(): CC = 2
    @Test
    @DisplayName("getShipsLike: retorna lista com correspondência de categoria")
    public void getShipsLike1() {
        Ship s1 = new Barge(Compass.NORTH, new Position(1, 1)); // "Barca"
        Ship s2 = new Caravel(Compass.NORTH, new Position(2, 2)); // "Caravela"
        sut.getShips().add(s1);
        sut.getShips().add(s2);
        List<IShip> barca = sut.getShipsLike("Barca");
        assertEquals(1, barca.size(), "Error: expected one 'Barca' in the fleet");
        assertEquals(s1, barca.get(0), "Error: expected returned ship to be the Barge instance");
    }

    @Test
    @DisplayName("getShipsLike: retorna lista vazia quando não há correspondências")
    public void getShipsLike2() {
        List<IShip> none = sut.getShipsLike("NonExistingCategory");
        assertTrue(none.isEmpty(), "Error: expected getShipsLike() to return empty list for unknown category");
    }

    // getFloatingShips(): CC = 2
    @Test
    @DisplayName("getFloatingShips: inclui apenas navios que ainda flutuam")
    public void getFloatingShips1() {
        Ship s1 = new Barge(Compass.NORTH, new Position(4, 4));
        Ship s2 = new Barge(Compass.NORTH, new Position(6, 6));
        sut.getShips().add(s1);
        sut.getShips().add(s2);
        // mark all positions of s2 as hit to sink it
        for (IPosition p : s2.getPositions()) p.shoot();
        List<IShip> floating = sut.getFloatingShips();
        assertTrue(floating.contains(s1) && !floating.contains(s2), "Error: expected only the non-sunk ship to be in getFloatingShips()");
    }

    @Test
    @DisplayName("getFloatingShips: retorna lista vazia quando não há navios flutuantes")
    public void getFloatingShips2() {
        Ship s = new Barge(Compass.NORTH, new Position(7, 7));
        sut.getShips().add(s);
        // sink it
        for (IPosition p : s.getPositions()) p.shoot();
        List<IShip> floating = sut.getFloatingShips();
        assertTrue(floating.isEmpty(), "Error: expected getFloatingShips() to be empty when all ships are sunk");
    }

    // shipAt(): CC = 2
    @Test
    @DisplayName("shipAt: retorna navio quando posição é ocupada")
    public void shipAt1() {
        Ship s = new Barge(Compass.NORTH, new Position(2, 2));
        sut.getShips().add(s);
        IShip found = sut.shipAt(new Position(2, 2));
        assertEquals(s, found, "Error: expected shipAt() to return the ship occupying the position");
    }

    @Test
    @DisplayName("shipAt: retorna null quando posição não é ocupada")
    public void shipAt2() {
        IShip found = sut.shipAt(new Position(99, 99));
        assertNull(found, "Error: expected shipAt() to return null for a position with no ship");
    }

    // printStatus(): CC = 1
    @Test
    @DisplayName("printStatus: não lança exceção ao imprimir estado da frota")
    public void printStatus() {
        // populate with some ships so printing does something
        sut.getShips().add(new Barge(Compass.NORTH, new Position(1, 1)));
        sut.getShips().add(new Caravel(Compass.NORTH, new Position(3, 3)));
        assertDoesNotThrow(() -> sut.printStatus(), "Error: expected printStatus() to run without throwing an exception");
    }

    @Test
    @DisplayName("printShipsByCategory: imprime por cada categoria conhecida sem lançar exceção")
    public void printShipsByCategoryAll() {
        // Add one ship of each category
        sut.getShips().add(new Galleon(Compass.NORTH, new Position(0, 0)));
        sut.getShips().add(new Frigate(Compass.NORTH, new Position(2, 2)));
        sut.getShips().add(new Carrack(Compass.NORTH, new Position(4, 4)));
        sut.getShips().add(new Caravel(Compass.NORTH, new Position(6, 6)));
        sut.getShips().add(new Barge(Compass.NORTH, new Position(8, 8)));

        assertAll("Error: printShipsByCategory should not throw for known categories",
                () -> assertDoesNotThrow(() -> sut.printShipsByCategory("Galeao")),
                () -> assertDoesNotThrow(() -> sut.printShipsByCategory("Fragata")),
                () -> assertDoesNotThrow(() -> sut.printShipsByCategory("Nau")),
                () -> assertDoesNotThrow(() -> sut.printShipsByCategory("Caravela")),
                () -> assertDoesNotThrow(() -> sut.printShipsByCategory("Barca"))
        );
    }

}
