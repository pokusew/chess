package cz.martinendler.chess.engine.move;

import cz.martinendler.chess.engine.CastlingRight;
import cz.martinendler.chess.engine.Constants;
import cz.martinendler.chess.engine.Side;
import cz.martinendler.chess.engine.board.Square;
import cz.martinendler.chess.engine.pieces.Piece;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * TODO
 */
public class Move {

	private final Square from;
	private final Square to;
	private final Piece promotion;

	/**
	 * Creates a new {@link Move}
	 *
	 * @param from      the from
	 * @param to        the to
	 * @param promotion the promotion
	 */
	public Move(@NotNull Square from, @NotNull Square to, @NotNull Piece promotion) {
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
		this(from, to, Piece.NONE);
	}

	/**
	 * Gets from
	 *
	 * @return the from
	 */
	public Square getFrom() {
		return from;
	}

	/**
	 * Gets to
	 *
	 * @return the to
	 */
	public Square getTo() {
		return to;
	}

	/**
	 * Gets promotion
	 *
	 * @return the promotion
	 */
	public Piece getPromotion() {
		return promotion;
	}

	/**
	 * Gets the king castle move
	 *
	 * @param side        the side
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
	 * Is castle move boolean.
	 *
	 * @return true if move is a castle one
	 */
	public boolean isCastleMove() {
		return this.equals(Constants.DEFAULT_WHITE_OO)
			|| this.equals(Constants.DEFAULT_WHITE_OOO)
			|| this.equals(Constants.DEFAULT_BLACK_OO)
			|| this.equals(Constants.DEFAULT_BLACK_OOO);
	}

	/**
	 * is King side castle
	 *
	 * @return boolean
	 */
	public boolean isKingSideCastle() {
		return this.equals(Constants.DEFAULT_WHITE_OO)
			|| this.equals(Constants.DEFAULT_BLACK_OO);
	}

	/**
	 * is queen side castle
	 *
	 * @return boolean
	 */
	public boolean isQueenSideCastle() {
		return this.equals(Constants.DEFAULT_WHITE_OOO)
			|| this.equals(Constants.DEFAULT_BLACK_OOO);
	}

	/**
	 * If castle move is a valid one
	 *
	 * @param castleRight the castle right
	 * @return boolean
	 */
	public boolean hasCastleRight(final CastlingRight castleRight) {
		return (CastlingRight.KING_AND_QUEEN_SIDE.equals(castleRight))
			|| (this.equals(Constants.DEFAULT_WHITE_OO) && CastlingRight.KING_SIDE.equals(castleRight))
			|| (this.equals(Constants.DEFAULT_BLACK_OO) && CastlingRight.KING_SIDE.equals(castleRight))
			|| (this.equals(Constants.DEFAULT_WHITE_OOO) && CastlingRight.QUEEN_SIDE.equals(castleRight))
			|| (this.equals(Constants.DEFAULT_BLACK_OOO) && CastlingRight.QUEEN_SIDE.equals(castleRight));
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

}
