package view;

import model.structs.Point;
import model.structs.Rect;

public class ViewImplConsole implements View {
    private final Window window;
    private Rect rect;
    private int dirty;

    protected ViewImplConsole(Rect rect, Window window) {
        dirty = 0;
        this.rect = rect;
        this.window= window;
    }

    /**
     * move the canvas
     * 
     * @param rect the desired new rect
     * @return true if succeeded
     */
    @Override
    public boolean move(Rect rect) {
        window.freeView(this.rect);
        if(!window.allocateView(rect)) {
            this.rect = rect;
            return false;
        }
        rect.top = 0;
        rect.left = 0;
        rect.right = 0;
        rect.bottom = 0;
        return true;
    }

    /**
     * set pixel on canvas
     * set dirty to true if really changed
     *
     * @param position pixel position
     * @param pix the pixel 'color'
     * @return dirty since we don't care one pixel but the whole canvas
     * @throws IndexOutOfBoundsException when position out of bound
     */
    @Override
    public int setPixel(Point position, Pixel pix) {
        if( position.x < 0 || position.x >= rect.right - rect.left ||
            position.y < 0 || position.y >= rect.bottom - rect.top) {
            throw new IndexOutOfBoundsException("ViewImplConsole: position out of bound");
        }
        dirty += window.setPixel(new Point(rect.left + position.x, rect.top + position.y), pix);
        return dirty;
    }

    /**
     * print string on canvas
     * @param position the starting x-position
     * @param str the string to be print
     * @return number of characters printed
     */
    @Override
    public int print(Point position, String str) {
        int cnt = 0;
        Point shift = new Point(1,0);
        for(int i = 0; i < str.length(); i++) {
            cnt+=setPixel(position, new PixelImplConsole(str.charAt(i)));
            position.translate(shift);
        }
        return cnt;
    }

    /**
     * print string on canvas, rewrite the whole line
     * @param y the starting line position
     * @param str the string to be print
     * @return number of characters changed
     */
    @Override
    public int println(int y, String str) {
        int cnt = 0;
        Point position = new Point(0,y);
        for(position.x = 0; position.x < str.length(); position.x++) {
            cnt+=setPixel(position, new PixelImplConsole(str.charAt(position.x)));
        }
        Pixel temp = new PixelImplConsole();
        for(; position.x < rect.right - rect.left; position.x++ ) {
            cnt+=setPixel(position, temp);
        }
        return cnt;
    }

    /**
     * show the canvas
     */
    @Override
    public void paint() {
        window.paint();
    }

    /**
     * clear the canvas
     */
    @Override
    public void clearView(){
        Pixel pix = new PixelImplConsole();
        for(int y = rect.top; y < rect.bottom; y++) {
            for(int x = rect.left; x < rect.right; x++) {
                dirty |= window.setPixel(new Point(x, y), pix);
            }
        }
        paint();
    }
}
