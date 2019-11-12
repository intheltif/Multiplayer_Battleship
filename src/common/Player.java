package common;

import server/Grid.java;

/**
 * Represents a player in a game of battleship.
 *
 * @author Evert Ball
 * @author Carlee Yancey
 * @version 18 November 2019
 */
public class Player {
    
    /** This grid that this play posseses */
    Grid grid;

    /** The chosen player name */
    String name;

    /** The number of ships this player currently has */
    int numShips;
    
    /** 
     * Creates a player with null and zero for all of it's values
     */
    Player() {
        this.grid = null;
        this.name = null;
        this.numShips = 0;
    } // end empty constructor
    
    /**
     * Creates a new player with a board, a name, and the number of ships the 
     * player starts with
     *
     * @param grid The Grid the player has containing all their ships.
     * @param name The chosen name of the player.
     * @param numShips The number of ships that the player starts with.
     */
    Player(Grid grid, String name, int numShips) {
        this.grid = grid;
        this.name = name;
        this.numShips = numShips;
    }

    //TODO Create needed methods (getters/setters, changeName, etc)

} // end Player class
