package server;

import java.util.Scanner;

/**
 * The driver of the server side of our application. Validates command line 
 * input and creates a BattleServer that listens for remote host requests.
 *
 * @author Evert Ball and Carlee Yancey
 * @version 18 November 2019
 */
public class BattleShipDriver {

    /** A constant value for successful exit */
    private static final int SUCCESS = 0;

    /** A constant value for failed exit */
    private static final int FAILURE = 1;
    
    /** The valid number of command line args */
    private static final int NUM_ARGS = 2;
    
    /** Usage message to print when program is used incorrectly */
    private static final String USAGE_MSG = ("Usage is: java " +
            "BattleShipDriver <port> <board_size>)");

    
    /**
     * The main entry point into the server side implementation of the 
     * application. Takes two command line arguments. The first being the port
     * for the server and the second the size of the board as a single integer.
     *
     * @param args The command line arguments of which 2 must be provided. The
     *             first argument is the port number for the server, the second 
     *             argument is the size of the board (default 10x10). The user
     *             only need provide one integer (i.e 10 for a board of size 
     *             10x10) since our application assumes square boards.
     */
    public static void main(String[] args) {
        int port = -1;
        int size = -1;
        // Validate correct number of arguments.
        if(args.length != NUM_ARGS) {
            System.out.println(USAGE_MSG);
            System.exit(FAILURE);
        }
        try{
            port = Integer.parseInt(args[0]);
            size = Integer.parseInt(args[1]);
        } catch(NumberFormatException nfe) {
            System.err.println("Invalid argument type. Arguments must be of" +
                                   "integers.");
            System.exit(FAILURE);
        }

        // Creates a BattleServer and starts it listening for connections
        if(port == -1 ) {
            System.out.println("The port was not initialized...");
            System.exit(FAILURE);
        }
        BattleServer bs = new BattleServer(port);
        bs.setSize(size);
        bs.listen();
        // If we made it here, then we can successfully exit
        System.exit(SUCCESS);
/*
        try {
            Scanner info = new Scanner(System.in);
            System.out.println("Enter Size of preferred grid 5-10");
            int size = info.nextInt();
            while(size > 10 || size < 5){
                System.out.println("Size must be between 5 - 10");
                size = info.nextInt();
            }
            int joined = 0;
            boolean started = false;
            Game game = new Game(size);
            while(joined < 2){ //This will run until at laest 2 players joined
                String command = info.next().concat(info.nextLine());
                String[] player = command.trim().split("\\s+");
                if(player[0].equals("/join")){
                    try{
                        game.join(player[1], size);
                    } catch(IndexOutOfBoundsException ioobe) {
                        System.out.println("Usage: /join <username>");
                        continue;
                    }
                    System.out.println("!!! " + player[1] + " has joined the " +
                            "game");
                    joined++;
                }else if(player[0].equals("/play")){
                    System.out.println("Not enough players to play the game");
                } else if(player[0].equals("/quit")){
                    System.out.println("Thanks for playing!");
                    System.exit(SUCCESS);
                }
            }
            while(!(started)) {//Game is able to start or more clients can join
                String command = info.next().concat(info.nextLine());
                String[] player = command.trim().split("\\s+");
                if (player[0].equals("/play")) {
                    if (joined >= 2) {
                        System.out.println("The game begins");
                        play(info, game, size);
                        started = true;
                    }
                } else if (player[0].equals("/join")){
                    game.join(player[1], size);
                    System.out.println("!!! " + player[1] + "has joined the " +
                            "game");
                    joined++;
                } else if(player[0].equals("/quit")) {
                    System.out.println("Thanks for playing!");
                    System.exit(SUCCESS);
                }
            }
            info.close(); //Closes the scanner
        }catch (IllegalArgumentException e){
            System.out.println(e.toString());
        }
 */
    } // end main method

    /**
     * This method runs the game with a given scanner and game.
     * @param scan The scanner that is being used.
     * @param game The current game in play.
     */
//    private static void play(Scanner scan, Game game, int size){
//        String current;
//        int turns = 0;
//        int attAgr = 4;
//        int showArgs = 2;
//        do{//Quit if a board has no ships left
//            current = game.turn(turns);
//            System.out.println(current + " it is you turn");
//            boolean attacked = false;
//            while(!(attacked)) { // this will quit if a player has attacked
//                String command = scan.next().concat(scan.nextLine());
//                String[] com = command.trim().split("\\s+");
//                String info = com[0];
//                int col = -1;
//                int row = -1;
//                switch (info) {
//                    case "/attack":
//                        try{
//                            col = Integer.parseInt(com[2]);
//                            row = Integer.parseInt(com[3]);
//                        } catch(NumberFormatException nfe) {
//                            System.out.println("Attack coordinates must be " +
//                                "integers.");
//                            continue;
//                        } catch(ArrayIndexOutOfBoundsException aioobe) {
//                            System.out.println("Usage: /attack <player> " +
//                                "<col> <row>");
//                            continue;
//                        }
//                        if(col > (size - 1) || row > (size - 1) ||
//                            col < 0 || row < 0) {
//                            System.out.println("Usage: /attack <player> " +
//                                "<col> <row>");
//                            continue;
//                        }
//                        if(com.length == attAgr) {
//                            if(!current.equals(com[1])) {
//                                attacked = attack(game, com);
//                                if(!attacked){
//                                    System.out.println("Move Failed, player " +
//                                            "turn: " + current);
//                                }else{
//                                    System.out.println("Shots Fired at " +
//                                            com [1] + " by " + current);
//                                }
//                            }
//                        }else{
//                            System.out.println("Invalid command: " + command);
//                        }
//                        break;
//                    case "/show":
//                        if (com.length == showArgs) {
//                            game.show(com[1], current);
//
//                        }else{
//                            System.out.println("/show <username>");
//                        }
//                        break;
//                    case "/quit":
//                        //TODO Put the steps in for what happens if quited.
//                        System.exit(SUCCESS);
//                        break;
//                }
//            }
//            turns++;
//           } while(!game.isGameOver());
//    }

    /**
     * This calls the hit method from game to try to attack the grid.
     *
     * @param game The current game.
     * @param command Information that way entered.
     * @return If the attack was complete.
     */
    private static boolean attack(Game game, String[] command){
        int row = Integer.parseInt(command[3]);
        int column = Integer.parseInt(command[2]);
        String nickname  = command[1];
        return game.hit(nickname, row, column);
    }

    public static void commandChoose(String command){
        String comm = command;

    }







} // end BattleShipDriver class
