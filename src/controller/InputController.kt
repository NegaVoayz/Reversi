package controller

import model.exceptions.GameException
import java.io.File
import java.io.FileNotFoundException
import java.time.Duration
import java.util.*
import kotlin.system.exitProcess

/**
 * Handles player input parsing and command execution for the game.
 *
 * This controller:
 * - Processes raw player input from console
 * - Identifies command types and validates syntax
 * - Delegates command execution to GameController
 * - Provides help documentation for available commands
 */
class InputController(
    private val scanner: Scanner,
    private val gameController: GameController
) {
    /**
     * Enumeration of supported command types.
     */
    private enum class CommandType {
        NONE,   // Default/fallback command
        ERROR,  // Invalid command
        HELP,   // Help request
        CHANGE_BOARD,  // Switch active board
        CREATE_BOARD,  // Create a new board
        LIST_BOARDS,   // List available boards
        PLACE_PIECE,   // Make a move
        DEMO,   // Load DEMO from the specified file
        QUIT    // Quit the game
    }

    /**
     * Internal structure representing a parsed command.
     */
    private data class Command(
        var type: CommandType = CommandType.NONE,
        var content: String = ""
    )

    private val command = Command()

    /**
     * Reads a command from the input source.
     */
    fun readCommand(): InputController {
        print("> ")
        command.content = scanner.nextLine().trim()
        return this
    }

    /**
     * Parses the raw input into a structured command.
     */
    fun parseCommand(): InputController {
        val tokens = LinkedList(command.content.split("\\s+".toRegex()).filter { it.isNotEmpty() })
        val firstToken = tokens.poll()?.lowercase()
        command.type = parseToken(firstToken, tokens)
        return this
    }

    /**
     * Executes the parsed command.
     * @return true if command executed successfully, false otherwise
     */
    fun executeCommand(): Boolean {
        try {
            return when (command.type) {
                CommandType.NONE -> handleDefaultCommand()
                CommandType.ERROR -> false.also { println("Invalid command. Type 'help' for assistance.") }
                CommandType.HELP -> showHelp()
                CommandType.CHANGE_BOARD -> gameController.setCurrentBoard(command.content)
                CommandType.CREATE_BOARD -> gameController.parseCreate(command.content).createBoard()
                CommandType.LIST_BOARDS -> gameController.selectBoards(command.content).listBoards()
                CommandType.PLACE_PIECE -> gameController.parseMove(command.content).placePiece()
                CommandType.DEMO -> runDemo(command.content)
                CommandType.QUIT -> handleQuit()
            }
        } catch (e: IllegalArgumentException) {
            println("Error: ${e.message}")
            return false
        } catch (e: GameException) {
            println("Game error: ${e.message}")
            return false
        }
    }

    private fun handleDefaultCommand(): Boolean {
        return if (command.content.all(Char::isDigit)) {
            gameController.setCurrentBoard(command.content)
        } else {
            gameController.parseMove(command.content).placePiece()
        }
    }

    private fun runDemo(path: String): Boolean {
        try {
            Scanner(File(path)).use { demoScanner ->
                println("Demo activated.")
                while (demoScanner.hasNextLine()) {
                    Thread.sleep(Duration.ofSeconds(1))
                    command.content = demoScanner.nextLine().trim()
                    val isValidMove = parseCommand().executeCommand()
                    println("Current Command > ${command.content}")
                    if (!isValidMove) {
                        println("Invalid operation in demo. Continuing...")
                    }
                }
                println("Demo completed.")
            }
            return true
        } catch (e: FileNotFoundException) {
            throw IllegalArgumentException("Demo file not found: $path")
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
            throw RuntimeException("Demo interrupted", e)
        }
    }

    private fun containsBrackets(): Boolean {
        return command.content.any { it in "()[]" }
    }

    private fun parseToken(firstToken: String?, tokens: Queue<String>): CommandType {
        if (containsBrackets()) {
            println("Error: Brackets are not allowed in commands")
            return CommandType.ERROR
        }

        return when (firstToken) {
            "help", "man" -> CommandType.HELP
            "switch" -> handleSwitch(tokens)
            "goto" -> handleGoto(tokens)
            "move" -> handleMove(tokens)
            "create" -> handleCreate(tokens)
            "list", "ls" -> handleList(tokens)
            "quit", "exit" -> CommandType.QUIT
            "demo" -> handleDemo(tokens)
            null -> CommandType.ERROR
            else -> CommandType.NONE
        }
    }

    private fun handleSwitch(tokens: Queue<String>): CommandType {
        if (tokens.size < 2 || tokens.poll() != "to") {
            return CommandType.ERROR
        }
        return handleGoto(tokens)
    }

    private fun handleGoto(tokens: Queue<String>): CommandType {
        if (tokens.size == 2 && tokens.poll().lowercase() != "board") {
            return CommandType.ERROR
        }
        if (tokens.size != 1) {
            return CommandType.ERROR
        }
        command.content = tokens.poll()
        return CommandType.CHANGE_BOARD
    }

    private fun handleMove(tokens: Queue<String>): CommandType {
        if (tokens.size != 1) {
            return CommandType.ERROR
        }
        command.content = tokens.poll()
        return CommandType.PLACE_PIECE
    }

    private fun handleCreate(tokens: Queue<String>): CommandType {
        if (tokens.size < 2 || tokens.size > 4 || tokens.poll() != "board") {
            return CommandType.ERROR
        }
        command.content = tokens.joinToString(" ")
        return CommandType.CREATE_BOARD
    }

    private fun handleList(tokens: Queue<String>): CommandType {
        command.content = tokens.poll() ?: ""
        return if (tokens.isEmpty()) CommandType.LIST_BOARDS else CommandType.ERROR
    }

    private fun handleDemo(tokens: Queue<String>): CommandType {
        if (tokens.size != 1) {
            return CommandType.ERROR
        }
        command.content = tokens.poll()
        return CommandType.DEMO
    }

    private fun handleQuit(): Boolean {
        scanner.close()
        exitProcess(0)
    }

    companion object {
        /**
         * Show all commands
         * @return true, always success
         */
        private fun showHelp(): Boolean {
            println("""
                | Command Manual:
                | [] means argument, () means optional.
                | Brackets are not allowed in all commands.
                |
                | Command         | Arguments                              | Effect
                |-----------------|----------------------------------------|-----------------------------
                | help/man       |                                        | Show this help
                | quit/exit      |                                        | Quit the game
                | switch to/goto | board NO                               | Switch to the desired board
                | move          | row-first position (e.g. 3D)           | Place piece at position
                | create board   | [game mode] ([column size] [row size]) | Create new board
                | list/ls       | ([mode: game mode/current])            | List the boards
                | demo          | path                                   | Load demo from a file
                | <position>    | (e.g. 3D)                             | Shortcut for move command
                | <number>     |                                        | Shortcut for switch to board
            """.trimMargin())
            return true
        }
    }
}