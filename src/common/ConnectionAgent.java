package common;

import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
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

    /** Reads Input */
    private Scanner in;

    /** The stream to print output to */
    private PrintStream out;

    /** The thread to start this thread running */
    private Thread thread;

    /**
     * This is a constructor for a <code>ConnectionAgent</code>.
     *
     * @param socket Socket to connect to a separate host through.
     */
    public ConnectionAgent(Socket socket) {
        try {
            this.socket = socket;
            this.out = new PrintStream(socket.getOutputStream());
            this.in = new Scanner(socket.getInputStream());
            this.thread =  null;
        } catch(IOException ioe){
            System.out.println(ioe.toString());
        }
    } // end ConnectionAgent constructor w/ socket

    /**
     * Sends a message as a String between hosts.
     * @param message The message to send between hosts.
     */
    public void sendMessage(String message) {
        this.out.println(message);
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

    public Socket getSocket(){
        return this.socket;
    }
    /**
     * Closes the connection between two hosts.
     */
    private void close() {
        try {
            thread.interrupt();
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
            this.thread = Thread.currentThread();
            InputStreamReader inputStream =
                    new InputStreamReader(socket.getInputStream());
            this.in = new Scanner(inputStream);
            String message = in.nextLine();
            while(!this.thread.isInterrupted() && message != null) {
                if(!thread.isAlive()) {
                    return;
                } else if(!message.equals("")) {
                    notifyReceipt(message);
                }
                out.flush();
                message = in.nextLine();
            }
            this.close();
        } catch (IOException ioe) {
            System.err.println("IOException in the thread.");
            System.exit(1);
            this.sendMessage("/quit");
            this.close();
        } catch(NoSuchElementException nsee) {
            this.sendMessage("/quit");
            this.close();
            //System.out.println(this.isConnected());
            // Do nothing. Fixing error in server.
        } // end try-catch
    } // end run method

    /**
     * This method gets the connection agents PrintStream
     *
     * @return The PrintStream.
     */
    public PrintStream getOut(){
        return this.out;
    }

} // end ConnectionAgent class
