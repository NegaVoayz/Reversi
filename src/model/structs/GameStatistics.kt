package model.structs

import model.Board
import model.enums.Player
import model.pieces.Piece
import model.rules.Rule
import view.components.AlignType
import view.components.DisplayBlock
import java.util.*

class GameStatistics(height: Int, width: Int, whitePlayerName: String, blackPlayerName: String, rule: Rule) {
    @JvmField
    val height: Int
    @JvmField
    val width: Int
    val pieceGrid: Array<Array<Piece>>
    @JvmField
    val whitePlayerName: String
    @JvmField
    val blackPlayerName: String
    var currentPlayer: Player
        private set
    @JvmField
    var winner: Player?
    var round: Int
        private set
    val moves: Queue<Move> = LinkedList<Move>()
    var extraInfo: Any? = null
    @JvmField
    val view: DisplayBlock = DisplayBlock(Rect(0, 0, 0, 0), AlignType.MIDDLE, AlignType.BEGIN)

    init {
        // Validate board size
        require(!(height < Board.MIN_BOARD_SIZE || height > Board.MAX_BOARD_SIZE || width < Board.MIN_BOARD_SIZE || width > Board.MAX_BOARD_SIZE)) { "Invalid board size: too large or too small" }
        this.height = height
        this.width = width
        // Validate player names
        require(!(whitePlayerName.length > 32 || blackPlayerName.length > 32)) { "Unable to initialize name: too long" }
        this.whitePlayerName = whitePlayerName
        this.blackPlayerName = blackPlayerName
        this.pieceGrid = rule.gameRule.initializeGrid(height, width)
        currentPlayer = Player.BLACK
        winner = null
        round = 1
    }

    fun switchPlayer() {
        currentPlayer = when (currentPlayer) {
            Player.WHITE -> Player.BLACK
            Player.BLACK -> Player.WHITE
            else -> throw IllegalStateException("Unexpected value: $currentPlayer")
        }
        // check new round
        if (currentPlayer == Player.BLACK) {
            round++
        }
    }

    fun addMove(move: Move?) {
        moves.add(move)
    }
}
