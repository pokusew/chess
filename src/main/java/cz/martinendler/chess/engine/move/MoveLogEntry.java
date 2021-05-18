package cz.martinendler.chess.engine.move;

import cz.martinendler.chess.engine.Side;
import cz.martinendler.chess.engine.board.Board;
import cz.martinendler.chess.engine.board.Square;
import cz.martinendler.chess.engine.pieces.Piece;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A description of legal move with full state information
 */
public class MoveLogEntry {

	/**
	 * The board state before the {@link MoveLogEntry#move} was executed
	 * so that the move can be easily undone
	 * (Although that could be done more efficiently than storing the whole board state.).
	 */
	private final @NotNull Board board;

	/**
	 * The side that played the {@link MoveLogEntry#move}
	 */
	private final @NotNull Side side;

	/**
	 * The move that was played by the {@link MoveLogEntry#side}
	 */
	private final @NotNull Move move;

	/**
	 * The piece that moved during the move
	 */
	private final @NotNull Piece movingPiece;

	/**
	 * The piece that was captured during the move
	 */
	private final @Nullable Piece capturedPiece;

	/**
	 * The square where the {@link MoveLogEntry#capturedPiece} was captured.
	 * <p>
	 * Must hold:
	 * {@code capturedSquare != null && !enPassantMove && capturedSquare != move.getTo()}
	 */
	private final @Nullable Square capturedSquare;

	/**
	 * The {@link MoveLogEntry#move} was en passant move
	 */
	private final boolean enPassantMove;

	public @NotNull Board getBoard() {
		return board;
	}

	public @NotNull Side getSide() {
		return side;
	}

	public @NotNull Move getMove() {
		return move;
	}

	public @NotNull Piece getMovingPiece() {
		return movingPiece;
	}

	public @Nullable Piece getCapturedPiece() {
		return capturedPiece;
	}

	public @Nullable Square getCapturedSquare() {
		return capturedSquare;
	}

	public boolean isEnPassantMove() {
		return enPassantMove;
	}

	public MoveLogEntry(
		@NotNull Board board,
		@NotNull Side side,
		@NotNull Move move,
		@NotNull Piece movingPiece,
		@Nullable Piece capturedPiece,
		@Nullable Square capturedSquare,
		boolean enPassantMove
	) {
		this.board = board;
		this.side = side;
		this.move = move;
		this.movingPiece = movingPiece;
		this.capturedPiece = capturedPiece;
		this.capturedSquare = capturedSquare;
		this.enPassantMove = enPassantMove;
	}

	public static class MoveLogEntryBuilder {

		private @Nullable Board board;
		private @Nullable Side side;
		private @Nullable Move move;
		private @Nullable Piece movingPiece;
		private @Nullable Piece capturedPiece;
		private @Nullable Square capturedSquare;
		private boolean enPassantMove;

		public MoveLogEntryBuilder() {

		}

		public @Nullable Board getBoard() {
			return board;
		}

		public MoveLogEntryBuilder setBoard(@Nullable Board board) {
			this.board = board;
			return this;
		}

		public @Nullable Side getSide() {
			return side;
		}

		public MoveLogEntryBuilder setSide(@Nullable Side side) {
			this.side = side;
			return this;
		}

		public @Nullable Move getMove() {
			return move;
		}

		public MoveLogEntryBuilder setMove(@Nullable Move move) {
			this.move = move;
			return this;
		}

		public @Nullable Piece getMovingPiece() {
			return movingPiece;
		}

		public MoveLogEntryBuilder setMovingPiece(@Nullable Piece movingPiece) {
			this.movingPiece = movingPiece;
			return this;
		}

		public @Nullable Piece getCapturedPiece() {
			return capturedPiece;
		}

		public MoveLogEntryBuilder setCapturedPiece(@Nullable Piece capturedPiece) {
			this.capturedPiece = capturedPiece;
			return this;
		}

		public @Nullable Square getCapturedSquare() {
			return capturedSquare;
		}

		public MoveLogEntryBuilder setCapturedSquare(@Nullable Square capturedSquare) {
			this.capturedSquare = capturedSquare;
			return this;
		}

		public boolean isEnPassantMove() {
			return enPassantMove;
		}

		public MoveLogEntryBuilder setEnPassantMove(boolean enPassantMove) {
			this.enPassantMove = enPassantMove;
			return this;
		}

		public MoveLogEntry build() throws IllegalStateException {

			if (board == null) {
				throw new IllegalStateException("board must not be null");
			}

			if (side == null) {
				throw new IllegalStateException("board must not be null");
			}

			if (move == null) {
				throw new IllegalStateException("board must not be null");
			}

			if (movingPiece == null) {
				throw new IllegalStateException("board must not be null");
			}

			if (capturedSquare != null && !enPassantMove && capturedSquare != move.getTo()) {
				throw new IllegalStateException(
					"capturedSquare != null && !enPassantMove && capturedSquare != move.getTo()"
				);
			}

			return new MoveLogEntry(
				board,
				side,
				move,
				movingPiece,
				capturedPiece,
				capturedSquare,
				enPassantMove
			);

		}

	}

}
