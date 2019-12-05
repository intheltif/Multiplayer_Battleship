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
     * This method sets a grid given a 2D String array.
     *
     * @param board The given 2D array.
     */
    public void setBoard(String[][] board) {
        this.board = board;
    }

    /**
     * This prints the current state of the grid.
     */
    public String printGrid(){
        String board = "";
        System.out.println("IN print Grid");
        for(int i = 0; i < this.board.length + 1; i++){
            if(i != 0) {
                board = board.concat((i - 1) + " | ");
                System.out.print((i - 1) + " | ");
            }
            for(int j = 0; j < this.board.length; j++){
                if(i == 0){
                    if(j == 0){
                        board = board.concat("    " + j + "   ");
                        System.out.print("    " + j + "   ");
                    }else {
                        board = board.concat(j + "   ");
                        System.out.print(j + "   ");
                    }
                }else {
                    board = board.concat(this.board[i-1][j] + " | ");
                    System.out.print(this.board[i-1][j] + " | ");
                }
            }
            board = board.concat("\n  +---");
            System.out.print("\n  +---");
            for (int k = 1; k < this.board.length ; k++) {
                board = board.concat("+---");
                System.out.print("+---");

            }
            board = board.concat("+\n");
            System.out.println("+");
        }
        return board;
    }

    /**
     * Prints the grid with only hits and misses.
     */
    public String printPartialGrid() {
        String board = "";
        for(int i = 0; i < this.board.length + 1; i++){
            if(i != 0) {
                board = board.concat((i - 1) + " | ");
                System.out.print((i - 1) + " | ");
            }
            for(int j = 0; j < this.board.length; j++){
                if(i == 0){
                    if(j == 0){
                        board = board.concat("    " + j + "   ");
                        System.out.print("    " + j + "   ");
                    }else {
                        board = board.concat(j + "   ");
                        System.out.print(j + "   ");
                    }
                }else {
                    if(this.board[i-1][j].equals("@") ||
                            this.board[i-1][j].equals("X")) {
                        board = board.concat(this.board[i - 1][j] + " | ");
                        System.out.print(this.board[i - 1][j] + " | ");
                    }else{
                        board = board.concat(" " + " | ");
                        System.out.print(" " + " | ");
                    }
                }
            }
            board = board.concat("\n  +---");
            System.out.print("\n  +---");
            for (int k = 1; k < this.board.length ; k++) {
                board = board.concat("+---");
                System.out.print("+---");
            }
            board = board.concat("+\n");
            System.out.println("+");
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
     * This method returns the size of the grid a in a single direction.
     *
     * @return The size of the grid.
     */
    public int getSize(){
        return this.size;
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
