package cz.martinendler.chess.engine.pieces;

/**
 * The enum Piece type.
 */
public enum PieceType {

	/**
	 * Pawn piece type.
	 */
	PAWN,
	/**
	 * Knight piece type.
	 */
	KNIGHT,
	/**
	 * Bishop piece type.
	 */
	BISHOP,
	/**
	 * Rook piece type.
	 */
	ROOK,
	/**
	 * Queen piece type.
	 */
	QUEEN,
	/**
	 * King piece type.
	 */
	KING,
	/**
	 * None piece type.
	 */
	NONE;

	/**
	 * From value piece type.
	 *
	 * @param v the v
	 * @return the piece type
	 */
	public static PieceType fromValue(String v) {
		return valueOf(v);
	}

	/**
	 * Value string.
	 *
	 * @return the string
	 */
	public String value() {
		return name();
	}

}
