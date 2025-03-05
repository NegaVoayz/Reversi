package model;

import view.Canvas;
import view.Pixel;

public class Board{
    public enum Player{
        White,
        Black
    }

    private Piece grid[][];
    private Canvas canvas;
    private int height;
    private int width;
    private Player current_player;
    private String white_player_name;
    private String black_player_name;
    private boolean end;

    // initiallization
    public Board(int height, int width) {
        if(height < 0 || height > 16 ||
            width < 0 || width > 16 ) {
            height = 8;
            width = 8;
        }
        this.height = height;
        this.width = width;
        this.current_player = Player.Black;
        grid = new Piece[height+2][width+2];
        canvas = new Canvas(height+2, width+2);
        init_canvas();
        init_grid();

        update_pieces();
        // well, it's possible some special grid size
        //   could cause an immediate endgame
        this.end = !get_vaild_moves(false);
    }

    // set players' name
    public void setName(String white_player_name, String black_player_name) {
        this.white_player_name = white_player_name;
        this.black_player_name = black_player_name;
    }

    // check whether game ends
    public boolean end() {
        return end;
    }

    // try lay piece at the position given
    // return true if succeeded
    public boolean lay_piece(int x, int y) {
        if(x <= 0 || x > width) {
            return false;
        }
        if(y <= 0 || y > height) {
            return false;
        }
        if(!grid[y][x].is_valid(get_piece_color())) {
            return false;
        }
        grid[y][x].lay_piece(get_piece_color());
        change_player();
        update_pieces();
        if(!get_vaild_moves(false)) {
            this.end = true;
        }
        return true;
    }

    // general paint
    public void paint() {
        canvas.paint(true);
        if(end()) {
            show_winner_info(get_winner());
        } else {
            show_player_info();
        }
    }

    // allocate grid and set the start pieces
    private void init_grid() {
        for(int i = 0; i < height+2; i++) {
            for(int j = 0; j < width+2; j++) {
                grid[i][j] = new Piece(j, i, grid);
            }
        }

        grid[height/2][width/2].setType(Piece.Type.White);
        grid[height/2][width/2+1].setType(Piece.Type.Black);
        grid[height/2+1][width/2].setType(Piece.Type.Black);
        grid[height/2+1][width/2+1].setType(Piece.Type.White);
    }

    // show edge scales
    private void init_canvas() {
        for(int i = 1; i <= height && i <= 9; i++) {
            canvas.setPixel(0, i, new Pixel((char)(48+i)));
        }
        for(int i = 10; i <= height; i++) {
            canvas.setPixel(0, i, new Pixel((char)(87+i)));
        }
        for(int j = 1; j <= width; j++) {
            canvas.setPixel(j, 0, new Pixel((char)(64+j)));
        }
    }

    // change player when his turn ends
    private void change_player(){
        if(current_player == Player.White) {
            current_player = Player.Black;
        } else {
            current_player = Player.White;
        }
        return;
    }

    // get current player's piece color
    // actually convert player type to piece type
    private Piece.Type get_piece_color(){
        if(current_player == Player.White) {
            return Piece.Type.White;
        } else {
            return Piece.Type.Black;
        }
    }

    // update pieces on canvas
    private void update_pieces() {
        for(int i = 1; i <= height; i++) {
            for(int j = 1; j <= width; j++) {
                canvas.setPixel(j, i, grid[i][j].getPixel());
            }
        }
    }

    // get and show valid moves for current player
    // automatically skip turns if no moves valid
    // if both players have no valid moves, set game end
    private boolean get_vaild_moves(boolean skipped) {
        boolean movable = false;
        Piece.Type type;
        if(current_player == Player.White) {
            type = Piece.Type.White;
        } else {
            type = Piece.Type.Black;
        }
        for(int i = 1; i <= height; i++) {
            for(int j = 1; j <= width; j++) {
                if(grid[i][j].is_valid(type)) {
                    movable = true;
                    canvas.setPixel(j, i, new Pixel('·'));
                }
            }
        }
        if(movable) {
            return true;
        }
        if(skipped) {
            return false;
        }
        change_player();
        return get_vaild_moves(true);
    }

    // show player info and current player while playing
    private void show_player_info() {
        System.out.print("White Player: " + white_player_name + " ");
        if(current_player == Player.White) {
            System.out.print(Piece.white_piece);
        }
        System.out.println();

        System.out.print("Black Player: " + black_player_name + " ");
        if(current_player == Player.Black) {
            System.out.print(Piece.black_piece);
        }
        System.out.println();
        
        System.out.print("Current Player:");
        if(current_player == Player.White) {
            System.out.print("White");
        } else {
            System.out.print("Black");
        }
        System.out.println();
    }


    private void show_winner_info(Piece.Type type) {
        switch(type) {
            case Piece.Type.White:
                System.out.println("White Wins\nCongratulations! " + white_player_name);
                break;
            case Piece.Type.Black:
                System.out.println("Black Wins\nCongratulations! " + black_player_name);
                break;
            case Piece.Type.None:
                System.out.println("Draw\nCool");
                break;
        }
    }

    // count pieces and return the winner
    private Piece.Type get_winner() {
        int white_piece_count = 0;
        int black_piece_count = 0;
        for(int i = 1; i <= height; i++) {
            for(int j = 1; j <= width; j++) {
                switch (grid[i][j].getType()) {
                    case Piece.Type.White:
                        white_piece_count++;
                        break;
                    case Piece.Type.Black:
                        black_piece_count++;
                        break;
                    default:
                        break;
                }
            }
        }

        if(white_piece_count > black_piece_count) {
            return Piece.Type.White;
        }
        if(white_piece_count == black_piece_count) {
            return Piece.Type.None;
        }
        if(white_piece_count < black_piece_count) {
            return Piece.Type.Black;
        }
        // to comfort the compiler
        // never used
        return Piece.Type.None;
    }
}
