package org.luaj.vm2.python;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

	static PyError overflow() {
		return err(Types.OVERFLOW, "integer overflow (this interpreter has 32-bit integers)");
	}

	static PyType typeOf(Object o) {
		if (o instanceof Integer)
			return Types.INT;
		if (o instanceof String)
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
		if (o instanceof PyRange)
			return Types.RANGE;
		if (o instanceof PyFunction)
			return Types.FUNCTION;
		if (o instanceof Builtin)
			return Types.BUILTIN;
		if (o instanceof BoundMethod)
			return Types.METHOD;
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
		if (v instanceof String)
			return !((String) v).isEmpty();
		if (v instanceof Double)
			return ((Double) v).doubleValue() != 0.0;
		if (v instanceof PyList)
			return !((PyList) v).items.isEmpty();
		if (v instanceof PyTuple)
			return ((PyTuple) v).items.length != 0;
		if (v instanceof PyDict)
			return ((PyDict) v).size() != 0;
		if (v instanceof PyRange)
			return ((PyRange) v).length() != 0;
		if (v instanceof PyView)
			return ((PyView) v).dict.size() != 0;
		return true;
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

	static int floatToInt(double d) {
		if (d != d)
			throw err(Types.VALUE_ERROR, "cannot convert float NaN to integer");
		if (Double.isInfinite(d))
			throw err(Types.OVERFLOW, "cannot convert float infinity to integer");
		if (d >= 2147483648.0 || d <= -2147483649.0)
			throw overflow();
		return (int) d;
	}

	private static Object box(long r) {
		if (r != (int) r)
			throw overflow();
		return Integer.valueOf((int) r);
	}

	private static Object intPow(int x, int y) {
		if (y < 0) {
			if (x == 0)
				throw err(Types.ZERO_DIVISION, "0.0 cannot be raised to a negative power");
			return Double.valueOf(Math.pow(x, y));
		}
		if (x == 0)
			return Integer.valueOf(y == 0 ? 1 : 0);
		if (x == 1)
			return Integer.valueOf(1);
		if (x == -1)
			return Integer.valueOf((y & 1) == 0 ? 1 : -1);
		if (y > 31)
			throw overflow();
		long r = 1;
		for (int i = 0; i < y; i++) {
			r *= x;
			if (r != (int) r)
				throw overflow();
		}
		return Integer.valueOf((int) r);
	}

	private static Object intOp(int op, int x, int y) {
		switch (op) {
		case Op.BIN_ADD:
			return box((long) x + y);
		case Op.BIN_SUB:
			return box((long) x - y);
		case Op.BIN_MUL:
			return box((long) x * y);
		case Op.BIN_DIV:
			if (y == 0)
				throw err(Types.ZERO_DIVISION, "division by zero");
			return Double.valueOf((double) x / y);
		case Op.BIN_FLOORDIV:
			if (y == 0)
				throw err(Types.ZERO_DIVISION, "integer division or modulo by zero");
			return box(Math.floorDiv((long) x, (long) y));
		case Op.BIN_MOD:
			if (y == 0)
				throw err(Types.ZERO_DIVISION, "integer modulo by zero");
			return Integer.valueOf((int) Math.floorMod((long) x, (long) y));
		case Op.BIN_POW:
			return intPow(x, y);
		case Op.BIN_LSHIFT:
			if (y < 0)
				throw err(Types.VALUE_ERROR, "negative shift count");
			if (x == 0)
				return Integer.valueOf(0);
			if (y >= 32)
				throw overflow();
			return box((long) x << y);
		case Op.BIN_RSHIFT:
			if (y < 0)
				throw err(Types.VALUE_ERROR, "negative shift count");
			if (y >= 32)
				return Integer.valueOf(x < 0 ? -1 : 0);
			return Integer.valueOf(x >> y);
		case Op.BIN_AND:
			return Integer.valueOf(x & y);
		case Op.BIN_OR:
			return Integer.valueOf(x | y);
		default:
			return Integer.valueOf(x ^ y);
		}
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

	static double floatPow(double x, double y) {
		if (y == 0)
			return 1.0;
		if (x == 1.0)
			return 1.0;
		if (x != x || y != y)
			return Double.NaN;
		if (x == 0 && y < 0)
			throw err(Types.ZERO_DIVISION, "0.0 cannot be raised to a negative power");
		if (x < 0 && !Double.isInfinite(y) && y != Math.rint(y))
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
			case Op.BIN_ADD: {
				long r = (long) x + y;
				if (r != (int) r)
					throw overflow();
				return Integer.valueOf((int) r);
			}
			case Op.BIN_SUB: {
				long r = (long) x - y;
				if (r != (int) r)
					throw overflow();
				return Integer.valueOf((int) r);
			}
			case Op.BIN_MUL: {
				long r = (long) x * y;
				if (r != (int) r)
					throw overflow();
				return Integer.valueOf((int) r);
			}
			default:
				return intOp(op, x, y);
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
				return intOp(op, asInt(a), asInt(b));
			}
		} else if (a instanceof String) {
			if (op == Op.BIN_ADD && b instanceof String) {
				String x = (String) a, y = (String) b;
				if ((long) x.length() + y.length() > MAX_STRING)
					throw err(Types.MEMORY_ERROR, "string too large");
				return x.concat(y);
			}
			if (op == Op.BIN_MUL && isIntLike(b))
				return repeatString((String) a, asInt(b));
			if (op == Op.BIN_MOD)
				return Fmt.percent((String) a, b);
		} else if (b instanceof String && op == Op.BIN_MUL && isIntLike(a)) {
			return repeatString((String) b, asInt(a));
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
		}
		throw err(Types.TYPE_ERROR, "unsupported operand type(s) for " + OP_SYMBOLS[op] + ": '" + typeName(a)
				+ "' and '" + typeName(b) + "'");
	}

	static Object inplace(int op, Object a, Object b) {
		if (a instanceof PyList) {
			if (op == Op.BIN_ADD) {
				((PyList) a).items.addAll(toArrayList(b));
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
			int x = ((Integer) a).intValue();
			if (x == Integer.MIN_VALUE)
				throw overflow();
			return Integer.valueOf(-x);
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
		if (a instanceof String)
			return b instanceof String && a.equals(b);
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
				if (o == null || !eq(e.getValue().value, o.value, depth + 1))
					return false;
			}
			return true;
		}
		if (a instanceof PyRange && b instanceof PyRange) {
			PyRange x = (PyRange) a, y = (PyRange) b;
			int n = x.length();
			if (n != y.length())
				return false;
			return n == 0 || (x.start == y.start && (n == 1 || x.step == y.step));
		}
		return false;
	}

	private static boolean seqEq(Object[] x, Object[] y, int depth) {
		if (x.length != y.length)
			return false;
		if (depth > MAX_NESTING)
			throw err(Types.RECURSION_ERROR, "maximum recursion depth exceeded in comparison");
		for (int i = 0; i < x.length; i++)
			if (!eq(x[i], y[i], depth + 1))
				return false;
		return true;
	}

	static int compareStrings(String a, String b) {
		int n = Math.min(a.length(), b.length());
		for (int i = 0; i < n; i++) {
			char x = a.charAt(i), y = b.charAt(i);
			if (x != y) {
				if (x >= 0xd800 && y >= 0xd800) {
					x = fixSurrogate(x);
					y = fixSurrogate(y);
				}
				return x - y;
			}
		}
		return a.length() - b.length();
	}

	private static char fixSurrogate(char c) {
		if (c >= 0xe000)
			return (char) (c - 0x800);
		return (char) (c + 0x2000);
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
		if (a instanceof String && b instanceof String)
			return test(op, compareStrings((String) a, (String) b));
		if (a instanceof PyList && b instanceof PyList)
			return seqCompare(op, ((PyList) a).items.toArray(), ((PyList) b).items.toArray(), depth);
		if (a instanceof PyTuple && b instanceof PyTuple)
			return seqCompare(op, ((PyTuple) a).items, ((PyTuple) b).items, depth);
		throw err(Types.TYPE_ERROR, "'" + CMP_SYMBOLS[op] + "' not supported between instances of '" + typeName(a)
				+ "' and '" + typeName(b) + "'");
	}

	private static boolean seqCompare(int op, Object[] x, Object[] y, int depth) {
		if (depth > MAX_NESTING)
			throw err(Types.RECURSION_ERROR, "maximum recursion depth exceeded in comparison");
		int n = Math.min(x.length, y.length);
		for (int i = 0; i < n; i++)
			if (!eq(x[i], y[i], depth + 1))
				return compareBool(op, x[i], y[i], depth + 1);
		return test(op, x.length < y.length ? -1 : x.length > y.length ? 1 : 0);
	}

	/** Three-way comparison for sorting, min and max: uses only the "less than" relation. */
	static int order(Object a, Object b) {
		if (a instanceof Integer && b instanceof Integer)
			return Integer.compare(((Integer) a).intValue(), ((Integer) b).intValue());
		if (a instanceof String && b instanceof String)
			return compareStrings((String) a, (String) b);
		if (compareBool(Op.CMP_LT, a, b, 0))
			return -1;
		if (compareBool(Op.CMP_LT, b, a, 0))
			return 1;
		return 0;
	}

	static boolean contains(Object container, Object item) {
		if (container instanceof String) {
			if (!(item instanceof String))
				throw err(Types.TYPE_ERROR,
						"'in <string>' requires string as left operand, not " + typeName(item));
			return ((String) container).contains((String) item);
		}
		if (container instanceof PyList) {
			ArrayList<Object> l = ((PyList) container).items;
			for (int i = 0; i < l.size(); i++)
				if (eq(l.get(i), item))
					return true;
			return false;
		}
		if (container instanceof PyTuple) {
			for (Object o : ((PyTuple) container).items)
				if (eq(o, item))
					return true;
			return false;
		}
		if (container instanceof PyDict)
			return ((PyDict) container).containsKey(item);
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
				if (eq(x, item))
					return true;
			return false;
		}
		throw err(Types.TYPE_ERROR, "argument of type '" + typeName(container) + "' is not iterable");
	}

	// ------------------------------------------------------------ strings and code points

	private static String lastString;
	private static boolean lastBmp;

	/** True if the string has no surrogate pair, so that chars are code points. */
	static boolean bmp(String s) {
		int n = s.length();
		if (n > 32 && s == lastString)
			return lastBmp;
		boolean r = true;
		for (int i = 0; i < n; i++) {
			char c = s.charAt(i);
			if (c >= 0xd800 && c <= 0xdfff) {
				r = false;
				break;
			}
		}
		if (n > 32) {
			lastString = s;
			lastBmp = r;
		}
		return r;
	}

	static int strLen(String s) {
		return bmp(s) ? s.length() : s.codePointCount(0, s.length());
	}

	private static final String[] ASCII = new String[128];
	static {
		for (int i = 0; i < 128; i++)
			ASCII[i] = String.valueOf((char) i);
	}

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

	static String strAt(String s, int i) {
		if (bmp(s))
			return charString(s.charAt(i));
		int off = s.offsetByCodePoints(0, i);
		return s.substring(off, off + Character.charCount(s.codePointAt(off)));
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
		if (o instanceof String) {
			String s = (String) o;
			if (idx instanceof PySlice) {
				int len = strLen(s);
				int[] r = new int[3];
				sliceRange((PySlice) idx, len, r);
				if (r[2] == 1) {
					if (r[1] == 0)
						return "";
					int from = offset(s, r[0]);
					int to = bmp(s) ? from + r[1] : s.offsetByCodePoints(from, r[1]);
					return s.substring(from, to);
				}
				StringBuilder sb = new StringBuilder();
				for (int k = 0, p = r[0]; k < r[1]; k++, p += r[2])
					sb.append(strAt(s, p));
				return sb.toString();
			}
			int len = strLen(s);
			return strAt(s, normIndex(asIndex(idx), len, "string"));
		}
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
			return Integer.valueOf(rg.get(normIndex(asIndex(idx), rg.length(), "range object")));
		}
		throw err(Types.TYPE_ERROR, "'" + typeName(o) + "' object is not subscriptable");
	}

	private static Object sliceOfRange(PyRange rg, int[] r) {
		long newStep = (long) rg.step * r[2];
		long newStart = (long) rg.start + (long) r[0] * rg.step;
		long newStop = newStart + newStep * r[1];
		if (newStart != (int) newStart || newStop != (int) newStop || newStep != (int) newStep)
			throw overflow();
		return new PyRange((int) newStart, (int) newStop, (int) newStep);
	}

	static void setitem(Object o, Object idx, Object v) {
		if (o instanceof PyList) {
			ArrayList<Object> l = ((PyList) o).items;
			if (idx instanceof PySlice) {
				int[] r = new int[3];
				sliceRange((PySlice) idx, l.size(), r);
				ArrayList<Object> src = toArrayList(v);
				if (r[2] == 1) {
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
		if (o instanceof String)
			return strLen((String) o);
		if (o instanceof PyList)
			return ((PyList) o).items.size();
		if (o instanceof PyTuple)
			return ((PyTuple) o).items.length;
		if (o instanceof PyDict)
			return ((PyDict) o).size();
		if (o instanceof PyRange)
			return ((PyRange) o).length();
		if (o instanceof PyView)
			return ((PyView) o).dict.size();
		throw err(Types.TYPE_ERROR, "object of type '" + typeName(o) + "' has no len()");
	}

	// ------------------------------------------------------------ iteration

	static PyIter getIter(Object o) {
		if (o instanceof PyList)
			return new PyIter.ListIter((PyList) o);
		if (o instanceof PyTuple)
			return new PyIter.ArrayIter(((PyTuple) o).items);
		if (o instanceof String)
			return new PyIter.StrIter((String) o);
		if (o instanceof PyRange)
			return new PyIter.RangeIter((PyRange) o);
		if (o instanceof PyDict)
			return new PyIter.DictIter((PyDict) o);
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
		if (o instanceof PyRange && ((PyRange) o).length() > MAX_LIST)
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
		Object[] values = items.toArray();
		final Object[] keys = new Object[n];
		for (int i = 0; i < n; i++)
			keys[i] = key == PyNone.NONE ? values[i] : in.call(key, values[i]);
		Integer[] idx = new Integer[n];
		for (int i = 0; i < n; i++)
			idx[i] = Integer.valueOf(i);
		Comparator<Integer> c = new Comparator<Integer>() {
			@Override
			public int compare(Integer a, Integer b) {
				return order(keys[a.intValue()], keys[b.intValue()]);
			}
		};
		try {
			List<Integer> order = java.util.Arrays.asList(idx);
			if (reverse)
				Collections.reverse(order);
			Collections.sort(order, c);
			if (reverse)
				Collections.reverse(order);
		} catch (IllegalArgumentException e) {
			throw err(Types.VALUE_ERROR, "inconsistent ordering in sort");
		}
		items.clear();
		for (int i = 0; i < n; i++)
			items.add(values[idx[i].intValue()]);
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
		if (o instanceof String) {
			sb.append(Fmt.reprStr((String) o));
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
			sb.append(e.type.name).append('(');
			for (int i = 0; i < e.args.length; i++) {
				if (i > 0)
					sb.append(", ");
				repr(e.args[i], sb, depth + 1);
			}
			sb.append(')');
		} else if (o instanceof PyType) {
			sb.append("<class '").append(((PyType) o).name).append("'>");
		} else if (o instanceof PyFunction) {
			sb.append("<function ").append(((PyFunction) o).code.name).append('>');
		} else if (o instanceof Builtin) {
			sb.append("<built-in function ").append(((Builtin) o).name).append('>');
		} else if (o instanceof BoundMethod) {
			sb.append("<built-in method ").append(((BoundMethod) o).method.name).append(" of ")
					.append(typeName(((BoundMethod) o).self)).append(" object>");
		} else if (o instanceof PyModule) {
			sb.append("<module '").append(((PyModule) o).name).append("'>");
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
		if (o instanceof String)
			return (String) o;
		if (o instanceof PyException)
			return excText((PyException) o);
		return repr(o);
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
		if (o instanceof PyException && name.equals("args"))
			return new PyTuple(((PyException) o).args);
		if (o instanceof PyType && name.equals("__name__"))
			return ((PyType) o).name;
		Builtin m = Methods.lookup(o, name);
		if (m != null)
			return new BoundMethod(o, m);
		throw err(Types.ATTRIBUTE_ERROR, "'" + typeName(o) + "' object has no attribute '" + name + "'");
	}
}
