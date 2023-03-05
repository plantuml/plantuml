package net.sourceforge.plantuml.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Math03Test {

	private final String math = "color(red)(f(t))=color(blue)((a_0)/2 + sum_(n=1)^ooa_ncos((npit)/L)+sum_(n=1)^oo b_n\\ sin((npit)/L))";
	private final String expected = "{\\textcolor{red}{{f{{\\left({t}\\right)}}}}}={\\textcolor{blue}{\\frac{{{a}_{{0}}}}{{2}}+{\\sum_{{{n}={1}}}^{\\infty}}{a}_{{n}}{\\cos{{\\left(\\frac{{{n}\\pi{t}}}{{L}}\\right)}}}+{\\sum_{{{n}={1}}}^{\\infty}}{b}_{{n}}\\ {\\sin{{\\left(\\frac{{{n}\\pi{t}}}{{L}}\\right)}}}}}";

	@Test
	public void testJava() {
		final String res = new ASCIIMathTeXImg().getTeX(math);
		assertEquals(expected, res);
	}

}
