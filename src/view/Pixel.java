package view;

public class Pixel {
    // use string to contain multi-byte character 
    private String data;

    // blank by default
    public Pixel() {
        data = " ";
    }

    // multi-byte character
    public Pixel(String ch) {
        data = ch;
    }

    // normal character
    public Pixel(char ch) {
        data = ""+ch;
    }

    // set data using another pixel to make things simple
    // return true if data changed
    public boolean set(Pixel pix) {
        if(this.data == pix.data)
            return false;
        this.data = pix.data;
        return true;
    }

    // return data for paint
    public String get() {
        return data;
    }
}