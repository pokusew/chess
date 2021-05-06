package cz.martinendler.chess.engine;

/**
 * A side (player color) in chess
 *
 * @see <a href="https://www.chessprogramming.org/Side_to_move">Side to move on CPW</a>
 * @see <a href="https://www.chessprogramming.org/Color">Color on CPW</a>
 */
public enum Side {

	/**
	 * White side
	 */
	WHITE,
	/**
	 * Black side
	 */
	BLACK;

	public static Side[] allSides = values();

	/**
	 * From value side.
	 *
	 * @param v the v
	 * @return the side
	 */
	public static Side fromValue(String v) {
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
