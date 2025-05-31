package model.rules.inputRule

import model.pieces.PieceImplMonochrome
import model.rules.inputRule.InputRule.Companion.parseCol
import model.rules.inputRule.InputRule.Companion.parseRow
import model.structs.Move
import model.structs.Point

class InputRuleImplMonochrome private constructor() : InputRule {
    override fun parseInput(input: String): Move? {
        if (input.length != 2) {
            return null
        }
        return Move(
            Point(0, 0),
            Point(
                parseCol(input[1]),
                parseRow(input[0])
            ),
            PieceImplMonochrome()
        )
    }

    companion object {
        val inputRule: InputRuleImplMonochrome = InputRuleImplMonochrome()
    }
}
