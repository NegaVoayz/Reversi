package model.rules;

import model.rules.displayRule.DisplayRule;
import model.rules.displayRule.DisplayRuleImplReversi;
import model.rules.gameRule.GameRule;
import model.rules.gameRule.GameRuleImplLandfill;
import model.rules.inputRule.InputRule;
import model.rules.inputRule.InputRuleImplMonochrome;

public class RuleImplLandfill implements Rule {

    private static final RuleImplLandfill instance = new RuleImplLandfill();

    public static RuleImplLandfill getRule() {
        return instance;
    }

    private RuleImplLandfill() {}

    final InputRule inputRule = InputRuleImplMonochrome.getInputRule();
    final GameRule gameRule = GameRuleImplLandfill.getGameRule();
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

    public static final String name = "peace";
    @Override
    public String getName() { return name; }
}
