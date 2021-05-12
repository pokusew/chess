package cz.martinendler.chess.engine.board;

/**
 * A diagonal in the same direction as the Main Diagonal on the chessboard
 * <p>
 * In total there is 15 such diagonals [idx 0-14] (including the Main Diagonal).
 *
 * @see <a href="https://www.chessprogramming.org/Diagonals">Diagonals on CPW</a>
 */
public enum DiagonalA1H8 {

	// note: the order in which the enum values are declared here MATTERS!
	//       and the rest of the code depends on that order (via DiagonalA1H8.ordinal())

	/**
	 * The diagonal from A8 to A8 (1 square, light) (idx 0)
	 */
	A8_A8,
	/**
	 * The diagonal from B8 to A7 (2 squares, dark) (idx 1)
	 */
	B8_A7,
	/**
	 * The diagonal from C8 to A6 (3 squares, light) (idx 2)
	 */
	C8_A6,
	/**
	 * The diagonal from D8 to A5 (4 squares, dark) (idx 3)
	 */
	D8_A5,
	/**
	 * The diagonal from E8 to A4 (5 squares, light) (idx 4)
	 */
	E8_A4,
	/**
	 * The diagonal from F8 to A3 (6 squares, dark) (idx 5)
	 */
	F8_A3,
	/**
	 * The diagonal from G8 to A2 (7 squares, light) (idx 6)
	 */
	G8_A2,
	/**
	 * The diagonal from H8 to A1 (8 squares, dark) (idx 7) (the Main Diagonal)
	 */
	H8_A1,
	/**
	 * The diagonal from B1 to H7 (7 squares, light) (idx 8)
	 */
	B1_H7,
	/**
	 * The diagonal from C1 to H6 (6 squares, dark) (idx 9)
	 */
	C1_H6,
	/**
	 * The diagonal from D1 to H5 (5 squares, light) (idx 10)
	 */
	D1_H5,
	/**
	 * The diagonal from E1 to H4 (4 squares, dark) (idx 11)
	 */
	E1_H4,
	/**
	 * The diagonal from F1 to H3 (3 squares, light) (idx 12)
	 */
	F1_H3,
	/**
	 * The diagonal from G1 to H2 (2 squares, dark) (idx 13)
	 */
	G1_H2,
	/**
	 * The diagonal from H1 to H1 (1 square, light) (idx 14)
	 */
	H1_H1;

}
