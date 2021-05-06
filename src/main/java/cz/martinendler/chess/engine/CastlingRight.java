package cz.martinendler.chess.engine;

/**
 * @see <a href="https://www.chessprogramming.org/Castling_Rights">Castling Rights on CPW</a>
 */
public enum CastlingRight {

	/**
	 * King side castling right
	 *
	 * A player can do ONLY kingside castling
	 */
	KING_SIDE,

	/**
	 * Queen side castling right
	 *
	 * A player can do ONLY queenside castling
	 */
	QUEEN_SIDE,

	/**
	 * King and queen side castling right
	 *
	 * A player can do BOTH kingside and queenside castling
	 */
	KING_AND_QUEEN_SIDE,

	/**
	 * None castling right
	 *
	 * A player can do NO castling of any type
	 */
	NONE;

}
