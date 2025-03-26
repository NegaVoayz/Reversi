package model;

import model.enums.Player;
import view.Pixel;
import view.PixelImplConsole;

public final class PieceImplMonochrome implements Piece {
    
    public final static Pixel NONE_PIECE  =  new PixelImplConsole(' ');
    public final static Pixel WHITE_PIECE =  new PixelImplConsole('●');
    public final static Pixel BLACK_PIECE =  new PixelImplConsole('○');
    public final static Pixel VALID_MOVE  =  new PixelImplConsole('·');

    private Player player;

    public PieceImplMonochrome() {
        this.player = Player.NONE;
    }

    /**
     * manually set piece type
     * designed for pieceGrid initialization
     * @param player Piece.type
     */
    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Get the pixel form of this piece
     * @return Pixel
     */
    @Override
    public Pixel getPixel() {
        return switch (this.player) {
            case WHITE -> WHITE_PIECE;
            case BLACK -> BLACK_PIECE;
            default -> NONE_PIECE;
        };
    }
}