package model;

public class Point {
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point translate(Point p) {
        this.x += p.x;
        this.y += p.y;
        return this;
    }

    public Point detranslate(Point p) {
        this.x -= p.x;
        this.y -= p.y;
        return this;
    }

    public Point set(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Point set(Point point) {
        this.x = point.x;
        this.y = point.y;
        return this;
    }
}
