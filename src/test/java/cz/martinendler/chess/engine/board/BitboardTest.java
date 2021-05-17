package cz.martinendler.chess.engine.board;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
	public void testFileBB() {

		String[] expectedBitboards = new String[]{
			// file A
			"""
			.   A B C D E F G H   .
			8 | 1 0 0 0 0 0 0 0 | 8
			7 | 1 0 0 0 0 0 0 0 | 7
			6 | 1 0 0 0 0 0 0 0 | 6
			5 | 1 0 0 0 0 0 0 0 | 5
			4 | 1 0 0 0 0 0 0 0 | 4
			3 | 1 0 0 0 0 0 0 0 | 3
			2 | 1 0 0 0 0 0 0 0 | 2
			1 | 1 0 0 0 0 0 0 0 | 1
			.   A B C D E F G H   .
			""",
			// file B
			"""
			.   A B C D E F G H   .
			8 | 0 1 0 0 0 0 0 0 | 8
			7 | 0 1 0 0 0 0 0 0 | 7
			6 | 0 1 0 0 0 0 0 0 | 6
			5 | 0 1 0 0 0 0 0 0 | 5
			4 | 0 1 0 0 0 0 0 0 | 4
			3 | 0 1 0 0 0 0 0 0 | 3
			2 | 0 1 0 0 0 0 0 0 | 2
			1 | 0 1 0 0 0 0 0 0 | 1
			.   A B C D E F G H   .
			""",
			// file C
			"""
			.   A B C D E F G H   .
			8 | 0 0 1 0 0 0 0 0 | 8
			7 | 0 0 1 0 0 0 0 0 | 7
			6 | 0 0 1 0 0 0 0 0 | 6
			5 | 0 0 1 0 0 0 0 0 | 5
			4 | 0 0 1 0 0 0 0 0 | 4
			3 | 0 0 1 0 0 0 0 0 | 3
			2 | 0 0 1 0 0 0 0 0 | 2
			1 | 0 0 1 0 0 0 0 0 | 1
			.   A B C D E F G H   .
			""",
			// file D
			"""
			.   A B C D E F G H   .
			8 | 0 0 0 1 0 0 0 0 | 8
			7 | 0 0 0 1 0 0 0 0 | 7
			6 | 0 0 0 1 0 0 0 0 | 6
			5 | 0 0 0 1 0 0 0 0 | 5
			4 | 0 0 0 1 0 0 0 0 | 4
			3 | 0 0 0 1 0 0 0 0 | 3
			2 | 0 0 0 1 0 0 0 0 | 2
			1 | 0 0 0 1 0 0 0 0 | 1
			.   A B C D E F G H   .
			""",
			// file E
			"""
			.   A B C D E F G H   .
			8 | 0 0 0 0 1 0 0 0 | 8
			7 | 0 0 0 0 1 0 0 0 | 7
			6 | 0 0 0 0 1 0 0 0 | 6
			5 | 0 0 0 0 1 0 0 0 | 5
			4 | 0 0 0 0 1 0 0 0 | 4
			3 | 0 0 0 0 1 0 0 0 | 3
			2 | 0 0 0 0 1 0 0 0 | 2
			1 | 0 0 0 0 1 0 0 0 | 1
			.   A B C D E F G H   .
			""",
			// file F
			"""
			.   A B C D E F G H   .
			8 | 0 0 0 0 0 1 0 0 | 8
			7 | 0 0 0 0 0 1 0 0 | 7
			6 | 0 0 0 0 0 1 0 0 | 6
			5 | 0 0 0 0 0 1 0 0 | 5
			4 | 0 0 0 0 0 1 0 0 | 4
			3 | 0 0 0 0 0 1 0 0 | 3
			2 | 0 0 0 0 0 1 0 0 | 2
			1 | 0 0 0 0 0 1 0 0 | 1
			.   A B C D E F G H   .
			""",
			// file G
			"""
			.   A B C D E F G H   .
			8 | 0 0 0 0 0 0 1 0 | 8
			7 | 0 0 0 0 0 0 1 0 | 7
			6 | 0 0 0 0 0 0 1 0 | 6
			5 | 0 0 0 0 0 0 1 0 | 5
			4 | 0 0 0 0 0 0 1 0 | 4
			3 | 0 0 0 0 0 0 1 0 | 3
			2 | 0 0 0 0 0 0 1 0 | 2
			1 | 0 0 0 0 0 0 1 0 | 1
			.   A B C D E F G H   .
			""",
			// file H
			"""
			.   A B C D E F G H   .
			8 | 0 0 0 0 0 0 0 1 | 8
			7 | 0 0 0 0 0 0 0 1 | 7
			6 | 0 0 0 0 0 0 0 1 | 6
			5 | 0 0 0 0 0 0 0 1 | 5
			4 | 0 0 0 0 0 0 0 1 | 4
			3 | 0 0 0 0 0 0 0 1 | 3
			2 | 0 0 0 0 0 0 0 1 | 2
			1 | 0 0 0 0 0 0 0 1 | 1
			.   A B C D E F G H   .
			""",
		};

		for (int i = 0; i < Bitboard.fileBB.length; i++) {
			assertEquals(
				expectedBitboards[i],
				Bitboard.bbToPrettyString(Bitboard.fileBB[i], true)
			);
		}
	}

	@Test
	public void testDiagonalH1A8BB() {
		assertEquals(
			"""
				.   A B C D E F G H   .
				8 | 0 0 0 0 0 0 0 0 | 8
				7 | 1 0 0 0 0 0 0 0 | 7
				6 | 0 1 0 0 0 0 0 0 | 6
				5 | 0 0 1 0 0 0 0 0 | 5
				4 | 0 0 0 1 0 0 0 0 | 4
				3 | 0 0 0 0 1 0 0 0 | 3
				2 | 0 0 0 0 0 1 0 0 | 2
				1 | 0 0 0 0 0 0 1 0 | 1
				.   A B C D E F G H   .
				""",
			Bitboard.bbToPrettyString(Bitboard.diagonalH1A8BB[6], true)
		);
	}

	@Test
	void testPawnAttacks() {

		for (int i = 0; i < Bitboard.blackPawnAttacks.length; i++) {
			System.out.printf(
				"Bitboard.blackPawnAttacks[%d]:%n%s%n", i,
				Bitboard.bbToPrettyString(Bitboard.blackPawnAttacks[i])
			);
		}

	}

	@Test
	void testKnightAttacks() {

		for (int i = 0; i < Bitboard.knightAttacks.length; i++) {
			System.out.printf(
				"Bitboard.knightAttacks[%d]:%n%s%n", i,
				Bitboard.bbToPrettyString(Bitboard.knightAttacks[i])
			);
		}

	}

	@Test
	void experiment() {
		// for (int i = 0; i < Bitboard.diagH1A8Attacks.length; i++) {
		// 	System.out.printf(
		// 		"Bitboard.diagH1A8Attacks[%d]:%n%s%n", i,
		// 		Bitboard.bbToPrettyString(Bitboard.diagH1A8Attacks[i])
		// 	);
		// }
		System.out.println("Bitboard.bbTable[28][18] = ");
		System.out.println(Bitboard.bbToPrettyString(Bitboard.bitsBetween[18][18]));
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

	@Test
	void testHasOnly1Bit() {

		for (int i = 0; i < 64; i++) {
			long value = 1L << i;
			assertTrue(
				Bitboard.hasOnly1Bit(value),
				"a long with one bit: " + Long.toBinaryString(value)
			);
		}

		assertFalse(Bitboard.hasOnly1Bit(
			0b00000001_00000000_00000000_00000001_00000000_00000000_00000000_00000000L
		));

		assertFalse(Bitboard.hasOnly1Bit(
			0L
		));

	}

	@Test
	void testGetBBTable() {

		for (Square sq : Square.values()) {

			assertEquals(sq.getBitboard(), Bitboard.getBBTable(sq));

		}

	}

	@Test
	void testExtractLSB() {

		long[][] testcases = new long[][]{
			{
				/*  IN: */ (1L << 10) | (1L << 20),
				/* OUT: */ (1L << 20),
			},
			{
				/*  IN: */ 0b1000_0000_0000_0010_0000_0100_0000_0010_0010_0100_0010_0000_0000_0000_0000_1000L,
				/* OUT: */ 0b1000_0000_0000_0010_0000_0100_0000_0010_0010_0100_0010_0000_0000_0000_0000_0000L,
			},
			{
				/*  IN: */ 0L,
				/* OUT: */ 0L,
			},
		};

		for (long[] testcase : testcases) {
			assertEquals(testcase[1], Bitboard.removeLSB(testcase[0]));
		}

	}

}
