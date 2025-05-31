package model.exceptions

import model.structs.Point

class OutOfBoardException(x: Int, y: Int) : InvalidMoveException(String.format("Position (%d,%d) out of board", x, y)) {
    constructor(point: Point) : this(point.x, point.y)
}
