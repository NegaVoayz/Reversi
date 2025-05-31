package model

import model.enums.Player
import model.exceptions.GameException
import model.rules.Rule
import model.rules.inputRule.InputRule
import model.structs.GameStatistics
import model.structs.Move
import view.components.AbstractDisplayBlock

/**
 * Represents a game board with all its state and rendering logic.
 *
 *
 * This class manages:
 *
 *  * Game state including pieces, players, and turns
 *  * Board rendering and display updates
 *  * Move validation and execution
 *  * Score tracking and winner determination
 *
 *
 *
 * The board uses a grid system with (1,1) as the top-left corner.
 */
class Board(
    /**
     * Gets the game rule implementation.
     *
     * @return Current game rule
     */
    // Game State
    val rule: Rule, private val statistics: GameStatistics
) {
    /**
     * Constructs a new game board.
     *
     * @param rule Game rules implementation
     * @param statistics Game statistics implementation
     * @throws IllegalArgumentException if parameters are invalid
     */
    init {
        this.rule.gameRule.initializeExtraInfo(this.statistics) /* Initialize game grid according to rules */

        // Initial setup
        this.rule.displayRule.buildView(this.statistics, this.rule)
        this.rule.displayRule.update(this.statistics, this.rule)
    }

    val briefInformation: String
        /**
         * Gets brief information about the board.
         *
         * @return String in format "ruleName <height>x<width>"
        </width></height> */
        get() = rule.name + " " + statistics.height + "x" + statistics.width

    val whitePlayerName: String
        /**
         * Gets the white player's name.
         *
         * @return White player name
         */
        get() = statistics.whitePlayerName
    val blackPlayerName: String
        /**
         * Gets the black player's name.
         *
         * @return Black player name
         */
        get() = statistics.blackPlayerName

    val inputRule: InputRule
        /**
         * Gets the input rule implementation.
         *
         * @return Current input rule
         */
        get() = rule.inputRule

    /**
     * Gets string representation of board state.
     *
     * @return String describing game state
     */
    override fun toString(): String {
        return rule.name + " " + statistics.height + "x" + statistics.width + " " + when (statistics.winner) {
            null -> "ongoing"
            Player.NONE -> "draw"
            Player.BLACK -> "black win"
            Player.WHITE -> "white win"
        }
    }

    val isGameOver: Boolean
        /**
         * Checks if game is over.
         *
         * @return true if the game has ended, false otherwise
         */
        get() = statistics.winner != null

    val winner: Player?
        /**
         * Gets the winner of the game.
         *
         * @return Winning player, or null if the game isn't over
         */
        get() = statistics.winner

    /**
     * Attempts to place a piece at the specified position.
     *
     * @param move Contains target position and player
     * @return true if the move was valid and executed, false otherwise
     */
    @Throws(GameException::class)
    fun placePiece(move: Move): Boolean {
        // Validate move coordinates
        if (!rule.gameRule.placePieceValidationCheck(move, statistics)) {
            return false
        }

        // Execute move
        rule.gameRule.placePiece(move, statistics)

        // Switch players
        rule.gameRule.nextPlayer(statistics)

        // Check Game Over
        rule.gameRule.gameOverCheck(statistics)

        // Update display
        rule.displayRule.update(statistics, rule)

        return true
    }

    /**
     * Renders the board to the display.
     */
    fun show(): AbstractDisplayBlock {
        return statistics.view
    }

    companion object {
        // Constants
        /** The answer to the Ultimate Question of Life, the Universe, and Everything  */
        const val ULTIMATE_ANSWER: Int = 42

        /** Maximum allowed board size (width/height)  */
        const val MAX_BOARD_SIZE: Int = 15

        /** Minimum allowed board size (width/height)  */
        const val MIN_BOARD_SIZE: Int = 4
    }
}
