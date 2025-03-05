package main;

import java.util.Scanner;

import view.Canvas;
import model.Board;

public class Reversi{
    private static String get_name(Board.Player player, Scanner scanner) {
        Canvas.clear_screen();
        if(player == Board.Player.White) {
            System.out.println("[You Play White]");
        } else {
            System.out.println("[You Play Black]");
        }
        System.out.print("your name is: ");
        if(scanner.hasNext()) {
            return scanner.nextLine();
        }
        scanner.close();
        return "";
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Board game = new Board(8, 8);
        
        // get names
        {
            String white_player_name = get_name(Board.Player.White, scanner);
            if(white_player_name == "") {
                return;
            }
            String black_player_name = get_name(Board.Player.Black, scanner);
            if(black_player_name == "") {
                return;
            }
            game.setName(white_player_name, black_player_name);
        }
        
        game.paint();

        // main loop
        while(!game.end()) {
            int x;
            int y;
            
            // convert input
            {
                String str;
                System.out.print("your decision is: ");
                // if player get bored, die
                if(!scanner.hasNext()) {
                    break;
                }
                str = scanner.nextLine();

                // input validation check
                if(str.length() != 2) {
                    System.out.println("oOps! invalid move");
                    continue;
                }
            
                // calculate location
                x = (int)str.charAt(1)-64;
                if(str.charAt(0) > '9') {
                    y = (int)str.charAt(0)-87;
                } else {
                    y = (int)str.charAt(0)-48;
                }
            }

            // location validation check done in "lay_piece" function
            if(!game.lay_piece(x,y)) {
                System.out.println("oOps! invalid move");
                continue;
            }

            // paint game scene
            game.paint();
        }
        scanner.close();
        return;
    }
}