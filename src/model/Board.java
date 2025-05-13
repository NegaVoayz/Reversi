package model;

import model.enums.Player;
import model.pieces.Piece;
import model.pieces.PieceImplMonochrome;
import model.rules.InputRule;
import model.rules.Rule;
import model.structs.GameStatistics;
import model.structs.Move;
import model.structs.Point;
import model.structs.Rect;
import view.Painter;
import view.console.PixelImplConsole;
import view.console.View;
import view.console.Window;

/**
 * Represents a game board with all its state and rendering logic.
 *
 * <p>This class manages:
 * <ul>
 *   <li>Game state including pieces, players, and turns</li>
 *   <li>Board rendering and display updates</li>
 *   <li>Move validation and execution</li>
 *   <li>Score tracking and winner determination</li>
 * </ul>
 *
 * <p>The board uses a grid system with (1,1) as the top-left corner.
 */
public class Board{
    // Constants
    /** The answer to the Ultimate Question of Life, the Universe, and Everything */
    public static final int ULTIMATE_ANSWER = 42;
    /** Maximum allowed board size (width/height) */
    public static final int MAX_BOARD_SIZE = 10;
    /** Minimum allowed board size (width/height) */
    public static final int MIN_BOARD_SIZE = 4;

    // Game State
    private final Rule rule;
    private final GameStatistics statistics;

    // Game Display
    private final Painter painter;

    /**
     * Constructs a new game board.
     *
     * @param rule Game rules implementation
     * @param statistics Game statistics implementation
     * @throws IllegalArgumentException if parameters are invalid
     */
    public Board(Rule rule, Painter painter, GameStatistics statistics) {

        this.rule = rule;
        this.statistics = statistics;
        this.painter = painter;
        this.rule.getGameRule().initializeGrid(this.statistics); /* Initialize game grid according to rules */

        // Initial setup
        painter.updateGameStatistics(statistics, rule);
    }

    /**
     * Gets brief information about the board.
     *
     * @return String in format "ruleName <height>x<width>"
     */
    public String getBriefInformation() {
        return rule.getName() + " " + statistics.getHeight() + "x" + statistics.getWidth();
    }

    /**
     * Gets the game rule implementation.
     *
     * @return Current game rule
     */
    public Rule getRule() {
        return rule;
    }

    /**
     * Gets the white player's name.
     *
     * @return White player name
     */
    public String getWhitePlayerName() {
        return statistics.getWhitePlayerName();
    }
    /**
     * Gets the black player's name.
     *
     * @return Black player name
     */
    public String getBlackPlayerName() {
        return statistics.getBlackPlayerName();
    }

    /**
     * Gets the input rule implementation.
     *
     * @return Current input rule
     */
    public InputRule getInputRule() {
        return rule.getInputRule();
    }

    /**
     * Gets string representation of board state.
     *
     * @return String describing game state
     */
    public String toString() {
        return rule.getName() + " " + statistics.getHeight() + "x" + statistics.getWidth() + " " + switch(statistics.getWinner()) {
            case null -> "ongoing";
            case NONE -> "draw";
            case BLACK -> "black win";
            case WHITE -> "white win";
        };
    }

    /**
     * Checks if game is over.
     *
     * @return true if the game has ended, false otherwise
     */
    public boolean isGameOver() {
        return statistics.getWinner() != null;
    }

    /**
     * Gets the winner of the game.
     *
     * @return Winning player, or null if the game isn't over
     */
    public Player getWinner() { return statistics.getWinner(); }

    /**
     * Attempts to place a piece at the specified position.
     *
     * @param move Contains target position and player
     * @return true if the move was valid and executed, false otherwise
     */
    public boolean placePiece(Move move) {
        // Validate move coordinates
        if(!this.rule.getGameRule().placePieceValidationCheck(move, statistics)) {
            return false;
        }

        // Execute move
        this.rule.getGameRule().placePiece(move, statistics);

        // Switch players
        this.rule.getGameRule().nextPlayer(statistics);

        // Update display
        painter.updateGameStatistics(statistics, rule);

        return true;
    }

    /**
     * Renders the board to the display.
     */
    public void show() {
        painter.flush();
    }

}
