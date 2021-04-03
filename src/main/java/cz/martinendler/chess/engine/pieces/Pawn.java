package cz.martinendler.chess.engine.pieces;

import cz.martinendler.chess.engine.Move;
import cz.martinendler.chess.engine.PlayerType;

import java.util.List;

public class Pawn extends Piece {

	public Pawn(int id, PlayerType color) {
		super(id, color);
	}

	@Override
	public List<Move> getMoves() {
		return null;
	}

}
