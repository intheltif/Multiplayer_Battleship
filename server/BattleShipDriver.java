package server;

/**
 * The driver of the server side of our application. Validates command line 
 * input and creates a BattleServer that listens for remote host requests.
 *
 * @author Evert Ball and Carlee Yancey
 * @version 18 November 2019
 */
public class BattleShipDriver {

    /** A constant value for successful exit */
    private static final int SUCCESS = 0;

    /** A constant value for failed exit */
    private static final int FAILURE = 1;
    
    /** The valid number of command line args */
    private static final int NUM_ARGS = 2;
    
    /** Usage message to print when program is used incorrectly */
    private static final int USAGE_MSG = "Usage is: java BattleShipDriver <port> <board_size>);

    
    /**
     * The main entry point into the server side implementation of the 
     * application. Takes two command line arguments. The first being the port
     * for the server and the second the size of the board as a single integer.
     *
     * @param args The command line arguments of which 2 must be provided. The
     *             first argument is the port number for the server, the second 
     *             argument is the size of the board (default 10x10). The user
     *             only need provide one integer (i.e 10 for a board of size 
     *             10x10) since our application assumes square boards.
     */
    public static void main(String[] args) {

        int port;
        int size;
        // Validate correct number of arguments.
        if(args.length != NUM_ARGS) {
            System.out.println(USAGE_MSG);
            System.exit(FAILURE);
        }
        try{
            port = Integer.parseInt(args[0]);
            size = Integer.parseInt(args[1]);
        } catch(NumberFormatException nfe) {
            System.err.println("Invalid argument type. Arguments must be of integers.");
            System.exit(FAILURE);
        }
        
        // Creates a BattleServer and starts it listening for connections
        // TODO Figure out how the size is used in this driver.
        BattleServer bs = new BattleServer(port);
        bs.listen();
        
        // If we made it here, then we can successfully exit
        System.exit(SUCCESS);
    } // end main method

} // end BattleShipDriver class
