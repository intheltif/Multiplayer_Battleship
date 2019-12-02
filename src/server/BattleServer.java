package server;

import common.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Implements the server-side logic of the Battleship application. This class
 * is responsible for accepting incoming connections, creating 
 * <code>ConnectionAgents</code>, and passing the <code>ConnectionAgents</code>
 * off to threads for processing. This class acts as an observer in the 
 * Observer design pattern implementation.
 *
 * @author Evert Ball and Carlee Yancey
 * @version 18 November 2019
 *
 * TODO Finish documentation for fields and methods.
 * TODO Update the class documentation as needed.
 * TODO Finish creating the methods.
 */
public class BattleServer implements MessageListener {

    private ServerSocket serverSocket;

    private int current;

    private Game game;

    private ArrayList<ConnectionAgent> conAgentCollection;

    public BattleServer(int port) {

        try {
            this.serverSocket = new ServerSocket(port);
            this.conAgentCollection = new ArrayList<>();
            this.game = null;
            this.current = -1;
        } catch (IOException ioe) {
            System.err.println("IO Error: " + ioe.getMessage());
            //TODO Maybe handle a bit better
        }

    } // end BattleServer constructor

    /**
     * Listens for connections from client devices that want to play a game.
     */
    public void listen() {
        while (!this.serverSocket.isClosed()) {
            try {
                ConnectionAgent agent = new ConnectionAgent(this.serverSocket.accept());
                conAgentCollection.add(agent);




                // TODO What to do from here?

            } catch (IOException ioe) {
                System.out.println("IO Exception Occurred.");
            } // end try-catch
    } // end while loop

    } // end listen method

    public void broadcast(String message) {

        // Send message to all CAs currently connected.
        for(ConnectionAgent agent : conAgentCollection) {
            if(agent.isConnected()) {
                agent.sendMessage(message);
            }
        }
        
    } // end broadcast method

    public void messageReceived(String message, MessageSource source) {

        //TODO finish messageReceived method

    } // end messageReceived method

    public void sourceClosed(MessageSource source) {

        //TODO finish sourceClosed method

    } // end sourceClosed method

} // end BattleServer class
