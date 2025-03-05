package model;

import view.Pixel;

public class Piece {
    enum Type{
        White,
        None,
        Black,
    }
    
    public final static char none_piece = ' ';
    public final static char white_piece = '○';
    public final static char black_piece = '●';
    public final static int directions[][] = {
        {1,1},
        {1,0},
        {1,-1},
        {0,1},
        {0,-1},
        {-1,1},
        {-1,0},
        {-1,-1}
    };

    //to gain access to neibours
    private Piece[][] grid;
    private Type type;
    private int x;
    private int y;

    public Piece(int x, int y, Piece[][] grid) {
        this.type = Type.None;
        this.x = x;
        this.y = y;
        this.grid = grid;
    }

    // manually set piece type
    // designed for grid initialization
    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }

    // What Color.
    public Pixel getPixel() {
        switch (this.type) {
            case Type.White:
                return new Pixel(white_piece);
            case Type.Black:
                return new Pixel(black_piece);
            default:
                return new Pixel(none_piece);
        }
    }

    // validation check function
    public boolean is_valid(Type expected_type) {
        if(this.type != Type.None) {
            return false;
        }
        for(int i = 0; i < 8; i++) {
            int dx = directions[i][0];
            int dy = directions[i][1];
            if(grid[y+dy][x+dx].flip(dx, dy, expected_type, false)>0) {
                return true;
            }
        }
        return false;
    }

    // require validation check
    // lay piece and automatically flip pieces
    public int lay_piece(Type type) {
        this.type = type;
        int count = 0;
        for(int i = 0; i < 8; i++) {
            int dx = directions[i][0];
            int dy = directions[i][1];
            int temp = grid[y+dy][x+dx].flip(dx, dy, type, true);
            if(temp > 0) {
                count += temp;
            }
        }
        return count;
    }

    // recursively check whether a flip is possible
    // apply change used to check.
    private int flip(int dx, int dy, Type type, boolean apply_change) {
        if(this.type == Type.None) {
            return -1; 
        }
        if(this.type == type) {
            return 0;
        }

        // try flip next
        int ans = grid[y+dy][x+dx].flip(dx, dy, type, apply_change);

        // if return number is negative, keep it below zero
        if(ans < 0) {
            return -1;
        }

        // change the grid if applied
        // or work as validation check only
        if(apply_change) {
            this.type = type;
        }

        // return type is the number of pieces flipped (or able to be flipped)
        // or negative number showing operation failed
        return ans + 1;
    }
}