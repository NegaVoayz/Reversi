package model.rules.gameRule;

import model.enums.BombPieceType;
import model.enums.Player;
import model.exceptions.GameException;
import model.exceptions.OccupiedPositionException;
import model.exceptions.RuleViolationException;
import model.pieces.Piece;
import model.pieces.PieceImplBomb;
import model.structs.GameStatistics;
import model.structs.Move;
import model.structs.Point;

public class GameRuleImplBomb extends AbstractGameRuleMonochrome {
    private static final GameRuleImplBomb instance = new GameRuleImplBomb();

    public static GameRuleImplBomb getGameRule() {
        return instance;
    }

    private GameRuleImplBomb() {}

    private final static Point[] DIRECTIONS = {
            new Point(1,1),  new Point(1,0),  new Point(1,-1),
            new Point(0,1),                         new Point(0,-1),
            new Point(-1,1), new Point(-1,0), new Point(-1,-1)
    };

    public static class BombRecord {
        private int blackBombCount;
        private int whiteBombCount;
        public BombRecord() {
            blackBombCount = 2;
            whiteBombCount = 3;
        }

        public int getBlackBombCount() {
            return blackBombCount;
        }

        public int getWhiteBombCount() {
            return whiteBombCount;
        }

        public void useBlackBomb() {
            blackBombCount--;
        }

        public void useWhiteBomb() {
            whiteBombCount--;
        }
    };

    @Override
    public Piece[][] initializeGrid(int height, int width){
        PieceImplBomb[][] pieceGrid = new PieceImplBomb[height+2][width+2];
        for(int i = 0; i < height+2; i++) {
            for(int j = 0; j < width+2; j++) {
                pieceGrid[i][j] = new PieceImplBomb();
            }
        }
        pieceGrid[3][6].setType(BombPieceType.BARRIER);
        pieceGrid[8][7].setType(BombPieceType.BARRIER);
        pieceGrid[9][6].setType(BombPieceType.BARRIER);
        pieceGrid[12][11].setType(BombPieceType.BARRIER);
        return pieceGrid;
    }

    @Override
    public void initializeExtraInfo(GameStatistics statistics) {
        statistics.setExtraInfo(new BombRecord());
    }

    @Override
    public boolean placePieceValidationCheck(Move move, GameStatistics statistics) throws GameException {
        if( move.end.x <= 0 || move.end.x > statistics.getHeight() ||
                move.end.y <= 0 || move.end.y > statistics.getWidth() ) {
            return false;
        }
        if( ! (statistics.getPieceGrid()[move.end.y][move.end.x] instanceof PieceImplBomb pieceToReplace)
                || !(move.piece instanceof PieceImplBomb pieceToPlace) ) {
            throw new IllegalArgumentException("Invalid Piece implementation");
        }
        move.piece.setPlayer(statistics.getCurrentPlayer());

        // debt
        return switch (pieceToPlace.getType()) {
            case NORMAL -> {
                if(pieceToReplace.getPlayer() != Player.NONE) {
                    throw new OccupiedPositionException(move.end);
                }

                if(pieceToReplace.getType() != BombPieceType.NORMAL) {
                    throw new RuleViolationException("Unable to place piece here");
                }

                yield true;
            }
            case CRATER -> {
                if(pieceToReplace.getType() == BombPieceType.BARRIER) {
                    throw new RuleViolationException("Unbreakable barrier!");
                }

                if(pieceToReplace.getType() == BombPieceType.CRATER) {
                    throw new RuleViolationException("The shell does not hit a crater twice.");
                }

                if(pieceToReplace.getPlayer() == Player.NONE) {
                    throw new RuleViolationException("No, it's a waste of bomb.");
                }

                if(pieceToReplace.getPlayer() == statistics.getCurrentPlayer()) {
                    throw new RuleViolationException("FRIENDLY FIRE!");
                }

                yield switch (statistics.getCurrentPlayer()) {
                    case BLACK -> {
                        if(((BombRecord)statistics.getExtraInfo()).getBlackBombCount() == 0) {
                            throw new RuleViolationException("No bombs left");
                        }
                        yield true;
                    }
                    case WHITE -> {
                        if(((BombRecord)statistics.getExtraInfo()).getWhiteBombCount() == 0) {
                            throw new RuleViolationException("No bombs left");
                        }
                        yield true;
                    }
                    default -> throw new IllegalArgumentException("Invalid player");
                };
            }
            case BARRIER -> throw new IllegalArgumentException("What? How the hell can you get a barrier?");
        };
    }

    @Override
    public void nextPlayer(GameStatistics statistics) {
        statistics.switchPlayer();
    }

    @Override
    public boolean placePiece(Move move, GameStatistics statistics) throws GameException {
        if(!placePieceValidationCheck(move, statistics)) {
            return false;
        }
        if(move.piece instanceof PieceImplBomb pieceToPlace) {
            if(pieceToPlace.getType() == BombPieceType.CRATER) {
                switch(pieceToPlace.getPlayer()) {
                    case BLACK -> ((BombRecord)statistics.getExtraInfo()).useBlackBomb();
                    case WHITE -> ((BombRecord)statistics.getExtraInfo()).useWhiteBomb();
                    default -> throw new IllegalArgumentException("Invalid player");
                }
            }
        } else {
            throw new IllegalArgumentException("Invalid piece type");
        }
        move.piece.setPlayer(statistics.getCurrentPlayer());
        statistics.getPieceGrid()[move.end.y][move.end.x].setPiece(move.piece);
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
        if(full) {
            statistics.setWinner(Player.NONE);
        }
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
        if(!(statistics.getPieceGrid() instanceof PieceImplBomb[][] pieceGrid)) {
            throw new IllegalArgumentException("Invalid piece grid");
        }
        Point temp = new Point(0,0);
        Player currentPlayer = statistics.getPieceGrid()[point.y][point.x].getPlayer();
        int maxConnectCount = 1;

        for(Point direction : DIRECTIONS) {
            int connectCount = 1;
            temp.set(point)
                    .translate(direction);
            while(pieceGrid[temp.y][temp.x].getPlayer() == currentPlayer
                    && pieceGrid[temp.y][temp.x].getType() == BombPieceType.NORMAL) {
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
