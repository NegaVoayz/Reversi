package view;

public interface Pixel {
    int set(Pixel pix);

    void paint();

    void flush();
}
