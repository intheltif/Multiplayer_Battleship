package server;

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

    public BattleServer(int port) {
    
        //TODO Finish constructor

    } // end BattleServer constructor

    public void listen() {

        //TODO finish listen method
        
    } // end listen method

    public void broadcast(String message) {

        //TODO finish broadcast method
        
    } // end broadcast method

    public void messageReceived(String message, MessageSource source) {

        //TODO finish messageReceived method

    } // end messageReceived method

    public void sourceClosed(MessageSource source) {

        //TODO finish sourceClosed method

    } // end sourceClosed method

} // end BattleServer class
