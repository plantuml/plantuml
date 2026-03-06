package net.sourceforge.plantuml.svg.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;

import net.sourceforge.plantuml.klimt.UShape;
import net.sourceforge.plantuml.klimt.awt.XColor;
import net.sourceforge.plantuml.klimt.color.HColorSimple;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
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
 * {@code drawU} with a mock {@link UGraphic}, capture the drawn shape, and assert
 * the weight on the resulting {@link UFontFace}.
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
		return new SvgSaxParser(Arrays.asList(svg));
	}

	/** Draws the parser, captures the first UText argument passed to draw(), and returns its UFontFace. */
	private static UFontFace capturedFace(SvgSaxParser parser) {
		final UGraphic ug = mock(UGraphic.class, org.mockito.Mockito.RETURNS_SELF);
		parser.drawU(ug, 1.0, FONT_COLOR, FORCED_COLOR);

		final ArgumentCaptor<UShape> captor = ArgumentCaptor.forClass(UShape.class);
		verify(ug, atLeastOnce()).draw(captor.capture());

		final List<UShape> shapes = captor.getAllValues();
		for (UShape shape : shapes) {
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
		assertThat(face.getCssWeight()).isEqualTo(700);
	}

	@Test
	void fontWeightBoldKeywordProducesWeight700() {
		final UFontFace face = capturedFace(parserWithText("bold", null));
		assertThat(face.getCssWeight()).isEqualTo(700);
	}

	@Test
	void fontWeight500ProducesWeight500() {
		final UFontFace face = capturedFace(parserWithText("500", null));
		assertThat(face.getCssWeight()).isEqualTo(500);
	}

	@Test
	void fontWeight300ProducesWeight300() {
		final UFontFace face = capturedFace(parserWithText("300", null));
		assertThat(face.getCssWeight()).isEqualTo(300);
	}

	@Test
	void fontWeightLighterKeywordProducesWeight300() {
		final UFontFace face = capturedFace(parserWithText("lighter", null));
		assertThat(face.getCssWeight()).isEqualTo(300);
	}

	@Test
	void fontWeightNormalKeywordProducesWeight400() {
		final UFontFace face = capturedFace(parserWithText("normal", null));
		assertThat(face.getCssWeight()).isEqualTo(400);
	}

	@Test
	void missingFontWeightDefaultsTo400() {
		final UFontFace face = capturedFace(parserWithText(null, null));
		assertThat(face.getCssWeight()).isEqualTo(400);
	}

	// -----------------------------------------------------------------------
	// font-style attribute parsing
	// -----------------------------------------------------------------------

	@Test
	void fontStyleItalicProducesItalicFace() {
		final UFontFace face = capturedFace(parserWithText(null, "italic"));
		assertThat(face.isItalic()).isTrue();
	}

	@Test
	void fontStyleObliqueProducesItalicFace() {
		final UFontFace face = capturedFace(parserWithText(null, "oblique"));
		assertThat(face.isItalic()).isTrue();
	}

	@Test
	void fontStyleNormalProducesNonItalicFace() {
		final UFontFace face = capturedFace(parserWithText(null, "normal"));
		assertThat(face.isItalic()).isFalse();
	}

	@Test
	void missingFontStyleDefaultsToNonItalic() {
		final UFontFace face = capturedFace(parserWithText(null, null));
		assertThat(face.isItalic()).isFalse();
	}

	// -----------------------------------------------------------------------
	// Combined weight + style
	// -----------------------------------------------------------------------

	@Test
	void weight600AndItalicAreIndependentAxes() {
		final UFontFace face = capturedFace(parserWithText("600", "italic"));
		assertThat(face.getCssWeight()).isEqualTo(600);
		assertThat(face.isItalic()).isTrue();
	}

	@Test
	void weight300NonItalicCombination() {
		final UFontFace face = capturedFace(parserWithText("300", "normal"));
		assertThat(face.getCssWeight()).isEqualTo(300);
		assertThat(face.isItalic()).isFalse();
	}

	// -----------------------------------------------------------------------
	// CSS output round-trip: weight survives to toCssWeightString()
	// -----------------------------------------------------------------------

	@ParameterizedTest(name = "weight {0} → toCssWeightString = ''{0}''")
	@CsvSource({"100", "200", "300", "400", "500", "600", "700", "800", "900"})
	void numericWeightRoundTrips(String cssWeight) {
		final UFontFace face = capturedFace(parserWithText(cssWeight, null));
		assertThat(face.toCssWeightString()).isEqualTo(cssWeight);
	}
}
