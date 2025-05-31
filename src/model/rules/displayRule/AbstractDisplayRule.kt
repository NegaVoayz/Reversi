package model.rules.displayRule

import model.rules.Rule
import model.structs.GameStatistics
import model.structs.Rect
import view.components.AbstractDisplayBlock
import view.components.AlignType
import view.components.GridBlock

abstract class AbstractDisplayRule : DisplayRule {
    /**
     * @param statistics
     * @return
     */
    override fun update(statistics: GameStatistics, rule: Rule): Boolean {
        updateBoard(statistics)
        showValidMoves(statistics, rule)
        if (statistics.winner != null) {
            displayWinnerInfo(statistics, rule)
        } else {
            displayPlayerInfo(statistics, rule)
        }
        return true
    }

    /**
     * Initializes the board canvas with row/column labels.
     *
     * @param height height of the board
     * @param width width of the board
     */
    protected fun initializeGrid(height: Int, width: Int): AbstractDisplayBlock {
        val gridBlock = GridBlock(
            Rect(0, 0, 0, 3),
            AlignType.MIDDLE, AlignType.BEGIN,
            height + 1, width + 1
        )

        val grid = gridBlock.grid
        for (i in grid.indices) {
            for (j in grid[0].indices) {
                grid[i][j] = ' '.code
            }
        }

        // Show row numbers (1-9 then A-Z)
        run {
            var y = 1
            while (y <= height && y <= 9) {
                grid[y][0] = '0'.code + y
                y++
            }
        }
        for (y in 10..height) {
            grid[y][0] = 'A'.code + y - 10
        }

        // Show column letters (A-Z)
        for (x in 1..width) {
            grid[0][x] = 'A'.code + x - 1
        }
        return gridBlock
    }

    /**
     * Updates the display with current piece positions.
     *
     * @param statistics game statistics to be shown
     */
    protected fun updateBoard(statistics: GameStatistics) {
        require(statistics.view.children.first() is GridBlock) { "The view is not a GridBlock" }
        val gridBlock = statistics.view.children.first() as GridBlock

        val grid: Array<IntArray> = gridBlock.grid
        val pieceGrid = statistics.pieceGrid

        for (y in 1..statistics.height) {
            for (x in 1..statistics.width) {
                grid[y][x] = pieceGrid[y][x].code
            }
        }
    }

    /**
     * Highlights valid moves for the current player.
     */
    protected abstract fun showValidMoves(statistics: GameStatistics, rule: Rule)

    /**
     * Displays game winner information.
     * The winning player: (WHITE, BLACK, or NONE for draw)
     *
     * @param statistics game statistics
     */
    protected abstract fun displayWinnerInfo(statistics: GameStatistics, rule: Rule)

    /**
     * Displays current player information and scores.
     */
    protected abstract fun displayPlayerInfo(statistics: GameStatistics, rule: Rule)
}
