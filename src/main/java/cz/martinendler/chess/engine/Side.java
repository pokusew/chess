package cz.martinendler.chess.engine;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

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
	WHITE("w"),
	/**
	 * Black side
	 */
	BLACK("b");

	// see https://stackoverflow.com/questions/507602/how-can-i-initialise-a-static-map

	private static final Map<String, Side> notationToPiece = Arrays.stream(values())
		.collect(Collectors.toMap(Side::getNotation, p -> p));

	private final @NotNull String notation;

	Side(@NotNull String notation) {
		this.notation = notation;
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

	/**
	 * Gets side notation for usage in SAN/FEN
	 *
	 * @return the side notation for usage in SAN/FEN
	 */
	public @NotNull String getNotation() {
		return notation;
	}

	/**
	 * Creates a side from the side notation as used in SAN/FEN
	 *
	 * @param notation the side notation as used in SAN/FEN
	 * @return {@link Side} or {@code null} if there is no side for the given notation
	 * @see Side#getNotation()
	 */
	public static @Nullable Side fromNotation(@NotNull String notation) {
		return notationToPiece.get(notation);
	}

}
