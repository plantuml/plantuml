package net.sourceforge.plantuml.asciiverse;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class InfiniteStringTest {

	@Test
	void freshLineIsAllSpaces() {
		final InfiniteString line = new InfiniteString();

		assertEquals(' ', line.getCharAt(0));
		assertEquals(' ', line.getCharAt(5));
		assertEquals(' ', line.getCharAt(-1));
		assertEquals(' ', line.getCharAt(-5));
	}

	@Test
	void setAndGetCharAtPositivePosition() {
		final InfiniteString line = new InfiniteString();

		line.setCharAt(3, 'x');

		assertEquals('x', line.getCharAt(3));
		// untouched neighbors stay blank
		assertEquals(' ', line.getCharAt(0));
		assertEquals(' ', line.getCharAt(2));
		assertEquals(' ', line.getCharAt(4));
	}

	@Test
	void setAndGetCharAtNegativePosition() {
		final InfiniteString line = new InfiniteString();

		line.setCharAt(-3, 'x');

		assertEquals('x', line.getCharAt(-3));
		// untouched neighbors on either side stay blank, including position 0
		assertEquals(' ', line.getCharAt(-1));
		assertEquals(' ', line.getCharAt(-2));
		assertEquals(' ', line.getCharAt(-4));
		assertEquals(' ', line.getCharAt(0));
	}

	@Test
	void setCharAtPositionZero() {
		final InfiniteString line = new InfiniteString();

		line.setCharAt(0, 'z');

		assertEquals('z', line.getCharAt(0));
		assertEquals(' ', line.getCharAt(-1));
		assertEquals(' ', line.getCharAt(1));
	}

	@Test
	void negativeAndPositiveHalvesAreIndependent() {
		final InfiniteString line = new InfiniteString();

		line.setCharAt(-1, 'a');
		line.setCharAt(0, 'b');
		line.setCharAt(1, 'c');

		assertEquals('a', line.getCharAt(-1));
		assertEquals('b', line.getCharAt(0));
		assertEquals('c', line.getCharAt(1));
	}

	@Test
	void canOverwriteANegativeChar() {
		final InfiniteString line = new InfiniteString();

		line.setCharAt(-2, 'x');
		line.setCharAt(-2, 'y');

		assertEquals('y', line.getCharAt(-2));
	}

	@Test
	void setStringAtEntirelyPositive() {
		final InfiniteString line = new InfiniteString();

		line.setStringAt(2, "abc");

		assertEquals('a', line.getCharAt(2));
		assertEquals('b', line.getCharAt(3));
		assertEquals('c', line.getCharAt(4));
	}

	@Test
	void setStringAtEntirelyNegative() {
		final InfiniteString line = new InfiniteString();

		// positions -5, -4, -3
		line.setStringAt(-5, "abc");

		assertEquals('a', line.getCharAt(-5));
		assertEquals('b', line.getCharAt(-4));
		assertEquals('c', line.getCharAt(-3));
		assertEquals(' ', line.getCharAt(-2));
	}

	@Test
	void setStringAtCrossingZero() {
		final InfiniteString line = new InfiniteString();

		// positions -2, -1, 0, 1
		line.setStringAt(-2, "abcd");

		assertEquals('a', line.getCharAt(-2));
		assertEquals('b', line.getCharAt(-1));
		assertEquals('c', line.getCharAt(0));
		assertEquals('d', line.getCharAt(1));
	}

	@Test
	void getStringFromZeroIsUnchangedFromHistoricalBehavior() {
		final InfiniteString line = new InfiniteString();

		line.setStringAt(0, "hello");

		assertEquals("hello", line.getString(0));
	}

	@Test
	void getStringFromZeroIgnoresNegativeChars() {
		final InfiniteString line = new InfiniteString();

		line.setCharAt(-1, 'x');
		line.setStringAt(0, "hello");

		assertEquals("hello", line.getString(0));
	}

	@Test
	void getStringFromNegativeStartIncludesNegativeChars() {
		final InfiniteString line = new InfiniteString();

		line.setStringAt(-2, "ab");
		line.setStringAt(0, "cd");

		assertEquals("abcd", line.getString(-2));
	}

	@Test
	void getStringPadsMissingNegativePositionsWithSpaces() {
		final InfiniteString line = new InfiniteString();

		// only -1 is written; asking from -3 should pad -3 and -2 with spaces
		line.setCharAt(-1, 'x');
		line.setStringAt(0, "yz");

		assertEquals("  xyz", line.getString(-3));
	}

	@Test
	void getStringFromNegativeStartWithNoPositiveContent() {
		final InfiniteString line = new InfiniteString();

		line.setStringAt(-3, "abc");

		assertEquals("abc", line.getString(-3));
	}

	@Test
	void getStringStartingPastLeftmostWrittenPositionIsAllSpacesPrefix() {
		final InfiniteString line = new InfiniteString();

		line.setCharAt(-1, 'x');

		assertEquals("   x", line.getString(-4));
	}

	@Test
	void leftmostPositionIsZeroWhenNothingNegativeWasWritten() {
		final InfiniteString line = new InfiniteString();

		assertEquals(0, line.getLeftmostPosition());

		line.setCharAt(0, 'a');
		line.setStringAt(3, "bcd");

		assertEquals(0, line.getLeftmostPosition());
	}

	@Test
	void leftmostPositionReflectsFurthestNegativeCharWritten() {
		final InfiniteString line = new InfiniteString();

		line.setCharAt(-2, 'x');

		assertEquals(-2, line.getLeftmostPosition());
	}

	@Test
	void leftmostPositionGrowsAsMoreNegativeCharsAreWritten() {
		final InfiniteString line = new InfiniteString();

		line.setCharAt(-2, 'x');
		assertEquals(-2, line.getLeftmostPosition());

		line.setCharAt(-5, 'y');
		assertEquals(-5, line.getLeftmostPosition());

		// writing a less-far-left position afterwards doesn't shrink it back
		line.setCharAt(-1, 'z');
		assertEquals(-5, line.getLeftmostPosition());
	}

	@Test
	void leftmostPositionReflectsSetStringAtCrossingZero() {
		final InfiniteString line = new InfiniteString();

		line.setStringAt(-3, "abcd");

		assertEquals(-3, line.getLeftmostPosition());
	}
}
