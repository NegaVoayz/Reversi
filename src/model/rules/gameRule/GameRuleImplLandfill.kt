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

class GameRuleImplLandfill private constructor() : AbstractGameRuleMonochrome() {
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

    override fun initializeExtraInfo(statistics: GameStatistics) {}


    /**
     * Check whether it is a valid move to place a piece here
     *
     * @param move       position of the move
     * @param statistics the board statistics
     * @return true when valid
     */
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
        if (checkStale(statistics)) {
            statistics.switchPlayer()
        }
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
        val gameOver = checkStale(statistics)
        if (gameOver) {
            statistics.winner = Player.NONE
        }
        return gameOver
    }

    override fun getWhiteScore(statistics: GameStatistics): Int {
        return -1
    }

    override fun getBlackScore(statistics: GameStatistics): Int {
        return -1
    }

    private fun checkStale(statistics: GameStatistics): Boolean {
        val boardPoint = Point(0, 0)
        boardPoint.y = 1
        while (boardPoint.y <= statistics.height) {
            boardPoint.x = 1
            while (boardPoint.x <= statistics.width) {
                if (statistics.pieceGrid[boardPoint.y][boardPoint.x].player == Player.NONE) {
                    return false
                }
                boardPoint.x++
            }
            boardPoint.y++
        }
        return true
    }

    companion object {
        val gameRule: GameRuleImplLandfill = GameRuleImplLandfill()
    }
}
