package model.rules;

import model.pieces.Piece;
import model.pieces.PieceImplMonochrome;
import model.structs.GameStatistics;
import model.structs.Move;
import model.structs.Point;
import model.enums.Player;

public class GameRuleImplLandfill extends AbstractGameRuleMonochrome {

    private static final GameRuleImplLandfill instance = new GameRuleImplLandfill();

    public static GameRuleImplLandfill getGameRule() {
        return instance;
    }

    private GameRuleImplLandfill() {}

    /**
     * allocate pieceGrid and set the start pieces
     */
    @Override
    public void initializeGrid(GameStatistics statistics) {
        basicInitializeGrid(statistics);

        statistics.getPieceGrid()[statistics.getHeight()/2][statistics.getWidth()/2].setPlayer(Player.WHITE);
        statistics.getPieceGrid()[statistics.getHeight()/2+1][statistics.getWidth()/2].setPlayer(Player.BLACK);
        statistics.getPieceGrid()[statistics.getHeight()/2][statistics.getWidth()/2+1].setPlayer(Player.BLACK);
        statistics.getPieceGrid()[statistics.getHeight()/2+1][statistics.getWidth()/2+1].setPlayer(Player.WHITE);
    }


    /**
     * Check whether it is a valid move to place a piece here
     *
     * @param move       position of the move
     * @param statistics the board statistics
     * @return true when valid
     */
    @Override
    public boolean placePieceValidationCheck(Move move, GameStatistics statistics) {
        if( ! (statistics.getPieceGrid()[move.end.y][move.end.x] instanceof PieceImplMonochrome pieceImplMonochrome)
                || !(move.piece instanceof PieceImplMonochrome) ) {
            throw new IllegalArgumentException("Invalid Piece implementation");
        }
        return pieceImplMonochrome.getPlayer() == Player.NONE;
    }

    @Override
    public void nextPlayer(GameStatistics statistics) {
        statistics.switchPlayer();
        if(checkStale(statistics)) {
            statistics.switchPlayer();
        }
    }

    @Override
    public boolean placePiece(Move move, GameStatistics statistics) {
        if(!placePieceValidationCheck(move, statistics)) {
            return false;
        }
        statistics.getPieceGrid()[move.end.y][move.end.x].setPlayer(statistics.getCurrentPlayer());
        move.piece.setPlayer(statistics.getCurrentPlayer());
        statistics.addMove(move);
        return true;
    }

    @Override
    public boolean gameOverCheck(GameStatistics statistics) {
        boolean gameOver = checkStale(statistics);
        if(gameOver) { statistics.setWinner(Player.NONE); }
        return gameOver;
    }

    @Override
    public int getWhiteScore(GameStatistics statistics) {
        return -1;
    }

    @Override
    public int getBlackScore(GameStatistics statistics) {
        return -1;
    }

    private boolean checkStale(GameStatistics statistics) {
        Point boardPoint = new Point(0, 0);
        for(boardPoint.y = 1; boardPoint.y <= statistics.getHeight(); boardPoint.y++) {
            for(boardPoint.x = 1; boardPoint.x <= statistics.getWidth(); boardPoint.x++) {
                if( statistics.getPieceGrid()[boardPoint.y][boardPoint.x].getPlayer() == Player.NONE ) {
                    return false;
                }
            }
        }
        return true;
    }
}
