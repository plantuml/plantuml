package net.sourceforge.plantuml.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Math07Test {

	private final String math = "[[a,b],[c,d]]";
	private final String expected = "{\\left[\\begin{matrix}{a}&{b}\\\\{c}&{d}\\end{matrix}\\right]}";

	@Test
	public void testJava() {
		final String res = new ASCIIMathTeXImg().getTeX(math);
		assertEquals(expected, res);
	}

}
