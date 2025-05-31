package model.rules

import model.rules.displayRule.DisplayRule
import model.rules.gameRule.GameRule
import model.rules.inputRule.InputRule

interface Rule {
    val gameRule: GameRule

    val inputRule: InputRule

    val displayRule: DisplayRule

    val name: String
        get() = "How the hell could you reach here?"
}
