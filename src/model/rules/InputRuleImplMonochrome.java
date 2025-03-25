package model.rules;

import model.enums.Player;
import model.structs.Move;
import model.structs.Point;

public class InputRuleImplMonochrome implements InputRule {

    @Override
    public Move ParseInput(String input) {
        if(input.length() != 2) {
            return null;
        }
        return new Move(new Point(0,0), new Point(getCol(input.charAt(1)),getRow(input.charAt(0))), Player.NONE);
    }

    /**
     * Converts a column character code to its corresponding integer value.
     *
     * @param code The character code representing the column.
     * @return The integer value of the column.
     */
    private static int getCol(char code) {
        return (int)code-'A'+1;
    }

    /**
     * Converts a row character code to its corresponding integer value.
     *
     * @param code The character code representing the row.
     * @return The integer value of the row.
     */
    private static int getRow(char code) {
        if(code > '9') {
            return (int)code-'a'+10;
        } else {
            return (int)code-'0';
        }
    }
}
