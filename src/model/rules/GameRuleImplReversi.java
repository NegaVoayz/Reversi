package model.rules;

import model.pieces.Piece;
import model.pieces.PieceImplMonochrome;
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

    /**
     * allocate pieceGrid and set the start pieces
     */
    @Override
    public void initializeGrid(Piece[][] pieceGrid) {
        basicInitializeGrid(pieceGrid);

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
        if(pieceImplMonochrome.getPlayer() != Player.NONE) {
            return false;
        }
        return flipPieces(move.end, player, pieceGrid, false)!=0;
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
        flipPieces(move.end, player, pieceGrid, true);
        return true;
    }

    @Override
    public boolean gameOverCheck(Player currentPlayer, Piece[][] pieceGrid) {
        Move move = new Move(new Point(0,0), new Point(0,0), currentPlayer);
        for(move.end.y = 1; move.end.y < pieceGrid.length-1; move.end.y++) {
            for(move.end.x = 1; move.end.x < pieceGrid[0].length-1; move.end.x++) {
                if( placePieceValidationCheck(move, currentPlayer, pieceGrid) ) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Player gameWonCheck(Player currentPlayer, Piece[][] pieceGrid) {
        int whiteCount = getWhiteScore(pieceGrid);
        int blackCount = getBlackScore(pieceGrid);

        if(whiteCount > blackCount) {
            return Player.WHITE;
        }
        if(whiteCount < blackCount) {
            return Player.BLACK;
        }
        return Player.NONE;
    }

    @Override
    public int getWhiteScore(Piece[][] pieceGrid) {
        int whiteCount = 0;
        for(Piece[] pieces : pieceGrid) {
            for(Piece piece : pieces) {
                if(piece.getPlayer() == Player.WHITE) {
                    whiteCount++;
                }
            }
        }
        return whiteCount;
    }

    @Override
    public int getBlackScore(Piece[][] pieceGrid) {
        int blackCount = 0;
        for(Piece[] pieces : pieceGrid) {
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
}
