package server;

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

    /** A HashMap */
    private HashMap<String, Grid> playerMap;

    /** ArrayList of the games players */
    private ArrayList<String> players;

    /**
     * This constructor creates a game with a grid of a given size.
     *
     * @param size The size of the grid.
     */
    public Game(int size){
        this.setTotalShips(size);
        this.playerMap = new HashMap<>();
        this.players = new ArrayList<>();
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
        placeShips(nickname);

    } // end join method

    public void leave(String nickname) {
        if (playerMap.containsKey(nickname)) {
            this.playerMap.remove(nickname);
        }
        if (players.contains(nickname)) {
            this.players.remove(nickname);
        }
    }

    /**
     * Returns the current grid for player that is passed in.
     *
     * @param player The player of the grid we need.
     * @return The current grid for player.
     */
    public Grid getGrid(String player) {
        return this.playerMap.get(player);
    } // end getGrid method

    /**
     * The max number of ships each player is allowed.
     *
     * @return The number of ships each player is allowed.
     */
    public int getTotalShips() {
        return this.totalShips;
    }
    
    /**
     * Returns the number of players currently playing
     * TODO Still have to handle clients dropping unexpectedly (ie CTRL+C)
     * @return The number of players in a game.
     */
    public int getNumPlayers() {
        return this.players.size();

    } // end getNumPlayers method

    public ArrayList<String> getPlayers(){
        return this.players;
    }

    /**
     * Checks to see if coordinates given by a player are a valid hit.
     *
     * @param player The player from which to get the grid that is being
     *               checked.
     * @param row The row of the Grid.
     * @param column The column of the Grid.
     * @return True if the coordinates hit a ship, false otherwise.
     */
    public boolean validHit(String player, int row, int column){
        boolean valid;
        String hit = "";
        String[][] board = getGrid(player).getBoard();
        try{
            hit = board[row][column];
        } catch(IndexOutOfBoundsException ioobe) {
            System.out.println("Invalid Attack Coordinates. Please choose " +
                " valid coordinates.");
            return false;
        }
        if(hit.equals(" ")){
            valid = false;
        }else{
            valid = true;
        }
        return valid;
    } // end validHit method

    /**
     * Places a hit or a miss on the board based on whether the given 
     * coordinates were a valid hit.
     *
     * @param nickname The player from which to get the grid that is being
     *                 checked.
     * @param row The row of the Grid.
     * @param column The column of the Grid.
     * @return attacked If the hit was successful.
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
     * @param place The spots the ships is in.
     * @param nickname The player from which to get the grid that is being
     *                 checked.
     */
    public void clearShip(String nickname,ArrayList<Integer> place){
        String[][] board = getGrid(nickname).getBoard();
        for (int i = 0; i < place.size(); i++) {
            int row = place.get(i);
            int col = place.get(i+1);
            board[row][col] = " ";
            i++;
        }
    } // end clear ship method

    /**
     * This method determines if the game is over. Returns true if one of the 
     * players has 0 (zero) ships remaining on the board. Prints the winner of 
     * the game to stdout.
     *
     * @return True if one of the players has zero ships remaining.
     */
    public boolean isGameOver(){
        boolean over = false;
        String loser = "";
        for (int i = 0; i < getNumPlayers(); i++) {
            if(i == 0 || !over){
                over = shipDestroyed(this.players.get(i));
                if (over) {
                    loser = this.players.get(i);
                }
            }
        }
        if(over) {
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
     * @param player The player from which to get the board that is being
     *               checked.
     * @return over If all the ships are destroyed.
     */
    public boolean shipDestroyed(String player) {
        boolean over = true;
        String[][] board = getGrid(player).getBoard();
        for(int i = 0; i < board.length; i++){
            for (int j = 0; j < board[i].length; j++) {
                String value = board[i][j];
                if(value.equals(Ship.BATTLESHIP.getShip())){
                    over = false;
                }else if (value.equals(Ship.CARRIER.getShip())){
                    over = false;
                }else if (value.equals(Ship.CRUISER.getShip())){
                    over = false;
                }else if (value.equals(Ship.DESTROYER.getShip())){
                    over = false;
                }else if (value.equals(Ship.SUBMARINE.getShip())){
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
            this.totalShips = r.nextInt(7 - 4) + 4;
        }else if(size == 9 || size == 8){
            //random # between 3-5
            this.totalShips = r.nextInt(6 - 3) + 3;
        }else if(size == 7 || size == 6) {
            //random # between 2-3
            this.totalShips = r.nextInt(4 - 2) + 2;
        }else if(size == 5){
            //random # between 1-2
            this.totalShips = r.nextInt(3 - 1) + 1;
        }
    } // end setTotalShips method

    /**
     * Places random ships on the board.
     *
     * @param nickname The name of the player.
     */
    public void placeShips(String nickname){
        String[][] board = getGrid(nickname).getBoard();
        Random r = new Random();
        for(int i=0; i < getTotalShips(); i++) {
            int randShipType = r.nextInt(5);
            switch(randShipType) {
                case 0:
                     singlePlaceShip(Ship.CARRIER, board, nickname);
                    break;
               case 1:
                   singlePlaceShip(Ship.BATTLESHIP, board, nickname);
                   break;
               case 2:
                  singlePlaceShip(Ship.CRUISER, board , nickname);
                 break;
               case 3:
                   singlePlaceShip(Ship.SUBMARINE, board, nickname);
                   break;
               case 4:
                   singlePlaceShip(Ship.DESTROYER, board, nickname);
                   break;
               default:
                   System.out.println("Outside range of ship types");
            } // end switch statement
        } // end for loop
    } // end placeShips method

    /**
     * Shows the correct board for the player.
     *
     * @param nickname The board being shown
     * @param current Who turn it currently is.
     */
    public void show(String nickname, String current){
        if (this.players.contains(nickname)) {
            if (current.equals(nickname)) {
                Grid show = getGrid(nickname);
                show.printGrid();
            } else {
                Grid show = getGrid(nickname);
                show.printPartialGrid();
            }
        }
    }

    /**
     * Determines who's turn it currently is based of the number of turns.
     *
     * @param turns The turn number.
     * @return Who's turn of the game.
     */
    public String turn(int turns){
        int turn = turns % getNumPlayers();
        return this.players.get(turn);

    }

    /**
     * Places a single ship on the board randomly.
     *
     * @param ship The ship being placed on board.
     * @param board The board being placed on.
     * @param nickname The name of the owner of the board.
     */
    public void singlePlaceShip(Ship ship, String[][] board, String nickname){
        Random r = new Random();
        ArrayList<Integer> place = new ArrayList<>();
        int oldRow, oldCol, way, row, col;
        oldRow = oldCol = way = row = col = -1;
        int direction = r.nextInt(2);
        for(int j = 0; j < ship.getSize(); j++) {
            if (j == 0) {
                row = r.nextInt(board.length);
            } else if (direction == 1) { // if dir=1, keep row
                row = oldRow;
            }else { // if dir=0, find a row
                if(j == 1){
                    if(oldRow > (board.length / 2)){
                        row = oldRow-1;
                        way = 0; // way=0 --> go west
                    }else{
                        row = oldRow+1;
                        way = 1; // way=1 --> go east
                    }
                }else if( j > 1){
                    if(way == 0){
                        row = oldRow-1;
                    }else if(way == 1){
                        row = oldRow+1;
                    }
                }
            }
            if (j == 0) {
                col = r.nextInt(board[row].length);
            } else if (direction == 0) {
                col = oldCol;
            }else {
                if(j == 1){
                    if(oldCol > (board.length/2)){
                        col = oldCol-1;
                        way = 0;
                    }else{
                        col = oldCol+ 1;
                        way = 1;
                    }
                }else if(j > 1){
                    if(way == 0){
                        col = oldCol-1;
                    }else if(way == 1){
                        col = oldCol+1;
                    }
                }
            }
            if(row >= board.length || col >= board.length || row < 0
                    || col < 0 || !(board[row][col].equals( " "))){
                clearShip(nickname,place);
                j = -1;
                row = r.nextInt(board.length);
                col = r.nextInt(board.length);
                oldCol = col;
                oldRow = row;
                place.clear();
            }else if (board[row][col].equals(" ")){
                board[row][col] = ship.getShip();
                place.add(row);
                place.add(col);
                oldRow = row;
                oldCol = col;
            }
        }
    }//end of singlePlaceShip

    public static void main(String[] args){
        Game g = new Game(10);
        g.join("rob", 10);
        g.show("rob","rob");
    }
} // end Game class
