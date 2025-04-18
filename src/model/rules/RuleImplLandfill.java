package model.rules;

public class RuleImplLandfill implements Rule {

    private static final RuleImplLandfill instance = new RuleImplLandfill();

    public static RuleImplLandfill getRule() {
        return instance;
    }

    private RuleImplLandfill() {}

    final InputRule inputRule = InputRuleImplMonochrome.getInputRule();
    final GameRule gameRule = GameRuleImplLandfill.getGameRule();

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
