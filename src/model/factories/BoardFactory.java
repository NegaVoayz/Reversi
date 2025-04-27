package model.factories;

import model.Board;
import model.enums.AlignType;
import model.rules.Rule;
import model.rules.RuleImplReversi;
import model.structs.Point;
import model.structs.Rect;
import view.Screen;

public class BoardFactory {
    private String whitePlayerName;
    private String blackPlayerName;
    private int boardSizeCol;
    private int boardSizeRow;
    private Rule rule;
    private Screen screen;
    private Rect windowRect;
    private final Point viewStart;
    private AlignType verticalAlign;
    private AlignType horizontalAlign;


    private BoardFactory() {
        whitePlayerName = "";
        blackPlayerName = "";
        boardSizeCol = 8;
        boardSizeRow = 8;
        verticalAlign = AlignType.BEGIN;
        horizontalAlign = AlignType.BEGIN;
        rule = RuleImplReversi.getRule();
        windowRect = new Rect(0, boardSizeRow+1, 0, boardSizeCol*2+Board.ULTIMATE_ANSWER);
        viewStart = new Point(0, 0);
    }

    public boolean isLegalSetting() {
        return  !whitePlayerName.isEmpty() &&
                !blackPlayerName.isEmpty() &&
                !(rule == null) &&
                !(screen == null) &&
                (Board.MIN_BOARD_SIZE <= boardSizeCol && boardSizeCol <= Board.MAX_BOARD_SIZE) &&
                (Board.MIN_BOARD_SIZE <= boardSizeRow && boardSizeRow <= Board.MAX_BOARD_SIZE);
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
        applyHorizontalAlign();
        return this;
    }

    public BoardFactory useDefaultBoardSizeCol() {
        this.boardSizeCol = 8;
        applyHorizontalAlign();
        return this;
    }

    public BoardFactory setBoardSizeRow(int boardSizeRow) {
        this.boardSizeRow = boardSizeRow;
        applyVerticalAlign();
        return this;
    }

    public BoardFactory useDefaultBoardSizeRow() {
        this.boardSizeRow = 8;
        applyVerticalAlign();
        return this;
    }

    public BoardFactory setRule(Rule rule) {
        this.rule = rule;
        return this;
    }

    public BoardFactory useDefaultRule() {
        this.rule = RuleImplReversi.getRule();
        return this;
    }

    public BoardFactory setScreen(Screen screen) {
        this.screen = screen;
        return this;
    }

    public BoardFactory setWindowRect(Rect windowRect) {
        this.windowRect = windowRect;
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
                screen.createWindow(windowRect),
                viewStart,
                whitePlayerName,
                blackPlayerName);
    }

    public static BoardFactory create() {
        return new BoardFactory();
    }

    private void applyVerticalAlign() {
        int height = this.boardSizeRow+1;
        int screenHeight = screen.getRect().bottom - screen.getRect().top;
        viewStart.y = switch (verticalAlign) {
            case BEGIN ->  0;
            case MIDDLE -> (screenHeight - height) / 2;
            case END -> screenHeight - height;
        };
    }

    private void applyHorizontalAlign() {
        int width = this.boardSizeCol*2+Board.ULTIMATE_ANSWER;
        int screenWidth = screen.getRect().right - screen.getRect().left;
        viewStart.x = switch (horizontalAlign) {
            case BEGIN -> 0;
            case MIDDLE -> (screenWidth - width) / 2;
            case END -> screenWidth - width;
        };
    }
}
