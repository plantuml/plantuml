package org.scilab.forge.jlatexmath;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * TeXUnicodeBlock replaces Character.UnicodeBlock (the TeaVM class library does not
 * have it): for the 65,536 characters it must tell the same blocks, the ones that
 * have TeX fonts, and "other" for the rest.
 */
class TeXUnicodeBlockTest {

	@BeforeAll
	static void resources() {
		LibraryResources.install();
	}

	private static TeXUnicodeBlock expected(char c) {
		final Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
		if (block == Character.UnicodeBlock.BASIC_LATIN)
			return TeXUnicodeBlock.BASIC_LATIN;
		if (block == Character.UnicodeBlock.GREEK)
			return TeXUnicodeBlock.GREEK;
		if (block == Character.UnicodeBlock.GREEK_EXTENDED)
			return TeXUnicodeBlock.GREEK_EXTENDED;
		if (block == Character.UnicodeBlock.CYRILLIC)
			return TeXUnicodeBlock.CYRILLIC;
		return TeXUnicodeBlock.OTHER;
	}

	@Test
	void testSameAsJavaForEveryCharacter() {
		for (int i = 0; i < 65536; i++) {
			final char c = (char) i;
			assertEquals(expected(c), TeXUnicodeBlock.of(c), "U+" + Integer.toHexString(i));
		}
	}

	@Test
	void testExamples() {
		assertEquals(TeXUnicodeBlock.BASIC_LATIN, TeXUnicodeBlock.of('a'));
		assertEquals(TeXUnicodeBlock.GREEK, TeXUnicodeBlock.of('α'));
		assertEquals(TeXUnicodeBlock.GREEK_EXTENDED, TeXUnicodeBlock.of('ἀ'));
		assertEquals(TeXUnicodeBlock.CYRILLIC, TeXUnicodeBlock.of('Ж'));
		assertEquals(TeXUnicodeBlock.OTHER, TeXUnicodeBlock.of('中'));
		assertEquals(TeXUnicodeBlock.OTHER, TeXUnicodeBlock.of('é'));
		assertEquals("GREEK", TeXUnicodeBlock.GREEK.toString());
	}

	@Test
	void testTheAlphabetsAreRegisteredWithoutReflection() {
		// the static block of TeXFormula registers Cyrillic and Greek by new, not by Class.forName
		new TeXFormula("x");
		assertNotNull(DefaultTeXFont.registeredAlphabets.get(TeXUnicodeBlock.CYRILLIC));
		assertNotNull(DefaultTeXFont.registeredAlphabets.get(TeXUnicodeBlock.GREEK));
		assertNotNull(DefaultTeXFont.registeredAlphabets.get(TeXUnicodeBlock.GREEK_EXTENDED));
		assertEquals(null, DefaultTeXFont.registeredAlphabets.get(TeXUnicodeBlock.OTHER));
		assertEquals(null, DefaultTeXFont.registeredAlphabets.get(TeXUnicodeBlock.BASIC_LATIN));
	}

	@Test
	void testNewcommandWorksWithoutReflection() {
		// \newcommand is run by a MacroHandler now, not by Method.invoke
		final String plain = size("\\alpha\\beta");
		assertEquals(plain, size("\\newcommand{\\zzab}{\\alpha\\beta} \\zzab"));
		assertEquals(size("\\frac{x}{y}"), size("\\newcommand{\\zzfr}[2]{\\frac{#1}{#2}} \\zzfr{x}{y}"));
		assertEquals(size("\\sqrt{x}"), size("\\newcommand{\\zzsq}[1]{\\sqrt{#1}} \\zzsq{x}"));
		// \renewcommand changes it
		assertEquals(size("\\frac{x}{2}"), size("\\renewcommand{\\zzsq}[1]{\\frac{#1}{2}} \\zzsq{x}"));
		assertTrue(MacroInfo.Commands.containsKey("zzab"));
	}

	private static String size(String tex) {
		final TeXIcon icon = new TeXFormula(tex).new TeXIconBuilder().setStyle(TeXConstants.STYLE_DISPLAY).setSize(20)
				.build();
		return icon.getIconWidth() + "x" + icon.getIconHeight();
	}

}
