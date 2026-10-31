package sanskrit;

/**
 * The integers of the interpreter. A Python int is a {@link Integer} (32 bits):
 * an operation whose exact result does not fit raises an OverflowError, it never
 * wraps around. Every operation that can overflow, and every test of the range,
 * is here, so the 32-bit rule is written once. The arguments of the operations
 * are the values of two ints, the results are boxed ints (or floats for a true
 * division or a negative power).
 */
final class PyInt {
	private PyInt() {
	}

	static final int MIN = Integer.MIN_VALUE;
	static final int MAX = Integer.MAX_VALUE;

	static PyError overflow() {
		return PyOps.err(Types.OVERFLOW, "integer overflow (this interpreter has 32-bit integers)");
	}

	/** True if the value is the value of an int. */
	static boolean fits(long v) {
		return v == (int) v;
	}

	/** True if the float is a whole number that fits in an int. */
	static boolean isWholeInt(double d) {
		return d == Math.rint(d) && d >= MIN && d <= MAX;
	}

	/** The value as an int, or an OverflowError. */
	static int check(long v) {
		if (v != (int) v)
			throw overflow();
		return (int) v;
	}

	/** The value as a boxed int, or an OverflowError. */
	static Integer box(long v) {
		return Integer.valueOf(check(v));
	}

	/** A float truncated towards zero, as int(); errors for NaN, the infinities and out of range values. */
	static int fromDouble(double d) {
		if (d != d)
			throw PyOps.err(Types.VALUE_ERROR, "cannot convert float NaN to integer");
		if (Double.isInfinite(d))
			throw PyOps.err(Types.OVERFLOW, "cannot convert float infinity to integer");
		if (d >= 2147483648.0 || d <= -2147483649.0)
			throw overflow();
		return (int) d;
	}

	static Object add(int x, int y) {
		return box((long) x + y);
	}

	static Object sub(int x, int y) {
		return box((long) x - y);
	}

	static Object mul(int x, int y) {
		return box((long) x * y);
	}

	static Object neg(int x) {
		if (x == MIN)
			throw overflow();
		return Integer.valueOf(-x);
	}

	static Object abs(int x) {
		if (x == MIN)
			throw overflow();
		return Integer.valueOf(Math.abs(x));
	}

	/** x // y. */
	static Object floorDiv(int x, int y) {
		if (y == 0)
			throw PyOps.err(Types.ZERO_DIVISION, "integer division or modulo by zero");
		return box(Math.floorDiv((long) x, (long) y));
	}

	/** x % y: the sign of the result is the sign of y. */
	static Object mod(int x, int y) {
		if (y == 0)
			throw PyOps.err(Types.ZERO_DIVISION, "integer modulo by zero");
		return Integer.valueOf((int) Math.floorMod((long) x, (long) y));
	}

	/** x / y, always a float. */
	static Object trueDiv(int x, int y) {
		if (y == 0)
			throw PyOps.err(Types.ZERO_DIVISION, "division by zero");
		return Double.valueOf((double) x / y);
	}

	/** x ** y: a float when y is negative. */
	static Object pow(int x, int y) {
		if (y < 0) {
			if (x == 0)
				throw PyOps.err(Types.ZERO_DIVISION, "0.0 cannot be raised to a negative power");
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

	static Object lshift(int x, int y) {
		if (y < 0)
			throw PyOps.err(Types.VALUE_ERROR, "negative shift count");
		if (x == 0)
			return Integer.valueOf(0);
		if (y >= 32)
			throw overflow();
		return box((long) x << y);
	}

	static Object rshift(int x, int y) {
		if (y < 0)
			throw PyOps.err(Types.VALUE_ERROR, "negative shift count");
		if (y >= 32)
			return Integer.valueOf(x < 0 ? -1 : 0);
		return Integer.valueOf(x >> y);
	}

	/** The binary operator {@code op} (an Op.BIN_* code) applied to two ints. */
	static Object binary(int op, int x, int y) {
		switch (op) {
		case Op.BIN_ADD:
			return add(x, y);
		case Op.BIN_SUB:
			return sub(x, y);
		case Op.BIN_MUL:
			return mul(x, y);
		case Op.BIN_DIV:
			return trueDiv(x, y);
		case Op.BIN_FLOORDIV:
			return floorDiv(x, y);
		case Op.BIN_MOD:
			return mod(x, y);
		case Op.BIN_POW:
			return pow(x, y);
		case Op.BIN_LSHIFT:
			return lshift(x, y);
		case Op.BIN_RSHIFT:
			return rshift(x, y);
		case Op.BIN_AND:
			return Integer.valueOf(x & y);
		case Op.BIN_OR:
			return Integer.valueOf(x | y);
		default:
			return Integer.valueOf(x ^ y);
		}
	}
}
