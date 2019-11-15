package server;

import common.Player;

import java.util.ArrayList;
import java.util.HashMap;
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
     */
    public Game(int size){
        this.setTotalShips(size);
        this.playerMap = new HashMap<>();
    }
    
    /**
     * Adds a new player to the game.
     *
     * @param nickname The chosen nickname for the player.
     * @param size This is the grid size to be made.
     */
    public void join(String nickname,int size) {
        this.playerMap.put(nickname, new Grid(size));
        this.players.add(nickname);

    } // end join method

    public void leave(String nickname) {
        if (playerMap.contains(nickname)) {
            this.playerMap.remove(nickname);
        }
        if (players.contains(nickname)) {
            this.players.remove(nickname);
        }
    }

    /**
     * Returns the current grid for player that is passed in..
     *
     * @return The current grid for player.
     */
    public Grid getGrid(String player) {
        //TODO make it return the correct grid using the HashMap
        return this.playerMap.get(player);
    } // end getGrid method

    /**
     * Returns the number of ships for player that is passed in.
     *
     * @return the number of ships for player.
     */
    public int getPlayerTotal(Player player) {
        //TODO make it return the correct grid to corrent map.
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
    public boolean validHit(String player, int row, int column){
        boolean valid;
        String[][] board = getGrid(player).getBoard();
        String hit = board[row][column];
        if(hit.equals(" ")){
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
     * @param nickname The player from which to get the grid that is being checked.
     * @param row The row of the Grid.
     * @param column The column of the Grid.
     */
    public boolean hit(String nickname, int row, int column){
        String[][] board = getGrid(nickname).getBoard();
        boolean attacked;
        boolean valid = validHit(nickname, row, column);
        if(board[row][column].equals("@")|| board[row][column].equals("X")){
            System.out.println("INVALID MOVE");
            attacked = false;
        }else if(valid){
            board[row][column] = HIT;
            attacked = true;
        }else{
            board[row][column] = MISS;
            attacked = true;
        }
        return attacked;
    } // end hit method

    /**
     * This method clears a certain ship from the grid.
     *
     * @param ship The ship to clear.
     * @param nickname The player from which to get the grid that is being checked.
     */
    public void clearShip(Ship ship, String nickname){
        String shipToRemove = ship.getShip();
        String[][] board = getGrid(nickname).getBoard();
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
        boolean over =false;
        String loser = " ";
        for(String name: this.players) {
            if (over == false){
                over = shipDestroyed(name);
                loser = name;
            }
        }
        if(over == true) {
            for (String name : this.players) {
                if (!(loser.equals(name))){
                    System.out.println(name + " WINS!");
                }
            }
        }
        return over;
    } // end isGameOver method

    /**
     * Checks to see if a certain ship is destroyed on a given player's board.
     *
     * @param player The player from which to get the board that is being checked.
     */
    public boolean shipDestroyed(String player) {
        boolean over = true;
        String[][] board = getGrid(player).getBoard();
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board.length; j++){
                String item = board[i][j];
                if((item.equals(" "))){
                    over = false;
                }
            }
        }
        return over;
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

    public void placeShips(String nickname){
        //TODO Decouple logic to create ships to their own methods
        String[][] board = getGrid(nickname).getBoard();
        Random r = new Random();
        int randShipType = r.nextInt(5);
        for(int i=0; i < getTotalShips(); i++) {
           switch(randShipType) {
               case 0:
                   int oldRow = -1;
                   int oldCol = -1;
                   for(int i=0; i < ship.CARRIER.getSize(); i++) {
                       int row = r.nextInt(board.length);
                       while(row == oldRow) {
                           row = r.nextInt(board.length);
                       }
                       oldRow = row;
                       int col = r.nextInt(board[row].length);
                       while(col == oldCol) {
                           col = r.nextInt(board[row].length);
                       }
                       oldCol = col;
                       board[row][col] = "C"
                   }
                   break;
               case 1:
                   int oldRow = -1;
                   int oldCol = -1;
                   for(int i=0; i < ship.BATTLESHIP.getSize(); i++) {
                       int row = r.nextInt(board.length);
                       while(row == oldRow) {
                           row = r.nextInt(board.length);
                       }
                       oldRow = row;
                       int col = r.nextInt(board[row].length);
                       while(col == oldCol) {
                           col = r.nextInt(board[row].length);
                       }
                       oldCol = col;
                       board[row][col] = "B"
                   }
                   break;
               case 2:
                   int oldRow = -1;
                   int oldCol = -1;
                   for(int i=0; i < ship.CRUISER.getSize(); i++) {
                       int row = r.nextInt(board.length);
                       while(row == oldRow) {
                           row = r.nextInt(board.length);
                       }
                       oldRow = row;
                       int col = r.nextInt(board[row].length);
                       while(col == oldCol) {
                           col = r.nextInt(board[row].length);
                       }
                       oldCol = col;
                       board[row][col] = "R"
                   }
                   break;
               case 3:
                   int oldRow = -1;
                   int oldCol = -1;
                   for(int i=0; i < ship.SUBMARINE.getSize(); i++) {
                       int row = r.nextInt(board.length);
                       while(row == oldRow) {
                           row = r.nextInt(board.length);
                       }
                       oldRow = row;
                       int col = r.nextInt(board[row].length);
                       while(col == oldCol) {
                           col = r.nextInt(board[row].length);
                       }
                       oldCol = col;
                       board[row][col] = "S"
                   }
                   break;
               case 4:
                   int oldRow = -1;
                   int oldCol = -1;
                   for(int i=0; i < ship.DESTROYER.getSize(); i++) {
                       int row = r.nextInt(board.length);
                       while(row == oldRow) {
                           row = r.nextInt(board.length);
                       }
                       oldRow = row;
                       int col = r.nextInt(board[row].length);
                       while(col == oldCol) {
                           col = r.nextInt(board[row].length);
                       }
                       oldCol = col;
                       board[row][col] = "D"
                   }
                   break;
               default:
                   System.out.println("Outside range of ship types");
            } // end switch statement
        } // end for loop
    } // end placeShips method


    public void show(String nickname, String current){
        // "/show <username>
        if(this.playerMap.get(nickname).equals(current)){
            //TODO make sure it prints the board with ships on it
            Grid show = getGrid(nickname);
            show.printGrid();
        }else{
            //TODO make sure it prints the board with no ships
            Grid show = getGrid(nickname);
            show.printPartialGrid();
        }
    }
} // end Game class
