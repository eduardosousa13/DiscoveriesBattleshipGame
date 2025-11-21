package iscteiul.ista.raulTesting.battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for class Position.
 * Author: raulf
 * Date: 2025-11-21 12:00:00
 * Cyclomatic Complexity:
 * - constructor: 1
 * - getRow(): 1
 * - getColumn(): 1
 * - hashCode(): 1
 * - equals(Object): 3
 * - isAdjacentTo(IPosition): 3 (note: tests include all 4 truth combinations for the compound condition)
 * - occupy(): 1
 * - shoot(): 1
 * - isOccupied(): 1
 * - isHit(): 1
 * - toString(): 1
 */
@DisplayName("Position entity tests")
class PositionTest {

    private Position pos;
    private Position posEqual;

    @BeforeEach
    void setUp() {
        // Create a default position used by many tests
        pos = new Position(2, 3);
        posEqual = new Position(2, 3);
    }

    @AfterEach
    void tearDown() {
        pos = null;
        posEqual = null;
    }

    @Test
    @DisplayName("constructor()")
    void constructor() {
        assertAll("constructor should initialize fields",
                () -> assertNotNull(pos, "Error: expected Position instance but got null"),
                () -> assertEquals(2, pos.getRow(), "Error: expected row 2 but got " + pos.getRow()),
                () -> assertEquals(3, pos.getColumn(), "Error: expected column 3 but got " + pos.getColumn()),
                () -> assertFalse(pos.isOccupied(), "Error: expected newly created position to be not occupied"),
                () -> assertFalse(pos.isHit(), "Error: expected newly created position to be not hit")
        );
    }

    @Test
    @DisplayName("getRow()")
    void getRow() {
        assertEquals(2, pos.getRow(), "Error: expected row 2 but got " + pos.getRow());
    }

    @Test
    @DisplayName("getColumn()")
    void getColumn() {
        assertEquals(3, pos.getColumn(), "Error: expected column 3 but got " + pos.getColumn());
    }

    @Test
    @DisplayName("hashCodeTest()")
    void hashCodeTest() {
        // equal positions should have same hashcode
        assertEquals(posEqual.hashCode(), pos.hashCode(), "Error: expected equal positions to have same hashCode");
    }

    @Test
    @DisplayName("equals1() - reflexive")
    void equals1() {
        assertTrue(pos.equals(pos), "Error: expected equals to be true for the same instance but was false");
    }

    @Test
    @DisplayName("equals2() - different instance same coords")
    void equals2() {
        assertTrue(pos.equals(posEqual), "Error: expected equals to be true for positions with same coordinates but was false");
    }

    @Test
    @DisplayName("equals3() - different types / different coordinates")
    void equals3() {
        Position other = new Position(5, 5);
        assertAll("equals negative cases",
                () -> assertFalse(pos.equals(other), "Error: expected positions with different coordinates to be not equal"),
                () -> assertFalse(pos.equals("not a position"), "Error: expected equals to return false when compared with other types")
        );
    }

    // isAdjacentTo: test all relevant true/false combinations for the compound condition
    @Test
    @DisplayName("isAdjacentTo1() - both differences <= 1 (adjacent)")
    void isAdjacentTo1() {
        Position neighbor = new Position(3, 3); // row diff = 1, col diff = 0 -> both true
        assertTrue(pos.isAdjacentTo(neighbor), "Error: expected positions to be adjacent but got not adjacent");
    }

    @Test
    @DisplayName("isAdjacentTo2() - row true, col false")
    void isAdjacentTo2() {
        Position p = new Position(3, 5); // row diff = 1 (true), col diff = 2 (false)
        assertFalse(pos.isAdjacentTo(p), "Error: expected non-adjacent when column difference > 1 but got adjacent");
    }

    @Test
    @DisplayName("isAdjacentTo3() - row false, col true")
    void isAdjacentTo3() {
        Position p = new Position(0, 3); // row diff = 2 (false), col diff = 0 (true)
        assertFalse(pos.isAdjacentTo(p), "Error: expected non-adjacent when row difference > 1 but got adjacent");
    }

    @Test
    @DisplayName("isAdjacentTo4() - both differences > 1")
    void isAdjacentTo4() {
        Position p = new Position(0, 0); // both diffs > 1
        assertFalse(pos.isAdjacentTo(p), "Error: expected non-adjacent when both differences > 1 but got adjacent");
    }

    @Test
    @DisplayName("occupy()")
    void occupy() {
        pos.occupy();
        assertTrue(pos.isOccupied(), "Error: expected position to be occupied after occupy() but it was not");
    }

    @Test
    @DisplayName("shoot()")
    void shoot() {
        pos.shoot();
        assertTrue(pos.isHit(), "Error: expected position to be hit after shoot() but it was not");
    }

    @Test
    @DisplayName("isOccupied()")
    void isOccupied() {
        assertFalse(pos.isOccupied(), "Error: expected newly created position to be not occupied");
    }

    @Test
    @DisplayName("isHit()")
    void isHit() {
        assertFalse(pos.isHit(), "Error: expected newly created position to be not hit");
    }

    @Test
    @DisplayName("toString()")
    void toStringTest() {
        assertEquals("Linha = 2 Coluna = 3", pos.toString(), "Error: expected specific toString output but got " + pos.toString());
    }

}

