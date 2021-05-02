package cz.martinendler.chess.engine;

/**
 * A possible
 *
 * @see <a href="https://www.chessprogramming.org/Side_to_move">Side to move on CPW</a>
 * @see <a href="https://www.chessprogramming.org/Color">Color on CPW</a>
 */
public enum Side {

	/**
	 * White side.
	 */
	WHITE,
	/**
	 * Black side.
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
	 * Flip side.
	 *
	 * @return the side
	 */
	public Side flip() {
		return Side.WHITE.equals(this)
			? Side.BLACK
			: Side.WHITE;
	}

}
