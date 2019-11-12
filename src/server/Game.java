package server;

import java.util.Random;

/**
 * Contains the logic for the game of BattleShip. Has a Grid for each client.
 *
 * @author Evert Ball and Carlee Yancey
 * @version 18 November 2019
 */
public class Game {
    /** The string for a Hit */
    private static final String HIT = "@";

    /** The string for a Miss */
    private static final String MISS = "X";

    /** The grid for player one */
    private Grid p1;

    /** The grid for player two */
    private Grid p2;

    /** The total number of ships left for player one */
    private int p1Total;

    /** The total number of ships left for player two */
    private int p2Total;

    /** The number of ships each player gets */
    private int totalShips;

    /**
     * This constructor creates a game with a grid without a given a size.
     */
    public Game(){
        this.p1 = new Grid();
        this.p2 = new Grid();
        this.setTotalShips(this.p1.getSize());
        p1Total = this.totalShips;
        p2Total = this.totalShips;
    }

    /**
     * This constructor creates a game with a grid of a given size.
     *
     * @param size Given size for a grid.
     */
    public Game(int size){
        this.p1 = new Grid(size);
        this.p2 = new Grid(size);
        this.setTotalShips(size);
        p1Total = this.totalShips;
        p2Total = this.totalShips;
    }

    //TODO The next four methods could be rewrote into two methods that each
    //     take a Player as a parameter. This allows the networking part of the 
    //     project to be much easier as any number of players can exist without 
    //     having to write new methods.

    /**
     * Returns the current grid for player 1.
     *
     * @return The current grid for player 1.
     */
    public Grid getP1() {
        return this.p1;
    } // end getP1 method

    /**
     * This returns the grid for player2
     *
     * @return the grid for player2
     */
    public Grid getP2() {
        return this.p2;
    } // end getP2 method

    /**
     * Returns the number of ships for player 1.
     *
     * @return the number of ships for player 1.
     */
    public int getP1Total() {
        return this.p1Total;
    } // end getP1Total method

    /**
     * Returns the number of ships for player 2.
     *
     * @return The number of ships for player 2.
     */
    public int getP2Total() {
        return this.p2Total;
    } // end getP2Total method

    /**
     * The max number of ships each player is allowed.
     *
     * @return The number of ships each player is allowed.
     */
    public int getTotalShips() {
        return this.totalShips;
    } // end getTotalShips method

    /**
     * Checks to see if coordinates given by a player are a valid hit.
     *
     * @param board The grid that is being checked.
     * @param row The row of the Grid.
     * @param column The column of the Grid.
     * @return True if the coordinates hit a ship, false otherwise.
     */
    public boolean validHit(String[][] board, int row, int column){
        boolean valid;
        String hit = board[row][column];
        if(hit.equals(" ") || hit.equals(MISS)){
            valid = false;
        }else{
            valid = true;
        }
        return valid;
    } // end validHit method

    /**
     * Places a hit or a miss on the board based on whether the given 
     * coordiantes were a valid hit.
     *
     * @param board The Grid that is being played.
     * @param row The row of the Grid.
     * @param column The column of the Grid.
     */
    public void hit(String[][] board, int row, int column){
        boolean valid = validHit(board, row, column);
        if(valid){
            board[row][column] = HIT;
        }else{
            board[row][column] = MISS;
        }
    } // end hit method

    /**
     * This method clears a certain ship from the grid.
     *
     * @param ship The ship to clear.
     * @param board The grid this is being cleared from.
     */
    public void clearShip(Ship ship, String[][]board){
        String shipToRemove = ship.getShip();
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board.length; j++){
                String current = board[i][j];
                if(current.equals(shipToRemove)){
                    board[i][j] = " ";
                } 
            } // end inner for loop
        } // end outter for loop
    } // end clear ship method

    /**
     * This method determines if the game is over. Returns true if one of the 
     * players has 0 (zero) ships remaining on the board. Prints the winner of 
     * the game to stdout.
     *
     * @return True if one of the players has zero ships remaining.
     */
    public boolean isGameOver(){
        boolean over;
        if(p1Total == 0){
            over = true;
            System.out.println("Player Two Wins");
        }if(p2Total == 0){
            over = true;
            System.out.println("Player One Wins");
        }else{
            over = false;
        }
        return over;
    } // end isGameOver method

    //TODO It might be good to have one method that checks if a ship is
    //     destoryed and we just pass in a player. This allows any number of 
    //     players to exist and the method won't have to be replicated for
    //     each one.

    /**
     * Checks to see if a certain ship is destroyed on p1 board.
     *
     * @param ship The ship that is being looked for.
     * @param board The board that is being checked.
     */
    public void shipDestroyedP1(Ship ship, String[][] board) {
        int hits = 0;
        int size = ship.getSize();
        String look = ship.getShip();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                String current = board[i][j];
                if (current.equals(look)) {
                    hits++;
                }
            }
        }
        if (hits == 0) {
            this.p1Total--;
        }
    } // end shipDestroyedP1 method

    /**
     *  Check to see if a certain ship is destroyed on p2 board.
     *
     * @param ship The ship that is being looked for.
     * @param board The board that is being checked.
     */
    public void shipDestroyedP2(Ship ship, String[][] board){
        int hits = 0;
        int size = ship.getSize();
        String look = ship.getShip();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                String current = board[i][j];
                if (current.equals(look)) {
                    hits++;
                }
            }
        }
        if (hits == 0) {
            this.p2Total--;
        }
    } // end shipDestoryedP2 method

    /**
     * This method set the number of ships to put on the board.
     *
     * @param size One way size of the grid.
     */
    public void setTotalShips(int size){
        Random r = new Random();
        if(size == 10){
            //random # between 4-6
            this.totalShips = r.nextInt(6 - 4) + 4;
        }else if(size == 9 || size == 8){
            //random # between 3-5
            this.totalShips = r.nextInt(5 - 3) + 3;
        }else if(size == 7 || size == 6) {
            //random # between 2-3
            this.totalShips = r.nextInt(3 - 2) + 2;
        }else if(size == 5){
            //random # between 1-2
            this.totalShips = r.nextInt(2 - 1) + 1;
        }
    } // end setTotalShips method

} // end Game class
