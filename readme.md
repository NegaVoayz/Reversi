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

* Description

  The main class of the game. 

* Function

  It handles all the IO operations.

  Contains main loop and initialization.

#### Package model

##### Class Board

* Description

  Board class is the main integration class of the game.

  It integrates all pieces.

* Function

  Provides methods to try lay piece

  Wrapped paint function

  Encapsulated valid move display

  Automatic game end check

##### Class Piece

* Description

  Piece class is the core unit of the game, instead of board.

  the core validation check and flip operation is in it.

* Function

  Store piece type ( black, white, none )

  Move Validation Check ( yes, our core function is in the piece class)

  Recursive Flip

#### Package view

##### Class Canvas

* Description

  Canvas is used to store and print a block of Pixels.

  Currently the canvas is only a block of characters.

* Function

  It can be changed pixel by pixel.

  Has a lazy painting mechanic which refuse to paint if no actual changes.

##### Class Pixel

* Description

  Pixel is a support class for Canvas now designed to store one character.

* Function

  It will automatically check whether the set function really changed its data.
