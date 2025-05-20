package model.rules.displayRule;

import model.enums.Player;
import model.exceptions.GameException;
import model.pieces.Piece;
import model.pieces.PieceImplMonochrome;
import model.rules.Rule;
import model.structs.GameStatistics;
import model.structs.Move;
import model.structs.Point;
import model.structs.Rect;
import view.components.*;

import java.util.ArrayList;

public class DisplayRuleImplGomoku extends AbstractDisplayRule {

    private static final DisplayRule INSTANCE = new DisplayRuleImplGomoku();

    public static DisplayRule getDisplayRule() {
        return INSTANCE;
    }

    /**
     * @param statistics
     * @return
     */
    @Override
    public boolean buildView(GameStatistics statistics, Rule rule) {
        DisplayBlock view = statistics.getView();
        view.setChildLayout(ChildLayout.FLEX_X);
        view.addChild(initializeGrid(statistics.getHeight(), statistics.getWidth()));
        DisplayBlock statsView = new DisplayBlock(
                new Rect(0,0,0,3),
                AlignType.MIDDLE, AlignType.BEGIN);
        statsView.setChildLayout(ChildLayout.UP_TO_DOWN);
        statsView.addChild(
                new TextBlock("Game"));
        statsView.addChild(
                new TextBlock("WhitePlayer"));
        statsView.addChild(
                new TextBlock("BlackPlayer"));
        statsView.addChild(
                new TextBlock("Current Player"));
        statsView.addChild(
                new TextBlock("Round"));
        view.addChild(statsView);
        return true;
    }

    /**
     * Displays current player information and scores.
     */
    @Override
    protected void displayPlayerInfo(GameStatistics statistics, Rule rule) {
        // | grid | player | ...
        if(!(statistics.getView().getChildren().get(1) instanceof DisplayBlock playerView)) {
            throw new IllegalArgumentException("The player view is not a DisplayBlock");
        }

        ArrayList<AbstractDisplayBlock> displayBlocks = playerView.getChildren();

        // White player info
        {
            StringBuilder buf = new StringBuilder("Player [" + statistics.getWhitePlayerName() + "] ");

            if(statistics.getCurrentPlayer() == Player.WHITE) {
                buf.append(PieceImplMonochrome.WHITE_PIECE);
            } else {
                buf.append(" ");
            }

            buf.append(" ");

            {
                int whiteScore = rule.getGameRule().getWhiteScore(statistics);
                if(whiteScore != -1) {
                    buf.append(whiteScore);
                }
            }

            if(!(displayBlocks.get(1) instanceof TextBlock textBlock)) {
                throw new IllegalArgumentException("The block is not a TextBlock");
            }
            textBlock.setText(buf.toString());
        }

        // Black player info
        {
            StringBuilder buf = new StringBuilder("Player [" + statistics.getBlackPlayerName() + "] ");

            if(statistics.getCurrentPlayer() == Player.BLACK) {
                buf.append(PieceImplMonochrome.BLACK_PIECE);
            } else {
                buf.append(" ");
            }

            buf.append(" ");

            {
                int blackScore = rule.getGameRule().getBlackScore(statistics);
                if(blackScore != -1) {
                    buf.append(blackScore);
                }
            }

            if(!(displayBlocks.get(2) instanceof TextBlock textBlock)) {
                throw new IllegalArgumentException("The block is not a TextBlock");
            }
            textBlock.setText(buf.toString());
        }

        {
            if(!(displayBlocks.get(3) instanceof TextBlock textBlock)) {
                throw new IllegalArgumentException("The block is not a TextBlock");
            }
            textBlock.setText("Current Player: " + statistics.getCurrentPlayer().name().toLowerCase());
        }

        {
            if(!(displayBlocks.get(4) instanceof TextBlock textBlock)) {
                throw new IllegalArgumentException("The block is not a TextBlock");
            }
            textBlock.setText("Current Round: " + statistics.getRound());
        }
    }

    /**
     * Displays game winner information.
     * The winning player: (WHITE, BLACK, or NONE for draw)
     *
     * @param statistics game statistics
     */
    @Override
    protected void displayWinnerInfo(GameStatistics statistics, Rule rule) {
        String winnerInfo;
        String winnerDetails;
        String scores;
        switch(statistics.getWinner()) {
            case Player.WHITE:
                winnerInfo = "White Wins";
                winnerDetails = "Good game " + statistics.getWhitePlayerName();
                break;
            case Player.BLACK:
                winnerInfo = "Black Wins";
                winnerDetails = "Good game " + statistics.getBlackPlayerName();
                break;
            case Player.NONE:
                winnerInfo = "Draw";
                winnerDetails = "Cool";
                break;
            default:
                // still running
                return;
        }
        scores = "White " + rule.getGameRule().getWhiteScore(statistics)
                + ": Black " + rule.getGameRule().getBlackScore(statistics);

        // | grid | player | ...
        if(!(statistics.getView().getChildren().get(1) instanceof DisplayBlock playerView)) {
            throw new IllegalArgumentException("The player view is not a DisplayBlock");
        }

        ArrayList<AbstractDisplayBlock> displayBlocks = playerView.getChildren();

        {
            if(!(displayBlocks.get(1) instanceof TextBlock textBlock)) {
                throw new IllegalArgumentException("The block is not a TextBlock");
            }
            textBlock.setText(winnerInfo);
        }

        {
            if(!(displayBlocks.get(2) instanceof TextBlock textBlock)) {
                throw new IllegalArgumentException("The block is not a TextBlock");
            }
            textBlock.setText(winnerDetails);
        }

        {
            if(!(displayBlocks.get(3) instanceof TextBlock textBlock)) {
                throw new IllegalArgumentException("The block is not a TextBlock");
            }
            textBlock.setText(scores);
        }

        {
            if(!(displayBlocks.get(4) instanceof TextBlock textBlock)) {
                throw new IllegalArgumentException("The block is not a TextBlock");
            }
            textBlock.setText("");
        }
    }

    /**
     * Highlights valid moves for the current player.
     */
    @Override
    protected void showValidMoves(GameStatistics statistics, Rule rule) {
        Piece piece = new PieceImplMonochrome();
        if(!(statistics.getView().getChildren().getFirst() instanceof GridBlock gridView)) {
            throw new IllegalArgumentException("The view is not a GridBlock");
        }
        int[][] grid = gridView.getGrid();
        piece.setPlayer(statistics.getCurrentPlayer());
        Move move = new Move(new Point(0,0), new Point(0,0), piece);
        for(move.end.y = 1; move.end.y <= statistics.getHeight(); move.end.y++) {
            for(move.end.x = 1; move.end.x <= statistics.getWidth(); move.end.x++) {
                try {
                    if(rule.getGameRule().placePieceValidationCheck(move, statistics)) {
                        grid[move.end.y][move.end.x] = PieceImplMonochrome.VALID_MOVE;
                    }
                } catch (GameException _) {}
            }
        }
    }
}
