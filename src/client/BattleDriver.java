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
        String hostname;
        int port;
        String username;
        if(args.length < 3) {
            System.out.println("Usage is: java BattleDriver <hostname> <port>" +
                    " <nickname>");
            System.exit(FAILURE);
        }
        hostname = args[0];
        port = Integer.parseInt(args[1]);
        username = args[2];
        try {
            BattleClient client = new BattleClient(hostname,port,username);
            //TODO How to handle the reading information from the keyboard
            // and sending them to the client.
            ConnectionAgent agent = client.getAgent();
            while(agent.isConnected()){
                Scanner scan = agent.getIn();
                //TODO using the scanner in connection agent, to read the messages that
                // are received by the keyboard.
                String command = scan.nextLine();
                agent.sendMessage(command);
            }

        } catch (IOException e) {
            System.out.println(e.toString());
        }
        //TODO Finish starting a client

    } // end main method

} // end BattleDriver class
