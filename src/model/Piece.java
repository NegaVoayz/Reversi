package model;

import view.Pixel;

public class Piece {
    public enum Type{
        WHITE,
        NONE,
        BLACK,
    }
    
    public final static char NONE_PIECE = ' ';
    public final static char WHITE_PIECE = '○';
    public final static char BLACK_PIECE = '●';
    public final static char VALID_MOVE = '·';
    public final static int[][] DIRECTIONS = {
        {1,1},  {1,0},  {1,-1},
        {0,1},          {0,-1},
        {-1,1}, {-1,0}, {-1,-1}
    };

    //to gain access to neighbours
    private final Piece[][] pieceGrid;
    private Piece.Type type;
    private final int col;
    private final int row;

    protected Piece(int col, int row, Piece[][] pieceGrid) {
        this.type = Piece.Type.NONE;
        this.col = col;
        this.row = row;
        this.pieceGrid = pieceGrid;
    }

    /**
     * manually set piece type
     * designed for pieceGrid initialization
     * @param type Piece.type
     */
    protected void setType(Piece.Type type) {
        this.type = type;
    }

    protected Piece.Type getType() {
        return this.type;
    }

    /**
     * Get the pixel form of this piece
     * @return Pixel
     */
    protected Pixel getPixel() {
        return switch (this.type) {
            case Type.WHITE -> new Pixel(WHITE_PIECE);
            case Type.BLACK -> new Pixel(BLACK_PIECE);
            default -> new Pixel(NONE_PIECE);
        };
    }

    /**
     * Check whether it is a valid move to place piece here
     * @param expected_type the expected type of piece
     * @return true when valid
     */
    protected boolean isValid(Piece.Type expected_type) {
        if(this.type != Piece.Type.NONE) {
            return false;
        }
        for(int i = 0; i < 8; i++) {
            int dx = DIRECTIONS[i][0];
            int dy = DIRECTIONS[i][1];
            if(pieceGrid[row+dy][col+dx].flipPieces(dx, dy, expected_type, false)>0) {
                return true;
            }
        }
        return false;
    }

    /**
     * set your piece and flip the pieces
     * @param type the type of piece you want to place here
     * @return the number of pieces you've flipped
     */
    protected int placePiece(Piece.Type type) {
        this.type = type;
        int count = 0;
        for(int i = 0; i < 8; i++) {
            int dx = DIRECTIONS[i][0];
            int dy = DIRECTIONS[i][1];
            int temp = pieceGrid[row+dy][col+dx].flipPieces(dx, dy, type, true);
            if(temp > 0) {
                count += temp;
            }
        }
        return count;
    }

    /**
     * recursively check/do a flip
     * @param dx direction on column
     * @param dy direction on row
     * @param type piece type of the original piece
     * @param applyChange flip the pieces or not
     * @return the number of pieces (could be) flipped (negative means operation failed)
     */
    private int flipPieces(int dx, int dy, Piece.Type type, boolean applyChange) {
        if(this.type == Piece.Type.NONE) {
            return -1; 
        }
        if(this.type == type) {
            return 0;
        }

        // try flip next
        int ans = pieceGrid[row+dy][col+dx].flipPieces(dx, dy, type, applyChange);

        if(ans < 0) {
            return -1;
        }

        if(applyChange) {
            this.type = type;
        }

        return ans + 1;
    }
}