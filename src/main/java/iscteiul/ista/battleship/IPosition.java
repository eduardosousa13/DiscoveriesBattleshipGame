package iscteiul.ista.battleship;

/**
 * Represents a board position identified by row and column coordinates and
 * state flags used during gameplay.
 */
public interface IPosition {
    /**
     * Gets the row index of this position.
     *
     * @return row index
     */
    int getRow();

    /**
     * Gets the column index of this position.
     *
     * @return column index
     */
    int getColumn();

    /**
     * Compares this position with another object.
     *
     * @param other object to compare with
     * @return true when both represent the same row and column
     */
    boolean equals(Object other);

    /**
     * Checks whether this position is adjacent to another one, including
     * diagonal neighbors.
     *
     * @param other position to compare with
     * @return true if the two positions are adjacent
     */
    boolean isAdjacentTo(IPosition other);

    /**
     * Marks this position as occupied by part of a ship.
     */
    void occupy();

    /**
     * Marks this position as shot.
     */
    void shoot();

    /**
     * Indicates whether this position is occupied.
     *
     * @return true if occupied
     */
    boolean isOccupied();

    /**
     * Indicates whether this position was hit by a shot.
     *
     * @return true if hit
     */
    boolean isHit();
}
