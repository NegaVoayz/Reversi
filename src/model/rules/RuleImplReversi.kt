package model.rules

import model.rules.displayRule.DisplayRule
import model.rules.displayRule.DisplayRuleImplReversi
import model.rules.gameRule.GameRule
import model.rules.gameRule.GameRuleImplReversi
import model.rules.inputRule.InputRule
import model.rules.inputRule.InputRuleImplReversi

class RuleImplReversi private constructor() : Rule {
    override val inputRule: InputRule = InputRuleImplReversi.inputRule
    override val gameRule: GameRule = GameRuleImplReversi.gameRule
    override val displayRule: DisplayRule = DisplayRuleImplReversi.displayRule

    override val name: String
        get() = Companion.name

    companion object {
        val rule: RuleImplReversi = RuleImplReversi()

        const val name: String = "reversi"
    }
}
