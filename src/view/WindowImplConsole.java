package view;

import model.structs.Point;
import model.structs.Rect;

public class WindowImplConsole implements Window {
    private final Pixel[][] buffer;
    private final boolean[][] occupied;
    private final Rect rect;
    private final Screen screen;
    private int dirty;

    protected WindowImplConsole(Rect rect, Screen screen) {
        this.buffer = new Pixel[rect.bottom-rect.top][rect.right-rect.left];
        this.occupied = new boolean[rect.bottom-rect.top][rect.right-rect.left];
        this.rect = rect;
        this.screen = screen;
        this.dirty = 0;

        for(int i = 0; i < rect.bottom - rect.top; i++) {
            for(int j = 0; j < rect.right - rect.left; j++) {
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

        if( expectedRect.left < 0 ||
            expectedRect.right > rect.right - rect.left ||
            expectedRect.top < 0 ||
            expectedRect.bottom > rect.bottom - rect.top ) {
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
     * @param previousRect the previous rect of a canvas
     */
    @Override
    public void freeView(Rect previousRect) {
        for(int i = previousRect.top; i < previousRect.bottom; i++) {
            for(int j = previousRect.left; j < previousRect.right; j++) {
                occupied[i][j] = false;
            }
        }
    }

    /**
     * display the screen, force refresh by default
     */
    @Override
    public void paint() {
        if(!(screen instanceof ScreenImplConsole screenImplConsole)) {
            throw new IllegalStateException("Screen is not a ScreenImplConsole");
        }
        Point position = new Point();
        for(position.y = rect.top; position.y < rect.bottom; position.y++) {
            for(position.x = rect.left; position.x < rect.right; position.x++) {
                screenImplConsole.setPixel(position, buffer[position.y-rect.top][position.x-rect.left]);
            }
        }
        screen.paint();
    }


    /**
     * clear the screen buffer
     */
    @Override
    public void clearWindowBuffer(){
        Pixel pix = new PixelImplConsole();
        Point point = new Point(0,0);
        for(point.y = rect.top; point.y < rect.bottom; point.y++) {
            for(point.x = rect.left; point.x < rect.right; point.x++) {
                setPixel(point, pix);
            }
        }
        paint();
    }
}
