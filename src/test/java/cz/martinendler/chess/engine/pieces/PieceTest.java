package cz.martinendler.chess.engine.pieces;

import cz.martinendler.chess.engine.Side;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PieceTest {

	@Test
	void testMake() {

		assertEquals(Piece.WHITE_PAWN, Piece.make(Side.WHITE, PieceType.PAWN));
		assertEquals(Piece.WHITE_KNIGHT, Piece.make(Side.WHITE, PieceType.KNIGHT));
		assertEquals(Piece.WHITE_BISHOP, Piece.make(Side.WHITE, PieceType.BISHOP));
		assertEquals(Piece.WHITE_ROOK, Piece.make(Side.WHITE, PieceType.ROOK));
		assertEquals(Piece.WHITE_QUEEN, Piece.make(Side.WHITE, PieceType.QUEEN));
		assertEquals(Piece.WHITE_KING, Piece.make(Side.WHITE, PieceType.KING));

		assertEquals(Piece.BLACK_PAWN, Piece.make(Side.BLACK, PieceType.PAWN));
		assertEquals(Piece.BLACK_KNIGHT, Piece.make(Side.BLACK, PieceType.KNIGHT));
		assertEquals(Piece.BLACK_BISHOP, Piece.make(Side.BLACK, PieceType.BISHOP));
		assertEquals(Piece.BLACK_ROOK, Piece.make(Side.BLACK, PieceType.ROOK));
		assertEquals(Piece.BLACK_QUEEN, Piece.make(Side.BLACK, PieceType.QUEEN));
		assertEquals(Piece.BLACK_KING, Piece.make(Side.BLACK, PieceType.KING));

	}

	@Test
	void testFromFenNotation() {

		assertEquals(Piece.WHITE_PAWN, Piece.fromFenNotation("P"));
		assertEquals(Piece.WHITE_KNIGHT, Piece.fromFenNotation("N"));
		assertEquals(Piece.WHITE_BISHOP, Piece.fromFenNotation("B"));
		assertEquals(Piece.WHITE_ROOK, Piece.fromFenNotation("R"));
		assertEquals(Piece.WHITE_QUEEN, Piece.fromFenNotation("Q"));
		assertEquals(Piece.WHITE_KING, Piece.fromFenNotation("K"));

		assertEquals(Piece.BLACK_PAWN, Piece.fromFenNotation("p"));
		assertEquals(Piece.BLACK_KNIGHT, Piece.fromFenNotation("n"));
		assertEquals(Piece.BLACK_BISHOP, Piece.fromFenNotation("b"));
		assertEquals(Piece.BLACK_ROOK, Piece.fromFenNotation("r"));
		assertEquals(Piece.BLACK_QUEEN, Piece.fromFenNotation("q"));
		assertEquals(Piece.BLACK_KING, Piece.fromFenNotation("k"));

		assertNull(Piece.fromFenNotation("x"));

	}

	@Test
	void testFromUnicodeSymbol() {

		assertEquals(Piece.WHITE_PAWN, Piece.fromUnicodeSymbol("♙"));
		assertEquals(Piece.WHITE_KNIGHT, Piece.fromUnicodeSymbol("♘"));
		assertEquals(Piece.WHITE_BISHOP, Piece.fromUnicodeSymbol("♗"));
		assertEquals(Piece.WHITE_ROOK, Piece.fromUnicodeSymbol("♖"));
		assertEquals(Piece.WHITE_QUEEN, Piece.fromUnicodeSymbol("♕"));
		assertEquals(Piece.WHITE_KING, Piece.fromUnicodeSymbol("♔"));
		assertEquals(Piece.BLACK_PAWN, Piece.fromUnicodeSymbol("♟"));
		assertEquals(Piece.BLACK_KNIGHT, Piece.fromUnicodeSymbol("♞"));
		assertEquals(Piece.BLACK_BISHOP, Piece.fromUnicodeSymbol("♝"));
		assertEquals(Piece.BLACK_ROOK, Piece.fromUnicodeSymbol("♜"));
		assertEquals(Piece.BLACK_QUEEN, Piece.fromUnicodeSymbol("♛"));
		assertEquals(Piece.BLACK_KING, Piece.fromUnicodeSymbol("♚"));

		assertNull(Piece.fromUnicodeSymbol("x"));

	}

}
