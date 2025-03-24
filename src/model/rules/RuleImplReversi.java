package model.rules;

import model.Piece;
import model.PieceImplReversi;
import model.enums.Player;

public class RuleImplReversi implements Rule {
    public final static int[][] DIRECTIONS = {
            {1,1},  {1,0},  {1,-1},
            {0,1},          {0,-1},
            {-1,1}, {-1,0}, {-1,-1}
    };

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
     * @param col column of the move
     * @param row row of the move
     * @param player the player of the move
     * @param pieceGrid the board
     * @return true when valid
     */
    @Override
    public boolean placePieceValidationCheck(int row, int col, Player player, Piece[][] pieceGrid) {
        if( ! (pieceGrid[row][col] instanceof PieceImplReversi pieceImplReversi) ) {
            throw new IllegalArgumentException("Invalid Piece implementation");
        }
        if(pieceImplReversi.getPlayer() != Player.NONE) {
            return false;
        }
        return flipPieces(row, col, player, pieceGrid, false)!=0;
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
    public boolean placePiece(int row, int col, Player player, Piece[][] pieceGrid) {
        if(!placePieceValidationCheck(row, col, player, pieceGrid)) {
            return false;
        }
        pieceGrid[row][col].setPlayer(player);
        flipPieces(row, col, player, pieceGrid, true);
        return true;
    }

    @Override
    public boolean gameOverCheck(Player currentPlayer, Piece[][] pieceGrid) {
        for(int i = 1; i < pieceGrid.length-1; i++) {
            for(int j = 1; j < pieceGrid[0].length-1; j++) {
                if( placePieceValidationCheck(i, j, currentPlayer, pieceGrid) ) {
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

    /**
     * recursively check/do a flip
     * @param applyChange flip the pieces or not
     * @return the number of pieces (could be) flipped (negative means operation failed)
     */
    private int flipPieces(int row, int col, Player player, Piece[][] pieceGrid, boolean applyChange) {
        int tempRow;
        int tempCol;
        int flipCount = 0;
        Player rival = switch (player) {
            case WHITE -> Player.BLACK;
            case BLACK -> Player.WHITE;
            case NONE -> throw new IllegalArgumentException("Invalid Player NONE");
        };

        for(int[] direction : DIRECTIONS) {
            tempRow = row+direction[0];
            tempCol = col+direction[1];
            while(pieceGrid[tempRow][tempCol].getPlayer() == rival) {
                tempRow += direction[0];
                tempCol += direction[1];
            }
            if(pieceGrid[tempRow][tempCol].getPlayer() == Player.NONE) {
                continue;
            }
            tempRow -= direction[0];
            tempCol -= direction[1];
            while(pieceGrid[tempRow][tempCol].getPlayer() == rival) {
                flipCount++;
                if (applyChange) {
                    pieceGrid[tempRow][tempCol].setPlayer(player);
                }
                tempRow -= direction[0];
                tempCol -= direction[1];
            }
        }

        return flipCount;
    }
}
