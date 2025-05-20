package model.structs;

import model.enums.Player;
import model.pieces.Piece;
import model.rules.Rule;
import view.components.AlignType;
import view.components.DisplayBlock;

import java.util.LinkedList;
import java.util.Queue;

import static model.Board.MAX_BOARD_SIZE;
import static model.Board.MIN_BOARD_SIZE;

public class GameStatistics {


    private final int height;
    private final int width;
    private final Piece[][] pieceGrid;
    private final String whitePlayerName;
    private final String blackPlayerName;
    private Player currentPlayer;
    private Player winner;
    private int round;
    private final Queue<Move> moves;
    private Object extraInfo;
    private DisplayBlock view;

    public GameStatistics(int height, int width, String whitePlayerName, String blackPlayerName, Rule rule) {
        this.moves = new LinkedList<>();
        // Validate board size
        if(height < MIN_BOARD_SIZE || height > MAX_BOARD_SIZE ||
                width < MIN_BOARD_SIZE || width > MAX_BOARD_SIZE ) {
            throw new IllegalArgumentException("Invalid board size: too large or too small");
        }
        this.height = height;
        this.width = width;
        // Validate player names
        if(whitePlayerName.length() > 32 || blackPlayerName.length() > 32) {
            throw new IllegalArgumentException("Unable to initialize name: too long");
        }
        this.whitePlayerName = whitePlayerName;
        this.blackPlayerName = blackPlayerName;
        this.pieceGrid = rule.getGameRule().initializeGrid(height, width);
        currentPlayer = Player.BLACK;
        winner = null;
        round = 1;
        view = new DisplayBlock(new Rect(0,0,0,0), AlignType.MIDDLE, AlignType.BEGIN);
    }

    public DisplayBlock getView() {
        return view;
    }

    public void setExtraInfo(Object extraInfo) {
        this.extraInfo = extraInfo;
    }

    public Object getExtraInfo() {
        return extraInfo;
    }

    public void switchPlayer() {
        currentPlayer = switch(currentPlayer) {
            case WHITE -> Player.BLACK;
            case BLACK -> Player.WHITE;
            default -> throw new IllegalStateException("Unexpected value: " + currentPlayer);
        };
        // check new round
        if (currentPlayer == Player.BLACK) {
            round++;
        }
    }

    public void addMove(Move move) {
        moves.add(move);
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Piece[][] getPieceGrid() {
        return pieceGrid;
    }

    public String getBlackPlayerName() {
        return blackPlayerName;
    }

    public String getWhitePlayerName() {
        return whitePlayerName;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getWinner() {
        return winner;
    }

    public int getRound() {
        return round;
    }

    public Queue<Move> getMoves() {
        return moves;
    }
}
