package model.rules;

import model.Piece;
import model.PieceImplMonochrome;

public abstract class GameRuleImplMonochrome implements GameRule {
    public void basicInitializeGrid(Piece[][] pieceGrid) {
        for(int i = 0; i < pieceGrid.length; i++) {
            for(int j = 0; j < pieceGrid[0].length; j++) {
                pieceGrid[i][j] = new PieceImplMonochrome();
            }
        }
    }
}
