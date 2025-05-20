package model.factories;

import model.Board;
import view.components.AlignType;
import model.rules.Rule;
import model.rules.RuleImplReversi;
import model.structs.GameStatistics;

/**
 * Factory class for creating and configuring Board instances.
 *
 * <p>Provides a fluent interface for setting up board parameters and creating
 * properly initialized Board objects. Implements the Builder pattern to allow
 * step-by-step configuration of board properties.
 *
 * <p>Typical usage:
 * <pre>
 * Board board = BoardFactory.create()
 *     .setWhitePlayerName("Alice")
 *     .setBlackPlayerName("Bob")
 *     .setBoardSizeCol(8)
 *     .setBoardSizeRow(8)
 *     .setRule(new ReversiRule())
 *     .createBoard();
 * </pre>
 */
public class BoardFactory {
    // Configuration fields with default values
    private String whitePlayerName;
    private String blackPlayerName;
    private int boardSizeCol;
    private int boardSizeRow;
    private Rule rule;
    private AlignType verticalAlign;
    private AlignType horizontalAlign;
    
    /**
     * Factory creation method.
     *
     * @return New BoardFactory instance with default configuration
     */
    public static BoardFactory create() {
        return new BoardFactory();
    }

    /**
     * Private constructor enforcing use of factory method.
     * Initializes default values for all board properties.
     */
    private BoardFactory() {
        whitePlayerName = "";
        blackPlayerName = "";
        boardSizeCol = 8;
        boardSizeRow = 8;
        verticalAlign = AlignType.BEGIN;
        horizontalAlign = AlignType.BEGIN;
        rule = RuleImplReversi.getRule();
    }

    /**
     * Validates current factory configuration.
     *
     * @return true if all required fields are set with valid values, false otherwise
     */
    public boolean isLegalSetting() {
        return  !whitePlayerName.isEmpty() &&
                !blackPlayerName.isEmpty() &&
                !(rule == null) &&
                (Board.MIN_BOARD_SIZE <= boardSizeCol && boardSizeCol <= Board.MAX_BOARD_SIZE) &&
                (Board.MIN_BOARD_SIZE <= boardSizeRow && boardSizeRow <= Board.MAX_BOARD_SIZE);
    }

    // Fluent interface setters with method chaining

    /**
     * Sets the white player's name.
     *
     * @param whitePlayerName Name of the white player
     * @return This factory instance for method chaining
     */
    public BoardFactory setWhitePlayerName(String whitePlayerName) {
        this.whitePlayerName = whitePlayerName;
        return this;
    }

    /**
     * Sets the black player's name.
     *
     * @param blackPlayerName Name of the black player
     * @return This factory instance for method chaining
     */
    public BoardFactory setBlackPlayerName(String blackPlayerName) {
        this.blackPlayerName = blackPlayerName;
        return this;
    }

    /**
     * Sets the number of columns for the board.
     *
     * @param boardSizeCol Number of columns (must be within MIN/MAX_BOARD_SIZE)
     * @return This factory instance for method chaining
     */
    public BoardFactory setBoardSizeCol(int boardSizeCol) {
        this.boardSizeCol = boardSizeCol;
        return this;
    }

    /**
     * Resets column count to default value (8).
     *
     * @return This factory instance for method chaining
     */
    public BoardFactory useDefaultBoardSizeCol() {
        this.boardSizeCol = 8;
        return this;
    }

    /**
     * Sets the number of rows for the board.
     *
     * @param boardSizeRow Number of rows (must be within MIN/MAX_BOARD_SIZE)
     * @return This factory instance for method chaining
     */
    public BoardFactory setBoardSizeRow(int boardSizeRow) {
        this.boardSizeRow = boardSizeRow;
        return this;
    }

    /**
     * Resets row count to default value (8).
     *
     * @return This factory instance for method chaining
     */
    public BoardFactory useDefaultBoardSizeRow() {
        this.boardSizeRow = 8;
        return this;
    }

    /**
     * Sets the game rule implementation.
     *
     * @param rule Game rule to use
     * @return This factory instance for method chaining
     */
    public BoardFactory setRule(Rule rule) {
        this.rule = rule;
        return this;
    }

    /**
     * Resets game rule to default (Reversi).
     *
     * @return This factory instance for method chaining
     */
    public BoardFactory useDefaultRule() {
        this.rule = RuleImplReversi.getRule();
        return this;
    }

    /**
     * Sets vertical alignment of board content.
     *
     * @param verticalAlign Alignment type (BEGIN, MIDDLE, END)
     * @return This factory instance for method chaining
     */
    public BoardFactory setVerticalAlign(AlignType verticalAlign) {
        this.verticalAlign = verticalAlign;
        return this;
    }

    /**
     * Resets vertical alignment to default (MIDDLE).
     *
     * @return This factory instance for method chaining
     */
    public BoardFactory useDefaultVerticalAlign() {
        this.verticalAlign = AlignType.MIDDLE;
        return this;
    }

    /**
     * Sets horizontal alignment of board content.
     *
     * @param horizontalAlign Alignment type (BEGIN, MIDDLE, END)
     * @return This factory instance for method chaining
     */
    public BoardFactory setHorizontalAlign(AlignType horizontalAlign) {
        this.horizontalAlign = horizontalAlign;
        return this;
    }

    /**
     * Resets horizontal alignment to default (BEGIN).
     *
     * @return This factory instance for method chaining
     */
    public BoardFactory useDefaultHorizontalAlign() {
        this.horizontalAlign = AlignType.BEGIN;
        return this;
    }

    /**
     * Creates a new Board instance with the current configuration.
     *
     * @return Newly created Board
     * @throws IllegalStateException if the configuration is invalid (check with isLegalSetting())
     */
    public Board createBoard() {
        return new Board(
                rule,
                new GameStatistics(
                        boardSizeCol,
                        boardSizeRow,
                        whitePlayerName,
                        blackPlayerName,
                        rule));
    }
}
