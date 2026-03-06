package net.sourceforge.plantuml.klimt.font;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Font;
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
		assertThat(face.getCssWeight()).isEqualTo(400);
		assertThat(face.isItalic()).isFalse();
		assertThat(face.isBold()).isFalse();
	}

	@Test
	void boldFaceHasWeight700AndIsNotItalic() {
		final UFontFace face = UFontFace.bold();
		assertThat(face.getCssWeight()).isEqualTo(700);
		assertThat(face.isBold()).isTrue();
		assertThat(face.isItalic()).isFalse();
	}

	@Test
	void italicFaceHasWeight400AndIsItalic() {
		final UFontFace face = UFontFace.italic();
		assertThat(face.getCssWeight()).isEqualTo(400);
		assertThat(face.isItalic()).isTrue();
		assertThat(face.isBold()).isFalse();
	}

	@Test
	void boldItalicFaceHasWeight700AndIsItalic() {
		final UFontFace face = UFontFace.boldItalic();
		assertThat(face.getCssWeight()).isEqualTo(700);
		assertThat(face.isBold()).isTrue();
		assertThat(face.isItalic()).isTrue();
	}

	// -----------------------------------------------------------------------
	// fromCssWeight — keyword and numeric parsing
	// -----------------------------------------------------------------------

	@Test
	void fromCssWeightNormalKeyword() {
		final UFontFace face = UFontFace.fromCssWeight("normal");
		assertThat(face).isNotNull();
		assertThat(face.getCssWeight()).isEqualTo(400);
	}

	@Test
	void fromCssWeightBoldKeyword() {
		final UFontFace face = UFontFace.fromCssWeight("bold");
		assertThat(face).isNotNull();
		assertThat(face.getCssWeight()).isEqualTo(700);
	}

	@Test
	void fromCssWeightLighterKeyword() {
		final UFontFace face = UFontFace.fromCssWeight("lighter");
		assertThat(face).isNotNull();
		assertThat(face.getCssWeight()).isEqualTo(300);
	}

	@Test
	void fromCssWeightBolderKeyword() {
		final UFontFace face = UFontFace.fromCssWeight("bolder");
		assertThat(face).isNotNull();
		assertThat(face.getCssWeight()).isEqualTo(800);
	}

	@Test
	void fromCssWeightNumeric500() {
		final UFontFace face = UFontFace.fromCssWeight("500");
		assertThat(face).isNotNull();
		assertThat(face.getCssWeight()).isEqualTo(500);
	}

	@Test
	void fromCssWeightNumeric100() {
		final UFontFace face = UFontFace.fromCssWeight("100");
		assertThat(face).isNotNull();
		assertThat(face.getCssWeight()).isEqualTo(100);
	}

	@Test
	void fromCssWeightNumeric900() {
		final UFontFace face = UFontFace.fromCssWeight("900");
		assertThat(face).isNotNull();
		assertThat(face.getCssWeight()).isEqualTo(900);
	}

	@Test
	void fromCssWeightWithWhitespace() {
		final UFontFace face = UFontFace.fromCssWeight("  600  ");
		assertThat(face).isNotNull();
		assertThat(face.getCssWeight()).isEqualTo(600);
	}

	@Test
	void fromCssWeightKeywordCaseInsensitive() {
		assertThat(UFontFace.fromCssWeight("BOLD")).isNotNull();
		assertThat(UFontFace.fromCssWeight("BOLD").getCssWeight()).isEqualTo(700);
		assertThat(UFontFace.fromCssWeight("Normal").getCssWeight()).isEqualTo(400);
	}

	@Test
	void fromCssWeightNullReturnsNull() {
		assertThat(UFontFace.fromCssWeight(null)).isNull();
	}

	@Test
	void fromCssWeightGarbageReturnsNull() {
		assertThat(UFontFace.fromCssWeight("ultra")).isNull();
		assertThat(UFontFace.fromCssWeight("abc")).isNull();
	}

	// -----------------------------------------------------------------------
	// fromLegacyStyle
	// -----------------------------------------------------------------------

	@Test
	void fromLegacyStylePlain() {
		final UFontFace face = UFontFace.fromLegacyStyle(Font.PLAIN);
		assertThat(face.getCssWeight()).isEqualTo(400);
		assertThat(face.isItalic()).isFalse();
	}

	@Test
	void fromLegacyStyleBold() {
		final UFontFace face = UFontFace.fromLegacyStyle(Font.BOLD);
		assertThat(face.getCssWeight()).isEqualTo(700);
		assertThat(face.isItalic()).isFalse();
	}

	@Test
	void fromLegacyStyleItalic() {
		final UFontFace face = UFontFace.fromLegacyStyle(Font.ITALIC);
		assertThat(face.getCssWeight()).isEqualTo(400);
		assertThat(face.isItalic()).isTrue();
	}

	@Test
	void fromLegacyStyleBoldItalic() {
		final UFontFace face = UFontFace.fromLegacyStyle(Font.BOLD | Font.ITALIC);
		assertThat(face.getCssWeight()).isEqualTo(700);
		assertThat(face.isItalic()).isTrue();
	}

	// -----------------------------------------------------------------------
	// withWeight / withStyle — immutability and mutation
	// -----------------------------------------------------------------------

	@Test
	void withWeightReturnsNewInstanceWithUpdatedWeight() {
		final UFontFace original = UFontFace.normal();
		final UFontFace mutated = original.withWeight(500);
		assertThat(mutated.getCssWeight()).isEqualTo(500);
		assertThat(original.getCssWeight()).isEqualTo(400); // original unchanged
	}

	@Test
	void withWeightSameValueReturnsSameOrEqualInstance() {
		final UFontFace face = UFontFace.normal();
		final UFontFace same = face.withWeight(400);
		assertThat(same.getCssWeight()).isEqualTo(400);
		assertThat(same.isItalic()).isFalse();
	}

	@Test
	void withStyleReturnsNewInstanceWithItalic() {
		final UFontFace original = UFontFace.normal();
		final UFontFace mutated = original.withStyle(UFontStyle.ITALIC);
		assertThat(mutated.isItalic()).isTrue();
		assertThat(mutated.getCssWeight()).isEqualTo(400);
		assertThat(original.isItalic()).isFalse(); // original unchanged
	}

	@Test
	void withStyleSameStyleReturnsSameOrEqualInstance() {
		final UFontFace face = UFontFace.normal();
		final UFontFace same = face.withStyle(UFontStyle.NORMAL);
		assertThat(same.isItalic()).isFalse();
	}

	// -----------------------------------------------------------------------
	// CSS output
	// -----------------------------------------------------------------------

	@Test
	void toCssWeightStringProducesNumericString() {
		assertThat(UFontFace.normal().toCssWeightString()).isEqualTo("400");
		assertThat(UFontFace.bold().toCssWeightString()).isEqualTo("700");
		assertThat(UFontFace.fromCssWeight("500").toCssWeightString()).isEqualTo("500");
		assertThat(UFontFace.fromCssWeight("300").toCssWeightString()).isEqualTo("300");
	}

	@Test
	void toCssStyleStringReturnsItalicOrNormal() {
		assertThat(UFontFace.normal().toCssStyleString()).isEqualTo("normal");
		assertThat(UFontFace.italic().toCssStyleString()).isEqualTo("italic");
		assertThat(UFontFace.boldItalic().toCssStyleString()).isEqualTo("italic");
		assertThat(UFontFace.bold().toCssStyleString()).isEqualTo("normal");
	}

	// -----------------------------------------------------------------------
	// toLegacyStyle round-trip
	// -----------------------------------------------------------------------

	@Test
	void toLegacyStylePreservesPlain() {
		assertThat(UFontFace.normal().toLegacyStyle()).isEqualTo(Font.PLAIN);
	}

	@Test
	void toLegacyStylePreservesBold() {
		assertThat(UFontFace.bold().toLegacyStyle()).isEqualTo(Font.BOLD);
	}

	@Test
	void toLegacyStylePreservesItalic() {
		assertThat(UFontFace.italic().toLegacyStyle()).isEqualTo(Font.ITALIC);
	}

	@Test
	void toLegacyStyleMapsNonBoldWeightToPlain() {
		// weight 500 is not >= 700, so legacy considers it non-bold
		final UFontFace face = UFontFace.fromCssWeight("500");
		assertThat(face.toLegacyStyle() & Font.BOLD).isEqualTo(0);
	}

	@Test
	void toLegacyStyleMapsWeight700ToBold() {
		final UFontFace face = UFontFace.fromCssWeight("700");
		assertThat(face.toLegacyStyle() & Font.BOLD).isEqualTo(Font.BOLD);
	}

	// -----------------------------------------------------------------------
	// toTextAttributeWeight — mapping to AWT TextAttribute
	// -----------------------------------------------------------------------

	@Test
	void weight400MapsToTextAttributeWeightRegular() {
		final float w = UFontFace.normal().toTextAttributeWeight();
		assertThat(w).isEqualTo(TextAttribute.WEIGHT_REGULAR);
	}

	@Test
	void weight700MapsToTextAttributeWeightBold() {
		final float w = UFontFace.bold().toTextAttributeWeight();
		assertThat(w).isEqualTo(TextAttribute.WEIGHT_BOLD);
	}

	@Test
	void weight300MapsToTextAttributeWeightLight() {
		final UFontFace face = UFontFace.fromCssWeight("300");
		assertThat(face.toTextAttributeWeight()).isEqualTo(TextAttribute.WEIGHT_LIGHT);
	}

	@Test
	void weight500MapsToTextAttributeWeightMedium() {
		final UFontFace face = UFontFace.fromCssWeight("500");
		assertThat(face.toTextAttributeWeight()).isEqualTo(TextAttribute.WEIGHT_MEDIUM);
	}

	// -----------------------------------------------------------------------
	// equals / hashCode
	// -----------------------------------------------------------------------

	@Test
	void equalFacesAreEqual() {
		assertThat(UFontFace.normal()).isEqualTo(UFontFace.fromLegacyStyle(Font.PLAIN));
		assertThat(UFontFace.bold()).isEqualTo(UFontFace.fromLegacyStyle(Font.BOLD));
	}

	@Test
	void differentWeightFacesAreNotEqual() {
		assertThat(UFontFace.normal()).isNotEqualTo(UFontFace.fromCssWeight("500"));
	}

	@Test
	void differentStyleFacesAreNotEqual() {
		assertThat(UFontFace.normal()).isNotEqualTo(UFontFace.italic());
	}
}
