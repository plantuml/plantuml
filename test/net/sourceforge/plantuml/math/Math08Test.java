package net.sourceforge.plantuml.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Math08Test {

	private final String math = "color(red)(x)";
	private final String expected = "{\\textcolor{red}{{x}}}";

	@Test
	public void testJava() {
		final String res = new ASCIIMathTeXImg().getTeX(math);
		assertEquals(expected, res);
	}

}
