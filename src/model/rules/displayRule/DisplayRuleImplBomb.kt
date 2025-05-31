package model.rules.displayRule

import model.enums.Player
import model.exceptions.GameException
import model.pieces.PieceImplBomb
import model.pieces.PieceImplMonochrome
import model.rules.Rule
import model.rules.gameRule.GameRuleImplBomb.BombRecord
import model.structs.GameStatistics
import model.structs.Move
import model.structs.Point
import model.structs.Rect
import view.components.*
import java.util.*

class DisplayRuleImplBomb : AbstractDisplayRule() {
    /**
     * @param statistics
     * @return
     */
    override fun buildView(statistics: GameStatistics, rule: Rule): Boolean {
        val view = statistics.view
        view.childLayout = ChildLayout.FLEX_X

        // add grid
        view.addChild(initializeGrid(statistics.height, statistics.width))

        // add statistics view
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
        statsView.addChild(
            TextBlock("Round")
        )
        view.addChild(statsView)

        // add bomb view
        val bombView = DisplayBlock(
            Rect(0, 0, 0, 3),
            AlignType.MIDDLE, AlignType.MIDDLE
        )
        bombView.childLayout = ChildLayout.UP_TO_DOWN
        bombView.addChild(
            TextBlock("Bombs")
        )
        bombView.addChild(
            TextBlock("WhitePlayerBombs")
        )
        bombView.addChild(
            TextBlock("BlackPlayerBombs")
        )
        bombView.addChild(
            TextBlock("")
        )
        bombView.addChild(
            TextBlock("")
        )
        view.addChild(bombView)
        return true
    }

    /**
     * Displays current player information and scores.
     */
    override fun displayPlayerInfo(statistics: GameStatistics, rule: Rule) {
        // | grid | player | bomb | ...
        require(statistics.view.children[1] is DisplayBlock) { "The player view is not a DisplayBlock" }
        require(statistics.view.children[2] is DisplayBlock) { "The bomb view is not a DisplayBlock" }
        require(statistics.extraInfo is BombRecord) { "Did you say the bomb version has no bomb?" }
        val playerView = statistics.view.children[1] as DisplayBlock
        val bombView = statistics.view.children[2] as DisplayBlock
        val bombRecord = statistics.extraInfo as BombRecord

        val bombViewChildren: ArrayList<AbstractDisplayBlock> = bombView.children
        val playerViewChildren: ArrayList<AbstractDisplayBlock> = playerView.children

        // Black Bombs
        run {
            require(bombViewChildren[1] is TextBlock) { "The block is not a TextBlock" }
            val textBlock = bombViewChildren[1] as TextBlock
            textBlock.setText(bombRecord.whiteBombCount.toString())
        }
        // White Bombs
        run {
            require(bombViewChildren[2] is TextBlock) { "The block is not a TextBlock" }
            val textBlock = bombViewChildren[2] as TextBlock
            textBlock.setText(bombRecord.blackBombCount.toString())
        }

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

            require(playerViewChildren[1] is TextBlock) { "The block is not a TextBlock" }
            val textBlock = playerViewChildren[1] as TextBlock
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

            require(playerViewChildren[2] is TextBlock) { "The block is not a TextBlock" }
            val textBlock = playerViewChildren[2] as TextBlock
            textBlock.setText(buf.toString())
        }

        run {
            require(playerViewChildren[3] is TextBlock) { "The block is not a TextBlock" }
            val textBlock = playerViewChildren[3] as TextBlock
            textBlock.setText("Current Player: " + statistics.currentPlayer.name.lowercase(Locale.getDefault()))
        }

        run {
            require(playerViewChildren[4] is TextBlock) { "The block is not a TextBlock" }
            val textBlock = playerViewChildren[4] as TextBlock
            textBlock.setText("Current Round: " + statistics.round)
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
        val scores: String = ("White " + rule.gameRule.getWhiteScore(statistics)
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

        playerView.removeChild(4)
    }

    /**
     * Highlights valid moves for the current player.
     */
    override fun showValidMoves(statistics: GameStatistics, rule: Rule) {
        val piece = PieceImplBomb()
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
        val displayRule: DisplayRule = DisplayRuleImplBomb()
    }
}
