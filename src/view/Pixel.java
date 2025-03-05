package view;

public class Pixel {
    // use string to contain multi-byte character 
    private String data;

    /**
     * blank by default
     */
    public Pixel() {
        data = " ";
    }

    /**
     * @param character multi-byte character
     */
    public Pixel(String character) {
        data = character;
    }

    /**
     * @param character normal one-byte character
     */
    public Pixel(char character) {
        data = ""+character;
    }

    /**
     * set data using another pixel
     * 
     * @param pix
     * @return true if data changed
     */
    public boolean set(Pixel pix) {
        if(this.data == pix.data)
            return false;
        this.data = pix.data;
        return true;
    }

    /**
     * @return character to be paint
     */
    public String get() {
        return data;
    }
}