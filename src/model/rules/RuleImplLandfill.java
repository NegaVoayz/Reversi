package model.rules;

public class RuleImplLandfill implements Rule {
    static final InputRule inputRule = new InputRuleImplMonochrome();
    static final GameRule gameRule = new GameRuleImplLandfill();

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
        return "peace";
    }
}
