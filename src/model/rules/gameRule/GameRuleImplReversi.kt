package model.rules.gameRule

import model.enums.Player
import model.exceptions.GameException
import model.exceptions.OccupiedPositionException
import model.exceptions.OutOfBoardException
import model.exceptions.RuleViolationException
import model.pieces.Piece
import model.pieces.PieceImplMonochrome
import model.structs.GameStatistics
import model.structs.Move
import model.structs.Point

class GameRuleImplReversi private constructor() : AbstractGameRuleMonochrome() {
    private class ExtraInfo {
        private var stale = false
        private var passed = false
        fun setStale(stale: Boolean) {
            this.stale = stale
        }

        fun reset() {
            this.passed = false
        }

        fun gameOver(): Boolean {
            return stale && passed
        }

        fun canPass(): Boolean {
            return stale
        }

        fun pass() {
            stale = false
            passed = true
        }
    }

    /**
     * allocate pieceGrid and set the start pieces
     */
    override fun initializeGrid(height: Int, width: Int): Array<Array<Piece>> {
        val pieceGrid = basicInitializeGrid(height, width)

        pieceGrid[height / 2][width / 2].player = Player.WHITE
        pieceGrid[height / 2 + 1][width / 2].player = Player.BLACK
        pieceGrid[height / 2][width / 2 + 1].player = Player.BLACK
        pieceGrid[height / 2 + 1][width / 2 + 1].player = Player.WHITE

        return pieceGrid
    }

    override fun initializeExtraInfo(statistics: GameStatistics) {
        statistics.extraInfo = ExtraInfo()
    }

    /**
     * Check whether it is a valid move to place a piece here
     *
     * @param move       position of the move
     * @param statistics the board statistics
     * @return true when valid
     */
    @Throws(GameException::class)
    override fun placePieceValidationCheck(move: Move, statistics: GameStatistics): Boolean {
        /* pass logic */
        if (move.end.x == 0 && move.end.y == 0) {
            val extraInfo: ExtraInfo = statistics.extraInfo as ExtraInfo
            if (!extraInfo.canPass()) {
                throw RuleViolationException("No pass here.")
            }
            return true
        }

        /* out of board check */
        if (move.end.x <= 0 || move.end.x > statistics.height || move.end.y <= 0 || move.end.y > statistics.width) {
            throw OutOfBoardException(move.end)
        }

        /* piece type check */
        require(!(statistics.pieceGrid[move.end.y][move.end.x] !is PieceImplMonochrome || move.piece !is PieceImplMonochrome)) { "Invalid Piece implementation" }
        val pieceImplMonochrome = statistics.pieceGrid[move.end.y][move.end.x] as PieceImplMonochrome

        /* place occupation check */
        if (pieceImplMonochrome.player != Player.NONE) {
            throw OccupiedPositionException(move.end)
        }

        /* flip check */
        if (flipPieces(move.end, statistics.currentPlayer, statistics.pieceGrid, false) == 0) {
            throw RuleViolationException("One move shall flip at least 1 piece")
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
        val extraInfo: ExtraInfo = statistics.extraInfo as ExtraInfo
        extraInfo.reset()

        /* pass logic */
        if (move.end.x == 0 && move.end.y == 0) {
            extraInfo.pass()
            return true
        }

        statistics.pieceGrid[move.end.y][move.end.x].player = statistics.currentPlayer
        flipPieces(move.end, statistics.currentPlayer, statistics.pieceGrid, true)
        move.piece!!.player = statistics.currentPlayer
        statistics.addMove(move)
        return true
    }

    override fun gameOverCheck(statistics: GameStatistics): Boolean {
        val extraInfo: ExtraInfo = statistics.extraInfo as ExtraInfo
        extraInfo.setStale(checkStale(statistics))
        if (extraInfo.gameOver()
            || statistics.moves.size ==
            statistics.height * statistics.width - 4
        ) {
            statistics.winner = calculateWinner(statistics)
            return true
        }
        return false
    }

    override fun getWhiteScore(statistics: GameStatistics): Int {
        var whiteCount = 0
        for (pieces in statistics.pieceGrid) {
            for (piece in pieces) {
                if (piece.player == Player.WHITE) {
                    whiteCount++
                }
            }
        }
        return whiteCount
    }

    override fun getBlackScore(statistics: GameStatistics): Int {
        var blackCount = 0
        for (pieces in statistics.pieceGrid) {
            for (piece in pieces) {
                if (piece.player == Player.BLACK) {
                    blackCount++
                }
            }
        }
        return blackCount
    }

    /**
     * recursively check/do a flip
     * @param applyChange flip the pieces or not
     * @return the number of pieces (could be) flipped (negative means operation failed)
     */
    private fun flipPieces(point: Point, player: Player, pieceGrid: Array<Array<Piece>>, applyChange: Boolean): Int {
        val temp = Point(0, 0)
        var flipCount = 0
        val rival = when (player) {
            Player.WHITE -> Player.BLACK
            Player.BLACK -> Player.WHITE
            Player.NONE -> throw IllegalArgumentException("Invalid Player NONE")
        }

        for (direction in DIRECTIONS) {
            temp.set(point)
                .translate(direction)
            while (pieceGrid[temp.y][temp.x].player == rival) {
                temp.translate(direction)
            }
            if (pieceGrid[temp.y][temp.x].player == Player.NONE) {
                continue
            }
            temp.detranslate(direction)
            while (pieceGrid[temp.y][temp.x].player == rival) {
                flipCount++
                if (applyChange) {
                    pieceGrid[temp.y][temp.x].player = player
                }
                temp.detranslate(direction)
            }
        }

        return flipCount
    }

    private fun calculateWinner(statistics: GameStatistics): Player {
        val whiteCount = getWhiteScore(statistics)
        val blackCount = getBlackScore(statistics)

        if (whiteCount > blackCount) {
            return Player.WHITE
        }
        if (whiteCount < blackCount) {
            return Player.BLACK
        }
        return Player.NONE
    }

    private fun checkStale(statistics: GameStatistics): Boolean {
        val piece: Piece = PieceImplMonochrome()
        piece.player = statistics.currentPlayer
        val move = Move(Point(0, 0), Point(0, 0), piece)
        move.end.y = 1
        while (move.end.y <= statistics.height) {
            move.end.x = 1
            while (move.end.x <= statistics.width) {
                try {
                    if (placePieceValidationCheck(move, statistics)) return false
                } catch (e: GameException) {
                }
                move.end.x++
            }
            move.end.y++
        }
        return true
    }

    companion object {
        val gameRule: GameRuleImplReversi = GameRuleImplReversi()

        private val DIRECTIONS = arrayOf<Point>(
            Point(1, 1), Point(1, 0), Point(1, -1),
            Point(0, 1), Point(0, -1),
            Point(-1, 1), Point(-1, 0), Point(-1, -1)
        )
    }
}
