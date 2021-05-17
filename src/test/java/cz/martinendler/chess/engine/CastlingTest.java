package cz.martinendler.chess.engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CastlingTest {

	@Test
	void testFromNotation() {

		assertEquals(Castling.KING_SIDE, Castling.fromNotation("O-O"));
		assertEquals(Castling.QUEEN_SIDE, Castling.fromNotation("O-O-O"));


		assertNull(Castling.fromNotation("x"));

	}
}
