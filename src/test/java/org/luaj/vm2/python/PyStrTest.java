package org.luaj.vm2.python;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * The code point rule of {@link PyStr}, on its own. The behaviour seen from
 * Python is tested by the script language/strings_unicode.py.
 */
class PyStrTest {

	/** a, an emoji (two chars), b */
	private static final String S = "a😀b";

	@Test
	void bmpTellsIfACharIsACodePoint() {
		assertTrue(PyStr.bmp(""));
		assertTrue(PyStr.bmp("abcé中￿"));
		assertFalse(PyStr.bmp(S));
		// long strings go through the memory of the last answer
		String longBmp = new String(new char[100]).replace('\0', 'x');
		String longAstral = longBmp + "😀";
		for (int i = 0; i < 3; i++) {
			assertTrue(PyStr.bmp(longBmp));
			assertFalse(PyStr.bmp(longAstral));
		}
	}

	@Test
	void lengthCountsCodePoints() {
		assertEquals(0, PyStr.length(""));
		assertEquals(3, PyStr.length(S));
		assertEquals(4, S.length());
		assertEquals(40, PyStr.length(new String(new char[40]).replace("\0", "😀")));
	}

	@Test
	void offsetsAndIndices() {
		assertEquals(0, PyStr.offset(S, 0));
		assertEquals(1, PyStr.offset(S, 1));
		assertEquals(3, PyStr.offset(S, 2));
		assertEquals(4, PyStr.offset(S, 3));
		assertEquals(2, PyStr.cpIndexOf(S, 3));
		assertEquals(1, PyStr.cpIndexOf(S, 1));
		assertEquals(2, PyStr.offset("abc", 2));
		assertEquals(2, PyStr.cpIndexOf("abc", 2));
	}

	@Test
	void charAtAndSubstring() {
		assertEquals("a", PyStr.charAt(S, 0));
		assertEquals("😀", PyStr.charAt(S, 1));
		assertEquals("b", PyStr.charAt(S, 2));
		assertEquals("😀b", PyStr.substring(S, 1, 2));
		assertEquals("a😀", PyStr.substring(S, 0, 2));
		assertEquals("", PyStr.substring(S, 3, 0));
		assertEquals("bc", PyStr.substring("abcd", 1, 2));
	}

	@Test
	void subscripts() {
		assertEquals("😀", PyStr.getitem(S, Integer.valueOf(-2)));
		assertEquals("b😀a", PyStr.getitem(S, new PySlice(PyNone.NONE, PyNone.NONE, Integer.valueOf(-1))));
		assertEquals("ab", PyStr.getitem(S, new PySlice(PyNone.NONE, PyNone.NONE, Integer.valueOf(2))));
	}
}
