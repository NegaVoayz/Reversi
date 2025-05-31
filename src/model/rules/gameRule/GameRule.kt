package model.rules.gameRule

import model.enums.GameType
import model.exceptions.GameException
import model.pieces.Piece
import model.structs.GameStatistics
import model.structs.Move

interface GameRule {
    val gameType: GameType

    fun initializeGrid(height: Int, width: Int): Array<Array<Piece>>

    fun initializeExtraInfo(statistics: GameStatistics)

    @Throws(GameException::class)
    fun placePieceValidationCheck(move: Move, statistics: GameStatistics): Boolean

    fun nextPlayer(statistics: GameStatistics)

    @Throws(GameException::class)
    fun placePiece(move: Move, statistics: GameStatistics): Boolean

    fun gameOverCheck(statistics: GameStatistics): Boolean

    fun getWhiteScore(statistics: GameStatistics): Int

    fun getBlackScore(statistics: GameStatistics): Int
}
