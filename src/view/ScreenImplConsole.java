package view;

import model.structs.Point;
import model.structs.Rect;

import java.util.ArrayList;

public class ScreenImplConsole implements Screen {
    private final Pixel[][] buffer;
    private final Rect rect;
    private final ArrayList<Window> windows;
    private int dirty;

    public ScreenImplConsole(int height, int width) {
        this.dirty = 0;
        this.rect = new Rect(0, height, 0, width);
        this.buffer = new Pixel[rect.bottom][rect.right];
        this.windows = new ArrayList<>();

        for(int i = rect.top; i < rect.bottom; i++) {
            for(int j = rect.left; j < rect.right; j++) {
                buffer[i][j] = new PixelImplConsole();
            }
        }
    }


    /**
     * get a view to draw on
     * @param rect the rect of view.
     * @return Window if allocated
     */
    @Override
    public Window createWindow(Rect rect) {
        Window window = new WindowImplConsole(rect, this);
        windows.add(window);
        return window;
    }

    @Override
    public Rect getRect() {
        return rect;
    }

    /**
     * set pixel on screen
     * set dirty to true if really changed
     *
     * @param position pixel position
     * @param pix the pixel 'color'
     * @return dirty since we don't care one pixel but the whole screen
     */
    protected int setPixel(Point position, Pixel pix) {
        dirty += buffer[position.y][position.x].set(pix);
        return dirty;
    }

    /**
     * display the screen, force refresh by default
     * @return true when repainted
     */
    @Override
    public boolean paint() {
        if(dirty == 0) {
            return false;
        }
        return forcePaint();
    }

    @Override
    public boolean forcePaint() {
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

    @Override
    public int clearScreenBuffer() {
        PixelImplConsole pixelBlank = new PixelImplConsole();
        for(int i = rect.top; i < rect.bottom; i++) {
            for(int j = rect.left; j < rect.right; j++) {
                buffer[i][j].set(pixelBlank);
            }
            System.out.print('\n');
        }
        return 0;
    }
}
