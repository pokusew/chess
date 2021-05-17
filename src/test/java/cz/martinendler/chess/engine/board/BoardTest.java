package cz.martinendler.chess.engine.board;

import cz.martinendler.chess.engine.CastlingRight;
import cz.martinendler.chess.engine.Side;
import cz.martinendler.chess.engine.move.Move;
import cz.martinendler.chess.engine.move.MoveGeneratorException;
import cz.martinendler.chess.engine.pieces.Piece;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

	@Test
	public void testClone() {

		Board b1 = new Board();

		Board b2 = new Board(b1);

		assertNotSame(b1, b2); // nothing else is possible
		assertNotEquals(b1, b2); // because custom equals not implemented (yet)

		// TODO: test that internal array are copied

	}

	@Test
	public void testMoveAndFENParsing() {

		String fen1 = "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1";
		String fen2 = "rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPP1PPP/RNBQKBNR w KQkq e6 0 2";
		String fen3 = "rnbqkbnr/ppp1pppp/8/3p4/4P3/8/PPPP1PPP/RNBQKBNR w KQkq d6 0 2";
		String fen4 = "rnbqkbnr/ppp1pppp/8/3pP3/8/8/PPPP1PPP/RNBQKBNR b KQkq - 0 2";
		String fen5 = "rnbqkbnr/ppp1p1pp/8/3pPp2/8/8/PPPP1PPP/RNBQKBNR w KQkq f6 0 3";
		String fen6 = "rnbqkbnr/ppp1p1pp/5P2/3p4/8/8/PPPP1PPP/RNBQKBNR b KQkq - 0 3";
		String fen7 = "rnbqkbnr/ppp3pp/5p2/3p4/8/8/PPPP1PPP/RNBQKBNR w KQkq - 0 4";

		Board board = new Board();

		board.loadFromFen(fen1);

		assertEquals(Piece.BLACK_BISHOP, board.getPiece(Square.C8));
		assertEquals(Piece.WHITE_BISHOP, board.getPiece(Square.C1));
		assertEquals(Piece.BLACK_ROOK, board.getPiece(Square.H8));
		assertEquals(Piece.WHITE_ROOK, board.getPiece(Square.H1));

		assertEquals(1, board.getMoveCounter());
		assertEquals(0, board.getHalfMoveCounter());
		assertEquals(Square.E3, board.getEnPassant());
		assertNull(board.getEnPassantTarget());

		assertEquals(fen1, board.getFen());

		Move move;

		// TODO: uncomment once we implement undo
		// Move move = new Move(Square.E7, Square.E5); //sm: b
		// assertTrue(board.doMove(move, true));
		// System.out.println("new FEN after: " + move.toString() + ": " + board.getFen());
		// System.out.println("hash code is: " + board.hashCode());
		// assertEquals(fen2, board.getFen()); //sm: w
		//
		// board.undoMove();
		// System.out.println("new FEN after: undo: " + board.getFen());
		// System.out.println("hash code is: " + board.hashCode());
		// assertEquals(fen1, board.getFen()); // sm: b

		assertEquals(Side.BLACK, board.getSideToMove());
		move = new Move(Square.D7, Square.D5);
		assertTrue(board.doMove(move, true));
		System.out.println("new FEN after: " + move.toString() + ": " + board.getFen());
		System.out.println("hash code is: " + board.hashCode());
		assertEquals(fen3, board.getFen()); // sm: w

		assertEquals(Side.WHITE, board.getSideToMove());
		move = new Move(Square.E4, Square.E5);
		assertTrue(board.doMove(move, true));
		System.out.println("new FEN after: " + move.toString() + ": " + board.getFen());
		System.out.println("hash code is: " + board.hashCode());
		assertEquals(fen4, board.getFen());

		assertEquals(Side.BLACK, board.getSideToMove());
		move = new Move(Square.F7, Square.F5);
		assertTrue(board.doMove(move, true));
		System.out.println("new FEN after: " + move.toString() + ": " + board.getFen());
		System.out.println("hash code is: " + board.hashCode());
		assertEquals(fen5, board.getFen());

		assertEquals(Side.WHITE, board.getSideToMove());
		move = new Move(Square.E5, Square.F6);
		assertTrue(board.doMove(move, true));
		System.out.println("new FEN after: " + move.toString() + ": " + board.getFen());
		System.out.println("hash code is: " + board.hashCode());
		assertEquals(fen6, board.getFen());

		assertEquals(Side.BLACK, board.getSideToMove());
		move = new Move(Square.E7, Square.F6);
		assertTrue(board.doMove(move, true));
		System.out.println("new FEN after: " + move.toString() + ": " + board.getFen());
		System.out.println("hash code is: " + board.hashCode());
		assertEquals(fen7, board.getFen());

		assertEquals(Side.WHITE, board.getSideToMove());

	}


	@Test
	public void testCastleAndFENParsing() {

		String fen1 = "rnbqk2r/ppp1b1pp/5p1n/3p4/8/3B1N2/PPPP1PPP/RNBQK2R w KQkq - 4 1";
		String fen2 = "rnbqk2r/ppp1b1pp/5p1n/3p4/8/3B1N2/PPPP1PPP/RNBQ1RK1 b kq - 5 1";
		String fen3 = "rnbq1rk1/ppp1b1pp/5p1n/3p4/8/3B1N2/PPPP1PPP/RNBQ1RK1 w - - 6 2";

		Board board = new Board();

		board.loadFromFen(fen1);

		assertEquals(fen1, board.getFen());

		Move move;

		assertEquals(Side.WHITE, board.getSideToMove());
		move = new Move(Square.E1, Square.G1);
		assertTrue(board.doMove(move, true));
		System.out.println("new FEN after: " + move.toString() + ": " + board.getFen());
		System.out.println("hash code is: " + board.hashCode());
		assertEquals(fen2, board.getFen());

		assertEquals(Side.BLACK, board.getSideToMove());
		move = new Move(Square.E8, Square.G8);
		assertTrue(board.doMove(move, true));
		System.out.println("new FEN after: " + move.toString() + ": " + board.getFen());
		System.out.println("hash code is: " + board.hashCode());
		assertEquals(fen3, board.getFen());

		assertEquals(Side.WHITE, board.getSideToMove());

		// board.undoMove();
		// System.out.println("new FEN after: undo: " + board.getFen());
		// System.out.println("hash code is: " + board.hashCode());
		// assertEquals(fen2, board.getFen());
		// assertEquals(Side.BLACK, board.getSideToMove());

	}

	@Test
	public void testGenerateLegalMoves1() throws MoveGeneratorException {

		String fen1 = "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/5Q2/PPPBBPpP/RN2K2R w KQkq - 0 2";

		Board b1 = new Board();

		b1.loadFromFen(fen1);

		assertEquals(Side.WHITE, b1.getSideToMove());
		assertEquals(CastlingRight.KING_AND_QUEEN_SIDE, b1.getCastlingRight(Side.WHITE));
		assertEquals(CastlingRight.KING_AND_QUEEN_SIDE, b1.getCastlingRight(Side.BLACK));
		assertNull(b1.getEnPassantTarget());
		assertNull(b1.getEnPassant());
		assertEquals(0, b1.getHalfMoveCounter());
		assertEquals(2, b1.getMoveCounter());

		List<Move> expectedMoves = new LinkedList<>();

		expectedMoves.add(new Move(Square.B1, Square.A3));
		expectedMoves.add(new Move(Square.B1, Square.C3));

		expectedMoves.add(new Move(Square.E1, Square.D1));

		expectedMoves.add(new Move(Square.H1, Square.G1));
		expectedMoves.add(new Move(Square.H1, Square.F1));

		expectedMoves.add(new Move(Square.A2, Square.A3));
		expectedMoves.add(new Move(Square.A2, Square.A4));

		expectedMoves.add(new Move(Square.B2, Square.B3));

		expectedMoves.add(new Move(Square.C2, Square.C3));
		expectedMoves.add(new Move(Square.C2, Square.C4));

		expectedMoves.add(new Move(Square.D2, Square.B4));
		expectedMoves.add(new Move(Square.D2, Square.C3));
		expectedMoves.add(new Move(Square.D2, Square.C1));
		expectedMoves.add(new Move(Square.D2, Square.E3));
		expectedMoves.add(new Move(Square.D2, Square.F4));
		expectedMoves.add(new Move(Square.D2, Square.G5));
		expectedMoves.add(new Move(Square.D2, Square.H6));

		expectedMoves.add(new Move(Square.D5, Square.D6));
		expectedMoves.add(new Move(Square.D5, Square.E6));

		expectedMoves.add(new Move(Square.E2, Square.A6));
		expectedMoves.add(new Move(Square.E2, Square.B5));
		expectedMoves.add(new Move(Square.E2, Square.C4));
		expectedMoves.add(new Move(Square.E2, Square.D3));
		expectedMoves.add(new Move(Square.E2, Square.D1));
		expectedMoves.add(new Move(Square.E2, Square.F1));

		expectedMoves.add(new Move(Square.E5, Square.C6));
		expectedMoves.add(new Move(Square.E5, Square.C4));
		expectedMoves.add(new Move(Square.E5, Square.D7));
		expectedMoves.add(new Move(Square.E5, Square.D3));
		expectedMoves.add(new Move(Square.E5, Square.F7));
		expectedMoves.add(new Move(Square.E5, Square.G6));
		expectedMoves.add(new Move(Square.E5, Square.G4));

		expectedMoves.add(new Move(Square.F3, Square.A3));
		expectedMoves.add(new Move(Square.F3, Square.B3));
		expectedMoves.add(new Move(Square.F3, Square.C3));
		expectedMoves.add(new Move(Square.F3, Square.D3));
		expectedMoves.add(new Move(Square.F3, Square.E3));
		expectedMoves.add(new Move(Square.F3, Square.F6));
		expectedMoves.add(new Move(Square.F3, Square.F5));
		expectedMoves.add(new Move(Square.F3, Square.F4));
		expectedMoves.add(new Move(Square.F3, Square.G4));
		expectedMoves.add(new Move(Square.F3, Square.G3));
		expectedMoves.add(new Move(Square.F3, Square.G2));
		expectedMoves.add(new Move(Square.F3, Square.H5));
		expectedMoves.add(new Move(Square.F3, Square.H3));

		expectedMoves.add(new Move(Square.H2, Square.H3));
		expectedMoves.add(new Move(Square.H2, Square.H4));

		List<Move> moves = b1.generateLegalMoves();

		assertEquals(47, moves.size());
		assertEquals(expectedMoves.size(), moves.size());

		for (Move expectedMove : expectedMoves) {
			assertTrue(
				moves.contains(expectedMove),
				"moves does not contain expected move " + expectedMove
			);
		}

	}

	@Test
	public void testGenerateLegalMoves2() throws MoveGeneratorException {

		String fen = "1r6/3k2p1/7p/Ppp2r1P/K1N1B1p1/2P2NP1/b7/4b3 w - - 0 56";
		Board b = new Board();
		b.loadFromFen(fen);

		List<Move> moves = b.generateLegalMoves();

		assertEquals(new Move(Square.A4, Square.A3), moves.get(0));

	}


	@Test
	public void testGenerateLegalMoves3() throws MoveGeneratorException {

		String fen = "2r3r3/4n3/p1kp3p/1p3pP1/1p1bPPKP/1PPP4/BR1R4/8 w - - 0 73";
		Board b = new Board();
		b.loadFromFen(fen);

		List<Move> moves = b.generateLegalMoves();

		assertTrue(moves.contains(new Move(Square.E4, Square.F5)));
		assertTrue(moves.contains(new Move(Square.G4, Square.F3)));
		assertTrue(moves.contains(new Move(Square.G4, Square.G3)));
		assertTrue(moves.contains(new Move(Square.G4, Square.H3)));
		assertTrue(moves.contains(new Move(Square.G4, Square.H5)));

	}

	@Test
	public void testGenerateLegalMoves4() throws MoveGeneratorException {

		String fen = "7k/8/R5Q1/1BpP4/3K4/8/8/8 w - c6 0 0";
		Board b = new Board();
		b.loadFromFen(fen);

		List<Move> moves = b.generateLegalMoves();

		assertTrue(moves.contains(new Move(Square.D5, Square.C6)));
		assertTrue(moves.contains(new Move(Square.D4, Square.C3)));
		assertTrue(moves.contains(new Move(Square.D4, Square.D3)));
		assertTrue(moves.contains(new Move(Square.D4, Square.E3)));
		assertTrue(moves.contains(new Move(Square.D4, Square.C4)));
		assertTrue(moves.contains(new Move(Square.D4, Square.E4)));
		assertTrue(moves.contains(new Move(Square.D4, Square.C5)));
		assertTrue(moves.contains(new Move(Square.D4, Square.E5)));
		assertEquals(8, moves.size());

	}

	// @Test
	// public void testDraws() {
	//
	// 	Board b = new Board();
	// 	b.loadFromFen("rnbqkbnr/p1pppppp/8/8/1p2P3/1P6/P1PP1PPP/RNBQKBNR w KQkq - 0 1");
	//
	// 	b.doMove(new Move(Square.D1, Square.E2));
	// 	assertFalse(b.isDraw());
	//
	// 	b.doMove(new Move(Square.C8, Square.B7));
	// 	assertFalse(b.isDraw());
	//
	// 	b.doMove(new Move(Square.E2, Square.D1));
	// 	assertFalse(b.isDraw());
	//
	// 	b.doMove(new Move(Square.B7, Square.C8));
	// 	assertFalse(b.isDraw());
	//
	// 	b.doMove(new Move(Square.D1, Square.E2));
	// 	assertFalse(b.isDraw());
	//
	// 	b.doMove(new Move(Square.C8, Square.B7));
	// 	assertFalse(b.isDraw());
	//
	// 	b.doMove(new Move(Square.E2, Square.D1));
	// 	assertFalse(b.isDraw());
	//
	// 	b.doMove(new Move(Square.B7, Square.C8));
	// 	assertTrue(b.isDraw());
	//
	// 	b.loadFromFen("1kr5/8/Q7/8/8/7q/4r3/6K1 w - - 0 1");
	//
	// 	b.doMove(new Move(Square.A6, Square.B6));
	// 	assertFalse(b.isDraw());
	//
	// 	b.doMove(new Move(Square.B8, Square.A8));
	// 	assertFalse(b.isDraw());
	//
	// 	b.doMove(new Move(Square.B6, Square.A6));
	// 	assertFalse(b.isDraw());
	//
	// 	b.doMove(new Move(Square.A8, Square.B8));
	// 	assertFalse(b.isDraw());
	//
	// 	b.doMove(new Move(Square.A6, Square.B6));
	// 	assertFalse(b.isDraw());
	//
	// 	b.doMove(new Move(Square.B8, Square.A8));
	// 	assertFalse(b.isDraw());
	//
	// 	b.doMove(new Move(Square.B6, Square.A6));
	// 	assertFalse(b.isDraw());
	//
	// 	b.doMove(new Move(Square.A8, Square.B8));
	// 	assertTrue(b.isDraw());
	//
	// }

	@Test
	public void testCastling() {

		final Board board = new Board();

		board.loadFromFen("r1bqk1nr/pppp1ppp/2n5/2b1p3/4P3/5N2/PPPPBPPP/RNBQK2R w KQkq - 0 1");

		assertEquals(Side.WHITE, board.getSideToMove());
		assertEquals(CastlingRight.KING_AND_QUEEN_SIDE, board.getCastlingRight(Side.WHITE));
		assertEquals(CastlingRight.KING_AND_QUEEN_SIDE, board.getCastlingRight(Side.BLACK));
		assertNull(board.getEnPassantTarget());
		assertNull(board.getEnPassant());
		assertEquals(0, board.getHalfMoveCounter());
		assertEquals(1, board.getMoveCounter());

		assertTrue(board.doMove(new Move(Square.E1, Square.G1), true)); // castling

		assertEquals("r1bqk1nr/pppp1ppp/2n5/2b1p3/4P3/5N2/PPPPBPPP/RNBQ1RK1 b kq - 1 1", board.getFen());

	}

	// @Test
	// public void testInvalidCastling() {
	//
	// 	final Board board = new Board();
	// 	board.loadFromFen("8/5k2/8/8/8/8/5K2/4R3 w - - 0 1");
	//
	// 	final Move whiteRookMoveE1G1 = new Move("e1g1", Side.WHITE);
	// 	board.doMove(whiteRookMoveE1G1);
	// 	final MoveBackup moveBackup = board.getBackup().getLast();
	// 	assertFalse(moveBackup.isCastleMove());
	// 	assertNull(moveBackup.getRookCastleMove());
	// }

	// @Test
	// public void testBoardToString() throws MoveConversionException {
	//
	// 	// Creates a new chessboard in the standard initial position
	// 	Board board = new Board();
	//
	// 	board.doMove(new Move(Square.E2, Square.E4));
	// 	board.doMove(new Move(Square.B8, Square.C6));
	// 	board.doMove(new Move(Square.F1, Square.C4));
	//
	// 	//print the chessboard in a human-readable form
	// 	System.out.println(board.toString());
	//
	// 	final String expected =
	// 		"r.bqkbnr\n" +
	// 			"pppppppp\n" +
	// 			"..n.....\n" +
	// 			"........\n" +
	// 			"..B.P...\n" +
	// 			"........\n" +
	// 			"PPPP.PPP\n" +
	// 			"RNBQK.NR\n" +
	// 			"Side: BLACK";
	// 	assertEquals(expected, board.toString());
	// }

	// @Test
	// public void testToStringFromWhiteViewPoint() throws MoveConversionException {
	//
	// 	// Creates a new chessboard in the standard initial position
	// 	Board board = new Board();
	//
	// 	board.doMove("e4");
	// 	board.doMove("Nc6");
	// 	board.doMove("Bc4");
	//
	// 	final String expected =
	// 		"r.bqkbnr\n" +
	// 			"pppppppp\n" +
	// 			"..n.....\n" +
	// 			"........\n" +
	// 			"..B.P...\n" +
	// 			"........\n" +
	// 			"PPPP.PPP\n" +
	// 			"RNBQK.NR\n";
	// 	assertEquals(expected, board.toStringFromWhiteViewPoint());
	// 	assertEquals(expected, board.toStringFromViewPoint(Side.WHITE));
	// }

	// @Test
	// public void testToStringFromBlackViewPoint() throws MoveConversionException {
	//
	// 	// Creates a new chessboard in the standard initial position
	// 	Board board = new Board();
	//
	// 	board.doMove("e4");
	// 	board.doMove("Nc6");
	// 	board.doMove("Bc4");
	//
	// 	final String expected =
	// 		"RN.KQBNR\n" +
	// 			"PPP.PPPP\n" +
	// 			"........\n" +
	// 			"...P.B..\n" +
	// 			"........\n" +
	// 			".....n..\n" +
	// 			"pppppppp\n" +
	// 			"rnbkqb.r\n";
	// 	assertEquals(expected, board.toStringFromBlackViewPoint());
	// 	assertEquals(expected, board.toStringFromViewPoint(Side.BLACK));
	// }

	// @Test
	// public void testDoSanMove() {
	//
	// 	Board board = new Board();
	// 	board.loadFromFen("4k3/8/8/8/1b6/2N5/8/4K1N1 w - - 0 1");
	// 	board.doMove("Ne2");
	// 	assertEquals("4k3/8/8/8/1b6/2N5/4N3/4K3 b - - 1 1", board.getFen());
	// 	board.doMove("Bxc3");
	// 	assertEquals("4k3/8/8/8/8/2b5/4N3/4K3 w - - 0 2", board.getFen());
	// 	board.doMove("Nxc3");
	// 	assertEquals("4k3/8/8/8/8/2N5/8/4K3 b - - 0 2", board.getFen());
	// }

	// @Test
	// public void testDoSanMove2() {
	//
	// 	Board board = new Board();
	// 	board.doMove("e4");
	// 	assertEquals("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1", board.getFen());
	// }


}
