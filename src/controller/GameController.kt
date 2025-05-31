package controller

import model.Board
import model.enums.Player
import model.exceptions.GameException
import model.factories.BoardFactory
import model.rules.RuleImplBomb
import model.rules.RuleImplGomoku
import model.rules.RuleImplLandfill
import model.rules.RuleImplReversi
import model.structs.Move
import view.components.ChildLayout
import view.components.DisplayBlock
import view.components.TextBlock
import view.renderer.Renderer
import java.util.*

/**
 * GameController: Controlling multi-board states and interaction.
 *
 *
 * Main Functions:
 *
 *  * Handle the Switch and Display of multiple Board Entities
 *  * Parse and Execute players' operations on board.
 *  * Create new boards during the game.
 *  * Track the scores.
 *  * Provide Game List View
 *
 */
class GameController(// All games
    private val boards: ArrayList<Board>, renderer: Renderer
) {
    // Current active board index
    private var currentBoardIdx = 0

    // Game Result Statistics
    private var gameOverCount = 0
    protected var whiteWinCount: Int = 0
        private set
    protected var blackWinCount: Int = 0
        private set

    protected val whitePlayerName: String
    protected val blackPlayerName: String

    private val boardFactory: BoardFactory

    // Display
    private val renderer: Renderer
    private val mainView: DisplayBlock


    fun showBoard() {
        mainView.changeChild(0, boards[currentBoardIdx].show())
        renderer.render(mainView)
    }

    val isAllGameOver: Boolean
        get() = gameOverCount == boards.size

    /**
     * switch board by input
     *
     * @param input board No
     * @return true if succeeded
     */
    fun setCurrentBoard(input: String): Boolean {
        val newBoardNo: Int
        try {
            newBoardNo = input.toInt()
        } catch (e: NumberFormatException) {
            println("Invalid input: \"$input\" is not a number")
            return false
        }
        if (newBoardNo <= 0 || newBoardNo > boards.size) {
            println("Invalid board number: $newBoardNo")
            return false
        }
        this.currentBoardIdx = newBoardNo - 1
        updateBoards(boards, currentBoardIdx)
        showBoard()
        return true
    }

    private var tempMove: Move? = null

    /**
     * parse players' move by current game rule
     *
     * @param input player's command
     * @return this entity for method chain
     */
    fun parseMove(input: String): GameController {
        tempMove = boards[currentBoardIdx].inputRule.parseInput(input)
        return this
    }

    /**
     * place piece by last parsed move
     *
     * @return true if succeeded
     */
    @Throws(GameException::class)
    fun placePiece(): Boolean {
        if (tempMove == null) {
            return false
        }
        return placePiece(tempMove)
    }

    /**
     * parse players' creating board operation
     *
     * @param input command "[game type] ([width] [height])"
     * @return this entity for method chain
     * @throws IllegalArgumentException when input is illegal
     */
    @Throws(IllegalArgumentException::class)
    fun parseCreate(input: String): GameController {
        val tokens: Array<String?> = input.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        // parse the rule type
        when (tokens[0]!!.lowercase(Locale.getDefault())) {
            RuleImplReversi.name -> boardFactory.setRule(RuleImplReversi.rule)
            RuleImplLandfill.name -> boardFactory.setRule(RuleImplLandfill.rule)
            RuleImplGomoku.name -> boardFactory.setRule(RuleImplGomoku.rule)
            RuleImplBomb.name -> boardFactory.setRule(RuleImplBomb.rule)
            else -> throw IllegalArgumentException("No rule named " + tokens[0] + ". Try 'reversi' or 'peace'.")
        }

        // if size is not specified
        if (tokens.size == 1) {
            boardFactory
                .useDefaultBoardSizeCol()
                .useDefaultBoardSizeRow()
            return this
        }

        // if only one size specified, take as a square board
        if (tokens.size == 2) {
            try {
                boardFactory
                    .setBoardSizeCol(tokens[1]!!.toInt())
                    .setBoardSizeRow(tokens[1]!!.toInt())
            } catch (e: NumberFormatException) {
                throw IllegalArgumentException("Invalid input: \"" + tokens[1] + "\" is not a number.")
            }
            return this
        }

        // if two sizes specified, set.
        try {
            boardFactory
                .setBoardSizeCol(tokens[1]!!.toInt())
                .setBoardSizeRow(tokens[2]!!.toInt())
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("Invalid input: \"" + tokens[1] + "\" or \"" + tokens[2] + "\" is not a number")
        }

        // check size validity
        require(boardFactory.isLegalSetting) {
            "Invalid board size.\n" +
                    "Board size must be between " + Board.MIN_BOARD_SIZE + " and " + Board.MAX_BOARD_SIZE
        }

        return this
    }

    /**
     * create board from last parsed input
     *
     * @return true if succeeded
     */
    fun createBoard(): Boolean {
        boards.add(boardFactory.createBoard())
        updateBoards(boards, currentBoardIdx)
        showBoard()
        return true
    }

    private val boardsSelected: Queue<Int?>

    /**
     * Initialize GameController
     *
     * @param boards initial game set
     * @param renderer game renderer for display
     */
    init {
        this.whitePlayerName = boards.first().whitePlayerName
        this.blackPlayerName = boards.first().blackPlayerName
        this.boardsSelected = LinkedList<Int?>()
        this.renderer = renderer
        boardFactory = BoardFactory
            .create()
            .setWhitePlayerName(whitePlayerName)
            .setBlackPlayerName(blackPlayerName)
            .useDefaultBoardSizeCol()
            .useDefaultBoardSizeRow()
            .useDefaultVerticalAlign()
            .useDefaultHorizontalAlign()
        this.mainView = DisplayBlock()
        mainView.addChild(DisplayBlock())
        mainView.addChild(DisplayBlock())
        updateBoards(boards, currentBoardIdx)
        showBoard()
    }

    /**
     * Select boards by selectors
     *
     * @param selector select rules
     * @return this entity for method chain
     */
    fun selectBoards(selector: String): GameController {
        if (selector.isEmpty()) {
            selectAllBoards()
            return this
        }
        boardsSelected.clear()
        val tokens: Array<String?> = selector.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (tokens[0]!!.compareTo("current", ignoreCase = true) == 0) {
            boardsSelected.add(currentBoardIdx)
            return this
        }
        selectAllBoards()
        selectBoardsByRuleName(tokens[0]!!.lowercase(Locale.getDefault()))
        return this
    }

    /**
     * list the selected boards
     *
     * @return true if succeeded
     */
    fun listBoards(): Boolean {
        if (boardsSelected.isEmpty()) {
            println("No boards meet the requirements")
            return true
        }
        do {
            val i = boardsSelected.poll()!!
            println("Board " + (i + 1) + ": " + boards.get(i))
        } while (!boardsSelected.isEmpty())
        return true
    }

    // add all boards into select queue
    private fun selectAllBoards() {
        for (i in boards.indices) {
            boardsSelected.add(i)
        }
    }

    // pick the boards matches the rule name
    private fun selectBoardsByRuleName(ruleName: String) {
        while (!boardsSelected.isEmpty()) {
            val tempBoardNO = boardsSelected.poll()!!
            if (boards[tempBoardNO].rule.name == ruleName) {
                boardsSelected.add(tempBoardNO)
            }
        }
    }

    /**
     * place piece by move, interacts with Board
     *
     * @return True if the position is valid and the piece is placed successfully
     */
    @Throws(GameException::class)
    private fun placePiece(move: Move?): Boolean {
        if (move == null) {
            return false
        }

        if (boards[currentBoardIdx].isGameOver) {
            println("Game Over!")
            return false
        }

        if (!boards[currentBoardIdx].placePiece(move)) {
            println("Invalid Move")
            return false
        }

        // the game over process
        if (boards[currentBoardIdx].isGameOver) {
            if (boards[currentBoardIdx].winner == Player.WHITE) {
                whiteWinCount++
            } else if (boards[currentBoardIdx].winner == Player.BLACK) {
                blackWinCount++
            }
            gameOverCount++
            updateBoards(boards, currentBoardIdx)
        }

        // update view since the board has changed.
        showBoard()

        return true
    }

    fun updateBoards(boards: ArrayList<Board>, currentBoardIdx: Int) {
        val gameListView = DisplayBlock()
        gameListView.childLayout = ChildLayout.UP_TO_DOWN
        // calculate the start position
        // align current board in the middle
        var startY = 1
        var startBoard = currentBoardIdx - 3
        if (startBoard < 0) {
            startY = 1 - startBoard
            startBoard = 0
        }

        // show omit symbol if the boards above exceeded the window
        if (currentBoardIdx - 3 > 0) {
            gameListView.addChild(TextBlock("    ..."))
        }

        // display the game list visible
        var i = startY
        var j = startBoard
        while (i < 9 && j < boards.size) {
            if (j == currentBoardIdx) {
                gameListView.addChild(TextBlock("--> Board " + (j + 1) + ": " + boards[j].briefInformation))
            } else {
                gameListView.addChild(TextBlock("    Board " + (j + 1) + ": " + boards[j].briefInformation))
            }
            i++
            j++
        }

        // show omit symbol if the boards below exceeded the window
        if (currentBoardIdx + 5 < boards.size) {
            gameListView.addChild(TextBlock("    ..."))
        }

        mainView.changeChild(1, gameListView)
    }
}
