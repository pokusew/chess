package cz.martinendler.chess.engine.board;

/**
 * A rank (i.e. row) on a chessboard
 *
 * @see <a href="https://www.chessprogramming.org/Ranks">Ranks on CPW</a>
 */
public enum Rank {

	/**
	 * Rank 1 rank (0)
	 */
	RANK_1("1"),
	/**
	 * Rank 2 rank (1)
	 */
	RANK_2("2"),
	/**
	 * Rank 3 rank (2)
	 */
	RANK_3("3"),
	/**
	 * Rank 4 rank (3)
	 */
	RANK_4("4"),
	/**
	 * Rank 5 rank (4)
	 */
	RANK_5("5"),
	/**
	 * Rank 6 rank (5)
	 */
	RANK_6("6"),
	/**
	 * Rank 7 rank (6)
	 */
	RANK_7("7"),
	/**
	 * Rank 8 rank (7)
	 */
	RANK_8("8"),
	/**
	 * None rank (8) (used instead of null)
	 */
	NONE("");

	public static Rank[] allRanks = values();

	/**
	 * Notation
	 */
	String notation;

	Rank(String notation) {
		this.notation = notation;
	}

	/**
	 * From value rank.
	 *
	 * @param v the v
	 * @return the rank
	 */
	public static Rank fromValue(String v) {
		return valueOf(v);
	}

	/**
	 * Gets notation.
	 *
	 * @return the notation
	 */
	public String getNotation() {
		return notation;
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
