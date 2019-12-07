package client;

import common.ConnectionAgent;
import common.MessageListener;
import common.MessageSource;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Implements the client-side logic of this client-server application. This 
 * class is responsible for creating a <code>ConnectionAgent</code>, reading 
 * input from the user, and sending that input to the server via the
 * <code>ConnectionAgent</code>. This class observes objects that are 
 * <code>MessageSource</code>s as well as plays the role of a subject in an 
 * instance of the observer pattern.
 *
 * @author Evert Ball
 * @author Carlee Yancey
 * @version 1.2.0 (08 December 2019)
 */
public class BattleClient extends MessageSource implements MessageListener {

    /** The domain of the host to connect to */
    private InetAddress hostname;

    /** The port number to communicate with the host on */
    private int port;

    /** Username of the player */
    private String username;

    /** The <code>ConnectionAgent</code> that allows host communication */
    private ConnectionAgent agent;

    /** The MessageListener that allows the server to communicate with us */
    private PrintStreamMessageListener print;

    /**
     * The constructor of the BattleClient.
     *
     * @param hostname Name of the Host.
     * @param port The port number.
     * @param username The username of the BattleClient.
     * @throws IOException If a network error occurs.
     */
    public BattleClient(String hostname, int port, String username)
            throws IOException {
        this.port = port;
        this.username = username;
        this.hostname =  InetAddress.getByName(hostname);

        // Allows communication with a remote host
        Socket socket = new Socket(this.getHostname(), this.port);
        this.agent = new ConnectionAgent(socket);

        // Allows message passing concurrently
        Thread thread = new Thread(this.agent);
        thread.start();

        // Allows this user to receive messages when a new one is available
        agent.addMessageListener(this);

        this.print = new PrintStreamMessageListener(agent.getOut());
        this.connect();
    } // end constructor

    /**
     * Gets the hostname that this client is connected to.
     *
     * @return The name of the host.
     */
    private InetAddress getHostname(){
        return this.hostname;
    }

    /**
     * Gets this player's username.
     *
     * @return The username.
     */
    private String getUsername(){
        return this.username;
    }

    /**
     * Joins this player to a game of BattleShip while the game is not in
     * progress.
     */
    private void connect(){
        String join = "/join ";
        join = join.concat(this.getUsername());
        this.send(join);
    } //end connect

    /**
     * Gets the connection agent of the BattleClient.
     *
     * @return The connection agent.
     */
    public ConnectionAgent getAgent(){
        return this.agent;
    }
    /**
     * Used to notify observers that the subject has received a message.
     *
     * @param message The message received by the subject
     * @param source  The source from which this message originated (if needed).
     */
    @Override
    public void messageReceived(String message, MessageSource source) {
        //System.out.println(message);
        print.messageReceived(message,source);
    } //end messageReceived

    /**
     * Used to notify observers that the subject will not receive new messages;
     * observers can deregister themselves.
     *
     * @param source The <code>MessageSource</code> that does not expect more
     *               messages.
     */
    @Override
    public void sourceClosed(MessageSource source) {
        source.removeMessageListener(this);
    } //end sourceClosed

    /**
     * Sends a message to a remote host using this clients
     * <code>ConnectionAgent</code>
     * @param message The message that is being sent.
     */
    public void send(String message){
        agent.sendMessage(message);
    } //end send

} // end BattleClient class
