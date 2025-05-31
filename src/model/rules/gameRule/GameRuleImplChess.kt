package model.rules.gameRule

import model.enums.ChessPieceType
import model.enums.GameType
import model.enums.Player
import model.pieces.Piece
import model.pieces.PieceImplChess
import model.structs.GameStatistics
import model.structs.Move

/*
* not completed
* */
class GameRuleImplChess private constructor() : GameRule {
    private fun checkMovePawn(move: Move, pieceGrid: Array<Array<Piece>>): Boolean {
        require(pieceGrid[move.start.y][move.start.x] is PieceImplChess) { "Invalid Piece implementation" }
        if (move.start.y >= move.end.y) {
            return false
        }
        val pieceImplChess = pieceGrid[move.start.y][move.start.x]
        return pieceGrid[move.end.y][move.end.x].player != pieceImplChess.player
    }

    override val gameType: GameType
        get() = GameType.MOVE_PIECE

    override fun initializeGrid(height: Int, width: Int): Array<Array<Piece>> {
        assert(height == 8 && width == 8)
        val pieceGrid: Array<Array<Piece>> = Array(8) {
            Array(8) { PieceImplChess() }
        }

        for (i in 0..7) {
            pieceGrid[0][i].player = Player.WHITE
            pieceGrid[1][i].player = Player.WHITE
            (pieceGrid[1][i] as PieceImplChess).pieceType = ChessPieceType.PAWN

            pieceGrid[6][i].player = Player.BLACK
            pieceGrid[7][i].player = Player.BLACK
            (pieceGrid[6][i] as PieceImplChess).pieceType = ChessPieceType.PAWN
        }
        setLine(0, pieceGrid)
        setLine(7, pieceGrid)
        return pieceGrid
    }

    override fun initializeExtraInfo(statistics: GameStatistics) {}

    override fun placePieceValidationCheck(move: Move, statistics: GameStatistics): Boolean {
        require(statistics.pieceGrid[move.end.y][move.end.x] is PieceImplChess) { "Invalid Piece implementation" }
        val pieceImplMonochrome = statistics.pieceGrid[move.end.y][move.end.x]
        return pieceImplMonochrome.player == Player.NONE
    }

    override fun nextPlayer(statistics: GameStatistics) {
        statistics.switchPlayer()
    }

    override fun placePiece(move: Move, statistics: GameStatistics): Boolean {
        if (!placePieceValidationCheck(move, statistics)) {
            return false
        }
        statistics.pieceGrid[move.end.y][move.end.x].setPiece(move.piece)
        statistics.pieceGrid[move.start.y][move.start.x].setPiece(PieceImplChess())
        statistics.addMove(move)
        return true
    }

    override fun gameOverCheck(statistics: GameStatistics): Boolean {
        var count = 0
        for (i in 0..7) {
            for (j in 0..7) {
                require(statistics.pieceGrid[i][j] is PieceImplChess) { "Invalid Piece implementation" }
                val pieceImplChess = statistics.pieceGrid[i][j] as PieceImplChess
                if (pieceImplChess.pieceType == ChessPieceType.KING) {
                    count++
                }
            }
        }
        return count == 1
    }

    override fun getWhiteScore(statistics: GameStatistics): Int {
        return if (statistics.winner == Player.WHITE) 1 else 0
    }

    override fun getBlackScore(statistics: GameStatistics): Int {
        return if (statistics.winner == Player.BLACK) 1 else 0
    }

    companion object {
        val gameRule: GameRuleImplChess = GameRuleImplChess()

        private fun setType(grid: Array<Array<Piece>>, x: Int, y: Int, chessPieceType: ChessPieceType) {
            require(grid[y][x] is PieceImplChess) { "Expected PieceImplChess at ($x, $y)" }
            (grid[x][y] as PieceImplChess).pieceType = chessPieceType
        }

        private fun setLine(line: Int, pieceGrid: Array<Array<Piece>>) {
            setType(pieceGrid, 0, line, ChessPieceType.ROOK)
            setType(pieceGrid, 1, line, ChessPieceType.KNIGHT)
            setType(pieceGrid, 2, line, ChessPieceType.BISHOP)
            setType(pieceGrid, 3, line, ChessPieceType.QUEEN)
            setType(pieceGrid, 4, line, ChessPieceType.KING)
            setType(pieceGrid, 5, line, ChessPieceType.BISHOP)
            setType(pieceGrid, 6, line, ChessPieceType.KNIGHT)
            setType(pieceGrid, 7, line, ChessPieceType.ROOK)
        }
    }
}
