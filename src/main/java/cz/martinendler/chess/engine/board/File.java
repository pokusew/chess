package cz.martinendler.chess.engine.board;

import org.jetbrains.annotations.NotNull;

/**
 * A file (i.e. column) on a chessboard
 *
 * @see <a href="https://www.chessprogramming.org/Files">Files on CPW</a>
 */
public enum File {

	// note: the order in which the enum values are declared here MATTERS!
	//       and the rest of the code depends on that order (via File.ordinal())

	/**
	 * File a file (0)
	 */
	FILE_A("A"),
	/**
	 * File b file (1)
	 */
	FILE_B("B"),
	/**
	 * File c file (2)
	 */
	FILE_C("C"),
	/**
	 * File d file (3)
	 */
	FILE_D("D"),
	/**
	 * File e file (4)
	 */
	FILE_E("E"),
	/**
	 * File f file (5)
	 */
	FILE_F("F"),
	/**
	 * File g file (6)
	 */
	FILE_G("G"),
	/**
	 * File h file (7)
	 */
	FILE_H("H");

	// see https://stackoverflow.com/questions/19303511/enum-values-method-efficiency
	private static final @NotNull File[] values = values();

	/**
	 * Notation
	 */
	private final @NotNull String notation;

	File(@NotNull String notation) {
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
	 * Gets the corresponding file for the given index
	 *
	 * @param index file index in range [0, 7]
	 * @return the corresponding file for the given index
	 * @throws ArrayIndexOutOfBoundsException when an invalid index is given
	 */
	public static @NotNull File fromIndex(int index) {
		return values[index];
	}

}
