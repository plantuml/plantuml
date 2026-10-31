package org.luaj.vm2.python;

/** The math module. */
final class MathLib {
	private MathLib() {
	}

	private interface D1 {
		double f(double x);
	}

	private interface D2 {
		double f(double x, double y);
	}

	private static void put(PyModule m, String name, Builtin.Fn fn) {
		m.members.put(name, new Builtin(name, fn));
	}

	private static double arg(Object[] a, int i, String name) {
		return PyOps.asDouble(a[i]);
	}

	private static void one(PyModule m, final String name, final D1 f, final boolean checkDomain) {
		put(m, name, (in, a) -> {
			Fmt0.count(name, a, 1);
			double x = arg(a, 0, name);
			double r = f.f(x);
			if (checkDomain && r != r && x == x)
				throw PyOps.err(Types.VALUE_ERROR, "math domain error");
			if (Double.isInfinite(r) && !Double.isInfinite(x))
				throw PyOps.err(Types.OVERFLOW, "math range error");
			return Double.valueOf(r);
		});
	}

	private static void two(PyModule m, final String name, final D2 f) {
		put(m, name, (in, a) -> {
			Fmt0.count(name, a, 2);
			double x = arg(a, 0, name), y = arg(a, 1, name);
			double r = f.f(x, y);
			if (r != r && x == x && y == y)
				throw PyOps.err(Types.VALUE_ERROR, "math domain error");
			return Double.valueOf(r);
		});
	}

	private static Object box(long r) {
		if (r != (int) r)
			throw PyOps.overflow();
		return Integer.valueOf((int) r);
	}

	private static boolean isClose(double a, double b, double rel, double abs) {
		if (a == b)
			return true;
		if (Double.isInfinite(a) || Double.isInfinite(b))
			return false;
		double diff = Math.abs(b - a);
		return diff <= Math.abs(rel * b) || diff <= Math.abs(rel * a) || diff <= abs;
	}

	static PyModule create() {
		PyModule m = new PyModule("math");
		m.members.put("pi", Double.valueOf(Math.PI));
		m.members.put("e", Double.valueOf(Math.E));
		m.members.put("inf", Double.valueOf(Double.POSITIVE_INFINITY));
		m.members.put("nan", Double.valueOf(Double.NaN));
		m.members.put("tau", Double.valueOf(2 * Math.PI));
		one(m, "sinh", Math::sinh, false);
		one(m, "cosh", Math::cosh, false);
		one(m, "tanh", Math::tanh, false);
		one(m, "expm1", Math::expm1, false);
		one(m, "degrees", Math::toDegrees, false);
		one(m, "radians", Math::toRadians, false);
		put(m, "log1p", (in, a) -> {
			Fmt0.count("log1p", a, 1);
			double x = arg(a, 0, "log1p");
			if (x <= -1)
				throw PyOps.err(Types.VALUE_ERROR, "math domain error");
			return Double.valueOf(Math.log1p(x));
		});
		put(m, "log2", (in, a) -> {
			Fmt0.count("log2", a, 1);
			double x = arg(a, 0, "log2");
			if (x <= 0)
				throw PyOps.err(Types.VALUE_ERROR, "math domain error");
			int e = Math.getExponent(x);
			if (x == Math.scalb(1.0, e))
				return Double.valueOf(e);
			return Double.valueOf(Math.log(x) / Math.log(2));
		});
		two(m, "hypot", Math::hypot);
		two(m, "copysign", Math::copySign);
		put(m, "isfinite", (in, a) -> {
			Fmt0.count("isfinite", a, 1);
			double x = arg(a, 0, "isfinite");
			return Boolean.valueOf(!Double.isNaN(x) && !Double.isInfinite(x));
		});
		put(m, "gcd", (in, a) -> {
			long g = 0;
			for (Object o : a) {
				if (!PyOps.isIntLike(o))
					throw PyOps.err(Types.TYPE_ERROR, "'" + PyOps.typeName(o) + "' object cannot be interpreted as an integer");
				long x = Math.abs((long) PyOps.asInt(o));
				while (x != 0) {
					long t = g % x;
					g = x;
					x = t;
				}
			}
			return PyOps.binary(Op.BIN_ADD, Integer.valueOf(0), box(g));
		});
		put(m, "factorial", (in, a) -> {
			Fmt0.count("factorial", a, 1);
			if (!PyOps.isIntLike(a[0]))
				throw PyOps.err(Types.TYPE_ERROR, "'" + PyOps.typeName(a[0]) + "' object cannot be interpreted as an integer");
			int n = PyOps.asInt(a[0]);
			if (n < 0)
				throw PyOps.err(Types.VALUE_ERROR, "factorial() not defined for negative values");
			long r = 1;
			for (int i = 2; i <= n; i++) {
				r *= i;
				if (r != (int) r)
					throw PyOps.overflow();
			}
			return box(r);
		});
		put(m, "fsum", (in, a) -> {
			Fmt0.count("fsum", a, 1);
			java.math.BigDecimal sum = java.math.BigDecimal.ZERO;
			PyIter it = PyOps.getIter(a[0]);
			double special = 0;
			boolean hasSpecial = false;
			for (Object x = it.next(); x != null; x = it.next()) {
				double d = PyOps.asDouble(x);
				if (Double.isNaN(d) || Double.isInfinite(d)) {
					special += d;
					hasSpecial = true;
				} else {
					sum = sum.add(new java.math.BigDecimal(d));
				}
			}
			return Double.valueOf(hasSpecial ? special : sum.doubleValue());
		});
		put(m, "isclose", (in, a) -> {
			Fmt0.count("isclose", a, 2);
			return Boolean.valueOf(isClose(arg(a, 0, "isclose"), arg(a, 1, "isclose"), 1e-9, 0.0));
		});
		m.members.put("isclose", new Builtin("isclose", (in, a) -> {
			Fmt0.count("isclose", a, 2);
			return Boolean.valueOf(isClose(arg(a, 0, "isclose"), arg(a, 1, "isclose"), 1e-9, 0.0));
		}, (in, a, kw) -> {
			Fmt0.count("isclose", a, 2);
			Methods.noKw(kw, "isclose", "rel_tol", "abs_tol");
			double rel = kw.containsKey("rel_tol") ? PyOps.asDouble(kw.get("rel_tol")) : 1e-9;
			double abs = kw.containsKey("abs_tol") ? PyOps.asDouble(kw.get("abs_tol")) : 0.0;
			return Boolean.valueOf(isClose(arg(a, 0, "isclose"), arg(a, 1, "isclose"), rel, abs));
		}));
		one(m, "sin", Math::sin, true);
		one(m, "cos", Math::cos, true);
		one(m, "tan", Math::tan, true);
		one(m, "asin", Math::asin, true);
		one(m, "acos", Math::acos, true);
		one(m, "atan", Math::atan, false);
		one(m, "exp", Math::exp, false);
		one(m, "fabs", Math::abs, false);
		put(m, "sqrt", (in, a) -> {
			Fmt0.count("sqrt", a, 1);
			double x = arg(a, 0, "sqrt");
			if (x < 0)
				throw PyOps.err(Types.VALUE_ERROR, "math domain error");
			return Double.valueOf(Math.sqrt(x));
		});
		put(m, "log", (in, a) -> {
			if (a.length < 1 || a.length > 2)
				throw PyOps.err(Types.TYPE_ERROR, "log expected 1 or 2 arguments, got " + a.length);
			double x = arg(a, 0, "log");
			if (x <= 0)
				throw PyOps.err(Types.VALUE_ERROR, "math domain error");
			double r = Math.log(x);
			if (a.length == 2) {
				double b = arg(a, 1, "log");
				if (b <= 0)
					throw PyOps.err(Types.VALUE_ERROR, "math domain error");
				double lb = Math.log(b);
				if (lb == 0)
					throw PyOps.err(Types.ZERO_DIVISION, "float division by zero");
				r /= lb;
			}
			return Double.valueOf(r);
		});
		put(m, "log10", (in, a) -> {
			Fmt0.count("log10", a, 1);
			double x = arg(a, 0, "log10");
			if (x <= 0)
				throw PyOps.err(Types.VALUE_ERROR, "math domain error");
			return Double.valueOf(Math.log10(x));
		});
		two(m, "atan2", Math::atan2);
		two(m, "fmod", (x, y) -> {
			if (y == 0 && x == x)
				throw PyOps.err(Types.VALUE_ERROR, "math domain error");
			if (Double.isInfinite(x))
				throw PyOps.err(Types.VALUE_ERROR, "math domain error");
			return x % y;
		});
		put(m, "pow", (in, a) -> {
			Fmt0.count("pow", a, 2);
			double x = arg(a, 0, "pow"), y = arg(a, 1, "pow");
			if (x == 0 && y < 0)
				throw PyOps.err(Types.VALUE_ERROR, "math domain error");
			if (x < 0 && !Double.isInfinite(y) && y != Math.rint(y))
				throw PyOps.err(Types.VALUE_ERROR, "math domain error");
			double r = Math.pow(x, y);
			if (Double.isInfinite(r) && !Double.isInfinite(x) && !Double.isInfinite(y))
				throw PyOps.err(Types.OVERFLOW, "math range error");
			return Double.valueOf(r);
		});
		put(m, "floor", (in, a) -> {
			Fmt0.count("floor", a, 1);
			if (PyOps.isIntLike(a[0]))
				return Integer.valueOf(PyOps.asInt(a[0]));
			return Integer.valueOf(PyOps.floatToInt(Math.floor(arg(a, 0, "floor"))));
		});
		put(m, "ceil", (in, a) -> {
			Fmt0.count("ceil", a, 1);
			if (PyOps.isIntLike(a[0]))
				return Integer.valueOf(PyOps.asInt(a[0]));
			return Integer.valueOf(PyOps.floatToInt(Math.ceil(arg(a, 0, "ceil"))));
		});
		put(m, "trunc", (in, a) -> {
			Fmt0.count("trunc", a, 1);
			if (PyOps.isIntLike(a[0]))
				return Integer.valueOf(PyOps.asInt(a[0]));
			return Integer.valueOf(PyOps.floatToInt(arg(a, 0, "trunc")));
		});
		put(m, "isnan", (in, a) -> {
			Fmt0.count("isnan", a, 1);
			return Boolean.valueOf(Double.isNaN(arg(a, 0, "isnan")));
		});
		put(m, "isinf", (in, a) -> {
			Fmt0.count("isinf", a, 1);
			return Boolean.valueOf(Double.isInfinite(arg(a, 0, "isinf")));
		});
		return m;
	}

	/** Argument counting shared by the module functions. */
	private static final class Fmt0 {
		static void count(String name, Object[] a, int n) {
			if (a.length != n)
				throw PyOps.err(Types.TYPE_ERROR, name + "() takes exactly " + n + " argument" + (n == 1 ? "" : "s")
						+ " (" + a.length + " given)");
		}
	}
}
