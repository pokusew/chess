package cz.martinendler.chess.engine.board;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BitboardTest {

	@Test
	public void testLightSquaresBB() {
		assertEquals(
			"""
				.   A B C D E F G H   .
				8 | 1 0 1 0 1 0 1 0 | 8
				7 | 0 1 0 1 0 1 0 1 | 7
				6 | 1 0 1 0 1 0 1 0 | 6
				5 | 0 1 0 1 0 1 0 1 | 5
				4 | 1 0 1 0 1 0 1 0 | 4
				3 | 0 1 0 1 0 1 0 1 | 3
				2 | 1 0 1 0 1 0 1 0 | 2
				1 | 0 1 0 1 0 1 0 1 | 1
				.   A B C D E F G H   .
				""",
			Bitboard.bbToPrettyString(Bitboard.lightSquaresBB, true)
		);
	}

	@Test
	public void testDarkSquaresBB() {
		assertEquals(
			"""
				.   A B C D E F G H   .
				8 | 0 1 0 1 0 1 0 1 | 8
				7 | 1 0 1 0 1 0 1 0 | 7
				6 | 0 1 0 1 0 1 0 1 | 6
				5 | 1 0 1 0 1 0 1 0 | 5
				4 | 0 1 0 1 0 1 0 1 | 4
				3 | 1 0 1 0 1 0 1 0 | 3
				2 | 0 1 0 1 0 1 0 1 | 2
				1 | 1 0 1 0 1 0 1 0 | 1
				.   A B C D E F G H   .
				""",
			Bitboard.bbToPrettyString(Bitboard.darkSquaresBB, true)
		);
	}

	@Test
	public void testRankBB() {

		for (int i = 0; i < Bitboard.fileBB.length; i++) {
			System.out.println(Bitboard.bbToPrettyString(Bitboard.fileBB[i], true));
		}
	}

	/**
	 * Test bitboard functions
	 */
	@Test
	public void testBBFunctions() {

		for (int x = 0; x < 64; x++) {
			assertEquals(Bitboard.bitScanForward(1L << x), x);
			assertEquals(Bitboard.bitScanReverse(1L << x), x);
		}

		long t = (1L << 10) | (1L << 20);
		long lsb = Bitboard.extractLSB(t);

		assertEquals(1L << 20, lsb);

		lsb = Bitboard.extractLSB(0L);

		assertEquals(0L, lsb);

		long ba = Bitboard.getBishopAttacks(0L, Square.D5);

		assertEquals(
			Bitboard.bbToString(ba),
			"""
				00000001
				10000010
				01000100
				00101000
				00000000
				00101000
				01000100
				10000010
				"""
		);

		long ra = Bitboard.getRookAttacks(0L, Square.D5);

		assertEquals(
			Bitboard.bbToString(ra),
			"""
				00010000
				00010000
				00010000
				00010000
				11101111
				00010000
				00010000
				00010000
				"""
		);

	}

}
