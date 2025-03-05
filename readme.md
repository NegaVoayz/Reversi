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
  // recursively check whether a flip is possible
  // apply change used to check.
  private int flip(int dx, int dy, Type type, boolean apply_change) {
      if(this.type == Type.None) {
          return -1; 
      }
      if(this.type == type) {
          return 0;
      }
  
      // try flip next
      int ans = grid[y+dy][x+dx].flip(dx, dy, type, apply_change);
  
      // if return number is negative, keep it below zero
      if(ans < 0) {
          return -1;
      }
  
      // change the grid if applied
      // or work as validation check only
      if(apply_change) {
          this.type = type;
      }
  
      // return type is the number of pieces flipped (or able to be flipped)
      // or negative number showing operation failed
      return ans + 1;
  }
  ```

#### Validation

* Design

  Firstly, when player have no valid moves, he skip his turn.

  And, if one player skipped and the other skipped too, there's no valid moves and game ends.

  So this function use parameter `skipped` to store whether the current player skipped his turn.

  If found possible move, paint on the canvas and return `true` as succeed.

  If no skip and no possible move, skip and recurse.

  If skipped and no possible move, set `end` to `true` and return `false` as failed.

* Code

  ```java
  // get and show valid moves for current player
  // automatically skip turns if no moves valid
  // if both players have no valid moves, set game end
  private boolean get_vaild_moves(boolean skipped) {
      boolean movable = false;
      Piece.Type type;
      if(current_player == Player.White) {
          type = Piece.Type.White;
      } else {
          type = Piece.Type.Black;
      }
      for(int i = 1; i <= height; i++) {
          for(int j = 1; j <= width; j++) {
              if(grid[i][j].is_valid(type)) {
                  movable = true;
                  canvas.setPixel(j, i, new Pixel('·'));
              }
          }
      }
      if(movable) {
          return true;
      }
      if(skipped) {
          return false;
      }
      change_player();
      return get_vaild_moves(true);
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
