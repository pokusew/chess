package cz.martinendler.chess.engine;

import org.jetbrains.annotations.NotNull;

/**
 * A castling right
 *
 * @see <a href="https://www.chessprogramming.org/Castling_Rights">Castling Rights on CPW</a>
 */
public enum CastlingRight {

	/**
	 * King side castling right
	 * <p>
	 * A player can do ONLY kingside castling
	 */
	KING_SIDE,

	/**
	 * Queen side castling right
	 * <p>
	 * A player can do ONLY queenside castling
	 */
	QUEEN_SIDE,

	/**
	 * King and queen side castling right
	 * <p>
	 * A player can do BOTH kingside and queenside castling
	 */
	KING_AND_QUEEN_SIDE,

	/**
	 * None castling right
	 * <p>
	 * A player can do NO castling of any type
	 */
	NONE;

	/**
	 * Determines if this castling right allows the given castling
	 *
	 * @param castling the castling type
	 * @return {@code true} iff this castling right allows the given castling
	 */
	public boolean allows(@NotNull Castling castling) {

		if (castling == Castling.KING_SIDE) {
			return this == KING_AND_QUEEN_SIDE || this == KING_SIDE;
		}

		if (castling == Castling.QUEEN_SIDE) {
			return this == KING_AND_QUEEN_SIDE || this == QUEEN_SIDE;
		}

		// this should never happen as there are no more types of castling
		return false;

	}

}
