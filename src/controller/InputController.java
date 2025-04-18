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
        ERROR,
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
    private final Command command = new Command();

    public InputController(Scanner scanner, GameController gameController) {
        this.scanner = scanner;
        this.gameController = gameController;
    }

    /**
     * Retrieves the input decision from the player.
     *
     * @return self for chain calling
     */
    public InputController readCommand() {
        System.out.print("> ");
        if(scanner.hasNext()) {
            command.content = scanner.nextLine();
        } else {
            command.content = "quit";
        }
        return this;
    }

    /**
     * parse the input into command
     *
     * @return self for chain calling
     */
    public InputController parseCommand() {
        Queue<String> tokens = splitCommandTokens();
        String firstToken = tokens.poll();
        command.type = parseToken(firstToken, tokens);
        return this;
    }

    public boolean executeCommand() {
        return switch (command.type) {
            case NONE -> gameController.parseMove(command.content).placePiece()
                    || gameController.setCurrentBoard(command.content);
            case ERROR -> false;
            case HELP -> showHelp();
            case CHANGE_BOARD -> gameController.setCurrentBoard(command.content);
            case CREATE_BOARD -> gameController.parseCreate(command.content).createBoard();
            case LIST_BOARDS  -> gameController.selectBoards(command.content).listBoards();
            case PLACE_PIECE  -> gameController.parseMove(command.content).placePiece();
        };
    }

    private Queue<String> splitCommandTokens() {
        return new LinkedList<>(Arrays.stream(command.content.split("\\s+")).toList());
    }

    private CommandType parseToken(String firstToken, Queue<String> tokens) {
        return switch (firstToken) {
            case "help", "man" -> handleHelp();
            case "switch" -> handleSwitch(tokens);
            case "goto" -> handleGoto(tokens);
            case "move" -> handleMove(tokens);
            case "create" -> handleCreate(tokens);
            case "list", "ls" -> handleList(tokens);
            case "quit" -> handleQuit();
            case null -> CommandType.ERROR;
            default -> CommandType.NONE;
        };
    }

    private CommandType handleHelp() {
        return CommandType.HELP;
    }

    private CommandType handleSwitch(Queue<String> tokens) {
        if(tokens.size() < 2 || !tokens.poll().equals("to")) {
            return CommandType.ERROR;
        }
        return handleGoto(tokens);
    }

    private CommandType handleGoto(Queue<String> tokens) {
        if(tokens.size() == 2 && tokens.poll().compareToIgnoreCase("board") != 0) {
            return CommandType.ERROR;
        }
        if(tokens.size() != 1) {
            return CommandType.ERROR;
        }
        command.content = tokens.poll();
        return CommandType.CHANGE_BOARD;
    }

    private CommandType handleMove(Queue<String> tokens) {
        if(tokens.size() != 1) {
            return CommandType.ERROR;
        }
        command.content = tokens.poll();
        return CommandType.PLACE_PIECE;
    }

    private CommandType handleCreate(Queue<String> tokens) {
        if(tokens.size() < 2 || tokens.size() > 4 || !tokens.poll().equals("board")) {
            return CommandType.ERROR;
        }
        command.content = String.join(" ", tokens);
        return CommandType.CREATE_BOARD;
    }

    private CommandType handleList(Queue<String> tokens) {
        if(tokens.size() == 1) {
            command.content = tokens.poll();
        } else {
            command.content = "";
        }
        if(!tokens.isEmpty()) {
            return CommandType.ERROR;
        }
        return CommandType.LIST_BOARDS;
    }

    private CommandType handleQuit() {
        scanner.close();
        System.exit(0);
        return CommandType.NONE;
    }

    private static boolean showHelp() {
        System.out.println("Command Manual:");
        System.out.println("| command        | arguments                              | effect                      |");
        System.out.println("|----------------|----------------------------------------|-----------------------------|");
        System.out.println("| help/man       |                                        | help                        |");
        System.out.println("| quit           |                                        | quit                        |");
        System.out.println("| switch to/goto | board NO                               | switch to the desired board |");
        System.out.println("| move           | row-first position (e.g. 3D)           | place piece at [position]   |");
        System.out.println("| create board   | [game mode] ([column size] [row size]) | create new board            |");
        System.out.println("| list/ls        | ([mode: game mode/current])            | list the boards.            |");
        System.out.println("|                | you can omit `move` or `switch to`     | whatever you aim at         |");
        return true;
    }
}
