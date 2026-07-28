package net.sourceforge.plantuml.klimt.font;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.awt.font.TextAttribute;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link UFontFace} — immutable value object that carries
 * font-style (normal/italic) and font-weight (CSS 100–900).
 */
class UFontFaceTest {

	// -----------------------------------------------------------------------
	// Factory methods
	// -----------------------------------------------------------------------

	@Test
	void normalFaceHasWeight400AndIsNotItalic() {
		final UFontFace face = UFontFace.normal();
		assertEquals(400, face.getCssWeight());
		assertFalse(face.isItalic());
		assertFalse(face.isBold());
	}

	@Test
	void boldFaceHasWeight700AndIsNotItalic() {
		final UFontFace face = UFontFace.bold();
		assertEquals(700, face.getCssWeight());
		assertTrue(face.isBold());
		assertFalse(face.isItalic());
	}

	@Test
	void italicFaceHasWeight400AndIsItalic() {
		final UFontFace face = UFontFace.italic();
		assertEquals(400, face.getCssWeight());
		assertTrue(face.isItalic());
		assertFalse(face.isBold());
	}

	@Test
	void boldItalicFaceHasWeight700AndIsItalic() {
		final UFontFace face = UFontFace.boldItalic();
		assertEquals(700, face.getCssWeight());
		assertTrue(face.isBold());
		assertTrue(face.isItalic());
	}

	// -----------------------------------------------------------------------
	// fromCssWeight — keyword and numeric parsing
	// -----------------------------------------------------------------------

	@Test
	void fromCssWeightNormalKeyword() {
		final UFontFace face = UFontFace.fromCssWeight("normal");
		assertNotNull(face);
		assertEquals(400, face.getCssWeight());
	}

	@Test
	void fromCssWeightBoldKeyword() {
		final UFontFace face = UFontFace.fromCssWeight("bold");
		assertNotNull(face);
		assertEquals(700, face.getCssWeight());
	}

	@Test
	void fromCssWeightLighterKeyword() {
		final UFontFace face = UFontFace.fromCssWeight("lighter");
		assertNotNull(face);
		assertEquals(300, face.getCssWeight());
	}

	@Test
	void fromCssWeightBolderKeyword() {
		final UFontFace face = UFontFace.fromCssWeight("bolder");
		assertNotNull(face);
		assertEquals(800, face.getCssWeight());
	}

	@Test
	void fromCssWeightNumeric500() {
		final UFontFace face = UFontFace.fromCssWeight("500");
		assertNotNull(face);
		assertEquals(500, face.getCssWeight());
	}

	@Test
	void fromCssWeightNumeric100() {
		final UFontFace face = UFontFace.fromCssWeight("100");
		assertNotNull(face);
		assertEquals(100, face.getCssWeight());
	}

	@Test
	void fromCssWeightNumeric900() {
		final UFontFace face = UFontFace.fromCssWeight("900");
		assertNotNull(face);
		assertEquals(900, face.getCssWeight());
	}

	@Test
	void fromCssWeightWithWhitespace() {
		final UFontFace face = UFontFace.fromCssWeight("  600  ");
		assertNotNull(face);
		assertEquals(600, face.getCssWeight());
	}

	@Test
	void fromCssWeightKeywordCaseInsensitive() {
		assertNotNull(UFontFace.fromCssWeight("BOLD"));
		assertEquals(700, UFontFace.fromCssWeight("BOLD").getCssWeight());
		assertEquals(400, UFontFace.fromCssWeight("Normal").getCssWeight());
	}

	@Test
	void fromCssWeightNullReturnsNull() {
		assertNull(UFontFace.fromCssWeight(null));
	}

	@Test
	void fromCssWeightGarbageReturnsNull() {
		assertNull(UFontFace.fromCssWeight("ultra"));
		assertNull(UFontFace.fromCssWeight("abc"));
	}

	// -----------------------------------------------------------------------
	// withWeight / withStyle — immutability and mutation
	// -----------------------------------------------------------------------

	@Test
	void withWeightReturnsNewInstanceWithUpdatedWeight() {
		final UFontFace original = UFontFace.normal();
		final UFontFace mutated = original.withWeight(500);
		assertEquals(500, mutated.getCssWeight());
		assertEquals(400, original.getCssWeight()); // original unchanged
	}

	@Test
	void withWeightSameValueReturnsSameOrEqualInstance() {
		final UFontFace face = UFontFace.normal();
		final UFontFace same = face.withWeight(400);
		assertEquals(400, same.getCssWeight());
		assertFalse(same.isItalic());
	}

	@Test
	void withStyleReturnsNewInstanceWithItalic() {
		final UFontFace original = UFontFace.normal();
		final UFontFace mutated = original.withStyle(UFontStyle.ITALIC);
		assertTrue(mutated.isItalic());
		assertEquals(400, mutated.getCssWeight());
		assertFalse(original.isItalic()); // original unchanged
	}

	@Test
	void withStyleSameStyleReturnsSameOrEqualInstance() {
		final UFontFace face = UFontFace.normal();
		final UFontFace same = face.withStyle(UFontStyle.NORMAL);
		assertFalse(same.isItalic());
	}

	// -----------------------------------------------------------------------
	// CSS output
	// -----------------------------------------------------------------------

	@Test
	void toCssWeightStringProducesNumericString() {
		assertEquals("400", UFontFace.normal().toCssWeightString());
		assertEquals("700", UFontFace.bold().toCssWeightString());
		assertEquals("500", UFontFace.fromCssWeight("500").toCssWeightString());
		assertEquals("300", UFontFace.fromCssWeight("300").toCssWeightString());
	}

	@Test
	void toCssStyleStringReturnsItalicOrNormal() {
		assertEquals("normal", UFontFace.normal().toCssStyleString());
		assertEquals("italic", UFontFace.italic().toCssStyleString());
		assertEquals("italic", UFontFace.boldItalic().toCssStyleString());
		assertEquals("normal", UFontFace.bold().toCssStyleString());
	}

	// -----------------------------------------------------------------------
	// toLegacyStyle round-trip
	// -----------------------------------------------------------------------

//	@Test
//	void toLegacyStylePreservesPlain() {
//		assertEquals(0, UFontFace.normal().toLegacyStyle()); // Font.PLAIN
//	}
//
//	@Test
//	void toLegacyStylePreservesBold() {
//		assertEquals(1, UFontFace.bold().toLegacyStyle()); // Font.BOLD
//	}
//
//	@Test
//	void toLegacyStylePreservesItalic() {
//		assertEquals(2, UFontFace.italic().toLegacyStyle()); // Font.ITALIC
//	}
//
//	@Test
//	void toLegacyStyleMapsNonBoldWeightToPlain() {
//		// weight 500 is not >= 700, so legacy considers it non-bold
//		final UFontFace face = UFontFace.fromCssWeight("500");
//		assertEquals(0, face.toLegacyStyle() & 1); // & Font.BOLD
//	}
//
//	@Test
//	void toLegacyStyleMapsWeight700ToBold() {
//		final UFontFace face = UFontFace.fromCssWeight("700");
//		assertEquals(1, face.toLegacyStyle() & 1); // & Font.BOLD == Font.BOLD
//	}

	// -----------------------------------------------------------------------
	// toTextAttributeWeight — mapping to AWT TextAttribute
	// -----------------------------------------------------------------------

	@Test
	void weight400MapsToTextAttributeWeightRegular() {
		final float w = UFontFace.normal().toTextAttributeWeight();
		assertEquals(TextAttribute.WEIGHT_REGULAR, w);
	}

	@Test
	void weight700MapsToTextAttributeWeightBold() {
		final float w = UFontFace.bold().toTextAttributeWeight();
		assertEquals(TextAttribute.WEIGHT_BOLD, w);
	}

	@Test
	void weight300MapsToTextAttributeWeightLight() {
		final UFontFace face = UFontFace.fromCssWeight("300");
		assertEquals(TextAttribute.WEIGHT_LIGHT, face.toTextAttributeWeight());
	}

	@Test
	void weight500MapsToTextAttributeWeightMedium() {
		final UFontFace face = UFontFace.fromCssWeight("500");
		assertEquals(TextAttribute.WEIGHT_MEDIUM, face.toTextAttributeWeight());
	}

	// -----------------------------------------------------------------------
	// equals / hashCode
	// -----------------------------------------------------------------------

	@Test
	void differentWeightFacesAreNotEqual() {
		assertNotEquals(UFontFace.fromCssWeight("500"), UFontFace.normal());
	}

	@Test
	void differentStyleFacesAreNotEqual() {
		assertNotEquals(UFontFace.italic(), UFontFace.normal());
	}
}
