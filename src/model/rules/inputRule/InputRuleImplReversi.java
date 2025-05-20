package model.rules.inputRule;

import model.pieces.PieceImplMonochrome;
import model.structs.Move;
import model.structs.Point;

public class InputRuleImplReversi implements InputRule {

    private static final InputRuleImplReversi instance = new InputRuleImplReversi();

    public static InputRuleImplReversi getInputRule() {
        return instance;
    }

    private InputRuleImplReversi() {}

    @Override
    public Move ParseInput(String input) {
        if(input.equalsIgnoreCase("pass")) {
            return new Move(new Point(0, 0), new Point(0, 0));
        }
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
