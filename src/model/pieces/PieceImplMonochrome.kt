package model.pieces

import model.enums.Player

class PieceImplMonochrome : Piece() {
    public override fun setPiece(piece: Piece?) {
        require(piece is PieceImplMonochrome)
        player = piece.player
    }

    override val code: Int
        /**
         * Get the pixel form of this piece
         *
         * @return Pixel
         */
        get() = when (player) {
                    Player.WHITE -> WHITE_PIECE
                    Player.BLACK -> BLACK_PIECE
                    else -> NONE_PIECE
                }.code

    companion object {
        const val NONE_PIECE: Char = ' '
        const val WHITE_PIECE: Char = '●'
        const val BLACK_PIECE: Char = '○'
        const val VALID_MOVE: Char = '·'
    }
}