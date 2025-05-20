package model.pieces;

import model.enums.ChessPieceType;

public class PieceImplChess extends Piece {

    ChessPieceType pieceType;

    public PieceImplChess() {
        super();
        pieceType = ChessPieceType.NONE;
    }

    public void setPieceType(ChessPieceType pieceType) {
        this.pieceType = pieceType;
    }

    public ChessPieceType getPieceType() {
        return pieceType;
    }

    @Override
    public void setPiece(Piece piece) {
        if(!(piece instanceof PieceImplChess pieceImplChess)) {
            throw new IllegalArgumentException();
        }
        this.pieceType = pieceImplChess.pieceType;
        this.setPlayer(pieceImplChess.getPlayer());
    }
    @Override
    public int getCode() {
        return 0;
    }
}
