package cz.martinendler.chess.engine.board;

import org.jetbrains.annotations.NotNull;

/**
 * A rank (i.e. row) on a chessboard
 *
 * @see <a href="https://www.chessprogramming.org/Ranks">Ranks on CPW</a>
 */
public enum Rank {

	// note: the order in which the enum values are declared here MATTERS!
	//       and the rest of the code depends on that order (via Rank.ordinal())

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
	RANK_8("8");

	// see https://stackoverflow.com/questions/19303511/enum-values-method-efficiency
	private static final @NotNull Rank[] values = values();

	/**
	 * Notation
	 */
	private final @NotNull String notation;

	Rank(@NotNull String notation) {
		this.notation = notation;
	}

	/**
	 * Gets notation
	 *
	 * @return the notation
	 */
	public @NotNull String getNotation() {
		return notation;
	}

	/**
	 * Gets the corresponding rank for the given index
	 *
	 * @param index rank index in range [0, 7]
	 * @return the corresponding rank for the given index
	 * @throws ArrayIndexOutOfBoundsException when an invalid index is given
	 */
	public static @NotNull Rank fromIndex(int index) {
		return values[index];
	}

}
