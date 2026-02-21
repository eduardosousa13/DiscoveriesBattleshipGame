/**
 * Represents the base class for all ships in the game.
 * This class defines the common properties and behaviors of a ship,
 * such as its category, bearing, and position.
 *
 * Invariants:
 * - A ship must always have a non-null bearing.
 * - A ship must always have a non-null initial position.
 */
package iscteiul.ista.battleship;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Ship implements IShip {

    private static final String GALEAO = "galeao";
    private static final String FRAGATA = "fragata";
    private static final String NAU = "nau";
    private static final String CARAVELA = "caravela";
    private static final String BARCA = "barca";

    /**
     * Factory method to build a ship of a specific kind.
     *
     * @param shipKind The kind of ship to build (e.g., "galeao", "fragata").
     * @param bearing The bearing of the ship.
     * @param pos The position of the ship.
     * @return A new Ship instance of the specified kind.
     */
    static Ship buildShip(String shipKind, Compass bearing, Position pos) {
        Ship s;
        switch (shipKind) {
            case BARCA:
                s = new Barge(bearing, pos);
                break;
            case CARAVELA:
                s = new Caravel(bearing, pos);
                break;
            case NAU:
                s = new Carrack(bearing, pos);
                break;
            case FRAGATA:
                s = new Frigate(bearing, pos);
                break;
            case GALEAO:
                s = new Galleon(bearing, pos);
                break;
            default:
                s = null;
        }
        return s;
    }


    private String category;
    private Compass bearing;
    private IPosition pos;
    protected List<IPosition> positions;


    /**
     * Constructs a new Ship with the given category, bearing, and position.
     *
     * @param category The category of the ship.
     * @param bearing The bearing of the ship.
     * @param pos The initial position of the ship.
     */
    public Ship(String category, Compass bearing, IPosition pos) {
        assert bearing != null;
        assert pos != null;

        this.category = category;
        this.bearing = bearing;
        this.pos = pos;
        positions = new ArrayList<>();
    }

    /*
     * (non-Javadoc)
     *
     * @see battleship.IShip#getCategory()
     */
    @Override
    public String getCategory() {
        return category;
    }

    /**
     * @return the positions
     */
    public List<IPosition> getPositions() {
        return positions;
    }

    /*
     * (non-Javadoc)
     *
     * @see battleship.IShip#getPosition()
     */
    @Override
    public IPosition getPosition() {
        return pos;
    }

    /*
     * (non-Javadoc)
     *
     * @see battleship.IShip#getBearing()
     */
    @Override
    public Compass getBearing() {
        return bearing;
    }

    /**
     * Checks if the ship is still floating.
     * A ship is considered floating if at least one of its positions has not been hit.
     *
     * @return true if the ship is still floating, false otherwise.
     */
    @Override
    public boolean stillFloating() {
        for (int i = 0; i < getSize(); i++)
            if (!getPositions().get(i).isHit())
                return true;
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see battleship.IShip#getTopMostPos()
     */
    @Override
    public int getTopMostPos() {
        int top = getPositions().get(0).getRow();
        for (int i = 1; i < getSize(); i++)
            if (getPositions().get(i).getRow() < top)
                top = getPositions().get(i).getRow();
        return top;
    }

    /*
     * (non-Javadoc)
     *
     * @see battleship.IShip#getBottomMostPos()
     */
    @Override
    public int getBottomMostPos() {
        int bottom = getPositions().get(0).getRow();
        for (int i = 1; i < getSize(); i++)
            if (getPositions().get(i).getRow() > bottom)
                bottom = getPositions().get(i).getRow();
        return bottom;
    }

    /*
     * (non-Javadoc)
     *
     * @see battleship.IShip#getLeftMostPos()
     */
    @Override
    public int getLeftMostPos() {
        int left = getPositions().get(0).getColumn();
        for (int i = 1; i < getSize(); i++)
            if (getPositions().get(i).getColumn() < left)
                left = getPositions().get(i).getColumn();
        return left;
    }

    /*
     * (non-Javadoc)
     *
     * @see battleship.IShip#getRightMostPos()
     */
    @Override
    public int getRightMostPos() {
        int right = getPositions().get(0).getColumn();
        for (int i = 1; i < getSize(); i++)
            if (getPositions().get(i).getColumn() > right)
                right = getPositions().get(i).getColumn();
        return right;
    }

    /*
     * (non-Javadoc)
     *
     * @see battleship.IShip#occupies(battleship.IPosition)
     */
    @Override
    public boolean occupies(IPosition pos) {
        assert pos != null;

        for (int i = 0; i < getSize(); i++)
            if (getPositions().get(i).equals(pos))
                return true;
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see battleship.IShip#tooCloseTo(battleship.IShip)
     */
    @Override
    public boolean tooCloseTo(IShip other) {
        assert other != null;

        Iterator<IPosition> otherPos = other.getPositions().iterator();
        while (otherPos.hasNext())
            if (tooCloseTo(otherPos.next()))
                return true;

        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see battleship.IShip#tooCloseTo(battleship.IPosition)
     */
    @Override
    public boolean tooCloseTo(IPosition pos) {
        for (int i = 0; i < this.getSize(); i++)
            if (getPositions().get(i).isAdjacentTo(pos))
                return true;
        return false;
    }


    /*
     * (non-Javadoc)
     *
     * @see battleship.IShip#shoot(battleship.IPosition)
     */
    @Override
    public void shoot(IPosition pos) {
        assert pos != null;

        for (IPosition position : getPositions()) {
            if (position.equals(pos))
                position.shoot();
        }
    }


    @Override
    public String toString() {
        return "[" + category + " " + bearing + " " + pos + "]";
    }

}
