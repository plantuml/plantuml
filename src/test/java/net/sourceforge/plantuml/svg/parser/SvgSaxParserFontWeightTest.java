package net.sourceforge.plantuml.svg.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import net.sourceforge.plantuml.klimt.UShape;
import net.sourceforge.plantuml.klimt.awt.XColor;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColorSimple;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.AbstractCommonUGraphic;
import net.sourceforge.plantuml.klimt.drawing.debug.StringBounderDebug;
import net.sourceforge.plantuml.klimt.font.UFontFace;
import net.sourceforge.plantuml.klimt.shape.UText;

/**
 * Tests that {@link SvgSaxParser} correctly parses {@code font-weight} and
 * {@code font-style} SVG attributes into a {@link UFontFace} carrying the full
 * CSS weight (100–900), and that the weight is preserved on the drawn
 * {@link UText} shape.
 *
 * <p>Strategy: build minimal SVG fragments containing a {@code <text>} element
 * with specific font-weight attributes, parse with {@code SvgSaxParser}, call
 * {@code drawU} with a {@link RecordingUGraphic} that just records every shape
 * it is asked to draw, and assert the weight on the resulting {@link UFontFace}.
 */
class SvgSaxParserFontWeightTest {

	private static final HColorSimple FONT_COLOR = HColorSimple.create(XColor.BLACK);
	private static final HColorSimple FORCED_COLOR = null;

	/** Build a minimal SVG with a {@code <text>} element carrying the given attributes. */
	private static SvgSaxParser parserWithText(String fontWeightAttr, String fontStyleAttr) {
		final StringBuilder text = new StringBuilder();
		text.append("<text x=\"2\" y=\"10\" font-family=\"SansSerif\" font-size=\"12px\" fill=\"#000000\"");
		if (fontWeightAttr != null)
			text.append(" font-weight=\"").append(fontWeightAttr).append("\"");
		if (fontStyleAttr != null)
			text.append(" font-style=\"").append(fontStyleAttr).append("\"");
		text.append(">Label</text>");

		final String svg = "<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 100 20\">"
				+ text.toString()
				+ "</svg>";
		return new SvgSaxParser(svg);
	}

	/** Draws the parser, finds the first drawn UText shape, and returns its UFontFace. */
	private static UFontFace capturedFace(SvgSaxParser parser) {
		final RecordingUGraphic ug = new RecordingUGraphic();
		parser.drawU(ug, 1.0, FONT_COLOR, FORCED_COLOR);

		for (final UShape shape : ug.getShapes()) {
			if (shape instanceof UText) {
				return ((UText) shape).getFontConfiguration().getFontFace();
			}
		}
		throw new AssertionError("No UText shape was drawn");
	}

	// -----------------------------------------------------------------------
	// font-weight attribute parsing
	// -----------------------------------------------------------------------

	@Test
	void fontWeight700ProducesWeight700() {
		final UFontFace face = capturedFace(parserWithText("700", null));
		assertEquals(700, face.getCssWeight());
	}

	@Test
	void fontWeightBoldKeywordProducesWeight700() {
		final UFontFace face = capturedFace(parserWithText("bold", null));
		assertEquals(700, face.getCssWeight());
	}

	@Test
	void fontWeight500ProducesWeight500() {
		final UFontFace face = capturedFace(parserWithText("500", null));
		assertEquals(500, face.getCssWeight());
	}

	@Test
	void fontWeight300ProducesWeight300() {
		final UFontFace face = capturedFace(parserWithText("300", null));
		assertEquals(300, face.getCssWeight());
	}

	@Test
	void fontWeightLighterKeywordProducesWeight300() {
		final UFontFace face = capturedFace(parserWithText("lighter", null));
		assertEquals(300, face.getCssWeight());
	}

	@Test
	void fontWeightNormalKeywordProducesWeight400() {
		final UFontFace face = capturedFace(parserWithText("normal", null));
		assertEquals(400, face.getCssWeight());
	}

	@Test
	void missingFontWeightDefaultsTo400() {
		final UFontFace face = capturedFace(parserWithText(null, null));
		assertEquals(400, face.getCssWeight());
	}

	// -----------------------------------------------------------------------
	// font-style attribute parsing
	// -----------------------------------------------------------------------

	@Test
	void fontStyleItalicProducesItalicFace() {
		final UFontFace face = capturedFace(parserWithText(null, "italic"));
		assertTrue(face.isItalic());
	}

	@Test
	void fontStyleObliqueProducesItalicFace() {
		final UFontFace face = capturedFace(parserWithText(null, "oblique"));
		assertTrue(face.isItalic());
	}

	@Test
	void fontStyleNormalProducesNonItalicFace() {
		final UFontFace face = capturedFace(parserWithText(null, "normal"));
		assertFalse(face.isItalic());
	}

	@Test
	void missingFontStyleDefaultsToNonItalic() {
		final UFontFace face = capturedFace(parserWithText(null, null));
		assertFalse(face.isItalic());
	}

	// -----------------------------------------------------------------------
	// Combined weight + style
	// -----------------------------------------------------------------------

	@Test
	void weight600AndItalicAreIndependentAxes() {
		final UFontFace face = capturedFace(parserWithText("600", "italic"));
		assertEquals(600, face.getCssWeight());
		assertTrue(face.isItalic());
	}

	@Test
	void weight300NonItalicCombination() {
		final UFontFace face = capturedFace(parserWithText("300", "normal"));
		assertEquals(300, face.getCssWeight());
		assertFalse(face.isItalic());
	}

	// -----------------------------------------------------------------------
	// CSS output round-trip: weight survives to toCssWeightString()
	// -----------------------------------------------------------------------

	@ParameterizedTest(name = "weight {0} → toCssWeightString = ''{0}''")
	@CsvSource({"100", "200", "300", "400", "500", "600", "700", "800", "900"})
	void numericWeightRoundTrips(String cssWeight) {
		final UFontFace face = capturedFace(parserWithText(cssWeight, null));
		assertEquals(cssWeight, face.toCssWeightString());
	}

	// -----------------------------------------------------------------------
	// Test double: a real (if minimal) UGraphic implementation - not a mock -
	// that just records every shape it is asked to draw, the same
	// architectural family as UGraphicTxt/UGraphicNull.
	// -----------------------------------------------------------------------

	private static final class RecordingUGraphic extends AbstractCommonUGraphic {

		private final List<UShape> shapes = new ArrayList<>();

		RecordingUGraphic() {
			super(new StringBounderDebug());
			basicCopy(HColors.BLACK, ColorMapper.IDENTITY);
		}

		@Override
		protected AbstractCommonUGraphic copyUGraphic() {
			return this;
		}

		@Override
		public <SHAPE extends UShape> void draw(final SHAPE shape) {
			shapes.add(shape);
		}

		List<UShape> getShapes() {
			return shapes;
		}

		@Override
		public void writeToStream(final OutputStream os, final String metadata, final int dpi) throws IOException {
			// never called: this test never exports the drawing to a stream
		}

	}
}
