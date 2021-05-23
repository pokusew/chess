package cz.martinendler.chess.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringUtilsTest {

	@Test
	void leftPad() {
		assertEquals("  0", StringUtils.leftPad("0", " ", 3));
		assertEquals("   ", StringUtils.leftPad(null, " ", 3));
	}

	@Test
	void formatTimeDuration() {

		assertEquals("10:00", StringUtils.formatTimeDuration(10 * 60 * 1000));
		assertEquals("10:00", StringUtils.formatTimeDuration(10 * 60 * 1000 + 256));
		assertEquals("04:28", StringUtils.formatTimeDuration(4 * 60 * 1000 + 28 * 1000 + 800));
		assertEquals("00:00", StringUtils.formatTimeDuration(0));
		assertEquals("00:00", StringUtils.formatTimeDuration(-1));

	}
}
