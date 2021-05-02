package cz.martinendler.chess.engine;

/**
 * @see <a href="https://www.chessprogramming.org/Castling_Rights">Castling Rights on CPW</a>
 */
public enum CastlingRight {

	/**
	 * King side castle right.
	 */
	KING_SIDE,

	/**
	 * Queen side castle right.
	 */
	QUEEN_SIDE,

	/**
	 * King and queen side castle right.
	 */
	KING_AND_QUEEN_SIDE,

	/**
	 * None castle right.
	 */
	NONE;

}
