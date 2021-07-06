package net.sourceforge.plantuml.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Math02Test {

	private final String math = "x";
	private final String expected = "{x}";

	@Test
	public void testJava() {
		final String res = new ASCIIMathTeXImg().getTeX(math);
		assertEquals(expected, res);
	}

}
