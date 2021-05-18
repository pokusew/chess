package cz.martinendler.chess.engine.board;

import cz.martinendler.chess.engine.Castling;
import cz.martinendler.chess.engine.CastlingRight;
import cz.martinendler.chess.engine.Game;
import cz.martinendler.chess.engine.Side;
import cz.martinendler.chess.engine.move.Move;
import cz.martinendler.chess.engine.move.MoveGenerator;
import cz.martinendler.chess.engine.pieces.Piece;
import cz.martinendler.chess.engine.pieces.PieceType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

/**
 * A chessboard within a chess {@link Game}
 */
public class Board {

	private static final Logger log = LoggerFactory.getLogger(Board.class);

	public static final String STANDARD_STARTING_POSITION_FEN =
		"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

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

	private final @NotNull EnumMap<@NotNull Side, @NotNull CastlingRight> castlingRights;

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
	 * Move counter counts full-moves (1 full-move = WHITE's move + BLACK's move).
	 * It starts from 1. After the first full-move, it is incremented by 1 (so the value is 2).
	 */
	private int moveCounter;
	/**
	 * Half-move counter takes care of enforcing the fifty-move rule
	 *
	 * @see <a href="https://www.chessprogramming.org/Halfmove_Clock">Halfmove Clock on CPW</a>
	 */
	private int halfMoveCounter;

	/**
	 * Instantiates a new instance of {@link Board}
	 * <p>
	 * The returned board:
	 * - has no pieces (all squares are empty)
	 * - both of the sides has no castling right
	 * - side to move is white
	 * - en passant an en passant rather are null
	 * - move counter set to 1
	 * - half move counter set to 0
	 * <p>
	 * You can use {@link Board#loadFromFen(String fen)} to load a chess position.
	 */
	public Board() {

		bitboardOfSide = new long[Side.values().length];
		bitboardOfPiece = new long[Piece.values().length];
		squareToPiece = new Piece[Square.values().length];
		castlingRights = new EnumMap<>(Side.class);
		castlingRights.put(Side.WHITE, CastlingRight.NONE);
		castlingRights.put(Side.BLACK, CastlingRight.NONE);

		sideToMove = Side.WHITE;
		enPassantTarget = null;
		enPassant = null;

		moveCounter = 1;
		halfMoveCounter = 0;

	}

	/**
	 * Instantiates a new instance as a copy of another {@link Board} instance
	 *
	 * @param anotherBoard board to copy
	 */
	public Board(Board anotherBoard) {

		// see Java Cloning: Copy Constructors vs. Cloning
		//   https://dzone.com/articles/java-cloning-copy-constructor-vs-cloning


		bitboardOfSide = anotherBoard.bitboardOfSide.clone();
		bitboardOfPiece = anotherBoard.bitboardOfPiece.clone();
		squareToPiece = anotherBoard.squareToPiece.clone();
		castlingRights = anotherBoard.castlingRights.clone();

		sideToMove = anotherBoard.sideToMove;
		enPassantTarget = anotherBoard.enPassantTarget;
		enPassant = anotherBoard.enPassant;

		moveCounter = anotherBoard.moveCounter;
		halfMoveCounter = anotherBoard.halfMoveCounter;

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
	 * Checks if the given piece is on the the given square
	 *
	 * @param piece  the piece
	 * @param square the square
	 * @return boolean {@code true} iff the given piece is on the the given square
	 */
	public boolean hasPiece(@NotNull Piece piece, @NotNull Square square) {
		return (getBitboard(piece) & square.getBitboard()) != 0L;
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
	 * @return the castling right of the given side
	 */
	public @NotNull CastlingRight getCastlingRight(@NotNull Side side) {
		return castlingRights.get(side);
	}

	/**
	 * Gets the current value of the full-move counter
	 * <p>
	 * Move counter counts full-moves (1 full-move = WHITE's move + BLACK's move).
	 * It starts from 1. After the first full-move, it is incremented by 1 (so the value is 2).
	 *
	 * @return positive integer
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

	/**
	 * Computes en passant target square from the en passant square
	 *
	 * @param enPassant the en passant square
	 * @param side      the side of the pawn
	 * @return the en passant target square
	 * @see Board#findEnPassant(Square enPassantTarget, Side side) the reverse method is findEnPassant
	 * @see Board#enPassantTarget
	 * @see Board#enPassant
	 */
	public static @Nullable Square findEnPassantTarget(@Nullable Square enPassant, @NotNull Side side) {

		if (enPassant == null) {
			return null;
		}

		return side.isWhite()
			? Square.encode(Rank.RANK_5, enPassant.getFile())
			: Square.encode(Rank.RANK_4, enPassant.getFile());

	}

	/**
	 * Computes en passant square from en passant target square
	 * (that is the current square of a pawn that advanced two squares forward)
	 *
	 * @param enPassantTarget the current square of a pawn that advanced two squares forward
	 * @param side            the side of the pawn that advanced two squares forward
	 * @return the destination square as if the pawn advanced only one square forward
	 * @see Board#findEnPassantTarget(Square enPassant, Side side) the reverse method is findEnPassantTarget
	 * @see Board#enPassantTarget
	 * @see Board#enPassant
	 */
	public static @Nullable Square findEnPassant(@Nullable Square enPassantTarget, @NotNull Side side) {

		if (enPassantTarget == null) {
			return null;
		}

		return side.isWhite()
			? Square.encode(Rank.RANK_3, enPassantTarget.getFile())
			: Square.encode(Rank.RANK_6, enPassantTarget.getFile());

	}

	/**
	 * Adds a piece into a given square
	 *
	 * @param piece the piece
	 * @param sq    the square
	 */
	private void addPiece(@NotNull Piece piece, @NotNull Square sq) {
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
	private void removePiece(@NotNull Piece piece, @NotNull Square sq) {
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
	private @Nullable Piece movePiece(@NotNull Move move) {
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
	private Piece movePiece(@NotNull Square from, @NotNull Square to, @Nullable Piece promotion) {

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
			movingPiece.isOfType(PieceType.PAWN) // moving piece is a pawn
				&& getEnPassantTarget() != null // en passant target is NOT null
				&& from.getFile() != to.getFile() // from and to files (columns) are different
				&& capturedPiece == null // NO piece was captured on the destination (to) square
		) {
			log.info("movePiece: did an en passant capture");
			// set the captured piece that was captured during en passant
			capturedPiece = getPiece(getEnPassantTarget());
			// captured piece MUST be explicitly removed
			if (capturedPiece != null) {
				removePiece(capturedPiece, getEnPassantTarget());
				// backup.setCapturedSquare(getEnPassantTarget());
				// backup.setCapturedPiece(capturedPiece);
			}
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

				Castling castling = move.getCastling();

				// only if this move is a castling
				if (castling != null) {

					// player does not have castling right for the attempted castling move
					if (!move.isAllowedBy(getCastlingRight(side))) {
						return false;
					}

					// some squares between the king and the rook are not empty
					if (!Bitboard.areSquaresFree(getBitboard(), castling.getAllSquaresBB(side))) {
						return false;
					}

					// if no squares (trough which the king moves during the castling) are attacked
					// then this move is valid (no further checks are needed)
					return !isSquareAttackedBy(castling.getSquares(side), side.flip());

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
		if (pawns != 0L && (Bitboard.getPawnAttacks(side, kingSq) & pawns) != 0L) {
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

			Castling castling = move.getCastling();

			// this move is a castling
			if (castling != null) {

				if (move.isAllowedBy(getCastlingRight(side))) {
					movePiece(castling.getRookMove(side));
				} else {
					// this could happen if fullValidation == false
					return false;
				}

			}

			// after the king's move the player looses castling right (if they has still any)
			if (getCastlingRight(side) != CastlingRight.NONE) {
				castlingRights.put(side, CastlingRight.NONE);
			}

		} else if (movingPiece.isOfType(PieceType.ROOK) && CastlingRight.NONE != getCastlingRight(side)) {

			final Move oo = Castling.KING_SIDE.getRookMove(side);
			final Move ooo = Castling.QUEEN_SIDE.getRookMove(side);

			if (move.getFrom() == oo.getFrom()) {
				// update castling right (kingside no longer possible)
				if (CastlingRight.KING_AND_QUEEN_SIDE == getCastlingRight(side)) {
					castlingRights.put(side, CastlingRight.QUEEN_SIDE);
				} else if (CastlingRight.KING_SIDE == getCastlingRight(side)) {
					castlingRights.put(side, CastlingRight.NONE);
				}
			} else if (move.getFrom() == ooo.getFrom()) {
				// update castling right (queenside no longer possible)
				if (CastlingRight.KING_AND_QUEEN_SIDE == getCastlingRight(side)) {
					castlingRights.put(side, CastlingRight.KING_SIDE);
				} else if (CastlingRight.QUEEN_SIDE == getCastlingRight(side)) {
					castlingRights.put(side, CastlingRight.NONE);
				}
			}

		}

		Piece capturedPiece = movePiece(move);

		// if the side captured the other side's rook
		// it might be needed to update the other side's castling right
		if (capturedPiece != null && capturedPiece.isOfType(PieceType.ROOK)) {

			final Move oo = Castling.KING_SIDE.getRookMove(side.flip());
			final Move ooo = Castling.QUEEN_SIDE.getRookMove(side.flip());

			if (move.getTo() == oo.getFrom()) {
				// update the other side's castling right (kingside no longer possible)
				if (CastlingRight.KING_AND_QUEEN_SIDE == getCastlingRight(side.flip())) {
					castlingRights.put(side.flip(), CastlingRight.QUEEN_SIDE);
				} else if (CastlingRight.KING_SIDE == getCastlingRight(side.flip())) {
					castlingRights.put(side.flip(), CastlingRight.NONE);
				}
			} else if (move.getTo() == ooo.getFrom()) {
				// update the other side's castling right (queenside no longer possible)
				if (CastlingRight.KING_AND_QUEEN_SIDE == getCastlingRight(side.flip())) {
					castlingRights.put(side.flip(), CastlingRight.KING_SIDE);
				} else if (CastlingRight.QUEEN_SIDE == getCastlingRight(side.flip())) {
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

	/**
	 * Resets the board state
	 */
	public void clear() {

		Arrays.fill(bitboardOfSide, 0L);
		Arrays.fill(bitboardOfPiece, 0L);
		Arrays.fill(squareToPiece, null);
		castlingRights.put(Side.WHITE, CastlingRight.NONE);
		castlingRights.put(Side.BLACK, CastlingRight.NONE);

		sideToMove = Side.WHITE;
		enPassantTarget = null;
		enPassant = null;

		moveCounter = 1;
		halfMoveCounter = 0;

	}

	/**
	 * Generates legal moves (including captures) for the current board
	 *
	 * @return list of only legal moves
	 * @see MoveGenerator#generateLegalMoves(Board board)
	 */
	public List<Move> generateLegalMoves() {
		return MoveGenerator.generateLegalMoves(this);
	}

	/**
	 * Generates all pseudo-legal moves (including captures) for the current board
	 *
	 * @return list of pseudo-legal moves
	 * @see MoveGenerator#generatePseudoLegalMoves(Board board)
	 */
	public List<Move> generatePseudoLegalMoves() {
		return MoveGenerator.generatePseudoLegalMoves(this);
	}

	/**
	 * Generates all pseudo-legal captures (ONLY captures) for the current board
	 *
	 * @return list of pseudo-legal captures
	 * @see MoveGenerator#generatePseudoLegalCaptures(Board board)
	 */
	public List<Move> generatePseudoLegalCaptures() {
		return MoveGenerator.generatePseudoLegalCaptures(this);
	}

	/**
	 * Checks if there is a stalemate on the board
	 * (i.e. side-to-move is not in check but has not legal move)
	 *
	 * @return {@code true} iff there is a stalemate on the board
	 */
	public boolean isStaleMate() {

		try {
			if (!isKingAttacked()) {
				List<Move> l = MoveGenerator.generateLegalMoves(this);
				if (l.size() == 0) {
					return true;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return false;

	}

	/**
	 * Checks if there is a checkmate on the board
	 *
	 * @return {@code true} iff there is a checkmate on the board
	 */
	public boolean isCheckMate() {

		try {
			if (isKingAttacked()) {
				final List<Move> l = MoveGenerator.generateLegalMoves(this);
				if (l.size() == 0) {
					return true;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return false;

	}

	// TODO
	private boolean verifyNotPinnedPiece(@NotNull Side side, @NotNull Square enPassant, @NotNull Square target) {

		long pawns = Bitboard.getPawnAttacks(side, enPassant) & getBitboard(Piece.make(side.flip(), PieceType.PAWN));

		return pawns != 0L && verifyAllPins(pawns, side, enPassant, target);

	}

	// TODO
	private boolean verifyAllPins(long pawns, Side side, Square enPassant, Square target) {

		long onePawn = Bitboard.removeLSB(pawns);
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

	/**
	 * Generates the FEN representation of this board
	 *
	 * @return the FEN representation of this board
	 */
	public String getFen() {
		return getFen(true);
	}

	/**
	 * Generates the FEN representation of this board
	 * <p>
	 * TODO: Consider moving to separate static method (to separate class) and pass Board as the first argument.
	 * It is possible, because this method uses only public methods of the Board instance.
	 *
	 * @param includeCounters if {@code true} then the FEN includes values
	 *                        of {@link Board#getHalfMoveCounter()} and {@link Board#getMoveCounter()} counters
	 * @return the FEN representation of this board
	 * @see <a href="https://www.chessprogramming.org/Forsyth-Edwards_Notation">Forsyth–Edwards Notation on CPW</a>
	 * @see <a href="http://www.saremba.de/chessgml/standards/pgn/pgn-complete.htm#c16.1">FEN in the PGN standard (Section 16.1)</a>
	 * @see <a href="https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation">Forsyth–Edwards Notation on Wikipedia</a>
	 */
	public String getFen(boolean includeCounters) {

		StringBuilder fen = new StringBuilder();

		// the board contents are specified starting
		// with the eighth rank and ending with the first rank
		for (int ri = 7; ri >= 0; ri--) {

			Rank r = Rank.fromIndex(ri);

			// Empty squares are represented by the digits one through eight;
			// the digit used represents the count of contiguous empty squares along a rank.
			int contiguousEmptySquaresCount = 0;

			// for each rank, the squares are specified from file A (0) to file H (7)
			for (int fi = 0; fi <= 7; fi++) {

				File f = File.fromIndex(fi);

				Square sq = Square.encode(r, f);

				Piece piece = getPiece(sq);

				if (piece != null) {

					// before adding the piece representation
					// add number of preceding contiguousEmptySquares in the current rank
					if (contiguousEmptySquaresCount > 0) {
						fen.append(contiguousEmptySquaresCount);
						contiguousEmptySquaresCount = 0;
					}

					// add the piece representation
					fen.append(piece.getFenNotation());

				} else {
					// otherwise increment contiguousEmptySquaresCount for the current rank
					contiguousEmptySquaresCount++;
				}

			}

			// before going to the next rank
			// add number of preceding contiguousEmptySquares in the current rank
			if (contiguousEmptySquaresCount > 0) {
				fen.append(contiguousEmptySquaresCount);
				// no need to reset here as it is declared per-rank iteration
				// contiguousEmptySquaresCount = 0;
			}

			// A solidus character "/" is used to separate data of adjacent ranks.
			// (i.e.: After each rank BUT the last there is "/".)
			if (ri != 0) {
				fen.append("/");
			}

		}

		// side to move
		fen.append(" ");
		fen.append(getSideToMove().getNotation());

		// castling rights (first WHITE, then BLACK side)
		String rights = ""
			+ getCastlingRight(Side.WHITE).getFenNotation(Side.WHITE)
			+ getCastlingRight(Side.BLACK).getFenNotation(Side.BLACK);
		if (rights.equals("")) {
			fen.append(" -");
		} else {
			fen.append(" ");
			fen.append(rights);
		}

		// en passant
		if (getEnPassant() == null) {
			fen.append(" -");
		} else {
			fen.append(" ");
			// TODO: Are we in in accordance with the standard?
			//   Section 16.1.3.4, http://www.saremba.de/chessgml/standards/pgn/pgn-complete.htm#c16.1.3):
			//     An en passant target square is given if and only if the last move was
			//     a pawn advance of two squares. Therefore, an en passant target square
			//     field may have a square name even if there is no pawn of the opposing side
			//     that may immediately execute the en passant capture.
			fen.append(getEnPassant().getNotation());
		}

		// counters
		if (includeCounters) {
			fen.append(" ");
			// The fifth field is a non-negative integer representing the halfmove clock.
			// This number is the count of half-moves (or ply) since the last pawn advance
			// or capturing move. This value is used for the fifty move draw rule.
			fen.append(getHalfMoveCounter());
			fen.append(" ");
			// The sixth and last field is a positive integer that gives the full-move number.
			// This will have the value "1" for the first move of a game for both White and Black.
			// It is incremented by one immediately after each move by Black.
			fen.append(getMoveCounter());
		}

		return fen.toString();

	}

	/**
	 * Loads a specific chess position using FEN notation to this board
	 * <p>
	 * An example FEN: {@code rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1}
	 *
	 * @param fen a specific chess position in FEN notation
	 */
	public void loadFromFen(@NotNull String fen) {

		// reset the board
		clear();

		String squares = fen.substring(0, fen.indexOf(' '));
		String state = fen.substring(fen.indexOf(' ') + 1);

		String[] ranks = squares.split("/");
		int file;
		int rank = 7;
		for (String r : ranks) {
			file = 0;
			for (int i = 0; i < r.length(); i++) {
				char c = r.charAt(i);
				if (Character.isDigit(c)) {
					file += Integer.parseInt(c + "");
				} else {
					Square sq = Square.encode(Rank.fromIndex(rank), File.fromIndex(file));
					// addPiece(Constants.getPieceByNotation(c + ""), sq);
					addPiece(Piece.fromFenNotation(c + ""), sq);
					file++;
				}
			}
			rank--;
		}

		sideToMove = state.toLowerCase().charAt(0) == 'w' ? Side.WHITE : Side.BLACK;

		if (state.contains("KQ")) {
			castlingRights.put(Side.WHITE, CastlingRight.KING_AND_QUEEN_SIDE);
		} else if (state.contains("K")) {
			castlingRights.put(Side.WHITE, CastlingRight.KING_SIDE);
		} else if (state.contains("Q")) {
			castlingRights.put(Side.WHITE, CastlingRight.QUEEN_SIDE);
		} else {
			castlingRights.put(Side.WHITE, CastlingRight.NONE);
		}

		if (state.contains("kq")) {
			castlingRights.put(Side.BLACK, CastlingRight.KING_AND_QUEEN_SIDE);
		} else if (state.contains("k")) {
			castlingRights.put(Side.BLACK, CastlingRight.KING_SIDE);
		} else if (state.contains("q")) {
			castlingRights.put(Side.BLACK, CastlingRight.QUEEN_SIDE);
		} else {
			castlingRights.put(Side.BLACK, CastlingRight.NONE);
		}

		String[] flags = state.split(" ");

		if (flags.length >= 3) {
			String s = flags[2].toUpperCase().trim();
			if (!s.equals("-")) {
				Square ep = Square.valueOf(s);
				enPassant = ep;
				enPassantTarget = findEnPassantTarget(ep, sideToMove);
				if (!(squareAttackedByPieceType(getEnPassant(), getSideToMove(), PieceType.PAWN) != 0 &&
					verifyNotPinnedPiece(getSideToMove().flip(), getEnPassant(), getEnPassantTarget()))) {
					enPassantTarget = null;
				}
			} else {
				enPassant = null;
				enPassantTarget = null;
			}
			if (flags.length >= 4) {
				halfMoveCounter = Integer.parseInt(flags[3]);
				if (flags.length >= 5) {
					moveCounter = Integer.parseInt(flags[4]);
				}
			}
		}

	}

}
