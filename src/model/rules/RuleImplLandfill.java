package model.rules;

public class RuleImplLandfill implements Rule {
    final InputRule inputRule = new InputRuleImplMonochrome();
    final GameRule gameRule = new GameRuleImplLandfill();

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
        return "peace";
    }
}
