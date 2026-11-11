package org.luaj.vm2.python;

/**
 * The strings of the interpreter. A Python str is a {@link String}, but a
 * Python str is a sequence of code points while a Java String is a sequence of
 * UTF-16 units: a character outside the Basic Multilingual Plane (an emoji) is
 * one element for Python and two chars for Java. The rule "one character is one
 * code point" is written here, once: the length of a str, the position of a
 * character, the str made of the characters between two positions, and the
 * subscript of a str. Positions given to and returned by these methods are
 * indices in code points, offsets are positions in chars of the Java String.
 * <p>
 * Almost all the strings have no character outside the BMP, and for them a
 * char is a code point; {@link #bmp} tells it, and every method has a fast path
 * for that case.
 */
final class PyStr {
	private PyStr() {
	}

	/** The answer of {@link #bmp} for the last long string: an immutable pair, so that it is read and replaced as a whole. */
	private static final class BmpAnswer {
		final String string;
		final boolean bmp;

		BmpAnswer(String string, boolean bmp) {
			this.string = string;
			this.bmp = bmp;
		}
	}

	private static volatile BmpAnswer lastAnswer;

	/** True if the string has no surrogate pair, so that chars are code points. */
	static boolean bmp(String s) {
		int n = s.length();
		if (n > 32) {
			BmpAnswer last = lastAnswer;
			if (last != null && last.string == s)
				return last.bmp;
		}
		boolean r = true;
		for (int i = 0; i < n; i++) {
			char c = s.charAt(i);
			if (c >= 0xd800 && c <= 0xdfff) {
				r = false;
				break;
			}
		}
		if (n > 32)
			lastAnswer = new BmpAnswer(s, r);
		return r;
	}

	/** The length of the str: its number of code points. */
	static int length(String s) {
		return bmp(s) ? s.length() : s.codePointCount(0, s.length());
	}

	private static final String[] ASCII = asciiStrings();

	private static String[] asciiStrings() {
		String[] table = new String[128];
		for (int i = 0; i < 128; i++)
			table[i] = String.valueOf((char) i);
		return table;
	}

	/** The str of one char, which is a whole character: a BMP one. */
	static String charString(char c) {
		return c < 128 ? ASCII[c] : String.valueOf(c);
	}

	/** Offset in chars of the code point with the given index. */
	static int offset(String s, int cpIndex) {
		return bmp(s) ? cpIndex : s.offsetByCodePoints(0, cpIndex);
	}

	/** Index in code points of a char offset. */
	static int cpIndexOf(String s, int charOffset) {
		return bmp(s) ? charOffset : s.codePointCount(0, charOffset);
	}

	/** The str of the character with the given index, which must be in range. */
	static String charAt(String s, int cpIndex) {
		if (bmp(s))
			return charString(s.charAt(cpIndex));
		int off = s.offsetByCodePoints(0, cpIndex);
		return s.substring(off, off + Character.charCount(s.codePointAt(off)));
	}

	/** The str of the {@code count} characters from index {@code from}: both must be in range. */
	static String substring(String s, int from, int count) {
		if (count == 0)
			return "";
		int start = offset(s, from);
		int end = bmp(s) ? start + count : s.offsetByCodePoints(start, count);
		return s.substring(start, end);
	}

	/** {@code s[idx]}: idx is an int or a slice. */
	static Object getitem(String s, Object idx) {
		int len = length(s);
		if (idx instanceof PySlice) {
			int[] r = new int[3];
			PyOps.sliceRange((PySlice) idx, len, r);
			if (r[2] == 1)
				return substring(s, r[0], r[1]);
			StringBuilder sb = new StringBuilder();
			for (int k = 0, p = r[0]; k < r[1]; k++, p += r[2])
				sb.append(charAt(s, p));
			return sb.toString();
		}
		return charAt(s, PyOps.normIndex(PyOps.asIndex(idx), len, "string"));
	}
}
