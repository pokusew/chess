package cz.martinendler.chess.engine.board;

/**
 * A anti-diagonal in the same direction as the Main Anti-Diagonal on the chessboard
 * <p>
 * In total there is 15 such diagonals [idx 0-14] (including the Main Anti-Diagonal).
 *
 * @see <a href="https://www.chessprogramming.org/Anti-Diagonals">Anti-Diagonals on CPW</a>
 * @see <a href="https://www.chessprogramming.org/Diagonals">Diagonals on CPW</a>
 */
public enum DiagonalH1A8 {

	// note: the order in which the enum values are declared here MATTERS!
	//       and the rest of the code depends on that order (via DiagonalH1A8.ordinal())

	/**
	 * The diagonal from A1 to A1 (1 square, dark) (idx 0)
	 */
	A1_A1,
	/**
	 * The diagonal from B1 to A2 (2 squares, light) (idx 1)
	 */
	B1_A2,
	/**
	 * The diagonal from C1 to A3 (3 squares, dark) (idx 2)
	 */
	C1_A3,
	/**
	 * The diagonal from D1 to A4 (4 squares, light) (idx 3)
	 */
	D1_A4,
	/**
	 * The diagonal from E1 to A5 (5 squares, dark) (idx 4)
	 */
	E1_A5,
	/**
	 * The diagonal from F1 to A6 (6 squares, light) (idx 5)
	 */
	F1_A6,
	/**
	 * The diagonal from G1 to A7 (7 squares, dark) (idx 6)
	 */
	G1_A7,
	/**
	 * The diagonal from H1 to A8 (8 squares, light) (idx 7) (the Main Anti-Diagonal)
	 */
	H1_A8,
	/**
	 * The diagonal from B8 to H2 (7 squares, dark) (idx 8)
	 */
	B8_H2,
	/**
	 * The diagonal from C8 to H3 (6 squares, light) (idx 9)
	 */
	C8_H3,
	/**
	 * The diagonal from D8 to H4 (5 squares, dark) (idx 10)
	 */
	D8_H4,
	/**
	 * The diagonal from E8 to H5 (4 squares, light) (idx 11)
	 */
	E8_H5,
	/**
	 * The diagonal from F8 to H6 (3 squares, dark) (idx 12)
	 */
	F8_H6,
	/**
	 * The diagonal from G8 to H7 (2 squares, light) (idx 13)
	 */
	G8_H7,
	/**
	 * The diagonal from H8 to H8 (1 square, dark) (idx 14)
	 */
	H8_H8;

}
