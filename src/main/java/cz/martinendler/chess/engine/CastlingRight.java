package cz.martinendler.chess.engine;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A castling right
 *
 * @see <a href="https://www.chessprogramming.org/Castling_Rights">Castling Rights on CPW</a>
 */
public enum CastlingRight {

	/**
	 * King and queen side castling right
	 * <p>
	 * A player can do BOTH kingside and queenside castling
	 */
	KING_AND_QUEEN_SIDE("KQ"),

	/**
	 * King side castling right
	 * <p>
	 * A player can do ONLY kingside castling
	 */
	KING_SIDE("K"),

	/**
	 * Queen side castling right
	 * <p>
	 * A player can do ONLY queenside castling
	 */
	QUEEN_SIDE("Q"),

	/**
	 * None castling right
	 * <p>
	 * A player can do NO castling of any type
	 */
	NONE("");

	// see https://stackoverflow.com/questions/507602/how-can-i-initialise-a-static-map

	private static final Map<String, CastlingRight> fenNotationToCastlingRight = Arrays.stream(values())
		.collect(Collectors.toMap((CastlingRight r) -> r.getFenNotation(Side.WHITE), r -> r));

	private final @NotNull String whiteFenNotation;
	private final @NotNull String blackFenNotation;

	CastlingRight(@NotNull String fenNotation) {
		this.whiteFenNotation = fenNotation.toUpperCase();
		this.blackFenNotation = fenNotation.toLowerCase();
	}

	/**
	 * Gets castling notation for usage in FEN
	 * <p>
	 * It is different for each {@link Side}:
	 * {@link Side#WHITE} pieces: KQ K Q (UPPERCASE letters)
	 * {@link Side#BLACK} pieces: kq k q (lowercase letters)
	 *
	 * @param side the side that has this castling right as the notation is side-dependent
	 * @return the castling notation for usage in FEN
	 */
	public @NotNull String getFenNotation(@NotNull Side side) {
		return side.isWhite() ? whiteFenNotation : blackFenNotation;
	}

	/**
	 * Creates a castling from the castling notation as used in FEN
	 *
	 * @param fenNotation the castling notation as used in FEN
	 * @return {@link CastlingRight} or {@code null} if there is no castling for the given notation
	 * @see CastlingRight#getFenNotation(Side side)
	 */
	public static @Nullable CastlingRight fromNotation(@NotNull String fenNotation) {
		return fenNotationToCastlingRight.get(fenNotation);
	}

	/**
	 * Determines if this castling right allows the given castling
	 *
	 * @param castling the castling type
	 * @return {@code true} iff this castling right allows the given castling
	 */
	public boolean allows(@NotNull Castling castling) {

		if (castling == Castling.KING_SIDE) {
			return this == KING_AND_QUEEN_SIDE || this == KING_SIDE;
		}

		if (castling == Castling.QUEEN_SIDE) {
			return this == KING_AND_QUEEN_SIDE || this == QUEEN_SIDE;
		}

		// this should never happen as there are no more types of castling
		return false;

	}

}
