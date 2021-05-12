package cz.martinendler.chess.engine.pieces;

/**
 * A type of a chess piece {@link Piece}
 */
public enum PieceType {

	// note: the order in which the enum values are declared here MATTERS!
	//       and the rest of the code depends on that order (via PieceType.ordinal())

	/**
	 * Pawn piece type
	 */
	PAWN(1),
	/**
	 * Knight piece type
	 */
	KNIGHT(3),
	/**
	 * Bishop piece type
	 */
	BISHOP(3),
	/**
	 * Rook piece type
	 */
	ROOK(5),
	/**
	 * Queen piece type
	 */
	QUEEN(9),
	/**
	 * King piece type
	 */
	KING(0);

	private final int value;

	PieceType(int value) {
		this.value = value;
	}

	/**
	 * Gets value of this piece type
	 *
	 * @return positive integer if the piece type has a value, {@code 0} otherwise (king has no measurable value)
	 * @see <a href="https://www.chessprogramming.org/Point_Value">Point Value on CPW</a>
	 * @see <a href="http://www.chessfornovices.com/chesspiecevalues.html">Chess Piece Values</a>
	 */
	public int getValue() {
		return value;
	}
}
