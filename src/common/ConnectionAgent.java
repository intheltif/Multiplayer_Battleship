/**
 * TODO Finish this documentation and do documentation for each 
 *      method/constructor
 *
 * A class that is responsible for sending messages to and receiving messages
 * from remote hosts. It can play the role of the &quot;subject&quot; in an
 * instance of the observer pattern. This class is also threadable.
 *
 * @author Evert Ball and Carlee Yancey
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
        this.socket = socket;
        this.out = System.out
        this.in = new Scanner(out);
        this.thread = new Thread();
    } // end ConnectionAgent constructor w/ socket

    public void sendMessage(String message) {

        //TODO Finish sendMessage method

    } // end sendMessage method

    public boolean isConnected() {
        
        //TODO Finish isConnected method

    } // end isConnected method

    public void close() {
        
        //TODO Finish close method

    } // end close method

    public void run() {
        
        //TODO Finish run method

    } // end run method

} // end ConnectionAgent class
