package model.rules.gameRule;

import model.exceptions.GameException;
import model.exceptions.OccupiedPositionException;
import model.exceptions.OutOfBoardException;
import model.exceptions.RuleViolationException;
import model.pieces.Piece;
import model.pieces.PieceImplMonochrome;
import model.structs.GameStatistics;
import model.structs.Move;
import model.structs.Point;
import model.enums.Player;

public class GameRuleImplReversi extends AbstractGameRuleMonochrome {

    private static final GameRuleImplReversi instance = new GameRuleImplReversi();

    public static GameRuleImplReversi getGameRule() {
        return instance;
    }

    private GameRuleImplReversi() {}

    private final static Point[] DIRECTIONS = {
            new Point(1,1),  new Point(1,0),  new Point(1,-1),
            new Point(0,1),                         new Point(0,-1),
            new Point(-1,1), new Point(-1,0), new Point(-1,-1)
    };

    private static class ExtraInfo{
        private boolean stale;
        private boolean passed;
        public ExtraInfo() {
            this.stale = false;
            this.passed = false;
        }
        public void setStale(boolean stale) {this.stale = stale;}
        public void reset() {
            this.passed = false;
        }
        public boolean gameOver() {return stale && passed;}
        public boolean canPass() {return stale;}
        public void pass() {
            stale = false;
            passed = true;
        }
    }

    /**
     * allocate pieceGrid and set the start pieces
     */
    @Override
    public Piece[][] initializeGrid(int height, int width){
        PieceImplMonochrome[][] pieceGrid = basicInitializeGrid(height, width);

        pieceGrid[height/2][width/2].setPlayer(Player.WHITE);
        pieceGrid[height/2+1][width/2].setPlayer(Player.BLACK);
        pieceGrid[height/2][width/2+1].setPlayer(Player.BLACK);
        pieceGrid[height/2+1][width/2+1].setPlayer(Player.WHITE);

        return pieceGrid;
    }

    @Override
    public void initializeExtraInfo(GameStatistics statistics) {
        statistics.setExtraInfo(new ExtraInfo());
    }

    /**
     * Check whether it is a valid move to place a piece here
     *
     * @param move       position of the move
     * @param statistics the board statistics
     * @return true when valid
     */
    @Override
    public boolean placePieceValidationCheck(Move move, GameStatistics statistics) throws GameException {
        /* pass logic */
        if(move.end.x == 0 && move.end.y == 0) {
            ExtraInfo extraInfo = (ExtraInfo) statistics.getExtraInfo();
            if(!extraInfo.canPass()) {
                throw new RuleViolationException("No pass here.");
            }
            return true;
        }

        /* out of board check */
        if( move.end.x <= 0 || move.end.x > statistics.getHeight() ||
                move.end.y <= 0 || move.end.y > statistics.getWidth() ) {
            throw new OutOfBoardException(move.end);
        }

        /* piece type check */
        if( ! (statistics.getPieceGrid()[move.end.y][move.end.x] instanceof PieceImplMonochrome pieceImplMonochrome)
                || !(move.piece instanceof PieceImplMonochrome)) {
            throw new IllegalArgumentException("Invalid Piece implementation");
        }

        /* place occupation check */
        if(pieceImplMonochrome.getPlayer() != Player.NONE) {
            throw new OccupiedPositionException(move.end);
        }

        /* flip check */
        if(flipPieces(move.end, statistics.getCurrentPlayer(), statistics.getPieceGrid(), false)==0) {
            throw new RuleViolationException("One move shall flip at least 1 piece");
        }

        return true;
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
        ExtraInfo extraInfo = (ExtraInfo) statistics.getExtraInfo();
        extraInfo.reset();

        /* pass logic */
        if(move.end.x == 0 && move.end.y == 0) {
            extraInfo.pass();
            return true;
        }

        statistics.getPieceGrid()[move.end.y][move.end.x].setPlayer(statistics.getCurrentPlayer());
        flipPieces(move.end, statistics.getCurrentPlayer(), statistics.getPieceGrid(), true);
        move.piece.setPlayer(statistics.getCurrentPlayer());
        statistics.addMove(move);
        return true;
    }

    @Override
    public boolean gameOverCheck(GameStatistics statistics) {
        ExtraInfo extraInfo = (ExtraInfo) statistics.getExtraInfo();
        extraInfo.setStale(checkStale(statistics));
        if(extraInfo.gameOver()
                || statistics.getMoves().size() ==
                statistics.getHeight() * statistics.getWidth() - 4) {
            statistics.setWinner(calculateWinner(statistics));
            return true;
        }
        return false;
    }

    @Override
    public int getWhiteScore(GameStatistics statistics) {
        int whiteCount = 0;
        for(Piece[] pieces : statistics.getPieceGrid()) {
            for(Piece piece : pieces) {
                if(piece.getPlayer() == Player.WHITE) {
                    whiteCount++;
                }
            }
        }
        return whiteCount;
    }

    @Override
    public int getBlackScore(GameStatistics statistics) {
        int blackCount = 0;
        for(Piece[] pieces : statistics.getPieceGrid()) {
            for(Piece piece : pieces) {
                if(piece.getPlayer() == Player.BLACK) {
                    blackCount++;
                }
            }
        }
        return blackCount;
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

    private Player calculateWinner(GameStatistics statistics) {
        int whiteCount = getWhiteScore(statistics);
        int blackCount = getBlackScore(statistics);

        if(whiteCount > blackCount) {
            return Player.WHITE;
        }
        if(whiteCount < blackCount) {
            return Player.BLACK;
        }
        return Player.NONE;
    }

    private boolean checkStale(GameStatistics statistics) {
        Piece piece = new PieceImplMonochrome();
        piece.setPlayer(statistics.getCurrentPlayer());
        Move move = new Move(new Point(0,0), new Point(0,0), piece);
        for(move.end.y = 1; move.end.y <= statistics.getHeight(); move.end.y++) {
            for(move.end.x = 1; move.end.x <= statistics.getWidth(); move.end.x++) {
                try {
                    if(placePieceValidationCheck(move, statistics))
                        return false;
                } catch (GameException _) {}
            }
        }
        return true;
    }
}
