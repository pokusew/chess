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

		assertEquals(Piece.WHITE_PAWN, Piece.fromNotation("P"));
		assertEquals(Piece.WHITE_KNIGHT, Piece.fromNotation("N"));
		assertEquals(Piece.WHITE_BISHOP, Piece.fromNotation("B"));
		assertEquals(Piece.WHITE_ROOK, Piece.fromNotation("R"));
		assertEquals(Piece.WHITE_QUEEN, Piece.fromNotation("Q"));
		assertEquals(Piece.WHITE_KING, Piece.fromNotation("K"));

		assertEquals(Piece.BLACK_PAWN, Piece.fromNotation("p"));
		assertEquals(Piece.BLACK_KNIGHT, Piece.fromNotation("n"));
		assertEquals(Piece.BLACK_BISHOP, Piece.fromNotation("b"));
		assertEquals(Piece.BLACK_ROOK, Piece.fromNotation("r"));
		assertEquals(Piece.BLACK_QUEEN, Piece.fromNotation("q"));
		assertEquals(Piece.BLACK_KING, Piece.fromNotation("k"));

		assertNull(Piece.fromNotation("x"));

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
