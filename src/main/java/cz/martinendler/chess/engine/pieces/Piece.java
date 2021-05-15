package cz.martinendler.chess.engine.pieces;

import cz.martinendler.chess.engine.Notation;
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
	WHITE_PAWN(Side.WHITE, PieceType.PAWN, "♙"),
	/**
	 * White knight piece
	 */
	WHITE_KNIGHT(Side.WHITE, PieceType.KNIGHT, "♘"),
	/**
	 * White bishop piece
	 */
	WHITE_BISHOP(Side.WHITE, PieceType.BISHOP, "♗"),
	/**
	 * White rook piece
	 */
	WHITE_ROOK(Side.WHITE, PieceType.ROOK, "♖"),
	/**
	 * White queen piece
	 */
	WHITE_QUEEN(Side.WHITE, PieceType.QUEEN, "♕"),
	/**
	 * White king piece
	 */
	WHITE_KING(Side.WHITE, PieceType.KING, "♔"),
	/**
	 * Black pawn piece
	 */
	BLACK_PAWN(Side.BLACK, PieceType.PAWN, "♟"),
	/**
	 * Black knight piece
	 */
	BLACK_KNIGHT(Side.BLACK, PieceType.KNIGHT, "♞"),
	/**
	 * Black bishop piece
	 */
	BLACK_BISHOP(Side.BLACK, PieceType.BISHOP, "♝"),
	/**
	 * Black rook piece
	 */
	BLACK_ROOK(Side.BLACK, PieceType.ROOK, "♜"),
	/**
	 * Black queen piece
	 */
	BLACK_QUEEN(Side.BLACK, PieceType.QUEEN, "♛"),
	/**
	 * Black king piece
	 */
	BLACK_KING(Side.BLACK, PieceType.KING, "♚");

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
	/**
	 * One letter notation for usage in FEN
	 * <p>
	 * It is same as {@link PieceType#getSanNotation()} but the letter-case is different for each {@link Side}:
	 * {@link Side#WHITE} pieces: P N B R Q K (UPPERCASE letters)
	 * {@link Side#BLACK} pieces: p n b r q k (lowercase letters)
	 */
	private final @NotNull String fenNotation;
	/**
	 * Unicode symbol that can be used
	 * as the miniature piece icon in {@link cz.martinendler.chess.engine.Notation#FAN}
	 *
	 * @see <a href="https://en.wikipedia.org/wiki/Chess_symbols_in_Unicode">Chess symbols in Unicode on Wikipedia</a>
	 */
	private final @NotNull String unicodeSymbol;

	Piece(@NotNull Side side, @NotNull PieceType type, @NotNull String unicodeSymbol) {
		this.side = side;
		this.type = type;
		this.fenNotation = side.isWhite()
			? type.getSanNotation().toUpperCase()  // white pieces are represented with UPPERCASE letters
			: type.getSanNotation().toLowerCase(); // black pieces are represented with lowercase letters
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
	 * Gets piece one letter notation for usage in FEN
	 * <p>
	 * {@link Side#WHITE} pieces: P N B R Q K (uppercase letters)
	 * {@link Side#BLACK} pieces: p n b r q k (lowercase letters)
	 *
	 * @return the piece one letter notation for usage in FEN
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
	 * Gets piece letter/symbol for usage in the given notation (SAN/FAN/FEN)
	 * <p>
	 * {@link cz.martinendler.chess.engine.Notation#SAN} pieces: P N B R Q K (no side information)
	 * {@link cz.martinendler.chess.engine.Notation#FAN} pieces: ♙ ♘ ♗ ♖ ♕ ♔ ♟ ♞ ♝ ♜ ♛ ♚
	 * {@link cz.martinendler.chess.engine.Notation#FEN} pieces: P N B R Q K p n b r q k
	 *
	 * @return the piece letter/symbol for use in the given notation
	 */
	public @NotNull String getNotation(Notation notation) {
		return switch (notation) {
			case SAN -> type.getSanNotation();
			case FAN -> getUnicodeSymbol();
			case FEN -> getFenNotation();
		};
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
	 * @return {@link Piece} or {@code null} if there is no piece for the given notation
	 * @see Piece#getFenNotation()
	 */
	public static @Nullable Piece fromFenNotation(@NotNull String fenNotation) {
		return fenNotationToPiece.get(fenNotation);
	}

	/**
	 * Creates a piece from the its Unicode symbol
	 *
	 * @param unicodeSymbol the piece Unicode symbol
	 * @return {@link Piece} or {@code null} if there is no piece for the given unicodeSymbol
	 * @see Piece#getUnicodeSymbol()
	 */
	public static @Nullable Piece fromUnicodeSymbol(@NotNull String unicodeSymbol) {
		return unicodeSymbolToPiece.get(unicodeSymbol);
	}

	/**
	 * Creates a piece from the piece notation as used in SAN
	 *
	 * @param side        side of the piece (as SAN notation does not differentiate between white nad black pieces
	 * @param sanNotation the piece type notation as used in SAN
	 * @return {@link Piece} or {@code null} if there is no piece type for the given SAN notation
	 */
	public static @Nullable Piece fromSanNotation(@NotNull Side side, @NotNull String sanNotation) {

		PieceType type = PieceType.fromSanNotation(sanNotation);

		if (type == null) {
			return null;
		}

		return make(side, type);

	}

}
