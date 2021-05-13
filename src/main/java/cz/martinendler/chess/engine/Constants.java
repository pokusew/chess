package cz.martinendler.chess.engine;

import cz.martinendler.chess.engine.board.Bitboard;
import cz.martinendler.chess.engine.board.Square;
import cz.martinendler.chess.engine.move.Move;

import java.util.List;

/**
 * Chess game constants (such as starting positions, castings, pieces' notation, ...)
 */
public class Constants {

	/**
	 * Standard chess starting position as a FEN string
	 */
	public static final String startStandardFENPosition = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

	/**
	 * The move of the white king as a part of the kingside castling (the short one)
	 */
	public static final Move DEFAULT_WHITE_OO = new Move(Square.E1, Square.G1);
	/**
	 * The move of the white king as a part of the queenside castling (the long one)
	 */
	public static final Move DEFAULT_WHITE_OOO = new Move(Square.E1, Square.C1);

	/**
	 * The move of the black king as a part of the kingside castling (the short one)
	 */
	public static final Move DEFAULT_BLACK_OO = new Move(Square.E8, Square.G8);
	/**
	 * The move of the black king as a part of the queenside castling (the long one)
	 */
	public static final Move DEFAULT_BLACK_OOO = new Move(Square.E8, Square.C8);

	/**
	 * The move of the white rook as a part of the kingside castling (the short one)
	 */
	public static final Move DEFAULT_WHITE_ROOK_OO = new Move(Square.H1, Square.F1);
	/**
	 * The move of the white rook as a part of the queenside castling (the long one)
	 */
	public static final Move DEFAULT_WHITE_ROOK_OOO = new Move(Square.A1, Square.D1);

	/**
	 * The move of the black rook as a part of the kingside castling (the short one)
	 */
	public static final Move DEFAULT_BLACK_ROOK_OO = new Move(Square.H8, Square.F8);
	/**
	 * The move of the black rook as a part of the queenside castling (the long one)
	 */
	public static final Move DEFAULT_BLACK_ROOK_OOO = new Move(Square.A8, Square.D8);

	/**
	 * The inner squares between the king and the rook
	 * in the white's kingside castling (the short one)
	 */
	public static final List<Square> DEFAULT_WHITE_OO_SQUARES = List.of(
		Square.F1,
		Square.G1
	);
	/**
	 * The inner squares between the king and the rook
	 * in the white's queenside castling (the long one)
	 */
	public static final List<Square> DEFAULT_WHITE_OOO_SQUARES = List.of(
		Square.D1,
		Square.C1
		// TODO: why not also Square.B1?
	);

	/**
	 * The inner squares between the king and the rook
	 * in the black's kingside castling (the short one)
	 */
	public static final List<Square> DEFAULT_BLACK_OO_SQUARES = List.of(
		Square.F8,
		Square.G8
	);
	/**
	 * The inner squares between the king and the rook
	 * in the black's queenside castling (the long one)
	 */
	public static final List<Square> DEFAULT_BLACK_OOO_SQUARES = List.of(
		Square.D8,
		Square.C8
		// TODO: why not also Square.B1?
	);

	/**
	 * The all (incl. the king and the rook) squares between the king and the rook
	 * in the white's kingside castling (the short one)
	 */
	public static final List<Square> DEFAULT_WHITE_OO_ALL_SQUARES = List.of(
		Square.F1,
		Square.G1
	);
	/**
	 * The all (incl. the king and the rook) squares between the king and the rook
	 * in the white's queenside castling (the long one)
	 */
	public static final List<Square> DEFAULT_WHITE_OOO_ALL_SQUARES = List.of(
		Square.D1,
		Square.C1,
		Square.B1
	);

	/**
	 * The all (incl. the king and the rook) squares between the king and the rook
	 * in the black's kingside castling (the short one)
	 */
	public static final List<Square> DEFAULT_BLACK_OO_ALL_SQUARES = List.of(
		Square.F8,
		Square.G8
	);
	/**
	 * The all (incl. the king and the rook) squares between the king and the rook
	 * in the black's queenside castling (the long one)
	 */
	public static final List<Square> DEFAULT_BLACK_OOO_ALL_SQUARES = List.of(
		Square.D8,
		Square.C8,
		Square.B8
	);

	public static final long DEFAULT_WHITE_OO_SQUARES_BB = Bitboard.squareListToBB(
		Constants.DEFAULT_WHITE_OO_SQUARES
	);
	public static final long DEFAULT_WHITE_OOO_SQUARES_BB = Bitboard.squareListToBB(
		Constants.DEFAULT_WHITE_OOO_SQUARES
	);
	public static final long DEFAULT_BLACK_OO_SQUARES_BB = Bitboard.squareListToBB(
		Constants.DEFAULT_BLACK_OO_SQUARES
	);
	public static final long DEFAULT_BLACK_OOO_SQUARES_BB = Bitboard.squareListToBB(
		Constants.DEFAULT_BLACK_OOO_SQUARES
	);
	public static final long DEFAULT_WHITE_OO_ALL_SQUARES_BB = Bitboard.squareListToBB(
		Constants.DEFAULT_WHITE_OO_ALL_SQUARES
	);
	public static final long DEFAULT_WHITE_OOO_ALL_SQUARES_BB = Bitboard.squareListToBB(
		Constants.DEFAULT_WHITE_OOO_ALL_SQUARES
	);
	public static final long DEFAULT_BLACK_OO_ALL_SQUARES_BB = Bitboard.squareListToBB(
		Constants.DEFAULT_BLACK_OO_ALL_SQUARES
	);
	public static final long DEFAULT_BLACK_OOO_ALL_SQUARES_BB = Bitboard.squareListToBB(
		Constants.DEFAULT_BLACK_OOO_ALL_SQUARES
	);

	private Constants() {
		// we do not want Constants to be instantiable
	}

	public static long getOOAllSquaresBB(Side side) {
		return Side.WHITE.equals(side)
			? DEFAULT_WHITE_OO_ALL_SQUARES_BB
			: DEFAULT_BLACK_OO_ALL_SQUARES_BB;
	}

	public static List<Square> getOOSquares(Side side) {
		return Side.WHITE.equals(side)
			? DEFAULT_WHITE_OO_SQUARES
			: DEFAULT_BLACK_OO_SQUARES;
	}

	public static long getOOOAllSquaresBB(Side side) {
		return Side.WHITE.equals(side)
			? DEFAULT_WHITE_OOO_ALL_SQUARES_BB
			: DEFAULT_BLACK_OOO_ALL_SQUARES_BB;
	}

	public static List<Square> getOOOSquares(Side side) {
		return Side.WHITE.equals(side)
			? DEFAULT_WHITE_OOO_SQUARES
			: DEFAULT_BLACK_OOO_SQUARES;
	}

	/**
	 * Gets the rook castle move
	 *
	 * @param side        the side
	 * @param castleRight the castle right
	 * @return rook castle move
	 */
	public static Move getRookCastleMove(Side side, CastlingRight castleRight) {
		Move move = null;
		if (Side.WHITE.equals(side)) {
			if (CastlingRight.KING_SIDE.equals(castleRight)) {
				move = DEFAULT_WHITE_ROOK_OO;
			} else if (CastlingRight.QUEEN_SIDE.equals(castleRight)) {
				move = DEFAULT_WHITE_ROOK_OOO;
			}
		} else {
			if (CastlingRight.KING_SIDE.equals(castleRight)) {
				move = DEFAULT_BLACK_ROOK_OO;
			} else if (CastlingRight.QUEEN_SIDE.equals(castleRight)) {
				move = DEFAULT_BLACK_ROOK_OOO;
			}
		}
		return move;
	}

	/**
	 * Gets rookoo.
	 *
	 * @param side the side
	 * @return the rookoo
	 */
	public static Move getRookoo(Side side) {
		return Side.WHITE.equals(side) ? DEFAULT_WHITE_ROOK_OO : DEFAULT_BLACK_ROOK_OO;
	}

	/**
	 * Gets rookooo.
	 *
	 * @param side the side
	 * @return the rookooo
	 */
	public static Move getRookooo(Side side) {
		return Side.WHITE.equals(side) ? DEFAULT_WHITE_ROOK_OOO : DEFAULT_BLACK_ROOK_OOO;
	}

}
