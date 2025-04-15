package model.rules;

public class RuleImplGomoku implements Rule {
    final InputRule inputRule = new InputRuleImplMonochrome();
    final GameRule gameRule = new GameRuleImplGomoku();

    @Override
    public GameRule getGameRule() {
        return gameRule;
    }

    @Override
    public InputRule getInputRule() {
        return inputRule;
    }

    public static final String name ="gomoku";
    @Override
    public String getName() { return name; }

    private static final boolean showRound = true;
    @Override
    public boolean showRound() { return showRound; }
}
