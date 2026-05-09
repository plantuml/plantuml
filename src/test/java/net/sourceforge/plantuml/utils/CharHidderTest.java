package net.sourceforge.plantuml.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class CharHidderTest {

	// -------------------- hide() --------------------

	@Test
	void testHideEmpty() {
		final String s = "";
		assertSame(s, CharHidder.hide(s));
	}

	@Test
	void testHidePlainText() {
		final String s = "hello world";
		// fast-path: no '\\' nor '~' -> same instance returned
		assertSame(s, CharHidder.hide(s));
	}

	@Test
	void testHideEscapedTilde() {
		// \~ becomes the hidden form of '~'
		final String expected = "" + (char) ('\uE000' + '~');
		assertEquals(expected, CharHidder.hide("\\~"));
	}

	@Test
	void testHideTildeFollowedByUnderscore() {
		// ~_ becomes the hidden form of '_'
		final String expected = "" + (char) ('\uE000' + '_');
		assertEquals(expected, CharHidder.hide("~_"));
	}

	@Test
	void testHideAllHiddableChars() {
		// All characters listed in isToBeHidden
		final char[] hiddable = { '_', '-', '"', '#', ']', '[', '*', '.', '/', '<' };
		for (char c : hiddable) {
			final String input = "~" + c;
			final String expected = "" + (char) ('\uE000' + c);
			assertEquals(expected, CharHidder.hide(input), "for char '" + c + "'");
		}
	}

	@Test
	void testHideTildeFollowedByNonHiddable() {
		// ~a -> kept as-is; both chars consumed (the trailing 'a' is NOT re-examined)
		assertEquals("~a", CharHidder.hide("~a"));
	}

	@Test
	void testHideTildeAtEnd() {
		// '~' at the very end of the string is kept as-is
		assertEquals("abc~", CharHidder.hide("abc~"));
	}

	@Test
	void testHideBackslashAtEnd() {
		// '\\' at the very end is not followed by '~' so it is kept as-is
		assertEquals("abc\\", CharHidder.hide("abc\\"));
	}

	@Test
	void testHideBackslashFollowedByNonTilde() {
		// '\a' is not '\~' so both chars are kept; fast-path semantics still hold
		// because we go through the else branch character-by-character
		assertEquals("a\\b", CharHidder.hide("a\\b"));
	}

	@Test
	void testHideDoubleTildeUnderscore() {
		// "~~_" -> first '~' is followed by '~' (non-hiddable), so both consumed as-is.
		// The trailing '_' is then plain text. Result: "~~_"
		// This guards the i += 2 behavior in the non-hiddable branch.
		assertEquals("~~_", CharHidder.hide("~~_"));
	}

	@Test
	void testHideTildeUnderscoreInContext() {
		final String hiddenUnderscore = "" + (char) ('\uE000' + '_');
		assertEquals("foo" + hiddenUnderscore + "bar", CharHidder.hide("foo~_bar"));
	}

	@Test
	void testHideMixedSequence() {
		// "a~_b\\~c~xd" =>
		//   'a'      -> 'a'
		//   '~_'     -> hidden '_'
		//   'b'      -> 'b'
		//   '\\~'    -> hidden '~'
		//   'c'      -> 'c'
		//   '~x'     -> '~x' (x not hiddable, both consumed)
		//   'd'      -> 'd'
		final char hu = (char) ('\uE000' + '_');
		final char ht = (char) ('\uE000' + '~');
		final String expected = "a" + hu + "b" + ht + "c~xd";
		assertEquals(expected, CharHidder.hide("a~_b\\~c~xd"));
	}

	@Test
	void testHideMultipleEscapedTildes() {
		final char ht = (char) ('\uE000' + '~');
		assertEquals("" + ht + ht + ht, CharHidder.hide("\\~\\~\\~"));
	}

	@Test
	void testHideOnlyTilde() {
		// A single '~' with no following character is kept as-is
		assertEquals("~", CharHidder.hide("~"));
	}

	@Test
	void testHideOnlyBackslash() {
		// A single '\\' with no following character is kept as-is
		assertEquals("\\", CharHidder.hide("\\"));
	}

	// -------------------- unhide() --------------------

	@Test
	void testUnhideEmpty() {
		final String s = "";
		assertSame(s, CharHidder.unhide(s));
	}

	@Test
	void testUnhidePlainText() {
		final String s = "hello world";
		// fast-path: no character in [\uE000..\uE0FF] -> same instance returned
		assertSame(s, CharHidder.unhide(s));
	}

	@Test
	void testUnhideSingleHiddenChar() {
		final String input = "" + (char) ('\uE000' + '_');
		assertEquals("_", CharHidder.unhide(input));
	}

	@Test
	void testUnhideMultipleHiddenChars() {
		final char hu = (char) ('\uE000' + '_');
		final char hd = (char) ('\uE000' + '-');
		final char ht = (char) ('\uE000' + '~');
		assertEquals("_-~", CharHidder.unhide("" + hu + hd + ht));
	}

	@Test
	void testUnhideHiddenCharAtStart() {
		final char hu = (char) ('\uE000' + '_');
		assertEquals("_abc", CharHidder.unhide(hu + "abc"));
	}

	@Test
	void testUnhideHiddenCharInMiddle() {
		final char hu = (char) ('\uE000' + '_');
		assertEquals("ab_cd", CharHidder.unhide("ab" + hu + "cd"));
	}

	@Test
	void testUnhideHiddenCharAtEnd() {
		final char hu = (char) ('\uE000' + '_');
		assertEquals("abc_", CharHidder.unhide("abc" + hu));
	}

	@Test
	void testUnhideMixedWithPlainText() {
		final char hu = (char) ('\uE000' + '_');
		final char hd = (char) ('\uE000' + '-');
		assertEquals("foo_bar-baz", CharHidder.unhide("foo" + hu + "bar" + hd + "baz"));
	}

	@Test
	void testUnhideRangeBoundaryLow() {
		// \uE000 is the lowest hidden value (codepoint 0)
		assertEquals("" + (char) 0, CharHidder.unhide("" + '\uE000'));
	}

	@Test
	void testUnhideRangeBoundaryHigh() {
		// \uE0FF is the highest hidden value (codepoint 0xFF)
		assertEquals("" + (char) 0xFF, CharHidder.unhide("" + '\uE0FF'));
	}

	@Test
	void testUnhideOutsideRangeStaysUnchanged() {
		// \uDFFF is just below the range, \uE100 is just above. Both should be kept as-is.
		final String s = "" + '\uDFFF' + '\uE100' + '\uF000';
		assertSame(s, CharHidder.unhide(s));
	}

	@Test
	void testUnhideIsIdempotent() {
		// unhide on already-unhidden text is a no-op (and returns the same instance)
		final String s = "plain text only";
		final String once = CharHidder.unhide(s);
		assertSame(once, CharHidder.unhide(once));
	}

	// -------------------- round-trip --------------------

	@Test
	void testRoundTripHideThenUnhide() {
		// hide turns "~_" into hidden '_', and unhide gives back "_"
		assertEquals("_", CharHidder.unhide(CharHidder.hide("~_")));
	}

	@Test
	void testRoundTripEscapedTilde() {
		// hide turns "\\~" into hidden '~', and unhide gives back "~"
		assertEquals("~", CharHidder.unhide(CharHidder.hide("\\~")));
	}

	@Test
	void testRoundTripPlainText() {
		final String s = "no special chars here";
		assertSame(s, CharHidder.unhide(CharHidder.hide(s)));
	}

	@Test
	void testRoundTripComplex() {
		// "foo~_bar\\~baz" -> hide -> unhide -> "foo_bar~baz"
		assertEquals("foo_bar~baz", CharHidder.unhide(CharHidder.hide("foo~_bar\\~baz")));
	}

}
