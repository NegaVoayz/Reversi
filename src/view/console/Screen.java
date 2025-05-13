package view.console;

import model.structs.Rect;

import java.io.IOException;

public interface Screen {
    Window createWindow(Rect rect);

    Rect getRect();

    boolean paint();

    boolean forcePaint();

    static void clear(){
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (InterruptedException | IOException _) {/*I don't care!*/}
    }

    int clearScreenBuffer();
}
