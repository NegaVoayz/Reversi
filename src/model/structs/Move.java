package model.structs;

import model.enums.Player;
import model.pieces.Piece;

public class Move {
    public final Point start;
    public final Point end;
    public final Piece piece;
    public Move(Point start, Point end, Piece piece) {
        this.start  = new Point(start);
        this.end    = new Point(end);
        this.piece  = piece;
    }
    public Move(Point start, Point end) {
        this.start  = new Point(start);
        this.end    = new Point(end);
        this.piece  = null;
    }
}
