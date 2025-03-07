package main;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import view.Screen;
import model.Board;

public class Reversi{
    /**
     * Retrieves the name of the player.
     *
     * @param player The player whose name is to be retrieved.
     * @param scanner The scanner object to read input from the user.
     * @return The name of the player.
     */
    private static String getName(Board.Player player, Scanner scanner) {
        Screen.clear();
        if(player == Board.Player.WHITE) {
            System.out.println("[You Play White]");
        } else {
            System.out.println("[You Play Black]");
        }
        System.out.print("your name is: ");
        if(scanner.hasNext()) {
            return scanner.nextLine();
        }
        scanner.close();
        return "";
    }


    static String whitePlayerName;
    static String blackPlayerName;
    /**
     * Prompts the players to input their names and sets them on the board.
     *
     * @param scanner The scanner object to read input from the user.
     * @param board The board object where player names will be set.
     */
    private static void inputPlayerNames(Scanner scanner, Board[] boards) {

        while(true) {
            whitePlayerName = getName(Board.Player.WHITE, scanner);
            if(whitePlayerName == "") {
                return;
            }
            if(whitePlayerName.length() <= 32) {
                break;
            }
        }
        while(true) {
            blackPlayerName = getName(Board.Player.BLACK, scanner);
            if(blackPlayerName == "") {
                return;
            }
            if(blackPlayerName.length() <= Board.canvas_width - 10) {
                break;
            }
        }
        for(Board board : boards) {
            board.setName(whitePlayerName, blackPlayerName);
        }
    }

    /**
     * Retrieves the input decision from the player.
     *
     * @param scanner The scanner object to read input from the user.
     * @return The player's decision as a string.
     */
    private static String getInput(Scanner scanner) {
        System.out.print("your decision is: ");
        // if player get bored, die
        if(!scanner.hasNext()) {
            return "wohohohoho";
        }
        return scanner.nextLine();
    }

    /**
     * Converts a hex code to its corresponding integer value.
     * @param code
     * @return
     */
    private static int getIndex(char code) {
        if(code > '9') {
            return ((int)code|32)-'a'+10;
        } else {
            return (int)code-'0';
        }
    }

    /**
     * Converts a column character code to its corresponding integer value.
     *
     * @param code The character code representing the column.
     * @return The integer value of the column.
     */
    private static int getCol(char code) {
        return (int)code-'A'+1;
    }

    /**
     * Converts a row character code to its corresponding integer value.
     *
     * @param code The character code representing the row.
     * @return The integer value of the row.
     */
    private static int getRow(char code) {
        if(code > '9') {
            return (int)code-'a'+10;
        } else {
            return (int)code-'0';
        }
    }

    /**
     * Prompts the player to input a position to place their piece and validates the input.
     *
     * @param scanner The scanner object to read input from the user.
     * @param board The board object where the piece will be placed.
     * @return True if the position is valid and the piece is placed successfully
     */
    private static boolean inputPlacePosition(Scanner scanner, Board[] boards) {
        int index;
        int col;
        int row;
        String input;

        input = getInput(scanner);

        if(input.length() != 3) {
            System.out.println("oOps! invalid move");
            return false;
        }
    
        index = getIndex(input.charAt(0));
        col = getCol(input.charAt(2));
        row = getRow(input.charAt(1));

        if(index < 0 || index > boards.length) {
            System.out.println("oOps! invalid board");
            return false;
        }

        if(boards[index].isGameOver()) {
            System.out.println("oOps! wrong board");
            return false;
        }

        // location validation check done in "placePiece" function
        if(!boards[index].placePiece(col,row)) {
            System.out.println("oOps! invalid move");
            return false;
        }
        return true;
    }

    /**
     * The main method to run the Reversi game.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        Board.canvas_width = 23;
        final int boardCount = 3;
        final int boardCountRow = 2;

        Scanner scanner = new Scanner(System.in);

        Screen screen = new Screen((boardCount+boardCountRow)/boardCountRow*(Board.canvas_height+1)-1, boardCountRow*Board.canvas_width);

        Board[] boards = new Board[boardCount];
        
        for(int i = 0; i < boardCount; i++) {
            boards[i] = new Board(8, 8, screen.getCanvas(i%boardCountRow*Board.canvas_width, (i/boardCountRow) * (Board.canvas_height + 1)));
        }

        inputPlayerNames(scanner, boards);

        for(Board board : boards) {
            board.paint();
        }

        boolean allGameOver = false;
        while(!allGameOver) {
            if( !inputPlacePosition(scanner, boards) ) {
                continue;
            }

            for(Board board : boards) {
                board.paint();
            }

            allGameOver = true;
            for(Board board : boards) {
                if(!board.isGameOver()) {
                    allGameOver = false;
                }
            }
        }

        System.out.println("Game Over!");
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int cnt = 0;
        for(Board board : boards) {
            if(board.getWinner() == Board.Player.WHITE) {
                cnt++;
            }
        }
        screen.clearScreenBuffer();
        if(cnt > boardCount/2) {
            screen.println(0, "White Wins"); 
            screen.println(1, "Good game " + whitePlayerName); 
        } else if(cnt < boardCount/2 || boardCount%2 == 1) {
            screen.println(0, "Black Wins"); 
            screen.println(1, "Good game " + blackPlayerName); 
        } else {
            screen.println(0, "Draw"); 
            screen.println(1, "Cool"); 
        }
        screen.paint();

        scanner.close();
        return;
    }
}