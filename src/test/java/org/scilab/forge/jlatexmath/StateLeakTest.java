package org.scilab.forge.jlatexmath;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * A formula must not change the formulas that are made after it. The symbols
 * are shared (SymbolAtom.get), and the macros that change an atom (\limits,
 * \nolimits, \normal) used to change the shared one: after a single
 * \sum\nolimits, every \sum of the JVM had its limits as scripts, so the result
 * of a formula depended on what had been drawn before (a server, a test suite).
 */
class StateLeakTest {

	private static String size(String tex) {
		final TeXIcon icon = new TeXFormula(tex).new TeXIconBuilder().setStyle(TeXConstants.STYLE_DISPLAY).setSize(20)
				.build();
		return icon.getIconWidth() + "x" + icon.getIconHeight();
	}

	@Test
	void testNolimitsDoesNotChangeTheNextSum() {
		final String sum = "\\sum_{i=1}^{n} i";
		final String before = size(sum);
		final String nolimits = size("\\sum\\nolimits_{i=1}^{n} i");
		assertEquals(before, size(sum), "after \\nolimits");
		// and \nolimits did something: the limits are scripts, so it is wider and lower
		assertEquals(false, before.equals(nolimits), before + " " + nolimits);
	}

	@Test
	void testLimitsDoesNotChangeTheNextIntegral() {
		final String integral = "\\int_0^1 x";
		final String before = size(integral);
		final String limits = size("\\int\\limits_0^1 x");
		assertEquals(before, size(integral), "after \\limits");
		assertEquals(false, before.equals(limits), before + " " + limits);
	}

	@Test
	void testNormalDoesNotChangeTheNextOperator() {
		final String sum = "\\sum_{i=1}^{n} i";
		final String before = size(sum);
		size("\\sum\\normal_{i=1}^{n} i");
		assertEquals(before, size(sum), "after \\normal");
	}

	@Test
	void testInOneFormula() {
		// the same operator twice in one formula, with a macro between: same size each time
		final String one = size("\\sum_{i=1}^{n} i");
		final String both = size("\\sum_{i=1}^{n} i \\quad \\sum\\nolimits_{i=1}^{n} i \\quad \\sum_{i=1}^{n} i");
		final String plain = size("\\sum_{i=1}^{n} i \\quad \\sum_{i=1}^{n} i");
		// the middle one is lower and wider: the whole is not the sum of the plain ones, but the last
		// \sum must be the plain one, so the height is the one of the plain ones
		assertEquals(one.substring(one.indexOf('x')), both.substring(both.indexOf('x')));
		assertEquals(one.substring(one.indexOf('x')), plain.substring(plain.indexOf('x')));
	}

	@Test
	void testTextModeUnicodeDoesNotStick() {
		// setUnicode changed the shared symbol too
		final String before = size("\\text{x \u00e9 y} \\acute{e}");
		size("\\text{\u00e9\u00e8\u00ea\u00e0}");
		assertEquals(before, size("\\text{x \u00e9 y} \\acute{e}"));
	}

}
