package view.console;

import model.structs.Point;
import model.structs.Rect;

public interface Window {

    View createView(Rect rect);

    int setPixel(Point position, Pixel pix);

    void paint();

    int print(Point position, String str);

    int println(int y, String str);

    boolean allocateView(Rect expectedRect);

    void freeView(Rect previousRect);

    void clearWindowBuffer();
}
