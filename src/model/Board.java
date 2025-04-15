package model;

import model.enums.Player;
import model.rules.InputRule;
import model.rules.Rule;
import model.structs.Move;
import model.structs.Point;
import model.structs.Rect;
import view.*;

public class Board{
    // Answer to the Ultimate Question of Life, the Universe, and Everything
    public static final int ULTIMATE_ANSWER = 42;
    public static final int MAX_BOARD_SIZE = 10;
    public static final int MIN_BOARD_SIZE = 4;

    private final Rule rule;
    private final Piece[][] pieceGrid;
    private final Window window;
    private final View boardView;
    private final View statisticsView;
    private final int height;
    private final int width;
    private final String whitePlayerName;
    private final String blackPlayerName;
    private Player currentPlayer;
    private Player winner;
    private int round;

    public Board(
            int height,
            int width,
            Rule rule,
            Window window,
            String whitePlayerName,
            String blackPlayerName) {
        if(height < MIN_BOARD_SIZE || height > MAX_BOARD_SIZE ||
            width < MIN_BOARD_SIZE || width > MAX_BOARD_SIZE ) {
            throw new IllegalArgumentException("Invalid board size: too large or too small");
        }
        this.height = height;
        this.width = width;
        this.round = 1;
        this.rule = rule;
        this.currentPlayer = Player.BLACK;
        this.pieceGrid = new Piece[height+2][width+2];
        this.rule.getGameRule().initializeGrid(pieceGrid);
        this.window = window;
        this.boardView = window.createView(new Rect(0, height+1, 0, width*2+1));
        if(this.boardView == null) { throw new IllegalArgumentException("Unable to draw board: Space occupied"); }
        this.statisticsView = window.createView(new Rect(0, height+1, width*2+1, width*2+ULTIMATE_ANSWER));
        if(this.statisticsView == null) { throw new IllegalArgumentException("Unable to draw statistics: Space occupied"); }
        if(whitePlayerName.length() > 32 || blackPlayerName.length() > 32) {
            throw new IllegalArgumentException("Unable to initialize name: too long");
        }
        this.whitePlayerName = whitePlayerName;
        this.blackPlayerName = blackPlayerName;
        initializeCanvas();
        updateBoard();
        displayPlayerInfo();
        showValidMoves();
    }

    public String getBriefInformation() {
        return rule.getName() + " " + height + "x" + width;
    }

    public Rule getRule() {
        return rule;
    }

    public String getWhitePlayerName() {
        return whitePlayerName;
    }
    public String getBlackPlayerName() {
        return blackPlayerName;
    }

    public InputRule getInputRule() {
        return rule.getInputRule();
    }

    public String toString() {
        return rule.getName() + " " + height + "x" + width + " " + switch(winner) {
            case null -> "ongoing";
            case NONE -> "draw";
            case BLACK -> "black win";
            case WHITE -> "white win";
        };
    }

    public boolean isGameOver() {
        return winner != null;
    }

    public Player getWinner() { return winner; }

    /**
     * try place piece at the position given
     * @param move position
     * @return true if succeeded
     */
    public boolean placePiece(Move move) {
        if( move.end.x <= 0 || move.end.x > width ||
                move.end.y <= 0 || move.end.y > height ||
            !this.rule.getGameRule().placePieceValidationCheck(move, currentPlayer, pieceGrid)) {
            return false;
        }
        this.rule.getGameRule().placePiece(move, currentPlayer, pieceGrid);
        currentPlayer = this.rule.getGameRule().nextPlayer(currentPlayer, pieceGrid);
        if(currentPlayer == Player.BLACK) {
            round++;
        }
        updateBoard();
        if(this.rule.getGameRule().gameOverCheck(currentPlayer, pieceGrid)) {
            this.winner = this.rule.getGameRule().gameWonCheck(currentPlayer, pieceGrid);
            displayWinnerInfo(winner);
        } else {
            showValidMoves();
            displayPlayerInfo();
        }
        return true;
    }


    /**
     * show edge scales
     */
    private void initializeCanvas() {
        Point point = new Point(0, 0);
        for(point.y = 1; point.y <= height && point.y <= 9; point.y++) {
            boardView.setPixel(point, new PixelImplConsole((char)('0'+point.y)));
        }
        for(; point.y <= height; point.y++) {
            boardView.setPixel(point, new PixelImplConsole((char)('a'+point.y-10)));
        }
        point.y = 0;
        for(int j = 1; j <= width; j++) {
            point.x = 2*j-1;
            boardView.setPixel(point, new PixelImplConsole((char)('A'+j-1)));
        }
    }

    /**
     * show the board on the screen
     */
    public void show() {
        window.paint();
    }

    /**
     * update pieces on canvas
     */
    private void updateBoard() {
        Point point = new Point(0, 0);
        for(int i = 1; i <= height; i++) {
            for(int j = 1; j <= width; j++) {
                point.x = 2*j-1;
                point.y = i;
                boardView.setPixel(point, pieceGrid[i][j].getPixel());
            }
        }
    }

    /**
     * get and show valid moves for current player
     */
    private void showValidMoves() {
        Point viewPoint = new Point(0, 0);
        Move move = new Move(new Point(0,0), new Point(0,0), currentPlayer);
        for(move.end.y = 1; move.end.y <= height; move.end.y++) {
            viewPoint.y++;
            viewPoint.x = 1;
            for(move.end.x = 1; move.end.x <= width; move.end.x++) {
                if(this.rule.getGameRule().placePieceValidationCheck(move, currentPlayer, pieceGrid)) {
                    boardView.setPixel(viewPoint, PieceImplMonochrome.VALID_MOVE);
                }
                viewPoint.x+=2;
            }
        }
    }

    /**
     * show player info and the current player while playing
     */
    private void displayPlayerInfo() {
        int align;
        if(rule.showRound()) {
            align = (height-3)/2;
        } else {
            align = (height-2)/2;
        }

        {
            StringBuilder buf = new StringBuilder("Player[" + whitePlayerName + "] ");

            if(currentPlayer == Player.WHITE) {
                buf.append(((PixelImplConsole) PieceImplMonochrome.WHITE_PIECE).get());
            } else {
                buf.append(" ");
            }

            buf.append(" ");

            {
                int whiteScore = rule.getGameRule().getWhiteScore(pieceGrid);
                if(whiteScore != -1) {
                    buf.append(whiteScore);
                }
            }

            statisticsView.println(align, buf.toString());
        }

        {
            StringBuilder buf = new StringBuilder("Player[" + blackPlayerName + "] ");

            if(currentPlayer == Player.BLACK) {
                buf.append(((PixelImplConsole) PieceImplMonochrome.BLACK_PIECE).get());
            } else {
                buf.append(" ");
            }

            buf.append(" ");

            {
                int blackScore = rule.getGameRule().getBlackScore(pieceGrid);
                if(blackScore != -1) {
                    buf.append(blackScore);
                }
            }

            statisticsView.println(align+1, buf.toString());
        }

        statisticsView.println(align+2, "Current Player: " + currentPlayer.name().toLowerCase());

        if(rule.showRound()) {
            statisticsView.println(align+3, "Current Round: " + round);
        }
    }

    /**
     * show who is the winner
     * @param type the winner
     */
    private void displayWinnerInfo(Player type) {
        int align = (height+1-3)/2;
        switch(type) {
            case Player.WHITE:
                statisticsView.println(align, "White Wins");
                statisticsView.println(align+1, "Good game " + whitePlayerName);
                break;
            case Player.BLACK:
                statisticsView.println(align, "Black Wins");
                statisticsView.println(align+1, "Good game " + blackPlayerName);
                break;
            case Player.NONE:
                statisticsView.println(align, "Draw");
                statisticsView.println(align+1, "Cool");
                break;
        }
        statisticsView.println(align+2, "White " + rule.getGameRule().getWhiteScore(pieceGrid)
                + ": Black " + rule.getGameRule().getBlackScore(pieceGrid));
    }
}
