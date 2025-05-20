package model.rules.gameRule;

import model.enums.GameType;
import model.exceptions.GameException;
import model.exceptions.InvalidMoveException;
import model.pieces.Piece;
import model.structs.GameStatistics;
import model.structs.Move;

public interface GameRule {

    GameType getGameType();

    Piece[][] initializeGrid(int height, int width);

    void initializeExtraInfo(GameStatistics statistics);

    boolean placePieceValidationCheck(Move move, GameStatistics statistics) throws GameException;

    void nextPlayer(GameStatistics statistics);

    boolean placePiece(Move move, GameStatistics statistics) throws GameException;

    boolean gameOverCheck(GameStatistics statistics);

    int getWhiteScore(GameStatistics statistics);

    int getBlackScore(GameStatistics statistics);
}
