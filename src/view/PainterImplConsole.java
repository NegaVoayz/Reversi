package view;

import model.Board;
import model.enums.Player;
import model.pieces.Piece;
import model.pieces.PieceImplMonochrome;
import model.rules.Rule;
import model.structs.GameStatistics;
import model.structs.Move;
import model.structs.Point;
import model.structs.Rect;
import view.console.PixelImplConsole;
import view.console.View;
import view.console.Window;

public class PainterImplConsole implements Painter {

    private final Window window;
    private final View boardView;
    private final View statisticsView;

    /**
     *
     * @param window Display a window for rendering
     * @param viewStart Starting coordinates for board display
     */
    protected PainterImplConsole(Window window, Point viewStart, int height, int width) {
        // Set up display views
        this.window = window;
        this.boardView = window.createView(new Rect(viewStart.y, viewStart.y+height+1, viewStart.x, viewStart.x+width*2+1));
        if(this.boardView == null) { throw new IllegalArgumentException("Unable to draw board: Space occupied"); }
        this.statisticsView = window.createView(new Rect(viewStart.y, viewStart.y+height+1, viewStart.x+width*2+1, viewStart.x+width*2+ Board.ULTIMATE_ANSWER));
        if(this.statisticsView == null) { throw new IllegalArgumentException("Unable to draw statistics: Space occupied"); }

        initializeCanvas(height, width);
    }

    /**
     */
    @Override
    public void updateGameStatistics(GameStatistics statistics, Rule rule) {
        updateBoard(statistics);
        if(statistics.getWinner() == null) {
            showValidMoves(statistics, rule);
            displayPlayerInfo(statistics,rule);
        } else {
            displayWinnerInfo(statistics,rule);
        }
        window.paint();
    }

    /**
     *
     */
    @Override
    public void flush() {
        window.paint();
    }


    /**
     * Initializes the board canvas with row/column labels.
     *
     * @param height height of the board
     * @param width width of the board
     */
    private void initializeCanvas(int height, int width) {
        Point point = new Point(0, 0);

        // Show column numbers (1-9 then a-z)
        for(point.y = 1; point.y <= height && point.y <= 9; point.y++) {
            boardView.setPixel(point, new PixelImplConsole((char)('0'+point.y)));
        }
        for(; point.y <= width; point.y++) {
            boardView.setPixel(point, new PixelImplConsole((char)('a'+point.y-10)));
        }

        // Show row letters (A-Z)
        point.y = 0;
        for(int j = 1; j <= width; j++) {
            point.x = 2*j-1;
            boardView.setPixel(point, new PixelImplConsole((char)('A'+j-1)));
        }
    }

    /**
     * Updates the display with current piece positions.
     *
     * @param statistics game statistics to be shown
     */
    private void updateBoard(GameStatistics statistics) {
        Point point = new Point(0, 0);
        for(int i = 1; i <= statistics.getHeight(); i++) {
            for(int j = 1; j <= statistics.getWidth(); j++) {
                point.set(2*j-1,i);
                boardView.setPixel(point, statistics.getPieceGrid()[i][j].getPixel());
            }
        }
    }

    /**
     * Displays current player information and scores.
     */
    private void displayPlayerInfo(GameStatistics statistics, Rule rule) {
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
     * The winning player: (WHITE, BLACK, or NONE for draw)
     *
     * @param statistics game statistics
     */
    private void displayWinnerInfo(GameStatistics statistics, Rule rule) {
        int align = (statistics.getHeight()+1-3)/2;
        switch(statistics.getWinner()) {
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

    /**
     * Highlights valid moves for the current player.
     */
    private void showValidMoves(GameStatistics statistics, Rule rule) {
        switch(rule.getGameRule().getGameType()) {
            case PLACE_PIECE -> showValidMovesForPlacingGame(statistics, rule);
            case MOVE_PIECE -> showValidMovesForMovingGame(statistics, rule);
        }
    }
    
    private void showValidMovesForPlacingGame(GameStatistics statistics, Rule rule) {
        Point viewPoint = new Point(0, 0);
        Piece piece = new PieceImplMonochrome();
        piece.setPlayer(statistics.getCurrentPlayer());
        Move move = new Move(new Point(0,0), new Point(0,0), piece);
        for(move.end.y = 1; move.end.y <= statistics.getHeight(); move.end.y++) {
            viewPoint.y++;
            viewPoint.x = 1;
            for(move.end.x = 1; move.end.x <= statistics.getWidth(); move.end.x++) {
                if(rule.getGameRule().placePieceValidationCheck(move, statistics)) {
                    boardView.setPixel(viewPoint, PieceImplMonochrome.VALID_MOVE);
                }
                viewPoint.x+=2;
            }
        }
    }

    private void showValidMovesForMovingGame(GameStatistics statistics, Rule rule) {
        return;
    }
}
