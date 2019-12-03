package server;

import common.*;

import java.io.*;
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

    private boolean started;
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
            this.started = false;
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
                this.game = new Game(5);
                if(!started) {
                    ConnectionAgent agent =
                            new ConnectionAgent(this.serverSocket.accept());
                    conAgentCollection.add(agent);
                    if (agent.isConnected()) {
                        //TODO need to receive a command from the client
                        // connection agent that is the join command then
                        addML(agent);
//                        String command = buff.
//                        String user = parseJoin(command, agent);
                        //String user = " ";
                        //broadcast("!!! " + user + "has joined");
                    }
                    //TODO gets a command that is sent by the Client connection agent.
                    // which makes a connection agent on the sever side, parses the message play the
                    // that it receives if it is the play command and the the connection agent array
                    // list is greater than or equal to 2 it will allow the game to start.f
                }

                // TODO What to do from here?

            } catch (IOException ioe) {
                System.out.println("IO Exception Occurred.");
            } // end try-catch
        } // end while loop

    } // end listen method

    public void addML(ConnectionAgent agent){
        agent.addMessageListener(this);
    }

    public void broadcast(String message) {
        System.out.println("I started broadcasting...");

        // Send message to all CAs currently connected.
        for(ConnectionAgent agent : conAgentCollection) {
            if(agent.isConnected()) {
                System.out.println("Iffy Lube");
                agent.sendMessage(message);
            }
        }
        System.out.println("I stopped broadcasting...");

    } // end broadcast method

    /**
     * Used to notify observers that the subject has received a message.
     *
     * @param message The message received by the subject
     * @param source  The source from which this message originated (if needed).
     */
    public void messageReceived(String message, MessageSource source) {
        ConnectionAgent ca2;
        ConnectionAgent ca;
        if(!started) {
            ca2 = this.conAgentCollection.get(this.conAgentCollection.size()-1);
            parseJoin(message, ca2);
        }else{
            ca = this.userToConnectionAgentMap.get(source);

        }

    } // end messageReceived method

    /**
     * Used to notify observers that the subject will not receive new messages; observers can
     * deregister themselves.
     *
     * @param source The <code>MessageSource</code> that does not expect more messages.
     */
    public void sourceClosed(MessageSource source) {

        // Remove the subject from all collections/maps associated with it
        conAgentCollection.remove(source);
        userToConnectionAgentMap.remove(source);
        connectionAgentToUserMap.remove(source);

    } // end sourceClosed method

    public String parseJoin(String command, ConnectionAgent agent){
        String user = "";
        String[] com = command.trim().split("\\s+");
        if(com.length == 2) {
            switch (com[0]) {
                case "/join":
                    this.connectionAgentToUserMap.put(agent, com[1]);
                    this.userToConnectionAgentMap.put(com[1], agent);
                    System.out.println(this.connectionAgentToUserMap.get(agent) + " Joined the game");
                    this.game.join(com[1],5); //Handle the grid size
                    broadcast("!!! " + user + " has joined");
                    user = com[1];
                    break;
            }
        }
        return user;
    }

    public void parseCommands(String command, ConnectionAgent agent){

        String[] com = command.trim().split("\\s+");
        if(com.length > 0){
            switch (com[0]){
                case"/join":
                    parseJoin(command,agent);
                    break;
                case "/play":
                    if(this.conAgentCollection.size() >=2 && !started){
                        started = true;
                        broadcast("The game begins");
                        String user = this.connectionAgentToUserMap.get(agent);
                        System.out.println(user + " Started the game");
                    }
                    break;
                case "/attack":
                    if(started){

                    }
                    break;
                case"/show" :
                    if(started){

                    }
                    break;
                case "/quit":

                    break;

            }
        }
    }
} // end BattleServer class
