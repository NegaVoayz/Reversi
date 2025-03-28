package controller;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
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
        Queue<String> tokens = new LinkedList<>(Arrays.stream(command.content.split("\\s+")).toList());
        switch (tokens.poll()) {
            case "help":
            case "man":
                this.command.type = CommandType.HELP;
                break;
            case "switch":
                if(tokens.size() != 2 || !tokens.poll().equals("to")) {
                    command.type = CommandType.NONE;
                    break;
                }
                /*fallthrough*/
            case "goto":
                command.type = CommandType.CHANGE_BOARD;
                command.content = tokens.poll();
                break;
            case "move":
                if(tokens.size() == 1) {
                    command.type = CommandType.PLACE_PIECE;
                    command.content = tokens.poll();
                    break;
                }
                command.type = CommandType.NONE;
                break;
            case "create":
                if(tokens.size() >= 2 && tokens.size() <= 4 && tokens.poll().equals("board")) {
                    command.type = CommandType.CREATE_BOARD;
                    StringBuilder tempString = new StringBuilder();
                    while(!tokens.isEmpty()) {
                        tempString.append(tokens.poll()).append(" ");
                    }
                    command.content = tempString.toString();
                    break;
                }
                command.type = CommandType.NONE;
                break;
            case "list":
            case "ls":
                if(tokens.size() == 1) {
                    command.type = CommandType.LIST_BOARDS;
                    command.content = tokens.poll();
                    break;
                } else if(tokens.isEmpty()) {
                    command.type = CommandType.LIST_BOARDS;
                    command.content = "";
                    break;
                }
                command.type = CommandType.NONE;
                break;
            case null:
                command.type = CommandType.NONE;
                command.content = "";
                break;
            default:
                command.type = CommandType.NONE;

                break;
        }
        return this;
    }

    public boolean executeCommand() {
        return switch (command.type) {
            case NONE -> gameController.parseMove(command.content).placePiece()
                      || gameController.setCurrentBoard(command.content)
                      || gameController.parseCreate(command.content).createBoard();
            case HELP -> showHelp();
            case CHANGE_BOARD -> gameController.setCurrentBoard(command.content);
            case CREATE_BOARD -> gameController.parseCreate(command.content).createBoard();
            case LIST_BOARDS  -> gameController.selectBoards(command.content).listBoards();
            case PLACE_PIECE  -> gameController.parseMove(command.content).placePiece();
        };
    }

    private static boolean showHelp() {
        System.out.println("Command Manual:");
        System.out.println("| command        | arguments                                            | effect                      |");
        System.out.println("|----------------|------------------------------------------------------|-----------------------------|");
        System.out.println("| help/man       |                                                      | help                        |");
        System.out.println("| switch to/goto | board NO                                             | switch to the desired board |");
        System.out.println("| move           | position(e.g. 3D)                                    | place piece at [position]   |");
        System.out.println("| create board   | [mode: reversi/peace] ([column size] [row size])     | create new board            |");
        System.out.println("| list/ls        | ([mode: reversi/peace/current])                      | list the boards.            |");
        System.out.println("|                | you can omit `move` or `create board` or `switch to` | whatever you aim at         |");
        return true;
    }
}
