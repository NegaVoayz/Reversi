package controller;

import model.rules.InputRule;
import model.structs.Point;

import java.util.Scanner;

public class InputController {
    private final Scanner scanner;
    private final GameController gameController;
    private enum CommandType {
        NONE,
        HELP,
        CHANGE_BOARD,
        CREATE_BOARD,
        LIST_BOARDS,
        PLACE_PIECE,
    }
    private static class Command {
        public CommandType type;
        public String content;
    }
    private final Command command;

    public InputController(Scanner scanner, GameController gameController) {
        this.scanner = scanner;
        this.gameController = gameController;
        this.command = new Command();
    }

    /**
     * Retrieves the input decision from the player.
     *
     * @return self for chain calling
     */
    public InputController readCommand() {
        System.out.print("your decision is: ");
        // if player get bored, die
        if(!scanner.hasNext()) {
            command.content = "wohohohoho";
        }
        command.content = scanner.nextLine();
        return this;
    }

    /**
     * parse the input into command
     *
     * @return self for chain calling
     */
    public InputController parseCommand() {
        String[] tokens = command.content.split("\\s+");
        switch (tokens[0]) {
            case "help":
            case "man":
                this.command.type = CommandType.HELP;
                break;
            case "switch":
                if(tokens.length == 3 && tokens[1].equals("to")) {
                    command.type = CommandType.CHANGE_BOARD;
                    command.content = tokens[2];
                    break;
                }
                command.type = CommandType.NONE;
                break;
            case "move":
                if(tokens.length == 2) {
                    command.type = CommandType.PLACE_PIECE;
                    command.content = tokens[1];
                    break;
                }
                command.type = CommandType.NONE;
                break;
            case "create":
                if(tokens.length >= 3 && tokens.length <= 5 && tokens[1].equals("board")) {
                    command.type = CommandType.CREATE_BOARD;
                    if(tokens.length == 4) {
                        command.content = tokens[2]+" "+tokens[3]+" "+tokens[3];
                    } else if(tokens.length == 5) {
                        command.content = tokens[2]+" "+tokens[3]+" "+tokens[4];
                    } else {
                        command.content = tokens[2];
                    }
                    break;
                }
                command.type = CommandType.NONE;
                break;
            case "list":
                if(tokens.length == 2) {
                    command.type = CommandType.LIST_BOARDS;
                    command.content = tokens[1];
                    break;
                } else if(tokens.length == 1) {
                    command.type = CommandType.LIST_BOARDS;
                    command.content = "";
                    break;
                }
                command.type = CommandType.NONE;
                break;
            default:
                command.type = CommandType.NONE;
                break;
        }
        return this;
    }

    public boolean executeCommand() {
        return switch (command.type) {
            case NONE -> false;
            case HELP -> showHelp();
            case CHANGE_BOARD -> gameController.setCurrentBoard(Integer.parseInt(command.content));
            case CREATE_BOARD -> gameController.parseCreate(command.content).createBoard();
            case LIST_BOARDS  -> gameController.selectBoards(command.content).listBoards();
            case PLACE_PIECE  -> gameController.parseMove(command.content).placePiece();
        };
    }

    private static boolean showHelp() {
        System.out.println("Command Manual:");
        System.out.println("| command      | arguments                                        | effect                      |");
        System.out.println("| help/man     |                                                  | help                        |");
        System.out.println("| switch to    | board NO                                         | switch to the desired board |");
        System.out.println("| move         | position(e.g. 3D)                                | place piece at [position]   |");
        System.out.println("| create board | [mode: reversi/peace] ([column size] [row size]) | create new board            |");
        System.out.println("| list         | ([mode: reversi/peace/current])                  | list the boards.            |");
        return true;
    }
}
