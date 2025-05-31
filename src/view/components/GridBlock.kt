package view.components

import model.structs.Rect

class GridBlock(margin: Rect, verticalAlign: AlignType?, horizontalAlign: AlignType?, height: Int, width: Int) :
    AbstractDisplayBlock(margin, verticalAlign, horizontalAlign) {
    var grid: Array<IntArray> = Array(height) { IntArray(width) }

    override val availableWidth: Int
        get() = grid[0].size * 2

    override val availableHeight: Int
        get() = grid.size

    override fun updateSize(child: AbstractDisplayBlock?) {}
}
