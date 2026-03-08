package net.sourceforge.plantuml.klimt.drawing.eps;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.font.UFontContext;
import net.sourceforge.plantuml.klimt.font.UFontFace;
import net.sourceforge.plantuml.klimt.font.UFontFactory;

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
	// drawText — different font families
	// -----------------------------------------------------------------------

	@Test
	void drawTextWithSerifFont() {
		final UFont serif = UFontFactory.build("Serif", UFontFace.normal(), 16);
		final EpsGraphicsMacroAndText eps = new EpsGraphicsMacroAndText();
		eps.drawText("Test", fc(serif), 0, 0);

		final String code = eps.getEPSCode();
		assertTrue(code.contains("16 scalefont"), "Expected 16 scalefont for Serif font");
		assertTrue(code.contains("findfont"), "Expected findfont for Serif font");
		assertTrue(code.contains("(Test) show"), "Expected text show for Serif font");
	}

	@Test
	void drawTextWithMonospacedFont() {
		final UFont mono = UFontFactory.build("Monospaced", UFontFace.normal(), 10);
		final EpsGraphicsMacroAndText eps = new EpsGraphicsMacroAndText();
		eps.drawText("code", fc(mono), 0, 0);

		final String code = eps.getEPSCode();
		assertTrue(code.contains("10 scalefont"), "Expected 10 scalefont for Monospaced font");
		assertTrue(code.contains("(code) show"), "Expected text show for Monospaced font");
	}

	@Test
	void drawTextWithCourierFont() {
		final UFont courier = UFontFactory.build("Courier", UFontFace.normal(), 11);
		final EpsGraphicsMacroAndText eps = new EpsGraphicsMacroAndText();
		eps.drawText("log", fc(courier), 0, 0);

		final String code = eps.getEPSCode();
		assertTrue(code.contains("11 scalefont"), "Expected 11 scalefont for Courier font");
	}

	@Test
	void drawTextProducesExpectedFindfontLine() {
		final UFont serif = UFontFactory.build("Serif", UFontFace.normal(), 12);
		final EpsGraphicsMacroAndText eps = new EpsGraphicsMacroAndText();
		eps.drawText("A", fc(serif), 0, 0);

		// Compute the expected PS name the same way the production code does
		final String expectedPSName = serif.getFamily("A", UFontContext.EPS);
		final String expectedLine = "/" + expectedPSName + " findfont 12 scalefont setfont";

		assertTrue(eps.getEPSCode().contains(expectedLine),
				"Expected line: " + expectedLine);
	}

	@Test
	void differentFamiliesProduceDifferentFindfontLines() {
		final UFont serif = UFontFactory.build("Serif", UFontFace.normal(), 12);
		final UFont sans = UFontFactory.build("SansSerif", UFontFace.normal(), 12);

		final String serifPSName = serif.getFamily("A", UFontContext.EPS);
		final String sansPSName = sans.getFamily("A", UFontContext.EPS);

		// Sanity check: the two families should map to different PS names
		assertFalse(serifPSName.equals(sansPSName),
				"Serif and SansSerif should have different PS names");

		final EpsGraphicsMacroAndText epsSerif = new EpsGraphicsMacroAndText();
		epsSerif.drawText("A", fc(serif), 0, 0);
		assertTrue(epsSerif.getEPSCode().contains(
				"/" + serifPSName + " findfont 12 scalefont setfont"));

		final EpsGraphicsMacroAndText epsSans = new EpsGraphicsMacroAndText();
		epsSans.drawText("A", fc(sans), 0, 0);
		assertTrue(epsSans.getEPSCode().contains(
				"/" + sansPSName + " findfont 12 scalefont setfont"));
	}

	// -----------------------------------------------------------------------
	// drawText — font size variations
	// -----------------------------------------------------------------------

	@Test
	void drawTextWithSmallFont() {
		final EpsGraphicsMacroAndText eps = new EpsGraphicsMacroAndText();
		eps.drawText("tiny", fc(sansSerif(6)), 0, 0);
		assertTrue(eps.getEPSCode().contains("6 scalefont"));
	}

	@Test
	void drawTextWithLargeFont() {
		final EpsGraphicsMacroAndText eps = new EpsGraphicsMacroAndText();
		eps.drawText("BIG", fc(sansSerif(72)), 0, 0);
		assertTrue(eps.getEPSCode().contains("72 scalefont"));
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
