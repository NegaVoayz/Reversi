package model.rules;

import model.Piece;
import model.PieceImplReversi;
import model.Point;
import model.enums.Player;

public class RuleImplReversi implements Rule {
    private final static Point[] DIRECTIONS = {
            new Point(1,1),  new Point(1,0),  new Point(1,-1),
            new Point(0,1),                         new Point(0,-1),
            new Point(-1,1), new Point(-1,0), new Point(-1,-1)
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
        if(pieceImplReversi.getPlayer() != Player.NONE) {
            return false;
        }
        return flipPieces(point, player, pieceGrid, false)!=0;
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
        flipPieces(point, player, pieceGrid, true);
        return true;
    }

    @Override
    public boolean gameOverCheck(Player currentPlayer, Piece[][] pieceGrid) {
        Point boardPoint = new Point(0, 0);
        for(boardPoint.y = 1; boardPoint.y < pieceGrid.length-1; boardPoint.y++) {
            for(boardPoint.x = 1; boardPoint.x < pieceGrid[0].length-1; boardPoint.x++) {
                if( placePieceValidationCheck(boardPoint, currentPlayer, pieceGrid) ) {
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
    private int flipPieces(Point point, Player player, Piece[][] pieceGrid, boolean applyChange) {
        Point temp = new Point(0,0);
        int flipCount = 0;
        Player rival = switch (player) {
            case WHITE -> Player.BLACK;
            case BLACK -> Player.WHITE;
            case NONE -> throw new IllegalArgumentException("Invalid Player NONE");
        };

        for(Point direction : DIRECTIONS) {
            temp.set(point)
                .translate(direction);
            while(pieceGrid[temp.y][temp.x].getPlayer() == rival) {
                temp.translate(direction);
            }
            if(pieceGrid[temp.y][temp.x].getPlayer() == Player.NONE) {
                continue;
            }
            temp.detranslate(direction);
            while(pieceGrid[temp.y][temp.x].getPlayer() == rival) {
                flipCount++;
                if (applyChange) {
                    pieceGrid[temp.y][temp.x].setPlayer(player);
                }
                temp.detranslate(direction);
            }
        }

        return flipCount;
    }
}
