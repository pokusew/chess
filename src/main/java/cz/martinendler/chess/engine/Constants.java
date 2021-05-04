package cz.martinendler.chess.engine;

import cz.martinendler.chess.engine.board.Square;
import cz.martinendler.chess.engine.move.Move;
import cz.martinendler.chess.engine.pieces.Piece;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	/**
	 * The constant pieceNotation.
	 */
	public static final EnumMap<Piece, String> pieceNotation = new EnumMap<>(Piece.class);
	/**
	 * The constant pieceNotationR.
	 */
	public static final Map<String, Piece> pieceNotationR = new HashMap<>(12);

	public static final Move emptyMove = new Move(Square.NONE, Square.NONE);

	static {

		pieceNotation.put(Piece.WHITE_PAWN, "P");
		pieceNotation.put(Piece.WHITE_KNIGHT, "N");
		pieceNotation.put(Piece.WHITE_BISHOP, "B");
		pieceNotation.put(Piece.WHITE_ROOK, "R");
		pieceNotation.put(Piece.WHITE_QUEEN, "Q");
		pieceNotation.put(Piece.WHITE_KING, "K");
		pieceNotation.put(Piece.BLACK_PAWN, "p");
		pieceNotation.put(Piece.BLACK_KNIGHT, "n");
		pieceNotation.put(Piece.BLACK_BISHOP, "b");
		pieceNotation.put(Piece.BLACK_ROOK, "r");
		pieceNotation.put(Piece.BLACK_QUEEN, "q");
		pieceNotation.put(Piece.BLACK_KING, "k");

		pieceNotationR.put("P", Piece.WHITE_PAWN);
		pieceNotationR.put("N", Piece.WHITE_KNIGHT);
		pieceNotationR.put("B", Piece.WHITE_BISHOP);
		pieceNotationR.put("R", Piece.WHITE_ROOK);
		pieceNotationR.put("Q", Piece.WHITE_QUEEN);
		pieceNotationR.put("K", Piece.WHITE_KING);
		pieceNotationR.put("p", Piece.BLACK_PAWN);
		pieceNotationR.put("n", Piece.BLACK_KNIGHT);
		pieceNotationR.put("b", Piece.BLACK_BISHOP);
		pieceNotationR.put("r", Piece.BLACK_ROOK);
		pieceNotationR.put("q", Piece.BLACK_QUEEN);
		pieceNotationR.put("k", Piece.BLACK_KING);
	}

	private Constants() {
		// we do not want Constants to be instantiable
	}

	/**
	 * Gets the notation of a piece
	 *
	 * @param piece the piece
	 * @return piece notation
	 */
	public static String getPieceNotation(Piece piece) {
		return pieceNotation.get(piece);
	}

	/**
	 * Gets the piece by its notation
	 *
	 * @param notation the notation
	 * @return piece by notation
	 */
	public static Piece getPieceByNotation(String notation) {
		return pieceNotationR.get(notation);
	}

}
