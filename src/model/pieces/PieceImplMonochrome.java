package model.pieces;

import view.Pixel;
import view.PixelImplConsole;

public final class PieceImplMonochrome extends Piece {
    
    public final static Pixel NONE_PIECE  =  new PixelImplConsole(' ');
    public final static Pixel WHITE_PIECE =  new PixelImplConsole('●');
    public final static Pixel BLACK_PIECE =  new PixelImplConsole('○');
    public final static Pixel VALID_MOVE  =  new PixelImplConsole('·');

    public PieceImplMonochrome(){
        super();
    }

    /**
     * Get the pixel form of this piece
     * @return Pixel
     */
    @Override
    public Pixel getPixel() {
        return switch (this.getPlayer()) {
            case WHITE -> WHITE_PIECE;
            case BLACK -> BLACK_PIECE;
            default -> NONE_PIECE;
        };
    }
}