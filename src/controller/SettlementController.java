package controller;

import model.Board;
import model.structs.Rect;
import view.*;

public class SettlementController {
    private final GameController gameController;

    public SettlementController(final GameController gameController) {
        this.gameController = gameController;
    }

    public void announceResult() {
        int whiteWinCount = gameController.getWhiteWinCount();
        int blackWinCount = gameController.getBlackWinCount();

        String whitePlayerName = gameController.getWhitePlayerName();
        String blackPlayerName = gameController.getBlackPlayerName();

        Window window = new WindowImplConsole(3, Board.ULTIMATE_ANSWER);
        View view = new ViewImplConsole(new Rect(0, 3, 0, Board.ULTIMATE_ANSWER), window);
        if(whiteWinCount > blackWinCount) {
            view.println(0, "White Wins");
            view.println(1, "Good game " + whitePlayerName);
        } else if(whiteWinCount < blackWinCount) {
            view.println(0, "Black Wins");
            view.println(1, "Good game " + blackPlayerName);
        } else {
            view.println(0, "Draw");
            view.println(1, "Cool");
        }
        view.println(2, "Score: "+whiteWinCount+" : "+blackWinCount);
        window.forcePaint();
    }
}
