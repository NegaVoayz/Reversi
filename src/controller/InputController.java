package controller;

import model.Board;

import java.util.Scanner;

public class InputController {
    private final Scanner scanner;
    private final GameController gameController;
    private enum CommandType {
        NONE,
        CHANGE_BOARD,
        PLACE_PIECE,
    }
    private static class Command {
        CommandType type;
        int[] arguments;
    }

    public InputController(Scanner scanner, GameController gameController) {
        this.scanner = scanner;
        this.gameController = gameController;
        this.commandBuffer = new Command();
    }

    private String inputBuffer;
    private final Command commandBuffer;

    /**
     * Retrieves the input decision from the player.
     *
     * @return self for chain calling
     */
    public InputController readCommand() {
        System.out.print("your decision is: ");
        // if player get bored, die
        if(!scanner.hasNext()) {
            inputBuffer = "wohohohoho";
        }
        inputBuffer = scanner.nextLine();
        return this;
    }

    /**
     * parse the input into command
     *
     * @return self for chain calling
     */
    public InputController parseCommand() {
        switch (inputBuffer.length()) {
            case 1:
                commandBuffer.type = CommandType.CHANGE_BOARD;
                commandBuffer.arguments = new int[]{getIndex(inputBuffer.charAt(0))};
                break;
            case 2:
                commandBuffer.type = CommandType.PLACE_PIECE;
                commandBuffer.arguments = new int[]{getRow(inputBuffer.charAt(0)),getCol(inputBuffer.charAt(1))};
                break;
            default:
                commandBuffer.type = CommandType.NONE;
        }
        return this;
    }

    public boolean executeCommand() {
        return switch (commandBuffer.type) {
            case NONE -> false;
            case CHANGE_BOARD -> gameController.setCurrentBoard(commandBuffer.arguments[0]);
            case PLACE_PIECE -> gameController.placePiece(commandBuffer.arguments[0], commandBuffer.arguments[1]);
        };
    }

    /**
     * Converts a hex code to its corresponding integer value.
     * @param code The character code representing the board No.
     * @return The integer value of board No.
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
}
