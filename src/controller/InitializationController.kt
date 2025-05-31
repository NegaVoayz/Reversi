package controller

import model.Board
import model.enums.Player
import model.factories.BoardFactory
import model.rules.RuleImplBomb
import model.rules.RuleImplGomoku
import model.rules.RuleImplLandfill
import model.rules.RuleImplReversi
import view.console.Screen
import java.util.*
import kotlin.system.exitProcess

/**
 * Handles the initialization phase of the game setup.
 *
 *
 * This controller is responsible for:
 *
 *  * Configuring game board parameters (size, rules)
 *  * Collecting and validating player information
 *  * Creating initial game board instances
 *  * Setting up default game configurations
 *
 *
 *
 * Usage example:
 * <pre>
 * InitializationController initController = new InitializationController(scanner, boards, screen);
 * initController.initialize();
</pre> *
 */
class InitializationController(private val scanner: Scanner, private val boards: ArrayList<Board>) {
    private val boardFactory: BoardFactory = BoardFactory
        .create()
        .useDefaultVerticalAlign()
        .useDefaultHorizontalAlign()
    private var boardNumber = 0

    /**
     * Executes the complete initialization sequence.
     *
     *
     * This method coordinates the setup process by calling individual
     * initialization methods in the proper sequence.
     */
    fun initialize() {
        inputBoardSize()
        inputPlayerNames()
        useDefaultBoardSets()
    }

    /**
     * Creates and adds default board configurations to the game.
     *
     *
     * Initializes three standard game boards with different rules:
     *
     *  1. Reversi
     *  1. Landfill
     *  1. Gomoku
     *
     */
    private fun useDefaultBoardSets() {
        boardFactory.setRule(RuleImplReversi.rule)
        boards.add(boardFactory.createBoard())
        boardFactory.setRule(RuleImplLandfill.rule)
        boards.add(boardFactory.createBoard())
        boardFactory.setRule(RuleImplGomoku.rule)
        boards.add(boardFactory.createBoard())
        boardFactory.setRule(RuleImplBomb.rule)
        boards.add(boardFactory.createBoard())
    }

    /**
     * Initializes the specified number of game boards.
     *
     *
     * Creates multiple board instances using the current factory configuration.
     */
    private fun initializeBoards() {
        boards.ensureCapacity(boardNumber)
        for (i in 0..<boardNumber) {
            boards.add(boardFactory.createBoard())
        }
    }

    /**
     * Prompts for and sets the game rule type.
     *
     *
     * Accepts either "peace" or "reversi" as valid inputs.
     * Falls back to default rule if input is invalid.
     */
    private fun inputBoardRule() {
        println("Enter board rule: (peace or reversi)")
        val input = scanner.nextLine()
        if (input.compareTo("peace", ignoreCase = true) == 0) {
            boardFactory.setRule(RuleImplLandfill.rule)
        } else if (input == "reversi") {
            boardFactory.setRule(RuleImplReversi.rule)
        } else {
            println("Invalid board rule, using default rule REVERSI.")
            boardFactory.useDefaultRule()
        }
    }

    /**
     * Collects and validates board size input from the user.
     *
     *
     * Handles various error cases:
     *
     *  * Non-numeric input
     *  * Out-of-range values
     *  * Unexpected input termination
     *
     *
     *
     * Board size must be between [Board.MIN_BOARD_SIZE] and
     * [Board.MAX_BOARD_SIZE].
     */
    private fun inputBoardSize() {
        println("Enter board size: (one number only)")
        var boardSize: Int
        while (true) {
            try {
                boardSize = scanner.nextInt()
                boardFactory
                    .setBoardSizeCol(boardSize)
                    .setBoardSizeRow(boardSize)

                if (boardSize < Board.MIN_BOARD_SIZE || boardSize > Board.MAX_BOARD_SIZE) {
                    println("Invalid board size.")
                    continue
                }
                break
            } catch (e: InputMismatchException) {
                println("Invalid input")
                scanner.nextLine()
            } catch (e: NoSuchElementException) {
                println("Session closed")
                exitProcess(0)
            }
        }
    }

    /**
     * Collects the number of boards to initialize.
     *
     *
     * Stores the value for later use in [.initializeBoards].
     */
    private fun inputBoardCount() {
        println("Enter board number: ")
        boardNumber = scanner.nextInt()
        scanner.nextLine()
    }

    /**
     * Collects and returns a player's name with proper validation.
     *
     * @param player The player (WHITE or BLACK) whose name is being collected
     * @return The validated player name
     */
    private fun getName(player: Player?): String {
        Screen.clear()
        if (player == Player.WHITE) {
            println("[You Play White]")
        } else {
            println("[You Play Black]")
        }
        print("your name is: ")
        if (scanner.hasNext()) {
            return scanner.nextLine()
        }
        scanner.close()
        return ""
    }

    /**
     * Manages the complete player name collection process.
     *
     *
     * Ensures names meet requirements:
     *
     *  * Not empty
     *  * Maximum length of 32 characters
     *
     *
     *
     * It Persists until valid names are provided for both players.
     */
    private fun inputPlayerNames() {
        var whitePlayerName: String
        var blackPlayerName: String
        do {
            whitePlayerName = getName(Player.WHITE)
        } while (whitePlayerName.length > 32 || whitePlayerName.isEmpty())

        do {
            blackPlayerName = getName(Player.BLACK)
        } while (blackPlayerName.length > 32 || blackPlayerName.isEmpty())

        boardFactory
            .setWhitePlayerName(whitePlayerName)
            .setBlackPlayerName(blackPlayerName)
    }
}
