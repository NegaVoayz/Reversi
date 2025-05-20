package model.rules;

import model.rules.displayRule.DisplayRule;
import model.rules.displayRule.DisplayRuleImplGomoku;
import model.rules.gameRule.GameRule;
import model.rules.gameRule.GameRuleImplGomoku;
import model.rules.inputRule.InputRule;
import model.rules.inputRule.InputRuleImplMonochrome;

public class RuleImplGomoku implements Rule {

    private static final RuleImplGomoku instance = new RuleImplGomoku();

    public static RuleImplGomoku getRule() {
        return instance;
    }

    private RuleImplGomoku() {}

    final InputRule inputRule = InputRuleImplMonochrome.getInputRule();
    final GameRule gameRule = GameRuleImplGomoku.getGameRule();
    final DisplayRule displayRule = DisplayRuleImplGomoku.getDisplayRule();

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

    public static final String name ="gomoku";
    @Override
    public String getName() { return name; }
}
