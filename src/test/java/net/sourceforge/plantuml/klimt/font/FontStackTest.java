package net.sourceforge.plantuml.klimt.font;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Font;

import org.junit.jupiter.api.Test;

class FontStackTest {

	@Test
	void testGetFont() {
		FontStack stack = new FontStack("foo");
		Font font = stack.getFont("hello", Font.PLAIN, 12);
		assertThat(font.toString()).isEqualTo("java.awt.Font[family=Dialog,name=foo,style=plain,size=12]");
	}

	@Test
	void shouldReturnMinusOneWhenAllCharsAreDisplayable() {
		FontStack stack = new FontStack("foo");
		// All characters are displayable in most fonts
		assertThat(stack.canDisplayUpTo(0, "a")).isEqualTo(-1);
	}

//	@Test
//	void shouldReturnMinusOneForCommonCJKAndEmoji() {
//		FontStack stack = new FontStack("foo");
//		// All characters ("具", "🐛") are displayable in many recent fonts
//		assertThat(stack.canDisplayUpTo(0, "具🐛")).isEqualTo(-1);
//	}
//
//	@Test
//	void shouldReturnZeroForRareEmojiNotCoveredByFont() {
//		FontStack stack = new FontStack("foo");
//		// U+1F9AC (banjo emoji) is rarely supported by standard fonts
//		String nonDisplayable = "\uD83E\uDEAC";
//		assertThat(stack.canDisplayUpTo(0, nonDisplayable)).isEqualTo(0); // First char is not displayable
//	}

	@Test
	void shouldReturnOneForFirstNonDisplayableCharAtSecondPosition() {
		FontStack stack = new FontStack("foo");
		// "a" is displayable, 𠀀 (U+20000, CJK Unified Ideograph-20000) usually not
		String nonDisplayable = "a\uD840\uDC00";
		assertThat(stack.canDisplayUpTo(0, nonDisplayable)).isEqualTo(1); // Second char is not displayable
	}

	@Test
	void shouldReturnTwoForFirstNonDisplayableCharAtThirdPosition() {
		FontStack stack = new FontStack("foo");
		// "a" and "b" are displayable, 𡃁 (U+210C1) is rare and not displayable
		String nonDisplayable = "ab\uD844\uDFC1";
		assertThat(stack.canDisplayUpTo(0, nonDisplayable)).isEqualTo(2); // Third char is not displayable
	}
	
//	soedwjw5b1il3af6udlfyzhw908v8fr
//	5wim76d0ju0wd2yd4yhowjfqc613u7s

}
