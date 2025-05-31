package model.rules.gameRule

import model.enums.BombPieceType.*
import model.enums.Player
import model.exceptions.GameException
import model.exceptions.OccupiedPositionException
import model.exceptions.RuleViolationException
import model.pieces.Piece
import model.pieces.PieceImplBomb
import model.structs.GameStatistics
import model.structs.Move
import model.structs.Point

class GameRuleImplBomb private constructor() : AbstractGameRuleMonochrome() {
    class BombRecord {
        var blackBombCount: Int = 2
            private set
        var whiteBombCount: Int = 3
            private set

        fun useBlackBomb() {
            blackBombCount--
        }

        fun useWhiteBomb() {
            whiteBombCount--
        }
    }

    fun setBarrier(grid: Array<Array<Piece>>, x: Int, y: Int) {
        require(grid[y][x] is PieceImplBomb) { "Expected PieceImplBomb at ($x, $y)" }
        (grid[y][x] as PieceImplBomb).type = BARRIER
    }

    override fun initializeGrid(height: Int, width: Int): Array<Array<Piece>> {
        val pieceGrid: Array<Array<Piece>> = Array(height + 2) {
            Array(width + 2) { PieceImplBomb() }
        }
        setBarrier(pieceGrid, 3, 6)
        setBarrier(pieceGrid, 8, 7)
        setBarrier(pieceGrid, 9, 6)
        setBarrier(pieceGrid, 12, 11)
        return pieceGrid
    }

    override fun initializeExtraInfo(statistics: GameStatistics) {
        statistics.extraInfo = BombRecord()
    }

    @Throws(GameException::class)
    override fun placePieceValidationCheck(move: Move, statistics: GameStatistics): Boolean {
        if (move.end.x <= 0 || move.end.x > statistics.height || move.end.y <= 0 || move.end.y > statistics.width) {
            return false
        }
        require(!(statistics.pieceGrid[move.end.y][move.end.x] !is PieceImplBomb || move.piece !is PieceImplBomb)) { "Invalid Piece implementation" }
        move.piece.player = statistics.currentPlayer

        // debt
        val piece = move.piece
        val pieceToReplace = statistics.pieceGrid[move.end.y][move.end.x] as PieceImplBomb
        return when (piece.type) {
            NORMAL -> {
                if (pieceToReplace.player != Player.NONE) {
                    throw OccupiedPositionException(move.end)
                }

                if (pieceToReplace.type != NORMAL) {
                    throw RuleViolationException("Unable to place piece here")
                }

                true
            }

            CRATER -> {
                if (pieceToReplace.type == BARRIER) {
                    throw RuleViolationException("Unbreakable barrier!")
                }

                if (pieceToReplace.type == CRATER) {
                    throw RuleViolationException("The shell does not hit a crater twice.")
                }

                if (pieceToReplace.player == Player.NONE) {
                    throw RuleViolationException("No, it's a waste of bomb.")
                }

                if (pieceToReplace.player == statistics.currentPlayer) {
                    throw RuleViolationException("FRIENDLY FIRE!")
                }

                when (statistics.currentPlayer) {
                    Player.BLACK -> {
                        if ((statistics.extraInfo as BombRecord).blackBombCount == 0) {
                            throw RuleViolationException("No bombs left")
                        }
                        true
                    }

                    Player.WHITE -> {
                        if ((statistics.extraInfo as BombRecord).whiteBombCount == 0) {
                            throw RuleViolationException("No bombs left")
                        }
                        true
                    }

                    else -> throw IllegalArgumentException("Invalid player")
                }
            }

            BARRIER -> throw IllegalArgumentException("What? How the hell can you get a barrier?")
        }
    }

    override fun nextPlayer(statistics: GameStatistics) {
        statistics.switchPlayer()
    }

    @Throws(GameException::class)
    override fun placePiece(move: Move, statistics: GameStatistics): Boolean {
        if (!placePieceValidationCheck(move, statistics)) {
            return false
        }
        if (move.piece is PieceImplBomb) {
            val piece = move.piece
            if (piece.type == CRATER) {
                when (piece.player) {
                    Player.BLACK -> (statistics.extraInfo as BombRecord).useBlackBomb()
                    Player.WHITE -> (statistics.extraInfo as BombRecord).useWhiteBomb()
                    else -> throw IllegalArgumentException("Invalid player")
                }
            }
        } else {
            throw IllegalArgumentException("Invalid piece type")
        }
        move.piece.player = statistics.currentPlayer
        statistics.pieceGrid[move.end.y][move.end.x].setPiece(move.piece)
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

    @Suppress("UNCHECKED_CAST")
    private fun checkConnects(point: Point, statistics: GameStatistics): Int {
        require(statistics.pieceGrid.isArrayOf<Array<PieceImplBomb>>()) { "Invalid piece grid" }
        val pieceGrid = statistics.pieceGrid as Array<Array<PieceImplBomb>>
        val temp = Point(0, 0)
        val currentPlayer = statistics.pieceGrid[point.y][point.x].player
        var maxConnectCount = 1

        for (direction in DIRECTIONS) {
            var connectCount = 1
            temp.set(point)
                .translate(direction)
            while (pieceGrid[temp.y][temp.x].player == currentPlayer
                && pieceGrid[temp.y][temp.x].type == NORMAL
            ) {
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
        val gameRule: GameRuleImplBomb = GameRuleImplBomb()

        private val DIRECTIONS = arrayOf<Point>(
            Point(1, 1), Point(1, 0), Point(1, -1),
            Point(0, 1), Point(0, -1),
            Point(-1, 1), Point(-1, 0), Point(-1, -1)
        )
    }
}
