package controller;

import model.Board;
import model.factories.BoardFactory;
import model.rules.InputRule;
import model.rules.Rule;
import model.rules.RuleImplLandfill;
import model.rules.RuleImplReversi;
import model.structs.Move;
import model.structs.Point;
import model.enums.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class GameController {
    private final ArrayList<Board> boards;
    private final String whitePlayerName;
    private final String blackPlayerName;
    private int currentBoard;
    private int gameOverCount;
    private int whiteWinCount;
    private int blackWinCount;
    private final BoardFactory boardFactory;

    public GameController(ArrayList<Board> boards) {
        this.boards = boards;
        this.gameOverCount = 0;
        this.currentBoard  = 0;
        this.whiteWinCount = 0;
        this.blackWinCount = 0;
        this.whitePlayerName = boards.getFirst().getWhitePlayerName();
        this.blackPlayerName = boards.getFirst().getBlackPlayerName();
        this.boardsSelected = new LinkedList<>();
        boardFactory = BoardFactory
                .create()
                .setWhitePlayerName(whitePlayerName)
                .setBlackPlayerName(blackPlayerName)
                .useDefaultBoardSizeCol()
                .useDefaultBoardSizeRow();
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
        return blackWinCount;
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

    private Move tempMove;
    protected GameController parseMove(String input) {
        tempMove = boards.get(currentBoard).getInputRule().ParseInput(input);
        return this;
    }

    protected boolean placePiece() {
        if(tempMove == null) {
            return false;
        }
        return placePiece(tempMove);
    }


    protected GameController parseCreate(String input) {
        String[] tokens = input.split("\\s+");
        tokens[0] = tokens[0].toLowerCase();
        if(tokens[0].compareToIgnoreCase("reversi") == 0) {
            boardFactory.setRule(new RuleImplReversi());
        } else {
            boardFactory.setRule(new RuleImplLandfill());
        }
        if(tokens.length == 1) {
            return this;
        }
        boardFactory
                .setBoardSizeCol(Integer.parseInt(tokens[1]))
                .setBoardSizeRow(Integer.parseInt(tokens[2]));
        return this;
    }

    protected boolean createBoard() {
        boards.add(boardFactory.createBoard());
        return this.selectBoards("").listBoards();
    }

    private final Queue<Integer> boardsSelected;
    protected GameController selectBoards(String selector) {
        if(selector.isEmpty()) {
            selectAllBoards();
            return this;
        }
        boardsSelected.clear();
        String[] tokens = selector.split("\\s+");
        if(tokens[0].compareToIgnoreCase("current") == 0) {
            boardsSelected.add(currentBoard);
            return this;
        }
        selectAllBoards();
        selectBoardsByRuleName(tokens[0].toLowerCase());
        return this;
    }

    protected boolean listBoards() {
        if(boardsSelected.isEmpty()) {
            System.out.println("No boards meet the requirements");
            return true;
        }
        do {
            int i = boardsSelected.poll();
            System.out.println("Board " + (i + 1) + ": " + boards.get(i).getRuleName());
        } while (!boardsSelected.isEmpty());
        return true;
    }

    private  void selectAllBoards() {
        for(int i = 0; i < boards.size(); i++) {
            boardsSelected.add(i);
        }
    }

    private void selectBoardsByRuleName(String ruleName) {
        for(int i = boardsSelected.size(); i > 0; i--) {
            int tempBoardNO = boardsSelected.poll();
            if(boards.get(tempBoardNO).getRuleName().equals(ruleName)) {
                boardsSelected.add(tempBoardNO);
            }
        }
    }

    /**
     * @return True if the position is valid and the piece is placed successfully
     */
    private boolean placePiece(Move move) {
        if( boards.get(currentBoard).isGameOver() ) {
            return false;
        }
        if( !boards.get(currentBoard).placePiece(move) ) {
            return false;
        }
        showBoard();
        if( boards.get(currentBoard).isGameOver() ) {
            if(boards.get(currentBoard).getWinner() == Player.WHITE) {
                whiteWinCount++;
            } else if(boards.get(currentBoard).getWinner() == Player.BLACK) {
                blackWinCount++;
            }
            gameOverCount++;
        }
        return true;
    }
}
