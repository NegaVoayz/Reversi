package controller;

import model.Board;
import model.enums.Player;
import model.factories.BoardFactory;
import model.rules.RuleImplLandfill;
import model.rules.RuleImplReversi;
import view.Screen;

import java.util.ArrayList;
import java.util.Scanner;

public class InitializationController {
    private final Scanner scanner;
    private final ArrayList<Board> boards;
    private final BoardFactory boardFactory;
    private int boardNumber;

    public InitializationController(Scanner scanner, ArrayList<Board> boards, Screen screen) {
        this.scanner = scanner;
        this.boards = boards;
        this.boardFactory = BoardFactory
                .create()
                .setScreen(screen)
                .useDefaultVerticalAlign()
                .useDefaultHorizontalAlign();
    }

    public void initialize() {
//        inputBoardRule();
//        inputBoardCount();
        inputBoardSize();
        inputPlayerNames();
        useDefaultBoardSets();
//        initializeBoards();
    }

    private void useDefaultBoardSets() {
        boardFactory.setRule(new RuleImplReversi());
        boards.add(boardFactory.createBoard());
        boardFactory.setRule(new RuleImplLandfill());
        boards.add(boardFactory.createBoard());
    }

    private void initializeBoards() {
        boards.ensureCapacity(boardNumber);
        for(int i = 0; i < boardNumber; i++) {
            boards.add(boardFactory.createBoard());
        }
    }

    private void inputBoardRule() {
        System.out.println("Enter board rule: (peace or reversi)");
        String input = scanner.nextLine();
        if( input.compareToIgnoreCase("peace")==0 ) {
            boardFactory.setRule(new RuleImplLandfill());
        } else if( input.equals("reversi") ) {
            boardFactory.setRule(new RuleImplReversi());
        } else {
            System.out.println("Invalid board rule, using default rule REVERSI.");
            boardFactory.useDefaultRule();
        }
    }

    private void inputBoardSize() {
        System.out.println("Enter board size: (one number only)");
        int boardSize = scanner.nextInt();
        boardFactory
                .setBoardSizeCol(boardSize)
                .setBoardSizeRow(boardSize);
    }

    private void inputBoardCount() {
        System.out.println("Enter board number: ");
        boardNumber = scanner.nextInt();
        scanner.nextLine();
    }

    /**
     * Retrieves the name of the player.
     *
     * @param player The player whose name is to be retrieved.
     * @return The name of the player.
     */
    private String getName(Player player) {
        Screen.clear();
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

        boardFactory
                .setWhitePlayerName(whitePlayerName)
                .setBlackPlayerName(blackPlayerName);
    }
}
