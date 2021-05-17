package cz.martinendler.chess.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringUtilsTest {

	@Test
	void leftPad() {
		assertEquals("  0", StringUtils.leftPad("0", " ", 3));
		assertEquals("   ", StringUtils.leftPad(null, " ", 3));
	}

}
