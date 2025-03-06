package view;

import java.io.IOException;

public class Screen {
    private Pixel buffer[][];
    private boolean occupied[][];
    private Rect rect;
    private boolean dirty;

    public Screen(int height, int width) {
        dirty = false;
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
     * @param x
     * @param y
     * @param pix
     * @return dirty since we don't care one pixel but the whole screen
     */
    public boolean setPixel(int x, int y, Pixel pix) {
        dirty |= buffer[y][x].set(pix);
        return dirty;
    }
    /**
     * set pixel on screen
     * set dirty to true if really changed
     * 
     * @param position
     * @param pix
     * @return dirty since we don't care one pixel but the whole screen
     */
    public boolean setPixel(Point position, Pixel pix) {
        return setPixel(position.x, position.y, pix);
    }

    /**
     * print string on screen
     * @param x
     * @param y
     * @param str
     * @return number of characters printed
     */
    public int print(int x, int y, String str) {
        int cnt = 0;
        for(int i = 0; i < str.length(); i++) {
            if(setPixel(x+i, y, new Pixel(str.charAt(i)))) {
                cnt++;
            }
        }
        return cnt;
    }

    /**
     * get a canvas to draw on
     * @param startX 
     * @param startY
     * @return Canvas
     */
    public Canvas getCanvas(int startX, int startY) {
        return new Canvas(startX, startY, this);
    }

    /**
     * get a canvas to draw on
     * @param startPosition
     * @return Canvas
     */
    public Canvas getCanvas(Point startPosition) {
        return new Canvas(startPosition.x, startPosition.y, this);
    }

    /**
     * try allocate canvas
     * overlap is not allowed
     * @param expectedRect
     * @return
     */
    public boolean allocateCanvas(Rect expectedRect) {

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
     * @param previousRect
     * @return
     */
    public boolean freeCanvas(Rect previousRect) {

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
        if(!dirty && !forceRefresh)
            return false;
        clear();
        for(int i = rect.top; i < rect.bottom; i++) {
            for(int j = rect.left; j < rect.right; j++) {
                System.out.print(buffer[i][j].get());
            }
            System.out.print('\n');
        }
        dirty = false;
        return true;
    }

    /**
     * clear the screen
     */
    public static void clear(){
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * clear the screen buffer
     * @return true if not empty before
     */
    public boolean clearScreenBuffer(){
        Pixel pix = new Pixel();
        for(int y = rect.top; y < rect.bottom; y++) {
            for(int x = rect.left; x < rect.right; x++) {
                dirty |= setPixel(x, y, pix);
            }
        }
        return dirty;
    }
}
