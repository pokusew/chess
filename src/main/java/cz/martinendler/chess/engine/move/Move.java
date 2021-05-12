package cz.martinendler.chess.engine.move;

import cz.martinendler.chess.engine.CastlingRight;
import cz.martinendler.chess.engine.Constants;
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
	 * Gets the king castle move
	 *
	 * @param side          the side
	 * @param castlingRight the castle right
	 * @return king castle move
	 */
	public static Move getKingCastleMove(Side side, CastlingRight castlingRight) {
		Move move = null;
		if (Side.WHITE.equals(side)) {
			if (CastlingRight.KING_SIDE.equals(castlingRight)) {
				move = Constants.DEFAULT_WHITE_OO;
			} else if (CastlingRight.QUEEN_SIDE.equals(castlingRight)) {
				move = Constants.DEFAULT_WHITE_OOO;
			}
		} else {
			if (CastlingRight.KING_SIDE.equals(castlingRight)) {
				move = Constants.DEFAULT_BLACK_OO;
			} else if (CastlingRight.QUEEN_SIDE.equals(castlingRight)) {
				move = Constants.DEFAULT_BLACK_OOO;
			}
		}
		return move;
	}

	/**
	 * Gets the rook castle move
	 *
	 * @param side        the side
	 * @param castleRight the castle right
	 * @return rook castle move
	 */
	public Move getRookCastleMove(Side side, CastlingRight castleRight) {
		Move move = null;
		if (Side.WHITE.equals(side)) {
			if (CastlingRight.KING_SIDE.equals(castleRight)) {
				move = Constants.DEFAULT_WHITE_ROOK_OO;
			} else if (CastlingRight.QUEEN_SIDE.equals(castleRight)) {
				move = Constants.DEFAULT_WHITE_ROOK_OOO;
			}
		} else {
			if (CastlingRight.KING_SIDE.equals(castleRight)) {
				move = Constants.DEFAULT_BLACK_ROOK_OO;
			} else if (CastlingRight.QUEEN_SIDE.equals(castleRight)) {
				move = Constants.DEFAULT_BLACK_ROOK_OOO;
			}
		}
		return move;
	}

	/**
	 * Checks if this move a castle move
	 *
	 * @return {@code true} if this is a castle move, {@code false} otherwise
	 */
	public boolean isCastleMove() {
		return this.equals(Constants.DEFAULT_WHITE_OO)
			|| this.equals(Constants.DEFAULT_WHITE_OOO)
			|| this.equals(Constants.DEFAULT_BLACK_OO)
			|| this.equals(Constants.DEFAULT_BLACK_OOO);
	}

	/**
	 * Checks if this move a king side castle move
	 *
	 * @return {@code true} if this is a king side castle move, {@code false} otherwise
	 */
	public boolean isKingSideCastle() {
		return this.equals(Constants.DEFAULT_WHITE_OO)
			|| this.equals(Constants.DEFAULT_BLACK_OO);
	}

	/**
	 * Checks if this move a queen side castle move
	 *
	 * @return {@code true} if this is a queen side castle move, {@code false} otherwise
	 */
	public boolean isQueenSideCastle() {
		return this.equals(Constants.DEFAULT_WHITE_OOO)
			|| this.equals(Constants.DEFAULT_BLACK_OOO);
	}

	/**
	 * Checks if the required castle right for this move matches the given castle right
	 *
	 * @param castleRight the castle right
	 * @return {@code true} if the required castle right for this move matches the given castle right
	 */
	public boolean hasCastleRight(final CastlingRight castleRight) {

		// if this is not a castle move, no castle right is required
		if (!isCastleMove()) {
			return true;
		}

		return (CastlingRight.KING_AND_QUEEN_SIDE.equals(castleRight))
			|| (isKingSideCastle() && CastlingRight.KING_SIDE.equals(castleRight))
			|| (isQueenSideCastle() && CastlingRight.QUEEN_SIDE.equals(castleRight));

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
