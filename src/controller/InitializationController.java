package controller;

import model.Board;
import model.enums.Player;
import model.rules.Rule;
import model.rules.RuleImplReversi;
import view.Window;
import view.WindowImplConsole;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class InitializationController {
    private final Scanner scanner;
    private final ArrayList<Board> boards;

    public InitializationController(Scanner scanner, ArrayList<Board> boards) {
        this.scanner = scanner;
        this.boards = boards;
    }

    public void initialize() {
        initialize(new RuleImplReversi());
    }

    public void initialize(Rule rule) {
        initializeBoards(rule);
        inputPlayerNames();
    }

    private void initializeBoards(Rule rule) {
        System.out.println("Enter board size: (one number only)");
        int boardSize = scanner.nextInt();

        System.out.println("Enter board number: ");
        int boardNumber = scanner.nextInt();

        boards.ensureCapacity(boardNumber);
        for(int i = 0; i < boardNumber; i++) {
            boards.add(new Board(boardSize, boardSize, rule, new WindowImplConsole(boardSize+1,boardSize*2+Board.ULTIMATE_ANSWER)));
        }
        scanner.nextLine();
    }

    /**
     * Retrieves the name of the player.
     *
     * @param player The player whose name is to be retrieved.
     * @return The name of the player.
     */
    private String getName(Player player) {
        Window.clear();
        if(player == Player.WHITE) {
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

    /**
     * Prompts the players to input their names and sets them on the board.
     */
    private void inputPlayerNames() {
        String whitePlayerName;
        String blackPlayerName;
        do {
            whitePlayerName = getName(Player.WHITE);
        } while (whitePlayerName.length() > 32 || whitePlayerName.isEmpty());

        do {
            blackPlayerName = getName(Player.BLACK);
        } while (blackPlayerName.length() > 32 || blackPlayerName.isEmpty());

        for(Board board : boards) {
            board.setName(whitePlayerName, blackPlayerName);
        }
    }
}
