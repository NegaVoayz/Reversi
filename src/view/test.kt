package view

import model.structs.Rect
import view.components.*
import view.renderer.Renderer
import view.renderer.RendererFactory

object test {
    @JvmStatic
    fun main(args: Array<String>) {
        val renderer: Renderer? = checkNotNull(RendererFactory.getRenderer("console"))
        val view = buildView()
        renderer!!.render(view)
    }


    fun buildView(): DisplayBlock {
        val view = DisplayBlock(Rect(0, 0, 0, 0), AlignType.MIDDLE, AlignType.BEGIN)
        view.childLayout = ChildLayout.FLEX_X
        view.addChild(initializeGrid(8, 8))
        val statsView = DisplayBlock(
            Rect(0, 0, 0, 0),
            AlignType.MIDDLE, AlignType.BEGIN
        )
        statsView.childLayout = ChildLayout.UP_TO_DOWN
        statsView.addChild(
            TextBlock(
                Rect(0, 0, 0, 0),
                AlignType.MIDDLE, AlignType.MIDDLE,
                "Game"
            )
        )
        statsView.addChild(
            TextBlock(
                Rect(0, 0, 0, 0),
                AlignType.MIDDLE, AlignType.BEGIN,
                "BlackPlayer"
            )
        )
        statsView.addChild(
            TextBlock(
                Rect(0, 0, 0, 0),
                AlignType.MIDDLE, AlignType.BEGIN,
                "WhitePlayer"
            )
        )
        view.addChild(statsView)
        return view
    }

    /**
     * Initializes the board canvas with row/column labels.
     *
     * @param height height of the board
     * @param width width of the board
     */
    private fun initializeGrid(height: Int, width: Int): AbstractDisplayBlock {
        val gridBlock = GridBlock(
            Rect(0, 0, 0, 1),
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
                grid[y]!![0] = '0'.code + y
                y++
            }
        }
        for (y in 10..height) {
            grid[y]!![0] = 'A'.code + y - 10
        }

        // Show column letters (A-Z)
        for (x in 1..width) {
            grid[0]!![x] = 'A'.code + x - 1
        }
        return gridBlock
    }
}
