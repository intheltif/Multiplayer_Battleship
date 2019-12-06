package server;

/**
 * The logic for a single board of Battleship.
 *
 * @author Evert Ball
 * @author Carlee Yancey
 * @version 18 November 2019
 */
public class Grid {

    /** The default size of a grid */
    private static final int DEFAULT = 10;

    /** A Constant representation of zero */
    private static final int ZERO = 0;

    /** A Constant representation of one */
    private static final int ONE = 1;

    /** A 2D array that represent the grid */
    private String[][] board;

    /** The size of the grid */
    private int size;

    /**
     * This constructs a grid based on the default size.
     */
    public Grid(){
        this.board = new String[DEFAULT][DEFAULT];
        this.size = DEFAULT;
        this.setUpBoard();
    }

    /**
     * This constructs a grid bases on a size that is given.
     *
     * @param size The size of the grid
     */
    public Grid(int size){
        this.board = new String[size][size];
        this.size = size;
        this.setUpBoard();
    }

    /**
     * This method returns the current version if the grid.
     *
     * @return Current venison of the gird.
     */
    public String[][] getBoard(){
        return this.board;
    }

    /**
     * This prints the current state of the grid.
     */
    public String printGrid(){
        String board = "";
        for(int i = 0; i < this.board.length + ONE; i++){
            if(i != 0) {
                board = board.concat((i - ONE) + " | ");
            }
            for(int j = 0; j < this.board.length; j++){
                if(i == 0){
                    if(j == 0){
                        board = board.concat("    " + j + "   ");
                    }else {
                        board = board.concat(j + "   ");
                    }
                }else {
                    board = board.concat(this.board[i-ONE][j] + " | ");
                }
            }
            board = board.concat("\n  +---");
            for (int k = 1; k < this.board.length ; k++) {
                board = board.concat("+---");
            }
            board = board.concat("+\n");
        }
        return board;
    }

    /**
     * Prints the grid with only hits and misses.
     */
    public String printPartialGrid() {
        String board = "";
        for(int i = 0; i < this.board.length + ONE; i++){
            if(i != ZERO) {
                board = board.concat((i - 1) + " | ");
            }
            for(int j = 0; j < this.board.length; j++){
                if(i == ZERO){
                    if(j == ZERO){
                        board = board.concat("    " + j + "   ");
                    }else {
                        board = board.concat(j + "   ");
                    }
                }else {
                    if(this.board[i-1][j].equals("@") ||
                            this.board[i-1][j].equals("X")) {
                        board = board.concat(this.board[i - ONE][j] + " | ");
                    }else{
                        board = board.concat(" " + " | ");
                    }
                }
            }
            board = board.concat("\n  +---");
            for (int k = 1; k < this.board.length ; k++) {
                board = board.concat("+---");
            }
            board = board.concat("+\n");
        }
        return board;
    }

    /**
     * This sets the grid up with a blank space.
     */
    public void setUpBoard(){
        for(int i = 0; i < (this.board.length); i++){
            for(int j = 0; j < (this.board.length); j++){
              this.board[i][j] = " ";
            }
        }
    }

    /**
     * Used for testing of printing board.
     */
    public static void main(String[] args) {
        Grid grid = new Grid();
        grid.setUpBoard();
        grid.printPartialGrid();
    }
} // end Grid class
