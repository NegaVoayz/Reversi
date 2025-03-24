package model;

import model.enums.Player;
import view.Pixel;

public interface Piece {

    void setPlayer(Player player);

    Player getPlayer();

    Pixel getPixel();
}
