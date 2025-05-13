package controller;

import model.Board;
import model.factories.BoardFactory;
import model.rules.RuleImplGomoku;
import model.rules.RuleImplLandfill;
import model.rules.RuleImplReversi;
import model.structs.Move;
import model.enums.Player;
import model.structs.Rect;
import view.Displayer;
import view.console.Screen;
import view.console.View;
import view.console.Window;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * GameController: Controlling multi-board states and interaction.
 *
 * <p>Main Functions:
 * <ul>
 *     <li>Handle the Switch and Display of multiple Board Entities</li>
 *     <li>Parse and Execute players' operations on board.</li>
 *     <li>Create new boards during the game.</li>
 *     <li>Track the scores.</li>
 *     <li>Provide Game List View</li>
 * </ul>
 */
public class GameController {

    // All games
    private final ArrayList<Board> boards;

    // Current active board index
    private int currentBoardIdx;

    // Game Result Statistics
    private int gameOverCount;
    private int whiteWinCount;
    private int blackWinCount;

    private final String whitePlayerName;
    private final String blackPlayerName;

    private final BoardFactory boardFactory;

    // Display
    private final Displayer displayer;


    /**
     * Initialize GameController
     *
     * @param boards initial game set
     * @param displayer game screen for display
     */
    public GameController(ArrayList<Board> boards, Displayer displayer) {
        this.boards = boards;
        this.gameOverCount = 0;
        this.whiteWinCount = 0;
        this.blackWinCount = 0;
        this.currentBoardIdx = 0;
        this.whitePlayerName = boards.getFirst().getWhitePlayerName();
        this.blackPlayerName = boards.getFirst().getBlackPlayerName();
        this.boardsSelected = new LinkedList<>();
        this.displayer = displayer;
        boardFactory = BoardFactory
                .create()
                .setWhitePlayerName(whitePlayerName)
                .setBlackPlayerName(blackPlayerName)
                .setDisplayer(displayer)
                .useDefaultBoardSizeCol()
                .useDefaultBoardSizeRow()
                .useDefaultVerticalAlign()
                .useDefaultHorizontalAlign();
        displayer.updateBoards(boards, currentBoardIdx);
        showBoard();
    }

    public void showBoard() {
        boards.get(currentBoardIdx).show();
        displayer.display();
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

    /**
     * switch board by input
     *
     * @param input board No
     * @return true if succeeded
     */
    protected boolean setCurrentBoard(String input) {
        int newBoardNo;
        try {
            newBoardNo = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input: \""+input+"\" is not a number");
            return false;
        }
        if(newBoardNo <= 0 || newBoardNo > boards.size()) {
            System.out.println("Invalid board number: " + newBoardNo);
            return false;
        }
        this.currentBoardIdx = newBoardNo - 1;
        displayer.updateBoards(boards, currentBoardIdx);
        showBoard();
        return true;
    }

    private Move tempMove;
    /**
     * parse players' move by current game rule
     *
     * @param input player's command
     * @return this entity for method chain
     */
    protected GameController parseMove(String input) {
        tempMove = boards.get(currentBoardIdx).getInputRule().ParseInput(input);
        return this;
    }

    /**
     * place piece by last parsed move
     *
     * @return true if succeeded
     */
    protected boolean placePiece() {
        if(tempMove == null) {
            return false;
        }
        return placePiece(tempMove);
    }

    /**
     * parse players' creating board operation
     *
     * @param input command "[game type] ([width] [height])"
     * @return this entity for method chain
     * @throws IllegalArgumentException when input is illegal
     */
    protected GameController parseCreate(String input) throws IllegalArgumentException {
        String[] tokens = input.split("\\s+");

        // parse the rule type
        switch(tokens[0].toLowerCase()) {
            case RuleImplReversi.name:
                boardFactory.setRule(RuleImplReversi.getRule());
                break;
            case RuleImplLandfill.name:
                boardFactory.setRule(RuleImplLandfill.getRule());
                break;
            case RuleImplGomoku.name:
                boardFactory.setRule(RuleImplGomoku.getRule());
                break;
            default:
                throw new IllegalArgumentException("No rule named " + tokens[0] + ". Try 'reversi' or 'peace'.");
        }

        // if size is not specified
        if(tokens.length == 1) {
            boardFactory
                    .useDefaultBoardSizeCol()
                    .useDefaultBoardSizeRow();
            return this;
        }

        // if only one size specified, take as a square board
        if(tokens.length == 2) {
            try {
                boardFactory
                        .setBoardSizeCol(Integer.parseInt(tokens[1]))
                        .setBoardSizeRow(Integer.parseInt(tokens[1]));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid input: \""+tokens[1]+"\" is not a number.");
            }
            return this;
        }

        // if two sizes specified, set.
        try {
            boardFactory
                    .setBoardSizeCol(Integer.parseInt(tokens[1]))
                    .setBoardSizeRow(Integer.parseInt(tokens[2]));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid input: \""+tokens[1]+"\" or \""+tokens[2]+"\" is not a number");
        }

        // check size validity
        if(!boardFactory.isLegalSetting()) {
            throw new IllegalArgumentException("Invalid board size.\n" +
                    "Board size must be between " + Board.MIN_BOARD_SIZE + " and " + Board.MAX_BOARD_SIZE);
        }

        return this;
    }

    /**
     * create board from last parsed input
     *
     * @return true if succeeded
     */
    protected boolean createBoard() {
        boards.add(boardFactory.createBoard());
        displayer.updateBoards(boards, currentBoardIdx);
        showBoard();
        return true;
    }

    private final Queue<Integer> boardsSelected;

    /**
     * Select boards by selectors
     *
     * @param selector select rules
     * @return this entity for method chain
     */
    protected GameController selectBoards(String selector) {
        if(selector.isEmpty()) {
            selectAllBoards();
            return this;
        }
        boardsSelected.clear();
        String[] tokens = selector.split("\\s+");
        if(tokens[0].compareToIgnoreCase("current") == 0) {
            boardsSelected.add(currentBoardIdx);
            return this;
        }
        selectAllBoards();
        selectBoardsByRuleName(tokens[0].toLowerCase());
        return this;
    }

    /**
     * list the selected boards
     *
     * @return true if succeeded
     */
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

    // add all boards into select queue
    private  void selectAllBoards() {
        for(int i = 0; i < boards.size(); i++) {
            boardsSelected.add(i);
        }
    }

    // pick the boards matches the rule name
    private void selectBoardsByRuleName(String ruleName) {
        while(!boardsSelected.isEmpty()) {
            int tempBoardNO = boardsSelected.poll();
            if(boards.get(tempBoardNO).getRule().getName().equals(ruleName)) {
                boardsSelected.add(tempBoardNO);
            }
        }
    }

    /**
     * place piece by move, interacts with Board
     *
     * @return True if the position is valid and the piece is placed successfully
     */
    private boolean placePiece(Move move) {
        if( move == null ) {
            return false;
        }

        if( boards.get(currentBoardIdx).isGameOver() ) {
            System.out.println("Game Over!");
            return false;
        }

        if( !boards.get(currentBoardIdx).placePiece(move) ) {
            System.out.println("Invalid Move");
            return false;
        }

        // the game over process
        if(boards.get(currentBoardIdx).isGameOver() ) {
            if(boards.get(currentBoardIdx).getWinner() == Player.WHITE) {
                whiteWinCount++;
            } else if(boards.get(currentBoardIdx).getWinner() == Player.BLACK) {
                blackWinCount++;
            }
            gameOverCount++;
            displayer.updateBoards(boards, currentBoardIdx);
        }

        // update view since the board has changed.
        showBoard();

        return true;
    }
}
