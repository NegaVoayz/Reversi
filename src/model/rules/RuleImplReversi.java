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

    public static final String name = "reversi";
    @Override
    public String getName() { return name; }

    private static final boolean showRound = false;
    @Override
    public boolean showRound() { return showRound; }
}
