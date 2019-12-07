package server;

/**
 * Enumeration of a ship that represents the ships character on the board and
 * the size of the ship.
 *
 * @author Carlee Yancey
 * @author Evert Ball
 * @version 1.0.0 (18 November 2019)
 */
public enum Ship {
    CARRIER("C", 5),
    BATTLESHIP("B", 4),
    CRUISER("R", 3),
    SUBMARINE("S", 3),
    DESTROYER("D", 2);

    /** String that represents a ship */
    private String ship;

    /** The size of the ship */
    private int size;

    /**
     * This constructor makes a ship based on the string representation and the
     * size of the ship.
     *
     * @param ship The string representation of the ship.
     * @param size The amount of space ship takes.
     */
    Ship(String ship, int size){
        this.ship = ship;
        this.size = size;
    }

    /**
     * This method returns the string representation of a ship.
     *
     * @return String representation of a ship.
     */
    public String getShip(){
        return this.ship;
    }

    /**
     * This method returns the size of the ship.
     *
     * @return Size of the ship.
     */
    public int getSize(){
        return this.size;
    }
}
