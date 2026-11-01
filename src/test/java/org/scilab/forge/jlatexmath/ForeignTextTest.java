package org.scilab.forge.jlatexmath;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * The text that has no TeX glyph (CJK, ...) is measured by a TeXTextMeasurer and
 * drawn by the canvas.
 */
class ForeignTextTest {

	@BeforeAll
	static void resources() {
		LibraryResources.install();
	}

	private static final TeXFormula.TeXIconBuilder builder(String tex) {
		return new TeXFormula(tex).new TeXIconBuilder().setStyle(TeXConstants.STYLE_DISPLAY).setSize(20);
	}

	/** One em wide per character, 0.8 above the baseline and 0.2 below. */
	private static final class OneEmPerChar implements TeXTextMeasurer {
		final List<String> measured = new ArrayList<String>();

		public float[] measure(String text) {
			measured.add(text);
			return new float[] { text.length(), 0.8f, 0.2f };
		}
	}

	/** Records what is drawn. */
	private static final class Drawn extends NoopGraphics {
		final List<String> texts = new ArrayList<String>();
		final List<Double> sizes = new ArrayList<Double>();

		@Override
		public void drawText(String text, double fontSize) {
			texts.add(text);
			sizes.add(fontSize);
		}
	}

	@Test
	void testTheMeasurerGivesTheWidth() {
		final OneEmPerChar measurer = new OneEmPerChar();
		final int one = builder("\u4e2d").setTextMeasurer(measurer).build().getIconWidth();
		final int three = builder("\u4e2d\u6587\u5b57").setTextMeasurer(measurer).build().getIconWidth();
		// 3 characters, one run: 3 em instead of 1, so 2 em (40 pixels at the size 20) wider
		assertEquals(40.0, three - one, 2.0);
		assertTrue(one >= 20, "one em is the size of the formula: " + one);
	}

	@Test
	void testARunIsOneMeasureAndOneText() {
		final OneEmPerChar measurer = new OneEmPerChar();
		final TeXIcon icon = builder("a \u4e2d\u6587 b").setTextMeasurer(measurer).build();
		assertEquals(1, measurer.measured.size());
		assertEquals("\u4e2d\u6587", measurer.measured.get(0));

		final Drawn canvas = new Drawn();
		icon.paint(canvas, TeXColor.BLACK, 0, 0);
		assertEquals(1, canvas.texts.size());
		assertEquals("\u4e2d\u6587", canvas.texts.get(0));
	}

	@Test
	void testTwoRunsAreSeparatedByATeXCharacter() {
		final OneEmPerChar measurer = new OneEmPerChar();
		builder("\u4e2d+\u6587").setTextMeasurer(measurer).build();
		assertEquals(2, measurer.measured.size());
	}

	@Test
	void testTextInTheScriptOfAFractionIsSmaller() {
		final Drawn canvas = new Drawn();
		builder("\\frac{\u4e2d}{x^{\u6587}}").setTextMeasurer(new OneEmPerChar()).build().paint(canvas,
				TeXColor.BLACK, 0, 0);
		assertEquals(2, canvas.texts.size());
		final double numerator = canvas.sizes.get(0);
		final double script = canvas.sizes.get(1);
		assertTrue(script < numerator, numerator + " " + script);
	}

	@Test
	void testWithoutAMeasurerTheSizeIsEstimated() {
		// no measurer: a wide character is one em, so 3 of them are 2 em (40 pixels) wider than one
		final int one = builder("\u4e2d").build().getIconWidth();
		final int three = builder("\u4e2d\u6587\u5b57").build().getIconWidth();
		assertEquals(40.0, three - one, 2.0);
	}

	@Test
	void testAControlCharacterIsStillAnError() {
		assertThrows(ParseException.class, () -> new TeXFormula("a\u0001b"));
	}

	@Test
	void testAlphabetsWithFontsAreNotForeign() {
		// Greek and Cyrillic have TeX fonts: no measure is asked for them
		final OneEmPerChar measurer = new OneEmPerChar();
		builder("\\text{\u041f\u0440\u0438\u0432\u0435\u0442 \u03b1\u03b2\u03b3}").setTextMeasurer(measurer).build();
		assertTrue(measurer.measured.isEmpty(), measurer.measured.toString());
	}

}
