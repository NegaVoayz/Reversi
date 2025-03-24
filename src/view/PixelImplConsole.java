package view;

public class PixelImplConsole implements Pixel {
    // use string to contain multibyte character
    private String data;
    private String lastPainted;

    /**
     * blank by default
     */
    public PixelImplConsole() {
        data = " ";
        lastPainted = "";
    }

    /**
     * @param character multibyte character
     */
    public PixelImplConsole(String character) {
        data = character;
        lastPainted = "";
    }

    /**
     * @param character normal one-byte character
     */
    public PixelImplConsole(char character) {
        data = ""+character;
        lastPainted = "";
    }

    /**
     * set data using another pixel
     * 
     * @param pix the pixel 'color'
     * @return 1 if data changed, 0 if not changed, -1 if changed back
     */
    public int set(Pixel pix) {
        // try to convert pixel
        if(!(pix instanceof PixelImplConsole pixelImplConsole)) {
            throw new IllegalArgumentException("Invalid Pixel implementation");
        }
        // lazy check
        if(this.lastPainted.equals(pixelImplConsole.data)) {
            if(this.data.equals(pixelImplConsole.data)) {
                return 0;
            } else {
                this.data = pixelImplConsole.data;
                return -1;
            }
        } else {
            this.data = pixelImplConsole.data;
            return 1;
        }
    }

    /**
     * @return character to be paint
     */
    public String get() {
        return data;
    }

    public void paint() {
        System.out.print(data);
    }

    /**
     * flush buffer
     */
    public void flush() {
        lastPainted = data;
        return;
    }
}