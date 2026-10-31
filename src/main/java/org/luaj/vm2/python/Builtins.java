package org.luaj.vm2.python;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Map;

/** The builtin functions, types and exception classes. */
final class Builtins {
	private Builtins() {
	}

	private static boolean initialised;

	static void install(Interp in) {
		init();
		for (PyType t : Types.ALL)
			in.builtins.put(t.name, t);
		for (Builtin b : FUNCTIONS)
			in.builtins.put(b.name, b);
		in.modules.put("math", MathLib.create());
	}

	private static final ArrayList<Builtin> FUNCTIONS = new ArrayList<Builtin>();

	private static void fn(String name, Builtin.Fn f) {
		FUNCTIONS.add(new Builtin(name, f));
	}

	private static void fnKw(String name, Builtin.Fn f, Builtin.KwFn k) {
		FUNCTIONS.add(new Builtin(name, f, k));
	}

	private static void count(String name, Object[] a, int min, int max) {
		if (a.length < min || a.length > max) {
			String exp = min == max ? "exactly " + min : max == Integer.MAX_VALUE ? "at least " + min
					: "from " + min + " to " + max;
			throw PyOps.err(Types.TYPE_ERROR, name + "() takes " + exp + " argument" + (max == 1 ? "" : "s") + " ("
					+ a.length + " given)");
		}
	}

	private static void noKw(String name, Map<String, Object> kw, String... allowed) {
		Methods.noKw(kw, name, allowed);
	}

	// ------------------------------------------------------------ int and float conversion

	private static int parseInt(String text, int base) {
		String s = text.trim();
		int i = 0;
		boolean neg = false;
		if (!s.isEmpty() && (s.charAt(0) == '+' || s.charAt(0) == '-')) {
			neg = s.charAt(0) == '-';
			i = 1;
		}
		if (base == 0 || base == 16 || base == 8 || base == 2) {
			if (i + 1 < s.length() && s.charAt(i) == '0') {
				char p = Character.toLowerCase(s.charAt(i + 1));
				int pb = p == 'x' ? 16 : p == 'o' ? 8 : p == 'b' ? 2 : 0;
				if (pb != 0 && (base == 0 || base == pb)) {
					base = pb;
					i += 2;
				}
			}
			if (base == 0)
				base = 10;
		}
		long v = 0;
		int digits = 0;
		boolean lastDigit = false;
		for (; i < s.length(); i++) {
			char c = s.charAt(i);
			int d;
			if (c == '_' && lastDigit && i + 1 < s.length()) {
				lastDigit = false;
				continue;
			}
			if (c >= '0' && c <= '9')
				d = c - '0';
			else if (c >= 'a' && c <= 'z')
				d = c - 'a' + 10;
			else if (c >= 'A' && c <= 'Z')
				d = c - 'A' + 10;
			else
				d = 99;
			if (d >= base)
				throw PyOps.err(Types.VALUE_ERROR,
						"invalid literal for int() with base " + base + ": " + PyOps.repr(text));
			v = v * base + d;
			if (v > 4294967296L)
				throw PyOps.overflow();
			digits++;
			lastDigit = true;
		}
		if (digits == 0 || !lastDigit)
			throw PyOps.err(Types.VALUE_ERROR, "invalid literal for int() with base " + base + ": " + PyOps.repr(text));
		if (neg)
			v = -v;
		if (v != (int) v)
			throw PyOps.overflow();
		return (int) v;
	}

	private static Object toInt(Object[] a) {
		count("int", a, 0, 2);
		if (a.length == 0)
			return Integer.valueOf(0);
		Object x = a[0];
		if (a.length == 2) {
			if (!(x instanceof String))
				throw PyOps.err(Types.TYPE_ERROR, "int() can't convert non-string with explicit base");
			int base = PyOps.asIndex(a[1]);
			if (base != 0 && (base < 2 || base > 36))
				throw PyOps.err(Types.VALUE_ERROR, "int() base must be >= 2 and <= 36, or 0");
			return Integer.valueOf(parseInt((String) x, base));
		}
		if (x instanceof Integer)
			return x;
		if (x instanceof Boolean)
			return Integer.valueOf(PyOps.asInt(x));
		if (x instanceof Double)
			return Integer.valueOf(PyOps.floatToInt(((Double) x).doubleValue()));
		if (x instanceof String)
			return Integer.valueOf(parseInt((String) x, 10));
		throw PyOps.err(Types.TYPE_ERROR,
				"int() argument must be a string or a real number, not '" + PyOps.typeName(x) + "'");
	}

	private static Object toFloat(Object[] a) {
		count("float", a, 0, 1);
		if (a.length == 0)
			return Double.valueOf(0.0);
		Object x = a[0];
		if (x instanceof Double)
			return x;
		if (PyOps.isNum(x))
			return Double.valueOf(PyOps.asDouble(x));
		if (x instanceof String) {
			Double d = Fmt.parseFloat((String) x);
			if (d == null)
				throw PyOps.err(Types.VALUE_ERROR, "could not convert string to float: " + PyOps.repr(x));
			return d;
		}
		throw PyOps.err(Types.TYPE_ERROR,
				"float() argument must be a string or a real number, not '" + PyOps.typeName(x) + "'");
	}

	private static Object makeDict(Interp in, Object[] a, Map<String, Object> kw) {
		count("dict", a, 0, 1);
		PyDict d = new PyDict();
		Object[] self = new Object[a.length + 1];
		self[0] = d;
		System.arraycopy(a, 0, self, 1, a.length);
		Methods.update(d, self, kw);
		return d;
	}

	private static Object makeRange(Object[] a) {
		count("range", a, 1, 3);
		for (Object o : a)
			if (!PyOps.isIntLike(o))
				throw PyOps.err(Types.TYPE_ERROR, "'" + PyOps.typeName(o) + "' object cannot be interpreted as an integer");
		int start = 0, stop, step = 1;
		if (a.length == 1) {
			stop = PyOps.asInt(a[0]);
		} else {
			start = PyOps.asInt(a[0]);
			stop = PyOps.asInt(a[1]);
			if (a.length == 3)
				step = PyOps.asInt(a[2]);
		}
		if (step == 0)
			throw PyOps.err(Types.VALUE_ERROR, "range() arg 3 must not be zero");
		return new PyRange(start, stop, step);
	}

	// ------------------------------------------------------------ numeric helpers

	private static Object round(Object[] a) {
		count("round", a, 1, 2);
		Object x = a[0];
		boolean hasN = a.length == 2 && a[1] != PyNone.NONE;
		int n = hasN ? PyOps.asIndex(a[1]) : 0;
		if (x instanceof Boolean)
			x = Integer.valueOf(PyOps.asInt(x));
		if (x instanceof Integer) {
			if (!hasN || n >= 0)
				return x;
			if (n < -10)
				return Integer.valueOf(0);
			long r = new BigDecimal(((Integer) x).intValue()).setScale(n, RoundingMode.HALF_EVEN).longValue();
			if (r != (int) r)
				throw PyOps.overflow();
			return Integer.valueOf((int) r);
		}
		if (x instanceof Double) {
			double d = ((Double) x).doubleValue();
			if (!hasN) {
				if (d != d)
					throw PyOps.err(Types.VALUE_ERROR, "cannot convert float NaN to integer");
				if (Double.isInfinite(d))
					throw PyOps.err(Types.OVERFLOW, "cannot convert float infinity to integer");
				return Integer.valueOf(PyOps.floatToInt(Math.rint(d)));
			}
			if (d != d || Double.isInfinite(d) || n > 400)
				return x;
			if (n < -400)
				return Double.valueOf(Math.copySign(0.0, d));
			double r = new BigDecimal(d).setScale(n, RoundingMode.HALF_EVEN).doubleValue();
			return Double.valueOf(r == 0 ? Math.copySign(0.0, d) : r);
		}
		throw PyOps.err(Types.TYPE_ERROR, "type " + PyOps.typeName(x) + " doesn't define __round__ method");
	}

	private static Object divmod(Object x, Object y) {
		if (PyOps.isIntLike(x) && PyOps.isIntLike(y)) {
			int a = PyOps.asInt(x), b = PyOps.asInt(y);
			if (b == 0)
				throw PyOps.err(Types.ZERO_DIVISION, "integer division or modulo by zero");
			long q = Math.floorDiv((long) a, (long) b);
			if (q != (int) q)
				throw PyOps.overflow();
			return new PyTuple(new Object[] { Integer.valueOf((int) q), Integer.valueOf((int) Math.floorMod((long) a, (long) b)) });
		}
		if (PyOps.isNum(x) && PyOps.isNum(y)) {
			double a = PyOps.asDouble(x), b = PyOps.asDouble(y);
			if (b == 0)
				throw PyOps.err(Types.ZERO_DIVISION, "float divmod()");
			return new PyTuple(new Object[] { Double.valueOf(PyOps.floatFloorDiv(a, b)),
					Double.valueOf(PyOps.floatMod(a, b)) });
		}
		throw PyOps.err(Types.TYPE_ERROR, "unsupported operand type(s) for divmod(): '" + PyOps.typeName(x) + "' and '"
				+ PyOps.typeName(y) + "'");
	}

	private static Object pow(Object[] a) {
		count("pow", a, 2, 3);
		if (a.length == 2)
			return PyOps.binary(Op.BIN_POW, a[0], a[1]);
		if (!PyOps.isIntLike(a[0]) || !PyOps.isIntLike(a[1]) || !PyOps.isIntLike(a[2]))
			throw PyOps.err(Types.TYPE_ERROR, "pow() 3rd argument not allowed unless all arguments are integers");
		long base = PyOps.asInt(a[0]), exp = PyOps.asInt(a[1]), mod = PyOps.asInt(a[2]);
		if (mod == 0)
			throw PyOps.err(Types.VALUE_ERROR, "pow() 3rd argument cannot be 0");
		if (exp < 0)
			throw PyOps.err(Types.VALUE_ERROR, "pow() with a negative exponent and a modulus is not supported");
		long m = Math.abs(mod);
		long r = 1 % m;
		long b = Math.floorMod(base, m);
		while (exp > 0) {
			if ((exp & 1) != 0)
				r = r * b % m;
			b = b * b % m;
			exp >>= 1;
		}
		if (mod < 0 && r != 0)
			r += mod;
		return Integer.valueOf((int) r);
	}

	private static String radix(Object x, int radix, String prefix, String name) {
		if (!PyOps.isIntLike(x))
			throw PyOps.err(Types.TYPE_ERROR, "'" + PyOps.typeName(x) + "' object cannot be interpreted as an integer");
		long v = PyOps.asInt(x);
		return (v < 0 ? "-" : "") + prefix + Long.toString(Math.abs(v), radix);
	}

	private static Object minmax(Interp in, String name, Object[] a, Map<String, Object> kw, boolean max) {
		if (kw != null)
			noKw(name, kw, "key", "default");
		Object key = kw != null && kw.containsKey("key") ? kw.get("key") : PyNone.NONE;
		if (a.length == 0)
			throw PyOps.err(Types.TYPE_ERROR, name + " expected at least 1 argument, got 0");
		ArrayList<Object> items;
		if (a.length == 1) {
			items = PyOps.toArrayList(a[0]);
		} else {
			if (kw != null && kw.containsKey("default"))
				throw PyOps.err(Types.TYPE_ERROR,
						"Cannot specify a default for " + name + "() with multiple positional arguments");
			items = new ArrayList<Object>();
			for (Object o : a)
				items.add(o);
		}
		if (items.isEmpty()) {
			if (kw != null && kw.containsKey("default"))
				return kw.get("default");
			throw PyOps.err(Types.VALUE_ERROR, name + "() iterable argument is empty");
		}
		Object best = items.get(0);
		Object bestKey = key == PyNone.NONE ? best : in.call(key, best);
		for (int i = 1; i < items.size(); i++) {
			in.tick();
			Object x = items.get(i);
			Object k = key == PyNone.NONE ? x : in.call(key, x);
			int c = PyOps.order(k, bestKey);
			if (max ? c > 0 : c < 0) {
				best = x;
				bestKey = k;
			}
		}
		return best;
	}

	private static boolean isInstance(Object x, Object t) {
		if (t instanceof PyType)
			return PyOps.typeOf(x).isSubtypeOf((PyType) t);
		if (t instanceof PyTuple) {
			for (Object o : ((PyTuple) t).items)
				if (isInstance(x, o))
					return true;
			return false;
		}
		throw PyOps.err(Types.TYPE_ERROR, "isinstance() arg 2 must be a type or tuple of types");
	}

	// ------------------------------------------------------------ registration

	private static void init() {
		if (initialised)
			return;
		initialised = true;

		Types.INT.ctor = new Builtin("int", (in, a) -> toInt(a));
		Types.FLOAT.ctor = new Builtin("float", (in, a) -> toFloat(a));
		Types.STR.ctor = new Builtin("str", (in, a) -> {
			count("str", a, 0, 1);
			return a.length == 0 ? "" : PyOps.str(a[0]);
		});
		Types.BOOL.ctor = new Builtin("bool", (in, a) -> {
			count("bool", a, 0, 1);
			return Boolean.valueOf(a.length == 1 && PyOps.isTrue(a[0]));
		});
		Types.LIST.ctor = new Builtin("list", (in, a) -> {
			count("list", a, 0, 1);
			return a.length == 0 ? new PyList() : new PyList(PyOps.toArrayList(a[0]));
		});
		Types.TUPLE.ctor = new Builtin("tuple", (in, a) -> {
			count("tuple", a, 0, 1);
			if (a.length == 0)
				return PyTuple.EMPTY;
			if (a[0] instanceof PyTuple)
				return a[0];
			return new PyTuple(PyOps.toArrayList(a[0]).toArray());
		});
		Types.DICT.ctor = new Builtin("dict", (in, a) -> makeDict(in, a, null), (in, a, kw) -> makeDict(in, a, kw));
		Types.RANGE.ctor = new Builtin("range", (in, a) -> makeRange(a));
		for (final PyType t : Types.ALL) {
			if (t.exception)
				t.ctor = new Builtin(t.name, (in, a) -> new PyException(t, a));
		}

		fnKw("print", (in, a) -> {
			printTo(in, a, " ", "\n");
			return PyNone.NONE;
		}, (in, a, kw) -> {
			noKw("print", kw, "sep", "end", "flush");
			Object sep = kw.containsKey("sep") ? kw.get("sep") : PyNone.NONE;
			Object end = kw.containsKey("end") ? kw.get("end") : PyNone.NONE;
			if (sep != PyNone.NONE && !(sep instanceof String))
				throw PyOps.err(Types.TYPE_ERROR, "sep must be None or a string, not " + PyOps.typeName(sep));
			if (end != PyNone.NONE && !(end instanceof String))
				throw PyOps.err(Types.TYPE_ERROR, "end must be None or a string, not " + PyOps.typeName(end));
			printTo(in, a, sep == PyNone.NONE ? " " : (String) sep, end == PyNone.NONE ? "\n" : (String) end);
			return PyNone.NONE;
		});
		fn("len", (in, a) -> {
			count("len", a, 1, 1);
			return Integer.valueOf(PyOps.len(a[0]));
		});
		fn("repr", (in, a) -> {
			count("repr", a, 1, 1);
			return PyOps.repr(a[0]);
		});
		fn("format", (in, a) -> {
			count("format", a, 1, 2);
			return Fmt.format(a[0], a.length > 1 ? (String) a[1] : "");
		});
		fn("abs", (in, a) -> {
			count("abs", a, 1, 1);
			Object x = a[0];
			if (x instanceof Double)
				return Double.valueOf(Math.abs(((Double) x).doubleValue()));
			if (PyOps.isIntLike(x)) {
				int v = PyOps.asInt(x);
				if (v == Integer.MIN_VALUE)
					throw PyOps.overflow();
				return Integer.valueOf(Math.abs(v));
			}
			throw PyOps.err(Types.TYPE_ERROR, "bad operand type for abs(): '" + PyOps.typeName(x) + "'");
		});
		fnKw("min", (in, a) -> minmax(in, "min", a, null, false), (in, a, kw) -> minmax(in, "min", a, kw, false));
		fnKw("max", (in, a) -> minmax(in, "max", a, null, true), (in, a, kw) -> minmax(in, "max", a, kw, true));
		fnKw("sum", (in, a) -> sum(in, a, null), (in, a, kw) -> sum(in, a, kw));
		fnKw("sorted", (in, a) -> {
			count("sorted", a, 1, 1);
			ArrayList<Object> l = PyOps.toArrayList(a[0]);
			PyOps.sortList(l, in, PyNone.NONE, false);
			return new PyList(l);
		}, (in, a, kw) -> {
			count("sorted", a, 1, 1);
			noKw("sorted", kw, "key", "reverse");
			ArrayList<Object> l = PyOps.toArrayList(a[0]);
			PyOps.sortList(l, in, kw.containsKey("key") ? kw.get("key") : PyNone.NONE,
					kw.containsKey("reverse") && PyOps.isTrue(kw.get("reverse")));
			return new PyList(l);
		});
		fn("reversed", (in, a) -> {
			count("reversed", a, 1, 1);
			if (a[0] instanceof PyTuple || a[0] instanceof PyList || a[0] instanceof String || a[0] instanceof PyRange
					|| a[0] instanceof PyDict) {
				ArrayList<Object> l = PyOps.toArrayList(a[0]);
				java.util.Collections.reverse(l);
				return new PyList(l);
			}
			throw PyOps.err(Types.TYPE_ERROR, "'" + PyOps.typeName(a[0]) + "' object is not reversible");
		});
		fnKw("enumerate", (in, a) -> {
			count("enumerate", a, 1, 2);
			return enumerate(in, new Object[] { a[0] }, a.length > 1 ? PyOps.asIndex(a[1]) : 0);
		}, (in, a, kw) -> {
			noKw("enumerate", kw, "start");
			count("enumerate", a, 1, 2);
			Object s = a.length > 1 ? a[1] : kw.containsKey("start") ? kw.get("start") : Integer.valueOf(0);
			return enumerate(in, new Object[] { a[0] }, PyOps.asIndex(s));
		});
		fn("zip", (in, a) -> {
			PyIter[] its = new PyIter[a.length];
			for (int i = 0; i < a.length; i++)
				its[i] = PyOps.getIter(a[i]);
			PyList out = new PyList();
			if (a.length == 0)
				return out;
			outer: while (true) {
				in.tick();
				Object[] row = new Object[a.length];
				for (int i = 0; i < a.length; i++) {
					row[i] = its[i].next();
					if (row[i] == null)
						break outer;
				}
				out.items.add(new PyTuple(row));
				if (out.items.size() > PyOps.MAX_LIST)
					throw PyOps.err(Types.MEMORY_ERROR, "sequence too large");
			}
			return out;
		});
		fn("map", (in, a) -> {
			count("map", a, 2, Integer.MAX_VALUE);
			PyIter[] its = new PyIter[a.length - 1];
			for (int i = 1; i < a.length; i++)
				its[i - 1] = PyOps.getIter(a[i]);
			PyList out = new PyList();
			outer: while (true) {
				in.tick();
				Object[] args = new Object[its.length];
				for (int i = 0; i < its.length; i++) {
					args[i] = its[i].next();
					if (args[i] == null)
						break outer;
				}
				out.items.add(in.callGeneric(a[0], args, 0, args.length, null, null));
			}
			return out;
		});
		fn("filter", (in, a) -> {
			count("filter", a, 2, 2);
			PyIter it = PyOps.getIter(a[1]);
			PyList out = new PyList();
			for (Object x = it.next(); x != null; x = it.next()) {
				in.tick();
				boolean keep = a[0] == PyNone.NONE ? PyOps.isTrue(x) : PyOps.isTrue(in.call(a[0], x));
				if (keep)
					out.items.add(x);
			}
			return out;
		});
		fn("any", (in, a) -> {
			count("any", a, 1, 1);
			PyIter it = PyOps.getIter(a[0]);
			for (Object x = it.next(); x != null; x = it.next()) {
				in.tick();
				if (PyOps.isTrue(x))
					return Boolean.TRUE;
			}
			return Boolean.FALSE;
		});
		fn("all", (in, a) -> {
			count("all", a, 1, 1);
			PyIter it = PyOps.getIter(a[0]);
			for (Object x = it.next(); x != null; x = it.next()) {
				in.tick();
				if (!PyOps.isTrue(x))
					return Boolean.FALSE;
			}
			return Boolean.TRUE;
		});
		fn("isinstance", (in, a) -> {
			count("isinstance", a, 2, 2);
			return Boolean.valueOf(isInstance(a[0], a[1]));
		});
		fn("type", (in, a) -> {
			count("type", a, 1, 1);
			return PyOps.typeOf(a[0]);
		});
		fn("round", (in, a) -> round(a));
		fn("divmod", (in, a) -> {
			count("divmod", a, 2, 2);
			return divmod(a[0], a[1]);
		});
		fn("pow", (in, a) -> pow(a));
		fn("ord", (in, a) -> {
			count("ord", a, 1, 1);
			if (!(a[0] instanceof String))
				throw PyOps.err(Types.TYPE_ERROR, "ord() expected string of length 1, but " + PyOps.typeName(a[0]) + " found");
			String s = (String) a[0];
			if (PyOps.strLen(s) != 1)
				throw PyOps.err(Types.TYPE_ERROR,
						"ord() expected a character, but string of length " + PyOps.strLen(s) + " found");
			return Integer.valueOf(s.codePointAt(0));
		});
		fn("chr", (in, a) -> {
			count("chr", a, 1, 1);
			int v = PyOps.asIndex(a[0]);
			if (v < 0 || v > 0x10ffff)
				throw PyOps.err(Types.VALUE_ERROR, "chr() arg not in range(0x110000)");
			return new String(Character.toChars(v));
		});
		fn("hex", (in, a) -> {
			count("hex", a, 1, 1);
			return radix(a[0], 16, "0x", "hex");
		});
		fn("oct", (in, a) -> {
			count("oct", a, 1, 1);
			return radix(a[0], 8, "0o", "oct");
		});
		fn("bin", (in, a) -> {
			count("bin", a, 1, 1);
			return radix(a[0], 2, "0b", "bin");
		});
		fn("callable", (in, a) -> {
			count("callable", a, 1, 1);
			Object x = a[0];
			return Boolean.valueOf(x instanceof PyFunction || x instanceof Builtin || x instanceof BoundMethod
					|| (x instanceof PyType && ((PyType) x).ctor != null));
		});
	}

	private static void printTo(Interp in, Object[] a, String sep, String end) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < a.length; i++) {
			if (i > 0)
				sb.append(sep);
			sb.append(PyOps.str(a[i]));
		}
		sb.append(end);
		in.write(sb.toString());
	}

	private static Object enumerate(Interp in, Object[] a, int start) {
		count("enumerate", a, 1, 1);
		PyIter it = PyOps.getIter(a[0]);
		PyList out = new PyList();
		long i = start;
		for (Object x = it.next(); x != null; x = it.next()) {
			in.tick();
			if (i > Integer.MAX_VALUE)
				throw PyOps.overflow();
			out.items.add(new PyTuple(new Object[] { Integer.valueOf((int) i++), x }));
			if (out.items.size() > PyOps.MAX_LIST)
				throw PyOps.err(Types.MEMORY_ERROR, "sequence too large");
		}
		return out;
	}

	private static Object sum(Interp in, Object[] a, Map<String, Object> kw) {
		if (kw != null)
			noKw("sum", kw, "start");
		count("sum", a, 1, 2);
		Object acc = a.length > 1 ? a[1] : kw != null && kw.containsKey("start") ? kw.get("start") : Integer.valueOf(0);
		if (acc instanceof String)
			throw PyOps.err(Types.TYPE_ERROR, "sum() can't sum strings [use ''.join(seq) instead]");
		PyIter it = PyOps.getIter(a[0]);
		for (Object x = it.next(); x != null; x = it.next()) {
			in.tick();
			acc = PyOps.binary(Op.BIN_ADD, acc, x);
		}
		return acc;
	}
}
