package server;

/**
 * The logic for a single board of Battleship.
 *
 * @author Evert Ball
 * @author Carlee Yancey
 * @version 18 November 2019
 */
public class Grid {

    /** A 2D array that represent the grid */
    private String[][] board;

    /** The default size of a grid */
    private static final int DEFAULT = 10;

    /**
     * This constructs a grid based on the default size.
     */
    public Grid(){
        this.board = new String[DEFAULT][DEFAULT];
    }
    /**
     * This constructs a grid bases on a size that is given.
     *
     * @param size The size of the grid
     */
    public Grid(int size){
        this.board = new String[size][size];
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
    public void printGrid(){
        for(int i = 0; i < this.board.length + 1; i++){
            if(i != 0) {
                System.out.print((i - 1) + " | ");
            }
            for(int j = 0; j < this.board.length; j++){
                if(i == 0){
                    System.out.print(j + " ");
                }else {
                    System.out.print(this.board[i][j] + " | ");
                }
            }
            System.out.println("\n+---+---+---+---+---+---+---+---+---+---+");
        }
    }

    public static void main(String[] args) {
        Grid grid = new Grid();
        grid.printGrid();
    }
} // end Grid class
