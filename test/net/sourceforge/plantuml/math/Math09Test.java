package net.sourceforge.plantuml.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Math09Test {

	private final String math = "color(red)(t)=color(blue)(x)";
	private final String expected = "{\\textcolor{red}{{t}}}={\\textcolor{blue}{{x}}}";

	@Test
	public void testJava() {
		final String res = new ASCIIMathTeXImg().getTeX(math);
		assertEquals(expected, res);
	}

}
