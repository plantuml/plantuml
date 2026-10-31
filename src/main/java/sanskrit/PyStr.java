package sanskrit;

import java.util.ArrayList;

/**
 * The strings of the interpreter. A Python str is a sequence of code points
 * while a Java String is a sequence of UTF-16 units: a character outside the
 * Basic Multilingual Plane (an emoji) is one element for Python and two chars
 * for Java. The rule "one character is one code point" is written here, once.
 * <p>
 * A PyStr is an immutable str: a {@link String} and what is learned about it
 * once, on demand: whether it has a character outside the BMP and, if it has,
 * its code points as an array, so that the length, the subscript and the slices
 * are then O(1) instead of a walk through the String. Almost all the strings
 * have no character outside the BMP, and for them a char is a code point.
 * <p>
 * The methods of the instance are indexed in code points; the offsets are
 * positions in chars of the String.
 * <p>
 * A lone surrogate is a character of a Python str (chr(0xD83D)), and two of
 * them side by side stay two characters. A Java String cannot say that: a high
 * surrogate followed by a low one is one character outside the BMP. So a lone
 * surrogate is never stored as itself: it is stored as two high surrogates, the
 * first one in D800..D81F and the second one in D900..D93F (see
 * {@link #appendCp}). With this form a String is still compared, hashed,
 * concatenated, cut at the limits of characters and searched as it is: a text
 * found in another one is always found at the limits of its characters.
 * Whatever reads the characters one by one goes through {@link #cpAt}; the
 * text given to the host goes through {@link #external}.
 */
final class PyStr {
	/** The str "". */
	static final PyStr EMPTY = new PyStr("");

	private static final PyStr[] ASCII_STRS = asciiStrs();

	private static PyStr[] asciiStrs() {
		PyStr[] table = new PyStr[128];
		for (int i = 0; i < 128; i++)
			table[i] = new PyStr(String.valueOf((char) i));
		return table;
	}

	/** The characters of the str, as the Java String that holds them. */
	final String s;

	/** 0: not known yet, 1: no surrogate (a char is a code point), 2: has surrogates. */
	private byte kind;

	/** The code points, made once and only for a str with surrogates. */
	private volatile int[] codePoints;

	private PyStr(String s) {
		this.s = s;
	}

	/** The str of a value for which {@link #is} is true. */
	static PyStr from(Object o) {
		return o instanceof PyStr ? (PyStr) o : of((String) o);
	}

	/** The str made of this String; the empty str and the ASCII characters are shared. */
	static PyStr of(String s) {
		int n = s.length();
		if (n == 0)
			return EMPTY;
		if (n == 1 && s.charAt(0) < 128)
			return ASCII_STRS[s.charAt(0)];
		return new PyStr(s);
	}

	/** True if the value is a str, in either of its two forms: a PyStr or a bare String. */
	static boolean is(Object o) {
		return o instanceof PyStr || o instanceof String;
	}

	/** The characters of a value for which {@link #is} is true. */
	static String text(Object o) {
		return o instanceof PyStr ? ((PyStr) o).s : (String) o;
	}

	/** The result of an operation on {@code model}: a PyStr if the model is one, else the bare String. */
	static Object like(Object model, String result) {
		return model instanceof PyStr ? of(result) : result;
	}

	/**
	 * The arguments with every PyStr replaced by its String, for the code that
	 * works on Strings: the array itself if there is none.
	 */
	static Object[] unwrapArgs(Object[] a) {
		return unwrapArgs(a, 0);
	}

	/** As {@link #unwrapArgs(Object[])}, but the arguments before index {@code from} are left as they are. */
	static Object[] unwrapArgs(Object[] a, int from) {
		Object[] out = null;
		for (int i = from; i < a.length; i++) {
			if (a[i] instanceof PyStr) {
				if (out == null)
					out = a.clone();
				out[i] = ((PyStr) a[i]).s;
			}
		}
		return out == null ? a : out;
	}

	/**
	 * The result of a method of a str called on a PyStr, as PyStr: a String
	 * becomes one, and so do the Strings in a list or a tuple. The list or the
	 * tuple is the one the method has just made, and is changed in place.
	 */
	static Object wrap(Object r) {
		if (r instanceof String)
			return of((String) r);
		if (r instanceof PyList) {
			final ArrayList<Object> items = ((PyList) r).items;
			for (int i = 0; i < items.size(); i++)
				if (items.get(i) instanceof String)
					items.set(i, of((String) items.get(i)));
		} else if (r instanceof PyTuple) {
			final Object[] items = ((PyTuple) r).items;
			for (int i = 0; i < items.length; i++)
				if (items[i] instanceof String)
					items[i] = of((String) items[i]);
		}
		return r;
	}

	// ------------------------------------------------------------ code points of a String

	private static final int LONE_A = 0xd800, LONE_B = 0xd900;

	private static boolean isSurrogate(int cp) {
		return cp >= 0xd800 && cp <= 0xdfff;
	}

	/** The code point that starts at this offset; a lone surrogate is one. */
	static int cpAt(String s, int i) {
		final char c = s.charAt(i);
		if (c < 0xd800 || c > 0xdbff || i + 1 >= s.length())
			return c;
		final char d = s.charAt(i + 1);
		if (d >= 0xdc00 && d <= 0xdfff)
			return Character.toCodePoint(c, d);
		if (c < LONE_A + 32 && d >= LONE_B && d < LONE_B + 64)
			return 0xd800 + ((c - LONE_A) << 6 | (d - LONE_B));
		return c;
	}

	/** True if a lone surrogate, in its stored form of two chars, starts at this offset. */
	static boolean loneAt(String s, int i) {
		if (i + 1 >= s.length())
			return false;
		final char c = s.charAt(i), d = s.charAt(i + 1);
		return c >= LONE_A && c < LONE_A + 32 && d >= LONE_B && d < LONE_B + 64;
	}

	/** The code point that ends at this offset. */
	static int cpBefore(String s, int i) {
		final char d = s.charAt(i - 1);
		if (d < 0xd800 || d > 0xdfff || i < 2)
			return d;
		final int cp = cpAt(s, i - 2);
		return cpSize(cp) == 2 ? cp : d;
	}

	/** The number of chars of a code point in a String: 2 outside the BMP and for a lone surrogate. */
	static int cpSize(int cp) {
		return cp >= 0x10000 || isSurrogate(cp) ? 2 : 1;
	}

	static StringBuilder appendCp(StringBuilder sb, int cp) {
		if (isSurrogate(cp))
			return sb.append((char) (LONE_A + ((cp - 0xd800) >> 6))).append((char) (LONE_B + ((cp - 0xd800) & 63)));
		return sb.appendCodePoint(cp);
	}

	/** The String of one code point. */
	static String ofCp(int cp) {
		if (cp < 0x10000 && !isSurrogate(cp))
			return String.valueOf((char) cp);
		return appendCp(new StringBuilder(2), cp).toString();
	}

	/** The number of code points of a String. */
	static int cpCount(String s) {
		int n = 0;
		for (int i = 0; i < s.length(); i += cpSize(cpAt(s, i)))
			n++;
		return n;
	}

	/** Offset in chars of the code point with the given index, which may be the number of code points. */
	static int offsetOf(String s, int cpIndex) {
		int i = 0;
		for (int k = 0; k < cpIndex; k++)
			i += cpSize(cpAt(s, i));
		return i;
	}

	/** True if the characters of the String contain this code point. */
	static boolean containsCp(String s, int cp) {
		return cpSize(cp) == 1 ? s.indexOf(cp) >= 0 : s.contains(ofCp(cp));
	}

	private static int[] toCps(String s) {
		final int[] c = new int[cpCount(s)];
		for (int i = 0, k = 0; i < s.length(); i += cpSize(c[k++]))
			c[k] = cpAt(s, i);
		return c;
	}

	private static String fromCps(int[] c, int from, int count) {
		final StringBuilder sb = new StringBuilder(count + 2);
		for (int k = 0; k < count; k++)
			appendCp(sb, c[from + k]);
		return sb.toString();
	}

	/**
	 * The text for the host: a lone surrogate is given as the char it is. (Two
	 * of them side by side are then one character for the host: a Java String
	 * cannot say otherwise.)
	 */
	static String external(String s) {
		if (bmpScan(s))
			return s;
		final StringBuilder sb = new StringBuilder(s.length());
		for (int i = 0; i < s.length();) {
			final int cp = cpAt(s, i);
			i += cpSize(cp);
			sb.appendCodePoint(cp);
		}
		return sb.toString();
	}

	/** True if the str has no surrogate, so that chars are code points. */
	boolean isBmp() {
		byte k = kind;
		if (k == 0) {
			k = bmpScan(s) ? (byte) 1 : (byte) 2;
			kind = k;
		}
		return k == 1;
	}

	private int[] cps() {
		int[] c = codePoints;
		if (c == null) {
			c = toCps(s);
			codePoints = c;
		}
		return c;
	}

	private int cpAt(int index) {
		return isBmp() ? s.charAt(index) : cps()[index];
	}

	/** The number of code points. */
	int length() {
		return isBmp() ? s.length() : cps().length;
	}

	/** The one-character str with the given index, which must be in range. */
	PyStr charAt(int index) {
		if (isBmp()) {
			final char c = s.charAt(index);
			return c < 128 ? ASCII_STRS[c] : new PyStr(String.valueOf(c));
		}
		return of(ofCp(cps()[index]));
	}

	/** The str of the {@code count} characters from index {@code from}: both must be in range. */
	PyStr substring(int from, int count) {
		if (count == 0)
			return EMPTY;
		if (from == 0 && count == length())
			return this;
		if (isBmp())
			return of(s.substring(from, from + count));
		return of(fromCps(cps(), from, count));
	}

	/** Offset in chars of the code point with the given index (the length of the str is allowed). */
	int offset(int cpIndex) {
		return isBmp() ? cpIndex : offsetOf(s, cpIndex);
	}

	/** Index in code points of a char offset. */
	int cpIndexOf(int charOffset) {
		return isBmp() ? charOffset : cpCount(s.substring(0, charOffset));
	}

	/** {@code self[idx]}: idx is an int or a slice. */
	PyStr getitem(Object idx) {
		int len = length();
		if (idx instanceof PySlice) {
			int[] r = new int[3];
			PyOps.sliceRange((PySlice) idx, len, r);
			if (r[2] == 1)
				return substring(r[0], r[1]);
			StringBuilder sb = new StringBuilder();
			for (int k = 0, p = r[0]; k < r[1]; k++, p += r[2])
				appendCp(sb, cpAt(p));
			return of(sb.toString());
		}
		return charAt(PyOps.normIndex(PyOps.asIndex(idx), len, "string"));
	}

	@Override
	public String toString() {
		return s;
	}

	/** Two strs are equal if their characters are: the same rule as for a String. */
	@Override
	public boolean equals(Object o) {
		return o instanceof PyStr && s.equals(((PyStr) o).s);
	}

	@Override
	public int hashCode() {
		return s.hashCode();
	}

	private static boolean bmpScan(String s) {
		for (int i = 0, n = s.length(); i < n; i++) {
			char c = s.charAt(i);
			if (c >= 0xd800 && c <= 0xdfff)
				return false;
		}
		return true;
	}
}
