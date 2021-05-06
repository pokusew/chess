package cz.martinendler.chess.engine.board;

import cz.martinendler.chess.engine.CastlingRight;
import cz.martinendler.chess.engine.Constants;
import cz.martinendler.chess.engine.Game;
import cz.martinendler.chess.engine.Side;
import cz.martinendler.chess.engine.move.Move;
import cz.martinendler.chess.engine.pieces.Piece;
import cz.martinendler.chess.engine.pieces.PieceType;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.List;

/**
 * A chessboard within a chess {@link Game}
 */
public class Board {

	// TODO: Should we use EnumMap or plain arrays
	//       for bitboardOfSide, bitboardOfPiece and squareToPiece?
	//       using EnumMap:
	//         pros: maybe cleaner, easier to use
	//         cons: probably slower (although EnumMap is also backed by array,
	//               the keys are validated thus it probably worsens access times)
	//         cons: cannot be use with Arrays.fill(arr, val) (see Board.clear())

	private final long[] bitboardOfSide;
	private final long[] bitboardOfPiece;
	private final Piece[] squareToPiece;

	private final EnumMap<Side, CastlingRight> castlingRights;

	private Side sideToMove;

	private Square enPassantTarget;
	private Square enPassant;

	private int moveId;
	private int halfMoveId;

	public Board() {

		bitboardOfSide = new long[Side.allSides.length];
		bitboardOfPiece = new long[Piece.allPieces.length];
		squareToPiece = new Piece[Square.values().length];
		castlingRights = new EnumMap<>(Side.class);

		// TODO:
		// setSideToMove(Side.WHITE);
		// setEnPassantTarget(Square.NONE);
		// setEnPassant(Square.NONE);
		// setMoveCounter(1);
		// setHalfMoveCounter(0);

	}

	/**
	 * Gets the global bitboard
	 * <p>
	 * 0b represents an empty square, 1b represents an occupied square (by either white or black piece)
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
	public long getBitboard(Piece piece) {
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
	public long getBitboard(Side side) {
		return bitboardOfSide[side.ordinal()];
	}

	/**
	 * Gets the piece on the given square
	 *
	 * @param square the square
	 * @return the piece
	 */
	public Piece getPiece(Square square) {
		return squareToPiece[square.ordinal()];
	}

	/**
	 * Checks if the given piece is on any of the given squares
	 *
	 * @param piece    the piece
	 * @param location the location
	 * @return boolean
	 */
	public boolean hasPiece(Piece piece, Square[] location) {
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
	public Side getSideToMove() {
		return sideToMove;
	}

	/**
	 * Gets en passant
	 *
	 * @return the en passant
	 */
	public Square getEnPassant() {
		return enPassant;
	}

	/**
	 * Gets en passant target
	 *
	 * @return the en passant target
	 */
	public Square getEnPassantTarget() {
		return enPassantTarget;
	}

	/**
	 * Gets castling right of the given side
	 *
	 * @param side the side
	 * @return the castleRight
	 */
	public CastlingRight getCastleRight(Side side) {
		return castlingRights.get(side);
	}

	/**
	 * Does the given mode of the given side leads to the pawn promotion?
	 *
	 * @param side the side
	 * @param move the move
	 * @return true/false
	 */
	private static boolean doesMoveLeadsToPromotion(Side side, Move move) {

		if (side.equals(Side.WHITE) && move.getTo().getRank().equals(Rank.RANK_8)) {
			return true;
		}

		return side.equals(Side.BLACK) && move.getTo().getRank().equals(Rank.RANK_1);

	}

	private static Square findEnPassantTarget(Square sq, Side side) {
		Square ep = Square.NONE;
		if (!Square.NONE.equals(sq)) {
			ep = Side.WHITE.equals(side)
				? Square.encode(Rank.RANK_5, sq.getFile())
				: Square.encode(Rank.RANK_4, sq.getFile());
		}
		return ep;
	}

	private static Square findEnPassant(Square sq, Side side) {
		Square ep = Square.NONE;
		if (!Square.NONE.equals(sq)) {
			ep = Side.WHITE.equals(side)
				? Square.encode(Rank.RANK_3, sq.getFile())
				: Square.encode(Rank.RANK_6, sq.getFile());
		}
		return ep;
	}


	/**
	 * TODO
	 * Adds a piece into a given square
	 *
	 * @param piece the piece
	 * @param sq    the sq
	 */
	public void setPiece(Piece piece, Square sq) {
		bitboardOfPiece[piece.ordinal()] |= sq.getBitboard();
		bitboardOfSide[piece.getPieceSide().ordinal()] |= sq.getBitboard();
		squareToPiece[sq.ordinal()] = piece;
	}

	/**
	 * TODO
	 * Removes a piece from a given square
	 *
	 * @param piece the piece
	 * @param sq    the sq
	 */
	public void unsetPiece(Piece piece, Square sq) {
		bitboardOfPiece[piece.ordinal()] ^= sq.getBitboard();
		bitboardOfSide[piece.getPieceSide().ordinal()] ^= sq.getBitboard();
		squareToPiece[sq.ordinal()] = Piece.NONE;
	}

	/*
	 * Move a piece
	 * @param move
	 * @return
	 */
	protected Piece movePiece(Move move) {
		return movePiece(move.getFrom(), move.getTo(), move.getPromotion());
	}

	/**
	 * Move piece piece.
	 *
	 * @param from      the from
	 * @param to        the to
	 * @param promotion the promotion
	 * @return the piece
	 */
	protected Piece movePiece(Square from, Square to, Piece promotion) {

		Piece movingPiece = getPiece(from);

		Piece capturedPiece = getPiece(to);

		unsetPiece(movingPiece, from);

		if (!Piece.NONE.equals(capturedPiece)) {
			unsetPiece(capturedPiece, to);
		}

		if (!Piece.NONE.equals(promotion)) {
			setPiece(promotion, to);
		} else {
			setPiece(movingPiece, to);
		}

		if (
			PieceType.PAWN.equals(movingPiece.getPieceType())
				&& !Square.NONE.equals(getEnPassantTarget())
				&& !to.getFile().equals(from.getFile())
				&& Piece.NONE.equals(capturedPiece)
		) {

			capturedPiece = getPiece(getEnPassantTarget());

		}

		return capturedPiece;

	}

	/**
	 * Returns if the the bitboard with pieces which can attack the given square
	 *
	 * @param square the square
	 * @param side   the side
	 * @return true if the square is attacked
	 */
	public long squareAttackedBy(Square square, Side side) {
		return squareAttackedBy(square, side, getBitboard());
	}

	/**
	 * Returns if the the bitboard with pieces which can attack the given square
	 *
	 * @param square          the square
	 * @param side            the side
	 * @param occupiedSquares occupied squares
	 * @return true if the square is attacked
	 */
	public long squareAttackedBy(Square square, Side side, long occupiedSquares) {

		long result;

		result = Bitboard.getPawnAttacks(side.flip(), square) & getBitboard(Piece.make(side, PieceType.PAWN)) & occupiedSquares;

		result |= Bitboard.getKnightAttacks(square, occupiedSquares) & getBitboard(Piece.make(side, PieceType.KNIGHT));
		result |= Bitboard.getBishopAttacks(occupiedSquares, square) & ((getBitboard(Piece.make(side, PieceType.BISHOP)) | getBitboard(Piece.make(side, PieceType.QUEEN))));
		result |= Bitboard.getRookAttacks(occupiedSquares, square) & ((getBitboard(Piece.make(side, PieceType.ROOK)) | getBitboard(Piece.make(side, PieceType.QUEEN))));
		result |= Bitboard.getKingAttacks(square, occupiedSquares) & getBitboard(Piece.make(side, PieceType.KING));

		return result;

	}

	/**
	 * Get all squares attacked by the given piece (side + piece type)
	 *
	 * @param square the square
	 * @param side   the side
	 * @param type   the type
	 * @return bitboard, 1b corresponds to the attacked squares
	 */
	public long squareAttackedByPieceType(Square square, Side side, PieceType type) {

		long result = 0L;

		long occ = getBitboard();

		switch (type) {
			case PAWN:
				result = Bitboard.getPawnAttacks(side.flip(), square) &
					getBitboard(Piece.make(side, PieceType.PAWN));
				break;
			case KNIGHT:
				result = Bitboard.getKnightAttacks(square, occ) &
					getBitboard(Piece.make(side, PieceType.KNIGHT));
				break;
			case BISHOP:
				result = Bitboard.getBishopAttacks(occ, square) &
					getBitboard(Piece.make(side, PieceType.BISHOP));
				break;
			case ROOK:
				result = Bitboard.getRookAttacks(occ, square) &
					getBitboard(Piece.make(side, PieceType.ROOK));
				break;
			case QUEEN:
				result = Bitboard.getQueenAttacks(occ, square) &
					getBitboard(Piece.make(side, PieceType.QUEEN));
				break;
			case KING:
				result |= Bitboard.getKingAttacks(square, occ) &
					getBitboard(Piece.make(side, PieceType.KING));
				break;
			default:
				break;
		}

		return result;

	}

	/**
	 * set of squares attacked by
	 *
	 * @param squares the squares
	 * @param side    the side
	 * @return boolean
	 */
	public boolean isSquareAttackedBy(List<Square> squares, Side side) {
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
	public Square getKingSquare(Side side) {

		Square result = Square.NONE;

		long piece = getBitboard(Piece.make(side, PieceType.KING));

		if (piece != 0L) {
			int square = Bitboard.bitScanForward(piece);
			return Square.squareAt(square);
		}

		return result;

	}

	/**
	 * Checks if the side-to-move's king is attacked
	 *
	 * @return boolean {@code true} when the side-to-move's king is attacked, false otherwise
	 */
	public boolean isKingAttacked() {
		return squareAttackedBy(getKingSquare(getSideToMove()), getSideToMove().flip()) != 0;
	}

	/**
	 * Verify if the move to be played leaves the resulting board in a legal position
	 *
	 * @param move           the move
	 * @param fullValidation performs a full validation on the move, not only if it leaves own king on check, but also if
	 *                       castling is legal, based on attacked pieces and squares, if board is in a consistent state:
	 *                       e.g.: Occupancy of source square by a piece that belongs to playing side, etc.
	 * @return the boolean
	 */
	public boolean isMoveLegal(@NotNull Move move, boolean fullValidation) {

		final Piece fromPiece = getPiece(move.getFrom());
		final Side side = getSideToMove();
		final PieceType fromType = fromPiece.getPieceType();
		final Piece capturedPiece = getPiece(move.getTo());

		if (fullValidation) {

			// player cannot capture their own pieces
			if (fromPiece.getPieceSide().equals(capturedPiece.getPieceSide())) {
				return false;
			}

			// player tries to move the opponent's piece
			if (!side.equals(fromPiece.getPieceSide())) {
				return false;
			}

			boolean pawnPromoting = fromPiece.getPieceType().equals(PieceType.PAWN) && doesMoveLeadsToPromotion(side, move);
			boolean hasPromoPiece = !move.getPromotion().equals(Piece.NONE);

			if (hasPromoPiece != pawnPromoting) {
				return false;
			}

			if (fromType.equals(PieceType.KING)) {
				if (move.isKingSideCastle()) {
					if (getCastleRight(side).equals(CastlingRight.KING_AND_QUEEN_SIDE) ||
						(getCastleRight(side).equals(CastlingRight.KING_SIDE))) {
						if ((getBitboard() & Constants.getOOAllSquaresBB(side)) == 0L) {
							return !isSquareAttackedBy(Constants.getOOSquares(side), side.flip());
						}
					}
					return false;
				}
				if (move.isQueenSideCastle()) {
					if (getCastleRight(side).equals(CastlingRight.KING_AND_QUEEN_SIDE) ||
						(getCastleRight(side).equals(CastlingRight.QUEEN_SIDE))) {
						if ((getBitboard() & Constants.getOOOAllSquaresBB(side)) == 0L) {
							return !isSquareAttackedBy(Constants.getOOOSquares(side), side.flip());
						}
					}
					return false;
				}
			}
		}

		if (fromType.equals(PieceType.KING)) {
			if (squareAttackedBy(move.getTo(), side.flip()) != 0L) {
				return false;
			}
		}

		Square kingSq = (fromType.equals(PieceType.KING) ? move.getTo() : getKingSquare(side));
		Side other = side.flip();
		long moveTo = move.getTo().getBitboard();
		long moveFrom = move.getFrom().getBitboard();
		long ep = getEnPassantTarget() != Square.NONE && move.getTo() == getEnPassant() &&
			(fromType.equals(PieceType.PAWN)) ? getEnPassantTarget().getBitboard() : 0;
		long allPieces = (getBitboard() ^ moveFrom ^ ep) | moveTo;

		long bishopAndQueens = ((getBitboard(Piece.make(other, PieceType.BISHOP)) |
			getBitboard(Piece.make(other, PieceType.QUEEN)))) & ~moveTo;

		if (bishopAndQueens != 0L &&
			(Bitboard.getBishopAttacks(allPieces, kingSq) & bishopAndQueens) != 0L) {
			return false;
		}

		long rookAndQueens = ((getBitboard(Piece.make(other, PieceType.ROOK)) |
			getBitboard(Piece.make(other, PieceType.QUEEN)))) & ~moveTo;

		if (rookAndQueens != 0L &&
			(Bitboard.getRookAttacks(allPieces, kingSq) & rookAndQueens) != 0L) {
			return false;
		}

		long knights = (getBitboard(Piece.make(other, PieceType.KNIGHT))) & ~moveTo;

		if (knights != 0L &&
			(Bitboard.getKnightAttacks(kingSq, allPieces) & knights) != 0L) {
			return false;
		}

		long pawns = (getBitboard(Piece.make(other, PieceType.PAWN))) & ~moveTo & ~ep;

		return pawns == 0L ||
			(Bitboard.getPawnAttacks(side, kingSq) & pawns) == 0L;

	}

	/**
	 * Executes the given move on this board
	 *
	 * @param move           the move
	 * @param fullValidation perform full validation
	 * @return true if operation was successful
	 */
	public boolean doMove(final Move move, boolean fullValidation) {

		if (!isMoveLegal(move, fullValidation)) {
			return false;
		}

		Piece movingPiece = getPiece(move.getFrom());
		Side side = getSideToMove();

		// MoveBackup backupMove = new MoveBackup(this, move);
		final boolean isCastle = move.isCastleMove();

		// incrementalHashKey ^= getSideKey(getSideToMove());
		// if (getEnPassantTarget() != Square.NONE) {
		// 	incrementalHashKey ^= getEnPassantKey(getEnPassantTarget());
		// }

		if (PieceType.KING.equals(movingPiece.getPieceType())) {
			if (isCastle) {
				if (move.hasCastleRight(getCastleRight(side))) {
					CastlingRight c = move.isKingSideCastle() ? CastlingRight.KING_SIDE : CastlingRight.QUEEN_SIDE;
					Move rookMove = Constants.getRookCastleMove(side, c);
					movePiece(rookMove);
				} else {
					return false;
				}
			}
			if (getCastleRight(side) != CastlingRight.NONE) {
				// incrementalHashKey ^= getCastleRightKey(side);
				castlingRights.put(side, CastlingRight.NONE);
			}
		} else if (PieceType.ROOK == movingPiece.getPieceType()
			&& CastlingRight.NONE != getCastleRight(side)) {
			final Move oo = Constants.getRookoo(side);
			final Move ooo = Constants.getRookooo(side);

			if (move.getFrom() == oo.getFrom()) {
				if (CastlingRight.KING_AND_QUEEN_SIDE == getCastleRight(side)) {
					// incrementalHashKey ^= getCastleRightKey(side);
					castlingRights.put(side, CastlingRight.QUEEN_SIDE);
					// incrementalHashKey ^= getCastleRightKey(side);
				} else if (CastlingRight.KING_SIDE == getCastleRight(side)) {
					// incrementalHashKey ^= getCastleRightKey(side);
					castlingRights.put(side, CastlingRight.NONE);
				}
			} else if (move.getFrom() == ooo.getFrom()) {
				if (CastlingRight.KING_AND_QUEEN_SIDE == getCastleRight(side)) {
					// incrementalHashKey ^= getCastleRightKey(side);
					castlingRights.put(side, CastlingRight.KING_SIDE);
					// incrementalHashKey ^= getCastleRightKey(side);
				} else if (CastlingRight.QUEEN_SIDE == getCastleRight(side)) {
					// incrementalHashKey ^= getCastleRightKey(side);
					castlingRights.put(side, CastlingRight.NONE);
				}
			}
		}

		Piece capturedPiece = movePiece(move);

		if (PieceType.ROOK == capturedPiece.getPieceType()) {
			final Move oo = Constants.getRookoo(side.flip());
			final Move ooo = Constants.getRookooo(side.flip());
			if (move.getTo() == oo.getFrom()) {
				if (CastlingRight.KING_AND_QUEEN_SIDE == getCastleRight(side.flip())) {
					// incrementalHashKey ^= getCastleRightKey(side.flip());
					castlingRights.put(side.flip(), CastlingRight.QUEEN_SIDE);
					// incrementalHashKey ^= getCastleRightKey(side.flip());
				} else if (CastlingRight.KING_SIDE == getCastleRight(side.flip())) {
					// incrementalHashKey ^= getCastleRightKey(side.flip());
					castlingRights.put(side.flip(), CastlingRight.NONE);
				}
			} else if (move.getTo() == ooo.getFrom()) {
				if (CastlingRight.KING_AND_QUEEN_SIDE == getCastleRight(side.flip())) {
					// incrementalHashKey ^= getCastleRightKey(side.flip());
					castlingRights.put(side.flip(), CastlingRight.KING_SIDE);
					// incrementalHashKey ^= getCastleRightKey(side.flip());
				} else if (CastlingRight.QUEEN_SIDE == getCastleRight(side.flip())) {
					// incrementalHashKey ^= getCastleRightKey(side.flip());
					castlingRights.put(side.flip(), CastlingRight.NONE);
				}
			}
		}

		if (Piece.NONE == capturedPiece) {
			halfMoveId++;
		} else {
			halfMoveId = 0;
		}

		enPassantTarget = Square.NONE;
		enPassant = Square.NONE;

		if (PieceType.PAWN == movingPiece.getPieceType()) {
			if (Math.abs(move.getTo().getRank().ordinal() - move.getFrom().getRank().ordinal()) == 2) {
				Piece otherPawn = Piece.make(side.flip(), PieceType.PAWN);
				enPassant = findEnPassant(move.getTo(), side);
				if (
					hasPiece(otherPawn, move.getTo().getSideSquares())
						&& verifyNotPinnedPiece(side, getEnPassant(), move.getTo())
				) {
					enPassantTarget = move.getTo();
					// incrementalHashKey ^= getEnPassantKey(getEnPassantTarget());
				}
			}
			halfMoveId = 0;
		}

		if (side == Side.BLACK) {
			moveId++;
		}

		sideToMove = side.flip();
		// incrementalHashKey ^= getSideKey(getSideToMove());

		// if (updateHistory) {
		// 	getHistory().addLast(getIncrementalHashKey());
		// }
		//
		// backup.add(backupMove);
		// // call listeners
		// if (isEnableEvents() && eventListener.get(BoardEventType.ON_MOVE).size() > 0) {
		// 	for (BoardEventListener evl : eventListener.get(BoardEventType.ON_MOVE)) {
		// 		evl.onEvent(move);
		// 	}
		// }

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
	private boolean verifyNotPinnedPiece(Side side, Square enPassant, Square target) {

		long pawns = Bitboard.getPawnAttacks(side, enPassant) & getBitboard(Piece.make(side.flip(), PieceType.PAWN));

		return pawns != 0 && verifyAllPins(pawns, side, enPassant, target);

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
