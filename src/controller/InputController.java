package controller;

import model.exceptions.GameException;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * Handles player input parsing and command execution for the game.
 *
 * <p>This controller:
 * <ul>
 *   <li>Processes raw player input from console</li>
 *   <li>Identifies command types and validates syntax</li>
 *   <li>Delegates command execution to GameController</li>
 *   <li>Provides help documentation for available commands</li>
 * </ul>
 */
public class InputController {
    private final Scanner scanner;
    private final GameController gameController;

    /**
     * Enumeration of supported command types.
     */
    private enum CommandType {
        NONE,       // Default/fallback command
        ERROR,      // Invalid command
        HELP,       // Help request
        CHANGE_BOARD, // Switch active board
        CREATE_BOARD, // Create a new board
        LIST_BOARDS,  // List available boards
        PLACE_PIECE,  // Make a move
        DEMO,       // Load DEMO from the specified file
    }

    /**
     * Internal structure representing a parsed command.
     */
    private static class Command {
        public CommandType type;    // Command classification
        public String content;     // Raw command content
    }

    private final Command command = new Command();

    /**
     * Constructs an InputController with required dependencies.
     *
     * @param scanner Input source for player commands
     * @param gameController Main game controller for command delegation
     */
    public InputController(Scanner scanner, GameController gameController) {
        this.scanner = scanner;
        this.gameController = gameController;
    }

    /**
     * Reads a command from the input source.
     *
     * @return This instance for method chaining
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
     * Parses the raw input into a structured command.
     *
     * @return This instance for method chaining
     */
    public InputController parseCommand() {
        Queue<String> tokens = splitCommandTokens();
        String firstToken = tokens.poll();
        command.type = parseToken(firstToken, tokens);
        return this;
    }

    /**
     * Executes the parsed command.
     *
     * @return true if command executed successfully, false otherwise
     */
    public boolean executeCommand() {
        try {
            return switch (command.type) {
                case NONE -> {
                    if(isAllDigits(command.content)) yield gameController.setCurrentBoard(command.content);
                    else yield gameController.parseMove(command.content).placePiece();
                }
                case ERROR -> false;
                case HELP -> showHelp();
                case CHANGE_BOARD -> gameController.setCurrentBoard(command.content);
                case CREATE_BOARD -> gameController.parseCreate(command.content).createBoard();
                case LIST_BOARDS  -> gameController.selectBoards(command.content).listBoards();
                case PLACE_PIECE  -> gameController.parseMove(command.content).placePiece();
                case DEMO         -> runDemo(command.content);
            };
        } catch (IllegalArgumentException | GameException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private boolean isAllDigits(String string) {
        for (char c : string.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    private boolean runDemo(String path) {
        try(Scanner scanner = new Scanner(new File(path))) {
            System.out.println("Demo activated.");
            while (scanner.hasNextLine()) {
                Thread.sleep(Duration.ofSeconds(1));
                String line = scanner.nextLine();
                command.content = line;
                boolean isValidMove = this
                        .parseCommand()
                        .executeCommand();
                System.out.println("Current Command > "+line);
                if( !isValidMove ) {
                    System.out.println("oOps! invalid operation. Try input 'help' for help.");
                }
            }
            System.out.println("Demo exited.");
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(String.format("File %s not found", path));
        } catch (InterruptedException e) {
            System.out.println("How the hell can wait be a");
            throw new RuntimeException(e);
        }
        return true;
    }

    /**
     * Split input string into tokens for further processing.
     *
     * @return split strings
     */
    private Queue<String> splitCommandTokens() {
        return new LinkedList<>(Arrays.stream(command.content.split("\\s+")).toList());
    }

    /**
     * Special bracket check for those who copy the commands literally.
     *
     * @return true if they really did so.
     */
    private boolean findBrackets() {
        String str = command.content;
        return str.indexOf('(') != -1 || str.indexOf(')') != -1
                || str.indexOf('[') != -1 || str.indexOf(']') != -1;
    }

    /**
     * Parse the command by first token, needs further processing
     *
     * @param firstToken the first token of the command, basically the command name.
     * @param tokens the rest tokens to be processed
     * @return This instance for method chaining
     */
    private CommandType parseToken(String firstToken, Queue<String> tokens) {
        if(findBrackets()) {
            System.out.println("No Brackets!");
            return CommandType.ERROR;
        }
        return switch (firstToken) {
            case "help", "man" -> handleHelp();
            case "switch" -> handleSwitch(tokens);
            case "goto" -> handleGoto(tokens);
            case "move" -> handleMove(tokens);
            case "create" -> handleCreate(tokens);
            case "list", "ls" -> handleList(tokens);
            case "quit" -> handleQuit();
            case "demo" -> handleDemo(tokens);
            case null -> CommandType.ERROR;
            default -> CommandType.NONE;
        };
    }

    /**
     * Handler of the 'help'/'man' command
     *
     * @return {@link CommandType#HELP} if succeeded.
     */
    private CommandType handleHelp() {
        return CommandType.HELP;
    }

    /**
     * Handler of the 'switch to' command
     *
     * @return {@link CommandType#CHANGE_BOARD} if succeeded,
     * {@link CommandType#ERROR} if failed.
     */
    private CommandType handleSwitch(Queue<String> tokens) {
        if(tokens.size() < 2 || !tokens.poll().equals("to")) {
            return CommandType.ERROR;
        }
        return handleGoto(tokens);
    }

    /**
     * Handler of the 'goto' command
     *
     * @return {@link CommandType#CHANGE_BOARD} if succeeded,
     * {@link CommandType#ERROR} if failed.
     */
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

    /**
     * Handler of the 'move' command
     *
     * @return {@link CommandType#PLACE_PIECE} if succeeded,
     * {@link CommandType#ERROR} if failed.
     */
    private CommandType handleMove(Queue<String> tokens) {
        if(tokens.size() != 1) {
            return CommandType.ERROR;
        }
        command.content = tokens.poll();
        return CommandType.PLACE_PIECE;
    }


    /**
     * Handler of the 'create board' command
     *
     * @return {@link CommandType#CREATE_BOARD} if succeeded,
     * {@link CommandType#ERROR} if failed.
     */
    private CommandType handleCreate(Queue<String> tokens) {
        if(tokens.size() < 2 || tokens.size() > 4 || !tokens.poll().equals("board")) {
            return CommandType.ERROR;
        }
        command.content = String.join(" ", tokens);
        return CommandType.CREATE_BOARD;
    }

    /**
     * Handler of the 'list'/'ls' command
     *
     * @return {@link CommandType#LIST_BOARDS} if succeeded,
     * {@link CommandType#ERROR} if failed.
     */
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


    /**
     * Handler of the 'quit' command,
     * Quit immediately.
     *
     * @return {@link CommandType#NONE}
     */
    private CommandType handleQuit() {
        scanner.close();
        System.exit(0);
        return CommandType.NONE;
    }

    /**
     * Handler of the 'demo' command
     *
     * @return {@link CommandType#DEMO} if succeeded,
     * {@link CommandType#ERROR} if failed.
     */
    private CommandType handleDemo(Queue<String> tokens) {
        if(tokens.size() != 1) {
            return CommandType.ERROR;
        }
        command.content = tokens.poll();
        return CommandType.DEMO;
    }

    /**
     * Show all commands
     *
     * @return true, always success
     */
    private static boolean showHelp() {
        System.out.println("Command Manual:");
        System.out.println("[] means argument, () means optional.");
        System.out.println("Brackets are not allowed in all commands.");
        System.out.println("| command        | arguments                              | effect                      |");
        System.out.println("|----------------|----------------------------------------|-----------------------------|");
        System.out.println("| help/man       |                                        | help                        |");
        System.out.println("| quit           |                                        | quit                        |");
        System.out.println("| switch to/goto | board NO                               | switch to the desired board |");
        System.out.println("| move           | row-first position (e.g. 3D)           | place piece at [position]   |");
        System.out.println("| create board   | [game mode] ([column size] [row size]) | create new board            |");
        System.out.println("| list/ls        | ([mode: game mode/current])            | list the boards.            |");
        System.out.println("| demo           | path                                   | load demo from a file       |");
        System.out.println("|                | you can omit `move` or `switch to`     | whatever you aim at         |");
        return true;
    }
}
