package model.rules;

import model.rules.displayRule.DisplayRule;
import model.rules.displayRule.DisplayRuleImplReversi;
import model.rules.gameRule.GameRule;
import model.rules.gameRule.GameRuleImplReversi;
import model.rules.inputRule.InputRule;
import model.rules.inputRule.InputRuleImplMonochrome;
import model.rules.inputRule.InputRuleImplReversi;

public class RuleImplReversi implements Rule {

    private static final RuleImplReversi instance = new RuleImplReversi();

    public static RuleImplReversi getRule() {
        return instance;
    }

    private RuleImplReversi() {}

    final InputRule inputRule = InputRuleImplReversi.getInputRule();
    final GameRule gameRule = GameRuleImplReversi.getGameRule();
    final DisplayRule displayRule = DisplayRuleImplReversi.getDisplayRule();

    @Override
    public GameRule getGameRule() {
        return gameRule;
    }

    @Override
    public InputRule getInputRule() {
        return inputRule;
    }

    @Override
    public DisplayRule getDisplayRule() {
        return displayRule;
    }

    public static final String name = "reversi";
    @Override
    public String getName() { return name; }
}
