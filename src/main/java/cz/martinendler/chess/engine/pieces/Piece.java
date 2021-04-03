package cz.martinendler.chess.engine.pieces;

import cz.martinendler.chess.engine.Move;
import cz.martinendler.chess.engine.PlayerType;

import java.util.List;

/**
 * A chess piece within a chess {@link cz.martinendler.chess.engine.Game}
 */
abstract public class Piece {

	/**
	 * An unique immutable identifier
	 * (across all pieces within the given {@link cz.martinendler.chess.engine.Game}, not matter their color)
	 * assigned upon piece creation.
	 */
	public final int id;

	/**
	 * Color indicates to which player the piece belongs
	 */
	public final PlayerType color;

	// TODO: in the future, piece value might be useful (for moves analysis):
	//       A pawn is worth one point,
	//       a knight or bishop is worth three points,
	//       a rook is worth five points
	//       and a queen is worth nine points.
	//       The king is the only piece that doesn't have a point value.

	public Piece(int id, PlayerType color) {
		this.id = id;
		this.color = color;
	}

	// TODO: rethink
	abstract public List<Move> getMoves();

}
