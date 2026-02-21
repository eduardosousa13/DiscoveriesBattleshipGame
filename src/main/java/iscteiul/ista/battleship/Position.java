package iscteiul.ista.battleship;

import java.util.Objects;

/**
 * Default implementation of {@link IPosition} that stores board coordinates and
 * gameplay state (occupied and hit flags).
 */
public class Position implements IPosition {
    private int row;
    private int column;
    private boolean isOccupied;
    private boolean isHit;

    /**
     * Creates a board position with the given coordinates.
     *
     * @param row row index
     * @param column column index
     */
    public Position(int row, int column) {
        this.row = row;
        this.column = column;
        this.isOccupied = false;
        this.isHit = false;
    }

    /**
     * Gets the row index of this position.
     *
     * @return row index
     */
    @Override
    public int getRow() {
        return row;
    }

    /**
     * Gets the column index of this position.
     *
     * @return column index
     */
    @Override
    public int getColumn() {
        return column;
    }


    @Override
    public int hashCode() {
        return Objects.hash(column, isHit, isOccupied, row);
    }

    /**
     * Compares this position with another object based on coordinates.
     *
     * @param otherPosition object to compare with
     * @return true if both positions have the same row and column
     */
    @Override
    public boolean equals(Object otherPosition) {
        if (this == otherPosition)
            return true;
        if (otherPosition instanceof IPosition) {
            IPosition other = (IPosition) otherPosition;
            return (this.getRow() == other.getRow() && this.getColumn() == other.getColumn());
        } else {
            return false;
        }
    }

    /**
     * Validates whether this position is adjacent to another one, including
     * diagonals.
     *
     * @param other position to compare with
     * @return true if the distance in row and column is at most one cell
     */
    @Override
    public boolean isAdjacentTo(IPosition other) {
        return (Math.abs(this.getRow() - other.getRow()) <= 1 && Math.abs(this.getColumn() - other.getColumn()) <= 1);
    }

    /**
     * Marks this position as occupied.
     */
    @Override
    public void occupy() {
        isOccupied = true;
    }

    /**
     * Marks this position as hit.
     */
    @Override
    public void shoot() {
        isHit = true;
    }

    /**
     * Checks whether this position is occupied.
     *
     * @return true if occupied
     */
    @Override
    public boolean isOccupied() {
        return isOccupied;
    }

    /**
     * Checks whether this position has been hit.
     *
     * @return true if hit
     */
    @Override
    public boolean isHit() {
        return isHit;
    }

    @Override
    public String toString() {
        return ("Linha = " + row + " Coluna = " + column);
    }

}
