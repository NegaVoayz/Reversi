package model.rules.gameRule

import model.enums.GameType
import model.pieces.Piece
import model.pieces.PieceImplBomb
import model.pieces.PieceImplMonochrome

abstract class AbstractGameRuleMonochrome : GameRule {
    override val gameType: GameType
        get() = GameType.PLACE_PIECE

    fun basicInitializeGrid(height: Int, width: Int): Array<Array<Piece>> {
        val pieceGrid: Array<Array<Piece>> = Array(height + 2) {
            Array(width + 2) { PieceImplMonochrome() }
        }
        return pieceGrid
    }
}
