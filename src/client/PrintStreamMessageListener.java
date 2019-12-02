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

    public void messageReceived(String message, MessageSource source) {

        //TODO Finish method

    } // end messageReceived method

    public void sourceClosed(MessageSource source) {

        //TODO Finish method

    } // end sourceClosed method

} // end PrintStreamMessageListener class
