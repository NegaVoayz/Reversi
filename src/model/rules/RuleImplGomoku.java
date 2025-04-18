package model.rules;

public class RuleImplGomoku implements Rule {

    private static final RuleImplGomoku instance = new RuleImplGomoku();

    public static RuleImplGomoku getRule() {
        return instance;
    }

    private RuleImplGomoku() {}

    final InputRule inputRule = InputRuleImplMonochrome.getInputRule();
    final GameRule gameRule = GameRuleImplGomoku.getGameRule();

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
