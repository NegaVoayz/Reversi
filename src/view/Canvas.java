package view;

public class Canvas {
    private Screen screen;
    private Rect rect;
    private int dirty;

    public Canvas(int startX, int startY, Screen screen) {
        this(startX, startY, 0, 0, screen);
    }

    public Canvas(int startX, int startY, int height, int width, Screen screen) {
        dirty = 0;
        rect = new Rect(startY, startY + height, startX, startX + width);
        screen.allocateCanvas(rect);
        this.screen = screen;
    }

    /**
     * resize the canvas
     * 
     * @param height
     * @param width
     * @return true if succeeded
     */
    public boolean resize(int height, int width) {
        screen.freeCanvas(rect);
        rect.bottom = rect.top + height;
        rect.right = rect.left + width;
        if(!screen.allocateCanvas(rect)) {
            rect.bottom = rect.top;
            rect.right = rect.left;
            return false;
        }
        return true;
    }

    /**
     * set pixel on canvas
     * set dirty to true if really changed
     * 
     * @param x
     * @param y
     * @param pix
     * @return dirty since we don't care one pixel but the whole canvas
     * @throws IndexOutOfBoundsException when position out of bound
     */
    public int setPixel(int x, int y, Pixel pix) {
        if( x < 0 || x >= rect.right - rect.left ||
            y < 0 || y >= rect.bottom - rect.top) {
            throw new IndexOutOfBoundsException("Canvas: position out of bound");
        }
        dirty += screen.setPixel(rect.left + x, rect.top + y, pix);
        return dirty;
    }

    /**
     * print string on canvas
     * @param x
     * @param y
     * @param str
     * @return number of characters printed
     */
    public int print(int x, int y, String str) {
        int cnt = 0;
        for(int i = 0; i < str.length(); i++) {
            cnt+=setPixel(x+i, y, new Pixel(str.charAt(i)));
        }
        return cnt;
    }

    /**
     * print string on canvas, rewrite the whole line
     * @param y
     * @param str
     * @return number of characters changed
     */
    public int println(int y, String str) {
        int cnt = 0;
        int i;
        for(i = 0; i < str.length(); i++) {
            cnt+=setPixel(i, y, new Pixel(str.charAt(i)));
        }
        Pixel temp = new Pixel();
        for(; i < rect.right - rect.left; i++ ) {
            cnt+=setPixel(i, y, temp);
        }
        return cnt;
    }

    /**
     * show the canvas, force refresh by default
     * 
     * @return true when repainted
     */
    public boolean forcePaint() {
        return paint(true);
    }
    
    /**
     * show the canvas
     * refuse to work if not forced and no change
     * 
     * @param forceRefresh force it to refresh
     * @return true when repainted
     */
    public boolean paint(boolean forceRefresh) {
        if(dirty == 0 && !forceRefresh)
            return false;
        screen.paint(forceRefresh);
        dirty = 0;
        return true;
    }

    /**
     * clear the canvas
     * 
     * @return dirty since we don't care one pixel but the whole canvas
     */
    public boolean clearCanvas(){
        Pixel pix = new Pixel();
        for(int y = rect.top; y < rect.bottom; y++) {
            for(int x = rect.left; x < rect.right; x++) {
                dirty |= screen.setPixel(x, y, pix);
            }
        }
        return paint(false);
    }
}
