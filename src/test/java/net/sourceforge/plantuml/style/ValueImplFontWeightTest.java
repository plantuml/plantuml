package net.sourceforge.plantuml.style;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ValueImpl#asFontWeight()} — CSS numeric weight
 * parsing from style property values.
 */
class ValueImplFontWeightTest {

	private static ValueImpl value(String s) {
		return ValueImpl.regular(s, 0);
	}

	// -----------------------------------------------------------------------
	// Keywords
	// -----------------------------------------------------------------------

	@Test
	void normalKeywordReturns400() {
		assertThat(value("normal").asFontWeight()).isEqualTo(400);
	}

	@Test
	void boldKeywordReturns700() {
		assertThat(value("bold").asFontWeight()).isEqualTo(700);
	}

	@Test
	void lighterKeywordReturns300() {
		assertThat(value("lighter").asFontWeight()).isEqualTo(300);
	}

	@Test
	void bolderKeywordReturns800() {
		assertThat(value("bolder").asFontWeight()).isEqualTo(800);
	}

	// -----------------------------------------------------------------------
	// Numeric values
	// -----------------------------------------------------------------------

	@Test
	void numericWeight100() {
		assertThat(value("100").asFontWeight()).isEqualTo(100);
	}

	@Test
	void numericWeight500() {
		assertThat(value("500").asFontWeight()).isEqualTo(500);
	}

	@Test
	void numericWeight900() {
		assertThat(value("900").asFontWeight()).isEqualTo(900);
	}

	@Test
	void numericWeightWithWhitespace() {
		assertThat(value("  600  ").asFontWeight()).isEqualTo(600);
	}

	// -----------------------------------------------------------------------
	// Invalid / out of range / unrecognised
	// -----------------------------------------------------------------------

	@Test
	void emptyStringReturnsZero() {
		assertThat(value("").asFontWeight()).isEqualTo(0);
	}

	@Test
	void unrecognisedKeywordReturnsZero() {
		assertThat(value("ultrabold").asFontWeight()).isEqualTo(0);
		assertThat(value("heavy").asFontWeight()).isEqualTo(0);
	}

	@Test
	void nonNumericStringReturnsZero() {
		assertThat(value("abc").asFontWeight()).isEqualTo(0);
	}

	// -----------------------------------------------------------------------
	// ValueNull
	// -----------------------------------------------------------------------

	@Test
	void valueNullReturnsZero() {
		assertThat(ValueNull.NULL.asFontWeight()).isEqualTo(0);
	}
}
