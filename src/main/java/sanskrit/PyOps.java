package sanskrit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/** The operations of the language on the builtin values. */
final class PyOps {
	private PyOps() {
	}

	static final int MAX_LIST = 1 << 24;
	static final int MAX_STRING = 1 << 26;
	private static final int MAX_NESTING = 200;

	// ------------------------------------------------------------ errors and types

	static PyError err(PyType type, String message) {
		return new PyError(new PyException(type, new Object[] { message }));
	}

	static PyType typeOf(Object o) {
		if (o instanceof Integer)
			return Types.INT;
		if (PyStr.is(o))
			return Types.STR;
		if (o instanceof Double)
			return Types.FLOAT;
		if (o instanceof Boolean)
			return Types.BOOL;
		if (o == PyNone.NONE)
			return Types.NONE_TYPE;
		if (o instanceof PyList)
			return Types.LIST;
		if (o instanceof PyTuple)
			return Types.TUPLE;
		if (o instanceof PyDict)
			return Types.DICT;
		if (o instanceof PySet)
			return Types.SET;
		if (o instanceof PyRange)
			return Types.RANGE;
		if (o instanceof PyFunction)
			return Types.FUNCTION;
		if (o instanceof Builtin)
			return Types.BUILTIN;
		if (o instanceof BoundMethod)
			return Types.METHOD;
		if (o instanceof PyInstance)
			return ((PyInstance) o).type;
		if (o instanceof PyException)
			return ((PyException) o).type;
		if (o instanceof PyType)
			return Types.TYPE;
		if (o instanceof PyModule)
			return Types.MODULE;
		if (o instanceof PySlice)
			return Types.SLICE;
		if (o instanceof PyView)
			return ((PyView) o).type();
		if (o instanceof PyNotImplemented)
			return Types.NOT_IMPLEMENTED_TYPE;
		return Types.ITERATOR;
	}

	static String typeName(Object o) {
		return typeOf(o).name;
	}

	static boolean isTrue(Object v) {
		if (v instanceof Boolean)
			return ((Boolean) v).booleanValue();
		if (v instanceof Integer)
			return ((Integer) v).intValue() != 0;
		if (v == PyNone.NONE)
			return false;
		if (PyStr.is(v))
			return !PyStr.text(v).isEmpty();
		if (v instanceof Double)
			return ((Double) v).doubleValue() != 0.0;
		if (v instanceof PyList)
			return !((PyList) v).items.isEmpty();
		if (v instanceof PyTuple)
			return ((PyTuple) v).items.length != 0;
		if (v instanceof PyDict)
			return ((PyDict) v).size() != 0;
		if (v instanceof PySet)
			return ((PySet) v).size() != 0;
		if (v instanceof PyRange)
			return ((PyRange) v).longLength() != 0;
		if (v instanceof PyView)
			return ((PyView) v).dict.size() != 0;
		return true;
	}

	/** MemoryError if a list cannot have this number of items. */
	static void checkList(long size) {
		if (size > MAX_LIST)
			throw err(Types.MEMORY_ERROR, "list too large");
	}

	/** MemoryError if a str cannot have this number of characters. */
	static void checkString(long length) {
		if (length > MAX_STRING)
			throw err(Types.MEMORY_ERROR, "string too large");
	}

	/** The number of items or characters of a value, for the count of the work on it; 0 if it has none. */
	static long size(Object o) {
		if (o instanceof String)
			return ((String) o).length();
		if (o instanceof PyStr)
			return ((PyStr) o).s.length();
		if (o instanceof PyList)
			return ((PyList) o).items.size();
		if (o instanceof PyTuple)
			return ((PyTuple) o).items.length;
		if (o instanceof PyDict)
			return ((PyDict) o).size();
		if (o instanceof PySet)
			return ((PySet) o).size();
		return 0;
	}

	/** list.extend(v), l += v and [*v]: the limit, the work and the memory. */
	static void extend(Interp in, ArrayList<Object> items, Object v) {
		final ArrayList<Object> src = toArrayList(v);
		checkList((long) items.size() + src.size());
		in.work(src.size());
		in.allocated((long) Memory.SLOT * src.size());
		items.addAll(src);
	}

	// ------------------------------------------------------------ numbers

	static boolean isIntLike(Object o) {
		return o instanceof Integer || o instanceof Boolean;
	}

	static boolean isNum(Object o) {
		return o instanceof Integer || o instanceof Double || o instanceof Boolean;
	}

	static int asInt(Object o) {
		if (o instanceof Integer)
			return ((Integer) o).intValue();
		if (o instanceof Boolean)
			return ((Boolean) o).booleanValue() ? 1 : 0;
		throw err(Types.TYPE_ERROR, "an integer is required, not '" + typeName(o) + "'");
	}

	static double asDouble(Object o) {
		if (o instanceof Double)
			return ((Double) o).doubleValue();
		if (o instanceof Integer)
			return ((Integer) o).intValue();
		if (o instanceof Boolean)
			return ((Boolean) o).booleanValue() ? 1 : 0;
		throw err(Types.TYPE_ERROR, "must be real number, not " + typeName(o));
	}

	/** An index argument. */
	static int asIndex(Object o) {
		if (o instanceof Integer)
			return ((Integer) o).intValue();
		if (o instanceof Boolean)
			return ((Boolean) o).booleanValue() ? 1 : 0;
		throw err(Types.TYPE_ERROR, "indices must be integers or slices, not " + typeName(o));
	}

	static double floatMod(double x, double y) {
		double m = x % y;
		if (m != 0) {
			if ((y < 0) != (m < 0))
				m += y;
		} else {
			m = Math.copySign(0.0, y);
		}
		return m;
	}

	static double floatFloorDiv(double x, double y) {
		double m = x % y;
		double div = (x - m) / y;
		if (m != 0 && (y < 0) != (m < 0))
			div -= 1.0;
		double f;
		if (div != 0) {
			f = Math.floor(div);
			if (div - f > 0.5)
				f += 1.0;
		} else {
			f = Math.copySign(0.0, x / y);
		}
		return f;
	}

	/** Math.pow with the special values of C99, which Java does not follow: 1 ** y, x ** 0 and (-1) ** inf are 1. */
	static double cPow(double x, double y) {
		if (y == 0 || x == 1.0 || (x == -1.0 && Double.isInfinite(y)))
			return 1.0;
		return Math.pow(x, y);
	}

	static double floatPow(double x, double y) {
		if (y == 0 || x == 1.0 || (x == -1.0 && Double.isInfinite(y)))
			return 1.0;
		if (x != x || y != y)
			return Double.NaN;
		if (x == 0 && y < 0)
			throw err(Types.ZERO_DIVISION, "0.0 cannot be raised to a negative power");
		if (x < 0 && y == y && !Double.isInfinite(y) && y != Math.rint(y))
			throw err(Types.VALUE_ERROR, "negative number cannot be raised to a fractional power");
		double r = Math.pow(x, y);
		if (Double.isInfinite(r) && !Double.isInfinite(x) && !Double.isInfinite(y))
			throw err(Types.OVERFLOW, "(34, 'Numerical result out of range')");
		return r;
	}

	private static Object floatOp(int op, double x, double y) {
		switch (op) {
		case Op.BIN_ADD:
			return Double.valueOf(x + y);
		case Op.BIN_SUB:
			return Double.valueOf(x - y);
		case Op.BIN_MUL:
			return Double.valueOf(x * y);
		case Op.BIN_DIV:
			if (y == 0)
				throw err(Types.ZERO_DIVISION, "float division by zero");
			return Double.valueOf(x / y);
		case Op.BIN_FLOORDIV:
			if (y == 0)
				throw err(Types.ZERO_DIVISION, "float floor division by zero");
			return Double.valueOf(floatFloorDiv(x, y));
		case Op.BIN_MOD:
			if (y == 0)
				throw err(Types.ZERO_DIVISION, "float modulo by zero");
			return Double.valueOf(floatMod(x, y));
		case Op.BIN_POW:
			return Double.valueOf(floatPow(x, y));
		default:
			return null;
		}
	}

	private static final String[] OP_SYMBOLS = { "+", "-", "*", "/", "//", "%", "** or pow()", "<<", ">>", "&", "|",
			"^" };

	static Object binary(int op, Object a, Object b) {
		if (a instanceof Integer && b instanceof Integer) {
			int x = ((Integer) a).intValue(), y = ((Integer) b).intValue();
			switch (op) {
			case Op.BIN_ADD:
				return PyInt.add(x, y);
			case Op.BIN_SUB:
				return PyInt.sub(x, y);
			case Op.BIN_MUL:
				return PyInt.mul(x, y);
			default:
				return PyInt.binary(op, x, y);
			}
		}
		if (a instanceof Double && b instanceof Double) {
			Object r = floatOp(op, ((Double) a).doubleValue(), ((Double) b).doubleValue());
			if (r != null)
				return r;
		}
		return binarySlow(op, a, b);
	}

	private static Object binarySlow(int op, Object a, Object b) {
		if (isNum(a) && isNum(b)) {
			if (a instanceof Double || b instanceof Double) {
				Object r = floatOp(op, asDouble(a), asDouble(b));
				if (r != null)
					return r;
			} else {
				if (a instanceof Boolean && b instanceof Boolean
						&& (op == Op.BIN_AND || op == Op.BIN_OR || op == Op.BIN_XOR)) {
					boolean x = ((Boolean) a).booleanValue(), y = ((Boolean) b).booleanValue();
					return Boolean.valueOf(op == Op.BIN_AND ? x & y : op == Op.BIN_OR ? x | y : x ^ y);
				}
				return PyInt.binary(op, asInt(a), asInt(b));
			}
		} else if (PyStr.is(a)) {
			if (op == Op.BIN_ADD && PyStr.is(b)) {
				String x = PyStr.text(a), y = PyStr.text(b);
				if ((long) x.length() + y.length() > MAX_STRING)
					throw err(Types.MEMORY_ERROR, "string too large");
				return PyStr.like(a instanceof PyStr ? a : b, x.concat(y));
			}
			if (op == Op.BIN_MUL && isIntLike(b))
				return PyStr.like(a, repeatString(PyStr.text(a), asInt(b)));
			if (op == Op.BIN_MOD)
				return PyStr.like(a, Fmt.percent(PyStr.text(a), b));
		} else if (PyStr.is(b) && op == Op.BIN_MUL && isIntLike(a)) {
			return PyStr.like(b, repeatString(PyStr.text(b), asInt(a)));
		} else if (a instanceof PyList) {
			if (op == Op.BIN_ADD && b instanceof PyList) {
				ArrayList<Object> x = ((PyList) a).items, y = ((PyList) b).items;
				if ((long) x.size() + y.size() > MAX_LIST)
					throw err(Types.MEMORY_ERROR, "list too large");
				ArrayList<Object> r = new ArrayList<Object>(x.size() + y.size());
				r.addAll(x);
				r.addAll(y);
				return new PyList(r);
			}
			if (op == Op.BIN_MUL && isIntLike(b))
				return repeatList((PyList) a, asInt(b));
		} else if (b instanceof PyList && op == Op.BIN_MUL && isIntLike(a)) {
			return repeatList((PyList) b, asInt(a));
		} else if (a instanceof PyTuple) {
			if (op == Op.BIN_ADD && b instanceof PyTuple) {
				Object[] x = ((PyTuple) a).items, y = ((PyTuple) b).items;
				if ((long) x.length + y.length > MAX_LIST)
					throw err(Types.MEMORY_ERROR, "tuple too large");
				Object[] r = new Object[x.length + y.length];
				System.arraycopy(x, 0, r, 0, x.length);
				System.arraycopy(y, 0, r, x.length, y.length);
				return new PyTuple(r);
			}
			if (op == Op.BIN_MUL && isIntLike(b))
				return repeatTuple((PyTuple) a, asInt(b));
		} else if (b instanceof PyTuple && op == Op.BIN_MUL && isIntLike(a)) {
			return repeatTuple((PyTuple) b, asInt(a));
		} else if (a instanceof PySet && b instanceof PySet) {
			PySet r = PySet.binary(op, (PySet) a, (PySet) b);
			if (r != null)
				return r;
		}
		throw err(Types.TYPE_ERROR, "unsupported operand type(s) for " + OP_SYMBOLS[op] + ": '" + typeName(a)
				+ "' and '" + typeName(b) + "'");
	}

	static Object inplace(int op, Object a, Object b) {
		if (a instanceof PySet && b instanceof PySet && PySet.inplace(op, (PySet) a, (PySet) b))
			return a;
		if (a instanceof PyList) {
			if (op == Op.BIN_ADD) {
				final ArrayList<Object> src = toArrayList(b);
				checkList((long) ((PyList) a).items.size() + src.size());
				((PyList) a).items.addAll(src);
				return a;
			}
			if (op == Op.BIN_MUL && isIntLike(b)) {
				PyList l = (PyList) a;
				int n = asInt(b);
				if (n <= 0) {
					l.items.clear();
				} else {
					if ((long) l.items.size() * n > MAX_LIST)
						throw err(Types.MEMORY_ERROR, "list too large");
					ArrayList<Object> copy = new ArrayList<Object>(l.items);
					for (int i = 1; i < n; i++)
						l.items.addAll(copy);
				}
				return a;
			}
		}
		return binary(op, a, b);
	}

	private static String repeatString(String s, int n) {
		if (n <= 0 || s.isEmpty())
			return "";
		if ((long) s.length() * n > MAX_STRING)
			throw err(Types.MEMORY_ERROR, "string too large");
		StringBuilder sb = new StringBuilder(s.length() * n);
		for (int i = 0; i < n; i++)
			sb.append(s);
		return sb.toString();
	}

	private static PyList repeatList(PyList l, int n) {
		ArrayList<Object> r = new ArrayList<Object>();
		if (n > 0) {
			if ((long) l.items.size() * n > MAX_LIST)
				throw err(Types.MEMORY_ERROR, "list too large");
			for (int i = 0; i < n; i++)
				r.addAll(l.items);
		}
		return new PyList(r);
	}

	private static PyTuple repeatTuple(PyTuple t, int n) {
		if (n <= 0 || t.items.length == 0)
			return PyTuple.EMPTY;
		if ((long) t.items.length * n > MAX_LIST)
			throw err(Types.MEMORY_ERROR, "tuple too large");
		Object[] r = new Object[t.items.length * n];
		for (int i = 0; i < n; i++)
			System.arraycopy(t.items, 0, r, i * t.items.length, t.items.length);
		return new PyTuple(r);
	}

	static Object negate(Object a) {
		if (a instanceof Integer) {
			return PyInt.neg(((Integer) a).intValue());
		}
		if (a instanceof Double)
			return Double.valueOf(-((Double) a).doubleValue());
		if (a instanceof Boolean)
			return Integer.valueOf(-asInt(a));
		throw err(Types.TYPE_ERROR, "bad operand type for unary -: '" + typeName(a) + "'");
	}

	static Object plus(Object a) {
		if (a instanceof Integer || a instanceof Double)
			return a;
		if (a instanceof Boolean)
			return Integer.valueOf(asInt(a));
		throw err(Types.TYPE_ERROR, "bad operand type for unary +: '" + typeName(a) + "'");
	}

	static Object invert(Object a) {
		if (a instanceof Integer || a instanceof Boolean)
			return Integer.valueOf(~asInt(a));
		throw err(Types.TYPE_ERROR, "bad operand type for unary ~: '" + typeName(a) + "'");
	}

	// ------------------------------------------------------------ comparison

	private static final String[] CMP_SYMBOLS = { "==", "!=", "<", "<=", ">", ">=" };

	static boolean sameObject(Object a, Object b) {
		if (a == b)
			return true;
		if (a instanceof Integer && b instanceof Integer)
			return a.equals(b);
		return false;
	}

	static boolean eq(Object a, Object b) {
		return eq(a, b, 0);
	}

	/** The comparison of the items of a container: "is" is tried before "==", so a NaN is equal to itself there. */
	static boolean eqItem(Object a, Object b) {
		return a == b || eq(a, b, 0);
	}

	private static boolean eq(Object a, Object b, int depth) {
		if (a == b)
			return !(a instanceof Double) || ((Double) a).doubleValue() == ((Double) a).doubleValue();
		if (a instanceof Integer && b instanceof Integer)
			return ((Integer) a).intValue() == ((Integer) b).intValue();
		if (isNum(a) && isNum(b)) {
			if (a instanceof Double || b instanceof Double)
				return asDouble(a) == asDouble(b);
			return asInt(a) == asInt(b);
		}
		if (PyStr.is(a))
			return PyStr.is(b) && PyStr.text(a).equals(PyStr.text(b));
		if (a instanceof PyList) {
			if (!(b instanceof PyList))
				return false;
			return seqEq(((PyList) a).items.toArray(), ((PyList) b).items.toArray(), depth);
		}
		if (a instanceof PyTuple) {
			if (!(b instanceof PyTuple))
				return false;
			return seqEq(((PyTuple) a).items, ((PyTuple) b).items, depth);
		}
		if (a instanceof PyDict) {
			if (!(b instanceof PyDict))
				return false;
			PyDict x = (PyDict) a, y = (PyDict) b;
			if (x.size() != y.size())
				return false;
			if (depth > MAX_NESTING)
				throw err(Types.RECURSION_ERROR, "maximum recursion depth exceeded in comparison");
			for (Map.Entry<Object, PyDict.Entry> e : x.map.entrySet()) {
				PyDict.Entry o = y.map.get(e.getKey());
				if (o == null || !(e.getValue().value == o.value || eq(e.getValue().value, o.value, depth + 1)))
					return false;
			}
			return true;
		}
		if (a instanceof PySet) {
			if (b instanceof PySet)
				return PySet.keysEqual(((PySet) a).map.keySet(), ((PySet) b).map.keySet());
			// a set and dict.keys() compare as sets
			return b instanceof PyView && ((PyView) b).kind == PyView.KEYS
					&& PySet.keysEqual(((PySet) a).map.keySet(), ((PyView) b).dict.map.keySet());
		}
		if (a instanceof PyView && b instanceof PySet)
			return ((PyView) a).kind == PyView.KEYS
					&& PySet.keysEqual(((PyView) a).dict.map.keySet(), ((PySet) b).map.keySet());
		if (a instanceof PyView && b instanceof PyView) {
			// keys() and items() compare as sets, values() by identity (done above)
			PyView x = (PyView) a, y = (PyView) b;
			if (x.kind != y.kind || x.kind == PyView.VALUES || x.dict.size() != y.dict.size())
				return false;
			for (Map.Entry<Object, PyDict.Entry> e : x.dict.map.entrySet()) {
				PyDict.Entry o = y.dict.map.get(e.getKey());
				if (o == null || (x.kind == PyView.ITEMS && !eqItem(e.getValue().value, o.value)))
					return false;
			}
			return true;
		}
		if (a instanceof PyRange && b instanceof PyRange) {
			PyRange x = (PyRange) a, y = (PyRange) b;
			long n = x.longLength();
			if (n != y.longLength())
				return false;
			return n == 0 || (x.start == y.start && (n == 1 || x.step == y.step));
		}
		if (isNative(a) || isNative(b)) {
			// a == b runs a.__eq__(b), then b.__eq__(a) if it is missing or returns NotImplemented;
			// only the classes written in Java have __eq__, the others compare by identity
			Object r = callNative(a, "__eq__", b);
			if (r == PyNotImplemented.INSTANCE)
				r = callNative(b, "__eq__", a);
			if (r != PyNotImplemented.INSTANCE)
				return isTrue(r);
		}
		return false;
	}

	static boolean isNative(Object o) {
		return o instanceof PyInstance && ((PyInstance) o).type.nativeClass;
	}

	/** Runs a special method of an instance of a native class: NotImplemented if there is none. */
	static Object callNative(Object self, String method, Object... args) {
		if (isNative(self)) {
			final PyInstance instance = (PyInstance) self;
			final Object fn = instance.type.lookup(method);
			if (fn != null) {
				final Object[] all = new Object[args.length + 1];
				all[0] = instance;
				System.arraycopy(args, 0, all, 1, args.length);
				return instance.type.interp.call(fn, all);
			}
		}
		return PyNotImplemented.INSTANCE;
	}

	private static boolean seqEq(Object[] x, Object[] y, int depth) {
		if (x.length != y.length)
			return false;
		if (depth > MAX_NESTING)
			throw err(Types.RECURSION_ERROR, "maximum recursion depth exceeded in comparison");
		for (int i = 0; i < x.length; i++)
			if (x[i] != y[i] && !eq(x[i], y[i], depth + 1))
				return false;
		return true;
	}

	/** The order of two strs: that of their code points. */
	static int compareStrings(String a, String b) {
		int n = Math.min(a.length(), b.length());
		for (int i = 0; i < n; i++) {
			char x = a.charAt(i), y = b.charAt(i);
			if (x != y) {
				if (Character.isSurrogate(x) || Character.isSurrogate(y))
					return compareCodePoints(a, b);
				return x - y;
			}
		}
		return a.length() - b.length();
	}

	/** For strs with a character outside the BMP or a lone surrogate, whose chars are not code points. */
	private static int compareCodePoints(String a, String b) {
		int i = 0, j = 0;
		while (i < a.length() && j < b.length()) {
			final int x = PyStr.cpAt(a, i), y = PyStr.cpAt(b, j);
			if (x != y)
				return x < y ? -1 : 1;
			i += PyStr.cpSize(x);
			j += PyStr.cpSize(y);
		}
		return (a.length() - i) - (b.length() - j);
	}

	private static boolean test(int op, int c) {
		switch (op) {
		case Op.CMP_EQ:
			return c == 0;
		case Op.CMP_NE:
			return c != 0;
		case Op.CMP_LT:
			return c < 0;
		case Op.CMP_LE:
			return c <= 0;
		case Op.CMP_GT:
			return c > 0;
		default:
			return c >= 0;
		}
	}

	static Boolean compare(int op, Object a, Object b) {
		return Boolean.valueOf(compareBool(op, a, b, 0));
	}

	private static boolean compareBool(int op, Object a, Object b, int depth) {
		if (a instanceof Integer && b instanceof Integer) {
			int x = ((Integer) a).intValue(), y = ((Integer) b).intValue();
			return test(op, x < y ? -1 : x > y ? 1 : 0);
		}
		if (op == Op.CMP_EQ)
			return eq(a, b, depth);
		if (op == Op.CMP_NE)
			return !eq(a, b, depth);
		if (isNum(a) && isNum(b)) {
			if (a instanceof Double || b instanceof Double) {
				double x = asDouble(a), y = asDouble(b);
				switch (op) {
				case Op.CMP_LT:
					return x < y;
				case Op.CMP_LE:
					return x <= y;
				case Op.CMP_GT:
					return x > y;
				default:
					return x >= y;
				}
			}
			int x = asInt(a), y = asInt(b);
			return test(op, x < y ? -1 : x > y ? 1 : 0);
		}
		if (PyStr.is(a) && PyStr.is(b))
			return test(op, compareStrings(PyStr.text(a), PyStr.text(b)));
		if (a instanceof PyList && b instanceof PyList)
			return seqCompare(op, ((PyList) a).items.toArray(), ((PyList) b).items.toArray(), depth);
		if (a instanceof PyTuple && b instanceof PyTuple)
			return seqCompare(op, ((PyTuple) a).items, ((PyTuple) b).items, depth);
		if (a instanceof PySet && b instanceof PySet) {
			// the order of sets is inclusion: a < b is "a is a proper subset of b"
			PySet x = (PySet) a, y = (PySet) b;
			switch (op) {
			case Op.CMP_LT:
				return x.size() < y.size() && PySet.isSubset(x, y);
			case Op.CMP_LE:
				return PySet.isSubset(x, y);
			case Op.CMP_GT:
				return x.size() > y.size() && PySet.isSubset(y, x);
			default:
				return PySet.isSubset(y, x);
			}
		}
		if (a instanceof PyInstance || b instanceof PyInstance) {
			final Object r = orderUser(op, a, b);
			if (r != PyNotImplemented.INSTANCE)
				return isTrue(r);
		}
		throw err(Types.TYPE_ERROR, "'" + CMP_SYMBOLS[op] + "' not supported between instances of '" + typeName(a)
				+ "' and '" + typeName(b) + "'");
	}

	private static final String[] ORDER_METHODS = { null, null, "__lt__", "__le__", "__gt__", "__ge__" };

	/**
	 * &lt;, &lt;=, &gt; and &gt;= between objects of classes of the program, as
	 * CPython does: the method of the left operand (a.__lt__(b)), then, if it is
	 * missing or returns NotImplemented, the reflected method of the right one
	 * (b.__gt__(a)). Returns NotImplemented when neither answers, and the caller
	 * raises the TypeError. There is no __eq__, so == and != stay identity.
	 */
	private static Object orderUser(int op, Object a, Object b) {
		Object r = callOrder(a, ORDER_METHODS[op], b);
		if (r != PyNotImplemented.INSTANCE)
			return r;
		// the reflected operator: a < b is b > a, a <= b is b >= a
		final int reflected = op == Op.CMP_LT ? Op.CMP_GT : op == Op.CMP_GT ? Op.CMP_LT : op == Op.CMP_LE ? Op.CMP_GE
				: Op.CMP_LE;
		return callOrder(b, ORDER_METHODS[reflected], a);
	}

	private static Object callOrder(Object self, String method, Object other) {
		if (self instanceof PyInstance) {
			final PyInstance instance = (PyInstance) self;
			final Object fn = instance.type.lookup(method);
			if (fn != null)
				return instance.type.interp.call(fn, instance, other);
		}
		return PyNotImplemented.INSTANCE;
	}

	private static boolean seqCompare(int op, Object[] x, Object[] y, int depth) {
		if (depth > MAX_NESTING)
			throw err(Types.RECURSION_ERROR, "maximum recursion depth exceeded in comparison");
		int n = Math.min(x.length, y.length);
		for (int i = 0; i < n; i++)
			if (x[i] != y[i] && !eq(x[i], y[i], depth + 1))
				return compareBool(op, x[i], y[i], depth + 1);
		return test(op, x.length < y.length ? -1 : x.length > y.length ? 1 : 0);
	}

	/** Three-way comparison for sorting, min and max: uses only the "less than" relation. */
	static int order(Object a, Object b) {
		if (a instanceof Integer && b instanceof Integer)
			return Integer.compare(((Integer) a).intValue(), ((Integer) b).intValue());
		if (PyStr.is(a) && PyStr.is(b))
			return compareStrings(PyStr.text(a), PyStr.text(b));
		if (compareBool(Op.CMP_LT, a, b, 0))
			return -1;
		if (compareBool(Op.CMP_LT, b, a, 0))
			return 1;
		return 0;
	}

	static boolean contains(Object container, Object item) {
		if (PyStr.is(container)) {
			if (PyStr.is(item) == false)
				throw err(Types.TYPE_ERROR,
						"'in <string>' requires string as left operand, not " + typeName(item));
			return PyStr.text(container).contains(PyStr.text(item));
		}
		if (container instanceof PyList) {
			ArrayList<Object> l = ((PyList) container).items;
			for (int i = 0; i < l.size(); i++)
				if (eqItem(l.get(i), item))
					return true;
			return false;
		}
		if (container instanceof PyTuple) {
			for (Object o : ((PyTuple) container).items)
				if (eqItem(o, item))
					return true;
			return false;
		}
		if (container instanceof PyDict)
			return ((PyDict) container).containsKey(item);
		if (container instanceof PySet)
			return ((PySet) container).contains(item);
		if (container instanceof PyRange) {
			PyRange r = (PyRange) container;
			if (!isNum(item))
				return false;
			double d = asDouble(item);
			if (d != Math.rint(d))
				return false;
			long v = (long) d;
			if (r.step > 0 ? (v < r.start || v >= r.stop) : (v > r.start || v <= r.stop))
				return false;
			return (v - r.start) % r.step == 0;
		}
		if (container instanceof PyView) {
			PyView v = (PyView) container;
			if (v.kind == PyView.KEYS)
				return v.dict.containsKey(item);
			PyIter it = v.iterator();
			for (Object x = it.next(); x != null; x = it.next())
				if (eqItem(x, item))
					return true;
			return false;
		}
		throw err(Types.TYPE_ERROR, "argument of type '" + typeName(container) + "' is not iterable");
	}

	// ------------------------------------------------------------ subscripts

	static int normIndex(int i, int len, String what) {
		if (i < 0)
			i += len;
		if (i < 0 || i >= len)
			throw err(Types.INDEX_ERROR, what + " index out of range");
		return i;
	}

	/** Resolves a slice against a length: out = {start, count, step}. */
	static void sliceRange(PySlice s, int len, int[] out) {
		int step = 1;
		if (s.step != PyNone.NONE) {
			step = asIndex(s.step);
			if (step == 0)
				throw err(Types.VALUE_ERROR, "slice step cannot be zero");
		}
		long start, stop;
		if (step > 0) {
			start = s.lower == PyNone.NONE ? 0 : clampIndex(asIndex(s.lower), len, 0, len);
			stop = s.upper == PyNone.NONE ? len : clampIndex(asIndex(s.upper), len, 0, len);
		} else {
			start = s.lower == PyNone.NONE ? len - 1 : clampIndex(asIndex(s.lower), len, -1, len - 1);
			stop = s.upper == PyNone.NONE ? -1 : clampIndex(asIndex(s.upper), len, -1, len - 1);
		}
		long count;
		if (step > 0)
			count = stop > start ? (stop - start + step - 1) / step : 0;
		else
			count = start > stop ? (start - stop + (-(long) step) - 1) / (-(long) step) : 0;
		out[0] = (int) start;
		out[1] = (int) count;
		out[2] = step;
	}

	private static long clampIndex(int i, int len, int lo, int hi) {
		long v = i;
		if (v < 0)
			v += len;
		if (v < lo)
			v = lo;
		if (v > hi)
			v = hi;
		return v;
	}

	static Object getitem(Object o, Object idx) {
		if (o instanceof PyList) {
			ArrayList<Object> l = ((PyList) o).items;
			if (idx instanceof Integer) {
				int i = ((Integer) idx).intValue();
				if (i < 0)
					i += l.size();
				if (i < 0 || i >= l.size())
					throw err(Types.INDEX_ERROR, "list index out of range");
				return l.get(i);
			}
			if (idx instanceof PySlice) {
				int[] r = new int[3];
				sliceRange((PySlice) idx, l.size(), r);
				ArrayList<Object> out = new ArrayList<Object>(r[1]);
				for (int k = 0, p = r[0]; k < r[1]; k++, p += r[2])
					out.add(l.get(p));
				return new PyList(out);
			}
			return l.get(normIndex(asIndex(idx), l.size(), "list"));
		}
		if (o instanceof PyDict) {
			Object v = ((PyDict) o).get(idx);
			if (v == null)
				throw new PyError(new PyException(Types.KEY_ERROR, new Object[] { idx }));
			return v;
		}
		if (o instanceof PyStr)
			return ((PyStr) o).getitem(idx);
		if (o instanceof String)
			return PyStr.of((String) o).getitem(idx);
		if (o instanceof PyTuple) {
			Object[] t = ((PyTuple) o).items;
			if (idx instanceof PySlice) {
				int[] r = new int[3];
				sliceRange((PySlice) idx, t.length, r);
				Object[] out = new Object[r[1]];
				for (int k = 0, p = r[0]; k < r[1]; k++, p += r[2])
					out[k] = t[p];
				return new PyTuple(out);
			}
			return t[normIndex(asIndex(idx), t.length, "tuple")];
		}
		if (o instanceof PyRange) {
			PyRange rg = (PyRange) o;
			if (idx instanceof PySlice) {
				int[] r = new int[3];
				sliceRange((PySlice) idx, rg.length(), r);
				return sliceOfRange(rg, r);
			}
			// the length may be above the largest int: the index is not
			final long len = rg.longLength();
			long i = asIndex(idx);
			if (i < 0)
				i += len;
			if (i < 0 || i >= len)
				throw err(Types.INDEX_ERROR, "range object index out of range");
			return Integer.valueOf(rg.get(i));
		}
		throw err(Types.TYPE_ERROR, "'" + typeName(o) + "' object is not subscriptable");
	}

	private static Object sliceOfRange(PyRange rg, int[] r) {
		long newStep = (long) rg.step * r[2];
		long newStart = (long) rg.start + (long) r[0] * rg.step;
		long newStop = newStart + newStep * r[1];
		return new PyRange(PyInt.check(newStart), PyInt.check(newStop), PyInt.check(newStep));
	}

	static void setitem(Object o, Object idx, Object v) {
		if (o instanceof PyList) {
			ArrayList<Object> l = ((PyList) o).items;
			if (idx instanceof PySlice) {
				int[] r = new int[3];
				sliceRange((PySlice) idx, l.size(), r);
				ArrayList<Object> src = toArrayList(v);
				if (r[2] == 1) {
					checkList((long) l.size() - r[1] + src.size());
					List<Object> sub = l.subList(r[0], r[0] + r[1]);
					sub.clear();
					l.addAll(r[0], src);
				} else {
					if (src.size() != r[1])
						throw err(Types.VALUE_ERROR, "attempt to assign sequence of size " + src.size()
								+ " to extended slice of size " + r[1]);
					for (int k = 0, p = r[0]; k < r[1]; k++, p += r[2])
						l.set(p, src.get(k));
				}
				return;
			}
			l.set(normIndex(asIndex(idx), l.size(), "list assignment"), v);
			return;
		}
		if (o instanceof PyDict) {
			((PyDict) o).put(idx, v);
			return;
		}
		throw err(Types.TYPE_ERROR, "'" + typeName(o) + "' object does not support item assignment");
	}

	static void delitem(Object o, Object idx) {
		if (o instanceof PyList) {
			ArrayList<Object> l = ((PyList) o).items;
			if (idx instanceof PySlice) {
				int[] r = new int[3];
				sliceRange((PySlice) idx, l.size(), r);
				if (r[2] == 1) {
					l.subList(r[0], r[0] + r[1]).clear();
				} else {
					boolean[] kill = new boolean[l.size()];
					for (int k = 0, p = r[0]; k < r[1]; k++, p += r[2])
						kill[p] = true;
					ArrayList<Object> keep = new ArrayList<Object>();
					for (int i = 0; i < kill.length; i++)
						if (!kill[i])
							keep.add(l.get(i));
					l.clear();
					l.addAll(keep);
				}
				return;
			}
			l.remove(normIndex(asIndex(idx), l.size(), "list assignment"));
			return;
		}
		if (o instanceof PyDict) {
			if (((PyDict) o).remove(idx) == null)
				throw new PyError(new PyException(Types.KEY_ERROR, new Object[] { idx }));
			return;
		}
		throw err(Types.TYPE_ERROR, "'" + typeName(o) + "' object doesn't support item deletion");
	}

	static int len(Object o) {
		if (o instanceof PyStr)
			return ((PyStr) o).length();
		if (o instanceof String)
			return PyStr.of((String) o).length();
		if (o instanceof PyList)
			return ((PyList) o).items.size();
		if (o instanceof PyTuple)
			return ((PyTuple) o).items.length;
		if (o instanceof PyDict)
			return ((PyDict) o).size();
		if (o instanceof PySet)
			return ((PySet) o).size();
		if (o instanceof PyRange)
			return ((PyRange) o).length();
		if (o instanceof PyView)
			return ((PyView) o).dict.size();
		throw err(Types.TYPE_ERROR, "object of type '" + typeName(o) + "' has no len()");
	}

	// ------------------------------------------------------------ iteration

	/** True for what {@link #getIter} accepts. */
	static boolean isIterable(Object o) {
		return o instanceof PyList || o instanceof PyTuple || PyStr.is(o) || o instanceof PyRange
				|| o instanceof PyDict || o instanceof PySet || o instanceof PyView || o instanceof PyIter;
	}

	static PyIter getIter(Object o) {
		if (o instanceof PyList)
			return new PyIter.ListIter((PyList) o);
		if (o instanceof PyTuple)
			return new PyIter.ArrayIter(((PyTuple) o).items);
		if (o instanceof PyStr)
			return new PyIter.StrIter(((PyStr) o).s, true);
		if (o instanceof String)
			return new PyIter.StrIter((String) o);
		if (o instanceof PyRange)
			return new PyIter.RangeIter((PyRange) o);
		if (o instanceof PyDict)
			return new PyIter.DictIter((PyDict) o);
		if (o instanceof PySet)
			return new PySet.SetIter((PySet) o);
		if (o instanceof PyView)
			return ((PyView) o).iterator();
		if (o instanceof PyIter)
			return (PyIter) o;
		throw err(Types.TYPE_ERROR, "'" + typeName(o) + "' object is not iterable");
	}

	static ArrayList<Object> toArrayList(Object o) {
		if (o instanceof PyList)
			return new ArrayList<Object>(((PyList) o).items);
		ArrayList<Object> r = new ArrayList<Object>();
		if (o instanceof PyTuple) {
			Collections.addAll(r, ((PyTuple) o).items);
			return r;
		}
		if (o instanceof PyRange && ((PyRange) o).longLength() > MAX_LIST)
			throw err(Types.MEMORY_ERROR, "range too large");
		PyIter it = getIter(o);
		for (Object x = it.next(); x != null; x = it.next()) {
			r.add(x);
			if (r.size() > MAX_LIST)
				throw err(Types.MEMORY_ERROR, "sequence too large");
		}
		return r;
	}

	static void sortList(ArrayList<Object> items, final Interp in, final Object key, boolean reverse) {
		int n = items.size();
		in.work((long) n * (32 - Integer.numberOfLeadingZeros(n)));
		Object[] values = items.toArray();
		Object[] keys = new Object[n];
		for (int i = 0; i < n; i++)
			keys[i] = key == PyNone.NONE ? values[i] : in.call(key, values[i]);
		// reverse=True sorts the reversed list and reverses the result, so that equal elements keep their order
		int[] idx = new int[n];
		for (int i = 0; i < n; i++)
			idx[i] = reverse ? n - 1 - i : i;
		sortIndices(idx, keys);
		items.clear();
		for (int i = 0; i < n; i++)
			items.add(values[idx[reverse ? n - 1 - i : i]]);
	}

	/** Below this length a list is sorted by binary insertion, as CPython and the Java library do. */
	private static final int MIN_MERGE = 32;

	/**
	 * Stable sort of the indices idx[0..n) by the order of their keys. Written
	 * here rather than left to the Java library, whose sort may refuse a
	 * comparison that is not a total order (a list with NaNs, for instance) while
	 * CPython sorts it: this one always ends, whatever order() answers. order()
	 * may raise a Python exception, which ends the sort.
	 *
	 * <p>A short list is sorted as CPython does (a first run, then binary
	 * insertion), so the comparisons are made in the same order: the error for two
	 * incomparable values names their types as CPython does, and a __lt__ of the
	 * program is called in the same sequence. A longer one is merge sorted.
	 */
	private static void sortIndices(int[] idx, Object[] keys) {
		int n = idx.length;
		if (n < 2)
			return;
		if (n < MIN_MERGE) {
			int run = countRunAndMakeAscending(idx, keys, 0, n);
			binaryInsertionSort(idx, keys, 0, n, run);
			return;
		}
		mergeSort(idx, new int[n], keys, 0, n);
	}

	/** Length of the run at lo (ascending, or strictly descending and then reversed in place). */
	private static int countRunAndMakeAscending(int[] idx, Object[] keys, int lo, int hi) {
		int runHi = lo + 1;
		if (runHi == hi)
			return 1;
		if (order(keys[idx[runHi++]], keys[idx[lo]]) < 0) {
			while (runHi < hi && order(keys[idx[runHi]], keys[idx[runHi - 1]]) < 0)
				runHi++;
			for (int i = lo, j = runHi - 1; i < j; i++, j--) {
				int t = idx[i];
				idx[i] = idx[j];
				idx[j] = t;
			}
		} else {
			while (runHi < hi && order(keys[idx[runHi]], keys[idx[runHi - 1]]) >= 0)
				runHi++;
		}
		return runHi - lo;
	}

	/** Sorts idx[lo..hi) knowing that idx[lo..start) is sorted: each element is inserted at the place a binary search finds. */
	private static void binaryInsertionSort(int[] idx, Object[] keys, int lo, int hi, int start) {
		if (start == lo)
			start++;
		for (; start < hi; start++) {
			int pivot = idx[start];
			int left = lo;
			int right = start;
			while (left < right) {
				int mid = (left + right) >>> 1;
				if (order(keys[pivot], keys[idx[mid]]) < 0)
					right = mid;
				else
					left = mid + 1;
			}
			System.arraycopy(idx, left, idx, left + 1, start - left);
			idx[left] = pivot;
		}
	}

	/** Stable merge sort of idx[lo..hi). */
	private static void mergeSort(int[] idx, int[] tmp, Object[] keys, int lo, int hi) {
		if (hi - lo < 2)
			return;
		int mid = (lo + hi) >>> 1;
		mergeSort(idx, tmp, keys, lo, mid);
		mergeSort(idx, tmp, keys, mid, hi);
		if (order(keys[idx[mid]], keys[idx[mid - 1]]) >= 0)
			return; // the two halves are already in order
		int i = lo, j = mid, k = lo;
		while (i < mid && j < hi) {
			// an element of the right half goes first only if it is strictly smaller: that keeps the sort stable
			if (order(keys[idx[j]], keys[idx[i]]) < 0)
				tmp[k++] = idx[j++];
			else
				tmp[k++] = idx[i++];
		}
		while (i < mid)
			tmp[k++] = idx[i++];
		while (j < hi)
			tmp[k++] = idx[j++];
		System.arraycopy(tmp, lo, idx, lo, hi - lo);
	}

	// ------------------------------------------------------------ repr and str

	static String repr(Object o) {
		StringBuilder sb = new StringBuilder();
		repr(o, sb, 0);
		return sb.toString();
	}

	private static void repr(Object o, StringBuilder sb, int depth) {
		if (depth > MAX_NESTING)
			throw err(Types.RECURSION_ERROR, "maximum recursion depth exceeded while getting the repr of an object");
		if (PyStr.is(o)) {
			// the repr of a list of large strs is not built above the largest str
			checkString((long) sb.length() + PyStr.text(o).length());
			sb.append(Fmt.reprStr(PyStr.text(o)));
		} else if (o instanceof Integer) {
			sb.append(((Integer) o).intValue());
		} else if (o instanceof Double) {
			sb.append(Fmt.repr(((Double) o).doubleValue()));
		} else if (o instanceof Boolean) {
			sb.append(((Boolean) o).booleanValue() ? "True" : "False");
		} else if (o == PyNone.NONE) {
			sb.append("None");
		} else if (o instanceof PyList) {
			sb.append('[');
			ArrayList<Object> l = ((PyList) o).items;
			for (int i = 0; i < l.size(); i++) {
				if (i > 0)
					sb.append(", ");
				repr(l.get(i), sb, depth + 1);
			}
			sb.append(']');
		} else if (o instanceof PyTuple) {
			Object[] t = ((PyTuple) o).items;
			sb.append('(');
			for (int i = 0; i < t.length; i++) {
				if (i > 0)
					sb.append(", ");
				repr(t[i], sb, depth + 1);
			}
			if (t.length == 1)
				sb.append(',');
			sb.append(')');
		} else if (o instanceof PySet) {
			PySet s = (PySet) o;
			if (s.size() == 0) {
				sb.append("set()");
			} else {
				sb.append('{');
				boolean first = true;
				for (Object item : s.map.values()) {
					if (!first)
						sb.append(", ");
					first = false;
					repr(item, sb, depth + 1);
				}
				sb.append('}');
			}
		} else if (o instanceof PyDict) {
			sb.append('{');
			boolean first = true;
			for (PyDict.Entry e : ((PyDict) o).map.values()) {
				if (!first)
					sb.append(", ");
				first = false;
				repr(e.key, sb, depth + 1);
				sb.append(": ");
				repr(e.value, sb, depth + 1);
			}
			sb.append('}');
		} else if (o instanceof PyView) {
			PyView v = (PyView) o;
			sb.append(v.type().name).append('(');
			PyList l = new PyList(toArrayList(v));
			repr(l, sb, depth + 1);
			sb.append(')');
		} else if (o instanceof PyRange) {
			PyRange r = (PyRange) o;
			sb.append("range(").append(r.start).append(", ").append(r.stop);
			if (r.step != 1)
				sb.append(", ").append(r.step);
			sb.append(')');
		} else if (o instanceof PyException) {
			PyException e = (PyException) o;
			final String text = e.type.isClass() ? userText(e.type, e, "__repr__") : null;
			if (text != null) {
				sb.append(text);
			} else {
				sb.append(e.type.name).append('(');
				for (int i = 0; i < e.args.length; i++) {
					if (i > 0)
						sb.append(", ");
					repr(e.args[i], sb, depth + 1);
				}
				sb.append(')');
			}
		} else if (o instanceof PyType) {
			PyType t = (PyType) o;
			sb.append("<class '").append(t.fullName()).append("'>");
		} else if (o instanceof PyInstance) {
			PyType t = ((PyInstance) o).type;
			final String text = userText((PyInstance) o, "__repr__");
			if (text != null)
				sb.append(text);
			else
				// CPython adds the address, which is not deterministic
				sb.append('<').append(t.fullName()).append(" object>");
		} else if (o instanceof PyFunction) {
			sb.append("<function ").append(((PyFunction) o).code.name).append('>');
		} else if (o instanceof Builtin) {
			sb.append("<built-in function ").append(((Builtin) o).name).append('>');
		} else if (o instanceof BoundMethod) {
			BoundMethod m = (BoundMethod) o;
			if (m.function != null) {
				sb.append("<bound method ").append(typeName(m.self)).append('.').append(m.name()).append(" of ");
				repr(m.self, sb, depth + 1);
				sb.append('>');
			} else {
				sb.append("<built-in method ").append(m.method.name).append(" of ").append(typeName(m.self))
						.append(" object>");
			}
		} else if (o instanceof PyModule) {
			sb.append("<module '").append(((PyModule) o).name).append("'>");
		} else if (o instanceof PyNotImplemented) {
			sb.append("NotImplemented");
		} else if (o instanceof PySlice) {
			PySlice s = (PySlice) o;
			sb.append("slice(");
			repr(s.lower, sb, depth + 1);
			sb.append(", ");
			repr(s.upper, sb, depth + 1);
			sb.append(", ");
			repr(s.step, sb, depth + 1);
			sb.append(')');
		} else {
			sb.append("<").append(typeName(o)).append(" object>");
		}
	}

	static String str(Object o) {
		if (PyStr.is(o))
			return PyStr.text(o);
		if (o instanceof PyException) {
			final PyException e = (PyException) o;
			final String text = e.type.isClass() ? userText(e.type, e, "__str__") : null;
			return text != null ? text : excText(e);
		}
		if (o instanceof PyInstance) {
			// as object.__str__, which is repr(self) when the class has no __str__
			final String text = userText((PyInstance) o, "__str__");
			if (text != null)
				return text;
		}
		return repr(o);
	}

	/**
	 * The text a special method of the class of the program gives (__str__ or
	 * __repr__), or null if the class has none. The method is run by the
	 * interpreter of the class; what it returns must be a str.
	 */
	private static String userText(PyInstance o, String method) {
		return userText(o.type, o, method);
	}

	private static String userText(PyType type, Object o, String method) {
		final Object fn = type.lookup(method);
		if (fn == null)
			return null;
		final Object result = type.interp.call(fn, o);
		if (PyStr.is(result) == false)
			throw err(Types.TYPE_ERROR, method + " returned non-string (type " + typeName(result) + ")");
		return PyStr.text(result);
	}

	static String excText(PyException e) {
		if (e.args.length == 0)
			return "";
		if (e.args.length == 1)
			return e.type.isSubtypeOf(Types.KEY_ERROR) ? repr(e.args[0]) : str(e.args[0]);
		return repr(new PyTuple(e.args));
	}

	// ------------------------------------------------------------ attributes

	static Object getattr(Object o, String name) {
		if (o instanceof PyModule) {
			Object v = ((PyModule) o).members.get(name);
			if (v == null)
				throw err(Types.ATTRIBUTE_ERROR,
						"module '" + ((PyModule) o).name + "' has no attribute '" + name + "'");
			return v;
		}
		if (o instanceof PyInstance) {
			Object v = ((PyInstance) o).attrs.get(name);
			if (v != null)
				return v;
			return classAttr(o, ((PyInstance) o).type, name);
		}
		if (o instanceof PyException) {
			PyException e = (PyException) o;
			if (e.attrs != null) {
				Object v = e.attrs.get(name);
				if (v != null)
					return v;
			}
			if (name.equals("args"))
				return new PyTuple(e.args);
			if (e.type.isClass())
				return classAttr(o, e.type, name);
		}
		if (o instanceof PyType && ((PyType) o).isClass()) {
			PyType t = (PyType) o;
			if (name.equals("__qualname__"))
				return PyStr.of(t.qualname);
			if (name.equals("__name__") == false) {
				Object v = t.lookup(name);
				if (v == null)
					throw err(Types.ATTRIBUTE_ERROR, "type object '" + t.name + "' has no attribute '" + name + "'");
				return v;
			}
		}
		if (name.equals("__name__")) {
			if (o instanceof PyType)
				return PyStr.of(((PyType) o).name);
			if (o instanceof PyFunction)
				return PyStr.of(((PyFunction) o).code.name);
			if (o instanceof Builtin)
				return PyStr.of(((Builtin) o).name);
			if (o instanceof BoundMethod)
				return PyStr.of(((BoundMethod) o).name());
		}
		if (o instanceof PyType && name.equals("__init__")) {
			// Exception.__init__(self, ...) or object.__init__(self), called by a class
			Object v = ((PyType) o).lookup(name);
			if (v != null)
				return v;
		}
		Builtin m = Methods.lookup(o, name);
		if (m != null)
			return new BoundMethod(o, m);
		throw err(Types.ATTRIBUTE_ERROR, "'" + typeName(o) + "' object has no attribute '" + name + "'");
	}

	/** An attribute found on the class of an instance: a function becomes a method bound to the instance. */
	private static Object classAttr(Object self, PyType type, String name) {
		Object v = type.lookup(name);
		if (v == null)
			throw err(Types.ATTRIBUTE_ERROR, "'" + type.name + "' object has no attribute '" + name + "'");
		if (v instanceof PyFunction)
			return new BoundMethod(self, (PyFunction) v);
		if (v == Builtins.EXCEPTION_INIT || v == Builtins.OBJECT_INIT)
			return new BoundMethod(self, (Builtin) v);
		if (v instanceof Builtin && ((Builtin) v).isMethod)
			return new BoundMethod(self, (Builtin) v); // a method of a native class
		return v; // as in CPython, a builtin function stored in a class is not bound
	}

	/** The instances and the classes written in Java cannot be changed, as in CPython. */
	private static void refuseNative(Object o, String name) {
		if (o instanceof PyInstance && ((PyInstance) o).type.nativeClass)
			throw err(Types.ATTRIBUTE_ERROR,
					"attribute '" + name + "' of '" + ((PyInstance) o).type.fullName() + "' objects is not writable");
		if (o instanceof PyType && ((PyType) o).nativeClass)
			throw err(Types.TYPE_ERROR,
					"cannot set '" + name + "' attribute of immutable type '" + ((PyType) o).fullName() + "'");
	}

	/** Attributes can be set on instances and classes of the program, and on exceptions. */
	static void setattr(Object o, String name, Object v) {
		refuseNative(o, name);
		if (o instanceof PyInstance && ((PyInstance) o).type.isClass()) {
			((PyInstance) o).attrs.put(name, v);
		} else if (o instanceof PyException) {
			((PyException) o).attrs().put(name, v);
		} else if (o instanceof PyType && ((PyType) o).isClass()) {
			((PyType) o).dict.put(name, v);
		} else {
			throw err(Types.ATTRIBUTE_ERROR, "'" + typeName(o) + "' object has no attribute '" + name
					+ "' and no __dict__ for setting new attributes");
		}
	}

	static void delattr(Object o, String name) {
		refuseNative(o, name);
		Map<String, Object> attrs = null;
		if (o instanceof PyInstance && ((PyInstance) o).type.isClass())
			attrs = ((PyInstance) o).attrs;
		else if (o instanceof PyException)
			attrs = ((PyException) o).attrs;
		else if (o instanceof PyType && ((PyType) o).isClass())
			attrs = ((PyType) o).dict;
		if (attrs == null || attrs.remove(name) == null)
			throw err(Types.ATTRIBUTE_ERROR, "'" + typeName(o) + "' object has no attribute '" + name + "'");
	}
}
