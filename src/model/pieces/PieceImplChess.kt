package model.pieces

import model.enums.ChessPieceType

class PieceImplChess : Piece() {
    @JvmField
    var pieceType: ChessPieceType

    init {
        pieceType = ChessPieceType.NONE
    }

    public override fun setPiece(piece: Piece?) {
        require(piece is PieceImplChess)
        this.pieceType = piece.pieceType
        this.player = piece.player
    }

    override val code: Int
        get() = 0
}
