package common;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * A class that is responsible for sending messages to and receiving messages
 * from remote hosts. It can play the role of the &quot;subject&quot; in an
 * instance of the observer pattern. This class is also threadable.
 *
 * @author Evert Ball
 * @author Carlee Yancey
 * @version 18 November 2019
 */
public class ConnectionAgent extends MessageSource implements Runnable {

    /** Socket to connect to a separate host through */
    private Socket socket;

    /** ??? */
    private Scanner in;

    /** The stream to print output to */
    private PrintStream out;

    /** The thread to start this thread running */
    private Thread thread;

    public ConnectionAgent(Socket socket) {
        //TODO Not certain this is the correct implementation. 
        //     Going with it for now.
        try {
            this.socket = socket;
            this.out = new PrintStream(socket.getOutputStream());
            this.in = new Scanner(socket.getInputStream());
            this.thread =  Thread.currentThread();
        } catch(IOException ioe){

        }
    } // end ConnectionAgent constructor w/ socket
    public Socket getSocket(){
        return this.socket;
    }
    /**
     * Sends a message as a String between hosts.
     * @param message The message to send between hosts.
     */
    public void sendMessage(String message) {
        this.out.println(message);
        this.out.flush();
    } // end sendMessage method

    /**
     * Returns whether or not this <code>ConnectionAgent</code> is connected to
     * a host
     *
     * @return True if connected to a remote host, false otherwise
     */
    public boolean isConnected() {
        return this.socket.isConnected();

    } // end isConnected method

    /**
     * Closes the connection between two hosts.
     */
    public void close() {
        try {
            this.socket.close();
        } catch( IOException ioe) {
            System.out.println("Failure closing connection. IO Error.");
        }

    } // end close method

    /**
     * Run method that reads input while the socket is connected so that hosts
     * can communicate through the socket.
     */
    @Override
    public void run() {
        try {
            this.out =  new PrintStream(socket.getOutputStream());
            InputStreamReader inputStream = new InputStreamReader(socket.getInputStream());
            this.in = new Scanner(inputStream);
            while(isConnected()) {

                String message = in.next();
                notifyReceipt(message);
                out.flush();
            }
            this.close();
        } catch (IOException ioe) {
            System.err.println("IOException in the thread.");
            System.exit(1);
        } // end try-catch

    } // end run method

    /**
     * This gets the scanner that the connection agent uses.
     *
     * @return The in scanner.
     */
    public Scanner getIn(){
        return this.in;
    }

    /**
     * This method gets the connection agents PrintStream
     *
     * @return The PrintStream.
     */
    public PrintStream getOut(){
        return this.out;
    }

} // end ConnectionAgent class
