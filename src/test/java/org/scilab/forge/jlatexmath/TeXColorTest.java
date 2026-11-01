package org.scilab.forge.jlatexmath;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;

import org.junit.jupiter.api.Test;

/** TeXColor replaces java.awt.Color in the library: same values, same exceptions. */
class TeXColorTest {

	private static void assertSame(Color awt, TeXColor color) {
		assertEquals(awt.getRed(), color.getRed());
		assertEquals(awt.getGreen(), color.getGreen());
		assertEquals(awt.getBlue(), color.getBlue());
		assertEquals(awt.getAlpha(), color.getAlpha());
	}

	@Test
	void testConstants() {
		assertSame(Color.BLACK, TeXColor.BLACK);
		assertSame(Color.WHITE, TeXColor.WHITE);
		assertSame(Color.RED, TeXColor.RED);
		assertSame(Color.GREEN, TeXColor.GREEN);
		assertSame(Color.BLUE, TeXColor.BLUE);
		assertSame(Color.CYAN, TeXColor.CYAN);
		assertSame(Color.MAGENTA, TeXColor.MAGENTA);
		assertSame(Color.YELLOW, TeXColor.YELLOW);
	}

	@Test
	void testIntegers() {
		assertSame(new Color(102, 102, 102), new TeXColor(102, 102, 102));
		assertSame(new Color(153, 153, 255, 7), new TeXColor(153, 153, 255, 7));
	}

	@Test
	void testFloatsAreRoundedAsAwtDoes() {
		for (float v = 0f; v <= 1.0001f; v += 0.0137f) {
			final float c = Math.min(1f, v);
			assertSame(new Color(c, 1f - c, c / 3), new TeXColor(c, 1f - c, c / 3));
			assertSame(new Color(c, c, c, 0.5f), new TeXColor(c, c, c, 0.5f));
		}
	}

	@Test
	void testDecode() {
		assertSame(Color.decode("#FF8800"), TeXColor.decode("#FF8800"));
		assertSame(Color.decode("0x0a0B0c"), TeXColor.decode("0x0a0B0c"));
		assertSame(Color.decode("255"), TeXColor.decode("255"));
		assertThrows(NumberFormatException.class, () -> TeXColor.decode("#GG0000"));
		assertThrows(NumberFormatException.class, () -> TeXColor.decode("red"));
	}

	@Test
	void testOutOfRangeIsAnError() {
		assertThrows(IllegalArgumentException.class, () -> new TeXColor(256, 0, 0));
		assertThrows(IllegalArgumentException.class, () -> new TeXColor(0, -1, 0));
		assertThrows(IllegalArgumentException.class, () -> new TeXColor(0f, 0f, 1.5f));
		assertThrows(IllegalArgumentException.class, () -> new TeXColor(0f, 0f, 0f, -0.1f));
	}

	@Test
	void testEquality() {
		assertEquals(new TeXColor(1, 2, 3), new TeXColor(1, 2, 3));
		assertEquals(new TeXColor(1, 2, 3).hashCode(), new TeXColor(1, 2, 3).hashCode());
		assertFalse(new TeXColor(1, 2, 3).equals(new TeXColor(1, 2, 4)));
		assertFalse(new TeXColor(1, 2, 3).equals(new TeXColor(1, 2, 3, 100)));
		assertTrue(new TeXColor(1, 2, 3).toString().contains("r=1"));
	}

	@Test
	void testTheColorsOfTheMacros() {
		// \definecolor and \textcolor build colors with floats and integers
		final TeXFormula formula = new TeXFormula(
				"\\definecolor{zq1}{rgb}{0.2,0.4,0.6}\\definecolor{zq2}{cmyk}{0.1,0.2,0.3,0.4}\\definecolor{zq3}{gray}{0.5}"
						+ "\\textcolor{zq1}{a}\\textcolor{zq2}{b}\\textcolor{zq3}{c}\\textcolor{#336699}{d}\\textcolor{red}{e}");
		assertSame(new Color(0.2f, 0.4f, 0.6f), ColorAtom.Colors.get("zq1"));
		assertSame(new Color(0.5f, 0.5f, 0.5f), ColorAtom.Colors.get("zq3"));
		assertSame(Color.decode("#336699"), ColorAtom.getColor("#336699"));
		assertTrue(formula.root != null);
	}

}
