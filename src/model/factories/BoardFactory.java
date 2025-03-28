package model.factories;

import model.Board;
import model.enums.AlignType;
import model.rules.Rule;
import model.rules.RuleImplReversi;
import model.structs.Rect;
import view.Screen;

public class BoardFactory {
    private String whitePlayerName;
    private String blackPlayerName;
    private int boardSizeCol;
    private int boardSizeRow;
    private Rule rule;
    private Screen screen;
    private final Rect rect;
    private AlignType verticalAlign;
    private AlignType horizontalAlign;


    private BoardFactory() {
        whitePlayerName = "";
        blackPlayerName = "";
        boardSizeCol = 8;
        boardSizeRow = 8;
        verticalAlign = AlignType.BEGIN;
        horizontalAlign = AlignType.BEGIN;
        rule = new RuleImplReversi();
        rect = new Rect(0, boardSizeRow+1, 0, boardSizeCol*2+Board.ULTIMATE_ANSWER);
    }

    public boolean isLegalSetting() {
        return  !whitePlayerName.isEmpty() &&
                !blackPlayerName.isEmpty() &&
                !(rule == null) &&
                !(screen == null);
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
        this.rect.right = this.boardSizeCol*2+Board.ULTIMATE_ANSWER;
        applyHorizontalAlign();
        return this;
    }

    public BoardFactory useDefaultBoardSizeCol() {
        this.boardSizeCol = 8;
        this.rect.right = this.boardSizeCol*2+Board.ULTIMATE_ANSWER;
        applyHorizontalAlign();
        return this;
    }

    public BoardFactory setBoardSizeRow(int boardSizeRow) {
        this.boardSizeRow = boardSizeRow;
        this.rect.bottom = this.boardSizeRow+1;
        applyVerticalAlign();
        return this;
    }

    public BoardFactory useDefaultBoardSizeRow() {
        this.boardSizeRow = 8;
        this.rect.bottom = this.boardSizeRow+1;
        applyVerticalAlign();
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

    public BoardFactory setScreen(Screen screen) {
        this.screen = screen;
        return this;
    }

    public BoardFactory setVerticalAlign(AlignType verticalAlign) {
        this.verticalAlign = verticalAlign;
        applyVerticalAlign();
        return this;
    }

    public BoardFactory useDefaultVerticalAlign() {
        this.verticalAlign = AlignType.MIDDLE;
        applyVerticalAlign();
        return this;
    }

    public BoardFactory setHorizontalAlign(AlignType horizontalAlign) {
        this.horizontalAlign = horizontalAlign;
        applyHorizontalAlign();
        return this;
    }

    public BoardFactory useDefaultHorizontalAlign() {
        this.horizontalAlign = AlignType.BEGIN;
        applyHorizontalAlign();
        return this;
    }

    public Board createBoard() {
        return new Board(
                boardSizeRow,
                boardSizeCol,
                rule,
                screen.createWindow(rect),
                whitePlayerName,
                blackPlayerName);
    }

    public static BoardFactory create() {
        return new BoardFactory();
    }

    private void applyVerticalAlign() {
        int height = rect.bottom - rect.top;
        int screenHeight = screen.getRect().bottom - screen.getRect().top;
        switch (verticalAlign) {
            case BEGIN:
                rect.top = 0;
                rect.bottom = height;
                break;
            case MIDDLE:
                rect.top = (screenHeight - height) / 2;
                rect.bottom = rect.top + height;
                break;
            case END:
                rect.top = screenHeight - height;
                rect.bottom = screenHeight;
                break;
        }
    }

    private void applyHorizontalAlign() {
        int width = rect.right - rect.left;
        int screenWidth = screen.getRect().right - screen.getRect().left;
        switch (horizontalAlign) {
            case BEGIN:
                rect.left = 0;
                rect.right = width;
                break;
            case MIDDLE:
                rect.left = (screenWidth - width) / 2;
                rect.right = rect.left + width;
                break;
            case END:
                rect.left = screenWidth - width;
                rect.right = screenWidth;
                break;
        }
    }
}
