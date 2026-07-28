package net.sourceforge.plantuml.klimt.font;

import static org.junit.jupiter.api.Assertions.assertEquals;


import java.awt.Font;

import org.junit.jupiter.api.Test;

class FontStackTest {

	@Test
	void testGetFont() {
		FontStack stack = FontStack.build("foo");
		Font font = stack.getFont("hello", Font.PLAIN, 12);
		assertEquals("java.awt.Font[family=Dialog,name=foo,style=plain,size=12]", font.toString());
	}

	@Test
	void shouldReturnMinusOneWhenAllCharsAreDisplayable() {
		FontStack stack = FontStack.build("foo");
		// All characters are displayable in most fonts
		assertEquals(-1, stack.canDisplayUpTo(0, "a"));
	}

//	@Test
//	void shouldReturnMinusOneForCommonCJKAndEmoji() {
//		FontStack stack = new FontStack("foo");
//		// All characters ("具", "🐛") are displayable in many recent fonts
//		assertEquals(-1, stack.canDisplayUpTo(0, "具🐛"));
//	}
//
//	@Test
//	void shouldReturnZeroForRareEmojiNotCoveredByFont() {
//		FontStack stack = new FontStack("foo");
//		// U+1F9AC (banjo emoji) is rarely supported by standard fonts
//		String nonDisplayable = "\uD83E\uDEAC";
//		assertEquals(0, stack.canDisplayUpTo(0, nonDisplayable)); // First char is not displayable
//	}
//
//	@Test
//	void shouldReturnOneForFirstNonDisplayableCharAtSecondPosition() {
//		FontStack stack = new FontStack("foo");
//		// "a" is displayable, 𠀀 (U+20000, CJK Unified Ideograph-20000) usually not
//		String nonDisplayable = "a\uD840\uDC00";
//		assertEquals(1, stack.canDisplayUpTo(0, nonDisplayable)); // Second char is not displayable
//	}
//
//	@Test
//	void shouldReturnTwoForFirstNonDisplayableCharAtThirdPosition() {
//		FontStack stack = new FontStack("foo");
//		// "a" and "b" are displayable, 𡃁 (U+210C1) is rare and not displayable
//		String nonDisplayable = "ab\uD844\uDFC1";
//		assertEquals(2, stack.canDisplayUpTo(0, nonDisplayable)); // Third char is not displayable
//	}
//	
//	soedwjw5b1il3af6udlfyzhw908v8fr
//	5wim76d0ju0wd2yd4yhowjfqc613u7s

}
