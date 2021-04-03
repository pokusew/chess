package cz.martinendler.chess.engine;

import cz.martinendler.chess.engine.pieces.Piece;

/**
 * A move within a chess {@link cz.martinendler.chess.engine.Game}
 */
public class Move {

	/**
	 * An unique immutable identifier (across all moves within the given game)
	 * assigned upon move creation.
	 *
	 * It is an auto-increment style id (1st move has id 0, 2nd has id 1, etc.).
	 */
	public final int id;

	public final Piece piece;
	public final Square src;
	public final Square dest;

	public Move(int id, Piece piece, Square src, Square dest) {
		this.id = id;
		this.piece = piece;
		this.src = src;
		this.dest = dest;
	}

}
