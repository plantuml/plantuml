package sanskrit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * The str as an object: the same code point rule as the static methods of
 * {@link PyStr}, and the str as a key of a dict.
 */
class PyStrObjectTest {

	/** a, an emoji (two chars), b */
	private static final String S = "a😀b";

	private static PySlice slice(Object lower, Object upper, Object step) {
		return new PySlice(lower, upper, step);
	}

	@Test
	void sharedStrs() {
		assertSame(PyStr.EMPTY, PyStr.of(""));
		assertSame(PyStr.of("a"), PyStr.of("a"));
		assertEquals("é", PyStr.of("é").toString());
	}

	@Test
	void equalityIsThatOfTheCharacters() {
		assertEquals(PyStr.of("abc"), PyStr.of(new String("abc")));
		assertEquals(PyStr.of("abc").hashCode(), "abc".hashCode());
		assertNotEquals(PyStr.of("abc"), PyStr.of("abd"));
		assertFalse(PyStr.of("abc").equals("abc"));
	}

	@Test
	void lengthCountsCodePoints() {
		assertEquals(0, PyStr.of("").length());
		assertEquals(3, PyStr.of(S).length());
		assertEquals(40, PyStr.of(new String(new char[40]).replace("\0", "😀")).length());
		assertTrue(PyStr.of("abcé中").isBmp());
		assertFalse(PyStr.of(S).isBmp());
	}

	@Test
	void aLoneSurrogateIsACharacterOfItsOwn() {
		// '\ud83d' + '\ude00' is two characters for Python, whatever Java makes of them
		assertEquals(1, PyStr.of("\ud83d").length());
		assertEquals(1, PyStr.of("\ud83d").charAt(0).length());
		// a lone surrogate has a form of its own inside a str; the host gets the char back
		assertEquals("\ud83d", PyStr.external(PyStr.of("\ud83dx").charAt(0).toString()));
		final PyStr both = PyStr.of(PyStr.ofCp(0xd83d) + PyStr.ofCp(0xde00));
		assertEquals(2, both.length());
		assertFalse(both.equals(PyStr.of("\ud83d\ude00")));
		assertEquals(PyStr.ofCp(0xde00), both.charAt(1).toString());
		assertEquals(0xde00, PyStr.cpAt(both.toString(), both.offset(1)));
		assertEquals("\ud83d\ude00", PyStr.external(both.toString()));
	}

	@Test
	void charAtAndSubstring() {
		final PyStr s = PyStr.of(S);
		assertEquals("a", s.charAt(0).toString());
		assertEquals("😀", s.charAt(1).toString());
		assertEquals("b", s.charAt(2).toString());
		assertEquals("😀b", s.substring(1, 2).toString());
		assertEquals("a😀", s.substring(0, 2).toString());
		assertSame(PyStr.EMPTY, s.substring(3, 0));
		assertSame(s, s.substring(0, 3));
		assertEquals("bc", PyStr.of("abcd").substring(1, 2).toString());
	}

	@Test
	void subscripts() {
		final PyStr s = PyStr.of(S);
		assertEquals("😀", s.getitem(Integer.valueOf(-2)).toString());
		assertEquals("b😀a", s.getitem(slice(PyNone.NONE, PyNone.NONE, Integer.valueOf(-1))).toString());
		assertEquals("ab", s.getitem(slice(PyNone.NONE, PyNone.NONE, Integer.valueOf(2))).toString());
		assertEquals("😀b", s.getitem(slice(Integer.valueOf(1), PyNone.NONE, PyNone.NONE)).toString());
	}

	@Test
	void sameAnswersAsTheCodePointsOfTheString() {
		final String[] samples = { "", "abc", S, "😀😀😀", "x😀y😀z😀", "é中😀" };
		for (String str : samples) {
			final PyStr p = PyStr.of(str);
			final int[] cps = str.codePoints().toArray();
			assertEquals(cps.length, p.length());
			for (int i = 0; i < cps.length; i++)
				assertEquals(new String(cps, i, 1), p.charAt(i).toString());
			for (int from = 0; from <= cps.length; from++)
				for (int count = 0; from + count <= cps.length; count++)
					assertEquals(new String(cps, from, count), p.substring(from, count).toString());
			final StringBuilder reversed = new StringBuilder();
			for (int i = cps.length - 1; i >= 0; i--)
				reversed.appendCodePoint(cps[i]);
			assertEquals(reversed.toString(),
					p.getitem(slice(PyNone.NONE, PyNone.NONE, Integer.valueOf(-1))).toString());
		}
	}

	@Test
	void offsetsAndIndices() {
		final PyStr s = PyStr.of(S);
		assertEquals(0, s.offset(0));
		assertEquals(1, s.offset(1));
		assertEquals(3, s.offset(2));
		assertEquals(4, s.offset(3));
		assertEquals(2, s.cpIndexOf(3));
		assertEquals(1, s.cpIndexOf(1));
		assertEquals(2, PyStr.of("abc").offset(2));
		assertEquals(2, PyStr.of("abc").cpIndexOf(2));
	}

	@Test
	void aStrAndAStringAreTheSameKey() {
		final PyDict d = new PyDict();
		d.put(PyStr.of("k"), Integer.valueOf(1));
		assertEquals(Integer.valueOf(1), d.get("k"));
		assertEquals(Integer.valueOf(1), d.get(PyStr.of("k")));
		d.put("k", Integer.valueOf(2));
		assertEquals(1, d.size());
		assertEquals(Integer.valueOf(2), d.get(PyStr.of(new String("k"))));
		assertTrue(d.containsKey("k"));
		assertEquals(Integer.valueOf(2), d.remove(PyStr.of("k")));
		assertNull(d.get("k"));
	}

	@Test
	void theEntryKeepsTheKeyAsItWasGiven() {
		final PyDict d = new PyDict();
		final PyStr key = PyStr.of(S);
		d.put(key, Integer.valueOf(1));
		assertSame(key, d.map.values().iterator().next().key);
	}

	@Test
	void aStrInsideATupleIsNormalised() {
		final PyDict d = new PyDict();
		d.put(new PyTuple(new Object[] { PyStr.of("a"), Integer.valueOf(1) }), "x");
		assertEquals("x", d.get(new PyTuple(new Object[] { "a", Integer.valueOf(1) })));
	}
}
