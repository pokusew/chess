package cz.martinendler.chess.engine.pieces;

import cz.martinendler.chess.engine.PlayerType;

abstract public class Piece {

	public final PlayerType color;

	public Piece(PlayerType color) {
		this.color = color;
	}

	abstract public void getMoves();

}
