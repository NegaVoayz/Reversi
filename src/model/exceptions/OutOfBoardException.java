package model.exceptions;

import model.structs.Point;

public class OutOfBoardException extends InvalidMoveException {
    public OutOfBoardException(int x, int y) {
        super(String.format("Position (%d,%d) out of board", x, y));
    }
    public OutOfBoardException(Point point) {
        this(point.x, point.y);
    }
}
