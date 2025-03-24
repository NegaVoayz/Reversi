package model.rules;

import model.Board;
import model.Piece;
import model.enums.Player;

public interface Rule {
    void initializeGrid(Piece[][] pieceGrid);
    boolean placePieceValidationCheck(int row, int col, Player player, Piece[][] pieceGrid);
    Player nextPlayer(Player player, Piece[][] pieceGrid);
    boolean placePiece(int row, int col, Player player, Piece[][] pieceGrid);
    boolean gameOverCheck(Player currentPlayer, Piece[][] pieceGrid);
    Player gameWonCheck(Player currentPlayer, Piece[][] pieceGrid);
}
