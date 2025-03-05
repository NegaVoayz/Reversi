package model;

import view.Canvas;
import view.Pixel;

public class Board{
    public enum Player{
        WHITE,
        NONE,
        BLACK,
    }
    
    private static final int MAX_BOARD_SIZE = 16;
    private static final int DEFAULT_BOARD_SIZE = 8;

    private Piece pieceGrid[][];
    private Canvas canvas;
    private int height;
    private int width;
    private Player currentPlayer;
    private String whitePlayerName;
    private String blackPlayerName;
    private boolean isGameOver;

    public Board(int height, int width) {
        if(height < 0 || height > MAX_BOARD_SIZE ||
            width < 0 || width > MAX_BOARD_SIZE ) {
            throw new IllegalArgumentException("Invalid board size: too large or negative");
        }
        if(height == 0) {
            height = DEFAULT_BOARD_SIZE;
        }
        if(width == 0) {
            width = DEFAULT_BOARD_SIZE;
        }
        this.height = height;
        this.width = width;
        this.currentPlayer = Player.BLACK;
        pieceGrid = new Piece[height+2][width+2];
        canvas = new Canvas(height+2, width+2);
        initializeCanvas();
        initializeGrid();

        updateBoard();
        // well, it's possible some special pieceGrid size
        //   could cause an immediate endgame
        if(!getValidMoves()) {
            switchPlayer();
            this.isGameOver = !getValidMoves();
        } else {
            this.isGameOver = false;
        }
    }

    public void setName(String whitePlayerName, String blackPlayerName) {
        this.whitePlayerName = whitePlayerName;
        this.blackPlayerName = blackPlayerName;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    /**
     * try place piece at the position given
     * @param col column position
     * @param row row position
     */
    public boolean placePiece(int col, int row) {
        if( col <= 0 || col > width ||
            row <= 0 || row > height ||
            !pieceGrid[row][col].isValid(getCurrentPlayerPieceType())) {
            return false;
        }
        pieceGrid[row][col].placePiece(getCurrentPlayerPieceType());
        switchPlayer();
        updateBoard();
        if(!getValidMoves()) {
            switchPlayer();
            this.isGameOver = !getValidMoves();
        }
        return true;
    }

    /**
     * paint game window
     */
    public void paint() {
        canvas.paint(true);
        if(isGameOver) {
            displayWinnerInfo(getWinner());
        } else {
            displayPlayerInfo();
        }
    }

    /**
     * allocate pieceGrid and set the start pieces
     */
    private void initializeGrid() {
        for(int i = 0; i < height+2; i++) {
            for(int j = 0; j < width+2; j++) {
                pieceGrid[i][j] = new Piece(j, i, pieceGrid);
            }
        }

        pieceGrid[height/2][width/2].setType(Piece.Type.WHITE);
        pieceGrid[height/2][width/2+1].setType(Piece.Type.BLACK);
        pieceGrid[height/2+1][width/2].setType(Piece.Type.BLACK);
        pieceGrid[height/2+1][width/2+1].setType(Piece.Type.WHITE);
    }

    /**
     * show edge scales
     */
    private void initializeCanvas() {
        for(int i = 1; i <= height && i <= 9; i++) {
            canvas.setPixel(0, i, new Pixel((char)(48+i)));
        }
        for(int i = 10; i <= height; i++) {
            canvas.setPixel(0, i, new Pixel((char)(87+i)));
        }
        for(int j = 1; j <= width; j++) {
            canvas.setPixel(j, 0, new Pixel((char)(64+j)));
        }
    }

    /**
     * switch player when his turn ends
     */
    private void switchPlayer(){
        if(currentPlayer == Player.WHITE) {
            currentPlayer = Player.BLACK;
        } else {
            currentPlayer = Player.WHITE;
        }
        return;
    }

    /**
     * get current player's piece color
     * actually convert player type to piece type
     * @return piece color
     */
    private Piece.Type getCurrentPlayerPieceType(){
        if(currentPlayer == Player.WHITE) {
            return Piece.Type.WHITE;
        } else {
            return Piece.Type.BLACK;
        }
    }

    /**
     * update pieces on canvas
     */
    private void updateBoard() {
        for(int i = 1; i <= height; i++) {
            for(int j = 1; j <= width; j++) {
                canvas.setPixel(j, i, pieceGrid[i][j].getPixel());
            }
        }
    }

    /**
     * get and show valid moves for current player
     * @return true when there are any moves valid
     */
    private boolean getValidMoves() {
        boolean movable = false;
        Piece.Type type;
        type = getCurrentPlayerPieceType();

        for(int i = 1; i <= height; i++) {
            for(int j = 1; j <= width; j++) {
                if(pieceGrid[i][j].isValid(type)) {
                    movable = true;
                    canvas.setPixel(j, i, new Pixel(Piece.VALID_MOVE));
                }
            }
        }

        return movable;
    }

    /**
     * show player info and the current player while playing
     */
    private void displayPlayerInfo() {
        System.out.print("White Player: " + whitePlayerName + " ");
        if(currentPlayer == Player.WHITE) {
            System.out.print(Piece.WHITE_PIECE);
        }
        System.out.println();

        System.out.print("Black Player: " + blackPlayerName + " ");
        if(currentPlayer == Player.BLACK) {
            System.out.print(Piece.BLACK_PIECE);
        }
        System.out.println();
        
        System.out.print("Current Player:");
        if(currentPlayer == Player.WHITE) {
            System.out.print("White");
        } else {
            System.out.print("Black");
        }
        System.out.println();
    }

    /**
     * show who is the winner
     * @param type the winner
     */
    private void displayWinnerInfo(Player type) {
        switch(type) {
            case Player.WHITE:
                System.out.println("White Wins\nCongratulations! " + whitePlayerName);
                break;
            case Player.BLACK:
                System.out.println("Black Wins\nCongratulations! " + blackPlayerName);
                break;
            case Player.NONE:
                System.out.println("Draw\nCool");
                break;
        }
    }

    /**
     * count pieces and return the winner
     * @return the winner
     */
    private Player getWinner() {
        int whitePieceCount = 0;
        int blackPieceCount = 0;
        for(int i = 1; i <= height; i++) {
            for(int j = 1; j <= width; j++) {
                switch (pieceGrid[i][j].getType()) {
                    case Piece.Type.WHITE:
                        whitePieceCount++;
                        break;
                    case Piece.Type.BLACK:
                        blackPieceCount++;
                        break;
                    default:
                        break;
                }
            }
        }

        if(whitePieceCount > blackPieceCount ) {
            return Player.WHITE;
        }
        if(whitePieceCount == blackPieceCount ) {
            return Player.NONE;// draw
        }
        if(whitePieceCount < blackPieceCount ) {
            return Player.BLACK;
        }
        // to comfort vs code
        throw new IllegalStateException("How the hell do you come here?");
    }
}
