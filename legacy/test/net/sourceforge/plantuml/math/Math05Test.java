package net.sourceforge.plantuml.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Math05Test {

	private final String math = "y=ceil(x)";
	private final String expected = "{y}={\\left\\lceil{{x}}\\right\\rceil}";

	@Test
	public void testJava() {
		final String res = new ASCIIMathTeXImg().getTeX(math);
		assertEquals(expected, res);
	}

}
