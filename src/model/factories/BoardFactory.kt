package model.factories

import model.Board
import model.rules.Rule
import model.rules.RuleImplReversi
import model.structs.GameStatistics
import view.components.AlignType

/**
 * Factory class for creating and configuring Board instances.
 *
 *
 * Provides a fluent interface for setting up board parameters and creating
 * properly initialized Board objects. Implement the Builder pattern to allow
 * step-by-step configuration of board properties.
 *
 *
 * Typical usage:
 * <pre>
 * Board board = BoardFactory.create()
 * .setWhitePlayerName("Alice")
 * .setBlackPlayerName("Bob")
 * .setBoardSizeCol(8)
 * .setBoardSizeRow(8)
 * .setRule(new ReversiRule())
 * .createBoard();
 * </pre>
 */
class BoardFactory private constructor() {
    // Configuration fields with default values
    private var whitePlayerName = ""
    private var blackPlayerName = ""
    private var boardSizeCol = 8
    private var boardSizeRow = 8
    private var rule: Rule
    private var verticalAlign: AlignType
    private var horizontalAlign: AlignType

    /**
     * Private constructor enforcing use of factory method.
     * Initializes default values for all board properties.
     */
    init {
        verticalAlign = AlignType.BEGIN
        horizontalAlign = AlignType.BEGIN
        rule = RuleImplReversi.rule
    }

    val isLegalSetting: Boolean
        /**
         * Validates current factory configuration.
         *
         * @return true if all required fields are set with valid values, false otherwise
         */
        get() = !whitePlayerName.isEmpty() && !blackPlayerName.isEmpty() && (Board.MIN_BOARD_SIZE <= boardSizeCol && boardSizeCol <= Board.MAX_BOARD_SIZE) && (Board.MIN_BOARD_SIZE <= boardSizeRow && boardSizeRow <= Board.MAX_BOARD_SIZE)

    // Fluent interface setters with method chaining
    /**
     * Sets the white player's name.
     *
     * @param whitePlayerName Name of the white player
     * @return This factory instance for method chaining
     */
    fun setWhitePlayerName(whitePlayerName: String): BoardFactory {
        this.whitePlayerName = whitePlayerName
        return this
    }

    /**
     * Sets the black player's name.
     *
     * @param blackPlayerName Name of the black player
     * @return This factory instance for method chaining
     */
    fun setBlackPlayerName(blackPlayerName: String): BoardFactory {
        this.blackPlayerName = blackPlayerName
        return this
    }

    /**
     * Sets the number of columns for the board.
     *
     * @param boardSizeCol Number of columns (must be within MIN/MAX_BOARD_SIZE)
     * @return This factory instance for method chaining
     */
    fun setBoardSizeCol(boardSizeCol: Int): BoardFactory {
        this.boardSizeCol = boardSizeCol
        return this
    }

    /**
     * Resets column count to default value (8).
     *
     * @return This factory instance for method chaining
     */
    fun useDefaultBoardSizeCol(): BoardFactory {
        this.boardSizeCol = 8
        return this
    }

    /**
     * Sets the number of rows for the board.
     *
     * @param boardSizeRow Number of rows (must be within MIN/MAX_BOARD_SIZE)
     * @return This factory instance for method chaining
     */
    fun setBoardSizeRow(boardSizeRow: Int): BoardFactory {
        this.boardSizeRow = boardSizeRow
        return this
    }

    /**
     * Resets row count to default value (8).
     *
     * @return This factory instance for method chaining
     */
    fun useDefaultBoardSizeRow(): BoardFactory {
        this.boardSizeRow = 8
        return this
    }

    /**
     * Sets the game rule implementation.
     *
     * @param rule Game rule to use
     * @return This factory instance for method chaining
     */
    fun setRule(rule: Rule): BoardFactory {
        this.rule = rule
        return this
    }

    /**
     * Resets game rule to default (Reversi).
     *
     * @return This factory instance for method chaining
     */
    fun useDefaultRule(): BoardFactory {
        this.rule = RuleImplReversi.rule
        return this
    }

    /**
     * Sets vertical alignment of board content.
     *
     * @param verticalAlign Alignment type (BEGIN, MIDDLE, END)
     * @return This factory instance for method chaining
     */
    fun setVerticalAlign(verticalAlign: AlignType): BoardFactory {
        this.verticalAlign = verticalAlign
        return this
    }

    /**
     * Resets vertical alignment to default (MIDDLE).
     *
     * @return This factory instance for method chaining
     */
    fun useDefaultVerticalAlign(): BoardFactory {
        this.verticalAlign = AlignType.MIDDLE
        return this
    }

    /**
     * Sets horizontal alignment of board content.
     *
     * @param horizontalAlign Alignment type (BEGIN, MIDDLE, END)
     * @return This factory instance for method chaining
     */
    fun setHorizontalAlign(horizontalAlign: AlignType): BoardFactory {
        this.horizontalAlign = horizontalAlign
        return this
    }

    /**
     * Resets horizontal alignment to default (BEGIN).
     *
     * @return This factory instance for method chaining
     */
    fun useDefaultHorizontalAlign(): BoardFactory {
        this.horizontalAlign = AlignType.BEGIN
        return this
    }

    /**
     * Creates a new Board instance with the current configuration.
     *
     * @return Newly created Board
     * @throws IllegalStateException if the configuration is invalid (check with isLegalSetting())
     */
    fun createBoard(): Board {
        return Board(
            rule,
            GameStatistics(
                boardSizeCol,
                boardSizeRow,
                whitePlayerName,
                blackPlayerName,
                rule
            )
        )
    }

    companion object {
        /**
         * Factory creation method.
         *
         * @return New BoardFactory instance with default configuration
         */
        fun create(): BoardFactory {
            return BoardFactory()
        }
    }
}
