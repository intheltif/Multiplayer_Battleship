package server;

import common.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Implements the server-side logic of the Battleship application. This class
 * is responsible for accepting incoming connections, creating 
 * <code>ConnectionAgents</code>, and passing the <code>ConnectionAgents</code>
 * off to threads for processing. This class acts as an observer in the 
 * Observer design pattern implementation.
 *
 * @author Evert Ball
 * @author Carlee Yancey
 * @version 1.2.0 (08 December 2019)
 *
 */
public class BattleServer implements MessageListener {

    /** Constant representation of a successful exit */
    private static final int SUCCESS = 0;

    /** Allows us to reset the game */
    private static final int FIRST_INDEX = 0;

    /** A Constant representation of one */
    private static final int ONE = 1;

    /** A Constant representation of two */
    private static final int TWO = 2;

    /** A Constant representation of zero */
    private static final int ZERO = 0;

    /** A Constant representation of three */
    private static final int THREE = 3;

    /** A Constant representation of negative one */
    private static final int NEG_ONE = -1;

    /** The welcoming socket that clients connect through */
    private ServerSocket serverSocket;

    /** The index to the current player */
    private int current;

    /** The Battleship game */
    private Game game;

    /** If the game has been started */
    private boolean started;

    /** An ArrayList of connection agents that have joined the game */
    private ArrayList<ConnectionAgent> conAgentCollection;

    /** Maps usernames to ConnectionAgents */
    private HashMap<String, ConnectionAgent> userToConnectionAgentMap;

    /** Maps ConnectionAgents to usernames */
    private HashMap<ConnectionAgent, String> connectionAgentToUserMap;

    /** The size of the Grid */
    private int size;

    /**
     * This is the constructor for the BattleServer, it ask for a port
     * number for its Server Socket.
     *
     * @param port The given port number.
     */
    public BattleServer(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            this.conAgentCollection = new ArrayList<>();
            this.connectionAgentToUserMap = new HashMap<>();
            this.userToConnectionAgentMap = new HashMap<>();
            this.game = null;
            this.current = 0;
            this.started = false;
        } catch (IOException ioe) {
            System.err.println("IO Error: " + ioe.getMessage());
        }
    } // end BattleServer constructor

    /**
     * This method sets the size to be given to game.
     * @param size The size given.
     */
    public void setSize(int size){
        this.size = size;
    } // end setSize method

    /**
     * Listens for connections from client devices that want to play a game.
     */
    public void listen() {
        this.game = new Game(this.size);
        while (!this.serverSocket.isClosed()) {
            try {
                /*
                Checks if game has started twice to fix issue where 1 more
                player can join after play has started.
                */
                if(!started) {
                    ConnectionAgent agent =
                            new ConnectionAgent(this.serverSocket.accept());
                    if(!started) {
                        if (agent.isConnected()) {
                            agent.addMessageListener(this);
                            Thread thread = new Thread(agent);
                            thread.start();
                            conAgentCollection.add(agent);
                        }
                    }else{
                        agent.sendMessage("Game In Progress, Unable to join.");
                        // Forces client to quit if joining a game in progress
                        agent.sendMessage("quit");
                    }
                }
            } catch (IOException ioe) {
                System.out.println("IO Exception Occurred.");
            } // end try-catch
        } // end while loop
    } // end listen method

    /**
     * Sends a message to all currently connected <code>Connection agent</code>.
     *
     * @param message The message being sent.
     */
    private void broadcast(String message) {
        // Send message to all CAs currently connected.
        for(ConnectionAgent agent : conAgentCollection) {
            if(agent.isConnected()) {
                agent.sendMessage(message);
            }
        }
    } // end broadcast method

    /**
     * Used to notify observers that the subject has received a message.
     *
     * @param message The message received by the subject
     * @param source  The source from which this message originated (if needed).
     */
    @Override
    public void messageReceived(String message, MessageSource source) {
        ConnectionAgent ca;
        String user;
        if(!started) {
            ca = this.conAgentCollection.get(this.conAgentCollection.size()-1);
            user = this.connectionAgentToUserMap.get(ca);
            if(user == null) {
                parseCommands(message,ca);
            } else {
                user = this.connectionAgentToUserMap.get(source);
                ca = this.userToConnectionAgentMap.get(user);
                parseCommands(message, ca);
            }
        } else {
            user = this.connectionAgentToUserMap.get(source);
            ca = this.userToConnectionAgentMap.get(user);
            parseCommands(message, ca);
        }
    } // end messageReceived method

    /**
     * Used to notify observers that the subject will not receive new messages;
     * observers can deregister themselves.
     *
     * @param source The <code>MessageSource</code> that does not expect more
     *               messages.
     */
    @Override
    public void sourceClosed(MessageSource source) {
        // Remove the subject from all collections/maps associated with it
        String user = connectionAgentToUserMap.get(source);
        ConnectionAgent ca = userToConnectionAgentMap.get(user);
        conAgentCollection.remove(ca);
        userToConnectionAgentMap.remove(user);
        connectionAgentToUserMap.remove(ca);
    } // end sourceClosed method

    /**
     * Parses the join command sent by a client upon initial connection.
     * Joins the client to a a game that has not been started and maps the user
     * to the correct <code>ConnectionAgent</code>.
     *
     * @param command The join command sent by the client
     * @param agent The connection agent associated with the connecting client.
     */
    private void parseJoin(String command, ConnectionAgent agent){
        String user;
        String[] com = command.trim().split("\\s+");
        boolean hasJoined = this.connectionAgentToUserMap.containsKey(agent);
        boolean isNameTaken = this.userToConnectionAgentMap.containsKey(com[ONE]);
        // Do not allow another player to join if a game is in progress
        if(!started) {
            if (com.length == TWO) {
                if(!hasJoined) {
                    if(!isNameTaken){
                        this.connectionAgentToUserMap.put(agent, com[ONE]);
                        this.userToConnectionAgentMap.put(com[ONE], agent);
                        user = this.connectionAgentToUserMap.get(agent);
                        this.game.join(com[ONE], size);
                        System.out.println("SERVER: " + user + " JOINED THE GAME.");
                        broadcast("!!! " + user + " has joined.");
                    } else {
                        this.conAgentCollection.remove(agent);
                        agent.sendMessage("Name already in use. Try again.");
                        // Cause the client's system to quit so they can rejoin
                        agent.sendMessage("disconnect");
                    } // end isNameTaken if statement
                } else {
                    agent.sendMessage("Cannot join a game you're already in.");
                } // end hasJoined if statement
            }
        } else {
            agent.sendMessage("Cannot join a game that is already in progress.");
        } // end if statement that checks if game is in progress.
    } // end parseJoin method

    /**
     * Parses commands sent by sent by clients through their
     * <code>ConnectionAgents</code>.
     *
     * @param command The command sent by the client.
     * @param agent The <code>ConnectionAgent</code> that passed the message.
     */
    private void parseCommands(String command, ConnectionAgent agent){
        String[] com = command.trim().split("\\s+");
        if(com.length > ZERO){
            switch (com[ZERO]){
                case"/join":
                    parseJoin(command,agent);
                    break;
                case "/play":
                    parsePlay(agent);
                    break;
                case "/attack":
                    if(started){
                        parseAttack(command,agent);
                    }else{
                        agent.sendMessage("Play not in progress.");
                    }
                    break;
                case"/show" :
                    if(started){
                        parseShow(command,agent);
                    }else{
                        agent.sendMessage("Play not in progress.");
                    }
                    break;
                case "/quit":
                    parseQuit(command, agent);
                    break;
                case "/help":
                    parseHelp(agent);
                    break;
                default:
                    agent.sendMessage("Unable to parse command. Use " +
                            "\"/help\" to see a list of commands.");
            } // end switch statement
        } // end if statement
    } // end parseCommands method

    /**
     * Parses the "play" command for valid input. Confirms that 2+ players are
     * currently connected and invalidates the input if the game is already
     * in progress.
     *
     * @param agent The current connection agent.
     */
    private void parsePlay(ConnectionAgent agent){
        String user;
        ArrayList<ConnectionAgent> closed = new ArrayList<>();
        if(this.conAgentCollection.size() >= TWO && !started){
            for (ConnectionAgent agentToCheck : this.conAgentCollection) {
                if(agentToCheck.getSocket().isClosed()){
                    closed.add(agentToCheck);
                }
            }
            removeConnectionAgents(closed);
            user = this.connectionAgentToUserMap.get(agent);
            String player = game.getPlayers().get(ZERO);
            started = true;
            broadcast("The game begins...");
            broadcast(player + " it is your turn.");
            System.out.println("PARSE COMMANDS: " + user +
                    " STARTED THE GAME.");

        }else if (!started){
            agent.sendMessage("Not enough players to play the " +
                    "game.");
        }else{
            agent.sendMessage("Game already in progress.");
        }
    } // end parsePlay method

    /**
     * Parses the attack command sent by a client through their
     * <code>ConnectionAgent</code>. On a failed command, logs to the server
     * and sends a failure message to the client.
     *
     * @param command The given command.
     * @param agent The current connection agent.
     */
    private void parseAttack(String command, ConnectionAgent agent){
        String[] com = command.trim().split("\\s+");
        if(this.current > (conAgentCollection.size() - ONE)) {
            this.current = ZERO;
        }
        String turn = this.game.turn(this.current);
        int col = NEG_ONE;
        int row = NEG_ONE;
        int attAgr = 4;
        String curr = this.connectionAgentToUserMap.get(agent);
        boolean attacked;
        if(!curr.equals(turn)){
            agent.sendMessage("Move failed. It is " + turn + "'s turn.");
            System.out.println("MOVED FAILED IN USER: " + curr);
        }else {
            try {
                col = Integer.parseInt(com[TWO]);
                row = Integer.parseInt(com[THREE]);
            } catch (NumberFormatException nfe) {
                System.out.println("Attack coordinates must be integers.");
                agent.sendMessage("Attack coordinates must be integers.");
            } catch (ArrayIndexOutOfBoundsException aioobe) {
                System.out.println("Usage: /attack <player> <col> <row>");
                agent.sendMessage("Attack coordinates must be on the board.");
            }
            if (col > (this.size - ONE) || row > (this.size - ONE) ||
                    col < ZERO || row < ZERO) {
                System.out.println("Usage: /attack <player> <col> <row>");
            }
            if (com.length == attAgr) {
                if (!turn.equals(com[ONE])) {
                    if(this.game.getPlayers().contains(com[ONE])) {
                        attacked = isValidAttack(game, com);
                        if (!attacked) {
                            System.out.println("MOVED FAILED IN USER: " + curr);
                            agent.sendMessage("Move Failed, player " +
                                    "turn: " + turn);
                        } else {
                            // Broadcasts to all clients and logs to server
                            System.out.println("Shots fired at " +
                                    com[ONE] + " by " + curr);
                            broadcast("Shots fired at " +
                                    com[ONE] + " by " + curr);
                            this.current++;
                            String over = game.isGameOver();
                            if (!over.equals("")) {
                                String[] overArray = over.trim().split("\\s+");
                                if(overArray[ZERO].equals("remove")) {
                                    String user = overArray[ONE];
                                    broadcast("*** All of " + user +
                                            "'s battleships have been sunk. ***");
                                    ConnectionAgent ca = this.userToConnectionAgentMap.get(user);
                                    this.conAgentCollection.remove(ca);
                                    this.connectionAgentToUserMap.remove(ca);
                                    this.userToConnectionAgentMap.remove(user);
                                    if(this.current > (conAgentCollection.size() - 1)) {
                                        this.current = 0;
                                    }
                                    ca.sendMessage("disconnect");
                                } else {
                                    this.current = FIRST_INDEX;
                                    System.out.println("GAME OVER: " + over +
                                            " WINS!");
                                    broadcast("GAME OVER: " + over + " WINS!");
                                    started = false;
                                    // If game is over, force all players to quit.
                                    broadcast("disconnect");
                                    // Quit accepting players since game was won.
                                    System.exit(SUCCESS);
                                }
                            }
                        }
                    }
                }
            } else {
                agent.sendMessage("Usage: /attack <player> <col> <row>");
                System.out.println("Invalid command: " + command + "from " +
                        curr);
            }
            if(started) {
                if(this.current > (this.conAgentCollection.size() - ONE)) {
                    this.current = ZERO;
                }
                turn = game.turn(this.current);
                System.out.println(turn + " it is your turn");
                broadcast(turn + " it is your turn");
            }
        }
    } // end parseAttack method

    /**
     * Sends the board of the requested player to the client that requested
     * the specified board. If the requested player is the client it shows the
     * ships on the board, otherwise it only shows hits and misses.
     *
     * @param command The given command.
     * @param agent The current connection agent.
     */
    private void parseShow(String command, ConnectionAgent agent){
        String[] com = command.trim().split("\\s+");
        String board;
        String curr = this.connectionAgentToUserMap.get(agent);
        System.out.println("PARSE COMMANDS: " + command +
                " USER: " + curr);
        if (com.length == TWO) {
            board = game.show(com[ONE], curr);
            agent.sendMessage(board);
        }
    }

    /**
     * Parses the command for quitting from a game of Battleship. Determines if
     * this player quitting causes the number of players to be 1 and if so,
     * displays that the remaining player is the winner.
     *
     * @param command The command that was issued from the client.
     * @param agent The <code>ConnectionAgent</code> that sent the command.
     */
    private void parseQuit(String command, ConnectionAgent agent) {
        if(this.current == (conAgentCollection.size() - 1)) {
            this.current = 0;
        }
        String user = this.connectionAgentToUserMap.get(agent);
        System.out.println("PARSE COMMANDS: " + command +
                " USER: " + user);
        broadcast("!!! " + user + " surrendered.");
        sourceClosed(agent);
        game.leave(user);
        String over = game.isGameOver();
        if (!over.equals("")) {
            this.current = FIRST_INDEX;
            System.out.println("GAME OVER: " + over +
                    " WINS!");
            broadcast("GAME OVER: " + over + " WINS!");
            started = false;
            // If the game is over, force all clients to quit.
            broadcast("disconnect");
            System.exit(SUCCESS);
        } else {
            String turn = game.turn(this.current);
            System.out.println(turn + " it is your turn");
            broadcast(turn + " it is your turn");
        }
    } // end parseQuit method

    /**
     * Sends a usage message that displays available commands for a game of
     * Battleship to the player that requested it.
     *
     * @param agent The <code>ConnectionAgent</code> of the client that sent
     *              the command.
     */
    private void parseHelp(ConnectionAgent agent) {
        StringBuilder builtStr = new StringBuilder();
        builtStr.append("COMMAND                           DESCRIPTION\n");
        builtStr.append("-------                           -----------\n");
        builtStr.append("/show <username>                  - " +
                "Shows the username for the specified player.\n");
        builtStr.append("/attack <username> <column> <row> - " +
                "Attack another user's board at the specified " +
                "column and row.\n");
        builtStr.append("/play                             - " +
                "Initiates a game of Battleship once 2 or more " +
                "players have joined.\n");
        builtStr.append("/quit                             - " +
                "Quits from a game of Battleship.\n");
        String str = new String(builtStr);
        agent.sendMessage(str);
    } // end parseHelp method

    /**
     * Determines whether an attack was valid based on the current game in
     * progress and the command that the user entered.
     *
     * @param game The current game.
     * @param command The attack command that was sent by a client.
     * @return True if the attack was valid, false otherwise.
     */
    private boolean isValidAttack(Game game, String[] command){
        int row = Integer.parseInt(command[THREE]);
        int column = Integer.parseInt(command[TWO]);
        String nickname  = command[ONE];
        return game.hit(nickname, row, column);
    } // end isValidAttack method

    /**
     * Removes <code>ConnectionAgents</code> from all of the collections as if
     * they had sent the quit command.
     *
     * @param closed An <code>ArrayList</code> of <code>ConnectionAgents</code>
     *               that have left the game and need to be removed.
     */
    private void removeConnectionAgents(ArrayList<ConnectionAgent> closed){
        for (ConnectionAgent agent : closed) {
            parseQuit("/quit", agent);
        }
    } // end removeConnectionAgents method

} // end BattleServer class
