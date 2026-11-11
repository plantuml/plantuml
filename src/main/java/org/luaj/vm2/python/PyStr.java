package org.luaj.vm2.python;

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
			c = s.codePoints().toArray();
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
		return of(new String(cps(), index, 1));
	}

	/** The str of the {@code count} characters from index {@code from}: both must be in range. */
	PyStr substring(int from, int count) {
		if (count == 0)
			return EMPTY;
		if (from == 0 && count == length())
			return this;
		if (isBmp())
			return of(s.substring(from, from + count));
		return of(new String(cps(), from, count));
	}

	/** Offset in chars of the code point with the given index (the length of the str is allowed). */
	int offset(int cpIndex) {
		return isBmp() ? cpIndex : s.offsetByCodePoints(0, cpIndex);
	}

	/** Index in code points of a char offset. */
	int cpIndexOf(int charOffset) {
		return isBmp() ? charOffset : s.codePointCount(0, charOffset);
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
				sb.appendCodePoint(cpAt(p));
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
