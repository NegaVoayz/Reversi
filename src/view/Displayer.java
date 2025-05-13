package view;

import model.Board;
import model.enums.AlignType;
import model.structs.GameStatistics;
import view.console.Screen;

import java.util.ArrayList;

public interface Displayer {

    DisplayerImplConsole displayerImplConsole = new DisplayerImplConsole();
    static Displayer getDisplayerImplConsole() {
        return displayerImplConsole;
    }

    Painter getPainter(AlignType verticalAlign, AlignType horizontalAlign, int height, int width);

    void updateBoards(ArrayList<Board> boards, int currentBoardIdx);

    void display();
}
