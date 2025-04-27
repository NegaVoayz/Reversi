package model.rules;

import model.pieces.Piece;
import model.pieces.PieceImplMonochrome;

public abstract class AbstractGameRuleMonochrome implements GameRule {
    public void basicInitializeGrid(Piece[][] pieceGrid) {
        for(int i = 0; i < pieceGrid.length; i++) {
            for(int j = 0; j < pieceGrid[0].length; j++) {
                pieceGrid[i][j] = new PieceImplMonochrome();
            }
        }
    }
}
