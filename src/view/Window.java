package view;

import model.structs.Point;
import model.structs.Rect;

import java.io.IOException;

public interface Window {
    public View createView(Rect rect);

    public int setPixel(Point position, Pixel pix);

    public boolean paint();

    boolean allocateView(Rect expectedRect);

    boolean freeView(Rect previousRect);

    public boolean forcePaint();

    public static void clear(){
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (InterruptedException | IOException _) {/*I don't care!*/}
    }

    int clearWindowBuffer();
}
