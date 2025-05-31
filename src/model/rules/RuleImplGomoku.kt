package model.rules

import model.rules.displayRule.DisplayRule
import model.rules.displayRule.DisplayRuleImplGomoku
import model.rules.gameRule.GameRule
import model.rules.gameRule.GameRuleImplGomoku
import model.rules.inputRule.InputRule
import model.rules.inputRule.InputRuleImplMonochrome

class RuleImplGomoku private constructor() : Rule {
    override val inputRule: InputRule = InputRuleImplMonochrome.inputRule
    override val gameRule: GameRule = GameRuleImplGomoku.gameRule
    override val displayRule: DisplayRule = DisplayRuleImplGomoku.displayRule

    override val name: String
        get() = Companion.name

    companion object {
        val rule: RuleImplGomoku = RuleImplGomoku()

        const val name: String = "gomoku"
    }
}
