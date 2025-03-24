import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import controller.GameController;
import controller.InitializationController;
import controller.InputController;
import controller.SettlementController;
import model.Board;
import view.Window;

public class Reversi{

    /**
     * The main method to run the Reversi game.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Board> boards = new ArrayList<>();

        InitializationController initializationController = new InitializationController(scanner, boards);
        initializationController.initialize();

        GameController gameController = new GameController(boards);
        InputController inputController = new InputController(scanner, gameController);
        SettlementController settlementController = new SettlementController(gameController);

        Window.clear();
        gameController.showBoard();
        while(!gameController.isAllGameOver()) {
            boolean validMove = inputController
                    .readCommand()
                    .parseCommand()
                    .executeCommand();
            if( !validMove ) {
                System.out.println("oOps! invalid move");
            }
        }

        System.out.println("Game Over!");
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException _) {
            /* I don't care unless your computer
             * ordered a pineapple pizza
             * due to this unprocessed exception.
             */
        }

        settlementController.announceResult();

        scanner.close();
        return;
    }
}