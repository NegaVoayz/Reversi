package model.rules.gameRule;

import model.enums.GameType;
import model.pieces.PieceImplMonochrome;

public abstract class AbstractGameRuleMonochrome implements GameRule {

    @Override
    public GameType getGameType() {
        return GameType.PLACE_PIECE;
    }

    public PieceImplMonochrome[][] basicInitializeGrid(int height, int width){
        PieceImplMonochrome[][] pieceGrid = new PieceImplMonochrome[height+2][width+2];
        for(int i = 0; i < height+2; i++) {
            for(int j = 0; j < width+2; j++) {
                pieceGrid[i][j] = new PieceImplMonochrome();
            }
        }
        return pieceGrid;
    }
}
