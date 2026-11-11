package org.luaj.vm2.python;

import java.util.ArrayList;

/**
 * The json module: <code>loads(s)</code> and <code>dumps(obj)</code>, written
 * by hand (no regex, no reflection) so that the result is the same on every
 * edition. <code>dumps</code> gives CPython's default output: separators
 * <code>", "</code> and <code>": "</code>, non-ASCII characters escaped, dict
 * keys in insertion order. Integers are 32-bit like everywhere else.
 */
final class JsonLib {
	private static final int MAX_DEPTH = 200;

	private JsonLib() {
	}

	static PyModule create() {
		final PyModule m = new PyModule("json");
		m.put("loads", (in, a) -> {
			one("loads", a);
			if (PyStr.is(a[0]) == false)
				throw PyOps.err(Types.TYPE_ERROR, "the JSON object must be str, not '" + PyOps.typeName(a[0]) + "'");
			return new Parser(PyStr.text(a[0])).parse();
		});
		m.put("dumps", (in, a) -> {
			one("dumps", a);
			final StringBuilder sb = new StringBuilder();
			dump(a[0], sb, 0);
			return sb.toString();
		});
		return m;
	}

	private static void one(String name, Object[] a) {
		if (a.length != 1)
			throw PyOps.err(Types.TYPE_ERROR, name + "() takes exactly 1 argument (" + a.length + " given)");
	}

	// ------------------------------------------------------------ dumps

	private static void dump(Object o, StringBuilder sb, int depth) {
		if (depth > MAX_DEPTH)
			throw PyOps.err(Types.RECURSION_ERROR, "maximum recursion depth exceeded while encoding a JSON object");
		if (PyStr.is(o)) {
			string(PyStr.text(o), sb);
		} else if (o instanceof Boolean) {
			sb.append(((Boolean) o).booleanValue() ? "true" : "false");
		} else if (o instanceof Integer) {
			sb.append(((Integer) o).intValue());
		} else if (o instanceof Double) {
			sb.append(number(((Double) o).doubleValue()));
		} else if (o == PyNone.NONE) {
			sb.append("null");
		} else if (o instanceof PyList) {
			array(((PyList) o).items.toArray(), sb, depth);
		} else if (o instanceof PyTuple) {
			array(((PyTuple) o).items, sb, depth);
		} else if (o instanceof PyDict) {
			sb.append('{');
			boolean first = true;
			for (PyDict.Entry e : ((PyDict) o).map.values()) {
				if (first == false)
					sb.append(", ");
				first = false;
				string(key(e.key), sb);
				sb.append(": ");
				dump(e.value, sb, depth + 1);
			}
			sb.append('}');
		} else {
			throw PyOps.err(Types.TYPE_ERROR, "Object of type " + PyOps.typeName(o) + " is not JSON serializable");
		}
	}

	private static void array(Object[] items, StringBuilder sb, int depth) {
		sb.append('[');
		for (int i = 0; i < items.length; i++) {
			if (i > 0)
				sb.append(", ");
			dump(items[i], sb, depth + 1);
		}
		sb.append(']');
	}

	/** A dict key as JSON wants it: always a string, as json.dumps converts it. */
	private static String key(Object k) {
		if (PyStr.is(k))
			return PyStr.text(k);
		if (k instanceof Boolean)
			return ((Boolean) k).booleanValue() ? "true" : "false";
		if (k instanceof Integer)
			return String.valueOf(((Integer) k).intValue());
		if (k instanceof Double)
			return number(((Double) k).doubleValue());
		if (k == PyNone.NONE)
			return "null";
		throw PyOps.err(Types.TYPE_ERROR, "keys must be str, int, float, bool or None, not " + PyOps.typeName(k));
	}

	private static String number(double d) {
		if (d != d)
			return "NaN";
		if (Double.isInfinite(d))
			return d > 0 ? "Infinity" : "-Infinity";
		return Fmt.repr(d);
	}

	private static void string(String s, StringBuilder sb) {
		sb.append('"');
		for (int i = 0; i < s.length(); i++) {
			final char ch = s.charAt(i);
			switch (ch) {
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			default:
				if (ch < 0x20 || ch > 0x7e) {
					sb.append("\\u");
					final String hex = Integer.toHexString(ch);
					for (int k = hex.length(); k < 4; k++)
						sb.append('0');
					sb.append(hex);
				} else {
					sb.append(ch);
				}
			}
		}
		sb.append('"');
	}

	// ------------------------------------------------------------ loads

	private static final class Parser {
		private final String s;
		private int pos;

		Parser(String s) {
			this.s = s;
		}

		Object parse() {
			skipSpaces();
			final Object result = value(0);
			skipSpaces();
			if (pos < s.length())
				throw error("Extra data");
			return result;
		}

		private PyError error(String message) {
			return PyOps.err(Types.VALUE_ERROR, message + ": char " + pos);
		}

		private void skipSpaces() {
			while (pos < s.length()) {
				final char ch = s.charAt(pos);
				if (ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r')
					pos++;
				else
					break;
			}
		}

		private Object value(int depth) {
			if (depth > MAX_DEPTH)
				throw PyOps.err(Types.RECURSION_ERROR, "maximum recursion depth exceeded while decoding a JSON document");
			if (pos >= s.length())
				throw error("Expecting value");
			final char ch = s.charAt(pos);
			if (ch == '{')
				return object(depth);
			if (ch == '[')
				return array(depth);
			if (ch == '"')
				return string();
			if (s.startsWith("true", pos)) {
				pos += 4;
				return Boolean.TRUE;
			}
			if (s.startsWith("false", pos)) {
				pos += 5;
				return Boolean.FALSE;
			}
			if (s.startsWith("null", pos)) {
				pos += 4;
				return PyNone.NONE;
			}
			if (ch == '-' || (ch >= '0' && ch <= '9'))
				return number();
			throw error("Expecting value");
		}

		private Object object(int depth) {
			final PyDict dict = new PyDict();
			pos++; // {
			skipSpaces();
			if (pos < s.length() && s.charAt(pos) == '}') {
				pos++;
				return dict;
			}
			while (true) {
				skipSpaces();
				if (pos >= s.length() || s.charAt(pos) != '"')
					throw error("Expecting property name enclosed in double quotes");
				final String key = string();
				skipSpaces();
				if (pos >= s.length() || s.charAt(pos) != ':')
					throw error("Expecting ':' delimiter");
				pos++;
				skipSpaces();
				dict.put(key, value(depth + 1));
				skipSpaces();
				if (pos < s.length() && s.charAt(pos) == ',') {
					pos++;
					continue;
				}
				if (pos < s.length() && s.charAt(pos) == '}') {
					pos++;
					return dict;
				}
				throw error("Expecting ',' delimiter");
			}
		}

		private Object array(int depth) {
			final ArrayList<Object> items = new ArrayList<Object>();
			pos++; // [
			skipSpaces();
			if (pos < s.length() && s.charAt(pos) == ']') {
				pos++;
				return new PyList(items);
			}
			while (true) {
				skipSpaces();
				items.add(value(depth + 1));
				skipSpaces();
				if (pos < s.length() && s.charAt(pos) == ',') {
					pos++;
					continue;
				}
				if (pos < s.length() && s.charAt(pos) == ']') {
					pos++;
					return new PyList(items);
				}
				throw error("Expecting ',' delimiter");
			}
		}

		private String string() {
			pos++; // opening quote
			final StringBuilder sb = new StringBuilder();
			while (true) {
				if (pos >= s.length())
					throw error("Unterminated string starting at");
				final char ch = s.charAt(pos++);
				if (ch == '"')
					return sb.toString();
				if (ch < 0x20)
					throw error("Invalid control character at");
				if (ch != '\\') {
					sb.append(ch);
					continue;
				}
				if (pos >= s.length())
					throw error("Unterminated string starting at");
				final char esc = s.charAt(pos++);
				switch (esc) {
				case '"':
				case '\\':
				case '/':
					sb.append(esc);
					break;
				case 'b':
					sb.append('\b');
					break;
				case 'f':
					sb.append('\f');
					break;
				case 'n':
					sb.append('\n');
					break;
				case 'r':
					sb.append('\r');
					break;
				case 't':
					sb.append('\t');
					break;
				case 'u':
					if (pos + 4 > s.length())
						throw error("Invalid \\uXXXX escape");
					int code = 0;
					for (int k = 0; k < 4; k++) {
						final int digit = Character.digit(s.charAt(pos + k), 16);
						if (digit < 0)
							throw error("Invalid \\uXXXX escape");
						code = code * 16 + digit;
					}
					pos += 4;
					sb.append((char) code);
					break;
				default:
					throw error("Invalid \\escape");
				}
			}
		}

		private Object number() {
			final int start = pos;
			if (s.charAt(pos) == '-')
				pos++;
			if (pos >= s.length() || isDigit(s.charAt(pos)) == false)
				throw error("Expecting value");
			if (s.charAt(pos) == '0')
				pos++;
			else
				while (pos < s.length() && isDigit(s.charAt(pos)))
					pos++;
			boolean isFloat = false;
			if (pos + 1 < s.length() && s.charAt(pos) == '.' && isDigit(s.charAt(pos + 1))) {
				isFloat = true;
				pos++;
				while (pos < s.length() && isDigit(s.charAt(pos)))
					pos++;
			}
			if (pos < s.length() && (s.charAt(pos) == 'e' || s.charAt(pos) == 'E')) {
				int p = pos + 1;
				if (p < s.length() && (s.charAt(p) == '+' || s.charAt(p) == '-'))
					p++;
				if (p < s.length() && isDigit(s.charAt(p))) {
					isFloat = true;
					while (p < s.length() && isDigit(s.charAt(p)))
						p++;
					pos = p;
				}
			}
			final String text = s.substring(start, pos);
			if (isFloat)
				return Double.valueOf(Double.parseDouble(text));
			try {
				return Integer.valueOf(Integer.parseInt(text));
			} catch (NumberFormatException e) {
				throw PyOps.err(Types.OVERFLOW, "integer overflow (this interpreter has 32-bit integers)");
			}
		}

		private static boolean isDigit(char ch) {
			return ch >= '0' && ch <= '9';
		}
	}
}
