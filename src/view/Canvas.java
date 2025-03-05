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

    // set pixel on canvas
    // dirty if really changed
    // return dirty since we don't care one pixel but the whole canvas
    public boolean setPixel(int x, int y, Pixel pix) {
        dirty |= buffer[y][x].set(pix);
        return dirty;
    }

    // force refresh by default
    public boolean paint() {
        return paint(false);
    }
    // show the canvas, return true when repainted
    // refuse to work if not forced and no change
    public boolean paint(boolean force_refresh) {
        if(!dirty && !force_refresh)
            return false;
        clear_screen();
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

    // canvas clear the screen
    public static void clear_screen(){
         try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
