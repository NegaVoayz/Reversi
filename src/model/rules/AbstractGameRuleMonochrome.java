package model.rules;

import model.enums.GameType;
import model.pieces.Piece;
import model.pieces.PieceImplMonochrome;
import model.structs.GameStatistics;

public abstract class AbstractGameRuleMonochrome implements GameRule {

    @Override
    public GameType getGameType() {
        return GameType.PLACE_PIECE;
    }

    public void basicInitializeGrid(GameStatistics statistics) {
        for(int i = 0; i < statistics.getHeight()+2; i++) {
            for(int j = 0; j < statistics.getWidth()+2; j++) {
                statistics.getPieceGrid()[i][j] = new PieceImplMonochrome();
            }
        }
    }
}
