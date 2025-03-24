# Reversi Project 2.2.1

## Class & Function Document

### Index

[toc]

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

#### Board, Pieces, and Rules:

* **Rules**

  Rule is an interface of a general set of rules that varies among games.

* **Pieces**

​	Piece is changed to a interface to fit more chess games, perhaps.

* **Board**

  Board is set as a general type that fits almost all chess games with only black and white pieces.

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
