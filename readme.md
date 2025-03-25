# Reversi Project 2.3.0

## Class & Function Document

### Index

[toc]

### How to play(command manual)

* #### Main Command

  Well, `help` must be the most useful and important command.

* #### Other Commands

  Why not calling `help` for help?

  | command      | arguments                                        | effect                      |
  | ------------ | ------------------------------------------------ | --------------------------- |
  | help/man     |                                                  | help                        |
  | switch to    | board NO                                         | switch to the desired board |
  | move         | position(e.g. 3D)                                | place piece at [position]   |
  | create board | [mode: reversi/peace] ([column size] [row size]) | create new board            |
  | list         | ([mode: reversi/peace/current])                  | list the boards.            |

### Core Design:

Using the MVC structure and interfaces to decouple the view, game and user interface.

#### Controllers:

* **Initialization Controller**

  Initialize boards, including Input guide.

  set starting boards and player names

* **Game Controller**

  Controls the interactions with multiple boards.

* **Input Controller**

  Handles user inputs and parse instructions for the game controller.

* **Settlement Controller**

  Show the final result.

#### Board, Pieces, and Rules:

* **Rules**

  Rule is an interface of a general set of rules that varies among games.

* **Pieces**

​	Piece is changed to a interface to fit more chess games, perhaps.

* **Board**

  Board is set as a general type that fits almost all chess games with only black and white pieces.
  
  Can be constructed by `BoardFactory`

#### View

* **Window**

  Window is the view buffer of an entity.

  It allows overlap and will clear the screen before paint.

* **View**

  view is an area in the window, it can't overlap and is the unit for painting.

  All painting missions should ***only*** use view.

  All painting operations will not change the corresponding views in the same window, that means, they will stay the same while the chosen view is updated.

### Update Record

#### V2.0.1

Rewrite the game in java.

The C version is not available since I don't want to publish that.

#### V2.1.1

Add `Screen` class to manage the whole window paint process. Preparing  for the coming version featuring multiple games shown  at the same time.

#### V2.1.2

Add multi-board feature, fixed occupation error and other bugs.

#### V2.1.3

Fix some spelling mistakes. Add more comments.

#### V2.2.0

Refactored the project to decouple.

#### V2.2.1

Turn all confusing coordinates into `Point`

#### V2.3.0

Use `BoardFactory` to initialize boards.

Enables more functions.

Use command-based input style.
