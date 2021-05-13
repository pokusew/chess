package cz.martinendler.chess.engine.pieces;

import cz.martinendler.chess.engine.Side;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A chess piece
 */
public enum Piece {

	// note: the order in which the enum values are declared here MATTERS!
	//       and the rest of the code depends on that order (via Piece.ordinal())

	/**
	 * White pawn piece
	 */
	WHITE_PAWN(Side.WHITE, PieceType.PAWN, "P", "♙"),
	/**
	 * White knight piece
	 */
	WHITE_KNIGHT(Side.WHITE, PieceType.KNIGHT, "N", "♘"),
	/**
	 * White bishop piece
	 */
	WHITE_BISHOP(Side.WHITE, PieceType.BISHOP, "B", "♗"),
	/**
	 * White rook piece
	 */
	WHITE_ROOK(Side.WHITE, PieceType.ROOK, "R", "♖"),
	/**
	 * White queen piece
	 */
	WHITE_QUEEN(Side.WHITE, PieceType.QUEEN, "Q", "♕"),
	/**
	 * White king piece
	 */
	WHITE_KING(Side.WHITE, PieceType.KING, "K", "♔"),
	/**
	 * Black pawn piece
	 */
	BLACK_PAWN(Side.BLACK, PieceType.PAWN, "p", "♟"),
	/**
	 * Black knight piece
	 */
	BLACK_KNIGHT(Side.BLACK, PieceType.KNIGHT, "n", "♞"),
	/**
	 * Black bishop piece
	 */
	BLACK_BISHOP(Side.BLACK, PieceType.BISHOP, "b", "♝"),
	/**
	 * Black rook piece
	 */
	BLACK_ROOK(Side.BLACK, PieceType.ROOK, "r", "♜"),
	/**
	 * Black queen piece
	 */
	BLACK_QUEEN(Side.BLACK, PieceType.QUEEN, "q", "♛"),
	/**
	 * Black king piece
	 */
	BLACK_KING(Side.BLACK, PieceType.KING, "k", "♚");

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

	// see https://stackoverflow.com/questions/507602/how-can-i-initialise-a-static-map

	private static final Map<String, Piece> fenNotationToPiece = Arrays.stream(values())
		.collect(Collectors.toMap(Piece::getFenNotation, p -> p));

	private static final Map<String, Piece> unicodeSymbolToPiece = Arrays.stream(values())
		.collect(Collectors.toMap(Piece::getUnicodeSymbol, p -> p));

	private final @NotNull Side side;
	private final @NotNull PieceType type;
	private final @NotNull String fenNotation;
	/**
	 * @see <a href="https://en.wikipedia.org/wiki/Chess_symbols_in_Unicode">Chess symbols in Unicode on Wikipedia</a>
	 */
	private final @NotNull String unicodeSymbol;

	Piece(@NotNull Side side, @NotNull PieceType type, @NotNull String fenNotation, @NotNull String unicodeSymbol) {
		this.side = side;
		this.type = type;
		this.fenNotation = fenNotation;
		this.unicodeSymbol = unicodeSymbol;
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
	 * Gets piece notation for usage in FEN
	 *
	 * @return the piece piece notation for usage in FEN
	 * @see Piece#fromFenNotation(String fenNotation)
	 */
	public @NotNull String getFenNotation() {
		return fenNotation;
	}

	/**
	 * Gets piece Unicode symbol
	 *
	 * @return the piece Unicode symbol
	 * @see <a href="https://en.wikipedia.org/wiki/Chess_symbols_in_Unicode">Chess symbols in Unicode on Wikipedia</a>
	 * @see Piece#fromUnicodeSymbol(String unicodeSmybol)
	 */
	public @NotNull String getUnicodeSymbol() {
		return unicodeSymbol;
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

	/**
	 * Creates a piece from the piece notation as used in FEN
	 *
	 * @param fenNotation the piece notation as used in FEN
	 * @return {@link Piece} or {@code null} if there is no piece for the given fenNotation
	 * @see Piece#getFenNotation()
	 */
	public static @Nullable Piece fromFenNotation(@NotNull String fenNotation) {
		return fenNotationToPiece.get(fenNotation);
	}

	/**
	 * Creates a piece from the its Unicode symbol
	 *
	 * @param unicodeSymbol the piece notation as used in FEN
	 * @return {@link Piece} or {@code null} if there is no piece for the given unicodeSymbol
	 * @see Piece#getUnicodeSymbol()
	 */
	public static @Nullable Piece fromUnicodeSymbol(@NotNull String unicodeSymbol) {
		return unicodeSymbolToPiece.get(unicodeSymbol);
	}

}
