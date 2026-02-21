/**
 * Represents a Carrack ship, which has a size of 3.
 */
package iscteiul.ista.battleship;

public class Carrack extends Ship {
    private static final Integer SIZE = 3;
    private static final String NAME = "Nau";

    /**
     * Creates a new Carrack ship with a given bearing and position.
     * @param bearing The orientation of the ship.
     * @param pos The starting position of the ship.
     * @throws IllegalArgumentException if the bearing is invalid.
     */
    public Carrack(Compass bearing, IPosition pos) throws IllegalArgumentException {
        super(Carrack.NAME, bearing, pos);
        switch (bearing) {
            case NORTH:
            case SOUTH:
                for (int r = 0; r < SIZE; r++)
                    getPositions().add(new Position(pos.getRow() + r, pos.getColumn()));
                break;
            case EAST:
            case WEST:
                for (int c = 0; c < SIZE; c++)
                    getPositions().add(new Position(pos.getRow(), pos.getColumn() + c));
                break;
            default:
                throw new IllegalArgumentException("ERROR! invalid bearing for the carrack");
        }
    }

    /**
     * Returns the size of the ship.
     * @return The size of the ship.
     */
    @Override
    public Integer getSize() {
        return Carrack.SIZE;
    }

}
