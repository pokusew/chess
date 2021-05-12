package cz.martinendler.chess.engine.board;

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

	/**
	 * Notation
	 */
	String notation;

	File(String notation) {
		this.notation = notation;
	}

	/**
	 * Gets notation
	 *
	 * @return the notation
	 */
	public String getNotation() {
		return notation;
	}

}
