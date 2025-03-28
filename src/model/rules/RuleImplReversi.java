package model.rules;

public class RuleImplReversi implements Rule {
    final InputRule inputRule = new InputRuleImplMonochrome();
    final GameRule gameRule = new GameRuleImplReversi();

    @Override
    public GameRule getGameRule() {
        return gameRule;
    }

    @Override
    public InputRule getInputRule() {
        return inputRule;
    }

    @Override
    public String toString() {
        return "reversi";
    }
}
