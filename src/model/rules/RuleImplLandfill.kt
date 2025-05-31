package model.rules

import model.rules.displayRule.DisplayRule
import model.rules.displayRule.DisplayRuleImplReversi
import model.rules.gameRule.GameRule
import model.rules.gameRule.GameRuleImplLandfill
import model.rules.inputRule.InputRule
import model.rules.inputRule.InputRuleImplMonochrome

class RuleImplLandfill private constructor() : Rule {
    override val inputRule: InputRule = InputRuleImplMonochrome.inputRule
    override val gameRule: GameRule = GameRuleImplLandfill.gameRule
    override val displayRule: DisplayRule = DisplayRuleImplReversi.displayRule

    override val name: String
        get() = Companion.name

    companion object {
        val rule: RuleImplLandfill = RuleImplLandfill()

        const val name: String = "peace"
    }
}
