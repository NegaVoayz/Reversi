package model;

import model.enums.Player;
import model.rules.Rule;
import view.*;

public class Board{
    // Answer to the Ultimate Question of Life, the Universe, and Everything
    public static final int ULTIMATE_ANSWER = 42;

    private static final int MAX_BOARD_SIZE = 16;
    private static final int DEFAULT_BOARD_SIZE = 8;

    private final Rule rule;
    private final Piece[][] pieceGrid;
    private final Window window;
    private final View boardView;
    private final View statisticsView;
    private final int height;
    private final int width;
    private Player currentPlayer;
    private String whitePlayerName;
    private String blackPlayerName;
    private boolean isGameOver;
    private Player winner;

    public Board(int height, int width, Rule rule, Window window) {
        if(height == 0) {
            height = DEFAULT_BOARD_SIZE;
        }
        if(width == 0) {
            width = DEFAULT_BOARD_SIZE;
        }
        if(height < 3 || height > MAX_BOARD_SIZE ||
            width < 3 || width > MAX_BOARD_SIZE ) {
            throw new IllegalArgumentException("Invalid board size: too large or too small");
        }
        this.height = height;
        this.width = width;
        this.rule = rule;
        this.currentPlayer = Player.BLACK;
        this.pieceGrid = new Piece[height+2][width+2];
        this.rule.initializeGrid(pieceGrid);
        this.window = window;
        this.boardView = window.createView(new Rect(0, height+1, 0, width*2+1));
        if(this.boardView == null) { throw new IllegalArgumentException("Unable to draw board: Space occupied"); }
        this.statisticsView = window.createView(new Rect(0, height+1, width*2+1, width*2+ULTIMATE_ANSWER));
        if(this.statisticsView == null) { throw new IllegalArgumentException("Unable to draw statistics: Space occupied"); }
        initializeCanvas();
        updateBoard();
        showValidMoves();
    }

    /**
     * set player names
     * keep names below 32 letters
     * @param whitePlayerName the name of whom plays white
     * @param blackPlayerName the name of whom plays black
     * @return true if succeeded
     */
    public boolean setName(String whitePlayerName, String blackPlayerName) {
        if(whitePlayerName.length() > 32 || blackPlayerName.length() > 32) {
            return false;
        }
        this.whitePlayerName = whitePlayerName;
        this.blackPlayerName = blackPlayerName;
        displayPlayerInfo();
        return true;
    }

    public String getWhitePlayerName() {
        return whitePlayerName;
    }
    public String getBlackPlayerName() {
        return blackPlayerName;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public Player getWinner() { return winner; }

    /**
     * try place piece at the position given
     * @param point position
     * @return true if succeeded
     */
    public boolean placePiece(Point point) {
        if( point.x <= 0 || point.x > width ||
            point.y <= 0 || point.y > height ||
            !rule.placePieceValidationCheck(point, currentPlayer, pieceGrid)) {
            return false;
        }
        rule.placePiece(point, currentPlayer, pieceGrid);
        currentPlayer = rule.nextPlayer(currentPlayer, pieceGrid);
        updateBoard();
        if(rule.gameOverCheck(currentPlayer, pieceGrid)) {
            this.isGameOver = true;
            this.winner = rule.gameWonCheck(currentPlayer, pieceGrid);
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
     * @return true if succeed
     */
    public boolean show() {
        return window.forcePaint();
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
        Point boardPoint = new Point(0, 0);
        for(boardPoint.y = 1; boardPoint.y <= height; boardPoint.y++) {
            viewPoint.y++;
            viewPoint.x = 1;
            for(boardPoint.x = 1; boardPoint.x <= width; boardPoint.x++) {
                if(rule.placePieceValidationCheck(boardPoint, currentPlayer, pieceGrid)) {
                    boardView.setPixel(viewPoint, PieceImplReversi.VALID_MOVE);
                }
                viewPoint.x+=2;
            }
        }
    }

    /**
     * show player info and the current player while playing
     */
    private void displayPlayerInfo() {
        int align = (height-2)/2;
        String buf;
        buf = "Player[" + whitePlayerName + "] ";
        if(currentPlayer == Player.WHITE) {
            buf += ((PixelImplConsole)PieceImplReversi.WHITE_PIECE).get();
        }
        statisticsView.println(align, buf);

        buf = "Player[" + blackPlayerName + "] ";
        if(currentPlayer == Player.BLACK) {
            buf += ((PixelImplConsole)PieceImplReversi.BLACK_PIECE).get();
        }
        statisticsView.println(align+1, buf);
        
        buf = "Current Player: ";
        if(currentPlayer == Player.WHITE) {
            buf += "White";
        } else {
            buf += "Black";
        }
        statisticsView.println(align+2, buf);
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
        statisticsView.println(align+2, "");
    }
}
