package model.rules.inputRule;

import model.structs.Move;

public interface InputRule {
    Move ParseInput(String input);

    /**
     * Converts a row character code to its corresponding integer value.
     *
     * @param code The character code representing the row.
     * @return The integer value of the row.
     */
    static int parseRow(char code) {
        return  code <= '9' ? code - '0' :
                code <= 'Z' ? code - 'A' + 10 :
                        code <= 'z' ? code - 'a' + 10 :
                                -1;
    }

    /**
     * Converts a column character code to its corresponding integer value.
     *
     * @param code The character code representing the column.
     * @return The integer value of the column.
     */
    static int parseCol(char code) {
        return  code <= 'Z' ?
                code - 'A' + 1 :
                code - 'a' + 1 ;
    }
}
