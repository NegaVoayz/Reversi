package main;

import java.util.Scanner;

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

    /**
     * Prompts the players to input their names and sets them on the board.
     *
     * @param scanner The scanner object to read input from the user.
     * @param board The board object where player names will be set.
     */
    private static void inputPlayerNames(Scanner scanner, Board board) {
        String whitePlayerName;
        String blackPlayerName;

        whitePlayerName = getName(Board.Player.WHITE, scanner);
        if(whitePlayerName == "") {
            return;
        }
        blackPlayerName = getName(Board.Player.BLACK, scanner);
        if(blackPlayerName == "") {
            return;
        }
        board.setName(whitePlayerName, blackPlayerName);
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
    private static boolean inputPlacePosition(Scanner scanner, Board board) {
        int col;
        int row;
        String input;

        input = getInput(scanner);

        if(input.length() != 2) {
            System.out.println("oOps! invalid move");
            return false;
        }
    
        col = getCol(input.charAt(1));
        row = getRow(input.charAt(0));

        // location validation check done in "placePiece" function
        if(!board.placePiece(col,row)) {
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
        Scanner scanner = new Scanner(System.in);

        Screen screen = new Screen(22, Board.ULTIMATE_ANSWER);

        Board board = new Board(8, 8, screen.getCanvas(0, 0));

        inputPlayerNames(scanner, board);

        board.paint();

        while(!board.isGameOver()) {
            if( !inputPlacePosition(scanner, board) ) {
                continue;
            }
            board.paint();
        }
        scanner.close();
        return;
    }
}