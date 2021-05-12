package cz.martinendler.chess.engine.pieces;

import cz.martinendler.chess.engine.Side;
import org.jetbrains.annotations.NotNull;

/**
 * A chess piece
 */
public enum Piece {

	// note: the order in which the enum values are declared here MATTERS!
	//       and the rest of the code depends on that order (via Piece.ordinal())

	/**
	 * White pawn piece
	 */
	WHITE_PAWN(Side.WHITE, PieceType.PAWN),
	/**
	 * White knight piece
	 */
	WHITE_KNIGHT(Side.WHITE, PieceType.KNIGHT),
	/**
	 * White bishop piece
	 */
	WHITE_BISHOP(Side.WHITE, PieceType.BISHOP),
	/**
	 * White rook piece
	 */
	WHITE_ROOK(Side.WHITE, PieceType.ROOK),
	/**
	 * White queen piece
	 */
	WHITE_QUEEN(Side.WHITE, PieceType.QUEEN),
	/**
	 * White king piece
	 */
	WHITE_KING(Side.WHITE, PieceType.KING),
	/**
	 * Black pawn piece
	 */
	BLACK_PAWN(Side.BLACK, PieceType.PAWN),
	/**
	 * Black knight piece
	 */
	BLACK_KNIGHT(Side.BLACK, PieceType.KNIGHT),
	/**
	 * Black bishop piece
	 */
	BLACK_BISHOP(Side.BLACK, PieceType.BISHOP),
	/**
	 * Black rook piece
	 */
	BLACK_ROOK(Side.BLACK, PieceType.ROOK),
	/**
	 * Black queen piece
	 */
	BLACK_QUEEN(Side.BLACK, PieceType.QUEEN),
	/**
	 * Black king piece
	 */
	BLACK_KING(Side.BLACK, PieceType.KING);

	/**
	 * Mapping for pieceMake[type.ordinal()][side.ordinal()]
	 */
	private static final Piece[][] pieceMake = {
		{WHITE_PAWN, BLACK_PAWN},
		{WHITE_KNIGHT, BLACK_KNIGHT},
		{WHITE_BISHOP, BLACK_BISHOP},
		{WHITE_ROOK, BLACK_ROOK},
		{WHITE_QUEEN, BLACK_QUEEN},
		{WHITE_KING, BLACK_KING},
	};

	private final Side side;
	private final PieceType type;

	Piece(@NotNull Side side, @NotNull PieceType type) {
		this.side = side;
		this.type = type;
	}

	/**
	 * Makes piece with the given parameters
	 *
	 * @param side the side
	 * @param type the type
	 * @return the piece
	 */
	public static @NotNull Piece make(@NotNull Side side, @NotNull PieceType type) {
		return pieceMake[type.ordinal()][side.ordinal()];
	}

	/**
	 * Gets piece side
	 *
	 * @return the piece side
	 */
	public @NotNull Side getPieceSide() {
		return side;
	}

	/**
	 * Gets piece type
	 *
	 * @return the piece type
	 */
	public @NotNull PieceType getPieceType() {
		return type;
	}

	/**
	 * Checks if this piece is of the given type
	 *
	 * @param type piece type
	 * @return {@code true} if this piece is of the given type
	 */
	public boolean isOfType(@NotNull PieceType type) {
		return getPieceType() == type;
	}

}
