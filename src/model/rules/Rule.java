package model.rules;

import model.Piece;
import model.Point;
import model.enums.Player;

public interface Rule {

    void initializeGrid(Piece[][] pieceGrid);

    boolean placePieceValidationCheck(Point point, Player player, Piece[][] pieceGrid);

    Player nextPlayer(Player player, Piece[][] pieceGrid);

    boolean placePiece(Point point, Player player, Piece[][] pieceGrid);

    boolean gameOverCheck(Player currentPlayer, Piece[][] pieceGrid);

    Player gameWonCheck(Player currentPlayer, Piece[][] pieceGrid);
}
