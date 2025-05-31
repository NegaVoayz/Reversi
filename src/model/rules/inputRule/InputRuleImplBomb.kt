package model.rules.inputRule

import model.enums.BombPieceType
import model.pieces.PieceImplBomb
import model.rules.inputRule.InputRule.Companion.parseCol
import model.rules.inputRule.InputRule.Companion.parseRow
import model.structs.Move
import model.structs.Point

class InputRuleImplBomb : InputRule {
    override fun parseInput(input: String): Move? {
        var input = input
        val isBomb =
            input[0] == PieceImplBomb.CRATER_PIECE &&
                    !(input.substring(1).also { input = it }).isEmpty()
        if (input.length != 2) {
            return null
        }
        val piece = PieceImplBomb()
        piece.type = if (isBomb) BombPieceType.CRATER else BombPieceType.NORMAL
        return Move(
            Point(),
            Point(
                parseCol(input[1]),
                parseRow(input[0])
            ),
            piece
        )
    }

    companion object {
        val inputRule: InputRuleImplBomb = InputRuleImplBomb()
    }
}
