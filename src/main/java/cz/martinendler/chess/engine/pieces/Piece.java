package cz.martinendler.chess.engine.pieces;

import cz.martinendler.chess.engine.Side;

import java.util.EnumMap;

/**
 * The enum Piece.
 */
public enum Piece {

	/**
	 * White pawn piece.
	 */
	WHITE_PAWN,
	/**
	 * White knight piece.
	 */
	WHITE_KNIGHT,
	/**
	 * White bishop piece.
	 */
	WHITE_BISHOP,
	/**
	 * White rook piece.
	 */
	WHITE_ROOK,
	/**
	 * White queen piece.
	 */
	WHITE_QUEEN,
	/**
	 * White king piece.
	 */
	WHITE_KING,
	/**
	 * Black pawn piece.
	 */
	BLACK_PAWN,
	/**
	 * Black knight piece.
	 */
	BLACK_KNIGHT,
	/**
	 * Black bishop piece.
	 */
	BLACK_BISHOP,
	/**
	 * Black rook piece.
	 */
	BLACK_ROOK,
	/**
	 * Black queen piece.
	 */
	BLACK_QUEEN,
	/**
	 * Black king piece.
	 */
	BLACK_KING,
	/**
	 * None piece.
	 */
	NONE;

	public static Piece[] allPieces = values();

	/**
	 * The Piece type.
	 */
	static EnumMap<Piece, PieceType> pieceType =
		new EnumMap<Piece, PieceType>(Piece.class);
	/**
	 * The Piece side.
	 */
	static EnumMap<Piece, Side> pieceSide =
		new EnumMap<Piece, Side>(Piece.class);
	private static Piece[][] pieceMake = {
		{WHITE_PAWN, BLACK_PAWN},
		{WHITE_KNIGHT, BLACK_KNIGHT},
		{WHITE_BISHOP, BLACK_BISHOP},
		{WHITE_ROOK, BLACK_ROOK},
		{WHITE_QUEEN, BLACK_QUEEN},
		{WHITE_KING, BLACK_KING},
		{NONE, NONE},
	};

	static {
		pieceType.put(Piece.WHITE_PAWN, PieceType.PAWN);
		pieceType.put(Piece.WHITE_KNIGHT, PieceType.KNIGHT);
		pieceType.put(Piece.WHITE_BISHOP, PieceType.BISHOP);
		pieceType.put(Piece.WHITE_ROOK, PieceType.ROOK);
		pieceType.put(Piece.WHITE_QUEEN, PieceType.QUEEN);
		pieceType.put(Piece.WHITE_KING, PieceType.KING);

		pieceType.put(Piece.BLACK_PAWN, PieceType.PAWN);
		pieceType.put(Piece.BLACK_KNIGHT, PieceType.KNIGHT);
		pieceType.put(Piece.BLACK_BISHOP, PieceType.BISHOP);
		pieceType.put(Piece.BLACK_ROOK, PieceType.ROOK);
		pieceType.put(Piece.BLACK_QUEEN, PieceType.QUEEN);
		pieceType.put(Piece.BLACK_KING, PieceType.KING);

		pieceSide.put(Piece.WHITE_PAWN, Side.WHITE);
		pieceSide.put(Piece.WHITE_KNIGHT, Side.WHITE);
		pieceSide.put(Piece.WHITE_BISHOP, Side.WHITE);
		pieceSide.put(Piece.WHITE_ROOK, Side.WHITE);
		pieceSide.put(Piece.WHITE_QUEEN, Side.WHITE);
		pieceSide.put(Piece.WHITE_KING, Side.WHITE);

		pieceSide.put(Piece.BLACK_PAWN, Side.BLACK);
		pieceSide.put(Piece.BLACK_KNIGHT, Side.BLACK);
		pieceSide.put(Piece.BLACK_BISHOP, Side.BLACK);
		pieceSide.put(Piece.BLACK_ROOK, Side.BLACK);
		pieceSide.put(Piece.BLACK_QUEEN, Side.BLACK);
		pieceSide.put(Piece.BLACK_KING, Side.BLACK);


	}

	/**
	 * From value piece.
	 *
	 * @param v the v
	 * @return the piece
	 */
	public static Piece fromValue(String v) {
		return valueOf(v);
	}

	/**
	 * Make piece.
	 *
	 * @param side the side
	 * @param type the type
	 * @return the piece
	 */
	public static Piece make(Side side, PieceType type) {
		//return Piece.valueOf(side+"_"+type);
		return pieceMake[type.ordinal()][side.ordinal()];
	}

	/**
	 * Value string.
	 *
	 * @return the string
	 */
	public String value() {
		return name();
	}

	/**
	 * Gets piece type.
	 *
	 * @return the piece type
	 */
	public PieceType getPieceType() {
		return pieceType.get(this);
	}

	/**
	 * Gets piece side.
	 *
	 * @return the piece side
	 */
	public Side getPieceSide() {
		return pieceSide.get(this);
	}

}
