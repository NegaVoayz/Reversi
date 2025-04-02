package model.rules;

import model.Piece;
import model.PieceImplMonochrome;
import model.enums.Player;
import model.structs.Move;
import model.structs.Point;

public class GameRuleImplGomoku extends GameRuleImplMonochrome {
    private final static Point[] DIRECTIONS = {
            new Point(1,1),  new Point(1,0),  new Point(1,-1),
            new Point(0,1),                         new Point(0,-1),
            new Point(-1,1), new Point(-1,0), new Point(-1,-1)
    };

    @Override
    public void initializeGrid(Piece[][] pieceGrid) {
        basicInitializeGrid(pieceGrid);
    }

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
        boolean full = true;
        Point boardPoint = new Point(0, 0);
        for(boardPoint.y = 1; boardPoint.y < pieceGrid.length-1; boardPoint.y++) {
            for(boardPoint.x = 1; boardPoint.x < pieceGrid[0].length-1; boardPoint.x++) {
                if( pieceGrid[boardPoint.y][boardPoint.x].getPlayer() == Player.NONE ) {
                    full = false;
                } else {
                    if(checkConnects(boardPoint, pieceGrid) >= 5) {
                        return true;
                    }
                }
            }
        }
        return full;
    }

    @Override
    public Player gameWonCheck(Player currentPlayer, Piece[][] pieceGrid) {
        Point boardPoint = new Point(0, 0);
        for(boardPoint.y = 1; boardPoint.y < pieceGrid.length-1; boardPoint.y++) {
            for(boardPoint.x = 1; boardPoint.x < pieceGrid[0].length-1; boardPoint.x++) {
                if( pieceGrid[boardPoint.y][boardPoint.x].getPlayer() != Player.NONE ) {
                    if(checkConnects(boardPoint, pieceGrid) >= 5) {
                        return pieceGrid[boardPoint.y][boardPoint.x].getPlayer();
                    }
                }
            }
        }
        return Player.NONE;
    }

    @Override
    public int getWhiteScore(Piece[][] pieceGrid) {
        return gameWonCheck(Player.NONE, pieceGrid) == Player.WHITE ? 1:0;
    }

    @Override
    public int getBlackScore(Piece[][] pieceGrid) {
        return gameWonCheck(Player.NONE, pieceGrid) == Player.BLACK ? 1:0;
    }

    private int checkConnects(Point point, Piece[][] pieceGrid) {
        Point temp = new Point(0,0);
        Player currentPlayer = pieceGrid[point.y][point.x].getPlayer();
        int maxConnectCount = 1;

        for(Point direction : DIRECTIONS) {
            int connectCount = 1;
            temp.set(point)
                    .translate(direction);
            while(pieceGrid[temp.y][temp.x].getPlayer() == currentPlayer) {
                temp.translate(direction);
                connectCount++;
            }
            if(connectCount > maxConnectCount) {
                maxConnectCount = connectCount;
            }
        }

        return maxConnectCount;
    }
}
