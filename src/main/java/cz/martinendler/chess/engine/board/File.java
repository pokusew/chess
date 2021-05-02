package cz.martinendler.chess.engine.board;

/**
 * A file (i.e. column) on a chessboard
 *
 * @see <a href="https://www.chessprogramming.org/Files">Files on CPW</a>
 */
public enum File {

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
	FILE_H("H"),
	/**
	 * None file (8) (used instead of null)
	 */
	NONE("");

	public static File[] allFiles = values();

	/**
	 * Notation
	 */
	String notation;

	File(String notation) {
		this.notation = notation;
	}

	/**
	 * From value file.
	 *
	 * @param v the v
	 * @return the file
	 */
	public static File fromValue(String v) {
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

