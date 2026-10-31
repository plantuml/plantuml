package org.luaj.vm2.python;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/** Methods of str, list, dict and tuple. The receiver is {@code args[0]}. */
final class Methods {
	private Methods() {
	}

	private static final Map<String, Builtin> STR = new HashMap<String, Builtin>();
	private static final Map<String, Builtin> LIST = new HashMap<String, Builtin>();
	private static final Map<String, Builtin> DICT = new HashMap<String, Builtin>();
	private static final Map<String, Builtin> TUPLE = new HashMap<String, Builtin>();

	static Builtin lookup(Object o, String name) {
		if (o instanceof String)
			return STR.get(name);
		if (o instanceof PyList)
			return LIST.get(name);
		if (o instanceof PyDict)
			return DICT.get(name);
		if (o instanceof PyTuple)
			return TUPLE.get(name);
		return null;
	}

	private static void def(Map<String, Builtin> table, String name, Builtin.Fn fn) {
		table.put(name, new Builtin(name, fn));
	}

	private static void defKw(Map<String, Builtin> table, String name, Builtin.Fn fn, Builtin.KwFn kfn) {
		table.put(name, new Builtin(name, fn, kfn));
	}

	/** Checks the number of arguments, not counting the receiver. */
	static void need(Object[] a, int min, int max, String name) {
		int n = a.length - 1;
		if (n < min || n > max) {
			String exp = min == max ? "exactly " + min : max == Integer.MAX_VALUE ? "at least " + min
					: "from " + min + " to " + max;
			throw PyOps.err(Types.TYPE_ERROR, name + "() takes " + exp + " argument" + (max == 1 && min == 1 ? "" : "s")
					+ " (" + n + " given)");
		}
	}

	static void noKw(Map<String, Object> kw, String name, String... allowed) {
		for (String k : kw.keySet()) {
			boolean ok = false;
			for (String s : allowed)
				if (s.equals(k))
					ok = true;
			if (!ok)
				throw PyOps.err(Types.TYPE_ERROR, name + "() got an unexpected keyword argument '" + k + "'");
		}
	}

	private static String str(Object[] a, int i, String method) {
		if (!(a[i] instanceof String))
			throw PyOps.err(Types.TYPE_ERROR, method + "() argument must be str, not " + PyOps.typeName(a[i]));
		return (String) a[i];
	}

	static boolean isSpace(int c) {
		return c == 32 || (c >= 9 && c <= 13) || (c >= 28 && c <= 31) || c == 0x85 || c == 0xa0 || c == 0x1680
				|| (c >= 0x2000 && c <= 0x200a) || c == 0x2028 || c == 0x2029 || c == 0x202f || c == 0x205f
				|| c == 0x3000;
	}

	// ------------------------------------------------------------ str

	private static int optIndex(Object[] a, int i, int dflt) {
		if (i >= a.length || a[i] == PyNone.NONE)
			return dflt;
		return PyOps.asIndex(a[i]);
	}

	/** Resolves optional start/end arguments in code points; returns {start, end}. */
	private static int[] window(Object[] a, int first, int len) {
		int start = optIndex(a, first, 0);
		int end = optIndex(a, first + 1, len);
		if (start < 0)
			start = Math.max(0, start + len);
		if (end < 0)
			end = Math.max(0, end + len);
		if (end > len)
			end = len;
		return new int[] { start, end };
	}

	/** Index in code points of sub in s[start:end], or -1. */
	private static int find(String s, String sub, int start, int end, boolean last) {
		int len = PyOps.strLen(s);
		if (start > len)
			return -1;
		if (end < start)
			return -1;
		int so = PyOps.offset(s, start);
		int eo = PyOps.offset(s, end);
		int r;
		if (last) {
			r = eo - sub.length() < so ? -1 : s.lastIndexOf(sub, eo - sub.length());
			if (r < so)
				r = -1;
		} else {
			r = s.indexOf(sub, so);
			if (r >= 0 && r + sub.length() > eo)
				r = -1;
		}
		return r < 0 ? -1 : PyOps.cpIndexOf(s, r);
	}

	private static String stripWith(String s, String chars, boolean left, boolean right) {
		int start = 0, end = s.length();
		if (left) {
			while (start < end) {
				int cp = s.codePointAt(start);
				if (!(chars == null ? isSpace(cp) : chars.indexOf(cp) >= 0))
					break;
				start += Character.charCount(cp);
			}
		}
		if (right) {
			while (end > start) {
				int cp = s.codePointBefore(end);
				if (!(chars == null ? isSpace(cp) : chars.indexOf(cp) >= 0))
					break;
				end -= Character.charCount(cp);
			}
		}
		return s.substring(start, end);
	}

	private static Builtin.Fn strip(final boolean left, final boolean right, final String name) {
		return (in, a) -> {
			need(a, 0, 1, name);
			String chars = a.length > 1 && a[1] != PyNone.NONE ? str(a, 1, name) : null;
			return stripWith((String) a[0], chars, left, right);
		};
	}

	private static Object split(String s, Object sepArg, int maxsplit, boolean fromRight) {
		PyList out = new PyList();
		if (sepArg == PyNone.NONE) {
			int n = s.length();
			ArrayList<String> parts = new ArrayList<String>();
			if (!fromRight) {
				int i = 0;
				while (true) {
					while (i < n && isSpace(s.codePointAt(i)))
						i += Character.charCount(s.codePointAt(i));
					if (i >= n)
						break;
					if (maxsplit >= 0 && parts.size() >= maxsplit) {
						parts.add(stripWith(s.substring(i), null, false, true));
						break;
					}
					int j = i;
					while (j < n && !isSpace(s.codePointAt(j)))
						j += Character.charCount(s.codePointAt(j));
					parts.add(s.substring(i, j));
					i = j;
				}
			} else {
				int i = n;
				ArrayList<String> rev = new ArrayList<String>();
				while (true) {
					while (i > 0 && isSpace(s.codePointBefore(i)))
						i -= Character.charCount(s.codePointBefore(i));
					if (i <= 0)
						break;
					if (maxsplit >= 0 && rev.size() >= maxsplit) {
						rev.add(stripWith(s.substring(0, i), null, true, false));
						break;
					}
					int j = i;
					while (j > 0 && !isSpace(s.codePointBefore(j)))
						j -= Character.charCount(s.codePointBefore(j));
					rev.add(s.substring(j, i));
					i = j;
				}
				for (int k = rev.size() - 1; k >= 0; k--)
					parts.add(rev.get(k));
			}
			out.items.addAll(parts);
			return out;
		}
		if (!(sepArg instanceof String))
			throw PyOps.err(Types.TYPE_ERROR, "must be str or None, not " + PyOps.typeName(sepArg));
		String sep = (String) sepArg;
		if (sep.isEmpty())
			throw PyOps.err(Types.VALUE_ERROR, "empty separator");
		if (!fromRight) {
			int i = 0;
			int count = 0;
			while (true) {
				int j = (maxsplit >= 0 && count >= maxsplit) ? -1 : s.indexOf(sep, i);
				if (j < 0) {
					out.items.add(s.substring(i));
					break;
				}
				out.items.add(s.substring(i, j));
				i = j + sep.length();
				count++;
			}
		} else {
			ArrayList<Object> rev = new ArrayList<Object>();
			int end = s.length();
			int count = 0;
			while (true) {
				int j = (maxsplit >= 0 && count >= maxsplit) ? -1 : (end - sep.length() < 0 ? -1 : s.lastIndexOf(sep, end - sep.length()));
				if (j < 0) {
					rev.add(s.substring(0, end));
					break;
				}
				rev.add(s.substring(j + sep.length(), end));
				end = j;
				count++;
			}
			for (int k = rev.size() - 1; k >= 0; k--)
				out.items.add(rev.get(k));
		}
		return out;
	}

	private static String pad(String s, int width, String fill, int mode) {
		int len = PyOps.strLen(s);
		if (width <= len)
			return s;
		if (PyOps.strLen(fill) != 1)
			throw PyOps.err(Types.TYPE_ERROR, "The fill character must be exactly one character long");
		if (width > PyOps.MAX_STRING)
			throw PyOps.err(Types.MEMORY_ERROR, "string too large");
		int marg = width - len;
		int left = mode == 0 ? 0 : mode == 1 ? marg : marg / 2 + (marg & width & 1);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < left; i++)
			sb.append(fill);
		sb.append(s);
		for (int i = 0; i < marg - left; i++)
			sb.append(fill);
		return sb.toString();
	}

	private static Builtin.Fn padder(final int mode, final String name) {
		return (in, a) -> {
			need(a, 1, 2, name);
			String fill = a.length > 2 ? str(a, 2, name) : " ";
			return pad((String) a[0], PyOps.asIndex(a[1]), fill, mode);
		};
	}

	private static Builtin.Fn allChars(final String name, final int kind) {
		return (in, a) -> {
			need(a, 0, 0, name);
			String s = (String) a[0];
			if (s.isEmpty())
				return Boolean.FALSE;
			boolean cased = false;
			for (int i = 0; i < s.length();) {
				int cp = s.codePointAt(i);
				i += Character.charCount(cp);
				switch (kind) {
				case 0:
					if (!Character.isDigit(cp) && !(cp >= 0xb2 && cp <= 0xb3) && cp != 0xb9)
						return Boolean.FALSE;
					break;
				case 1:
					if (!Character.isLetter(cp))
						return Boolean.FALSE;
					break;
				case 2:
					if (!Character.isLetterOrDigit(cp))
						return Boolean.FALSE;
					break;
				case 3:
					if (!isSpace(cp))
						return Boolean.FALSE;
					break;
				case 4:
					if (Character.isLowerCase(cp) || Character.isTitleCase(cp))
						return Boolean.FALSE;
					if (Character.isUpperCase(cp))
						cased = true;
					break;
				default:
					if (Character.isUpperCase(cp) || Character.isTitleCase(cp))
						return Boolean.FALSE;
					if (Character.isLowerCase(cp))
						cased = true;
					break;
				}
			}
			return Boolean.valueOf(kind < 4 || cased);
		};
	}

	private static Object affix(Object[] a, boolean prefix, String name) {
		need(a, 1, 3, name);
		String s = (String) a[0];
		int len = PyOps.strLen(s);
		int[] w = window(a, 2, len);
		if (w[0] > len)
			return Boolean.FALSE;
		String sub = s.substring(PyOps.offset(s, w[0]), PyOps.offset(s, Math.max(w[0], w[1])));
		Object p = a[1];
		if (p instanceof String)
			return Boolean.valueOf(prefix ? sub.startsWith((String) p) : sub.endsWith((String) p));
		if (p instanceof PyTuple) {
			for (Object o : ((PyTuple) p).items) {
				if (!(o instanceof String))
					throw PyOps.err(Types.TYPE_ERROR, name + " first arg must be str or a tuple of str, not "
							+ PyOps.typeName(o));
				if (prefix ? sub.startsWith((String) o) : sub.endsWith((String) o))
					return Boolean.TRUE;
			}
			return Boolean.FALSE;
		}
		throw PyOps.err(Types.TYPE_ERROR, name + " first arg must be str or a tuple of str, not " + PyOps.typeName(p));
	}

	private static String replace(String s, String old, String rep, int count) {
		if (count < 0)
			count = Integer.MAX_VALUE;
		StringBuilder sb = new StringBuilder();
		if (old.isEmpty()) {
			int n = 0;
			int i = 0;
			while (n < count) {
				sb.append(rep);
				n++;
				if (i >= s.length())
					return sb.toString();
				int cp = s.codePointAt(i);
				sb.appendCodePoint(cp);
				i += Character.charCount(cp);
			}
			sb.append(s, i, s.length());
			return sb.toString();
		}
		int i = 0;
		int n = 0;
		while (n < count) {
			int j = s.indexOf(old, i);
			if (j < 0)
				break;
			sb.append(s, i, j).append(rep);
			i = j + old.length();
			n++;
		}
		sb.append(s, i, s.length());
		if (sb.length() > PyOps.MAX_STRING)
			throw PyOps.err(Types.MEMORY_ERROR, "string too large");
		return sb.toString();
	}

	static {
		def(STR, "upper", (in, a) -> {
			need(a, 0, 0, "upper");
			return ((String) a[0]).toUpperCase(Locale.ROOT);
		});
		def(STR, "lower", (in, a) -> {
			need(a, 0, 0, "lower");
			return ((String) a[0]).toLowerCase(Locale.ROOT);
		});
		def(STR, "title", (in, a) -> {
			need(a, 0, 0, "title");
			String s = (String) a[0];
			StringBuilder sb = new StringBuilder();
			boolean prev = false;
			for (int i = 0; i < s.length();) {
				int cp = s.codePointAt(i);
				i += Character.charCount(cp);
				boolean letter = Character.isLetter(cp);
				sb.appendCodePoint(letter ? (prev ? Character.toLowerCase(cp) : Character.toTitleCase(cp)) : cp);
				prev = letter;
			}
			return sb.toString();
		});
		def(STR, "capitalize", (in, a) -> {
			need(a, 0, 0, "capitalize");
			String s = (String) a[0];
			if (s.isEmpty())
				return s;
			int n = Character.charCount(s.codePointAt(0));
			return s.substring(0, n).toUpperCase(Locale.ROOT).substring(0, 1).concat(s.substring(0, n).toUpperCase(Locale.ROOT).substring(1))
					+ s.substring(n).toLowerCase(Locale.ROOT);
		});
		def(STR, "swapcase", (in, a) -> {
			need(a, 0, 0, "swapcase");
			String s = (String) a[0];
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < s.length();) {
				int cp = s.codePointAt(i);
				i += Character.charCount(cp);
				sb.appendCodePoint(Character.isUpperCase(cp) ? Character.toLowerCase(cp)
						: Character.isLowerCase(cp) ? Character.toUpperCase(cp) : cp);
			}
			return sb.toString();
		});
		for (final boolean right : new boolean[] { false, true }) {
			final String nm = right ? "rpartition" : "partition";
			def(STR, nm, (in, a) -> {
				need(a, 1, 1, nm);
				String s = (String) a[0];
				String sep = str(a, 1, nm);
				if (sep.isEmpty())
					throw PyOps.err(Types.VALUE_ERROR, "empty separator");
				int i = right ? s.lastIndexOf(sep) : s.indexOf(sep);
				if (i < 0)
					return new PyTuple(right ? new Object[] { "", "", s } : new Object[] { s, "", "" });
				return new PyTuple(new Object[] { s.substring(0, i), sep, s.substring(i + sep.length()) });
			});
		}
		def(STR, "removeprefix", (in, a) -> {
			need(a, 1, 1, "removeprefix");
			String s = (String) a[0], p = str(a, 1, "removeprefix");
			return s.startsWith(p) ? s.substring(p.length()) : s;
		});
		def(STR, "removesuffix", (in, a) -> {
			need(a, 1, 1, "removesuffix");
			String s = (String) a[0], p = str(a, 1, "removesuffix");
			return !p.isEmpty() && s.endsWith(p) ? s.substring(0, s.length() - p.length()) : s;
		});
		def(STR, "strip", strip(true, true, "strip"));
		def(STR, "lstrip", strip(true, false, "lstrip"));
		def(STR, "rstrip", strip(false, true, "rstrip"));
		defKw(STR, "split", (in, a) -> {
			need(a, 0, 2, "split");
			return split((String) a[0], a.length > 1 ? a[1] : PyNone.NONE, a.length > 2 ? PyOps.asIndex(a[2]) : -1, false);
		}, (in, a, kw) -> {
			noKw(kw, "split", "sep", "maxsplit");
			Object sep = a.length > 1 ? a[1] : kw.containsKey("sep") ? kw.get("sep") : PyNone.NONE;
			int max = a.length > 2 ? PyOps.asIndex(a[2]) : kw.containsKey("maxsplit") ? PyOps.asIndex(kw.get("maxsplit")) : -1;
			return split((String) a[0], sep, max, false);
		});
		defKw(STR, "rsplit", (in, a) -> {
			need(a, 0, 2, "rsplit");
			return split((String) a[0], a.length > 1 ? a[1] : PyNone.NONE, a.length > 2 ? PyOps.asIndex(a[2]) : -1, true);
		}, (in, a, kw) -> {
			noKw(kw, "rsplit", "sep", "maxsplit");
			Object sep = a.length > 1 ? a[1] : kw.containsKey("sep") ? kw.get("sep") : PyNone.NONE;
			int max = a.length > 2 ? PyOps.asIndex(a[2]) : kw.containsKey("maxsplit") ? PyOps.asIndex(kw.get("maxsplit")) : -1;
			return split((String) a[0], sep, max, true);
		});
		def(STR, "join", (in, a) -> {
			need(a, 1, 1, "join");
			String sep = (String) a[0];
			ArrayList<Object> items = PyOps.toArrayList(a[1]);
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < items.size(); i++) {
				Object o = items.get(i);
				if (!(o instanceof String))
					throw PyOps.err(Types.TYPE_ERROR, "sequence item " + i + ": expected str instance, "
							+ PyOps.typeName(o) + " found");
				if (i > 0)
					sb.append(sep);
				sb.append((String) o);
				if (sb.length() > PyOps.MAX_STRING)
					throw PyOps.err(Types.MEMORY_ERROR, "string too large");
			}
			return sb.toString();
		});
		def(STR, "replace", (in, a) -> {
			need(a, 2, 3, "replace");
			return replace((String) a[0], str(a, 1, "replace"), str(a, 2, "replace"),
					a.length > 3 ? PyOps.asIndex(a[3]) : -1);
		});
		for (final String nm : new String[] { "find", "rfind", "index", "rindex" }) {
			final boolean last = nm.startsWith("r");
			final boolean raise = nm.endsWith("index");
			def(STR, nm, (in, a) -> {
				need(a, 1, 3, nm);
				String s = (String) a[0];
				int[] w = window(a, 2, PyOps.strLen(s));
				int r = find(s, str(a, 1, nm), w[0], w[1], last);
				if (r < 0 && raise)
					throw PyOps.err(Types.VALUE_ERROR, "substring not found");
				return Integer.valueOf(r);
			});
		}
		def(STR, "count", (in, a) -> {
			need(a, 1, 3, "count");
			String s = (String) a[0];
			String sub = str(a, 1, "count");
			int len = PyOps.strLen(s);
			int[] w = window(a, 2, len);
			if (w[0] > len || w[1] < w[0])
				return Integer.valueOf(0);
			String part = s.substring(PyOps.offset(s, w[0]), PyOps.offset(s, w[1]));
			if (sub.isEmpty())
				return Integer.valueOf(PyOps.strLen(part) + 1);
			int n = 0;
			for (int i = part.indexOf(sub); i >= 0; i = part.indexOf(sub, i + sub.length()))
				n++;
			return Integer.valueOf(n);
		});
		def(STR, "startswith", (in, a) -> affix(a, true, "startswith"));
		def(STR, "endswith", (in, a) -> affix(a, false, "endswith"));
		def(STR, "isdigit", allChars("isdigit", 0));
		def(STR, "isalpha", allChars("isalpha", 1));
		def(STR, "isalnum", allChars("isalnum", 2));
		def(STR, "isspace", allChars("isspace", 3));
		def(STR, "isupper", allChars("isupper", 4));
		def(STR, "islower", allChars("islower", 5));
		def(STR, "ljust", padder(0, "ljust"));
		def(STR, "rjust", padder(1, "rjust"));
		def(STR, "center", padder(2, "center"));
		def(STR, "zfill", (in, a) -> {
			need(a, 1, 1, "zfill");
			String s = (String) a[0];
			int width = PyOps.asIndex(a[1]);
			int len = PyOps.strLen(s);
			if (width <= len)
				return s;
			if (width > PyOps.MAX_STRING)
				throw PyOps.err(Types.MEMORY_ERROR, "string too large");
			StringBuilder sb = new StringBuilder();
			int start = 0;
			if (!s.isEmpty() && (s.charAt(0) == '+' || s.charAt(0) == '-')) {
				sb.append(s.charAt(0));
				start = 1;
			}
			for (int i = len; i < width; i++)
				sb.append('0');
			sb.append(s, start, s.length());
			return sb.toString();
		});
		defKw(STR, "splitlines", (in, a) -> splitlines((String) a[0], a.length > 1 && PyOps.isTrue(a[1])),
				(in, a, kw) -> {
					noKw(kw, "splitlines", "keepends");
					return splitlines((String) a[0], a.length > 1 ? PyOps.isTrue(a[1])
							: kw.containsKey("keepends") && PyOps.isTrue(kw.get("keepends")));
				});
		defKw(STR, "format", (in, a) -> Fmt.strFormat((String) a[0], a, 1, null),
				(in, a, kw) -> Fmt.strFormat((String) a[0], a, 1, kw));
	}

	private static Object splitlines(String s, boolean keep) {
		PyList out = new PyList();
		int n = s.length();
		int start = 0;
		int i = 0;
		while (i < n) {
			char c = s.charAt(i);
			boolean brk = c == '\n' || c == '\r' || c == 0x0b || c == 0x0c || (c >= 0x1c && c <= 0x1e) || c == 0x85
					|| c == 0x2028 || c == 0x2029;
			if (!brk) {
				i++;
				continue;
			}
			int end = i;
			i++;
			if (c == '\r' && i < n && s.charAt(i) == '\n')
				i++;
			out.items.add(s.substring(start, keep ? i : end));
			start = i;
		}
		if (start < n)
			out.items.add(s.substring(start));
		return out;
	}

	// ------------------------------------------------------------ list

	static {
		def(LIST, "append", (in, a) -> {
			need(a, 1, 1, "append");
			((PyList) a[0]).items.add(a[1]);
			return PyNone.NONE;
		});
		def(LIST, "extend", (in, a) -> {
			need(a, 1, 1, "extend");
			((PyList) a[0]).items.addAll(PyOps.toArrayList(a[1]));
			return PyNone.NONE;
		});
		def(LIST, "insert", (in, a) -> {
			need(a, 2, 2, "insert");
			ArrayList<Object> l = ((PyList) a[0]).items;
			int i = PyOps.asIndex(a[1]);
			if (i < 0)
				i = Math.max(0, i + l.size());
			if (i > l.size())
				i = l.size();
			l.add(i, a[2]);
			return PyNone.NONE;
		});
		def(LIST, "pop", (in, a) -> {
			need(a, 0, 1, "pop");
			ArrayList<Object> l = ((PyList) a[0]).items;
			int i = a.length > 1 ? PyOps.asIndex(a[1]) : -1;
			if (l.isEmpty())
				throw PyOps.err(Types.INDEX_ERROR, "pop from empty list");
			if (i < 0)
				i += l.size();
			if (i < 0 || i >= l.size())
				throw PyOps.err(Types.INDEX_ERROR, "pop index out of range");
			return l.remove(i);
		});
		def(LIST, "remove", (in, a) -> {
			need(a, 1, 1, "remove");
			ArrayList<Object> l = ((PyList) a[0]).items;
			for (int i = 0; i < l.size(); i++)
				if (PyOps.eq(l.get(i), a[1])) {
					l.remove(i);
					return PyNone.NONE;
				}
			throw PyOps.err(Types.VALUE_ERROR, "list.remove(x): x not in list");
		});
		def(LIST, "index", (in, a) -> {
			need(a, 1, 3, "index");
			ArrayList<Object> l = ((PyList) a[0]).items;
			int[] w = window(a, 2, l.size());
			for (int i = w[0]; i < w[1]; i++)
				if (PyOps.eq(l.get(i), a[1]))
					return Integer.valueOf(i);
			throw PyOps.err(Types.VALUE_ERROR, PyOps.repr(a[1]) + " is not in list");
		});
		def(LIST, "count", (in, a) -> {
			need(a, 1, 1, "count");
			int n = 0;
			for (Object o : ((PyList) a[0]).items)
				if (PyOps.eq(o, a[1]))
					n++;
			return Integer.valueOf(n);
		});
		defKw(LIST, "sort", (in, a) -> {
			need(a, 0, 0, "sort");
			PyOps.sortList(((PyList) a[0]).items, in, PyNone.NONE, false);
			return PyNone.NONE;
		}, (in, a, kw) -> {
			need(a, 0, 0, "sort");
			noKw(kw, "sort", "key", "reverse");
			PyOps.sortList(((PyList) a[0]).items, in, kw.containsKey("key") ? kw.get("key") : PyNone.NONE,
					kw.containsKey("reverse") && PyOps.isTrue(kw.get("reverse")));
			return PyNone.NONE;
		});
		def(LIST, "reverse", (in, a) -> {
			need(a, 0, 0, "reverse");
			java.util.Collections.reverse(((PyList) a[0]).items);
			return PyNone.NONE;
		});
		def(LIST, "clear", (in, a) -> {
			need(a, 0, 0, "clear");
			((PyList) a[0]).items.clear();
			return PyNone.NONE;
		});
		def(LIST, "copy", (in, a) -> {
			need(a, 0, 0, "copy");
			return new PyList(new ArrayList<Object>(((PyList) a[0]).items));
		});
	}

	// ------------------------------------------------------------ tuple

	static {
		def(TUPLE, "index", (in, a) -> {
			need(a, 1, 3, "index");
			Object[] t = ((PyTuple) a[0]).items;
			int[] w = window(a, 2, t.length);
			for (int i = w[0]; i < w[1]; i++)
				if (PyOps.eq(t[i], a[1]))
					return Integer.valueOf(i);
			throw PyOps.err(Types.VALUE_ERROR, "tuple.index(x): x not in tuple");
		});
		def(TUPLE, "count", (in, a) -> {
			need(a, 1, 1, "count");
			int n = 0;
			for (Object o : ((PyTuple) a[0]).items)
				if (PyOps.eq(o, a[1]))
					n++;
			return Integer.valueOf(n);
		});
	}

	// ------------------------------------------------------------ dict

	static void update(PyDict d, Object[] a, Map<String, Object> kw) {
		if (a.length > 2)
			throw PyOps.err(Types.TYPE_ERROR, "update expected at most 1 argument, got " + (a.length - 1));
		if (a.length == 2) {
			Object src = a[1];
			if (src instanceof PyDict) {
				for (PyDict.Entry e : new ArrayList<PyDict.Entry>(((PyDict) src).map.values()))
					d.put(e.key, e.value);
			} else {
				int i = 0;
				for (Object item : PyOps.toArrayList(src)) {
					ArrayList<Object> pair;
					try {
						pair = PyOps.toArrayList(item);
					} catch (PyError e) {
						throw PyOps.err(Types.TYPE_ERROR, "cannot convert dictionary update sequence element #" + i
								+ " to a sequence");
					}
					if (pair.size() != 2)
						throw PyOps.err(Types.VALUE_ERROR, "dictionary update sequence element #" + i
								+ " has length " + pair.size() + "; 2 is required");
					d.put(pair.get(0), pair.get(1));
					i++;
				}
			}
		}
		if (kw != null)
			for (Map.Entry<String, Object> e : kw.entrySet())
				d.put(e.getKey(), e.getValue());
	}

	static {
		def(DICT, "get", (in, a) -> {
			need(a, 1, 2, "get");
			Object v = ((PyDict) a[0]).get(a[1]);
			return v != null ? v : a.length > 2 ? a[2] : PyNone.NONE;
		});
		def(DICT, "keys", (in, a) -> {
			need(a, 0, 0, "keys");
			return new PyView((PyDict) a[0], PyView.KEYS);
		});
		def(DICT, "values", (in, a) -> {
			need(a, 0, 0, "values");
			return new PyView((PyDict) a[0], PyView.VALUES);
		});
		def(DICT, "items", (in, a) -> {
			need(a, 0, 0, "items");
			return new PyView((PyDict) a[0], PyView.ITEMS);
		});
		def(DICT, "pop", (in, a) -> {
			need(a, 1, 2, "pop");
			Object v = ((PyDict) a[0]).remove(a[1]);
			if (v != null)
				return v;
			if (a.length > 2)
				return a[2];
			throw new PyError(new PyException(Types.KEY_ERROR, new Object[] { a[1] }));
		});
		defKw(DICT, "update", (in, a) -> {
			update((PyDict) a[0], a, null);
			return PyNone.NONE;
		}, (in, a, kw) -> {
			update((PyDict) a[0], a, kw);
			return PyNone.NONE;
		});
		def(DICT, "setdefault", (in, a) -> {
			need(a, 1, 2, "setdefault");
			PyDict d = (PyDict) a[0];
			Object v = d.get(a[1]);
			if (v != null)
				return v;
			Object dflt = a.length > 2 ? a[2] : PyNone.NONE;
			d.put(a[1], dflt);
			return dflt;
		});
		def(DICT, "clear", (in, a) -> {
			need(a, 0, 0, "clear");
			((PyDict) a[0]).map.clear();
			return PyNone.NONE;
		});
		def(DICT, "copy", (in, a) -> {
			need(a, 0, 0, "copy");
			PyDict r = new PyDict();
			for (PyDict.Entry e : ((PyDict) a[0]).map.values())
				r.put(e.key, e.value);
			return r;
		});
	}
}
