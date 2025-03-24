package controller;

import model.Board;
import model.Point;
import model.enums.Player;

import java.util.ArrayList;

public class GameController {
    private final ArrayList<Board> boards;
    private final String whitePlayerName;
    private final String blackPlayerName;
    private int currentBoard;
    private int gameOverCount;
    private int whiteWinCount;

    public GameController(ArrayList<Board> boards) {
        this.boards = boards;
        this.gameOverCount = 0;
        this.currentBoard = 0;
        this.whiteWinCount = 0;
        this.whitePlayerName = boards.getFirst().getWhitePlayerName();
        this.blackPlayerName = boards.getFirst().getBlackPlayerName();
        boards.getFirst().show();
    }

    public void showBoard() {
        boards.get(currentBoard).show();
    }

    public boolean isAllGameOver() {
        return gameOverCount == boards.size();
    }

    protected int getWhiteWinCount() {
        return whiteWinCount;
    }

    protected int getBlackWinCount() {
        return boards.size() - whiteWinCount;
    }

    protected String getWhitePlayerName() {
        return whitePlayerName;
    }

    protected String getBlackPlayerName() {
        return blackPlayerName;
    }

    protected boolean setCurrentBoard(int currentBoard) {
        if(currentBoard <= 0 || currentBoard > boards.size()) {
            return false;
        }
        this.currentBoard = currentBoard - 1;
        showBoard();
        return true;
    }

    /**
     * @return True if the position is valid and the piece is placed successfully
     */
    protected boolean placePiece(Point point) {
        if( boards.get(currentBoard).isGameOver() ) {
            return false;
        }
        if( !boards.get(currentBoard).placePiece(point) ) {
            return false;
        }
        showBoard();
        if( boards.get(currentBoard).isGameOver() ) {
            if(boards.get(currentBoard).getWinner() == Player.WHITE) {
                whiteWinCount++;
            }
            gameOverCount++;
        }
        return true;
    }
}
