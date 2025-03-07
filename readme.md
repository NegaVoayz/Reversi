# Reversi Project 2.0.1

## Class & Function Document

### Index

[toc]

### Core Function & Design

#### Flip

* Design

  Flip is the core of the reversi game.

  Since flip is on a straight line, a recursive function may fit in the requirements.

  Each piece check its color and pass down if correct.

  If the last piece matches the required color, it returns 0 as 0 pieces are flipped now.

  If not, return -1 as error
  each piece in middle add one on the return value if not error(-1)

  

  Finally check the flip count, greater than 0 means succeeded.

* Code

  ```java
  /**
   * recursively check/do a flip
   * @param dx direction on column
   * @param dy direction on row
   * @param type piece type of the original piece
   * @param apply_change flip the pieces or not
   * @return the number of pieces (could be) flipped (negative means operation failed)
       */
  private int flipPieces(int dx, int dy, Piece.Type type, boolean apply_change) {
      if(this.type == Piece.Type.NONE) {
          return -1; 
      }
      if(this.type == type) {
          return 0;
      }
  
      // try flip next
      int ans = pieceGrid[row+dy][col+dx].flipPieces(dx, dy, type, apply_change);
  
      if(ans < 0) {
          return -1;
      }
  
      if(apply_change) {
          this.type = type;
      }
  
      return ans + 1;
  }
  ```

#### Validation

* Design

  If found possible move, paint on the canvas and return `true` as succeed.

  If no possible move, return `false` as failed.

* Code
  
  ```java
  public class Board {
      ...
  	/**
       * get and show valid moves for current player
       * @return true when there are any moves valid
       */
      private boolean getValidMoves() {
          boolean movable = false;
          Piece.Type type;
          type = getCurrentPlayerPieceType();
  
          for(int i = 1; i <= height; i++) {
              for(int j = 1; j <= width; j++) {
                  if(pieceGrid[i][j].isValid(type)) {
                      movable = true;
                      canvas.setPixel(j, i, new Pixel(Piece.VALID_MOVE));
                  }
              }
          }
  
          return movable;
      }
      ...
  }
  ```
  
  ```java
  public class Piece {
      ...
      /**
       * Check whether it is a valid move to place piece here
       * @param expected_type the expected type of piece
       * @return true when valid
       */
      public boolean isValid(Piece.Type expected_type) {
          if(this.type != Piece.Type.NONE) {
              return false;
          }
          for(int i = 0; i < 8; i++) {
              int dx = DIRECTIONS[i][0];
              int dy = DIRECTIONS[i][1];
              if(pieceGrid[row+dy][col+dx].flipPieces(dx, dy, expected_type, false)>0) {
                  return true;
              }
          }
          return false;
      }
      ...
  }
  ```

### Source Code

#### Package main

##### Class Reversi

* **Description**:

  The `Reversi` class in Java is the main class for running a Reversi (Othello) game. It handles the game's initialization, player input, and game flow. The class manages multiple game boards, prompts players to input their names, and processes their moves. It also displays the game state and determines the winner when the game ends.


- **Key Functions**:

1. **Player Name Input:**
   - `getName(Board.Player player, Scanner scanner)`: Prompts the player to input their name based on their assigned color (`WHITE` or `BLACK`). Returns the player's name.
   - `inputPlayerNames(Scanner scanner, Board[] boards)`: Prompts both players to input their names and sets the names on all game boards.

2. **Move Input and Validation:**
   - `getInput(Scanner scanner)`: Retrieves the player's move input from the console. Returns the input as a string.
   - `getIndex(char code)`: Converts a hexadecimal character to its corresponding integer value.
   - `getCol(char code)`: Converts a column character (e.g., `A`, `B`) to its corresponding integer value.
   - `getRow(char code)`: Converts a row character (e.g., `1`, `2`, `a`, `b`) to its corresponding integer value.
   - `inputPlacePosition(Scanner scanner, Board[] boards)`: Validates the player's move input and places the piece on the specified board if the move is valid. Returns `true` if the move is successful.

3. **Game Flow:**
   - `main(String[] args)`: The main method that runs the Reversi game. It initializes the game boards, prompts for player names, processes player moves, and determines the winner when the game ends.

4. **Game Initialization:**
   
   - Initializes the game with a specified number of boards and canvas dimensions.
   - Creates a `Screen` object to display the game boards.
   - Initializes an array of `Board` objects for the game.
   
5. **Game Loop:**
   - Continuously processes player moves until all boards are in a game-over state.
   - Updates and displays the game boards after each move.

6. **Winner Determination:**
   - Counts the number of boards won by each player.
   - Displays the overall winner or a draw result on the screen.
   
7. **Summary**:

   The `Reversi` class is the main driver for a Reversi game. It handles player input, manages multiple game boards, and controls the game flow. It ensures that the game is played according to the rules and provides a visual representation of the game state. This class is essential for running the game and determining the winner.

#### Package model

##### Class Board

* **Description**:

  The `Board` class in Java represents a game board for Reversi (Othello). It manages the grid of pieces, the current player, and the game state. The class includes functionalities for initializing the board, placing pieces, validating moves, switching players, and determining the winner. It also interacts with a `Canvas` object to display the game board and player information.

* **Key Functions**:

  1. **Enums and Constants:**
     - `Player`: An enumerator representing the players (`WHITE`, `BLACK`, `NONE`).
     - `ULTIMATE_ANSWER`, `canvas_height`, `canvas_width`: Constants for board dimensions and canvas setup.
     - `MAX_BOARD_SIZE`, `DEFAULT_BOARD_SIZE`: Constants for the maximum and default board sizes.

  2. **Constructors:**
     - `Board(int height, int width, Canvas canvas)`: Initializes the board with the specified height and width. Sets up the grid of pieces, initializes the canvas, and starts the game with `BLACK` as the first player.

  3. **Player Management:**
     - `setName(String whitePlayerName, String blackPlayerName)`: Sets the names of the players. Names must be 32 characters or fewer.
     - `switchPlayer()`: Switches the current player between `WHITE` and `BLACK`.
     - `getCurrentPlayerPieceType()`: Returns the piece type (`Piece.Type`) of the current player.

  4. **Piece Placement and Game Logic:**
     - `placePiece(int col, int row)`: Places a piece at the specified column (`col`) and row (`row`) if the move is valid. Updates the board and switches players. Returns `true` if the move is successful.
     - `getValidMoves()`: Checks and displays valid moves for the current player. Returns `true` if there are valid moves.

  5. **Board Initialization and Updates:**
     - `initializeGrid()`: Initializes the grid of pieces and sets the starting pieces in the center of the board.
     - `initializeCanvas()`: Sets up the canvas with edge scales for row and column labels.
     - `updateBoard()`: Updates the canvas to reflect the current state of the board.

  6. **Game State and Winner Determination:**
     - `isGameOver()`: Returns `true` if the game is over.
     - `getWinner()`: Determines and returns the winner (`Player.WHITE`, `Player.BLACK`, or `Player.NONE` for a draw) based on the number of pieces on the board.

  7. **Display Functions:**
     - `paint()`: Displays the game board and either player information or the winner, depending on the game state.
     - `displayPlayerInfo()`: Displays the current player's information on the canvas.
     - `displayWinnerInfo(Player type)`: Displays the winner's information on the canvas.

* **Summary**:

  The `Board` class is the core of a grid-based game like Reversi. It manages the game state, player turns, piece placement, and board display. It ensures that game rules are followed and provides a visual representation of the game through a `Canvas` object. This class is essential for implementing and managing the gameplay logic.

##### Class Piece

* **Description**:

  The `Piece` class in Java represents a game piece in a grid-based game, such as a board game. It manages the type of piece (e.g., White, Black, or None), its position on the grid, and its interactions with neighboring pieces. The class also handles logic for validating moves and flipping pieces based on game rules.

* **Key Functions**:

  1. **Constants and Enums:**
     - `Type`: An enumerator representing the type of piece (`WHITE`, `NONE`, `BLACK`).
     - `NONE_PIECE`, `WHITE_PIECE`, `BLACK_PIECE`, `VALID_MOVE`: Constants for representing piece states and valid moves.
     - `DIRECTIONS`: A 2D array defining the eight possible directions for neighboring pieces.

  2. **Constructors:**
     - `Piece(int col, int row, Piece[][] pieceGrid)`: Initializes a piece at the specified column (`col`) and row (`row`) in the grid. The `pieceGrid` parameter is the grid of pieces the piece belongs to.

  3. **Piece Type Management:**
     - `setType(Piece.Type type)`: Sets the type of the piece (e.g., `WHITE`, `BLACK`, `NONE`).
     - `getType()`: Returns the current type of the piece.

  4. **Pixel Representation:**
     - `getPixel()`: Returns a `Pixel` object representing the piece's visual form based on its type.

  5. **Move Validation:**
     - `isValid(Piece.Type expected_type)`: Checks if placing a piece of the specified type (`expected_type`) at this position is a valid move. Returns `true` if the move is valid.

  6. **Piece Placement and Flipping:**
     - `placePiece(Piece.Type type)`: Places a piece of the specified type at this position and flips neighboring pieces as per game rules. Returns the number of pieces flipped.
     - `flipPieces(int dx, int dy, Piece.Type type, boolean applyChange)`: Recursively flips pieces in a specific direction (`dx`, `dy`). If `applyChange` is `true`, the flip is performed; otherwise, it only checks if the flip is possible. Returns the number of pieces flipped or `-1` if the operation fails.

* **Summary**:

  The `Piece` class is a core component for managing game pieces in a grid-based game. It handles piece types, move validation, and flipping logic, ensuring that game rules are followed. This class is essential for implementing games like Reversi (Othello) or similar board games.

#### Package view

##### Class Screen

* **Description**

  The `Screen` class is designed to manage and manipulate a virtual screen composed of pixels. It provides functionalities to set pixels, print strings, manage canvases for drawing, and display the screen content. The class maintains a buffer of pixels and a flag to track whether the screen content has changed (`dirty` flag). It also handles screen clearing and refreshing operations.

* **Key Functions**:

  1. **Constructor:**
     - `Screen(int height, int width)`: Initializes a screen with the specified height and width. It creates a buffer of pixels and initializes an `occupied` array to track which areas of the screen are in use.
  2. **Pixel Management:**
     - `setPixel(int x, int y, Pixel pix)`: Sets a pixel at the specified coordinates. Marks the screen as dirty if the pixel value changes.
     - `setPixel(Point position, Pixel pix)`: Overloaded method to set a pixel using a `Point` object.
  3. **Text Printing:**
     - `print(int x, int y, String str)`: Prints a string starting at the specified coordinates. Returns the number of characters successfully printed.
  4. **Canvas Management:**
     - `getCanvas(int startX, int startY)`: Returns a `Canvas` object starting at the specified coordinates.
     - `getCanvas(Point startPosition)`: Overloaded method to return a `Canvas` object using a `Point` object.
     - `allocateCanvas(Rect expectedRect)`: Allocates a rectangular area on the screen for a canvas, ensuring no overlap with occupied areas.
     - `freeCanvas(Rect previousRect)`: Frees a previously allocated rectangular area on the screen.
  5. **Screen Display:**
     - `paint()`: Displays the screen content, forcing a refresh.
     - `paint(boolean forceRefresh)`: Displays the screen content, with an option to force a refresh even if the screen hasn't changed.
  6. **Screen Clearing:**
     - `clear()`: Clears the console screen.
     - `clearScreenBuffer()`: Clears the screen buffer by setting all pixels to a default value. Returns `true` if the buffer was not empty before clearing.

* **Summary**:

  The `Screen` class provides a comprehensive set of tools for managing a virtual screen, including pixel manipulation, text rendering, canvas allocation, and screen refreshing. It ensures efficient screen updates by tracking changes and only repainting when necessary.

##### Class Canvas

* **Description**:

  The `Canvas` class in Java is designed to manage a rectangular drawing area on a `Screen`. It provides functionalities to manipulate pixels, print text, resize the canvas, and manage its display. The class maintains a reference to the `Screen` it belongs to, a `Rect` object defining its boundaries, and a `dirty` flag to track whether the canvas content has changed.

* **Key Functions**:

  1. **Constructors:**
     - `Canvas(int startX, int startY, Screen screen)`: Initializes a canvas at the specified starting coordinates (`startX`, `startY`) with default height and width (0).
     - `Canvas(int startX, int startY, int height, int width, Screen screen)`: Initializes a canvas with the specified starting coordinates, height, and width. It allocates the canvas on the `Screen`.
  2. **Canvas Resizing:**
     - `resize(int height, int width)`: Resizes the canvas to the specified height and width. It frees the previously allocated area, updates the `Rect` boundaries, and reallocates the canvas on the `Screen`. Returns `true` if the resizing succeeds; otherwise, `false`.
  3. **Pixel Manipulation:**
     - `setPixel(int x, int y, Pixel pix)`: Sets a pixel at the specified coordinates (`x`, `y`) on the canvas. Marks the canvas as `dirty` if the pixel value changes. Throws an `IndexOutOfBoundsException` if the coordinates are out of bounds.
  4. **Text Printing:**
     - `print(int x, int y, String str)`: Prints a string starting at the specified coordinates (`x`, `y`) on the canvas. Returns the number of characters successfully printed.
  5. **Canvas Display:**
     - `paint(boolean forceRefresh)`: Displays the canvas on the `Screen`. Refreshes the display if the canvas is marked as `dirty` or if `forceRefresh` is `true`. Returns `true` if the canvas is repainted.
     - `forcePaint()`: Forces the canvas to refresh and display its content. Calls the `paint(false)` method internally.
  6. **Canvas Clearing:**
     - `clearCanvas()`: Clears the canvas by setting all pixels to a default value (empty pixel). Marks the canvas as `dirty` and repaints it. Returns the `dirty` flag status.

* **Summary**:

  The `Canvas` class provides tools for managing a rectangular drawing area on a `Screen`. It supports pixel manipulation, text rendering, resizing, and refreshing the canvas. The `dirty` flag ensures efficient updates by tracking changes and only repainting when necessary. This class is essential for applications that require dynamic drawing or text display on a screen.


##### Class Pixel

* **Description**:

  The `Pixel` class in Java represents a single pixel on a screen or canvas. It is designed to handle both single-byte (ASCII) and multi-byte (Unicode) characters. The class stores the pixel's character data and tracks the last painted state to manage changes efficiently.

* **Key Functions**:

  1. **Constructors:**

     - `Pixel()`: Initializes a pixel with a default blank character (`" "`).
     - `Pixel(String character)`: Initializes a pixel with a multi-byte character (e.g., Unicode characters).
     - `Pixel(char character)`: Initializes a pixel with a single-byte character (e.g., ASCII characters).

  2. **Pixel Data Management:**

     - `set(Pixel pix)`:
     - Sets the pixel's character data using another `Pixel` object. Returns:

       - `1` if the data changed.
       - `0` if the data remained the same.
       - `-1` if the data changed back to a previously painted value.

     - `get()`: Returns the current character data of the pixel.

  3. **Buffer Management:**

     - `flush()`: Updates the `lastPainted` buffer to the current character data. This is used to track the last displayed state of the pixel.

* **Summary**:

  The `Pixel` class is a simple yet essential component for managing individual pixels on a screen or canvas. It supports both single-byte and multi-byte characters and provides methods to set, retrieve, and track changes to pixel data. This class is particularly useful for applications that require dynamic rendering of text or graphics.
