package model.pieces;

import model.enums.BombPieceType;

public final class PieceImplBomb extends Piece {

    public final static char NONE_PIECE  = ' ';
    public final static char WHITE_PIECE = '●';
    public final static char BLACK_PIECE = '○';
    public final static char BARRIER_PIECE  = '#';
    public final static char CRATER_PIECE  = '@';
    public final static char VALID_MOVE  = '·';

    private BombPieceType type;

    public PieceImplBomb(){
        super();
        type = BombPieceType.NORMAL;
    }

    @Override
    public void setPiece(Piece piece) {
        if(!(piece instanceof PieceImplBomb pieceImplMonochrome)) {
            throw new IllegalArgumentException();
        }
        setPlayer(pieceImplMonochrome.getPlayer());
        setType(pieceImplMonochrome.getType());
    }

    public void setType(BombPieceType type) {
        this.type = type;
    }

    public BombPieceType getType() {
        return type;
    }

    /**
     * Get the pixel form of this piece
     *
     * @return Pixel
     */
    @Override
    public int getCode() {
        return switch (this.getType()) {
            case NORMAL -> switch (this.getPlayer()) {
                case WHITE -> WHITE_PIECE;
                case BLACK -> BLACK_PIECE;
                default -> NONE_PIECE;};
            case CRATER -> CRATER_PIECE;
            case BARRIER -> BARRIER_PIECE;
        };
    }
}