package cz.martinendler.chess.engine;

/**
 * A side (player color) in chess
 *
 * @see <a href="https://www.chessprogramming.org/Side_to_move">Side to move on CPW</a>
 * @see <a href="https://www.chessprogramming.org/Color">Color on CPW</a>
 */
public enum Side {

	// note: the order in which the enum values are declared here MATTERS!
	//       and the rest of the code depends on that order (via Side.ordinal())

	/**
	 * White side
	 */
	WHITE,
	/**
	 * Black side
	 */
	BLACK;

	/**
	 * Returns the opposite side
	 *
	 * @return the opposite side
	 */
	public Side flip() {
		return this == Side.WHITE ? Side.BLACK : Side.WHITE;
	}

	public boolean isWhite() {
		return this == Side.WHITE;
	}

	public boolean isBlack() {
		return this == Side.BLACK;
	}

}
