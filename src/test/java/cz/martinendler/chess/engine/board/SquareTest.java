package cz.martinendler.chess.engine.board;

import cz.martinendler.chess.utils.StringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SquareTest {

	@Test
	public void testEndianness() {

		for (Square square : Square.values()) {
			System.out.printf(
				"square %s%s: (%d)%n",
				square.getFile().getNotation(), square.getRank().getNotation(), square.ordinal()
			);
			System.out.printf(
				"0b%s%n",
				StringUtils.leftPad(Long.toBinaryString(square.getBitboard()), "0", 64)
			);
			System.out.println(Bitboard.bbToPrettyString(square.getBitboard(), true));
		}

	}

}
