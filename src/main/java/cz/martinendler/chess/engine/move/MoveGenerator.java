package cz.martinendler.chess.engine.move;

import cz.martinendler.chess.engine.Castling;
import cz.martinendler.chess.engine.Side;
import cz.martinendler.chess.engine.board.Bitboard;
import cz.martinendler.chess.engine.board.Board;
import cz.martinendler.chess.engine.board.Rank;
import cz.martinendler.chess.engine.board.Square;
import cz.martinendler.chess.engine.pieces.Piece;
import cz.martinendler.chess.engine.pieces.PieceType;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

import static cz.martinendler.chess.engine.board.Bitboard.bitScanForward;
import static cz.martinendler.chess.engine.board.Bitboard.removeLSB;

/**
 * Generator of chess moves
 */
public class MoveGenerator {

	private MoveGenerator() {
		// we do not want MoveGenerator to be instantiable
	}

	/**
	 * Generates pawn captures
	 *
	 * @param board the board
	 * @param moves the moves
	 */
	public static void generatePawnCaptures(@NotNull Board board, @NotNull List<Move> moves) {

		Side side = board.getSideToMove();

		long pieces = board.getBitboard(Piece.make(side, PieceType.PAWN));

		// iterate trough all side-to-move's pawn pieces on the board
		while (pieces != 0L) {

			int sourceIndex = bitScanForward(pieces);
			pieces = removeLSB(pieces);

			Square sourceSq = Square.fromIndex(sourceIndex);

			long attacks = Bitboard.getPawnCaptures(
				side,
				sourceSq,
				board.getBitboard(side),
				board.getEnPassantTarget()
			) & ~board.getBitboard(side);
			// the part `& ~board.getBitboard(side)` ensures
			// that the side does not attempt to capture their own pieces
			// idea: why not use directly board.getBitboard(side.flip())

			while (attacks != 0L) {
				int targetIndex = bitScanForward(attacks);
				attacks = removeLSB(attacks);
				Square targetSq = Square.fromIndex(targetIndex);
				addPawnMoveWithPromotions(moves, side, targetSq, sourceSq);
			}

		}

	}

	/**
	 * Generates pawn moves (without captures)
	 *
	 * @param board the board
	 * @param moves the moves
	 */
	public static void generatePawnMoves(Board board, List<Move> moves) {

		Side side = board.getSideToMove();

		long pieces = board.getBitboard(Piece.make(side, PieceType.PAWN));

		// iterate trough all side-to-move's pawn pieces on the board
		while (pieces != 0L) {

			int sourceIndex = bitScanForward(pieces);
			pieces = removeLSB(pieces);

			Square sourceSq = Square.fromIndex(sourceIndex);

			long attacks = Bitboard.getPawnMoves(side, sourceSq, board.getBitboard());

			while (attacks != 0L) {
				int targetIndex = bitScanForward(attacks);
				attacks = removeLSB(attacks);
				Square targetSq = Square.fromIndex(targetIndex);
				addPawnMoveWithPromotions(moves, side, targetSq, sourceSq);
			}

		}

	}

	/**
	 * Adds the side's pawn move together with possible promotions (if any are possible)
	 *
	 * @param moves    move list
	 * @param side     side of the pawn whose move we are adding
	 * @param targetSq the pawn's move target square
	 * @param sourceSq the pawn's move source square
	 */
	private static void addPawnMoveWithPromotions(
		@NotNull List<Move> moves,
		@NotNull Side side,
		@NotNull Square targetSq,
		@NotNull Square sourceSq
	) {

		// the possible promotions for white pawn that moves to the 8th rank
		if (side.isWhite() && Rank.RANK_8 == targetSq.getRank()) {
			// the possible promotions
			moves.add(new Move(sourceSq, targetSq, Piece.WHITE_QUEEN));
			moves.add(new Move(sourceSq, targetSq, Piece.WHITE_ROOK));
			moves.add(new Move(sourceSq, targetSq, Piece.WHITE_BISHOP));
			moves.add(new Move(sourceSq, targetSq, Piece.WHITE_KNIGHT));
			return;
		}

		// the possible promotions for black pawn that moves to the 1st rank
		if (side.isBlack() && Rank.RANK_1 == targetSq.getRank()) {
			moves.add(new Move(sourceSq, targetSq, Piece.BLACK_QUEEN));
			moves.add(new Move(sourceSq, targetSq, Piece.BLACK_ROOK));
			moves.add(new Move(sourceSq, targetSq, Piece.BLACK_BISHOP));
			moves.add(new Move(sourceSq, targetSq, Piece.BLACK_KNIGHT));
			return;

		}

		// just the move itself as no promotions are possible
		moves.add(new Move(sourceSq, targetSq, null));

	}

	/**
	 * Generates knight moves/captures on the target squares allowed in the mask argument
	 *
	 * @param board the board
	 * @param moves the moves
	 * @param mask  mask of allowed targets
	 */
	public static void generateKnightMoves(@NotNull Board board, @NotNull List<Move> moves, long mask) {

		Side side = board.getSideToMove();

		long pieces = board.getBitboard(Piece.make(side, PieceType.KNIGHT));

		while (pieces != 0L) {

			int knightIndex = bitScanForward(pieces);
			pieces = removeLSB(pieces);

			Square sourceSq = Square.fromIndex(knightIndex);

			long attacks = Bitboard.getKnightAttacks(sourceSq, mask);

			while (attacks != 0L) {
				int attackIndex = bitScanForward(attacks);
				attacks = removeLSB(attacks);
				Square targetSq = Square.fromIndex(attackIndex);
				moves.add(new Move(sourceSq, targetSq, null));
			}

		}

	}

	/**
	 * Generates knight moves and captures
	 *
	 * @param board the board
	 * @param moves the moves
	 */
	public static void generateKnightMoves(@NotNull Board board, @NotNull List<Move> moves) {

		// allowed targets are all squares that are empty or occupied by an opposite side's piece
		generateKnightMoves(board, moves, ~board.getBitboard(board.getSideToMove()));

	}

	/**
	 * Generates bishop moves/captures on the target squares allowed in mask argument
	 *
	 * @param board the board
	 * @param moves the moves
	 * @param mask  mask of allowed targets
	 */
	public static void generateBishopMoves(Board board, List<Move> moves, long mask) {

		Side side = board.getSideToMove();

		long pieces = board.getBitboard(Piece.make(side, PieceType.BISHOP));

		while (pieces != 0L) {

			int sourceIndex = bitScanForward(pieces);
			pieces = removeLSB(pieces);

			Square sourceSq = Square.fromIndex(sourceIndex);

			long attacks = Bitboard.getBishopAttacks(board.getBitboard(), sourceSq) & mask;

			while (attacks != 0L) {
				int attackIndex = bitScanForward(attacks);
				attacks = removeLSB(attacks);
				Square targetSq = Square.fromIndex(attackIndex);
				moves.add(new Move(sourceSq, targetSq, null));
			}

		}

	}

	/**
	 * Generates bishop moves and captures
	 *
	 * @param board the board
	 * @param moves the moves
	 */
	public static void generateBishopMoves(Board board, List<Move> moves) {

		// allowed targets are all squares that are empty or occupied by an opposite side's piece
		generateBishopMoves(board, moves, ~board.getBitboard(board.getSideToMove()));

	}

	/**
	 * Generates rook moves/captures on the target squares allowed in mask param
	 *
	 * @param board the board
	 * @param moves the moves
	 * @param mask  mask of allowed targets
	 */
	public static void generateRookMoves(@NotNull Board board, @NotNull List<Move> moves, long mask) {

		Side side = board.getSideToMove();

		long pieces = board.getBitboard(Piece.make(side, PieceType.ROOK));

		while (pieces != 0L) {

			int sourceIndex = bitScanForward(pieces);
			pieces = removeLSB(pieces);

			Square sourceSq = Square.fromIndex(sourceIndex);

			long attacks = Bitboard.getRookAttacks(board.getBitboard(), sourceSq) & mask;

			while (attacks != 0L) {
				int attackIndex = bitScanForward(attacks);
				attacks = removeLSB(attacks);
				Square targetSq = Square.fromIndex(attackIndex);
				moves.add(new Move(sourceSq, targetSq, null));
			}

		}

	}

	/**
	 * Generates rook moves and captures
	 *
	 * @param board the board
	 * @param moves the moves
	 */
	public static void generateRookMoves(@NotNull Board board, @NotNull List<Move> moves) {

		// allowed targets are all squares that are empty or occupied by an opposite side's piece
		generateRookMoves(board, moves, ~board.getBitboard(board.getSideToMove()));

	}

	/**
	 * Generates queen moves/captures on the target squares allowed in mask param
	 *
	 * @param board the board
	 * @param moves the moves
	 * @param mask  mask of allowed targets
	 */
	public static void generateQueenMoves(@NotNull Board board, @NotNull List<Move> moves, long mask) {

		Side side = board.getSideToMove();

		long pieces = board.getBitboard(Piece.make(side, PieceType.QUEEN));

		while (pieces != 0L) {

			int sourceIndex = bitScanForward(pieces);
			pieces = removeLSB(pieces);

			Square sourceSq = Square.fromIndex(sourceIndex);

			long attacks = Bitboard.getQueenAttacks(board.getBitboard(), sourceSq) & mask;

			while (attacks != 0L) {
				int attackIndex = bitScanForward(attacks);
				attacks = removeLSB(attacks);
				Square targetSq = Square.fromIndex(attackIndex);
				moves.add(new Move(sourceSq, targetSq, null));
			}

		}

	}

	/**
	 * Generates queen moves and captures
	 *
	 * @param board the board
	 * @param moves the moves
	 */
	public static void generateQueenMoves(@NotNull Board board, @NotNull List<Move> moves) {

		// allowed targets are all squares that are empty or occupied by an opposite side's piece
		generateQueenMoves(board, moves, ~board.getBitboard(board.getSideToMove()));

	}

	/**
	 * Generates king moves/captures on the target squares allowed in mask param
	 *
	 * @param board the board
	 * @param moves the moves
	 * @param mask  mask of allowed targets
	 */
	public static void generateKingMoves(Board board, List<Move> moves, long mask) {

		Side side = board.getSideToMove();

		long pieces = board.getBitboard(Piece.make(side, PieceType.KING));

		while (pieces != 0L) {

			int sourceIndex = bitScanForward(pieces);
			pieces = removeLSB(pieces);

			Square sourceSq = Square.fromIndex(sourceIndex);

			long attacks = Bitboard.getKingAttacks(sourceSq, mask);

			while (attacks != 0L) {
				int attackIndex = bitScanForward(attacks);
				attacks = removeLSB(attacks);
				Square targetSq = Square.fromIndex(attackIndex);
				moves.add(new Move(sourceSq, targetSq, null));
			}

		}

	}

	/**
	 * Generates king moves and captures
	 *
	 * @param board the board
	 * @param moves the moves
	 */
	public static void generateKingMoves(Board board, List<Move> moves) {

		// allowed targets are all squares that are empty or occupied by an opposite side's piece
		generateKingMoves(board, moves, ~board.getBitboard(board.getSideToMove()));

	}

	/**
	 * Generates the given castling move
	 * <p>
	 * NOTE: This method is designed to be called from {@link MoveGenerator#generateCastlingMoves(Board board, List moves)}.
	 *
	 * @param board    the board
	 * @param moves    the moves
	 * @param castling the castling
	 */
	private static void generateCastlingMove(
		@NotNull Board board,
		@NotNull List<Move> moves,
		@NotNull Castling castling
	) {

		Side side = board.getSideToMove();

		if (
			// has the needed castling right
			board.getCastlingRight(side).allows(castling)
				// all squares in the path are empty
				&& (board.getBitboard() & castling.getAllSquaresBB(side)) == 0L
				// none of the king squares are attacked
				&& !board.isSquareAttackedBy(castling.getSquares(side), side.flip())
		) {
			moves.add(castling.getKingMove(side));
		}

	}

	/**
	 * Generates all castling moves
	 *
	 * @param board the board
	 * @param moves the moves
	 */
	public static void generateCastlingMoves(@NotNull Board board, @NotNull List<Move> moves) {

		// castling cannot be done when the king is in check
		// (the king cannot escape the check via castling)
		if (board.isKingAttacked()) {
			return;
		}

		generateCastlingMove(board, moves, Castling.KING_SIDE);
		generateCastlingMove(board, moves, Castling.QUEEN_SIDE);

	}

	/**
	 * Generates all pseudo-legal moves (including captures)
	 *
	 * @param board the board
	 * @return move list
	 */
	public static List<Move> generatePseudoLegalMoves(@NotNull Board board) {
		List<Move> moves = new LinkedList<>();
		generatePawnCaptures(board, moves);
		generatePawnMoves(board, moves);
		generateKnightMoves(board, moves);
		generateBishopMoves(board, moves);
		generateRookMoves(board, moves);
		generateQueenMoves(board, moves);
		generateKingMoves(board, moves);
		generateCastlingMoves(board, moves);
		return moves;
	}

	/**
	 * Generates all pseudo-legal captures (ONLY captures)
	 *
	 * @param board the board
	 * @return move list
	 */
	public static List<Move> generatePseudoLegalCaptures(@NotNull Board board) {
		List<Move> moves = new LinkedList<>();
		Side other = board.getSideToMove().flip();
		generatePawnCaptures(board, moves);
		generateKnightMoves(board, moves, board.getBitboard(other));
		generateBishopMoves(board, moves, board.getBitboard(other));
		generateRookMoves(board, moves, board.getBitboard(other));
		generateQueenMoves(board, moves, board.getBitboard(other));
		generateKingMoves(board, moves, board.getBitboard(other));
		return moves;
	}

	/**
	 * Generates legal moves (including captures)
	 *
	 * @param board the board
	 * @return move list
	 * @throws MoveGeneratorException when there is an unexpected error (normally, that should not happen)
	 */
	public static List<Move> generateLegalMoves(@NotNull Board board) throws MoveGeneratorException {
		try {
			List<Move> moves = generatePseudoLegalMoves(board);
			moves.removeIf(move -> !board.isMoveLegal(move, false));
			return moves;
		} catch (Exception e) {
			throw new MoveGeneratorException("Couldn't generate Legal moves: ", e);
		}
	}

}
