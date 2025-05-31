package model.pieces

import model.enums.BombPieceType
import model.enums.Player

class PieceImplBomb : Piece() {
    @JvmField
    var type: BombPieceType

    init {
        type = BombPieceType.NORMAL
    }

    override fun setPiece(piece: Piece?) {
        require(piece is PieceImplBomb)
        player = piece.player
        this.type = piece.type
    }

    override val code: Int
        /**
         * Get the pixel form of this piece
         *
         * @return Pixel
         */
        get() = when (type) {
                    BombPieceType.NORMAL -> when (player) {
                        Player.WHITE -> WHITE_PIECE
                        Player.BLACK -> BLACK_PIECE
                        else -> NONE_PIECE
                    }

                    BombPieceType.CRATER -> CRATER_PIECE
                    BombPieceType.BARRIER -> BARRIER_PIECE
                }.code

    companion object {
        const val NONE_PIECE: Char = ' '
        const val WHITE_PIECE: Char = '●'
        const val BLACK_PIECE: Char = '○'
        const val BARRIER_PIECE: Char = '#'
        const val CRATER_PIECE: Char = '@'
        const val VALID_MOVE: Char = '·'
    }
}