package cz.martinendler.chess.engine.move;

import cz.martinendler.chess.engine.Castling;
import cz.martinendler.chess.engine.CastlingRight;
import cz.martinendler.chess.engine.Side;
import cz.martinendler.chess.engine.board.Rank;
import cz.martinendler.chess.engine.board.Square;
import cz.martinendler.chess.engine.pieces.Piece;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.Objects;

/**
 * A description of a possible move intention (may be illegal)
 */
public class Move {

	@NotNull
	private final Square from;
	@NotNull
	private final Square to;
	@Nullable
	private final Piece promotion;

	/**
	 * Creates a new {@link Move}
	 *
	 * @param from      the from
	 * @param to        the to
	 * @param promotion the promotion
	 */
	public Move(@NotNull Square from, @NotNull Square to, @Nullable Piece promotion) {
		this.from = from;
		this.to = to;
		this.promotion = promotion;
	}

	/**
	 * Creates a new {@link Move}
	 *
	 * @param from the from
	 * @param to   the to
	 */
	public Move(@NotNull Square from, @NotNull Square to) {
		this(from, to, null);
	}

	/**
	 * Gets from
	 *
	 * @return the from
	 */
	public @NotNull Square getFrom() {
		return from;
	}

	/**
	 * Gets to
	 *
	 * @return the to
	 */
	public @NotNull Square getTo() {
		return to;
	}

	/**
	 * Gets promotion
	 *
	 * @return the promotion
	 */
	public @Nullable Piece getPromotion() {
		return promotion;
	}

	public boolean hasPromotion() {
		return promotion != null;
	}

	/**
	 * Check if this move could lead to a pawn promotion
	 * (if it was played by the given side and a pawn was the moving piece)
	 *
	 * @param side the side
	 * @return {@code true} if this leads to a pawn promotion for the given side
	 */
	public boolean couldLeadToPromotion(@NotNull Side side) {
		return (
			(
				side.isWhite() && to.getRank() == Rank.RANK_8
			) || (
				side.isBlack() && to.getRank() == Rank.RANK_1
			)
		);
	}

	/**
	 * Checks if this move is a castling
	 *
	 * @return {@code true} if this move is a castling, {@code false} otherwise
	 */
	public boolean isCastling() {
		return this.equals(Castling.KING_SIDE.getKingMove(Side.WHITE))
			|| this.equals(Castling.QUEEN_SIDE.getKingMove(Side.WHITE))
			|| this.equals(Castling.KING_SIDE.getKingMove(Side.BLACK))
			|| this.equals(Castling.QUEEN_SIDE.getKingMove(Side.BLACK));
	}

	/**
	 * Checks if this move is a king side castling
	 *
	 * @return {@code true} if this move is a king side castling, {@code false} otherwise
	 */
	public boolean isKingSideCastling() {
		return this.equals(Castling.KING_SIDE.getKingMove(Side.WHITE))
			|| this.equals(Castling.KING_SIDE.getKingMove(Side.BLACK));
	}

	/**
	 * Checks if this move a queen side castling
	 *
	 * @return {@code true} if this is a queen side castling, {@code false} otherwise
	 */
	public boolean isQueenSideCastling() {
		return this.equals(Castling.QUEEN_SIDE.getKingMove(Side.WHITE))
			|| this.equals(Castling.QUEEN_SIDE.getKingMove(Side.BLACK));
	}

	/**
	 * Gets the corresponding castling type (if any)
	 *
	 * @return the corresponding castling type iff this move is a castling, {@code null} otherwise
	 * @see Move#isCastling()
	 * @see Move#isKingSideCastling()
	 * @see Move#isQueenSideCastling()
	 */
	public @Nullable Castling getCastling() {

		if (isKingSideCastling()) {
			return Castling.KING_SIDE;
		}

		if (isQueenSideCastling()) {
			return Castling.QUEEN_SIDE;
		}

		return null;

	}

	/**
	 * Checks if the required castling right for this move matches the given castling right
	 *
	 * @param castlingRight the castling right that the side has
	 * @return {@code true} if the required castling right for this move matches the given castling right
	 */
	public boolean isAllowedBy(@NotNull CastlingRight castlingRight) {

		Castling castling = getCastling();

		// if this is not a castling move, no castling right is required
		if (castling == null) {
			return true;
		}

		return castlingRight.allows(castling);

	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Move move = (Move) o;
		return from == move.from &&
			to == move.to &&
			promotion == move.promotion;
	}

	@Override
	public int hashCode() {
		return Objects.hash(from, to, promotion);
	}

	public @NotNull String toDebugString() {
		return MessageFormat.format(
			"{0} -> {1} P: {2}",
			from.name(), to.name(), promotion != null ? promotion.name() : "(none)"
		);
	}

}
