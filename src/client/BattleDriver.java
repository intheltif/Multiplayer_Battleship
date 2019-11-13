/**
 * Contains the main() method for the client. Responsible for parsing command 
 * line options, instantiating a BattleClient, reading messages from keyboards, 
 * and sending them to the client. The command line arguments are: hostname,
 * port number, and user nickname. All command line arguments are required.
 *
 * @author Evert Ball
 * @author Carlee Yancey
 * @version 18 November 2019
 */
public class BattleDriver {
    
    /** A named constant for a failed exit */
    public static final int FAILURE = 1;
    
    /**
     * The main entry point into our program. Reads input from the user and 
     * sends the required information to the server.
     *
     * @param args The 3 command line arguments required for the program to 
     *             start. The required arguments, in order, are as follows:
     *             hostname, port number, and user nickname.
     */
    public static void main(String[] args) {
        if(args.length < 3) {
            System.out.println("Usage is: java BattleDriver <hostname> <port> <nickname>");
            System.exit(FAILURE);
        } // end usage check

        //TODO Finish starting a client

    } // end main method

} // end BattleDriver class
