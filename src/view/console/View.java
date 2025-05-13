package view.console;

import model.structs.Point;
import model.structs.Rect;

public interface View {
    boolean move(Rect rect);

    int setPixel(Point position, Pixel pix);
    int print(Point position, String str);
    int println(int y, String str);
    void paint();
    void clearView();
}
