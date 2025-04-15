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

    public static final String name = "peace";
    @Override
    public String getName() { return name; }

    private static final boolean showRound = false;
    @Override
    public boolean showRound() { return showRound; }
}
