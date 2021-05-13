package cz.martinendler.chess.engine.board;

import cz.martinendler.chess.engine.CastlingRight;
import cz.martinendler.chess.engine.Constants;
import cz.martinendler.chess.engine.Game;
import cz.martinendler.chess.engine.Side;
import cz.martinendler.chess.engine.move.Move;
import cz.martinendler.chess.engine.pieces.Piece;
import cz.martinendler.chess.engine.pieces.PieceType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.List;

/**
 * A chessboard within a chess {@link Game}
 */
public class Board {

	private static final Logger log = LoggerFactory.getLogger(Board.class);

	// TODO: Should we use EnumMap or plain arrays
	//       for bitboardOfSide, bitboardOfPiece and squareToPiece?
	//       using EnumMap:
	//         pros: maybe cleaner, easier to use (type checks)
	//         cons: probably slower (although EnumMap is also backed by array,
	//               the keys are validated thus it probably worsens access times)
	//         cons: cannot be use with Arrays.fill(arr, val) (see Board.clear())

	private final @NotNull long[] bitboardOfSide;
	private final @NotNull long[] bitboardOfPiece;
	private final @Nullable Piece[] squareToPiece;

	private final @NotNull EnumMap<Side, CastlingRight> castlingRights;

	private @NotNull Side sideToMove;

	/**
	 * The square of the pawn that could be captured via en passant
	 * if the the other side's pawn moves to the {@link Board#enPassant} square
	 * in the next immediate move.
	 */
	private @Nullable Square enPassantTarget;
	/**
	 * If the side-to-move's pawn moves to this square then the other side's pawn
	 * that is on the {@link Board#enPassantTarget} square is captured via en passant.
	 */
	private @Nullable Square enPassant;

	/**
	 * Move counter counts full-moves (1 full-move = WHITE's move + BLACK's move)
	 */
	private int moveCounter;
	/**
	 * Half-move counter takes care of enforcing the fifty-move rule
	 *
	 * @see <a href="https://www.chessprogramming.org/Halfmove_Clock">Halfmove Clock on CPW</a>
	 */
	private int halfMoveCounter;

	public Board() {

		bitboardOfSide = new long[Side.values().length];
		bitboardOfPiece = new long[Piece.values().length];
		squareToPiece = new Piece[Square.values().length];
		castlingRights = new EnumMap<>(Side.class);
		castlingRights.put(Side.WHITE, CastlingRight.KING_AND_QUEEN_SIDE);
		castlingRights.put(Side.BLACK, CastlingRight.KING_AND_QUEEN_SIDE);

		sideToMove = Side.WHITE;
		enPassantTarget = null;
		enPassant = null;

		moveCounter = 0;
		halfMoveCounter = 0;

	}

	/**
	 * Gets the global bitboard
	 * <p>
	 * 0b represents an empty square, 1b represents an occupied square (by either a white or a black piece)
	 *
	 * @return the global bitboard, 64-bits-long vector
	 */
	public long getBitboard() {
		return bitboardOfSide[Side.WHITE.ordinal()] | bitboardOfSide[Side.BLACK.ordinal()];
	}

	/**
	 * Gets the bitboard for the given {@code piece}
	 * <p>
	 * 0b represents a square that is NOT occupied by the given {@code piece},
	 * while 1b represents a square that is occupied by the given {@code piece}
	 *
	 * @param piece the piece
	 * @return the bitboard of the given {@code piece}, 64-bits-long vector
	 */
	public long getBitboard(@NotNull Piece piece) {
		return bitboardOfPiece[piece.ordinal()];
	}

	/**
	 * Gets the bitboard for the given {@code side}
	 * <p>
	 * 0b represents a square that is NOT occupied by any of the given {@code side}'s pieces,
	 * while 1b represents a square that is occupied by any of the given {@code side}'s pieces.
	 *
	 * @param side the side
	 * @return the bitboard of the given {@code side}, 64-bits-long vector
	 */
	public long getBitboard(@NotNull Side side) {
		return bitboardOfSide[side.ordinal()];
	}

	/**
	 * Gets the piece on the given square
	 *
	 * @param square the square
	 * @return the piece or {@code null} if there is no piece on the given square
	 */
	public @Nullable Piece getPiece(@NotNull Square square) {
		return squareToPiece[square.ordinal()];
	}

	/**
	 * Checks if the given piece is on any of the given squares
	 *
	 * @param piece    the piece
	 * @param location the location
	 * @return boolean
	 */
	public boolean hasPiece(@NotNull Piece piece, @NotNull Square[] location) {
		for (Square sq : location) {
			if ((getBitboard(piece) & sq.getBitboard()) != 0L) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets side to move
	 *
	 * @return the side to move
	 */
	public @NotNull Side getSideToMove() {
		return sideToMove;
	}

	/**
	 * Gets enPassantTarget square
	 * <p>
	 * The square of the pawn that could be captured via en passant
	 * if the the other side's pawn moves to the {@link Board#enPassant} square
	 * in the next immediate move.
	 *
	 * @return the enPassantTarget square
	 */
	public @Nullable Square getEnPassantTarget() {
		return enPassantTarget;
	}

	/**
	 * Gets enPassant square
	 * <p>
	 * If the side-to-move's pawn moves to this square then the other side's pawn
	 * that is on the {@link Board#enPassantTarget} square is captured via en passant.
	 *
	 * @return the enPassant square
	 */
	public @Nullable Square getEnPassant() {
		return enPassant;
	}

	/**
	 * Gets castling right of the given side
	 *
	 * @param side the side
	 * @return the castleRight
	 */
	public @NotNull CastlingRight getCastleRight(@NotNull Side side) {
		return castlingRights.get(side);
	}

	/**
	 * Gets the current value of the full-move counter
	 * <p>
	 * Move counter counts full-moves (1 full-move = WHITE's move + BLACK's move).
	 *
	 * @return non-negative integer
	 */
	public int getMoveCounter() {
		return moveCounter;
	}

	/**
	 * Gets the current value of the half-move counter
	 * <p>
	 * Half-move counter takes care of enforcing the fifty-move rule
	 *
	 * @return non-negative integer
	 * @see <a href="https://www.chessprogramming.org/Halfmove_Clock">Halfmove Clock on CPW</a>
	 */
	public int getHalfMoveCounter() {
		return halfMoveCounter;
	}

	// TODO: Javadoc
	private static @Nullable Square findEnPassantTarget(@Nullable Square sq, @NotNull Side side) {

		if (sq == null) {
			return null;
		}

		return side.isWhite()
			? Square.encode(Rank.RANK_5, sq.getFile())
			: Square.encode(Rank.RANK_4, sq.getFile());

	}

	/**
	 * Computes en passant square from the current square of a pawn that advanced two squares forward
	 *
	 * @param sq   the current square of a pawn that advanced two squares forward
	 * @param side the side of the pawn
	 * @return the destination square as if the pawn advanced only one square forward
	 */
	public static @Nullable Square findEnPassant(@Nullable Square sq, @NotNull Side side) {

		if (sq == null) {
			return null;
		}

		return side.isWhite()
			? Square.encode(Rank.RANK_3, sq.getFile())
			: Square.encode(Rank.RANK_6, sq.getFile());

	}

	/**
	 * Adds a piece into a given square
	 *
	 * @param piece the piece
	 * @param sq    the square
	 */
	public void addPiece(@NotNull Piece piece, @NotNull Square sq) {
		bitboardOfPiece[piece.ordinal()] |= sq.getBitboard();
		bitboardOfSide[piece.getPieceSide().ordinal()] |= sq.getBitboard();
		squareToPiece[sq.ordinal()] = piece;
	}

	/**
	 * Removes a piece from a given square
	 *
	 * @param piece the piece
	 * @param sq    the square
	 */
	public void removePiece(@NotNull Piece piece, @NotNull Square sq) {
		bitboardOfPiece[piece.ordinal()] ^= sq.getBitboard();
		bitboardOfSide[piece.getPieceSide().ordinal()] ^= sq.getBitboard();
		squareToPiece[sq.ordinal()] = null;
	}

	/**
	 * Moves a piece as described by the given move
	 *
	 * @param move the move
	 * @return a captured piece if any, otherwise {@code null}
	 */
	protected @Nullable Piece movePiece(@NotNull Move move) {
		return movePiece(move.getFrom(), move.getTo(), move.getPromotion());
	}

	/**
	 * Moves a piece as described the given arguments
	 *
	 * @param from      the from
	 * @param to        the to
	 * @param promotion the promotion
	 * @return a captured piece if any, otherwise {@code null}
	 */
	protected Piece movePiece(@NotNull Square from, @NotNull Square to, @Nullable Piece promotion) {

		Piece movingPiece = getPiece(from);

		if (movingPiece == null) {
			throw new IllegalArgumentException("There is no piece on the from square.");
		}

		Piece capturedPiece = getPiece(to);

		removePiece(movingPiece, from);

		// captured piece MUST be explicitly removed before adding any piece to the destination (to) square
		if (capturedPiece != null) {
			removePiece(capturedPiece, to);
		}

		// if there is a promotion, add the promoted piece to the destination (to) square
		// otherwise add the moving piece to the destination (to) square
		addPiece(promotion != null ? promotion : movingPiece, to);

		// handle en passant
		if (
			// TODO: test
			// TODO: Is this really correct? (where is set the enPassantTarget)
			movingPiece.isOfType(PieceType.PAWN) // moving piece is a pawn
				&& getEnPassantTarget() != null // en passant target is NOT null
				&& from.getFile() != to.getFile() // from and to files (columns) are different
				&& capturedPiece == null // NO piece was captured on the destination (to) square
		) {
			log.info("movePiece: did a en passant capture");
			// set the captured piece that was captured during en passant
			capturedPiece = getPiece(getEnPassantTarget());
		}

		return capturedPiece;

	}

	/**
	 * Checks if the given side's pieces can attack the given square
	 *
	 * @param square the square
	 * @param side   the side
	 * @return {@code 0L} iff the square is NOT attacked by the given side
	 */
	public long squareAttackedBy(@NotNull Square square, @NotNull Side side) {
		return squareAttackedBy(square, side, getBitboard());
	}

	/**
	 * Checks if the given side's pieces can attack the given square
	 *
	 * @param square   the square
	 * @param side     the side
	 * @param occupied occupied squares (all pieces on the board)
	 * @return {@code 0L} iff the square is NOT attacked by the given side
	 */
	public long squareAttackedBy(@NotNull Square square, @NotNull Side side, long occupied) {

		long result = 0L;

		result |=
			// this gets the required positions of the pawns
			// that cloud attack the given square
			// note: the reason for using side.flip() is that
			//       the pawn attacks are not symmetrical (unlike all other piece types attacks)
			Bitboard.getPawnAttacks(side.flip(), square)
				// filter by the really available pawns
				& getBitboard(Piece.make(side, PieceType.PAWN))
				// TODO: Why we use occupied here?
				& occupied;

		// knights
		result |=
			// this gets the required positions of the knights
			// that cloud attack the given square
			// TODO: Why we pass occupied here?
			Bitboard.getKnightAttacks(square, occupied)
				// filter by the really available knights
				& getBitboard(Piece.make(side, PieceType.KNIGHT));

		// bishops + queens bishops-like attacks
		result |=
			// this gets the required positions of the bishops + queens
			// that cloud attack (bishops-like) the given square
			Bitboard.getBishopAttacks(occupied, square)
				// filter by the really available bishops + queens
				& (getBitboard(Piece.make(side, PieceType.BISHOP)) | getBitboard(Piece.make(side, PieceType.QUEEN)));

		// rooks + queens rooks-like attacks
		result |=
			// this gets the required positions of the rooks + queens
			// that cloud attack (rooks-like) the given square
			Bitboard.getRookAttacks(occupied, square)
				// filter by the really available rooks + queens
				& (getBitboard(Piece.make(side, PieceType.ROOK)) | getBitboard(Piece.make(side, PieceType.QUEEN)));

		result |=
			// this gets the required positions of the king
			// that cloud attack the given square
			// TODO: Why we pass occupied here?
			Bitboard.getKingAttacks(square, occupied)
				// filter by the really available king
				& getBitboard(Piece.make(side, PieceType.KING));

		return result;

	}

	/**
	 * Checks if the given side's pieces of the given type can attack the given square
	 *
	 * @param square the square
	 * @param side   the side
	 * @param type   the type
	 * @return {@code 0L} iff the square is NOT attacked by the given side
	 */
	public long squareAttackedByPieceType(@NotNull Square square, @NotNull Side side, @NotNull PieceType type) {

		long occupied = getBitboard();

		return switch (type) {
			case PAWN -> (
				Bitboard.getPawnAttacks(side.flip(), square) & getBitboard(Piece.make(side, PieceType.PAWN))
			);
			case KNIGHT -> (
				// TODO: Why we pass occupied here?
				Bitboard.getKnightAttacks(square, occupied) & getBitboard(Piece.make(side, PieceType.KNIGHT))
			);
			case BISHOP -> (
				Bitboard.getBishopAttacks(occupied, square) & getBitboard(Piece.make(side, PieceType.BISHOP))
			);
			case ROOK -> (
				Bitboard.getRookAttacks(occupied, square) & getBitboard(Piece.make(side, PieceType.ROOK))
			);
			case QUEEN -> (
				Bitboard.getQueenAttacks(occupied, square) & getBitboard(Piece.make(side, PieceType.QUEEN))
			);
			case KING -> (
				// TODO: Why we pass occupied here?
				Bitboard.getKingAttacks(square, occupied) & getBitboard(Piece.make(side, PieceType.KING))
			);
		};

	}

	/**
	 * Checks if the given side's pieces can attack any of the given squares
	 *
	 * @param squares the squares
	 * @param side    the side
	 * @return {@code true} if any of the given squares is attacked by the given side, {@code false} otherwise
	 */
	public boolean isSquareAttackedBy(@NotNull List<Square> squares, @NotNull Side side) {
		for (Square sq : squares) {
			if (squareAttackedBy(sq, side) != 0L) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the square where the given side's king currently is
	 *
	 * @param side the side
	 * @return the given side's king square
	 */
	public @Nullable Square getKingSquare(@NotNull Side side) {

		long piece = getBitboard(Piece.make(side, PieceType.KING));

		if (piece != 0L) {
			int square = Bitboard.bitScanForward(piece);
			return Square.fromIndex(square);
		}

		return null;

	}

	/**
	 * Checks if the side-to-move's king is attacked
	 *
	 * @return boolean {@code true} when the side-to-move's king is attacked, false otherwise
	 */
	public boolean isKingAttacked() {

		Square kingSquare = getKingSquare(getSideToMove());

		if (kingSquare == null) {
			// side-to-move's king is not on the board
			// should we rather throw an exception
			return false;
		}

		return squareAttackedBy(kingSquare, getSideToMove().flip()) != 0L;

	}

	/**
	 * Verifies if the move to be played leaves the resulting board in a legal position
	 *
	 * @param move           the move
	 * @param fullValidation performs a full validation on the move, not only if it leaves own king on check, but also if
	 *                       castling is legal, based on attacked pieces and squares, if board is in a consistent state:
	 *                       e.g.: Occupancy of source square by a piece that belongs to playing side, etc.
	 * @return {@code true} iff the move is legal
	 */
	public boolean isMoveLegal(final @NotNull Move move, final boolean fullValidation) {

		final Piece fromPiece = getPiece(move.getFrom());

		// there is no piece on the from square
		if (fromPiece == null) {
			log.debug(
				"isMoveLegal({}): false (there is no piece on the from square)",
				move.toDebugString()
			);
			return false;
		}

		final Side side = getSideToMove();
		final Side otherSide = side.flip();
		final PieceType fromType = fromPiece.getPieceType();
		final Piece capturedPiece = getPiece(move.getTo());

		if (fullValidation) {

			// player tries to move the opponent's piece
			if (side != fromPiece.getPieceSide()) {
				log.debug(
					"isMoveLegal({}): false (player tries to move the opponent's piece)",
					move.toDebugString()
				);
				return false;
			}

			// player cannot capture their own pieces
			if (capturedPiece != null && fromPiece.getPieceSide() == capturedPiece.getPieceSide()) {
				log.debug(
					"isMoveLegal({}): false (player cannot capture their own pieces)",
					move.toDebugString()
				);
				return false;
			}

			boolean pawnPromoting = fromPiece.isOfType(PieceType.PAWN) && move.couldLeadToPromotion(side);

			// pawn should be promoted but the promotion NOT set on the move
			// or pawn should NOT be promoted but the promotion is set on the move
			if (move.hasPromotion() != pawnPromoting) {
				log.debug(
					"isMoveLegal({}): false (move.hasPromotion() != pawnPromoting)",
					move.toDebugString()
				);
				return false;
			}

			// if players moves with their king
			// we need to check if this move is a (valid) attempt for a castling
			if (fromType == PieceType.KING) {

				// player does not have castle right for the attempted castle move
				if (!move.hasCastleRight(getCastleRight(side))) {
					return false;
				}

				if (move.isKingSideCastle()) {

					// some squares between the king and the rook are not empty
					if (!Bitboard.areSquaresFree(getBitboard(), Constants.getOOAllSquaresBB(side))) {
						return false;
					}

					// if no squares (required for the castling) are attacked
					// then this move is valid
					// TODO: Shouldn't we check all squares?
					return !isSquareAttackedBy(Constants.getOOSquares(side), side.flip());

				}

				if (move.isQueenSideCastle()) {

					// some squares between the king and the rook are not empty
					if (!Bitboard.areSquaresFree(getBitboard(), Constants.getOOOAllSquaresBB(side))) {
						return false;
					}

					// if no squares (required for the castling) are attacked
					// then this move is valid
					// TODO: Shouldn't we check all squares?
					return !isSquareAttackedBy(Constants.getOOOSquares(side), side.flip());

				}

			}

		}

		// king cannot be moved to a square that is currently under attack by the opposite side
		if (fromType == PieceType.KING && squareAttackedBy(move.getTo(), otherSide) != 0L) {
			log.debug(
				"isMoveLegal({}): false (king cannot be moved to a square"
					+ "that is currently under attack by the opposite side)",
				move.toDebugString()
			);
			return false;
		}

		// the code below checks that the move would not leave the side's king in check

		// the square of the side's king after this move
		Square kingSq = fromType == PieceType.KING ? move.getTo() : getKingSquare(side);

		if (kingSq == null) {
			throw new IllegalStateException(side.name() + "'s king not on board!");
		}

		long moveTo = move.getTo().getBitboard();
		long moveFrom = move.getFrom().getBitboard();

		// en passant capture square
		long ep = (
			getEnPassantTarget() != null
				&& move.getTo() == getEnPassant()
				&& fromType == PieceType.PAWN
		) ? getEnPassantTarget().getBitboard() : 0L;

		// bitboard of all pieces after the move considering also en passant capture
		long allPieces = (getBitboard() ^ moveFrom ^ ep) | moveTo;

		long bishopsAndQueens = (
			(
				getBitboard(Piece.make(otherSide, PieceType.BISHOP))
					| getBitboard(Piece.make(otherSide, PieceType.QUEEN))
			) & ~moveTo
		);

		// after this move, the king would be attacked by some of the other side's bishops and/or queens (diagonals)
		if (bishopsAndQueens != 0L && (Bitboard.getBishopAttacks(allPieces, kingSq) & bishopsAndQueens) != 0L) {
			log.debug(
				"isMoveLegal({}): false (after this move, the king would be attacked by some" +
					"of the other side's bishops and/or queens (diagonals)",
				move.toDebugString()
			);
			return false;
		}

		long rooksAndQueens = (
			(
				getBitboard(Piece.make(otherSide, PieceType.ROOK)) |
					getBitboard(Piece.make(otherSide, PieceType.QUEEN))
			) & ~moveTo
		);

		// after this move, the king would be attacked by some of the other side's bishops and/or queens (rank or files)
		if (rooksAndQueens != 0L && (Bitboard.getRookAttacks(allPieces, kingSq) & rooksAndQueens) != 0L) {
			log.debug(
				"isMoveLegal({}): false (after this move, the king would be attacked by some" +
					"of the other side's bishops and/or queens (rank or files)",
				move.toDebugString()
			);
			return false;
		}

		long knights = getBitboard(Piece.make(otherSide, PieceType.KNIGHT)) & ~moveTo;

		// after this move, the king would be attacked by some of the other side's knights
		if (knights != 0L && (Bitboard.getKnightAttacks(kingSq, allPieces) & knights) != 0L) {
			log.debug(
				"isMoveLegal({}): false (after this move, the king would be attacked by some" +
					"of the other side's knights",
				move.toDebugString()
			);
			return false;
		}

		long pawns = getBitboard(Piece.make(otherSide, PieceType.PAWN)) & ~moveTo & ~ep;

		// after this move, the king would be attacked by some of the other side's pawns
		if (pawns != 0L && (Bitboard.getPawnAttacks(side, kingSq) & pawns) == 0L) {
			log.debug(
				"isMoveLegal({}): false (after this move, the king would be attacked by some" +
					"of the other side's pawns",
				move.toDebugString()
			);
			return false;
		}

		// the move is legal
		return true;

	}

	/**
	 * Executes the given move on this board
	 *
	 * @param move           the move
	 * @param fullValidation perform full validation
	 * @return true if operation was successful
	 */
	public boolean doMove(final Move move, final boolean fullValidation) {

		if (!isMoveLegal(move, fullValidation)) {
			return false;
		}

		Piece movingPiece = getPiece(move.getFrom());

		if (movingPiece == null) {
			// that should never happen
			throw new IllegalStateException("movingPiece in doMove is null after isMoveLegal returned true");
		}

		Side side = getSideToMove();

		// castling rules
		if (movingPiece.isOfType(PieceType.KING)) {

			if (move.isCastleMove()) {
				if (move.hasCastleRight(getCastleRight(side))) {
					CastlingRight c = move.isKingSideCastle() ? CastlingRight.KING_SIDE : CastlingRight.QUEEN_SIDE;
					Move rookMove = Constants.getRookCastleMove(side, c);
					movePiece(rookMove);
				} else {
					return false;
				}
			}

			// after the king's move the player looses castling right (if they has still any)
			if (getCastleRight(side) != CastlingRight.NONE) {
				castlingRights.put(side, CastlingRight.NONE);
			}

		} else if (movingPiece.isOfType(PieceType.ROOK) && CastlingRight.NONE != getCastleRight(side)) {

			final Move oo = Constants.getRookoo(side);
			final Move ooo = Constants.getRookooo(side);

			if (move.getFrom() == oo.getFrom()) {
				// update castling right (kingside no longer possible)
				if (CastlingRight.KING_AND_QUEEN_SIDE == getCastleRight(side)) {
					castlingRights.put(side, CastlingRight.QUEEN_SIDE);
				} else if (CastlingRight.KING_SIDE == getCastleRight(side)) {
					castlingRights.put(side, CastlingRight.NONE);
				}
			} else if (move.getFrom() == ooo.getFrom()) {
				// update castling right (queenside no longer possible)
				if (CastlingRight.KING_AND_QUEEN_SIDE == getCastleRight(side)) {
					castlingRights.put(side, CastlingRight.KING_SIDE);
				} else if (CastlingRight.QUEEN_SIDE == getCastleRight(side)) {
					castlingRights.put(side, CastlingRight.NONE);
				}
			}

		}

		Piece capturedPiece = movePiece(move);

		// if the side captured the other side's rook
		// it might be needed to update the other side's castling right
		if (capturedPiece != null && capturedPiece.isOfType(PieceType.ROOK)) {

			final Move oo = Constants.getRookoo(side.flip());
			final Move ooo = Constants.getRookooo(side.flip());

			if (move.getTo() == oo.getFrom()) {
				// update the other side's castling right (kingside no longer possible)
				if (CastlingRight.KING_AND_QUEEN_SIDE == getCastleRight(side.flip())) {
					castlingRights.put(side.flip(), CastlingRight.QUEEN_SIDE);
				} else if (CastlingRight.KING_SIDE == getCastleRight(side.flip())) {
					castlingRights.put(side.flip(), CastlingRight.NONE);
				}
			} else if (move.getTo() == ooo.getFrom()) {
				// update the other side's castling right (queenside no longer possible)
				if (CastlingRight.KING_AND_QUEEN_SIDE == getCastleRight(side.flip())) {
					castlingRights.put(side.flip(), CastlingRight.KING_SIDE);
				} else if (CastlingRight.QUEEN_SIDE == getCastleRight(side.flip())) {
					castlingRights.put(side.flip(), CastlingRight.NONE);
				}
			}

		}

		if (capturedPiece == null) {
			// half-move counter is incremented if there is no capture
			halfMoveCounter++;
		} else {
			// half-move counter is reset after captures
			halfMoveCounter = 0;
		}

		// reset en passant
		enPassantTarget = null;
		enPassant = null;

		if (movingPiece.isOfType(PieceType.PAWN)) {

			// compute information about possible en passant in the next immediate move
			// if the pawn advanced two squares
			if (Math.abs(move.getTo().getRank().ordinal() - move.getFrom().getRank().ordinal()) == 2) {
				Piece otherPawn = Piece.make(side.flip(), PieceType.PAWN);
				// en passant square would be the destination (to) square if the pawn advanced only one square
				enPassant = findEnPassant(move.getTo(), side);
				if (
					// if the opposite side has any pawns on the side-adjacent (side-neighbour) squares
					hasPiece(otherPawn, move.getTo().getSideSquares())
						// TODO: What does verifyNotPinnedPiece check?
						&& verifyNotPinnedPiece(side, getEnPassant(), move.getTo())
				) {
					enPassantTarget = move.getTo();
				}
			}

			// half-move counter is reset after pawn moves
			halfMoveCounter = 0;

		}

		if (side == Side.BLACK) {
			// full-move completed
			moveCounter++;
		}

		sideToMove = side.flip();

		return true;

	}

	public void clear() {
		// TODO
		// setSideToMove(Side.WHITE);
		// setEnPassantTarget(Square.NONE);
		// setEnPassant(Square.NONE);
		// setMoveCounter(0);
		// setHalfMoveCounter(0);
		// Arrays.fill(bitboard, 0L);
		// Arrays.fill(bbSide, 0L);
		// Arrays.fill(occupation, Piece.NONE);
	}

	// TODO
	private boolean verifyNotPinnedPiece(@NotNull Side side, @NotNull Square enPassant, @NotNull Square target) {

		long pawns = Bitboard.getPawnAttacks(side, enPassant) & getBitboard(Piece.make(side.flip(), PieceType.PAWN));

		return pawns != 0L && verifyAllPins(pawns, side, enPassant, target);

	}

	// TODO
	private boolean verifyAllPins(long pawns, Side side, Square enPassant, Square target) {

		long onePawn = Bitboard.extractLSB(pawns);
		long otherPawn = pawns ^ onePawn;

		if (onePawn != 0L && verifyKingIsNotAttackedWithoutPin(side, enPassant, target, onePawn)) {
			return true;
		}

		return verifyKingIsNotAttackedWithoutPin(side, enPassant, target, otherPawn);

	}

	// TODO
	private boolean verifyKingIsNotAttackedWithoutPin(Side side, Square enPassant, Square target, long pawns) {
		return squareAttackedBy(getKingSquare(side.flip()), side, removePieces(enPassant, target, pawns)) == 0L;
	}

	// TODO
	private long removePieces(Square enPassant, Square target, long pieces) {
		return (getBitboard() ^ pieces ^ target.getBitboard()) | enPassant.getBitboard();
	}

}
