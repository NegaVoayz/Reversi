package model.rules;

import model.rules.displayRule.DisplayRule;
import model.rules.gameRule.GameRule;
import model.rules.inputRule.InputRule;

public interface Rule {

    GameRule getGameRule();

    InputRule getInputRule();

    DisplayRule getDisplayRule();

    default String getName() { return "How the hell could you reach here?"; }
}
