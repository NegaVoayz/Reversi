package view;

import model.Point;
import model.Rect;

public class WindowImplConsole implements Window {
    private final Pixel[][] buffer;
    private final boolean[][] occupied;
    private final Rect rect;
    private int dirty;

    public WindowImplConsole(int height, int width) {
        dirty = 0;
        this.rect = new Rect(0, height, 0, width);
        buffer = new Pixel[rect.bottom][rect.right];
        occupied = new boolean[rect.bottom][rect.right];

        for(int i = rect.top; i < rect.bottom; i++) {
            for(int j = rect.left; j < rect.right; j++) {
                buffer[i][j] = new PixelImplConsole();
                occupied[i][j] = false;
            }
        }
    }

    /**
     * set pixel on screen
     * set dirty to true if really changed
     * 
     * @param position pixel position
     * @param pix the pixel 'color'
     * @return dirty since we don't care one pixel but the whole screen
     */
    @Override
    public int setPixel(Point position, Pixel pix) {
        dirty += buffer[position.y][position.x].set(pix);
        return dirty;
    }

    /**
     * get a view to draw on
     * @param rect the rect of view.
     * @return Canvas if allocated
     */
    @Override
    public View createView(Rect rect) {
        if(!allocateView(rect)) return null;
        return new ViewImplConsole(rect, this);
    }

    /**
     * try to allocate canvas
     * overlap is not allowed
     * @param expectedRect the expected rect of canvas.
     * @return true if succeeded
     */
    @Override
    public boolean allocateView(Rect expectedRect) {

        if( expectedRect.left < rect.left ||
            expectedRect.right > rect.right ||
            expectedRect.top < rect.top ||
            expectedRect.bottom > rect.bottom ) {
            return false;
        }
        // check
        for(int i = expectedRect.top; i < expectedRect.bottom; i++) {
            for(int j = expectedRect.left; j < expectedRect.right; j++) {
                if(occupied[i][j]) {
                    return false;
                }
            }
        }

        // allocate
        for(int i = expectedRect.top; i < expectedRect.bottom; i++) {
            for(int j = expectedRect.left; j < expectedRect.right; j++) {
                occupied[i][j] = true;
            }
        }

        return true;
    }

    /**
     * free canvas area
     * 
     * @param previousRect the previous rect of an canvas
     * @return true if succeeded
     */
    @Override
    public boolean freeView(Rect previousRect) {

        for(int i = previousRect.top; i < previousRect.bottom; i++) {
            for(int j = previousRect.left; j < previousRect.right; j++) {
                occupied[i][j] = false;
            }
        }

        return true;
    }

    /**
     * display the screen, force refresh by default
     * @return true when repainted
     */
    @Override
    public boolean forcePaint() {
        Window.clear();
        for(int i = rect.top; i < rect.bottom; i++) {
            for(int j = rect.left; j < rect.right; j++) {
                buffer[i][j].paint();
                buffer[i][j].flush();
            }
            System.out.print('\n');
        }
        dirty = 0;
        return true;
    }
    
    /**
     * display the screen
     * refuse to work if not forced and no change
     *
     * @return true when repainted
     */
    @Override
    public boolean paint() {
        if(dirty == 0)
            return false;
        return forcePaint();
    }


    /**
     * clear the screen buffer
     * @return 0 if empty before
     */
    @Override
    public int clearWindowBuffer(){
        Pixel pix = new PixelImplConsole();
        Point point = new Point(0,0);
        for(point.y = rect.top; point.y < rect.bottom; point.y++) {
            for(point.x = rect.left; point.x < rect.right; point.x++) {
                dirty += setPixel(point, pix);
            }
        }
        return dirty;
    }
}
