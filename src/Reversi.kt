import controller.GameController
import controller.InitializationController
import controller.InputController
import model.Board
import view.renderer.RendererFactory
import java.util.*

object Reversi {
    /**
     * The main method to run the Reversi game.
     *
     * @param args Command line arguments.
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val scanner = Scanner(System.`in`)
        val boards: ArrayList<Board> = ArrayList<Board>()
        val renderer = RendererFactory.getRenderer("console")

        // initialize game
        val initializationController = InitializationController(scanner, boards)
        initializationController.initialize()

        val gameController = GameController(boards, renderer!!)
        val inputController = InputController(scanner, gameController)

        // main loop
        println("Welcome, players. Try 'help' for command instructions.")
        while (!gameController.isAllGameOver) {
            val isValidMove = inputController
                .readCommand()
                .parseCommand()
                .executeCommand()
            if (!isValidMove) {
                println("oOps! invalid operation. Try input 'help' for help.")
            }
        }

        scanner.close()
    }
}