import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import controller.GameController;
import controller.InitializationController;
import controller.InputController;
import controller.SettlementController;
import model.Board;

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

        while(!gameController.isAllGameOver()) {
            boolean isValidMove = inputController
                    .readCommand()
                    .parseCommand()
                    .executeCommand();
            if( !isValidMove ) {
                System.out.println("oOps! invalid move");
            }
        }

        System.out.println("Game Over!");
        try {
            TimeUnit.SECONDS.sleep(1);
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