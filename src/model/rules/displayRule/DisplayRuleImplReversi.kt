package model.rules.displayRule

import model.enums.Player
import model.exceptions.GameException
import model.pieces.Piece
import model.pieces.PieceImplMonochrome
import model.rules.Rule
import model.structs.GameStatistics
import model.structs.Move
import model.structs.Point
import model.structs.Rect
import view.components.*
import java.util.*

class DisplayRuleImplReversi : AbstractDisplayRule() {
    /**
     * @param statistics
     * @return
     */
    override fun buildView(statistics: GameStatistics, rule: Rule): Boolean {
        val view = statistics.view
        view.childLayout = ChildLayout.FLEX_X
        view.addChild(initializeGrid(statistics.height, statistics.width))
        val statsView = DisplayBlock(
            Rect(0, 0, 0, 3),
            AlignType.MIDDLE, AlignType.BEGIN
        )
        statsView.childLayout = ChildLayout.UP_TO_DOWN
        statsView.addChild(
            TextBlock("Game")
        )
        statsView.addChild(
            TextBlock("WhitePlayer")
        )
        statsView.addChild(
            TextBlock("BlackPlayer")
        )
        statsView.addChild(
            TextBlock("Current Player")
        )
        view.addChild(statsView)
        return true
    }

    /**
     * Displays current player information and scores.
     */
    override fun displayPlayerInfo(statistics: GameStatistics, rule: Rule) {
        // | grid | player | ...
        require(statistics.view.children[1] is DisplayBlock) { "The player view is not a DisplayBlock" }
        val playerView = statistics.view.children[1] as DisplayBlock

        val displayBlocks: ArrayList<AbstractDisplayBlock> = playerView.children

        // White player info
        run {
            val buf = StringBuilder("Player [" + statistics.whitePlayerName + "] ")
            if (statistics.currentPlayer == Player.WHITE) {
                buf.append(PieceImplMonochrome.WHITE_PIECE)
            } else {
                buf.append(" ")
            }

            buf.append(" ")

            run {
                val whiteScore = rule.gameRule.getWhiteScore(statistics)
                if (whiteScore != -1) {
                    buf.append(whiteScore)
                }
            }

            require(displayBlocks[1] is TextBlock) { "The block is not a TextBlock" }
            val textBlock = displayBlocks[1] as TextBlock
            textBlock.setText(buf.toString())
        }

        // Black player info
        run {
            val buf = StringBuilder("Player [" + statistics.blackPlayerName + "] ")
            if (statistics.currentPlayer == Player.BLACK) {
                buf.append(PieceImplMonochrome.BLACK_PIECE)
            } else {
                buf.append(" ")
            }

            buf.append(" ")

            run {
                val blackScore = rule.gameRule.getBlackScore(statistics)
                if (blackScore != -1) {
                    buf.append(blackScore)
                }
            }

            require(displayBlocks[2] is TextBlock) { "The block is not a TextBlock" }
            val textBlock = displayBlocks[2] as TextBlock
            textBlock.setText(buf.toString())
        }

        run {
            require(displayBlocks[3] is TextBlock) { "The block is not a TextBlock" }
            val textBlock = displayBlocks[3] as TextBlock
            textBlock.setText("Current Player: " + statistics.currentPlayer.name.lowercase(Locale.getDefault()))
        }
    }

    /**
     * Displays game winner information.
     * The winning player: (WHITE, BLACK, or NONE for draw)
     *
     * @param statistics game statistics
     */
    override fun displayWinnerInfo(statistics: GameStatistics, rule: Rule) {
        val winnerInfo: String
        val winnerDetails: String
        val scores: String
        when (statistics.winner) {
            Player.WHITE -> {
                winnerInfo = "White Wins"
                winnerDetails = "Good game " + statistics.whitePlayerName
            }

            Player.BLACK -> {
                winnerInfo = "Black Wins"
                winnerDetails = "Good game " + statistics.blackPlayerName
            }

            Player.NONE -> {
                winnerInfo = "Draw"
                winnerDetails = "Cool"
            }

            else ->                 // still running
                return
        }
        scores = ("White " + rule.gameRule.getWhiteScore(statistics)
                + ": Black " + rule.gameRule.getBlackScore(statistics))

        // | grid | player | ...
        require(statistics.view.children[1] is DisplayBlock) { "The player view is not a DisplayBlock" }
        val playerView = statistics.view.children[1] as DisplayBlock

        val displayBlocks: ArrayList<AbstractDisplayBlock> = playerView.children

        run {
            require(displayBlocks[1] is TextBlock) { "The block is not a TextBlock" }
            val textBlock = displayBlocks[1] as TextBlock
            textBlock.setText(winnerInfo)
        }

        run {
            require(displayBlocks[2] is TextBlock) { "The block is not a TextBlock" }
            val textBlock = displayBlocks[2] as TextBlock
            textBlock.setText(winnerDetails)
        }

        run {
            require(displayBlocks[3] is TextBlock) { "The block is not a TextBlock" }
            val textBlock = displayBlocks[3] as TextBlock
            textBlock.setText(scores)
        }
    }

    /**
     * Highlights valid moves for the current player.
     */
    override fun showValidMoves(statistics: GameStatistics, rule: Rule) {
        val piece: Piece = PieceImplMonochrome()
        require(statistics.view.children.first() is GridBlock) { "The view is not a GridBlock" }
        val gridView = statistics.view.children.first() as GridBlock
        val grid: Array<IntArray> = gridView.grid
        piece.player = statistics.currentPlayer
        val move = Move(Point(0, 0), Point(0, 0), piece)
        move.end.y = 1
        while (move.end.y <= statistics.height) {
            move.end.x = 1
            while (move.end.x <= statistics.width) {
                try {
                    if (rule.gameRule.placePieceValidationCheck(move, statistics)) {
                        grid[move.end.y][move.end.x] = PieceImplMonochrome.VALID_MOVE.code
                    }
                } catch (e: GameException) {
                }
                move.end.x++
            }
            move.end.y++
        }
    }

    companion object {
        val displayRule: DisplayRule = DisplayRuleImplReversi()
    }
}
