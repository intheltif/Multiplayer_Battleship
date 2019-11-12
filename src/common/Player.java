package common;

import server.Grid;

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
    public Player() {
        this.grid = null; //TODO Grid has a default we could set up a defaut grid here.
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
    public Player(Grid grid, String name, int numShips) {
        this.grid = grid;
        this.name = name;
        this.numShips = numShips;
    }

    /**
     * This method set the number of ships for the player.
     * @param num Number of ships
     */
    public void setNumShips(int num){ this.numShips = num; }

    /**
     * This method gets the number of ships the player has.
     * @return Number of ships
     */
    public int getNumShips(){ return this.numShips; }

    /**
     * This method gets the players grid.
     * @return The grid of the player.
     */
    public Grid getGrid(){ return this.grid; }

    /**
     * This method sets the players name.
     * @param name Name of the player.
     */
    public void setName(String name){ this.name = name; }

    /**
     * This method get the players Name.
     * @return The name of the player.
     */
    public String getName(){return this.name; }

    /**
     * This decrease the number of ships that the player has.
     * @return One less ship than the player had.
     */
    public int decreaseShips(){return this.numShips--;}


} // end Player class
