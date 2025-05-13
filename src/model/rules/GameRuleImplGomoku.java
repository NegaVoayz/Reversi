package model.rules;

import model.pieces.Piece;
import model.pieces.PieceImplMonochrome;
import model.enums.Player;
import model.structs.GameStatistics;
import model.structs.Move;
import model.structs.Point;

public class GameRuleImplGomoku extends AbstractGameRuleMonochrome {
    private static final GameRuleImplGomoku instance = new GameRuleImplGomoku();

    public static GameRuleImplGomoku getGameRule() {
        return instance;
    }

    private GameRuleImplGomoku() {}

    private final static Point[] DIRECTIONS = {
            new Point(1,1),  new Point(1,0),  new Point(1,-1),
            new Point(0,1),                         new Point(0,-1),
            new Point(-1,1), new Point(-1,0), new Point(-1,-1)
    };

    @Override
    public void initializeGrid(GameStatistics statistics) {
        basicInitializeGrid(statistics);
    }

    @Override
    public boolean placePieceValidationCheck(Move move, GameStatistics statistics) {
        if( ! (statistics.getPieceGrid()[move.end.y][move.end.x] instanceof PieceImplMonochrome pieceImplMonochrome)
                || !(move.piece instanceof PieceImplMonochrome) ) {
            throw new IllegalArgumentException("Invalid Piece implementation");
        }
        if( move.end.x <= 0 || move.end.x > statistics.getHeight() ||
                move.end.y <= 0 || move.end.y > statistics.getWidth() ) {
            return false;
        }
        return pieceImplMonochrome.getPlayer() == Player.NONE;
    }

    @Override
    public void nextPlayer(GameStatistics statistics) {
        statistics.switchPlayer();
    }

    @Override
    public boolean placePiece(Move move, GameStatistics statistics) {
        if(!placePieceValidationCheck(move, statistics)) {
            return false;
        }
        statistics.getPieceGrid()[move.end.y][move.end.x].setPlayer(statistics.getCurrentPlayer());
        move.piece.setPlayer(statistics.getCurrentPlayer());
        statistics.addMove(move);
        return true;
    }

    @Override
    public boolean gameOverCheck(GameStatistics statistics) {
        boolean full = true;
        Point boardPoint = new Point(0, 0);
        for(boardPoint.y = 1; boardPoint.y <= statistics.getHeight(); boardPoint.y++) {
            for(boardPoint.x = 1; boardPoint.x <= statistics.getWidth(); boardPoint.x++) {
                if( statistics.getPieceGrid()[boardPoint.y][boardPoint.x].getPlayer() == Player.NONE ) {
                    full = false;
                } else {
                    if(checkConnects(boardPoint, statistics) >= 5) {
                        statistics.setWinner(statistics.getPieceGrid()[boardPoint.y][boardPoint.x].getPlayer());
                        return true;
                    }
                }
            }
        }
        statistics.setWinner(Player.NONE);
        return full;
    }

    @Override
    public int getWhiteScore(GameStatistics statistics) {
        return statistics.getWinner() == Player.WHITE ? 1:0;
    }

    @Override
    public int getBlackScore(GameStatistics statistics) {
        return statistics.getWinner() == Player.BLACK ? 1:0;
    }

    private int checkConnects(Point point, GameStatistics statistics) {
        Point temp = new Point(0,0);
        Player currentPlayer = statistics.getPieceGrid()[point.y][point.x].getPlayer();
        int maxConnectCount = 1;

        for(Point direction : DIRECTIONS) {
            int connectCount = 1;
            temp.set(point)
                    .translate(direction);
            while(statistics.getPieceGrid()[temp.y][temp.x].getPlayer() == currentPlayer) {
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
