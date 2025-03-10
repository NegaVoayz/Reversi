package view;

import java.io.IOException;

public class Screen {
    private final Pixel[][] buffer;
    private final boolean[][] occupied;
    private final Rect rect;
    private int dirty;

    public Screen(int height, int width) {
        dirty = 0;
        this.rect = new Rect(0, height, 0, width);
        buffer = new Pixel[rect.bottom][rect.right];
        occupied = new boolean[rect.bottom][rect.right];

        for(int i = rect.top; i < rect.bottom; i++) {
            for(int j = rect.left; j < rect.right; j++) {
                buffer[i][j] = new Pixel();
                occupied[i][j] = false;
            }
        }
    }

    /**
     * set pixel on screen
     * set dirty to true if really changed
     *
     * @param x the x-axis position
     * @param y the y-axis position
     * @param pix the pixel 'color'
     * @return dirty since we don't care one pixel but the whole screen
     */
    public int setPixel(int x, int y, Pixel pix) {
        dirty += buffer[y][x].set(pix);
        return dirty;
    }
    /**
     * set pixel on screen
     * set dirty to true if really changed
     * 
     * @param position pixel position
     * @param pix the pixel 'color'
     * @return dirty since we don't care one pixel but the whole screen
     */
    public int setPixel(Point position, Pixel pix) {
        return setPixel(position.x, position.y, pix);
    }

    /**
     * print string on screen
     * @param x the starting x-position
     * @param y the starting y-position
     * @param str the string to be print
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
     * @param y the starting line position
     * @param str the string to be print
     * @return number of characters changed
     */
    public int println(int y, String str) {
        int cnt = 0;
        int i;
        for(i = 0; i < str.length(); i++) {
            cnt+=setPixel(i, y, new Pixel(str.charAt(i)));
        }
        Pixel temp = new Pixel();
        for(; i < rect.right; i++ ) {
            cnt+=setPixel(i, y, temp);
        }
        return cnt;
    }

    /**
     * get a canvas to draw on
     * @param startX start y-position of canvas, the left-up corner.
     * @param startY start x-position of canvas, the left-up corner.
     * @return Canvas
     */
    public Canvas getCanvas(int startX, int startY) {
        return new Canvas(startX, startY, this);
    }

    /**
     * get a canvas to draw on
     * @param startPosition the left-up corner of the canvas.
     * @return Canvas
     */
    public Canvas getCanvas(Point startPosition) {
        return new Canvas(startPosition.x, startPosition.y, this);
    }

    /**
     * try to allocate canvas
     * overlap is not allowed
     * @param expectedRect the expected rect of canvas.
     * @return true if succeeded
     */
    protected boolean allocateCanvas(Rect expectedRect) {

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
    protected boolean freeCanvas(Rect previousRect) {

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
    public boolean paint() {
        return paint(false);
    }
    
    /**
     * display the screen
     * refuse to work if not forced and no change
     * 
     * @param forceRefresh force it to refresh
     * @return true when repainted
     */
    public boolean paint(boolean forceRefresh) {
        if(dirty == 0 && !forceRefresh)
            return false;
        clear();
        for(int i = rect.top; i < rect.bottom; i++) {
            for(int j = rect.left; j < rect.right; j++) {
                System.out.print(buffer[i][j].get());
                buffer[i][j].flush();
            }
            System.out.print('\n');
        }
        dirty = 0;
        return true;
    }

    /**
     * clear the screen
     */
    public static void clear(){
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (InterruptedException | IOException _) {
        }
    }

    /**
     * clear the screen buffer
     * @return 0 if empty before
     */
    public int clearScreenBuffer(){
        Pixel pix = new Pixel();
        for(int y = rect.top; y < rect.bottom; y++) {
            for(int x = rect.left; x < rect.right; x++) {
                dirty += setPixel(x, y, pix);
            }
        }
        return dirty;
    }
}
