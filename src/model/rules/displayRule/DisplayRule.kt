package model.rules.displayRule

import model.rules.Rule
import model.structs.GameStatistics

interface DisplayRule {
    fun buildView(statistics: GameStatistics, rule: Rule): Boolean

    fun update(statistics: GameStatistics, rule: Rule): Boolean
}
