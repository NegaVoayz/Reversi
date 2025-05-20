package model.exceptions;

import model.structs.Point;

public class OccupiedPositionException extends InvalidMoveException {
    public OccupiedPositionException(int x, int y) {
        super(String.format("Position (%d,%d) already occupied", x, y));
    }
    public OccupiedPositionException(Point point) {
        this(point.x, point.y);
    }
}
