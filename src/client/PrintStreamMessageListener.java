package client;

import common.MessageListener;
import common.MessageSource;

import java.io.PrintStream;

/**
 * Responsible for writing messages to a <code>PrintStream</code>
 * (e.g., <code>System.out</code>). It implements the role of an observer in 
 * the observer pattern.
 *
 * @author Evert Ball
 * @author Carlee Yancey
 * @version 18 November 2019
 */
public class PrintStreamMessageListener implements MessageListener {
    
    /** The PrintStream to write messages to */
    private PrintStream out;
    
    /** 
     * Creates a PrintStreamMessageListener to listen for messages from the
     * server and send them to the appropriate players.
     * 
     * @param out The <code>PrintStream</code> object to send the messages to.
     */
    public PrintStreamMessageListener(PrintStream out) {
        this.out = out;

    } // end constructor

    /**
     * Used to notify observers that the subject has received a message.
     *
     * @param message The message received by the subject
     * @param source  The source from which this message originated (if needed).
     */
    public void messageReceived(String message, MessageSource source) {
        //TODO finish writing
       // System.out.println("Inside msgRcvd PSML");
        System.out.println(message);
        //System.out.println("Leaving msgRcvd PSML");
        //this.out.println(message);
        //this.out.flush();

    } // end messageReceived method

    /**
     * Used to notify observers that the subject will not receive new messages; observers can
     * deregister themselves.
     *
     * @param source The <code>MessageSource</code> that does not expect more messages.
     */
    public void sourceClosed(MessageSource source) {
        //TODO Finish method
        source.removeMessageListener(this);
    } // end sourceClosed method

} // end PrintStreamMessageListener class
