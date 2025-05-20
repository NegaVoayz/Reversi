import java.util.ArrayList;
import java.util.Scanner;

import controller.GameController;
import controller.InitializationController;
import controller.InputController;
import model.Board;
import view.renderer.Renderer;
import view.renderer.RendererFactory;

public class Reversi{

    /**
     * The main method to run the Reversi game.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Board> boards = new ArrayList<>();
        Renderer renderer = RendererFactory.getRenderer("console");

        // initialize game
        InitializationController initializationController = new InitializationController(scanner, boards);
        initializationController.initialize();

        GameController gameController = new GameController(boards, renderer);
        InputController inputController = new InputController(scanner, gameController);

        // main loop
        System.out.println("Welcome, players. Try 'help' for command instructions.");
        while(!gameController.isAllGameOver()) {
            boolean isValidMove = inputController
                    .readCommand()
                    .parseCommand()
                    .executeCommand();
            if( !isValidMove ) {
                System.out.println("oOps! invalid operation. Try input 'help' for help.");
            }
        }

        scanner.close();
    }
}