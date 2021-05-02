package cz.martinendler.chess.engine.move;

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
