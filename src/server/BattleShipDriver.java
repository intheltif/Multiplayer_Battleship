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
    private static final int USAGE_MSG = "Usage is: java BattleShipDriver <port> <board_size>);

    
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
            System.err.println("Invalid argument type. Arguments must be of integers.");
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
            System.out.println("Enter Player1 NickName");
            String player1 = info.next();
            System.out.println("Enter Player2 NickName");
            String player2 = info.next();
            String next = info.next();
            if(next.equals("/play")){
                play(info, player1,player2);
            }
            info.close();
        }catch (){

        }
    } // end main method


    public void play(Scanner scan,String p1, String p2){
        Game game = new Game();
        String current;
        int turns = 0;
        while(!(game.isGameOver())) { //This will quit if a board has no ships left
            current = turn(turns, p1, p2);
            System.out.println(current + "it is you turn");
            boolean attacked = false;
            while(!(attacked)) { // this will quit if a player has attacked
                String command = scan.next();
                String[] com = command.trim().split("\\s");
                String info = com[0];
                switch (info) {
                    case "/attack":
                        int row = Integer.parseInt(com[3]);
                        int column = Integer.parseInt(com[2]);
                        attack(game, com[1], column, row);
                        attacked = true;
                        break;
                    case "/show":
                        show(game, com[1], current);
                        break;
                    case "/quit":
                        //TODO Put the steps in for what happens if quited.
                        break;
                }
            }
            turns++;
        }
    }

    public void attack(Game game, String nickname, int column, int row){
        // "/attack <username> column row
        String[][] grid = game.getGrid(nickname);
        game.hit(grid, row, column);
    }

    public void show(Game game, String nickname, String current){
        // "/show <username>
        if(current.equals(nickname)){
            //TODO make sure it prints the board with ships on it
            Grid show = game.getGrid(nickname);
            show.printGrid();
        }else{
            //TODO make sure it prints the board with no ships
            Grid show = game.getGrid(nickname);
            show.printGrid();
        }


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
