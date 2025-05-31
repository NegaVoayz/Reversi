package model.rules

import model.rules.displayRule.DisplayRule
import model.rules.displayRule.DisplayRuleImplBomb
import model.rules.gameRule.GameRule
import model.rules.gameRule.GameRuleImplBomb
import model.rules.inputRule.InputRule
import model.rules.inputRule.InputRuleImplBomb

class RuleImplBomb private constructor() : Rule {
    override val inputRule: InputRule = InputRuleImplBomb.inputRule
    override val gameRule: GameRule = GameRuleImplBomb.gameRule
    override val displayRule: DisplayRule = DisplayRuleImplBomb.displayRule

    override val name: String
        get() = Companion.name

    companion object {
        val rule: RuleImplBomb = RuleImplBomb()

        const val name: String = "bomb"
    }
}
