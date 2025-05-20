package model.rules;

import model.rules.displayRule.DisplayRule;
import model.rules.displayRule.DisplayRuleImplBomb;
import model.rules.gameRule.GameRule;
import model.rules.gameRule.GameRuleImplBomb;
import model.rules.inputRule.InputRule;
import model.rules.inputRule.InputRuleImplBomb;

public class RuleImplBomb implements Rule {

        private static final RuleImplBomb instance = new RuleImplBomb();

        public static RuleImplBomb getRule() {
            return instance;
        }

        private RuleImplBomb() {}

        final InputRule inputRule = InputRuleImplBomb.getInputRule();
        final GameRule gameRule = GameRuleImplBomb.getGameRule();
        final DisplayRule displayRule = DisplayRuleImplBomb.getDisplayRule();

        @Override
        public GameRule getGameRule() {
            return gameRule;
        }

        @Override
        public InputRule getInputRule() {
            return inputRule;
        }

        @Override
        public DisplayRule getDisplayRule() {
            return displayRule;
        }

        public static final String name ="bomb";
        @Override
        public String getName() { return name; }
}
