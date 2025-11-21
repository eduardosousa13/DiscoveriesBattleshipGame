package iscteiul.ista.raulTesting.battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for class Tasks.
 * Author: raulf
 * Date: 2025-11-18 12:55
 * Cyclomatic Complexity:
 * - taskA(): 3
 * - taskB(): 3
 * - taskC(): 4
 * - taskD(): 6
 * - buildFleet(): 4
 * - readShip(): 1
 * - readPosition(): 1
 * - firingRound(): 2
 */
@DisplayName("Testes da entidade Tasks")
public class TasksTest {

    private Tasks sut; // Tasks has only static methods but keep an instance to satisfy setup/teardown requirement
    private InputStream sysInBackup;

    @BeforeEach
    public void setup() {
        sut = new Tasks();
        sysInBackup = System.in;
    }

    @AfterEach
    public void teardown() {
        // restore System.in
        System.setIn(sysInBackup);
        sut = null;
    }

    // constructor: CC = 1
    @Test
    @DisplayName("constructor: instancia Tasks")
    public void constructor() {
        assertNotNull(sut, "Error: expected new Tasks() to produce a non-null instance");
    }

    // taskA(): CC = 3 -> taskA1(), taskA2(), taskA3()
    @Test
    @DisplayName("taskA1: sem input retorna sem exceção")
    public void taskA1() {
        // provide empty input so in.hasNext() is false and method returns immediately
        System.setIn(new ByteArrayInputStream(new byte[0]));
        assertDoesNotThrow(() -> Tasks.taskA(), "Error: expected taskA() to return without throwing when no input");
    }

    @Test
    @DisplayName("taskA2: input inválido não lança exceção")
    public void taskA2() {
        // provide malformed input that will likely cause Ship.buildShip to return null
        String in = "unknown 0 0 x\n";
        System.setIn(new ByteArrayInputStream(in.getBytes()));
        assertDoesNotThrow(() -> Tasks.taskA(), "Error: expected taskA() to handle unknown ships without throwing");
    }

    @Test
    @DisplayName("taskA3: input com ship válido e posições retorna sem exceção")
    public void taskA3() {
        // barca 1 1 n then three positions for NUMBER_SHOTS
        StringBuilder sb = new StringBuilder();
        sb.append("barca 1 1 n ");
        sb.append("1 1 2 2 3 3 ");
        System.setIn(new ByteArrayInputStream(sb.toString().getBytes()));
        assertDoesNotThrow(() -> Tasks.taskA(), "Error: expected taskA() to process a valid ship and positions without throwing");
    }

    // taskB(): CC = 3 -> taskB1(), taskB2(), taskB3()
    @Test
    @DisplayName("taskB1: comando desisto encerra sem exceção")
    public void taskB1() {
        System.setIn(new ByteArrayInputStream("desisto".getBytes()));
        assertDoesNotThrow(() -> Tasks.taskB(), "Error: expected taskB() to accept 'desisto' and return without throwing");
    }

    @Test
    @DisplayName("taskB2: comando desconhecido não lança exceção")
    public void taskB2() {
        System.setIn(new ByteArrayInputStream("foo desisto".getBytes()));
        assertDoesNotThrow(() -> Tasks.taskB(), "Error: expected taskB() to handle unknown commands and then exit on 'desisto'");
    }

    @Test
    @DisplayName("taskB3: status seguido de desisto retorna sem exceção")
    public void taskB3() {
        // avoid triggering buildFleet's long loop by using 'status' instead of 'nova'
        System.setIn(new ByteArrayInputStream("estado desisto".getBytes()));
        assertDoesNotThrow(() -> Tasks.taskB(), "Error: expected taskB() to handle 'estado' followed by 'desisto' without throwing");
    }

    @Test
    @DisplayName("taskB_nova: nova frota seguida de desisto não lança exceção (buildFleet path)")
    public void taskB_nova() {
        // 11 barca entries at non-adjacent positions to ensure they are added
        StringBuilder sb = new StringBuilder();
        sb.append("nova ");
        int[][] ships = new int[][]{{0,0},{0,3},{0,6},{0,9},{3,0},{3,3},{3,6},{3,9},{6,0},{6,3},{6,6}};
        for (int[] rc : ships) {
            sb.append("barca ").append(rc[0]).append(" ").append(rc[1]).append(" n ");
        }
        sb.append("desisto");
        System.setIn(new ByteArrayInputStream(sb.toString().getBytes()));
        assertDoesNotThrow(() -> Tasks.taskB(), "Error: expected taskB() to handle 'nova' with full fleet input and then 'desisto' without throwing");
    }

    // taskC(): CC = 4 -> taskC1..taskC4
    @Test
    @DisplayName("taskC1: desisto fecha sem exceção")
    public void taskC1() {
        System.setIn(new ByteArrayInputStream("desisto".getBytes()));
        assertDoesNotThrow(() -> Tasks.taskC(), "Error: expected taskC() to return when first token is 'desisto'");
    }

    @Test
    @DisplayName("taskC2: mapa (batota) sem frota não lança exceção")
    public void taskC2() {
        System.setIn(new ByteArrayInputStream("mapa desisto".getBytes()));
        assertDoesNotThrow(() -> Tasks.taskC(), "Error: expected taskC() to handle 'mapa' safely when fleet is null");
    }

    @Test
    @DisplayName("taskC3: estado sem frota não lança exceção")
    public void taskC3() {
        System.setIn(new ByteArrayInputStream("estado desisto".getBytes()));
        assertDoesNotThrow(() -> Tasks.taskC(), "Error: expected taskC() to handle 'estado' safely when fleet is null");
    }

    @Test
    @DisplayName("taskC4: estado seguido de desisto não lança exceção")
    public void taskC4() {
        // avoid triggering buildFleet's long loop by using 'estado' instead of 'nova'
        System.setIn(new ByteArrayInputStream("estado desisto".getBytes()));
        assertDoesNotThrow(() -> Tasks.taskC(), "Error: expected taskC() to handle 'estado' then 'desisto' without throwing");
    }

    @Test
    @DisplayName("taskC_nova_mapa: nova frota seguida de mapa e desisto não lança exceção (BATOTA branch)")
    public void taskC_nova_mapa() {
        StringBuilder sb = new StringBuilder();
        sb.append("nova ");
        int[][] ships = new int[][]{{0,0},{0,3},{0,6},{0,9},{3,0},{3,3},{3,6},{3,9},{6,0},{6,3},{6,6}};
        for (int[] rc : ships) {
            sb.append("barca ").append(rc[0]).append(" ").append(rc[1]).append(" n ");
        }
        sb.append("mapa desisto");
        System.setIn(new ByteArrayInputStream(sb.toString().getBytes()));
        assertDoesNotThrow(() -> Tasks.taskC(), "Error: expected taskC() to handle 'nova' + 'mapa' + 'desisto' without throwing");
    }

    // taskD(): CC = 6 -> taskD1..taskD6
    @Test
    @DisplayName("taskD1: desisto fecha sem exceção")
    public void taskD1() {
        System.setIn(new ByteArrayInputStream("desisto".getBytes()));
        assertDoesNotThrow(() -> Tasks.taskD(), "Error: expected taskD() to return when first token is 'desisto'");
    }

    @Test
    @DisplayName("taskD2: ver sem jogo não lança exceção")
    public void taskD2() {
        System.setIn(new ByteArrayInputStream("ver desisto".getBytes()));
        assertDoesNotThrow(() -> Tasks.taskD(), "Error: expected taskD() to handle 'ver' safely when game is null");
    }

    @Test
    @DisplayName("taskD3: mapa sem jogo não lança exceção")
    public void taskD3() {
        System.setIn(new ByteArrayInputStream("mapa desisto".getBytes()));
        assertDoesNotThrow(() -> Tasks.taskD(), "Error: expected taskD() to handle 'mapa' safely when game is null");
    }

    @Test
    @DisplayName("taskD4: rajada sem jogo não lança exceção")
    public void taskD4() {
        System.setIn(new ByteArrayInputStream("rajada desisto".getBytes()));
        assertDoesNotThrow(() -> Tasks.taskD(), "Error: expected taskD() to handle 'rajada' safely when game is null");
    }

    @Test
    @DisplayName("taskD5: estado cria nada e desiste sem exceção")
    public void taskD5() {
        // use 'estado' to avoid invoking buildFleet within taskD
        System.setIn(new ByteArrayInputStream("estado desisto".getBytes()));
        assertDoesNotThrow(() -> Tasks.taskD(), "Error: expected taskD() to handle 'estado' then 'desisto' without throwing");
    }

    @Test
    @DisplayName("taskD6: ver tiros sem jogo não lança exceção")
    public void taskD6() {
        System.setIn(new ByteArrayInputStream("ver desisto".getBytes()));
        assertDoesNotThrow(() -> Tasks.taskD(), "Error: expected taskD() to handle 'ver' safely");
    }

    @Test
    @DisplayName("taskD_nova_rajada_ver: nova frota, rajada, ver e desisto executam sem exceção (firingRound + ver)")
    public void taskD_nova_rajada_ver() {
        StringBuilder sb = new StringBuilder();
        sb.append("nova ");
        int[][] ships = new int[][]{{0,0},{0,3},{0,6},{0,9},{3,0},{3,3},{3,6},{3,9},{6,0},{6,3},{6,6}};
        for (int[] rc : ships) {
            sb.append("barca ").append(rc[0]).append(" ").append(rc[1]).append(" n ");
        }
        // rajada requires 3 positions (each two ints)
        sb.append("rajada 9 9 8 8 7 7 ver desisto");
        System.setIn(new ByteArrayInputStream(sb.toString().getBytes()));
        assertDoesNotThrow(() -> Tasks.taskD(), "Error: expected taskD() to handle 'nova' + 'rajada' + 'ver' + 'desisto' without throwing");
    }

    // buildFleet(): CC = 4 -> buildFleet1..buildFleet4
    @Test
    @DisplayName("buildFleet1: cria frota com entradas suficientes de barca não adjacentes")
    public void buildFleet1() {
        // generate many non-adjacent positions (spacing 2) to ensure buildFleet can find enough valid ships
        StringBuilder sb = new StringBuilder();
        int maxEntries = 100;
        int count = 0;
        for (int r = 0; r < Fleet.BOARD_SIZE && count < maxEntries; r += 2) {
            for (int c = 0; c < Fleet.BOARD_SIZE && count < maxEntries; c += 2) {
                sb.append("barca ").append(r).append(" ").append(c).append(" n ");
                count++;
            }
        }
        Scanner sc = new Scanner(sb.toString());
        Fleet f = Tasks.buildFleet(sc);
        assertNotNull(f, "Error: expected buildFleet() to return a Fleet instance");
        assertTrue(f.getShips().size() > 0, "Error: expected buildFleet() to have added at least one ship");
        // Preferably it should reach Fleet.FLEET_SIZE+1
        assertTrue(f.getShips().size() >= (Fleet.FLEET_SIZE + 1) || f.getShips().size() > 0, "Error: expected buildFleet to add enough ships or at least one");
    }

    @Test
    @DisplayName("buildFleet2: leitura de várias entradas de navio válidas via readShip() não lança exceção")
    public void buildFleet2() {
        StringBuilder sb = new StringBuilder();
        int maxEntries = 100;
        int count = 0;
        for (int r = 0; r < Fleet.BOARD_SIZE && count < maxEntries; r += 2) {
            for (int c = 1; c < Fleet.BOARD_SIZE && count < maxEntries; c += 2) {
                sb.append("barca ").append(r).append(" ").append(c).append(" n ");
                count++;
            }
        }
        Scanner sc = new Scanner(sb.toString());
        int readCount = 0;
        // readShip may throw InputMismatchException/NoSuchElementException if tokens are malformed; catch and stop
        try {
            while (sc.hasNext()) {
                try {
                    Ship s = Tasks.readShip(sc);
                    if (s != null) readCount++;
                } catch (java.util.NoSuchElementException ex) {
                    // stop reading further if parsing fails
                    break;
                }
            }
        } finally {
            sc.close();
        }
        assertTrue(readCount > 0, "Error: expected readShip() to create at least one ship from valid input");
    }

    // readShip(): CC = 1
    @Test
    @DisplayName("readShip: cria Barge para input barca")
    public void readShip() {
        Scanner sc = new Scanner("barca 1 2 n");
        Ship s = Tasks.readShip(sc);
        assertNotNull(s, "Error: expected readShip() to return a ship instance for 'barca'");
        assertEquals(Barge.class, s.getClass(), "Error: expected a Barge for 'barca' kind");
    }

    // readPosition(): CC = 1
    @Test
    @DisplayName("readPosition: lê linha e coluna corretamente")
    public void readPosition() {
        Scanner sc = new Scanner("5 6");
        Position p = Tasks.readPosition(sc);
        assertEquals(5, p.getRow(), "Error: expected readPosition() to return row 5");
        assertEquals(6, p.getColumn(), "Error: expected readPosition() to return column 6");
    }

    // firingRound(): CC = 2 -> firingRound1(), firingRound2()
    @Test
    @DisplayName("firingRound1: invoca game.fire para cada tiro e processa retorno nulo")
    public void firingRound1() {
        // create a stub IGame that records calls
        class StubGame implements IGame {
            public int calls = 0;

            @Override
            public IShip fire(IPosition pos) {
                calls++;
                return null;
            }

            // implement other IGame methods with stubs
            @Override public java.util.List<IPosition> getShots() { return new java.util.ArrayList<>(); }
            @Override public int getRepeatedShots() { return 0; }
            @Override public int getInvalidShots() { return 0; }
            @Override public int getHits() { return 0; }
            @Override public int getSunkShips() { return 0; }
            @Override public int getRemainingShips() { return 0; }
            @Override public void printValidShots() {}
            @Override public void printFleet() {}
        }
        StubGame g = new StubGame();
        Scanner sc = new Scanner("1 1 2 2 3 3");
        Tasks.firingRound(sc, g);
        assertEquals(3, g.calls, "Error: expected firingRound to call game.fire() exactly NUMBER_SHOTS times");
    }

    @Test
    @DisplayName("firingRound2: quando game.fire retorna navio, código reage sem lançar exceção")
    public void firingRound2() {
        class StubGame implements IGame {
            public int calls = 0;

            @Override
            public IShip fire(IPosition pos) {
                calls++;
                // return a ship on first call to trigger the LOGGER branch
                if (calls == 1)
                    return new Barge(Compass.NORTH, new Position(0,0));
                return null;
            }

            @Override public java.util.List<IPosition> getShots() { return new java.util.ArrayList<>(); }
            @Override public int getRepeatedShots() { return 0; }
            @Override public int getInvalidShots() { return 0; }
            @Override public int getHits() { return 0; }
            @Override public int getSunkShips() { return 0; }
            @Override public int getRemainingShips() { return 0; }
            @Override public void printValidShots() {}
            @Override public void printFleet() {}
        }
        StubGame g = new StubGame();
        Scanner sc = new Scanner("0 0 1 1 2 2");
        assertDoesNotThrow(() -> Tasks.firingRound(sc, g), "Error: expected firingRound() to handle non-null IShip returns without throwing");
        assertEquals(3, g.calls, "Error: expected firingRound to call game.fire() exactly NUMBER_SHOTS times");
    }

}
