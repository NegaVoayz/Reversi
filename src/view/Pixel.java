package view;

public class Pixel {
    // use string to contain multi-byte character 
    private String data;
    private String lastPainted;

    /**
     * blank by default
     */
    public Pixel() {
        data = " ";
        lastPainted = "";
    }

    /**
     * @param character multi-byte character
     */
    public Pixel(String character) {
        data = character;
        lastPainted = "";
    }

    /**
     * @param character normal one-byte character
     */
    public Pixel(char character) {
        data = ""+character;
        lastPainted = "";
    }

    /**
     * set data using another pixel
     * 
     * @param pix
     * @return 1 if data changed, 0 if not changed, -1 if changed back
     */
    public int set(Pixel pix) {
        if(this.lastPainted.equals(pix.data)) {
            if(this.data.equals(pix.data)) {
                return 0;
            } else {
                this.data = pix.data;
                return -1;
            }
        } else {
            this.data = pix.data;
            return 1;
        }
    }

    /**
     * @return character to be paint
     */
    public String get() {
        return data;
    }

    /**
     * flush buffer
     */
    public void flush() {
        lastPainted = data;
        return;
    }
}