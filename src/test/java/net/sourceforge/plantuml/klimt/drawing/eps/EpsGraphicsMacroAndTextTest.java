package net.sourceforge.plantuml.klimt.drawing.eps;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.UFontFactory;
import net.sourceforge.plantuml.klimt.font.UFontFace;
import net.sourceforge.plantuml.klimt.font.UFont;

/**
 * Minimal unit tests for {@link EpsGraphicsMacroAndText#drawText}.
 */
class EpsGraphicsMacroAndTextTest {

	private static FontConfiguration fc(UFont font) {
		return FontConfiguration.blackBlueTrue(font);
	}

	private static UFont sansSerif(int size) {
		return UFontFactory.build("SansSerif", UFontFace.normal(), size);
	}

	// -----------------------------------------------------------------------
	// drawText — basic PostScript output
	// -----------------------------------------------------------------------

	@Test
	void drawTextProducesMoveto() {
		final EpsGraphicsMacroAndText eps = new EpsGraphicsMacroAndText();
		eps.drawText("Hello", fc(sansSerif(12)), 100, 200);

		final String code = eps.getEPSCode();
		assertTrue(code.contains("moveto"), "Expected moveto command in EPS output");
	}

	@Test
	void drawTextProducesShow() {
		final EpsGraphicsMacroAndText eps = new EpsGraphicsMacroAndText();
		eps.drawText("Hello", fc(sansSerif(12)), 0, 0);

		final String code = eps.getEPSCode();
		assertTrue(code.contains("(Hello) show"), "Expected (Hello) show in EPS output");
	}

	@Test
	void drawTextSetsFontAndSize() {
		final EpsGraphicsMacroAndText eps = new EpsGraphicsMacroAndText();
		eps.drawText("A", fc(sansSerif(14)), 0, 0);

		final String code = eps.getEPSCode();
		assertTrue(code.contains("findfont"), "Expected findfont in EPS output");
		assertTrue(code.contains("14 scalefont"), "Expected 14 scalefont in EPS output");
		assertTrue(code.contains("setfont"), "Expected setfont in EPS output");
	}

	// -----------------------------------------------------------------------
	// drawText — special character escaping
	// -----------------------------------------------------------------------

	@Test
	void drawTextEscapesBackslash() {
		final EpsGraphicsMacroAndText eps = new EpsGraphicsMacroAndText();
		eps.drawText("a\\b", fc(sansSerif(12)), 0, 0);

		final String code = eps.getEPSCode();
		assertTrue(code.contains("(a\\\\b) show"), "Backslash should be escaped to \\\\");
	}

	@Test
	void drawTextEscapesParentheses() {
		final EpsGraphicsMacroAndText eps = new EpsGraphicsMacroAndText();
		eps.drawText("f(x)", fc(sansSerif(12)), 0, 0);

		final String code = eps.getEPSCode();
		assertTrue(code.contains("(f\\(x\\)) show"), "Parentheses should be escaped");
	}

	@Test
	void drawTextReplacesControlCharsWithQuestionMark() {
		final EpsGraphicsMacroAndText eps = new EpsGraphicsMacroAndText();
		eps.drawText("a\tb", fc(sansSerif(12)), 0, 0);

		final String code = eps.getEPSCode();
		assertTrue(code.contains("(a?b) show"), "Control characters should be replaced by ?");
	}

	// -----------------------------------------------------------------------
	// drawText — different font sizes
	// -----------------------------------------------------------------------

	@Test
	void drawTextUsesDifferentFontSizes() {
		final EpsGraphicsMacroAndText eps8 = new EpsGraphicsMacroAndText();
		eps8.drawText("X", fc(sansSerif(8)), 0, 0);
		assertTrue(eps8.getEPSCode().contains("8 scalefont"));

		final EpsGraphicsMacroAndText eps24 = new EpsGraphicsMacroAndText();
		eps24.drawText("X", fc(sansSerif(24)), 0, 0);
		assertTrue(eps24.getEPSCode().contains("24 scalefont"));
	}

	// -----------------------------------------------------------------------
	// drawText — empty string
	// -----------------------------------------------------------------------

	@Test
	void drawTextWithEmptyString() {
		final EpsGraphicsMacroAndText eps = new EpsGraphicsMacroAndText();
		eps.drawText("", fc(sansSerif(12)), 0, 0);

		final String code = eps.getEPSCode();
		assertTrue(code.contains("() show"), "Empty string should produce () show");
	}

}
