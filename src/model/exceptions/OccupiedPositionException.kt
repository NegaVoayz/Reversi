package model.exceptions

import model.structs.Point

class OccupiedPositionException(x: Int, y: Int) :
    InvalidMoveException(String.format("Position (%d,%d) already occupied", x, y)) {
    constructor(point: Point) : this(point.x, point.y)
}
