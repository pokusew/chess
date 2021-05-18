package cz.martinendler.chess.engine.pieces;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A type of a chess piece {@link Piece}
 */
public enum PieceType {

	// note: the order in which the enum values are declared here MATTERS!
	//       and the rest of the code depends on that order (via PieceType.ordinal())

	/**
	 * Pawn piece type
	 */
	PAWN("P", 1),
	/**
	 * Knight piece type
	 */
	KNIGHT("N", 3),
	/**
	 * Bishop piece type
	 */
	BISHOP("B", 3),
	/**
	 * Rook piece type
	 */
	ROOK("R", 5),
	/**
	 * Queen piece type
	 */
	QUEEN("Q", 9),
	/**
	 * King piece type
	 */
	KING("K", 0);

	// see https://stackoverflow.com/questions/507602/how-can-i-initialise-a-static-map

	private static final Map<String, PieceType> sanNotationToPiece = Arrays.stream(values())
		.collect(Collectors.toMap(PieceType::getSanNotation, p -> p));

	private static final List<PieceType> pawnPromotionTypes = List.of(
		QUEEN,
		ROOK,
		BISHOP,
		KNIGHT
	);

	/**
	 * One letter notation for usage in SAN
	 * <p>
	 * Pieces: P N B R Q K (uppercase letters)
	 */
	private final @NotNull String sanNotation;

	/**
	 * Integer that expresses value of this piece type
	 * <p>
	 * Positive integer if the piece type has a value, {@code 0} otherwise (king has no measurable value).
	 * <p>
	 * NOTE: currently not used, may be used in the future for chess positions ranking
	 *
	 * @see <a href="https://www.chessprogramming.org/Point_Value">Point Value on CPW</a>
	 * @see <a href="http://www.chessfornovices.com/chesspiecevalues.html">Chess Piece Values</a>
	 */
	private final int value;

	PieceType(@NotNull String sanNotation, int value) {
		this.sanNotation = sanNotation;
		this.value = value;
	}

	/**
	 * Gets piece type one letter notation for usage in SAN
	 * <p>
	 * Pieces: P N B R Q K (UPPERCASE letters)
	 *
	 * @return the piece type one letter notation for usage in SAN
	 * @see PieceType#fromSanNotation(String sanNotation)
	 */
	public @NotNull String getSanNotation() {
		return sanNotation;
	}

	/**
	 * Creates a piece type from the piece type notation as used in SAN
	 *
	 * @param sanNotation the piece type notation as used in SAN
	 * @return {@link Piece} or {@code null} if there is no piece for the given notation
	 * @see PieceType#getSanNotation()
	 */
	public static @Nullable PieceType fromSanNotation(@NotNull String sanNotation) {
		return sanNotationToPiece.get(sanNotation);
	}

	/**
	 * Gets value of this piece type
	 *
	 * @return positive integer if the piece type has a value, {@code 0} otherwise ({@link PieceType#KING} has no measurable value)
	 * @see <a href="https://www.chessprogramming.org/Point_Value">Point Value on CPW</a>
	 * @see <a href="http://www.chessfornovices.com/chesspiecevalues.html">Chess Piece Values</a>
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Gets all possible piece types a pawn can be promoted to
	 * <p>
	 * Sorted by descending piece type value
	 *
	 * @return all possible piece types a pawn can be promoted to
	 */
	public static List<PieceType> getPawnPromotionTypes() {
		return pawnPromotionTypes;
	}

}
