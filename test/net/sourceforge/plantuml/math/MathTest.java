package net.sourceforge.plantuml.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MathTest {
	@ParameterizedTest
	@CsvSource(value = {
			" f(x),                         {f{{\\left({x}\\right)}}} ",
			" x,                            {x} ",
			" color(red)(f(t))=color(blue)((a_0)/2 + sum_(n=1)^ooa_ncos((npit)/L)+sum_(n=1)^oo b_n\\ sin((npit)/L)), {\\textcolor{red}{{f{{\\left({t}\\right)}}}}}={\\textcolor{blue}{\\frac{{{a}_{{0}}}}{{2}}+{\\sum_{{{n}={1}}}^{\\infty}}{a}_{{n}}{\\cos{{\\left(\\frac{{{n}\\pi{t}}}{{L}}\\right)}}}+{\\sum_{{{n}={1}}}^{\\infty}}{b}_{{n}}\\ {\\sin{{\\left(\\frac{{{n}\\pi{t}}}{{L}}\\right)}}}}} ",
			" abs(x),                       {\\left|{{x}}\\right|} ",
			" y=ceil(x),                    {y}={\\left\\lceil{{x}}\\right\\rceil} ",
			" f(t)=(a_0)/2 + sum_(n=1)^ooa_ncos((npit)/L)+sum_(n=1)^oo b_n\\ sin((npit)/L), {f{{\\left({t}\\right)}}}=\\frac{{{a}_{{0}}}}{{2}}+{\\sum_{{{n}={1}}}^{\\infty}}{a}_{{n}}{\\cos{{\\left(\\frac{{{n}\\pi{t}}}{{L}}\\right)}}}+{\\sum_{{{n}={1}}}^{\\infty}}{b}_{{n}}\\ {\\sin{{\\left(\\frac{{{n}\\pi{t}}}{{L}}\\right)}}} ",
			" '[[a,b],[c,d]]',              {\\left[\\begin{matrix}{a}&{b}\\\\{c}&{d}\\end{matrix}\\right]} ",
			" color(red)(x),                {\\textcolor{red}{{x}}} ",
			" color(red)(t)=color(blue)(x), {\\textcolor{red}{{t}}}={\\textcolor{blue}{{x}}} ",
	})
	public void testMath(String input, String expected) {
		final String res = new ASCIIMathTeXImg().getTeX(input);
		assertEquals(expected, res);
	}
}
