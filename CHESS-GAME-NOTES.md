# Chess Game Notes

My notes related to the implementation of chess game.
Used as brainstorming place during development.
Also, it contains useful links to chess related tools and standards. 


## Brainstorming

```

https://www.chessprogramming.org/Board_Representation

https://www.chessprogramming.org/Fifty-move_Rule

https://www.chessprogramming.org/Ranks
https://www.chessprogramming.org/Files

ranks = rows (numbered from bottom to top)
    (board TOP)
    visual number / id
    8 - 7
    7 - 6
    6 - 5
    5 - 4
    4 - 3
    3 - 2
    2 - 1
    1 - 0
    (board BOTTOM)

ranks = columns (labeled from left to right)
    visual label: (board LEFT) a b c d e f g h (board RIGHT)
              id:              0 1 2 3 4 5 6 7


Preferences
	Animation
	valid moves
	mark last move

	Highlight Last Move
	Play Sounds
	Show Legal Moves

	Show legal moves true/false
	Play Sounds true/false

File
	New Game
	Open Game
	Save Game

Edit
	Undo Move
	Redo Move

NewGameDialog
	against player
	against computer
	network game

	use chess clock option
```

```
isCheck
isCheckmate
isStalemate

Piece
Square
Square

piece.getSquare().getGame()

piece.getSquare().getAdjacentTopLeft()

Position

game = new Game()

move([0,0], [0,4]) --> true/false

board
	piece

Game
	Board board
		Square square
			Piece piece

move()

Piece

PieceLogic

IPiece

Game
	config
	moves
	Board squares
```


## Rules

* https://en.wikipedia.org/wiki/Rules_of_chess
* https://handbook.fide.com/chapter/E012018
* https://www.fide.com/FIDE/handbook/LawsOfChess.pdf


## Terms

* Game
* GameBoard / ChessBoard
* File
* Rank
* Square
* Piece
* Type
* Color WHITE / BLACK
* Square
* Piece
	* Pawn
	* Rook
	* Knight
	* Bishop
	* Queen
	* King
* Check
* Checkmate
* Stalemate


## UCI (Universal Chess Interface)

* https://www.shredderchess.com/chess-features/uci-universal-chess-interface.html
* https://www.shredderchess.com/download.html
* https://www.chessprogramming.org/UCI


# PGN (Portable Game Notation)

* http://www.saremba.de/chessgml/standards/pgn/pgn-complete.htm
* https://en.wikipedia.org/wiki/Portable_Game_Notation
* https://www.chess.com/analysis


## Chess engines

* https://stockfishchess.org/
* https://github.com/gcp/sjeng


## Online chess

* https://www.chess.com/cs/play/computer
