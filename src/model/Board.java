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
import view.*;

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

    // Game state
    private final Rule rule;
    private final GameStatistics statistics;
    private final Window window;
    private final View boardView;
    private final View statisticsView;

    /**
     * Constructs a new game board.
     *
     * @param height Number of rows (must be between MIN/MAX_BOARD_SIZE)
     * @param width Number of columns (must be between MIN/MAX_BOARD_SIZE)
     * @param rule Game rules implementation
     * @param window Display a window for rendering
     * @param viewStart Starting coordinates for board display
     * @param whitePlayerName Name of white player (max 32 chars)
     * @param blackPlayerName Name of black player (max 32 chars)
     * @throws IllegalArgumentException if parameters are invalid
     */
    public Board(
            int height,
            int width,
            Rule rule,
            Window window,
            Point viewStart,
            String whitePlayerName,
            String blackPlayerName) {

        this.rule = rule;
        this.statistics = new GameStatistics(height, width, whitePlayerName, blackPlayerName);
        this.rule.getGameRule().initializeGrid(this.statistics); /* Initialize game grid according to rules */

        // Set up display views
        this.window = window;
        this.boardView = window.createView(new Rect(viewStart.y, viewStart.y+height+1, viewStart.x, viewStart.x+width*2+1));
        if(this.boardView == null) { throw new IllegalArgumentException("Unable to draw board: Space occupied"); }
        this.statisticsView = window.createView(new Rect(viewStart.y, viewStart.y+height+1, viewStart.x+width*2+1, viewStart.x+width*2+ULTIMATE_ANSWER));
        if(this.statisticsView == null) { throw new IllegalArgumentException("Unable to draw statistics: Space occupied"); }

        // Initial setup
        initializeCanvas();
        updateBoard();
        displayPlayerInfo();
        showValidMoves();
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
        updateBoard();

        // Check for game over
        if(this.rule.getGameRule().gameOverCheck(statistics)) {
            displayWinnerInfo(statistics.getWinner());
        } else {
            showValidMoves();
            displayPlayerInfo();
        }
        return true;
    }

    /**
     * Renders the board to the display.
     */
    public void show() {
        window.paint();
    }

    /**
     * Initializes the board canvas with row/column labels.
     */
    private void initializeCanvas() {
        Point point = new Point(0, 0);

        // Show column numbers (1-9 then a-z)
        for(point.y = 1; point.y <= statistics.getHeight() && point.y <= 9; point.y++) {
            boardView.setPixel(point, new PixelImplConsole((char)('0'+point.y)));
        }
        for(; point.y <= statistics.getHeight(); point.y++) {
            boardView.setPixel(point, new PixelImplConsole((char)('a'+point.y-10)));
        }

        // Show row letters (A-Z)
        point.y = 0;
        for(int j = 1; j <= statistics.getWidth(); j++) {
            point.x = 2*j-1;
            boardView.setPixel(point, new PixelImplConsole((char)('A'+j-1)));
        }
    }

    /**
     * Updates the display with current piece positions.
     */
    private void updateBoard() {
        Point point = new Point(0, 0);
        for(int i = 1; i <= statistics.getHeight(); i++) {
            for(int j = 1; j <= statistics.getWidth(); j++) {
                point.set(2*j-1,i);
                boardView.setPixel(point, statistics.getPieceGrid()[i][j].getPixel());
            }
        }
    }

    /**
     * Highlights valid moves for current player.
     */
    private void showValidMoves() {
        Point viewPoint = new Point(0, 0);
        Piece piece = new PieceImplMonochrome();
        piece.setPlayer(statistics.getCurrentPlayer());
        Move move = new Move(new Point(0,0), new Point(0,0), piece);
        for(move.end.y = 1; move.end.y <= statistics.getHeight(); move.end.y++) {
            viewPoint.y++;
            viewPoint.x = 1;
            for(move.end.x = 1; move.end.x <= statistics.getWidth(); move.end.x++) {
                if(this.rule.getGameRule().placePieceValidationCheck(move, statistics)) {
                    boardView.setPixel(viewPoint, PieceImplMonochrome.VALID_MOVE);
                }
                viewPoint.x+=2;
            }
        }
    }

    /**
     * Displays current player information and scores.
     */
    private void displayPlayerInfo() {
        // Calculate vertical alignment
        int align;
        if(rule.showRound()) {
            align = (statistics.getHeight()-3)/2;
        } else {
            align = (statistics.getHeight()-2)/2;
        }

        // White player info
        {
            StringBuilder buf = new StringBuilder("Player[" + statistics.getWhitePlayerName() + "] ");

            if(statistics.getCurrentPlayer() == Player.WHITE) {
                buf.append(((PixelImplConsole) PieceImplMonochrome.WHITE_PIECE).get());
            } else {
                buf.append(" ");
            }

            buf.append(" ");

            {
                int whiteScore = rule.getGameRule().getWhiteScore(statistics);
                if(whiteScore != -1) {
                    buf.append(whiteScore);
                }
            }

            statisticsView.println(align, buf.toString());
        }

        // Black player info
        {
            StringBuilder buf = new StringBuilder("Player[" + statistics.getBlackPlayerName() + "] ");

            if(statistics.getCurrentPlayer() == Player.BLACK) {
                buf.append(((PixelImplConsole) PieceImplMonochrome.BLACK_PIECE).get());
            } else {
                buf.append(" ");
            }

            buf.append(" ");

            {
                int blackScore = rule.getGameRule().getBlackScore(statistics);
                if(blackScore != -1) {
                    buf.append(blackScore);
                }
            }

            statisticsView.println(align+1, buf.toString());
        }

        statisticsView.println(align+2, "Current Player: " + statistics.getCurrentPlayer().name().toLowerCase());

        if(rule.showRound()) {
            statisticsView.println(align+3, "Current Round: " + statistics.getRound());
        }
    }

    /**
     * Displays game winner information.
     *
     * @param type The winning player (WHITE, BLACK, or NONE for draw)
     */
    private void displayWinnerInfo(Player type) {
        int align = (statistics.getHeight()+1-3)/2;
        switch(type) {
            case Player.WHITE:
                statisticsView.println(align, "White Wins");
                statisticsView.println(align+1, "Good game " + statistics.getWhitePlayerName());
                break;
            case Player.BLACK:
                statisticsView.println(align, "Black Wins");
                statisticsView.println(align+1, "Good game " + statistics.getBlackPlayerName());
                break;
            case Player.NONE:
                statisticsView.println(align, "Draw");
                statisticsView.println(align+1, "Cool");
                break;
        }
        statisticsView.println(align+2, "White " + rule.getGameRule().getWhiteScore(statistics)
                + ": Black " + rule.getGameRule().getBlackScore(statistics));
    }
}
