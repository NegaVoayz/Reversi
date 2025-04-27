package model.rules;

import model.pieces.Piece;
import model.structs.Move;
import model.enums.Player;

public interface GameRule {

    void initializeGrid(Piece[][] pieceGrid);

    boolean placePieceValidationCheck(Move move, Player player, Piece[][] pieceGrid);

    Player nextPlayer(Player player, Piece[][] pieceGrid);

    boolean placePiece(Move move, Player player, Piece[][] pieceGrid);

    boolean gameOverCheck(Player currentPlayer, Piece[][] pieceGrid);

    Player gameWonCheck(Player currentPlayer, Piece[][] pieceGrid);

    int getWhiteScore(Piece[][] pieceGrid);

    int getBlackScore(Piece[][] pieceGrid);
}
