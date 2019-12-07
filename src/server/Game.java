package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Contains the logic for the game of BattleShip. Has a Grid for each client.
 *
 * @author Evert Ball 
 * @author Carlee Yancey
 * @version 1.2.1 (08 December 2019)
 */
public class Game {
    /** The string for a Hit */
    private static final String HIT = "@";

    /** The string for a Miss */
    private static final String MISS = "X";

    /** A Constant representation of zero */
    private static final int ZERO = 0;

    /** A Constant representation of one */
    private static final int ONE = 1;

    /** A Constant representation of two */
    private static final int TWO = 2;

    /** A Constant representation of negative one */
    private static final int NEG_ONE = -1;

    /** A Constant representation of ten */
    private static final int TEN = 10;

    /** A Constant representation of seven */
    private static final int SEVEN = 7;

    /** A Constant representation of four */
    private static final int FOUR = 4;

    /** A Constant representation of nine */
    private static final int NINE = 9;

    /** A Constant representation of eight */
    private static final int EIGHT = 8;

    /** A Constant representation of six */
    private static final int SIX = 6;

    /** A Constant representation of three */
    private static final int THREE = 3;

    /** A Constant representation of five */
    private static final int FIVE  = 5;

    /** The number of ships each player gets */
    private int totalShips;

    /** A mapping of players to their boards */
    private HashMap<String, Grid> playerMap;

    /** ArrayList of the players in a game */
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

    /**
     * Removes a specified player from the game based on their username.
     *
     * @param nickname The username of the user.
     */
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
    private Grid getGrid(String player) {
        return this.playerMap.get(player);
    } // end getGrid method

    /**
     * The max number of ships each player is allowed.
     *
     * @return The number of ships each player is allowed.
     */
    private int getTotalShips() {
        return this.totalShips;
    }
    
    /**
     * Returns the number of players currently playing
     * @return The number of players in a game.
     */
    private int getNumPlayers() {
        return this.players.size();

    } // end getNumPlayers method

    /**
     * Returns an ArrayList of the current players in the game.
     *
     * @return An ArrayList of players.
     */
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
    private boolean validHit(String player, int row, int column){
        boolean valid;
        String hit;
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
        boolean attacked = false;
        boolean valid = validHit(nickname, row, column);
        try {
            if (board[row][column].equals("@") ||
                    board[row][column].equals("X")){
                System.out.println("INVALID MOVE");
                attacked = false;
            } else if (valid) {
                board[row][column] = HIT;
                attacked = true;
            } else {
                board[row][column] = MISS;
                attacked = true;
            }
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            System.out.println("Invalid Attack Coordinates. Please choose " +
                    " valid coordinates.");
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
    private void clearShip(String nickname,ArrayList<Integer> place){
        String[][] board = getGrid(nickname).getBoard();
        for (int i = 0; i < place.size(); i++) {
            int row = place.get(i);
            int col = place.get(i + ONE);
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
    public String isGameOver(){
        boolean over = false;
        String winner = "";
        String loser;
        for (int i = 0; i < getNumPlayers(); i++) {
            if(i == ZERO || !over){
                over = shipDestroyed(this.players.get(i));
                if (over) {
                    loser = this.players.get(i);
                    leave(loser);
                    System.out.println(loser + " REMOVED FROM THE GAME");
                }
            }
        }
        /* If the there are only two players and one quits over will still be
        true thus the need for the else if. */
        if(over && this.getNumPlayers() == ONE) {
            winner = this.players.get(ZERO);
            System.out.println("GAME OVER: "+ winner + " WINS!");
        }else if(this.getNumPlayers() == ONE){
            winner = this.players.get(ZERO);
            System.out.println("GAME OVER: "+ winner + " WINS!");
        }
        return winner;
    } // end isGameOver method

    /**
     * Checks to see if a certain ship is destroyed on a given player's board.
     *
     * @param player The player from which to get the board that is being
     *               checked.
     * @return over If all the ships are destroyed.
     */
    private boolean shipDestroyed(String player) {
        boolean over = true;
        String[][] board = getGrid(player).getBoard();
        for (String[] strings : board) {
            for (String value : strings) {
                if (value.equals(Ship.BATTLESHIP.getShip())) {
                    over = false;
                } else if (value.equals(Ship.CARRIER.getShip())) {
                    over = false;
                } else if (value.equals(Ship.CRUISER.getShip())) {
                    over = false;
                } else if (value.equals(Ship.DESTROYER.getShip())) {
                    over = false;
                } else if (value.equals(Ship.SUBMARINE.getShip())) {
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
    private void setTotalShips(int size){
        Random r = new Random();
        if(size == TEN){
            //random # between 4-6
            this.totalShips = r.nextInt(SEVEN - FOUR) + FOUR;
        }else if(size == NINE || size == EIGHT){
            //random # between 3-5
            this.totalShips = r.nextInt(SIX - THREE) + THREE;
        }else if(size == SEVEN || size == SIX) {
            //random # between 2-3
            this.totalShips = r.nextInt(FOUR - TWO) + TWO;
        }else if(size == FIVE){
            //random # between 1-2
            this.totalShips = r.nextInt(THREE - ONE) + ONE;
        }
    } // end setTotalShips method

    /**
     * Places random ships on the board.
     *
     * @param nickname The name of the player.
     */
    private void placeShips(String nickname){
        String[][] board = getGrid(nickname).getBoard();
        Random r = new Random();
        for(int i = 0; i < getTotalShips(); i++) {
            int randShipType = r.nextInt(FIVE);
            switch(randShipType) {
                case ZERO:
                     singlePlaceShip(Ship.CARRIER, board, nickname);
                    break;
                case ONE:
                   singlePlaceShip(Ship.BATTLESHIP, board, nickname);
                   break;
                case TWO:
                  singlePlaceShip(Ship.CRUISER, board , nickname);
                 break;
                case THREE:
                   singlePlaceShip(Ship.SUBMARINE, board, nickname);
                   break;
                case FOUR:
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
    public String show(String nickname, String current){
        String board = "";
        if (this.players.contains(nickname)) {
            if (current.equals(nickname)) {
                Grid show = getGrid(nickname);
                board = show.printGrid();
            } else {
                Grid show = getGrid(nickname);
                board = show.printPartialGrid();
            }
        }
        return board;
    }

    /**
     * Determines who's turn it currently is based of the number of turns.
     *
     * @param turns The turn number.
     * @return Who's turn of the game.
     */
    public String turn(int turns){
        String player = null;
        try {
            player = this.players.get(turns++);
        } catch(IndexOutOfBoundsException ioobe) {
            System.out.println("All players have quit.");
            System.exit(ZERO); // Successfully exit since all players quit
        }
        return player;
    }

    /**
     * Places a single ship on the board randomly.
     *
     * @param ship The ship being placed on board.
     * @param board The board being placed on.
     * @param nickname The name of the owner of the board.
     */
    private void singlePlaceShip(Ship ship, String[][] board, String nickname){
        Random r = new Random();
        ArrayList<Integer> place = new ArrayList<>();
        int oldRow, oldCol, way, row, col;
        oldRow = oldCol = way = row = col = NEG_ONE;
        int direction = r.nextInt(TWO);
        for(int j = 0; j < ship.getSize(); j++) {
            if (j == ZERO) {
                row = r.nextInt(board.length);
            } else if (direction == ONE) { // if dir=1, keep row
                row = oldRow;
            }else { // if dir=0, find a row
                if(j == ONE){
                    if(oldRow > (board.length / TWO)){
                        row = oldRow - ONE;
                        way = ZERO; // way=0 --> go west
                    }else{
                        row = oldRow + ONE;
                        way = ONE; // way=1 --> go east
                    }
                }else if( j > ONE){
                    if(way == ZERO){
                        row = oldRow - ONE;
                    }else if(way == ONE){
                        row = oldRow + ONE;
                    }
                }
            }
            if (j == ZERO) {
                col = r.nextInt(board[row].length);
            } else if (direction == ZERO) {
                col = oldCol;
            }else {
                if(j == ONE){
                    if(oldCol > (board.length / TWO)){
                        col = oldCol - ONE;
                        way = ZERO;
                    }else{
                        col = oldCol + ONE;
                        way = ONE;
                    }
                }else if(j > ONE){
                    if(way == ZERO){
                        col = oldCol - ONE;
                    }else if(way == ONE){
                        col = oldCol + ONE;
                    }
                }
            }
            if(row >= board.length || col >= board.length || row < ZERO
                    || col < ZERO || !(board[row][col].equals(" "))){
                clearShip(nickname,place);
                j = NEG_ONE;
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
} // end Game class
