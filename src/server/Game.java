package server;

import common.Player;

import java.util.Random;

/**
 * Contains the logic for the game of BattleShip. Has a Grid for each client.
 *
 * @author Evert Ball 
 * @author Carlee Yancey
 * @version 18 November 2019
 */
public class Game {
    /** The string for a Hit */
    private static final String HIT = "@";

    /** The string for a Miss */
    private static final String MISS = "X";

    /** The number of ships each player gets */
    private int totalShips;

    /** A hashmap */
    private HashMap<String, Grid> playerMap;

    private ArrayList<String> players;

    /**
     * This constructor creates a game with a grid of a given size.
     *
     * @param size The size of the grid.
     * @param name The name of Player One.
     * @param name2 The name of Player Two
     */
    public Game(int size){
        this.setTotalShips(size);
        this.playerMap = new HashMap<>();
    }
    
    /**
     * Adds a new player to the game.
     *
     * @param nickname The chosen nickname for the player.
     */
    public void join(String nickname) {

        this.playerMap.put(nickname, new Grid());
        this.players.add(nickname);

    } // end join method

    public void leave(String nickname) {
        if(playerMap.contains(nickname)) {
            this.playerMap.remove(nickname);
        }
        if(players.contains(nickname)) {
            this.players.remove(nickname);
        }


    /**
     * Returns the current grid for player that is passed in..
     *
     * @return The current grid for player.
     */
    public Grid getGrid(Player player) {
        return player.getGrid();
    } // end getGrid method

    /**
     * Returns the number of ships for player that is passed in.
     *
     * @return the number of ships for player.
     */
    public int getPlayerTotal(Player player) {
        return player.getNumShips();
    } // end getPlayerTotal method

    /**
     * The max number of ships each player is allowed.
     *
     * @return The number of ships each player is allowed.
     */
    public int getTotalShips() {
        return this.totalShips;
    } // end getTotalShips method
    
    /**
     * Returns the number of players currently playing
     * TODO Still have to handle clients dropping unexpectedly (ie CTRL+C)
     * @return The number of players in a game.
     */
    public int getNumPlayers() {

        return this.players.size();

    } // end getNumPlayers method

    /**
     * Checks to see if coordinates given by a player are a valid hit.
     *
     * @param player The player from which to get the grid that is being checked.
     * @param row The row of the Grid.
     * @param column The column of the Grid.
     * @return True if the coordinates hit a ship, false otherwise.
     */
    public boolean validHit(Player player, int row, int column){
        boolean valid;
        String[][] board = player.getGrid().getBoard();
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
     * @param player The player from which to get the grid that is being checked.
     * @param row The row of the Grid.
     * @param column The column of the Grid.
     */
    public void hit(Player player, int row, int column){
        String[][] board = player.getGrid().getBoard();
        boolean valid = validHit(player, row, column);
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
     * @param player The player from which to get the grid that is being checked.
     */
    public void clearShip(Ship ship, Player player){
        String shipToRemove = ship.getShip();
        String[][] board = player.getGrid().getBoard();
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
        if(this.player1.getNumShips() == 0){
            over = true;
            System.out.println(this.player2.getName() + " WINS!");
        }if(this.player2.getNumShips() == 0){
            over = true;
            System.out.println(this.player1.getName() + " WINS!");
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
     * Checks to see if a certain ship is destroyed on a given player's board.
     *
     * @param ship The ship that is being looked for.
     * @param player The player from which to get the board that is being checked.
     */
    public void shipDestroyed(Ship ship, Player player) {
        int hits = 0;
        String look = ship.getShip();
        String[][] board = player.getGrid().getBoard();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                String current = board[i][j];
                if (current.equals(look)) {
                    hits++;
                }
            }
        }
        if (hits == 0) {
            player.decreaseShips();
        }
    } // end shipDestroyed method

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
