package client;

import common.ConnectionAgent;
import java.io.IOException;
import java.util.Scanner;

/**
 * Contains the main() method for the client. Responsible for parsing command 
 * line options, instantiating a BattleClient, reading messages from keyboards, 
 * and sending them to the client. The command line arguments are: hostname,
 * port number, and user nickname. All command line arguments are required.
 *
 * @author Evert Ball
 * @author Carlee Yancey
 * @version 1.2.0 (08 December 2019)
 */
public class BattleDriver {
    
    /** A named constant for a failed exit */
    private static final int FAILURE = 1;

    /** A named constant for a successful exit */
    private static final int SUCCESS = 1;

    /** Constant for a usage message */
    private static final String USAGE = "Usage is: java BattleDriver " +
            "<hostname> <port> <nickname>";

    private static final String QUIT_COMMAND = "/quit";

    /**
     * The main entry point into our program. Reads input from the user and
     * sends the required information to the server.
     *
     * @param args The 3 command line arguments required for the program to
     *             start. The required arguments, in order, are as follows:
     *             hostname, port number, and user nickname.
     */
    public static void main(String[] args) {
        String hostname;
        int port;
        String username;

        if(args.length < 3) {
            System.out.println(USAGE);
            System.exit(FAILURE);
        }
        hostname = args[0];
        port = Integer.parseInt(args[1]);
        username = args[2];
        try {
            BattleClient client = new BattleClient(hostname,port,username);
            ConnectionAgent agent = client.getAgent();
            while(agent.isConnected()) {
                Scanner action = new Scanner(System.in);
                if(action.hasNextLine()){
                    String toDo = action.nextLine();
                    client.send(toDo);
                    if(toDo.trim().equals(QUIT_COMMAND)) {
                        System.exit(SUCCESS);
                    } // end inner if
                } // end outter if
            } // end while loop
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        System.exit(SUCCESS);
    } // end main method

} // end BattleDriver class
