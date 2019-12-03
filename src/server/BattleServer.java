package server;

import common.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

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

    private HashMap<String, ConnectionAgent> userToConnectionAgentMap;

    private HashMap<ConnectionAgent, String> connectionAgentToUserMap;

    public BattleServer(int port) {

        try {
            this.serverSocket = new ServerSocket(port);
            this.conAgentCollection = new ArrayList<>();
            this.connectionAgentToUserMap = new HashMap<>();
            this.userToConnectionAgentMap = new HashMap<>();
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
                ConnectionAgent agent =
                        new ConnectionAgent(this.serverSocket.accept());
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

    /**
     * Used to notify observers that the subject has received a message.
     *
     * @param message The message received by the subject
     * @param source  The source from which this message originated (if needed).
     */
    public void messageReceived(String message, MessageSource source) {

        ConnectionAgent ca = this.userToConnectionAgentMap.get(source);
        ca.sendMessage(message);

    } // end messageReceived method

    /**
     * Used to notify observers that the subject will not receive new messages; observers can
     * deregister themselves.
     *
     * @param source The <code>MessageSource</code> that does not expect more messages.
     */
    public void sourceClosed(MessageSource source) {

        source.removeMessageListener(this);

    } // end sourceClosed method

} // end BattleServer class
