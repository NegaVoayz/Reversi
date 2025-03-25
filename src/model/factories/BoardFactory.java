package model.factories;

import model.Board;
import model.rules.Rule;
import model.rules.RuleImplReversi;
import view.WindowImplConsole;

public class BoardFactory {

    private String whitePlayerName;
    private String blackPlayerName;
    private int boardSizeCol;
    private int boardSizeRow;
    private Rule rule;


    private BoardFactory() {
        whitePlayerName = "";
        blackPlayerName = "";
        boardSizeCol = 8;
        boardSizeRow = 8;
        rule = new RuleImplReversi();
    }

    public BoardFactory setWhitePlayerName(String whitePlayerName) {
        this.whitePlayerName = whitePlayerName;
        return this;
    }

    public BoardFactory setBlackPlayerName(String blackPlayerName) {
        this.blackPlayerName = blackPlayerName;
        return this;
    }

    public BoardFactory setBoardSizeCol(int boardSizeCol) {
        this.boardSizeCol = boardSizeCol;
        return this;
    }

    public BoardFactory useDefaultBoardSizeCol() {
        this.boardSizeCol = 8;
        return this;
    }

    public BoardFactory setBoardSizeRow(int boardSizeRow) {
        this.boardSizeRow = boardSizeRow;
        return this;
    }

    public BoardFactory useDefaultBoardSizeRow() {
        this.boardSizeRow = 8;
        return this;
    }

    public BoardFactory setRule(Rule rule) {
        this.rule = rule;
        return this;
    }

    public BoardFactory useDefaultRule() {
        this.rule = new RuleImplReversi();
        return this;
    }

    public Board createBoard() {
        return new Board(
                boardSizeRow,
                boardSizeCol,
                rule,
                new WindowImplConsole(boardSizeRow+1,boardSizeCol*2+Board.ULTIMATE_ANSWER),
                whitePlayerName,
                blackPlayerName);
    }

    public static BoardFactory create() {
        return new BoardFactory();
    }
}
