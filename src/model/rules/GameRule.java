package model.rules;

import model.pieces.Piece;
import model.structs.GameStatistics;
import model.structs.Move;

public interface GameRule {

    void initializeGrid(GameStatistics statistics);

    boolean placePieceValidationCheck(Move move, GameStatistics statistics);

    void nextPlayer(GameStatistics statistics);

    boolean placePiece(Move move, GameStatistics statistics);

    boolean gameOverCheck(GameStatistics statistics);

    int getWhiteScore(GameStatistics statistics);

    int getBlackScore(GameStatistics statistics);
}
