package model.rules.gameRule;

import model.enums.ChessPieceType;
import model.enums.GameType;
import model.enums.Player;
import model.pieces.Piece;
import model.pieces.PieceImplChess;
import model.pieces.PieceImplMonochrome;
import model.structs.GameStatistics;
import model.structs.Move;

/*
* not completed
* */

public class GameRuleImplChess implements GameRule {
    private static final GameRuleImplChess instance = new GameRuleImplChess();

    public static GameRuleImplChess getGameRule() {
        return instance;
    }

    private GameRuleImplChess() {}

    private boolean checkMovePawn(Move move, Piece[][] pieceGrid) {
        if( ! (pieceGrid[move.start.y][move.start.x] instanceof PieceImplMonochrome pieceImplMonochrome) ) {
            throw new IllegalArgumentException("Invalid Piece implementation");
        }
        if(move.start.y >= move.end.y) {return false;}
        return pieceGrid[move.end.y][move.end.x].getPlayer() != pieceImplMonochrome.getPlayer();
    }

    private static void setLine(int line, PieceImplChess[][] pieceGrid) {
        pieceGrid[line][0].setPieceType(ChessPieceType.ROOK);
        pieceGrid[line][1].setPieceType(ChessPieceType.KNIGHT);
        pieceGrid[line][2].setPieceType(ChessPieceType.BISHOP);
        pieceGrid[line][3].setPieceType(ChessPieceType.QUEEN);
        pieceGrid[line][4].setPieceType(ChessPieceType.KING);
        pieceGrid[line][5].setPieceType(ChessPieceType.BISHOP);
        pieceGrid[line][6].setPieceType(ChessPieceType.KNIGHT);
        pieceGrid[line][7].setPieceType(ChessPieceType.ROOK);
    }

    @Override
    public GameType getGameType() {
        return GameType.MOVE_PIECE;
    }

    @Override
    public Piece[][] initializeGrid(int height, int width) {
        assert height == 8 && width == 8;
        PieceImplChess[][] pieceGrid = new PieceImplChess[height+2][width+2];
        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                pieceGrid[i][j] = new PieceImplChess();
            }
        }

        for(int i = 0; i < 8; i++) {
            pieceGrid[0][i].setPlayer(Player.WHITE);
            pieceGrid[1][i].setPlayer(Player.WHITE);
            pieceGrid[1][i].setPieceType(ChessPieceType.PAWN);

            pieceGrid[6][i].setPlayer(Player.BLACK);
            pieceGrid[7][i].setPlayer(Player.BLACK);
            pieceGrid[6][i].setPieceType(ChessPieceType.PAWN);
        }
        setLine(0, pieceGrid);
        setLine(7, pieceGrid);
        return pieceGrid;
    }

    @Override
    public void initializeExtraInfo(GameStatistics statistics) {}

    @Override
    public boolean placePieceValidationCheck(Move move, GameStatistics statistics) {
        if( ! (statistics.getPieceGrid()[move.end.y][move.end.x] instanceof PieceImplMonochrome pieceImplMonochrome) ) {
            throw new IllegalArgumentException("Invalid Piece implementation");
        }
        return pieceImplMonochrome.getPlayer() == Player.NONE;
    }

    @Override
    public void nextPlayer(GameStatistics statistics) {
        statistics.switchPlayer();
    }

    @Override
    public boolean placePiece(Move move, GameStatistics statistics) {
        if(!placePieceValidationCheck(move, statistics)) {
            return false;
        }
        statistics.getPieceGrid()[move.end.y][move.end.x].setPiece(move.piece);
        statistics.getPieceGrid()[move.start.y][move.start.x].setPiece(new PieceImplChess());
        statistics.addMove(move);
        return true;
    }

    @Override
    public boolean gameOverCheck(GameStatistics statistics) {
        if(!(statistics.getPieceGrid() instanceof PieceImplChess[][] pieceGridImplChess)) {
            throw new IllegalArgumentException("Invalid Piece implementation");
        }
        int count = 0;
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(pieceGridImplChess[i][j].getPieceType() == ChessPieceType.KING) {
                    count ++;
                }
            }
        }
        return count == 1;
    }

    @Override
    public int getWhiteScore(GameStatistics statistics) {
        return statistics.getWinner() == Player.WHITE ? 1:0;
    }

    @Override
    public int getBlackScore(GameStatistics statistics) {
        return statistics.getWinner() == Player.BLACK ? 1:0;
    }
}
