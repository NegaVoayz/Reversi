package model.rules.inputRule

import model.structs.Move

interface InputRule {
    fun parseInput(input: String): Move?

    companion object {
        /**
         * Converts a row character code to its corresponding integer value.
         *
         * @param code The character code representing the row.
         * @return The integer value of the row.
         */
        @JvmStatic
        fun parseRow(code: Char): Int {
            return if (code <= '9') code.code - '0'.code else if (code <= 'Z') code.code - 'A'.code + 10 else if (code <= 'z') code.code - 'a'.code + 10 else -1
        }

        /**
         * Converts a column character code to its corresponding integer value.
         *
         * @param code The character code representing the column.
         * @return The integer value of the column.
         */
        @JvmStatic
        fun parseCol(code: Char): Int {
            return if (code <= 'Z') code.code - 'A'.code + 1 else code.code - 'a'.code + 1
        }
    }
}
