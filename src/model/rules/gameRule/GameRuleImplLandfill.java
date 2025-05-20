package model.rules.gameRule;

import model.exceptions.GameException;
import model.exceptions.OccupiedPositionException;
import model.exceptions.OutOfBoardException;
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
    public Piece[][] initializeGrid(int height, int width){
        PieceImplMonochrome[][] pieceGrid = basicInitializeGrid(height, width);

        pieceGrid[height/2][width/2].setPlayer(Player.WHITE);
        pieceGrid[height/2+1][width/2].setPlayer(Player.BLACK);
        pieceGrid[height/2][width/2+1].setPlayer(Player.BLACK);
        pieceGrid[height/2+1][width/2+1].setPlayer(Player.WHITE);

        return pieceGrid;
    }

    @Override
    public void initializeExtraInfo(GameStatistics statistics) {}


    /**
     * Check whether it is a valid move to place a piece here
     *
     * @param move       position of the move
     * @param statistics the board statistics
     * @return true when valid
     */
    @Override
    public boolean placePieceValidationCheck(Move move, GameStatistics statistics) throws GameException {
        if( move.end.x <= 0 || move.end.x > statistics.getHeight() ||
                move.end.y <= 0 || move.end.y > statistics.getWidth() ) {
            throw new OutOfBoardException(move.end);
        }
        if( ! (statistics.getPieceGrid()[move.end.y][move.end.x] instanceof PieceImplMonochrome pieceImplMonochrome)
                || !(move.piece instanceof PieceImplMonochrome) ) {
            throw new IllegalArgumentException("Invalid Piece implementation");
        }

        if( pieceImplMonochrome.getPlayer() != Player.NONE ) {
            throw new OccupiedPositionException(move.end);
        }

        return true;
    }

    @Override
    public void nextPlayer(GameStatistics statistics) {
        statistics.switchPlayer();
        if(checkStale(statistics)) {
            statistics.switchPlayer();
        }
    }

    @Override
    public boolean placePiece(Move move, GameStatistics statistics) throws GameException {
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
