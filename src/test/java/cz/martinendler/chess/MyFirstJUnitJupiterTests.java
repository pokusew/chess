package cz.martinendler.chess;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

// see https://junit.org/junit5/docs/current/user-guide/#writing-tests
class MyFirstJUnitJupiterTests {

	// private final Calculator calculator = new Calculator();

	@Test
	void addition() {
		assertEquals(2, 2/* calculator.add(1, 1) */);
	}

}
