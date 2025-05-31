package model.rules.gameRule

import model.enums.Player
import model.exceptions.GameException
import model.exceptions.OccupiedPositionException
import model.exceptions.OutOfBoardException
import model.pieces.Piece
import model.pieces.PieceImplMonochrome
import model.structs.GameStatistics
import model.structs.Move
import model.structs.Point

class GameRuleImplGomoku private constructor() : AbstractGameRuleMonochrome() {
    override fun initializeGrid(height: Int, width: Int): Array<Array<Piece>> {
        return basicInitializeGrid(height, width)
    }

    override fun initializeExtraInfo(statistics: GameStatistics) {}

    @Throws(GameException::class)
    override fun placePieceValidationCheck(move: Move, statistics: GameStatistics): Boolean {
        if (move.end.x <= 0 || move.end.x > statistics.height || move.end.y <= 0 || move.end.y > statistics.width) {
            throw OutOfBoardException(move.end)
        }
        require(!(statistics.pieceGrid[move.end.y][move.end.x] !is PieceImplMonochrome || move.piece !is PieceImplMonochrome)) { "Invalid Piece implementation" }
        val pieceImplMonochrome = move.piece

        if (pieceImplMonochrome.player != Player.NONE) {
            throw OccupiedPositionException(move.end)
        }

        return true
    }

    override fun nextPlayer(statistics: GameStatistics) {
        statistics.switchPlayer()
    }

    @Throws(GameException::class)
    override fun placePiece(move: Move, statistics: GameStatistics): Boolean {
        if (!placePieceValidationCheck(move, statistics)) {
            return false
        }
        statistics.pieceGrid[move.end.y][move.end.x].player = statistics.currentPlayer
        move.piece!!.player = statistics.currentPlayer
        statistics.addMove(move)
        return true
    }

    override fun gameOverCheck(statistics: GameStatistics): Boolean {
        var full = true
        val boardPoint = Point(0, 0)
        boardPoint.y = 1
        while (boardPoint.y <= statistics.height) {
            boardPoint.x = 1
            while (boardPoint.x <= statistics.width) {
                if (statistics.pieceGrid[boardPoint.y][boardPoint.x].player == Player.NONE) {
                    full = false
                } else {
                    if (checkConnects(boardPoint, statistics) >= 5) {
                        statistics.winner = statistics.pieceGrid[boardPoint.y][boardPoint.x].player
                        return true
                    }
                }
                boardPoint.x++
            }
            boardPoint.y++
        }
        if (full) {
            statistics.winner = Player.NONE
        }
        return full
    }

    override fun getWhiteScore(statistics: GameStatistics): Int {
        return if (statistics.winner == Player.WHITE) 1 else 0
    }

    override fun getBlackScore(statistics: GameStatistics): Int {
        return if (statistics.winner == Player.BLACK) 1 else 0
    }

    private fun checkConnects(point: Point, statistics: GameStatistics): Int {
        val temp = Point(0, 0)
        val currentPlayer = statistics.pieceGrid[point.y][point.x].player
        var maxConnectCount = 1

        for (direction in DIRECTIONS) {
            var connectCount = 1
            temp.set(point)
                .translate(direction)
            while (statistics.pieceGrid[temp.y][temp.x].player == currentPlayer) {
                temp.translate(direction)
                connectCount++
            }
            if (connectCount > maxConnectCount) {
                maxConnectCount = connectCount
            }
        }

        return maxConnectCount
    }

    companion object {
        val gameRule: GameRuleImplGomoku = GameRuleImplGomoku()

        private val DIRECTIONS = arrayOf<Point>(
            Point(1, 1), Point(1, 0), Point(1, -1),
            Point(0, 1), Point(0, -1),
            Point(-1, 1), Point(-1, 0), Point(-1, -1)
        )
    }
}
