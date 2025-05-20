package view.components;

import model.structs.Rect;

public class GridBlock extends AbstractDisplayBlock{
    int[][] grid;

    public GridBlock(Rect margin, AlignType verticalAlign, AlignType horizontalAlign, int height, int width) {
        super(margin, verticalAlign, horizontalAlign);
        this.grid = new int[height][width];
    }

    public int[][] getGrid() {
        return grid;
    }

    @Override
    public int getAvailableWidth() {
        return grid[0].length * 2;
    }

    @Override
    public int getAvailableHeight() {
        return grid.length;
    }

    @Override
    protected void updateSize(AbstractDisplayBlock child) {}
}
