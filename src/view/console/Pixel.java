package view.console;

public interface Pixel {
    int set(Pixel pix);

    String paint();

    void flush();
}
