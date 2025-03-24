package model.rules;

import model.Piece;
import model.PieceImplReversi;
import model.Point;
import model.enums.Player;

public class RuleImplLandfill implements Rule {

    /**
     * allocate pieceGrid and set the start pieces
     */
    @Override
    public void initializeGrid(Piece[][] pieceGrid) {
        for(int i = 0; i < pieceGrid.length; i++) {
            for(int j = 0; j < pieceGrid[0].length; j++) {
                pieceGrid[i][j] = new PieceImplReversi();
            }
        }

        pieceGrid[pieceGrid.length/2-1][pieceGrid[0].length/2-1].setPlayer(Player.WHITE);
        pieceGrid[pieceGrid.length/2-1][pieceGrid[0].length/2].setPlayer(Player.BLACK);
        pieceGrid[pieceGrid.length/2][pieceGrid[0].length/2-1].setPlayer(Player.BLACK);
        pieceGrid[pieceGrid.length/2][pieceGrid[0].length/2].setPlayer(Player.WHITE);
    }


    /**
     * Check whether it is a valid move to place piece here
     * @param point position of the move
     * @param player the player of the move
     * @param pieceGrid the board
     * @return true when valid
     */
    @Override
    public boolean placePieceValidationCheck(Point point, Player player, Piece[][] pieceGrid) {
        if( ! (pieceGrid[point.y][point.x] instanceof PieceImplReversi pieceImplReversi) ) {
            throw new IllegalArgumentException("Invalid Piece implementation");
        }
        return pieceImplReversi.getPlayer() == Player.NONE;
    }

    @Override
    public Player nextPlayer(Player player, Piece[][] pieceGrid) {
        Player nextPlayer;
        if(player == Player.WHITE) {
            nextPlayer = Player.BLACK;
        } else {
            nextPlayer = Player.WHITE;
        }
        if(gameOverCheck(nextPlayer, pieceGrid)) {
            return player;
        }
        return nextPlayer;
    }

    @Override
    public boolean placePiece(Point point, Player player, Piece[][] pieceGrid) {
        if(!placePieceValidationCheck(point, player, pieceGrid)) {
            return false;
        }
        pieceGrid[point.y][point.x].setPlayer(player);
        return true;
    }

    @Override
    public boolean gameOverCheck(Player currentPlayer, Piece[][] pieceGrid) {
        Point boardPoint = new Point(0, 0);
        for(boardPoint.y = 1; boardPoint.y < pieceGrid.length-1; boardPoint.y++) {
            for(boardPoint.x = 1; boardPoint.x < pieceGrid[0].length-1; boardPoint.x++) {
                if( pieceGrid[boardPoint.y][boardPoint.x].getPlayer() == Player.NONE ) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Player gameWonCheck(Player currentPlayer, Piece[][] pieceGrid) {
        int whiteCount = 0;
        int blackCount = 0;
        for(Piece[] pieces : pieceGrid) {
            for(Piece piece : pieces) {
                if(piece.getPlayer() == Player.WHITE) {
                    whiteCount++;
                } else {
                    if(piece.getPlayer() == Player.BLACK) {
                        blackCount++;
                    }
                }
            }
        }

        if(whiteCount > blackCount) {
            return Player.WHITE;
        }
        if(whiteCount < blackCount) {
            return Player.BLACK;
        }
        return Player.NONE;
    }
}
