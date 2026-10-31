package org.luaj.vm2.python;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

/** Number and string formatting: repr of floats, format specs, the % operator. */
final class Fmt {
	private Fmt() {
	}

	// ------------------------------------------------------------ floats

	/** repr(float): the shortest digits that read back as the same value. */
	static String repr(double d) {
		if (d != d)
			return "nan";
		if (Double.isInfinite(d))
			return d > 0 ? "inf" : "-inf";
		if (d == 0)
			return 1 / d < 0 ? "-0.0" : "0.0";
		boolean neg = d < 0;
		double a = Math.abs(d);
		BigDecimal bd = new BigDecimal(a);
		BigDecimal r = null;
		for (int p = 1; p <= 17; p++) {
			r = bd.round(new MathContext(p, RoundingMode.HALF_EVEN));
			if (Double.parseDouble(r.toString()) == a)
				break;
		}
		String digits = r.unscaledValue().toString();
		int decpt = digits.length() - r.scale();
		int end = digits.length();
		while (end > 1 && digits.charAt(end - 1) == '0')
			end--;
		digits = digits.substring(0, end);
		int exp = decpt - 1;
		StringBuilder sb = new StringBuilder();
		if (neg)
			sb.append('-');
		if (exp < -4 || exp >= 16) {
			sb.append(digits.charAt(0));
			if (digits.length() > 1)
				sb.append('.').append(digits, 1, digits.length());
			sb.append('e').append(exp < 0 ? '-' : '+');
			int ae = Math.abs(exp);
			if (ae < 10)
				sb.append('0');
			sb.append(ae);
		} else if (decpt <= 0) {
			sb.append("0.");
			for (int i = 0; i < -decpt; i++)
				sb.append('0');
			sb.append(digits);
		} else if (decpt >= digits.length()) {
			sb.append(digits);
			for (int i = digits.length(); i < decpt; i++)
				sb.append('0');
			sb.append(".0");
		} else {
			sb.append(digits, 0, decpt).append('.').append(digits, decpt, digits.length());
		}
		return sb.toString();
	}

	/** Digits of a finite non-negative value in the fixed notation. */
	private static String fixed(double a, int prec) {
		return new BigDecimal(a).setScale(prec, RoundingMode.HALF_EVEN).toPlainString();
	}

	private static String sci(double a, int prec, boolean upper, boolean alt) {
		String digits;
		int exp;
		if (a == 0) {
			StringBuilder z = new StringBuilder();
			for (int i = 0; i <= prec; i++)
				z.append('0');
			digits = z.toString();
			exp = 0;
		} else {
			BigDecimal r = new BigDecimal(a).round(new MathContext(prec + 1, RoundingMode.HALF_EVEN));
			digits = r.unscaledValue().toString();
			exp = digits.length() - r.scale() - 1;
			StringBuilder z = new StringBuilder(digits);
			while (z.length() < prec + 1)
				z.append('0');
			digits = z.substring(0, prec + 1);
		}
		StringBuilder sb = new StringBuilder();
		sb.append(digits.charAt(0));
		if (prec > 0 || alt)
			sb.append('.');
		sb.append(digits, 1, digits.length());
		sb.append(upper ? 'E' : 'e').append(exp < 0 ? '-' : '+');
		int ae = Math.abs(exp);
		if (ae < 10)
			sb.append('0');
		sb.append(ae);
		return sb.toString();
	}

	private static String general(double a, int prec, boolean upper, boolean alt) {
		if (prec == 0)
			prec = 1;
		int x = 0;
		if (a != 0) {
			BigDecimal r = new BigDecimal(a).round(new MathContext(prec, RoundingMode.HALF_EVEN));
			x = r.unscaledValue().toString().length() - r.scale() - 1;
		}
		String s;
		if (x >= -4 && x < prec)
			s = fixed(a, prec - 1 - x);
		else
			s = sci(a, prec - 1, upper, alt);
		if (!alt) {
			int e = s.indexOf(upper ? 'E' : 'e');
			String mant = e < 0 ? s : s.substring(0, e);
			String tail = e < 0 ? "" : s.substring(e);
			if (mant.indexOf('.') >= 0) {
				int end = mant.length();
				while (mant.charAt(end - 1) == '0')
					end--;
				if (mant.charAt(end - 1) == '.')
					end--;
				mant = mant.substring(0, end);
			}
			s = mant + tail;
		}
		return s;
	}

	/** Unsigned text of a finite or infinite value for a float presentation type. */
	private static String floatBody(double a, char type, int prec, boolean alt) {
		boolean upper = type == 'E' || type == 'F' || type == 'G';
		if (a != a)
			return upper ? "NAN" : "nan";
		if (Double.isInfinite(a))
			return upper ? "INF" : "inf";
		switch (type) {
		case 'f':
		case 'F': {
			String s = fixed(a, prec);
			return alt && prec == 0 ? s + "." : s;
		}
		case 'e':
		case 'E':
			return sci(a, prec, upper, alt);
		default:
			return general(a, prec, upper, alt);
		}
	}

	private static boolean negative(double d) {
		return d < 0 || (d == 0 && 1 / d < 0);
	}

	static Double parseFloat(String text) {
		String s = text.trim();
		if (s.isEmpty())
			return null;
		int i = 0;
		boolean neg = false;
		if (s.charAt(0) == '+' || s.charAt(0) == '-') {
			neg = s.charAt(0) == '-';
			i = 1;
		}
		String rest = s.substring(i).toLowerCase();
		if (rest.equals("inf") || rest.equals("infinity"))
			return Double.valueOf(neg ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);
		if (rest.equals("nan"))
			return Double.valueOf(Double.NaN);
		StringBuilder sb = new StringBuilder();
		if (neg)
			sb.append('-');
		int digits = 0;
		boolean lastDigit = false;
		int n = s.length();
		for (; i < n; i++) {
			char c = s.charAt(i);
			if (c >= '0' && c <= '9') {
				sb.append(c);
				digits++;
				lastDigit = true;
			} else if (c == '_' && lastDigit && i + 1 < n && s.charAt(i + 1) >= '0' && s.charAt(i + 1) <= '9') {
				lastDigit = false;
			} else {
				break;
			}
		}
		if (i < n && s.charAt(i) == '.') {
			sb.append('.');
			i++;
			lastDigit = false;
			for (; i < n; i++) {
				char c = s.charAt(i);
				if (c >= '0' && c <= '9') {
					sb.append(c);
					digits++;
					lastDigit = true;
				} else if (c == '_' && lastDigit && i + 1 < n && s.charAt(i + 1) >= '0' && s.charAt(i + 1) <= '9') {
					lastDigit = false;
				} else {
					break;
				}
			}
		}
		if (digits == 0)
			return null;
		if (i < n && (s.charAt(i) == 'e' || s.charAt(i) == 'E')) {
			sb.append('e');
			i++;
			if (i < n && (s.charAt(i) == '+' || s.charAt(i) == '-'))
				sb.append(s.charAt(i++));
			int ed = 0;
			for (; i < n && s.charAt(i) >= '0' && s.charAt(i) <= '9'; i++, ed++)
				sb.append(s.charAt(i));
			if (ed == 0)
				return null;
		}
		if (i != n)
			return null;
		try {
			return Double.valueOf(Double.parseDouble(sb.toString()));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	// ------------------------------------------------------------ repr of strings

	static String reprStr(String s) {
		boolean sq = s.indexOf('\'') >= 0;
		boolean dq = s.indexOf('"') >= 0;
		char quote = sq && !dq ? '"' : '\'';
		StringBuilder sb = new StringBuilder(s.length() + 2);
		sb.append(quote);
		for (int i = 0; i < s.length();) {
			int cp = s.codePointAt(i);
			i += Character.charCount(cp);
			if (cp == quote || cp == '\\') {
				sb.append('\\').appendCodePoint(cp);
			} else if (cp == '\n') {
				sb.append("\\n");
			} else if (cp == '\r') {
				sb.append("\\r");
			} else if (cp == '\t') {
				sb.append("\\t");
			} else if (cp < 0x20 || cp == 0x7f) {
				sb.append(hexEscape(cp));
			} else if (cp < 0x7f || printable(cp)) {
				sb.appendCodePoint(cp);
			} else {
				sb.append(hexEscape(cp));
			}
		}
		return sb.append(quote).toString();
	}

	static String asciiStr(String s) {
		String r = reprStr(s);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < r.length();) {
			int cp = r.codePointAt(i);
			i += Character.charCount(cp);
			if (cp < 0x80)
				sb.append((char) cp);
			else
				sb.append(hexEscape(cp));
		}
		return sb.toString();
	}

	private static String hexEscape(int cp) {
		String h = Integer.toHexString(cp);
		StringBuilder sb = new StringBuilder();
		int width;
		if (cp < 0x100) {
			sb.append("\\x");
			width = 2;
		} else if (cp < 0x10000) {
			sb.append("\\u");
			width = 4;
		} else {
			sb.append("\\U");
			width = 8;
		}
		for (int i = h.length(); i < width; i++)
			sb.append('0');
		return sb.append(h).toString();
	}

	private static boolean printable(int cp) {
		switch (Character.getType(cp)) {
		case Character.CONTROL:
		case Character.FORMAT:
		case Character.SURROGATE:
		case Character.PRIVATE_USE:
		case Character.UNASSIGNED:
		case Character.LINE_SEPARATOR:
		case Character.PARAGRAPH_SEPARATOR:
		case Character.SPACE_SEPARATOR:
			return cp == ' ';
		default:
			return true;
		}
	}

	// ------------------------------------------------------------ format specs

	private static final class Spec {
		int fill = ' ';
		boolean fillGiven;
		char align;
		char sign = '-';
		boolean alt;
		boolean zero;
		int width = -1;
		char grouping;
		int prec = -1;
		char type;
	}

	private static boolean isAlign(int c) {
		return c == '<' || c == '>' || c == '^' || c == '=';
	}

	private static Spec parseSpec(String spec) {
		Spec s = new Spec();
		int n = spec.length();
		int i = 0;
		if (n > 0) {
			int first = spec.codePointAt(0);
			int fl = Character.charCount(first);
			if (n > fl && isAlign(spec.charAt(fl))) {
				s.fill = first;
				s.fillGiven = true;
				s.align = spec.charAt(fl);
				i = fl + 1;
			} else if (isAlign(first)) {
				s.align = (char) first;
				i = 1;
			}
		}
		if (i < n && (spec.charAt(i) == '+' || spec.charAt(i) == '-' || spec.charAt(i) == ' '))
			s.sign = spec.charAt(i++);
		if (i < n && spec.charAt(i) == '#') {
			s.alt = true;
			i++;
		}
		if (i < n && spec.charAt(i) == '0') {
			s.zero = true;
			i++;
		}
		int w = 0;
		boolean hasW = false;
		while (i < n && spec.charAt(i) >= '0' && spec.charAt(i) <= '9') {
			w = w * 10 + (spec.charAt(i++) - '0');
			hasW = true;
			if (w > 100000000)
				throw PyOps.err(Types.VALUE_ERROR, "Too many decimal digits in format string");
		}
		if (hasW)
			s.width = w;
		if (i < n && (spec.charAt(i) == ',' || spec.charAt(i) == '_'))
			s.grouping = spec.charAt(i++);
		if (i < n && spec.charAt(i) == '.') {
			i++;
			int p = 0;
			boolean hasP = false;
			while (i < n && spec.charAt(i) >= '0' && spec.charAt(i) <= '9') {
				p = p * 10 + (spec.charAt(i++) - '0');
				hasP = true;
				if (p > 100000000)
					throw PyOps.err(Types.VALUE_ERROR, "Too many decimal digits in format string");
			}
			if (!hasP)
				throw PyOps.err(Types.VALUE_ERROR, "Format specifier missing precision");
			s.prec = p;
		}
		if (i < n)
			s.type = spec.charAt(i++);
		if (i < n)
			throw PyOps.err(Types.VALUE_ERROR, "Invalid format specifier '" + spec + "'");
		if (s.zero && !s.fillGiven) {
			s.fill = '0';
			if (s.align == 0)
				s.align = '=';
		}
		return s;
	}

	private static int cpLength(String s) {
		return s.codePointCount(0, s.length());
	}

	private static String repeat(int cp, int n) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++)
			sb.appendCodePoint(cp);
		return sb.toString();
	}

	/** Pads sign + prefix + body to the width. */
	private static String pad(Spec s, String sign, String prefix, String body, char defaultAlign) {
		int len = cpLength(sign) + cpLength(prefix) + cpLength(body);
		if (s.width <= len)
			return sign + prefix + body;
		int n = s.width - len;
		char a = s.align == 0 ? defaultAlign : s.align;
		switch (a) {
		case '<':
			return sign + prefix + body + repeat(s.fill, n);
		case '^': {
			int left = n / 2;
			return repeat(s.fill, left) + sign + prefix + body + repeat(s.fill, n - left);
		}
		case '=':
			return sign + prefix + repeat(s.fill, n) + body;
		default:
			return repeat(s.fill, n) + sign + prefix + body;
		}
	}

	private static String group(String digits, char sep, int size) {
		int n = digits.length();
		if (n <= size)
			return digits;
		StringBuilder sb = new StringBuilder();
		int first = n % size;
		if (first > 0)
			sb.append(digits, 0, first);
		for (int i = first; i < n; i += size) {
			if (sb.length() > 0)
				sb.append(sep);
			sb.append(digits, i, i + size);
		}
		return sb.toString();
	}

	private static String signOf(Spec s, boolean neg) {
		if (neg)
			return "-";
		return s.sign == '+' ? "+" : s.sign == ' ' ? " " : "";
	}

	/** format(value, spec). */
	static String format(Object v, String spec) {
		if (spec.isEmpty())
			return PyOps.str(v);
		Spec s = parseSpec(spec);
		if (v instanceof Boolean && s.type != 0)
			v = Integer.valueOf(((Boolean) v).booleanValue() ? 1 : 0);
		if (v instanceof Integer)
			return formatInt(((Integer) v).intValue(), s);
		if (v instanceof Double)
			return formatFloat(((Double) v).doubleValue(), s);
		if (v instanceof String) {
			if (s.type != 0 && s.type != 's')
				throw PyOps.err(Types.VALUE_ERROR,
						"Unknown format code '" + s.type + "' for object of type 'str'");
			String str = (String) v;
			if (s.prec >= 0 && cpLength(str) > s.prec)
				str = str.substring(0, str.offsetByCodePoints(0, s.prec));
			return pad(s, "", "", str, '<');
		}
		throw PyOps.err(Types.TYPE_ERROR, "unsupported format string passed to " + PyOps.typeName(v) + ".__format__");
	}

	private static String formatInt(int v, Spec s) {
		char t = s.type;
		if (t == 'e' || t == 'E' || t == 'f' || t == 'F' || t == 'g' || t == 'G' || t == '%')
			return formatFloat(v, s);
		long a = Math.abs((long) v);
		String body;
		String prefix = "";
		switch (t) {
		case 0:
		case 'd':
		case 'n':
			body = Long.toString(a);
			if (s.grouping != 0)
				body = group(body, s.grouping, 3);
			break;
		case 'b':
			body = Long.toString(a, 2);
			prefix = s.alt ? "0b" : "";
			if (s.grouping == '_')
				body = group(body, '_', 4);
			break;
		case 'o':
			body = Long.toString(a, 8);
			prefix = s.alt ? "0o" : "";
			if (s.grouping == '_')
				body = group(body, '_', 4);
			break;
		case 'x':
			body = Long.toString(a, 16);
			prefix = s.alt ? "0x" : "";
			if (s.grouping == '_')
				body = group(body, '_', 4);
			break;
		case 'X':
			body = Long.toString(a, 16).toUpperCase();
			prefix = s.alt ? "0X" : "";
			if (s.grouping == '_')
				body = group(body, '_', 4);
			break;
		case 'c':
			if (v < 0 || v > 0x10ffff)
				throw PyOps.err(Types.OVERFLOW, "%c arg not in range(0x110000)");
			return pad(s, "", "", new String(Character.toChars(v)), '<');
		default:
			throw PyOps.err(Types.VALUE_ERROR, "Unknown format code '" + t + "' for object of type 'int'");
		}
		return pad(s, signOf(s, v < 0), prefix, body, '>');
	}

	private static String formatFloat(double d, Spec s) {
		char t = s.type;
		if (t != 0 && "eEfFgGn%".indexOf(t) < 0)
			throw PyOps.err(Types.VALUE_ERROR, "Unknown format code '" + t + "' for object of type 'float'");
		boolean neg = negative(d) && d == d;
		double a = Math.abs(d);
		String body;
		if (t == 0 && s.prec < 0) {
			body = repr(a);
			if (a != a)
				body = "nan";
		} else {
			char tt = t == 0 || t == 'n' ? 'g' : t;
			int prec = s.prec < 0 ? 6 : s.prec;
			if (tt == '%') {
				body = floatBody(a * 100, 'f', prec, s.alt) + "%";
			} else {
				body = floatBody(a, tt, prec, s.alt);
			}
			if (t == 0 && body.indexOf('.') < 0 && body.indexOf('e') < 0 && body.indexOf("n") < 0)
				body += ".0";
		}
		if (s.grouping != 0 && !Double.isInfinite(a) && a == a) {
			int dot = body.indexOf('.');
			int e = body.indexOf('e');
			int end = dot >= 0 ? dot : e >= 0 ? e : body.length();
			body = group(body.substring(0, end), s.grouping, 3) + body.substring(end);
		}
		return pad(s, signOf(s, neg), "", body, '>');
	}

	// ------------------------------------------------------------ the % operator

	static String percent(String fmt, Object arg) {
		Object[] args;
		PyDict mapping = null;
		if (arg instanceof PyTuple)
			args = ((PyTuple) arg).items;
		else
			args = new Object[] { arg };
		if (arg instanceof PyDict)
			mapping = (PyDict) arg;
		int next = 0;
		boolean usedMapping = false;
		StringBuilder out = new StringBuilder();
		int n = fmt.length();
		for (int i = 0; i < n;) {
			char c = fmt.charAt(i++);
			if (c != '%') {
				out.append(c);
				continue;
			}
			Object keyed = null;
			boolean hasKey = false;
			if (i < n && fmt.charAt(i) == '(') {
				int depth = 1;
				int j = i + 1;
				while (j < n && depth > 0) {
					if (fmt.charAt(j) == '(')
						depth++;
					else if (fmt.charAt(j) == ')')
						depth--;
					j++;
				}
				if (depth > 0)
					throw PyOps.err(Types.VALUE_ERROR, "incomplete format key");
				if (mapping == null)
					throw PyOps.err(Types.TYPE_ERROR, "format requires a mapping");
				String key = fmt.substring(i + 1, j - 1);
				keyed = mapping.get(key);
				if (keyed == null)
					throw PyOps.err(Types.KEY_ERROR, PyOps.repr(key));
				hasKey = true;
				usedMapping = true;
				i = j;
			}
			boolean left = false, plus = false, space = false, zero = false, alt = false;
			for (; i < n; i++) {
				char f = fmt.charAt(i);
				if (f == '-')
					left = true;
				else if (f == '+')
					plus = true;
				else if (f == ' ')
					space = true;
				else if (f == '0')
					zero = true;
				else if (f == '#')
					alt = true;
				else
					break;
			}
			int width = -1;
			if (i < n && fmt.charAt(i) == '*') {
				i++;
				if (next >= args.length)
					throw PyOps.err(Types.TYPE_ERROR, "not enough arguments for format string");
				Object w = args[next++];
				if (!(w instanceof Integer))
					throw PyOps.err(Types.TYPE_ERROR, "* wants int");
				width = ((Integer) w).intValue();
				if (width < 0) {
					left = true;
					width = -width;
				}
			} else {
				int w = 0;
				boolean has = false;
				while (i < n && fmt.charAt(i) >= '0' && fmt.charAt(i) <= '9') {
					w = w * 10 + (fmt.charAt(i++) - '0');
					has = true;
					if (w > 100000000)
						throw PyOps.err(Types.VALUE_ERROR, "width too big");
				}
				if (has)
					width = w;
			}
			int prec = -1;
			if (i < n && fmt.charAt(i) == '.') {
				i++;
				if (i < n && fmt.charAt(i) == '*') {
					i++;
					if (next >= args.length)
						throw PyOps.err(Types.TYPE_ERROR, "not enough arguments for format string");
					Object p = args[next++];
					if (!(p instanceof Integer))
						throw PyOps.err(Types.TYPE_ERROR, "* wants int");
					prec = Math.max(0, ((Integer) p).intValue());
				} else {
					int p = 0;
					while (i < n && fmt.charAt(i) >= '0' && fmt.charAt(i) <= '9') {
						p = p * 10 + (fmt.charAt(i++) - '0');
						if (p > 100000000)
							throw PyOps.err(Types.VALUE_ERROR, "precision too big");
					}
					prec = p;
				}
			}
			while (i < n && (fmt.charAt(i) == 'l' || fmt.charAt(i) == 'h' || fmt.charAt(i) == 'L'))
				i++;
			if (i >= n)
				throw PyOps.err(Types.VALUE_ERROR, "incomplete format");
			char type = fmt.charAt(i++);
			if (type == '%') {
				out.append('%');
				continue;
			}
			Object v;
			if (hasKey) {
				v = keyed;
			} else {
				if (next >= args.length)
					throw PyOps.err(Types.TYPE_ERROR, "not enough arguments for format string");
				v = args[next++];
			}
			Spec s = new Spec();
			s.width = width;
			s.prec = prec;
			s.alt = alt;
			s.sign = plus ? '+' : space ? ' ' : '-';
			if (left) {
				s.align = '<';
			} else if (zero && "diouxXeEfFgG".indexOf(type) >= 0) {
				s.fill = '0';
				s.align = '=';
			}
			out.append(percentOne(type, v, s));
		}
		if (next < args.length && !usedMapping)
			throw PyOps.err(Types.TYPE_ERROR, "not all arguments converted during string formatting");
		return out.toString();
	}

	private static String percentOne(char type, Object v, Spec s) {
		switch (type) {
		case 's':
		case 'r':
		case 'a': {
			String str = type == 's' ? PyOps.str(v) : type == 'r' ? PyOps.repr(v) : asciiStr(PyOps.repr(v));
			if (s.prec >= 0 && cpLength(str) > s.prec)
				str = str.substring(0, str.offsetByCodePoints(0, s.prec));
			return pad(s, "", "", str, '>');
		}
		case 'c': {
			String str;
			if (v instanceof String && cpLength((String) v) == 1) {
				str = (String) v;
			} else if (v instanceof Integer || v instanceof Boolean) {
				int cp = PyOps.asInt(v);
				if (cp < 0 || cp > 0x10ffff)
					throw PyOps.err(Types.OVERFLOW, "%c arg not in range(0x110000)");
				str = new String(Character.toChars(cp));
			} else {
				throw PyOps.err(Types.TYPE_ERROR, "%c requires int or char");
			}
			return pad(s, "", "", str, '>');
		}
		case 'd':
		case 'i':
		case 'u':
		case 'o':
		case 'x':
		case 'X': {
			int iv;
			if (v instanceof Integer || v instanceof Boolean) {
				iv = PyOps.asInt(v);
			} else if (v instanceof Double) {
				iv = PyOps.floatToInt(((Double) v).doubleValue());
			} else {
				throw PyOps.err(Types.TYPE_ERROR, "%" + type + " format: a real number is required, not "
						+ PyOps.typeName(v));
			}
			s.type = type == 'i' || type == 'u' ? 'd' : type;
			Spec t = s;
			if (t.prec >= 0) {
				// precision: minimum number of digits
				long a = Math.abs((long) iv);
				String digits = t.type == 'd' ? Long.toString(a) : t.type == 'o' ? Long.toString(a, 8)
						: t.type == 'x' ? Long.toString(a, 16) : Long.toString(a, 16).toUpperCase();
				StringBuilder z = new StringBuilder();
				for (int i = digits.length(); i < t.prec; i++)
					z.append('0');
				String prefix = t.alt ? (t.type == 'o' ? "0o" : t.type == 'x' ? "0x" : t.type == 'X' ? "0X" : "") : "";
				t.fill = ' ';
				if (t.align == '=')
					t.align = 0;
				return pad(t, signOf(t, iv < 0), prefix, z + digits, '>');
			}
			return formatInt(iv, t);
		}
		case 'e':
		case 'E':
		case 'f':
		case 'F':
		case 'g':
		case 'G': {
			double d;
			if (v instanceof Double)
				d = ((Double) v).doubleValue();
			else if (v instanceof Integer || v instanceof Boolean)
				d = PyOps.asInt(v);
			else
				throw PyOps.err(Types.TYPE_ERROR, "must be real number, not " + PyOps.typeName(v));
			s.type = type;
			if (s.prec < 0)
				s.prec = 6;
			return formatFloat(d, s);
		}
		default:
			throw PyOps.err(Types.VALUE_ERROR, "unsupported format character '" + type + "'");
		}
	}

	// ------------------------------------------------------------ str.format

	static String strFormat(String fmt, Object[] args, int firstArg, java.util.Map<String, Object> kw) {
		StringBuilder out = new StringBuilder();
		int n = fmt.length();
		int auto = 0;
		boolean manual = false;
		boolean automatic = false;
		for (int i = 0; i < n;) {
			char c = fmt.charAt(i++);
			if (c == '{') {
				if (i < n && fmt.charAt(i) == '{') {
					out.append('{');
					i++;
					continue;
				}
				int depth = 1;
				int j = i;
				while (j < n && depth > 0) {
					if (fmt.charAt(j) == '{')
						depth++;
					else if (fmt.charAt(j) == '}')
						depth--;
					j++;
				}
				if (depth > 0)
					throw PyOps.err(Types.VALUE_ERROR, "expected '}' before end of string");
				String field = fmt.substring(i, j - 1);
				i = j;
				String spec = "";
				String conv = null;
				int colon = -1;
				for (int k = 0; k < field.length(); k++) {
					char fc = field.charAt(k);
					if (fc == '[') {
						int close = field.indexOf(']', k);
						if (close < 0)
							throw PyOps.err(Types.VALUE_ERROR, "Missing ']' in format string");
						k = close;
					} else if (fc == ':') {
						colon = k;
						break;
					}
				}
				String head = colon >= 0 ? field.substring(0, colon) : field;
				if (colon >= 0)
					spec = field.substring(colon + 1);
				int bang = head.indexOf('!');
				if (bang >= 0) {
					conv = head.substring(bang + 1);
					head = head.substring(0, bang);
				}
				if (spec.indexOf('{') >= 0)
					spec = strFormat(spec, args, firstArg, kw);
				Object value = resolveField(head, args, firstArg, kw, auto);
				if (head.isEmpty()) {
					if (manual)
						throw PyOps.err(Types.VALUE_ERROR,
								"cannot switch from manual field specification to automatic field numbering");
					automatic = true;
					auto++;
				} else if (isDigits(head.split("[.\\[]")[0])) {
					if (automatic)
						throw PyOps.err(Types.VALUE_ERROR,
								"cannot switch from automatic field numbering to manual field specification");
					manual = true;
				}
				if (conv != null) {
					if (conv.equals("r"))
						value = PyOps.repr(value);
					else if (conv.equals("s"))
						value = PyOps.str(value);
					else if (conv.equals("a"))
						value = asciiStr(PyOps.repr(value));
					else
						throw PyOps.err(Types.VALUE_ERROR, "Unknown conversion specifier " + conv);
				}
				out.append(format(value, spec));
			} else if (c == '}') {
				if (i < n && fmt.charAt(i) == '}') {
					out.append('}');
					i++;
				} else {
					throw PyOps.err(Types.VALUE_ERROR, "Single '}' encountered in format string");
				}
			} else {
				out.append(c);
			}
		}
		return out.toString();
	}

	private static boolean isDigits(String s) {
		if (s.isEmpty())
			return false;
		for (int i = 0; i < s.length(); i++)
			if (s.charAt(i) < '0' || s.charAt(i) > '9')
				return false;
		return true;
	}

	private static Object resolveField(String head, Object[] args, int firstArg, java.util.Map<String, Object> kw,
			int auto) {
		int end = 0;
		while (end < head.length() && head.charAt(end) != '.' && head.charAt(end) != '[')
			end++;
		String first = head.substring(0, end);
		Object value;
		if (first.isEmpty()) {
			int idx = firstArg + auto;
			if (idx >= args.length)
				throw PyOps.err(Types.INDEX_ERROR, "Replacement index " + auto + " out of range for positional args tuple");
			value = args[idx];
		} else if (isDigits(first)) {
			int idx = firstArg + Integer.parseInt(first);
			if (idx >= args.length)
				throw PyOps.err(Types.INDEX_ERROR,
						"Replacement index " + first + " out of range for positional args tuple");
			value = args[idx];
		} else {
			value = kw == null ? null : kw.get(first);
			if (value == null)
				throw PyOps.err(Types.KEY_ERROR, PyOps.repr(first));
		}
		int i = end;
		while (i < head.length()) {
			if (head.charAt(i) == '[') {
				int close = head.indexOf(']', i);
				if (close < 0)
					throw PyOps.err(Types.VALUE_ERROR, "Missing ']' in format string");
				String key = head.substring(i + 1, close);
				Object k = isDigits(key) ? (Object) Integer.valueOf(Integer.parseInt(key)) : key;
				value = PyOps.getitem(value, k);
				i = close + 1;
			} else if (head.charAt(i) == '.') {
				int j = i + 1;
				while (j < head.length() && head.charAt(j) != '.' && head.charAt(j) != '[')
					j++;
				throw PyOps.err(Types.ATTRIBUTE_ERROR, "'" + PyOps.typeName(value) + "' object has no attribute '"
						+ head.substring(i + 1, j) + "'");
			} else {
				throw PyOps.err(Types.VALUE_ERROR, "Only '.' or '[' may follow ']' in format field specifier");
			}
		}
		return value;
	}

	@SuppressWarnings("unused")
	private static void unused(List<Object> l) {
	}
}
