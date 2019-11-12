import common.ConnectionAgent;

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
public class BattleClient implements MessageListener extends MessageSource {

    public BattleClient() {

    } // end constructor

} // end BattleClient class
