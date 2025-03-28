package model.rules;

import model.Piece;
import model.PieceImplMonochrome;
import model.structs.Move;
import model.structs.Point;
import model.enums.Player;

public class GameRuleImplLandfill implements GameRule {

    /**
     * allocate pieceGrid and set the start pieces
     */
    @Override
    public void initializeGrid(Piece[][] pieceGrid) {
        for(int i = 0; i < pieceGrid.length; i++) {
            for(int j = 0; j < pieceGrid[0].length; j++) {
                pieceGrid[i][j] = new PieceImplMonochrome();
            }
        }

        pieceGrid[pieceGrid.length/2-1][pieceGrid[0].length/2-1].setPlayer(Player.WHITE);
        pieceGrid[pieceGrid.length/2-1][pieceGrid[0].length/2].setPlayer(Player.BLACK);
        pieceGrid[pieceGrid.length/2][pieceGrid[0].length/2-1].setPlayer(Player.BLACK);
        pieceGrid[pieceGrid.length/2][pieceGrid[0].length/2].setPlayer(Player.WHITE);
    }


    /**
     * Check whether it is a valid move to place piece here
     * @param move position of the move
     * @param player the player of the move
     * @param pieceGrid the board
     * @return true when valid
     */
    @Override
    public boolean placePieceValidationCheck(Move move, Player player, Piece[][] pieceGrid) {
        if( ! (pieceGrid[move.end.y][move.end.x] instanceof PieceImplMonochrome pieceImplMonochrome) ) {
            throw new IllegalArgumentException("Invalid Piece implementation");
        }
        return pieceImplMonochrome.getPlayer() == Player.NONE;
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
    public boolean placePiece(Move move, Player player, Piece[][] pieceGrid) {
        if(!placePieceValidationCheck(move, player, pieceGrid)) {
            return false;
        }
        pieceGrid[move.end.y][move.end.x].setPlayer(player);
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
        return Player.NONE;
    }

    @Override
    public int getWhiteScore(Piece[][] pieceGrid) {
        return -1;
    }

    @Override
    public int getBlackScore(Piece[][] pieceGrid) {
        return -1;
    }
}
