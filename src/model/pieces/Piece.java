package model.pieces;

import model.enums.Player;
import view.Pixel;

public abstract class Piece {

    private Player player;

    Piece() {
        player = Player.NONE;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public abstract void setPiece(Piece piece);

    public abstract Pixel getPixel();
}
