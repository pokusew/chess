package cz.martinendler.chess.engine;

import cz.martinendler.chess.engine.board.Board;

public class Game {

	private Board board;

	public Game() {

		board = new Board();
		board.loadFromFen(Constants.startStandardFENPosition);

	}

	// TODO
	public boolean isCheck() {
		return false;
	}

	// TODO
	public boolean isCheckmate() {
		return false;
	}

	// TODO
	public boolean isStalemate() {
		return false;
	}

}
