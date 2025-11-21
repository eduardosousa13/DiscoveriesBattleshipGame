package iscteiul.ista.raulTesting.battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for class Compass.
 * Author: raulf
 * Date: 2025-11-18 12:00
 * Cyclomatic Complexity:
 * - constructor: 1
 * - getDirection(): 1
 * - toString(): 1
 * - charToCompass(): 5
 */
@DisplayName("Testes da entidade Compass")
class CompassTest {

    // Instance under test (for enums we still use one constant as a fixture)
    private Compass sut;

    @BeforeEach
    public void setup() {
        // create a default instance to use in instance-method tests
        sut = Compass.NORTH;
    }

    @AfterEach
    public void teardown() {
        // clean up
        sut = null;
    }

    // constructor: CC = 1
    @Test
    @DisplayName("Verifica existência dos constantes do enum Compass")
    public void constructor() {
        // Verify the enum constants exist and can be retrieved by name
        assertAll("Error: Compass enum constants should be present",
                () -> assertEquals(Compass.NORTH, Compass.valueOf("NORTH"), "Error: expected NORTH constant to exist and match Compass.NORTH"),
                () -> assertEquals(Compass.SOUTH, Compass.valueOf("SOUTH"), "Error: expected SOUTH constant to exist and match Compass.SOUTH"),
                () -> assertEquals(Compass.EAST, Compass.valueOf("EAST"), "Error: expected EAST constant to exist and match Compass.EAST"),
                () -> assertEquals(Compass.WEST, Compass.valueOf("WEST"), "Error: expected WEST constant to exist and match Compass.WEST"),
                () -> assertEquals(Compass.UNKNOWN, Compass.valueOf("UNKNOWN"), "Error: expected UNKNOWN constant to exist and match Compass.UNKNOWN")
        );
    }

    // getDirection(): CC = 1
    @Test
    @DisplayName("getDirection() should return the correct char for NORTH")
    public void getDirection() {
        // Using sut == NORTH set in @BeforeEach, expect 'n'
        char expected = 'n';
        char actual = sut.getDirection();
        assertEquals(expected, actual, "Error: expected getDirection() to return 'n' for NORTH but got '" + actual + "'");
    }

    // toString(): CC = 1
    @Test
    @DisplayName("toString() should match the character representation")
    public void toStringTest() {
        // Using sut == NORTH set in @BeforeEach, toString should be the character representation
        String expected = "n";
        String actual = sut.toString();
        assertEquals(expected, actual, "Error: expected toString() to return \"n\" for NORTH but got \"" + actual + "\"");
    }

    // charToCompass(): CC = 5 -> generate 5 test methods, each a distinct control-flow path

    @Test
    @DisplayName("charToCompass('n') -> NORTH")
    public void charToCompass1() {
        // path for 'n' -> NORTH
        Compass expected = Compass.NORTH;
        Compass actual = Compass.charToCompass('n');
        assertEquals(expected, actual, "Error: expected charToCompass('n') to return NORTH but got " + actual);
    }

    @Test
    @DisplayName("charToCompass('s') -> SOUTH")
    public void charToCompass2() {
        // path for 's' -> SOUTH
        Compass expected = Compass.SOUTH;
        Compass actual = Compass.charToCompass('s');
        assertEquals(expected, actual, "Error: expected charToCompass('s') to return SOUTH but got " + actual);
    }

    @Test
    @DisplayName("charToCompass('e') -> EAST")
    public void charToCompass3() {
        // path for 'e' -> EAST
        Compass expected = Compass.EAST;
        Compass actual = Compass.charToCompass('e');
        assertEquals(expected, actual, "Error: expected charToCompass('e') to return EAST but got " + actual);
    }

    @Test
    @DisplayName("charToCompass('o') -> WEST (Oeste)")
    public void charToCompass4() {
        // path for 'o' -> WEST (note: the source uses 'o' for Oeste/West)
        Compass expected = Compass.WEST;
        Compass actual = Compass.charToCompass('o');
        assertEquals(expected, actual, "Error: expected charToCompass('o') to return WEST but got " + actual);
    }

    @Test
    @DisplayName("charToCompass(default) -> UNKNOWN for unknown chars")
    public void charToCompass5() {
        // default path -> UNKNOWN (use an input that doesn't match any case)
        Compass expected = Compass.UNKNOWN;
        Compass actual = Compass.charToCompass('x');
        assertEquals(expected, actual, "Error: expected charToCompass('x') to return UNKNOWN but got " + actual);
    }

}