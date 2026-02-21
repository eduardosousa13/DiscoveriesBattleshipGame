/**
 * Represents a Barge ship, which has a size of 1.
 */
package iscteiul.ista.battleship;

public class Barge extends Ship {
    private static final Integer SIZE = 1;
    private static final String NAME = "Barca";

    /**
     * Creates a new Barge ship with a given bearing and position.
     * @param bearing - barge bearing
     * @param pos     - upper left position of the barge
     */
    public Barge(Compass bearing, IPosition pos) {
        super(Barge.NAME, bearing, pos);
        getPositions().add(new Position(pos.getRow(), pos.getColumn()));
    }

    /**
     * Returns the size of the ship.
     * @return The size of the ship.
     */
    @Override
    public Integer getSize() {
        return SIZE;
    }

}
