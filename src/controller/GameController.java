package controller;

import model.Board;
import model.factories.BoardFactory;
import model.rules.RuleImplGomoku;
import model.rules.RuleImplLandfill;
import model.rules.RuleImplReversi;
import model.structs.Move;
import model.enums.Player;
import model.structs.Rect;
import view.Screen;
import view.View;
import view.Window;

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
    private final Window boardsWindow;
    private final View boardsView;

    public GameController(ArrayList<Board> boards, Screen screen) {
        this.boards = boards;
        this.gameOverCount = 0;
        this.currentBoard  = 0;
        this.whiteWinCount = 0;
        this.blackWinCount = 0;
        this.whitePlayerName = boards.getFirst().getWhitePlayerName();
        this.blackPlayerName = boards.getFirst().getBlackPlayerName();
        this.boardsSelected = new LinkedList<>();
        this.boardsWindow = screen.createWindow(new Rect(0, 10, 80, 120));
        this.boardsView = boardsWindow.createView(new Rect(0, 10, 0, 32));
        boardFactory = BoardFactory
                .create()
                .setWhitePlayerName(whitePlayerName)
                .setBlackPlayerName(blackPlayerName)
                .setScreen(screen)
                .useDefaultBoardSizeCol()
                .useDefaultBoardSizeRow()
                .useDefaultVerticalAlign()
                .useDefaultHorizontalAlign();
        setBoardsWindow();
        showBoard();
    }

    public void showBoard() {
        boards.get(currentBoard).show();
        boardsView.paint();
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

    protected boolean setCurrentBoard(String input) {
        int currentBoard;
        try {
            currentBoard = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return false;
        }
        if(currentBoard <= 0 || currentBoard > boards.size()) {
            System.out.println("Invalid board number: " + currentBoard);
            return false;
        }
        this.currentBoard = currentBoard - 1;
        setBoardsWindow();
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
        switch(tokens[0].toLowerCase()) {
            case RuleImplReversi.name:
                boardFactory.setRule(new RuleImplReversi());
                break;
            case RuleImplLandfill.name:
                boardFactory.setRule(new RuleImplLandfill());
                break;
            case RuleImplGomoku.name:
                boardFactory.setRule(new RuleImplGomoku());
                break;
            default:
                System.out.println("No rule named " + tokens[0] + ". Try 'reversi' or 'peace'.");
                boardFactory.setRule(null);
        }
        if(tokens.length == 1) {
            return this;
        }
        if(tokens.length == 2) {
            boardFactory
                .setBoardSizeCol(Integer.parseInt(tokens[1]))
                .setBoardSizeRow(Integer.parseInt(tokens[1]));
            return this;
        }
        boardFactory
                .setBoardSizeCol(Integer.parseInt(tokens[1]))
                .setBoardSizeRow(Integer.parseInt(tokens[2]));
        return this;
    }

    protected boolean createBoard() {
        if(!boardFactory.isLegalSetting()) {
            System.out.println("Invalid board settings. And, board size must be between " + Board.MIN_BOARD_SIZE + " and " + Board.MAX_BOARD_SIZE);
            return false;
        }
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
            System.out.println("Board " + (i + 1) + ": " + boards.get(i));
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
            if(boards.get(tempBoardNO).getRule().toString().equals(ruleName)) {
                boardsSelected.add(tempBoardNO);
            }
        }
    }

    /**
     * @return True if the position is valid and the piece is placed successfully
     */
    private boolean placePiece(Move move) {
        if( move == null ) {
            return false;
        }
        if( boards.get(currentBoard).isGameOver() ) {
            System.out.println("Game Over!");
            return false;
        }
        if( !boards.get(currentBoard).placePiece(move) ) {
            System.out.println("Invalid Move");
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

    private void setBoardsWindow() {
        int startY = 1;
        int startBoard = currentBoard-3;
        if(startBoard < 0) {
            startY = 1-startBoard;
            startBoard = 0;
        }
        boardsView.clearView();
        if(currentBoard-3 > 0) {
            boardsView.println(0,"    ...");
        }
        if(currentBoard+5 < boards.size()) {
            boardsView.println(9,"    ...");
        }
        for(int i = startY, j = startBoard; i < 9 && j < boards.size(); i++, j++) {
            if(j == currentBoard) {
                boardsView.println(i, "--> Board " + (j + 1) + ": " + boards.get(j).getBriefInformation());
            } else {
                boardsView.println(i, "    Board " + (j + 1) + ": " + boards.get(j).getBriefInformation());
            }
        }
    }
}
