package model.rules;

public class RuleImplReversi implements Rule {

    private static final RuleImplReversi instance = new RuleImplReversi();

    public static RuleImplReversi getRule() {
        return instance;
    }

    private RuleImplReversi() {}

    final InputRule inputRule = InputRuleImplMonochrome.getInputRule();
    final GameRule gameRule = GameRuleImplReversi.getGameRule();

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
