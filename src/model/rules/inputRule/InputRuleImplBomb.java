package model.rules.inputRule;

import model.enums.BombPieceType;
import model.pieces.PieceImplBomb;
import model.structs.Move;
import model.structs.Point;

public class InputRuleImplBomb implements InputRule {

    private static final InputRuleImplBomb instance = new InputRuleImplBomb();

    public static InputRuleImplBomb getInputRule() {
        return instance;
    }

    @Override
    public Move ParseInput(String input) {
        boolean isBomb =
                input.charAt(0) == PieceImplBomb.CRATER_PIECE &&
                        !(input = input.substring(1)).isEmpty();
        if(input.length() != 2) {
            return null;
        }
        PieceImplBomb piece = new PieceImplBomb();
        piece.setType(
                isBomb ?
                BombPieceType.CRATER :
                BombPieceType.NORMAL);
        return new Move(
                new Point(),
                new Point(
                        InputRule.parseCol(input.charAt(1)),
                        InputRule.parseRow(input.charAt(0))),
                piece
        );
    }
}
