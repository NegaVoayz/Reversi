package model.rules.inputRule;

import model.pieces.PieceImplMonochrome;
import model.structs.Move;
import model.structs.Point;

public class InputRuleImplMonochrome implements InputRule {

    private static final InputRuleImplMonochrome instance = new InputRuleImplMonochrome();

    public static InputRuleImplMonochrome getInputRule() {
        return instance;
    }

    private InputRuleImplMonochrome() {}

    @Override
    public Move ParseInput(String input) {
        if(input.length() != 2) {
            return null;
        }
        return new Move(
                new Point(0,0),
                new Point(
                        InputRule.parseCol(input.charAt(1)),
                        InputRule.parseRow(input.charAt(0))),
                new PieceImplMonochrome());
    }
}
