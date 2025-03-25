package model.structs;

import model.enums.Player;

public class Move {
    public Point start;
    public Point end;
    public Player player;
    public Move(Point start, Point end, Player player) {
        this.start  = new Point(start);
        this.end    = new Point(end);
        this.player = player;
    }
}
