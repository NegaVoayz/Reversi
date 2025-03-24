package view;

import model.Point;
import model.Rect;

public interface View {
    boolean move(Rect rect);

    public int setPixel(Point position, Pixel pix);
    public int print(Point position, String str);
    public int println(int y, String str);
    public boolean paint();
    public boolean forcePaint();

    boolean clearView();
}
