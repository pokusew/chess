package cz.martinendler.chess.engine;

/**
 * @see <a href="https://www.chessprogramming.org/Algebraic_Chess_Notation">Algebraic Chess Notation on CPW</a>
 */
public enum Notation {

	/**
	 * Figurine Algebraic Notation uses miniature piece icons instead of single letter piece abbreviations
	 *
	 * @see <a href="https://www.chessprogramming.org/Algebraic_Chess_Notation#Standard_Algebraic_Notation_.28SAN.29">SAN on CPW</a>
	 */
	SAN,

	/**
	 * Figurine Algebraic Notation uses miniature piece icons instead of single letter piece abbreviations
	 *
	 * @see <a href="https://www.chessprogramming.org/Algebraic_Chess_Notation#Figurine_Algebraic_Notation_.28FAN.29">FAN on CPW</a>
	 */
	FAN,

	/**
	 * FEN
	 *
	 * @see <a href="https://www.chessprogramming.org/Forsyth-Edwards_Notation#Piece_Placement">FEN on CPW</a>
	 */
	FEN;

}
