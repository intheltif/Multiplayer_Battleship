package common;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * TODO Finish this documentation and do documentation for each 
 *      method/constructor
 *
 * A class that is responsible for sending messages to and receiving messages
 * from remote hosts. It can play the role of the &quot;subject&quot; in an
 * instance of the observer pattern. This class is also threadable.
 *
 * @author Evert Ball
 * @author Carlee Yancey
 * @version 18 November 2019
 */
public class ConnectionAgent extends MessageSource implements Runnable {

    private Socket socket;

    private Scanner in;

    private PrintStream out;

    private Thread thread;

    public ConnectionAgent(Socket socket) {
        //TODO Not certain this is the correct implementation. 
        //     Going with it for now.
        try {
            this.socket = socket;
            this.out = new PrintStream(socket.getOutputStream());
            this.in = new Scanner(socket.getInputStream());
            this.thread = new Thread(this);
            this.thread.start();
        } catch(IOException ioe){

        }
    } // end ConnectionAgent constructor w/ socket

    public void sendMessage(String message) {

        out.println(message);

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

    public void run() {
        try {
            InputStreamReader inputStream = new InputStreamReader(socket.getInputStream());
            BufferedReader buffReader = new BufferedReader(inputStream);
            // while connected
            while(isConnected()) {
                String message = buffReader.readLine();
                sendMessage(message);
            }

        } catch (IOException ioe) {
            //TODO Something
        }

    } // end run method

} // end ConnectionAgent class
