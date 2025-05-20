package controller;

import model.Board;
import model.enums.Player;
import model.factories.BoardFactory;
import model.rules.RuleImplBomb;
import model.rules.RuleImplGomoku;
import model.rules.RuleImplLandfill;
import model.rules.RuleImplReversi;
import view.console.Screen;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Handles the initialization phase of the game setup.
 *
 * <p>This controller is responsible for:
 * <ul>
 *     <li>Configuring game board parameters (size, rules)</li>
 *     <li>Collecting and validating player information</li>
 *     <li>Creating initial game board instances</li>
 *     <li>Setting up default game configurations</li>
 * </ul>
 *
 * <p>Usage example:
 * <pre>
 * InitializationController initController = new InitializationController(scanner, boards, screen);
 * initController.initialize();
 * </pre>
 */
public class InitializationController {
    private final Scanner scanner;
    private final ArrayList<Board> boards;
    private final BoardFactory boardFactory;
    private int boardNumber;

    /**
     * Constructs an InitializationController with required dependencies.
     *
     * @param scanner Input scanner for user interactions
     * @param boards Collection to store initialized boards
     */
    public InitializationController(Scanner scanner, ArrayList<Board> boards) {
        this.scanner = scanner;
        this.boards = boards;
        this.boardFactory = BoardFactory
                .create()
                .useDefaultVerticalAlign()
                .useDefaultHorizontalAlign();
    }

    /**
     * Executes the complete initialization sequence.
     *
     * <p>This method coordinates the setup process by calling individual
     * initialization methods in the proper sequence.
     */
    public void initialize() {
        inputBoardSize();
        inputPlayerNames();
        useDefaultBoardSets();
    }

    /**
     * Creates and adds default board configurations to the game.
     *
     * <p>Initializes three standard game boards with different rules:
     * <ol>
     *   <li>Reversi</li>
     *   <li>Landfill</li>
     *   <li>Gomoku</li>
     * </ol>
     */
    private void useDefaultBoardSets() {
        boardFactory.setRule(RuleImplReversi.getRule());
        boards.add(boardFactory.createBoard());
        boardFactory.setRule(RuleImplLandfill.getRule());
        boards.add(boardFactory.createBoard());
        boardFactory.setRule(RuleImplGomoku.getRule());
        boards.add(boardFactory.createBoard());
        boardFactory.setRule(RuleImplBomb.getRule());
        boards.add(boardFactory.createBoard());
    }

    /**
     * Initializes the specified number of game boards.
     *
     * <p>Creates multiple board instances using the current factory configuration.
     */
    private void initializeBoards() {
        boards.ensureCapacity(boardNumber);
        for(int i = 0; i < boardNumber; i++) {
            boards.add(boardFactory.createBoard());
        }
    }

    /**
     * Prompts for and sets the game rule type.
     *
     * <p>Accepts either "peace" or "reversi" as valid inputs.
     * Falls back to default rule if input is invalid.
     */
    private void inputBoardRule() {
        System.out.println("Enter board rule: (peace or reversi)");
        String input = scanner.nextLine();
        if( input.compareToIgnoreCase("peace")==0 ) {
            boardFactory.setRule(RuleImplLandfill.getRule());
        } else if( input.equals("reversi") ) {
            boardFactory.setRule(RuleImplReversi.getRule());
        } else {
            System.out.println("Invalid board rule, using default rule REVERSI.");
            boardFactory.useDefaultRule();
        }
    }

    /**
     * Collects and validates board size input from the user.
     *
     * <p>Handles various error cases:
     * <ul>
     *   <li>Non-numeric input</li>
     *   <li>Out-of-range values</li>
     *   <li>Unexpected input termination</li>
     * </ul>
     *
     * <p>Board size must be between {@link Board#MIN_BOARD_SIZE} and
     * {@link Board#MAX_BOARD_SIZE}.
     */
    private void inputBoardSize() {
        System.out.println("Enter board size: (one number only)");
        int boardSize;
        while(true) {
            try {
                boardSize = scanner.nextInt();
                boardFactory
                        .setBoardSizeCol(boardSize)
                        .setBoardSizeRow(boardSize);

                if(boardSize < Board.MIN_BOARD_SIZE || boardSize > Board.MAX_BOARD_SIZE) {
                    System.out.println("Invalid board size.");
                    continue;
                }
                break;
            } catch (InputMismatchException _) {
                System.out.println("Invalid input");
                scanner.nextLine();
            } catch (NoSuchElementException _) {
                System.out.println("Session closed");
                System.exit(0);
            }
        }
    }

    /**
     * Collects the number of boards to initialize.
     *
     * <p>Stores the value for later use in {@link #initializeBoards()}.
     */
    private void inputBoardCount() {
        System.out.println("Enter board number: ");
        boardNumber = scanner.nextInt();
        scanner.nextLine();
    }

    /**
     * Collects and returns a player's name with proper validation.
     *
     * @param player The player (WHITE or BLACK) whose name is being collected
     * @return The validated player name
     */
    private String getName(Player player) {
        Screen.clear();
        if(player == Player.WHITE) {
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
     * Manages the complete player name collection process.
     *
     * <p>Ensures names meet requirements:
     * <ul>
     *   <li>Not empty</li>
     *   <li>Maximum length of 32 characters</li>
     * </ul>
     *
     * <p>Persists until valid names are provided for both players.
     */
    private void inputPlayerNames() {
        String whitePlayerName;
        String blackPlayerName;
        do {
            whitePlayerName = getName(Player.WHITE);
        } while (whitePlayerName.length() > 32 || whitePlayerName.isEmpty());

        do {
            blackPlayerName = getName(Player.BLACK);
        } while (blackPlayerName.length() > 32 || blackPlayerName.isEmpty());

        boardFactory
                .setWhitePlayerName(whitePlayerName)
                .setBlackPlayerName(blackPlayerName);
    }
}
