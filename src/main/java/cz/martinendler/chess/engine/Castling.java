package cz.martinendler.chess.engine;

import cz.martinendler.chess.engine.board.Bitboard;
import cz.martinendler.chess.engine.board.Square;
import cz.martinendler.chess.engine.move.Move;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A castling
 *
 * @see <a href="https://www.chessprogramming.org/Castling">Castling</a>
 */
public enum Castling {

	/**
	 * King side castling (the short one) (O-O)
	 */
	KING_SIDE("O-O"),

	/**
	 * Queen side castling (the long one) (O-O-O)
	 */
	QUEEN_SIDE("O-O-O");

	// see https://stackoverflow.com/questions/507602/how-can-i-initialise-a-static-map

	private static final @NotNull Map<@NotNull String, @NotNull Castling> notationToCastling = Arrays.stream(values())
		.collect(Collectors.toMap(Castling::getNotation, p -> p));

	/**
	 * The move of the [side] king as a part of the [type] castling
	 * kingMove[side.ordinal()][castling.ordinal()]
	 */
	private static final @NotNull Move[][] kingMove = {
		{
			// WHITE
			new Move(Square.E1, Square.G1), // O-O
			new Move(Square.E1, Square.C1), // O-O-O
		},
		{
			// BLACK
			new Move(Square.E8, Square.G8), // O-O
			new Move(Square.E8, Square.C8), // O-O-O
		}
	};

	/**
	 * The move of the [side] rook as a part of the [type] castling
	 * rookMove[side.ordinal()][castling.ordinal()]
	 */
	private static final @NotNull Move[][] rookMove = {
		{
			// WHITE
			new Move(Square.H1, Square.F1), // O-O
			new Move(Square.A1, Square.D1), // O-O-O
		},
		{
			// BLACK
			new Move(Square.H8, Square.F8), // O-O
			new Move(Square.A8, Square.D8), // O-O-O
		}
	};

	/**
	 * The two inner squares (closer to the king) between the king and the rook in [side][type] castling
	 * squares[side.ordinal()][castling.ordinal()]
	 */
	private static final @NotNull List<@NotNull List<@NotNull List<@NotNull Square>>> squares = List.of(
		List.of(
			// WHITE
			List.of(Square.F1, Square.G1), // O-O
			List.of(Square.D1, Square.C1)  // O-O-O TODO: why not also Square.B1?
		),
		List.of(
			// BLACK
			List.of(Square.F8, Square.G8), // O-O
			List.of(Square.D8, Square.C8)  // O-O-O TODO: why not also Square.B8?
		)
	);
	/**
	 * The two inner squares (bitboard) (closer to the king) between the king and the rook in [side][type] castling
	 * squaresBB[side.ordinal()][castling.ordinal()]
	 */
	private static final @NotNull List<@NotNull List<@NotNull Long>> squaresBB = squares.stream()
		.map(inner -> inner.stream().map(Bitboard::squareListToBB).collect(Collectors.toUnmodifiableList()))
		.collect(Collectors.toUnmodifiableList());

	/**
	 * The all inner squares (two or three) between the king and the rook in [side][type] castling
	 * allSquares[side.ordinal()][castling.ordinal()]
	 */
	private static final @NotNull List<@NotNull List<@NotNull List<@NotNull Square>>> allSquares = List.of(
		List.of(
			// WHITE
			List.of(Square.F1, Square.G1), // O-O
			List.of(Square.D1, Square.C1, Square.B1) // O-O-O
		),
		List.of(
			// BLACK
			List.of(Square.F8, Square.G8), // O-O
			List.of(Square.D8, Square.C8, Square.B8) // O-O-O
		)
	);
	/**
	 * The all inner squares (bitboard) (two or three) between the king and the rook in [side][type] castling
	 * allSquares[side.ordinal()][castling.ordinal()]
	 */
	private static final @NotNull List<@NotNull List<@NotNull Long>> allSquaresBB = allSquares.stream()
		.map(inner -> inner.stream().map(Bitboard::squareListToBB).collect(Collectors.toUnmodifiableList()))
		.collect(Collectors.toUnmodifiableList());

	private final @NotNull String notation;

	Castling(@NotNull String notation) {
		this.notation = notation;
	}

	/**
	 * Gets castling notation for usage in SAN
	 *
	 * @return the castling notation for usage in SAN
	 */
	public @NotNull String getNotation() {
		return notation;
	}

	/**
	 * Creates a castling from the castling notation as used in SAN
	 *
	 * @param notation the castling notation as used in SAN
	 * @return {@link Castling} or {@code null} if there is no castling for the given notation
	 * @see Castling#getNotation()
	 */
	public static @Nullable Castling fromNotation(@NotNull String notation) {
		return notationToCastling.get(notation);
	}

	/**
	 * Gets king move for this castling type and given side
	 *
	 * @param side the side
	 * @return the move of the [side] king as a part of the [this type] castling
	 */
	public @NotNull Move getKingMove(@NotNull Side side) {
		return kingMove[side.ordinal()][this.ordinal()];
	}

	/**
	 * Gets rook move for this castling type and given side
	 *
	 * @param side the side
	 * @return the move of the [side] rook as a part of the [this type] castling
	 */
	public @NotNull Move getRookMove(@NotNull Side side) {
		return rookMove[side.ordinal()][this.ordinal()];
	}

	/**
	 * Gets the two inner squares (closer to the king) between the king and the rook in [side][this type] castling
	 *
	 * @param side the side
	 * @return the two inner squares (closer to the king) between the king and the rook in [side][this type] castling
	 */
	public @NotNull List<Square> getSquares(@NotNull Side side) {
		return squares.get(side.ordinal()).get(this.ordinal());
	}

	/**
	 * Gets the two inner squares (bitboard) (closer to the king) between the king and the rook in [side][this type] castling
	 *
	 * @param side the side
	 * @return the two inner squares (bitboard) (closer to the king) between the king and the rook in [side][this type] castling
	 */
	public long getSquaresBB(@NotNull Side side) {
		return squaresBB.get(side.ordinal()).get(this.ordinal());
	}

	/**
	 * Gets the all inner squares (two or three) between the king and the rook in [side][this type] castling
	 *
	 * @param side the side
	 * @return the all inner squares (two or three) between the king and the rook in [side][this type] castling
	 */
	public @NotNull List<Square> getAllSquares(@NotNull Side side) {
		return allSquares.get(side.ordinal()).get(this.ordinal());
	}

	/**
	 * Gets the all inner squares (bitboard) (two or three) between the king and the rook in [side][this type] castling
	 *
	 * @param side the side
	 * @return the all inner squares (bitboard) (two or three) between the king and the rook in [side][this type] castling
	 */
	public long getAllSquaresBB(@NotNull Side side) {
		return allSquaresBB.get(side.ordinal()).get(this.ordinal());
	}

}
