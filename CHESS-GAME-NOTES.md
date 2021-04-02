# Chess rules and notes


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


## Plan


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

		time

	Technical Manual

	User Manual
		what the app can do
		how to control it


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
* Ctalemate


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
