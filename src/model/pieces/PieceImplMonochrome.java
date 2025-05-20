package model.pieces;

import view.console.Pixel;
import view.console.PixelImplConsole;

public final class PieceImplMonochrome extends Piece {

    public final static char NONE_PIECE  = ' ';
    public final static char WHITE_PIECE = '●';
    public final static char BLACK_PIECE = '○';
    public final static char VALID_MOVE  = '·';

    public PieceImplMonochrome(){
        super();
    }

    @Override
    public void setPiece(Piece piece) {
        if(!(piece instanceof PieceImplMonochrome pieceImplMonochrome)) {
            throw new IllegalArgumentException();
        }
        setPlayer(pieceImplMonochrome.getPlayer());
    }

    /**
     * Get the pixel form of this piece
     *
     * @return Pixel
     */
    @Override
    public int getCode() {
        return switch (this.getPlayer()) {
            case WHITE -> WHITE_PIECE;
            case BLACK -> BLACK_PIECE;
            default -> NONE_PIECE;
        };
    }
}