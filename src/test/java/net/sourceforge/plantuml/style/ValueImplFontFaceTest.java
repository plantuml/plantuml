package net.sourceforge.plantuml.style;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.klimt.font.UFontFace;

/**
 * Unit tests for {@link ValueImpl#asFontFace()} and
 * {@link ValueNull#asFontFace()} — CSS font-face parsing from style
 * property values.
 */
class ValueImplFontFaceTest {

	private static ValueImpl value(String s) {
		return ValueImpl.regular(s, Specificity.atOrder(0));
	}

	// -----------------------------------------------------------------------
	// Style keywords
	// -----------------------------------------------------------------------

	@Test
	void boldKeyword() {
		final UFontFace face = value("bold").asFontFace();
		assertEquals(UFontFace.bold(), face);
	}

	@Test
	void italicKeyword() {
		final UFontFace face = value("italic").asFontFace();
		assertEquals(UFontFace.italic(), face);
	}

	@Test
	void plainKeyword() {
		final UFontFace face = value("plain").asFontFace();
		assertEquals(UFontFace.normal(), face);
	}

	@Test
	void normalKeyword() {
		final UFontFace face = value("normal").asFontFace();
		assertEquals(UFontFace.normal(), face);
	}

	// -----------------------------------------------------------------------
	// CSS numeric weight keywords
	// -----------------------------------------------------------------------

	@Test
	void lighterKeywordReturnsWeight300() {
		final UFontFace face = value("lighter").asFontFace();
		assertEquals(300, face.getCssWeight());
		assertFalse(face.isItalic());
	}

	@Test
	void bolderKeywordReturnsWeight800() {
		final UFontFace face = value("bolder").asFontFace();
		assertEquals(800, face.getCssWeight());
		assertFalse(face.isItalic());
	}

	// -----------------------------------------------------------------------
	// CSS numeric weight values
	// -----------------------------------------------------------------------

	@Test
	void numericWeight100() {
		assertEquals(100, value("100").asFontFace().getCssWeight());
	}

	@Test
	void numericWeight500() {
		assertEquals(500, value("500").asFontFace().getCssWeight());
	}

	@Test
	void numericWeight900() {
		assertEquals(900, value("900").asFontFace().getCssWeight());
	}

	@Test
	void numericWeightWithWhitespace() {
		assertEquals(600, value("  600  ").asFontFace().getCssWeight());
	}

	// -----------------------------------------------------------------------
	// Invalid / unrecognised → defaults to normal
	// -----------------------------------------------------------------------

	@Test
	void emptyStringReturnsNormal() {
		assertEquals(UFontFace.normal(), value("").asFontFace());
	}

	@Test
	void unrecognisedKeywordReturnsNormal() {
		assertEquals(UFontFace.normal(), value("ultrabold").asFontFace());
		assertEquals(UFontFace.normal(), value("heavy").asFontFace());
	}

	@Test
	void nonNumericStringReturnsNormal() {
		assertEquals(UFontFace.normal(), value("abc").asFontFace());
	}

	// -----------------------------------------------------------------------
	// Weight threshold for isBold()
	// -----------------------------------------------------------------------

	@Test
	void weight700IsBold() {
		assertTrue(value("700").asFontFace().isBold());
	}

	@Test
	void weight600IsNotBold() {
		assertFalse(value("600").asFontFace().isBold());
	}

	// -----------------------------------------------------------------------
	// ValueNull
	// -----------------------------------------------------------------------

	@Test
	void valueNullReturnsNormal() {
		assertEquals(UFontFace.normal(), ValueNull.NULL.asFontFace());
	}
}
