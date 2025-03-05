package view;

import java.io.IOException;

public class Canvas {
    private Pixel buffer[][];
    private int height;
    private int width;
    private boolean dirty;

    public Canvas(int height, int width) {
        dirty = false;
        this.height = height;
        this.width = width;
        buffer = new Pixel[height][width];

        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                buffer[i][j] = new Pixel();
            }
        }
    }

    /**
     * set pixel on canvas
     * set dirty to true if really changed
     * 
     * @param x
     * @param y
     * @param pix
     * @return dirty since we don't care one pixel but the whole canvas
     */
    public boolean setPixel(int x, int y, Pixel pix) {
        dirty |= buffer[y][x].set(pix);
        return dirty;
    }

    /**
     * show the canvas, force refresh by default
     * @return true when repainted
     */
    public boolean paint() {
        return paint(false);
    }
    
    /**
     * show the canvas
     * refuse to work if not forced and no change
     * 
     * @param forceRefresh force it to refresh
     * @return true when repainted
     */
    public boolean paint(boolean forceRefresh) {
        if(!dirty && !forceRefresh)
            return false;
        clearScreen();
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                System.out.print(buffer[i][j].get());
                System.out.print(' ');
            }
            System.out.print('\n');
        }
        dirty = false;
        return true;
    }

    /**
     * clear the screen
     */
    public static void clearScreen(){
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
