package controller;

import model.Board;
import model.structs.Rect;
import view.*;

public class SettlementController {
    private final GameController gameController;
    private final Screen screen;

    public SettlementController(final GameController gameController, final Screen screen) {
        this.gameController = gameController;
        this.screen = screen;
    }

    public void announceResult() {
        int whiteWinCount = gameController.getWhiteWinCount();
        int blackWinCount = gameController.getBlackWinCount();

        String whitePlayerName = gameController.getWhitePlayerName();
        String blackPlayerName = gameController.getBlackPlayerName();

        Window window = screen.createWindow(new Rect(0,3,0, Board.ULTIMATE_ANSWER));
        View view = window.createView(new Rect(0, 3, 0, Board.ULTIMATE_ANSWER));
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
        window.paint();
    }
}
