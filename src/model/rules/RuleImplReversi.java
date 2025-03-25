package model.rules;

public class RuleImplReversi implements Rule {
    static final InputRule inputRule = new InputRuleImplMonochrome();
    static final GameRule gameRule = new GameRuleImplReversi();

    @Override
    public GameRule getGameRule() {
        return gameRule;
    }

    @Override
    public InputRule getInputRule() {
        return inputRule;
    }

    @Override
    public String getRuleName() {
        return "reversi";
    }
}
