package model.rules.displayRule;

import model.pieces.Piece;
import model.rules.Rule;
import model.structs.GameStatistics;
import model.structs.Rect;
import view.components.AbstractDisplayBlock;
import view.components.AlignType;
import view.components.GridBlock;

public abstract class AbstractDisplayRule implements DisplayRule {

    /**
     * @param statistics
     * @return
     */
    @Override
    public boolean update(GameStatistics statistics, Rule rule) {
        updateBoard(statistics);
        showValidMoves(statistics, rule);
        if(statistics.getWinner() != null) {
            displayWinnerInfo(statistics, rule);
        } else {
            displayPlayerInfo(statistics, rule);
        }
        return true;
    }

    /**
     * Initializes the board canvas with row/column labels.
     *
     * @param height height of the board
     * @param width width of the board
     */
    protected AbstractDisplayBlock initializeGrid(int height, int width) {
        GridBlock gridBlock = new GridBlock(
                new Rect(0,0,0,3),
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

    /**
     * Updates the display with current piece positions.
     *
     * @param statistics game statistics to be shown
     */
    protected void updateBoard(GameStatistics statistics) {
        if(!(statistics.getView().getChildren().getFirst() instanceof GridBlock gridBlock)) {
            throw new IllegalArgumentException("The view is not a GridBlock");
        }

        int[][] grid = gridBlock.getGrid();
        Piece[][] pieceGrid = statistics.getPieceGrid();

        for(int y = 1; y <= statistics.getHeight(); y++) {
            for(int x = 1; x <= statistics.getWidth(); x++) {
                grid[y][x] = pieceGrid[y][x].getCode();
            }
        }
    }

    /**
     * Highlights valid moves for the current player.
     */
    protected abstract void showValidMoves(GameStatistics statistics, Rule rule);

    /**
     * Displays game winner information.
     * The winning player: (WHITE, BLACK, or NONE for draw)
     *
     * @param statistics game statistics
     */
    protected abstract void displayWinnerInfo(GameStatistics statistics, Rule rule);

    /**
     * Displays current player information and scores.
     */
    protected abstract void displayPlayerInfo(GameStatistics statistics, Rule rule);
}
