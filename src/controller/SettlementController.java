package controller;

import model.Board;
import model.structs.Rect;
import view.*;

/**
 * Handles end-game results presentation and final score display.
 *
 * <p>Responsibilities:
 * <ul>
 *   <li>Calculates final game results</li>
 *   <li>Creates visual presentation of the outcome</li>
 *   <li>Displays winner information and final scores</li>
 * </ul>
 */
public class SettlementController {
    private final GameController gameController;
    private final Screen screen;

    /**
     * Constructs a SettlementController with required dependencies.
     *
     * @param gameController Source for game result data
     * @param screen Display surface for rendering results
     */
    public SettlementController(final GameController gameController, final Screen screen) {
        this.gameController = gameController;
        this.screen = screen;
    }

    /**
     * Displays the final game results.
     *
     * <p>Creates a visual presentation showing:
     * <ul>
     *   <li>The winning player (or draw)</li>
     *   <li>Personalized victory message</li>
     *   <li>Final score tally</li>
     * </ul>
     */
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
