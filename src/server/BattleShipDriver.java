package server;

import common.Player;

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
    private static final int USAGE_MSG = "Usage is: java BattleShipDriver " +
            "<port> <board_size>);

    
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
/*
        int port;
        int size;
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
        // TODO Figure out how the size is used in this driver.
        BattleServer bs = new BattleServer(port);
        bs.listen();
        // If we made it here, then we can successfully exit
        System.exit(SUCCESS);
*/
        // TODO WHEN WE START NETWORKED PORTION:
        //      Might be best to have a while loop in client that continually 
        //      waits for input and when the next command is a legal command 
        //      then execute it (like /join or /play) by calling the correct
        //      method from Game class. Maybe have the Game class control
        //      handling user input after the /play command has been issued?
        try {
            Scanner info = new Scanner(System.in);
            System.out.println("Enter Size of preferred grid 5-10");
            int size = info.nextInt();
            int joined = 0;
            boolean started = false;
            Game game = new Game(size);
            while(joined < 2){ //This will run until at laest 2 players joined
                String command = info.next();
                String[] player = command.trim().split("\\s");
                if(player[0].equals("/join") && player.length == 2){
                    game.join(player[1], size);
                    System.out.println("!!! " + player[1] + "has joined the " +
                            "game");
                    joined++;
                }else{
                    System.out.println("Not enough players to play the game");
                }
            }
            while(!(started)) {//Game is able to start or more clients can join
                String command = info.next();
                String[] player = command.trim().split("\\s");
                if (player[0].equals("/play")) {
                    if (joined >= 2) {
                        play(info, game);
                        started = true;
                    }
                } else if (player[0].equals("/joined")){
                    game.join(player[1], size);
                    System.out.println("!!! " + player[1] + "has joined the  +" +
                            "game");
                    joined++;
                }
            }
            info.close(); //Closes the scanner
        }catch (){

        }
    } // end main method


    public void play(Scanner scan, Game game){
        String current;
        int turns = 0;
        while(!(game.isGameOver())) {//Quit if a board has no ships left
            current = turn(turns, p1, p2); //TODO figure out how to determine the players based on player ArrayList
            System.out.println(current + "it is you turn");
            boolean attacked = false;
            while(!(attacked)) { // this will quit if a player has attacked
                String command = scan.next();
                String[] com = command.trim().split("\\s");
                String info = com[0];
                switch (info) {
                    case "/attack":
                        if(com.length == 4) {
                            int row = Integer.parseInt(com[3]);
                            int column = Integer.parseInt(com[2]);
                            attacked = attack(game, com[1], column, row);
                        }else{
                            System.out.println("/attack <username> <column> " +
                                    "<row>");
                        }
                        break;
                    case "/show":
                        if (com.length == 2) {
                            game.show(com[1], current);
                        }else{
                            System.out.println("/show <username>");
                        }
                        break;
                    case "/quit":
                        //TODO Put the steps in for what happens if quited.
                        break;
                }
            }
            turns++;
        }
    }

    public boolean attack(Game game, String nickname, int column, int row){
        // "/attack <username> column row
        return game.hit(nickname, row, column);
    }



    public String turn(int turns, String p1, String p2){
        int turn = turns;
        String current = null;
        if((turn % 2) == 0 ){
            current = p1;
        }else{
            current = p2;
        }
        return current;
    }



} // end BattleShipDriver class
