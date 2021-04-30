package cz.martinendler.chess.pgn.entity;


import java.util.HashMap;
import java.util.Map;

/**
 * Game Termination Marker
 * <p>
 * Each movetext section has exactly one game termination marker;
 * the marker always occurs as the last element in the movetext.
 * The game termination marker is a symbol that is one of the following four values: "1-0" (White wins),
 * "0-1" (Black wins), "1/2-1/2" (drawn game), and "*" (game in progress, result unknown, or game abandoned).
 * Note that the digit zero is used in the above; not the upper case letter "O".
 * <p>
 * The game termination marker appearing in the movetext of a game must match the value of the game's
 * Result tag pair. (While the marker appears as a string in the Result tag,
 * it appears as a symbol without quotes in the movetext.)
 *
 * @see <a href="http://www.saremba.de/chessgml/standards/pgn/pgn-complete.htm#c8.2.6">PGN 8.2.6: Game Termination Markers</a>
 * @see <a href="http://www.saremba.de/chessgml/standards/pgn/pgn-complete.htm#c8.2.6">PGN 8.2.6: Game Termination Markers</a>
 */
public enum PgnGameTermination {

	WHITE_WINS("1-0"),
	BLACK_WINS("0-1"),
	DRAWN_GAME("1/2-1/2"),
	/**
	 * game in progress, result unknown, or game abandoned
	 */
	UNKNOWN("*");

	private static final Map<String, PgnGameTermination> fromNotation = new HashMap<>(4);

	static {
		for (PgnGameTermination value : values()) {
			fromNotation.put(value.getNotation(), value);
		}

		fromNotation.put("0-1", BLACK_WINS);
		fromNotation.put("1/2-1/2", DRAWN_GAME);
		fromNotation.put("*", UNKNOWN);
	}

	final private String notation;

	PgnGameTermination(String notation) {
		this.notation = notation;
	}

	public static PgnGameTermination fromNotation(String s) {
		return fromNotation.get(s);
	}

	public String getNotation() {
		return notation;
	}

}
