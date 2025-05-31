package model.pieces

import model.enums.Player

abstract class Piece internal constructor() {
    @JvmField
    var player: Player = Player.NONE

    abstract fun setPiece(piece: Piece?)

    abstract val code: Int
}
