package server;

/**
 * The driver of the server side of our application. Validates command line 
 * input and creates a BattleServer that listens for remote host requests.
 *
 * @author Evert Ball
 * @author Carlee Yancey
 * @version 1.2.1 (08 December 2019)
 */
public class BattleShipDriver {

    /** A constant value for successful exit */
    private static final int SUCCESS = 0;

    /** A constant value for failed exit */
    private static final int FAILURE = 1;
    
    /** The valid number of command line args */
    private static final int NUM_ARGS = 2;
    
    private static final int DEFAULT_SIZE = 10;
    
    /** Usage message to print when program is used incorrectly */
    private static final String USAGE_MSG = ("Usage is: java " +
            "BattleShipDriver <port> <board_size>)");

    /** Constant for invalid type message used to notify user of invalid args */
    private static final String INVALID_TYPE_MSG = "Invalid argument type. " +
            "Arguments must be of type integer.";

    /** Constant for no port given message */
    private static final String PORT_MSG = "The port was not initialized...";

    
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
        int port = -1;
        int size = -1;
        // Validate correct number of arguments.
        if( 1  > args.length || args.length > 2  ) {
            System.out.println(USAGE_MSG);
            System.exit(FAILURE);
        }
        try{
            port = Integer.parseInt(args[0]);
            if(args.length == NUM_ARGS) {
                size = Integer.parseInt(args[1]);
            }else{
                size = DEFAULT_SIZE;
            }
        } catch(NumberFormatException nfe) {
            System.err.println(INVALID_TYPE_MSG);
            System.exit(FAILURE);
        }
        // Creates a BattleServer and starts it listening for connections
        if(port == -1 ) {
            System.out.println(PORT_MSG);
            System.exit(FAILURE);
        }
        BattleServer bs = new BattleServer(port);
        bs.setSize(size);
        bs.listen();
        // If we made it here, then we can successfully exit
        System.exit(SUCCESS);
    } // end main method
} // end BattleShipDriver class
