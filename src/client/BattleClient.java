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
 * @version 18 November 2019
 */
public class BattleClient extends MessageSource implements MessageListener  {

    /** The name of the host */
    private InetAddress hostname;

    /** Port number */
    private int port;

    /** Username of the Battle Client */
    private String username;

    /** The ConnectionAgent of the BattleClient */
    private ConnectionAgent agent;

    /** The print Stream listener */
    private PrintStreamMessageListener print;

    /**
     * The constructor of the BattleClient.
     *
     * @param hostname Name of the Host.
     * @param port The port number.
     * @param username The username of the BattleClient.
     * @throws IOException If a network error occurs.
     */
    public BattleClient(String hostname, int port, String username) throws IOException {
        this.port = port;
        this.username = username;
        this.hostname =  InetAddress.getByName(hostname);
        Socket socket = new Socket(this.getHostname(), this.port);
        this.agent = new ConnectionAgent(socket);
        Thread thread = new Thread(this.agent);
        thread.start();
        addMessageListener(this);
        this.print = new PrintStreamMessageListener(agent.getOut());
        this.connect();
    } // end constructor

    /**
     * This method gets the hostname of the BattleClient.
     *
     * @return The name of the host.
     */
    public InetAddress getHostname(){
        return this.hostname;
    }

    /**
     * This method gets the username of the BattleClient
     *
     * @return The username.
     */
    public String getUsername(){
        return this.username;
    }

    /**
     * This connects the battleClient using its connection agent to the
     * battleship game.
     */
    public void connect(){
        String join = "/join ";
        join = join.concat(this.getUsername());
        this.send(join);

    } //end connect

    /**
     * This method gets the connection agent of the BattleClient.
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
        //TODO Finish method, ?where it prints the message of this curtain client
        // or is this with the PrintStreamMessageListener?
        //agent.getOut().print(message);
        System.out.println("I received a message");
        //System.out.println(message);
        print.messageReceived(message,source);
        System.out.println("I printed that message!");
        //agent.getOut().flush();
       // System.out.println("Message received in client: " + message);
    } //end messageReceived

    /**
     * Used to notify observers that the subject will not receive new messages; observers can
     * deregister themselves.
     *
     * @param source The <code>MessageSource</code> that does not expect more messages.
     */
    @Override
    public void sourceClosed(MessageSource source) {
        //TODO finish writing
        source.removeMessageListener(this);
    } //end sourceClosed

    /**
     * This method sends the message using its connection agent.
     * @param message The message that is being sent.
     */
    public void send(String message){
        agent.sendMessage(message);

    } //end send

} // end BattleClient class
