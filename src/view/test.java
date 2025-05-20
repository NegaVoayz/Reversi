package view;

import model.structs.Rect;
import view.components.*;
import view.renderer.Renderer;
import view.renderer.RendererFactory;

public class test {
    public static void main(String[] args) {
        Renderer renderer = RendererFactory.getRenderer("console");
        assert renderer != null;
        DisplayBlock view = buildView();
        renderer.render(view);
    }


    public static DisplayBlock buildView() {
        DisplayBlock view = new DisplayBlock(new Rect(0,0,0,0), AlignType.MIDDLE, AlignType.BEGIN);
        view.setChildLayout(ChildLayout.FLEX_X);
        view.addChild(initializeGrid(8,8));
        DisplayBlock statsView = new DisplayBlock(
                new Rect(0,0,0,0),
                AlignType.MIDDLE, AlignType.BEGIN);
        statsView.setChildLayout(ChildLayout.UP_TO_DOWN);
        statsView.addChild(
                new TextBlock(
                        new Rect(0,0,0,0),
                        AlignType.MIDDLE, AlignType.MIDDLE,
                        "Game"));
        statsView.addChild(
                new TextBlock(
                        new Rect(0,0,0,0),
                        AlignType.MIDDLE, AlignType.BEGIN,
                        "BlackPlayer"));
        statsView.addChild(
                new TextBlock(
                        new Rect(0,0,0,0),
                        AlignType.MIDDLE, AlignType.BEGIN,
                        "WhitePlayer"));
        view.addChild(statsView);
        return view;
    }

    /**
     * Initializes the board canvas with row/column labels.
     *
     * @param height height of the board
     * @param width width of the board
     */
    private static AbstractDisplayBlock initializeGrid(int height, int width) {
        GridBlock gridBlock = new GridBlock(
                new Rect(0,0,0,1),
                AlignType.MIDDLE,AlignType.BEGIN,
                height+1, width+1);

        int[][] grid = gridBlock.getGrid();
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[0].length; j++) {
                grid[i][j] = ' ';
            }
        }

        // Show row numbers (1-9 then A-Z)
        for(int y = 1; y <= height && y <= 9; y++) {
            grid[y][0] = '0'+y;
        }
        for(int y = 10; y <= height; y++) {
            grid[y][0] = 'A'+y-10;
        }

        // Show column letters (A-Z)
        for(int x = 1; x <= width; x++) {
            grid[0][x] = 'A'+x-1;
        }
        return gridBlock;
    }
}
