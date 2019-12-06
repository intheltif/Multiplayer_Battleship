package server;

import common.*;

import java.io.*;
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
 * @version 18 November 2019
 *
 */
public class BattleServer implements MessageListener {

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
    private static final int NEGONE = -1;

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
            //TODO Maybe handle a bit better
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
                        agent.sendMessage("Game In Progress, Unable to join");
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
     * This method parses the command that starts with "/join" and joins a game
     * that has not been started. Also maps the user to the correct connection
     * agent
     * @param command The command that has been given.
     * @param agent The connection agent.
     */
    private void parseJoin(String command, ConnectionAgent agent){
        String user;
        String[] com = command.trim().split("\\s+");
        if(com.length == TWO) {
            this.connectionAgentToUserMap.put(agent, com[ONE]);
            this.userToConnectionAgentMap.put(com[ONE], agent);
            user = this.connectionAgentToUserMap.get(agent);
            this.game.join(com[ONE],size);
            System.out.println("SERVER: " + user + " JOINED THE GAME");
            broadcast("!!! " + user + " has joined");
        }
    } // end parseJoin method

    /**
     * This method parses a command from a curtain Connection Agent.
     *
     * @param command The command.
     * @param agent The connection agent.
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
                        agent.sendMessage("Play not in progress");
                    }
                    break;
                case"/show" :
                    if(started){
                        parseShow(command,agent);
                    }else{
                        agent.sendMessage("Play not in progress");
                    }
                    break;
                case "/quit":
                    parseQuit(command, agent);
                    break;
                case "/help":
                    parseHelp(agent);
                    break;
            }
        }
    } // end parseCommands method

    /**
     * This parses the command for a play command.
     *
     * @param agent The current connection agent.
     */
    private void parsePlay(ConnectionAgent agent){
        String user;
        ArrayList<ConnectionAgent> closed = new ArrayList<>();
        if(this.conAgentCollection.size() >= TWO && !started){
            for (ConnectionAgent check : this.conAgentCollection) {
                if(check.getSocket().isClosed()){
                    closed.add(check);
                }
            }
            removeCA(closed);
            user = this.connectionAgentToUserMap.get(agent);
            String player = game.getPlayers().get(ZERO);
            started = true;
            broadcast("The game begins");
            broadcast(player + " it is you turn");
            System.out.println("PARSE COMMANDS: " + user +
                    " STARTED THE GAME");

        }else if (!started){
            agent.sendMessage("Not enough players to play the " +
                    "game");
        }else{
            agent.sendMessage("Game has been Started");
        }
    } // end parsePlay method

    /**
     * This parses the command for a show command.
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
        int col = NEGONE;
        int row = NEGONE;
        int attAgr = 4;
        String curr = this.connectionAgentToUserMap.get(agent);
        boolean attacked;
        if(!curr.equals(turn)){
            broadcast("Move Failed, player turn: " + turn);
            System.out.println("MOVED FAILED IN USER: " + curr);
        }else {
            try {
                col = Integer.parseInt(com[TWO]);
                row = Integer.parseInt(com[THREE]);
            } catch (NumberFormatException nfe) {
                System.out.println("Attack coordinates must be integers.");
            } catch (ArrayIndexOutOfBoundsException aioobe) {
                System.out.println("Usage: /attack <player> <col> <row>");
            }
            if (col > (this.size - ONE) || row > (this.size - ONE) ||
                    col < ZERO || row < ZERO) {
                System.out.println("Usage: /attack <player> <col> <row>");
            }
            if (com.length == attAgr) {
                if (!turn.equals(com[ONE])) {
                    if(this.game.getPlayers().contains(com[ONE])) {
                        attacked = attack(game, com);
                        if (!attacked) {
                            System.out.println("MOVED FAILED IN USER: " + curr);
                            agent.sendMessage("Move Failed, player " +
                                    "turn: " + turn);
                        } else {
                            System.out.println("Shots Fired at " +
                                    com[ONE] + " by " + curr);
                            this.current++;
                            String over = game.isGameOver();
                            if (!over.equals("")) {
                                this.current = FIRST_INDEX;
                                System.out.println("GAME OVER: " + over +
                                        " WINS!");
                                broadcast("GAME OVER: " + over + " WINS!");
                                started = false;
                            }
                        }
                    }
                }
            } else {
                agent.sendMessage("Invalid command: " + command);
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
     * This parses the command for a show command.
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
     * Parses the command for quitting from a game of Battleship.
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
        broadcast("!!! " + user + " surrendered");
        sourceClosed(agent);
        game.leave(user);
        String over = game.isGameOver();
        if (!over.equals("")) {
            this.current = FIRST_INDEX;
            System.out.println("GAME OVER: " + over +
                    " WINS!");
            broadcast("GAME OVER: " + over + " WINS!");
            started = false;
        } else {
            String turn = game.turn(this.current);
            System.out.println(turn + " it is yoru turn");
            broadcast(turn + " it is your turn");
        }
    } // end parseQuit method

    /**
     * Parses the command for showing all of the available commands clients can
     * send.
     *
     * @param agent The <code>ConnectionAgent</code> that sent the command.
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
     * This calls the hit method from game to try to attack the grid.
     *
     * @param game The current game.
     * @param command Information that way entered.
     * @return If the attack was complete.
     */
    private boolean attack(Game game, String[] command){
        int row = Integer.parseInt(command[THREE]);
        int column = Integer.parseInt(command[TWO]);
        String nickname  = command[ONE];
        return game.hit(nickname, row, column);
    }

    public void removeCA(ArrayList<ConnectionAgent> closed){
        for (ConnectionAgent agent : closed) {
            parseQuit("/quit", agent);
        }
    }
} // end BattleServer class
