package model.structs

import model.pieces.Piece

class Move {
    val start: Point
    val end: Point
    val piece: Piece?

    constructor(start: Point, end: Point, piece: Piece?) {
        this.start = Point(start)
        this.end = Point(end)
        this.piece = piece
    }

    constructor(start: Point, end: Point) {
        this.start = Point(start)
        this.end = Point(end)
        this.piece = null
    }
}
