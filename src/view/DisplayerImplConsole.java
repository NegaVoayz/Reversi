package view;

import model.Board;
import model.enums.AlignType;
import model.structs.Point;
import model.structs.Rect;
import view.console.Screen;
import view.console.ScreenImplConsole;
import view.console.View;
import view.console.Window;

import java.util.ArrayList;

public class DisplayerImplConsole implements Displayer {

    // Board Display Area on Screen (y1, y2, x1, x2)
    public static final Rect BOARD_RECT = new Rect(0, 10, 0, 80);

    // Game List Display Area on Screen (y1, y2, x1, x2)
    public static final Rect BOARD_LIST_RECT = new Rect(0, 10, 80, 120);

    // Game List Display Size (0, ySize, 0, xSize)
    public static final Rect BOARD_LIST_SIZE = new Rect(0, 10, 0, 32);

    final Screen screen;
    // Display Window
    private final Window boardsWindow;
    private final View boardsView;

    public DisplayerImplConsole() {
        this.screen = new ScreenImplConsole(12, 120);;
        this.boardsWindow = screen.createWindow(BOARD_LIST_RECT);
        this.boardsView = boardsWindow.createView(BOARD_LIST_SIZE);
    }

    /**
     *
     */
    @Override
    public Painter getPainter(AlignType verticalAlign, AlignType horizontalAlign, int height, int width) {
        Point viewStart = new Point(
                applyAlign(horizontalAlign, width*2, BOARD_RECT.right - BOARD_RECT.left),
                applyAlign(verticalAlign, height, BOARD_RECT.bottom - BOARD_RECT.top));
        return new PainterImplConsole(
                screen.createWindow(BOARD_RECT),
                viewStart,
                height,
                width);
    }

    /**
     * @param boards
     * @param currentBoardIdx
     */
    @Override
    public void updateBoards(ArrayList<Board> boards, int currentBoardIdx) {
        // calculate the start position
        // align current board in the middle
        int startY = 1;
        int startBoard = currentBoardIdx-3;
        if(startBoard < 0) {
            startY = 1-startBoard;
            startBoard = 0;
        }

        boardsView.clearView();

        // show omit symbol if the boards above exceeded the window
        if(currentBoardIdx-3 > 0) {
            boardsView.println(0,"    ...");
        }

        // show omit symbol if the boards below exceeded the window
        if(currentBoardIdx+5 < boards.size()) {
            boardsView.println(9,"    ...");
        }

        // display the game list visible
        for(int i = startY, j = startBoard; i < 9 && j < boards.size(); i++, j++) {
            if(j == currentBoardIdx) {
                boardsView.println(i, "--> Board " + (j + 1) + ": " + boards.get(j).getBriefInformation());
            } else {
                boardsView.println(i, "    Board " + (j + 1) + ": " + boards.get(j).getBriefInformation());
            }
        }

        boardsWindow.paint();
    }

    /**
     *
     */
    @Override
    public void display() {
        screen.paint();
    }

    /**
     * Applies alignment settings to view start position.
     */
    private static int applyAlign(AlignType align, int size, int capacity) {
        return switch (align) {
            case BEGIN ->  0;
            case MIDDLE -> (capacity - size) / 2;
            case END -> capacity - size;
        };
    }
}
