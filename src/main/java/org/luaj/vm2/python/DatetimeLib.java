package org.luaj.vm2.python;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The datetime module, a first version: the classes <code>date</code> and
 * <code>datetime</code> of CPython, written in Java, for naive objects in UTC.
 * <p>
 * They are <em>native classes</em> (<code>PyType.nativeClass</code>): the
 * methods are builtins bound to the instance, the instances cannot be
 * modified, and they have <code>__eq__</code> and <code>__hash__</code> (which a
 * class of the program may not), so that <code>d1 == d2</code> and the dates as
 * keys of a dict work on the values. The ordering methods are there too.
 * <p>
 * Not provided: <code>timedelta</code>, <code>time</code>, <code>timezone</code>,
 * <code>strptime</code>, the operators <code>+</code> and <code>-</code>, and
 * anything with a time zone (<code>tzinfo</code> is always <code>None</code>).
 * The arithmetic is done as in CPython with the methods that exist:
 * <code>date.fromordinal(d.toordinal() + 7)</code>,
 * <code>b.toordinal() - a.toordinal()</code>,
 * <code>datetime.fromtimestamp(dt.timestamp() + 3600)</code>. Everything is
 * computed from the proleptic Gregorian calendar of {@link TimeLib}, so the
 * result is the same on every edition.
 */
final class DatetimeLib {
	/** The ordinal of 1970-01-01 (the ordinal of 0001-01-01 is 1). */
	private static final long ORDINAL_1970 = 719163L;
	private static final long MAX_ORDINAL = 3652059L; // 9999-12-31
	private static final Object REQUIRED = new Object();

	private final PyType date;
	private final PyType datetime;

	private DatetimeLib(PyType date, PyType datetime) {
		this.date = date;
		this.datetime = datetime;
	}

	static PyModule create(Interp in) {
		final Map<String, Object> dateDict = new LinkedHashMap<String, Object>();
		dateDict.put("__module__", "datetime");
		final PyType date = new PyType("date", Types.OBJECT, false, dateDict, "date", "datetime");
		final Map<String, Object> datetimeDict = new LinkedHashMap<String, Object>();
		datetimeDict.put("__module__", "datetime");
		final PyType datetime = new PyType("datetime", date, false, datetimeDict, "datetime", "datetime");
		for (PyType t : new PyType[] { date, datetime }) {
			t.interp = in;
			t.nativeClass = true;
		}
		final DatetimeLib lib = new DatetimeLib(date, datetime);
		lib.fillDate(dateDict);
		lib.fillDatetime(datetimeDict);

		final PyModule m = new PyModule("datetime");
		m.members.put("date", date);
		m.members.put("datetime", datetime);
		m.members.put("MINYEAR", Integer.valueOf(1));
		m.members.put("MAXYEAR", Integer.valueOf(9999));
		return m;
	}

	// ------------------------------------------------------------ instances

	private PyInstance newDate(long y, long m, long d) {
		checkDate(y, m, d);
		final PyInstance i = new PyInstance(date);
		fillDateFields(i, y, m, d);
		return i;
	}

	private PyInstance newDatetime(long y, long m, long d, long h, long mi, long s, long us) {
		checkDate(y, m, d);
		checkTime(h, mi, s, us);
		final PyInstance i = new PyInstance(datetime);
		fillDateFields(i, y, m, d);
		i.attrs.put("hour", Integer.valueOf((int) h));
		i.attrs.put("minute", Integer.valueOf((int) mi));
		i.attrs.put("second", Integer.valueOf((int) s));
		i.attrs.put("microsecond", Integer.valueOf((int) us));
		i.attrs.put("tzinfo", PyNone.NONE);
		return i;
	}

	private static void fillDateFields(PyInstance i, long y, long m, long d) {
		i.attrs.put("year", Integer.valueOf((int) y));
		i.attrs.put("month", Integer.valueOf((int) m));
		i.attrs.put("day", Integer.valueOf((int) d));
	}

	private static void checkDate(long y, long m, long d) {
		if (y < 1 || y > 9999)
			throw PyOps.err(Types.VALUE_ERROR, "year " + y + " is out of range");
		if (m < 1 || m > 12)
			throw PyOps.err(Types.VALUE_ERROR, "month must be in 1..12");
		if (d < 1 || d > daysInMonth(y, m))
			throw PyOps.err(Types.VALUE_ERROR, "day is out of range for month");
	}

	private static void checkTime(long h, long mi, long s, long us) {
		if (h < 0 || h > 23)
			throw PyOps.err(Types.VALUE_ERROR, "hour must be in 0..23");
		if (mi < 0 || mi > 59)
			throw PyOps.err(Types.VALUE_ERROR, "minute must be in 0..59");
		if (s < 0 || s > 59)
			throw PyOps.err(Types.VALUE_ERROR, "second must be in 0..59");
		if (us < 0 || us > 999999)
			throw PyOps.err(Types.VALUE_ERROR, "microsecond must be in 0..999999");
	}

	private static long daysInMonth(long y, long m) {
		if (m == 2)
			return y % 4 == 0 && (y % 100 != 0 || y % 400 == 0) ? 29 : 28;
		return m == 4 || m == 6 || m == 9 || m == 11 ? 30 : 31;
	}

	/** The instance behind self, which must be a date or a datetime. */
	private PyInstance self(Object o) {
		if (o instanceof PyInstance && ((PyInstance) o).type.isSubtypeOf(date))
			return (PyInstance) o;
		throw PyOps.err(Types.TYPE_ERROR, "descriptor requires a 'datetime.date' object but received a '"
				+ PyOps.typeName(o) + "'");
	}

	private boolean isDatetime(PyInstance i) {
		return i.type == datetime;
	}

	private static int field(PyInstance i, String name) {
		final Object v = i.attrs.get(name);
		return v == null ? 0 : ((Integer) v).intValue();
	}

	private static long ordinal(PyInstance i) {
		return TimeLib.daysFromCivil(field(i, "year"), field(i, "month"), field(i, "day")) + ORDINAL_1970;
	}

	/** Microseconds since 0001-01-01: what the comparisons and the hash use. */
	private long key(PyInstance i) {
		final long days = ordinal(i);
		if (isDatetime(i) == false)
			return days;
		final long secs = days * 86400L + field(i, "hour") * 3600L + field(i, "minute") * 60L + field(i, "second");
		return secs * 1000000L + field(i, "microsecond");
	}

	/** The 9 fields of a time tuple (weekday: Monday is 0; the last, isdst, is -1 as for a naive object). */
	private static long[] tuple(PyInstance i) {
		final int y = field(i, "year"), m = field(i, "month"), d = field(i, "day");
		final long days = TimeLib.daysFromCivil(y, m, d);
		final long wday = Math.floorMod(days + 3, 7L);
		final long yday = days - TimeLib.daysFromCivil(y, 1, 1) + 1;
		return new long[] { y, m, d, field(i, "hour"), field(i, "minute"), field(i, "second"), wday, yday, -1 };
	}

	// ------------------------------------------------------------ arguments

	private static long intArg(Object o) {
		if (o instanceof Integer)
			return ((Integer) o).longValue();
		if (o instanceof Boolean)
			return ((Boolean) o).booleanValue() ? 1 : 0;
		throw PyOps.err(Types.TYPE_ERROR, "'" + PyOps.typeName(o) + "' object cannot be interpreted as an integer");
	}

	private static String strArg(Object o, String what) {
		if (o instanceof String == false)
			throw PyOps.err(Types.TYPE_ERROR, what + " must be str, not " + PyOps.typeName(o));
		return (String) o;
	}

	/** Binds the positional (from index from) and the keyword arguments to the names, with defaults. */
	private static Object[] bind(String fn, String[] names, Object[] defaults, Object[] pos, int from,
			Map<String, Object> kw) {
		final Object[] r = new Object[names.length];
		final int np = pos.length - from;
		if (np > names.length)
			throw PyOps.err(Types.TYPE_ERROR,
					fn + "() takes at most " + names.length + " arguments (" + np + " given)");
		for (int i = 0; i < np; i++)
			r[i] = pos[from + i];
		if (kw != null) {
			for (Map.Entry<String, Object> e : kw.entrySet()) {
				int idx = -1;
				for (int i = 0; i < names.length; i++)
					if (names[i].equals(e.getKey()))
						idx = i;
				if (idx < 0)
					throw PyOps.err(Types.TYPE_ERROR,
							"'" + e.getKey() + "' is an invalid keyword argument for " + fn + "()");
				if (idx < np)
					throw PyOps.err(Types.TYPE_ERROR, fn + "() got multiple values for argument '" + e.getKey() + "'");
				r[idx] = e.getValue();
			}
		}
		for (int i = 0; i < names.length; i++) {
			if (r[i] == null) {
				if (defaults[i] == REQUIRED)
					throw PyOps.err(Types.TYPE_ERROR,
							"function missing required argument '" + names[i] + "' (pos " + (i + 1) + ")");
				r[i] = defaults[i];
			}
		}
		return r;
	}

	private static void noTimezone(Object tz) {
		if (tz != PyNone.NONE)
			throw PyOps.err(Types.TYPE_ERROR, "time zones are not supported: tzinfo must be None");
	}

	// ------------------------------------------------------------ text

	private static String pad(long v, int width) {
		String s = Long.toString(v);
		while (s.length() < width)
			s = "0" + s;
		return s;
	}

	private static String isoDate(PyInstance i) {
		return pad(field(i, "year"), 4) + "-" + pad(field(i, "month"), 2) + "-" + pad(field(i, "day"), 2);
	}

	private String isoDatetime(PyInstance i, String sep, String timespec) {
		final String h = pad(field(i, "hour"), 2), mi = pad(field(i, "minute"), 2), s = pad(field(i, "second"), 2);
		final int us = field(i, "microsecond");
		String time;
		switch (timespec) {
		case "auto":
			time = us != 0 ? h + ":" + mi + ":" + s + "." + pad(us, 6) : h + ":" + mi + ":" + s;
			break;
		case "hours":
			time = h;
			break;
		case "minutes":
			time = h + ":" + mi;
			break;
		case "seconds":
			time = h + ":" + mi + ":" + s;
			break;
		case "milliseconds":
			time = h + ":" + mi + ":" + s + "." + pad(us / 1000, 3);
			break;
		case "microseconds":
			time = h + ":" + mi + ":" + s + "." + pad(us, 6);
			break;
		default:
			throw PyOps.err(Types.VALUE_ERROR, "Unknown timespec value");
		}
		return isoDate(i) + sep + time;
	}

	private String repr(PyInstance i) {
		final StringBuilder sb = new StringBuilder(isDatetime(i) ? "datetime.datetime(" : "datetime.date(");
		sb.append(field(i, "year")).append(", ").append(field(i, "month")).append(", ").append(field(i, "day"));
		if (isDatetime(i)) {
			sb.append(", ").append(field(i, "hour")).append(", ").append(field(i, "minute"));
			if (field(i, "microsecond") != 0)
				sb.append(", ").append(field(i, "second")).append(", ").append(field(i, "microsecond"));
			else if (field(i, "second") != 0)
				sb.append(", ").append(field(i, "second"));
		}
		return sb.append(')').toString();
	}

	/**
	 * strftime of C99 as time.strftime has it, with what a naive object changes: %Z and %z
	 * are empty, and %f is the microseconds on 6 digits (a directive of Python).
	 */
	private static String strftime(PyInstance i, String fmt) {
		final StringBuilder sb = new StringBuilder();
		for (int k = 0; k < fmt.length(); k++) {
			final char c = fmt.charAt(k);
			if (c == '%' && k + 1 < fmt.length()) {
				final char d = fmt.charAt(++k);
				if (d == 'f')
					sb.append(pad(field(i, "microsecond"), 6));
				else if (d != 'z' && d != 'Z')
					sb.append('%').append(d);
				continue;
			}
			sb.append(c);
		}
		return TimeLib.format(sb.toString(), tuple(i));
	}

	private static PyTuple ints(long... values) {
		final Object[] items = new Object[values.length];
		for (int k = 0; k < values.length; k++)
			items[k] = Integer.valueOf((int) values[k]);
		return new PyTuple(items);
	}

	// ------------------------------------------------------------ the methods

	private void method(Map<String, Object> dict, String name, Builtin.Fn fn) {
		dict.put(name, Builtin.method(name, fn));
	}

	private void methodKw(Map<String, Object> dict, String name, Builtin.KwFn fn) {
		dict.put(name, Builtin.methodKw(name, fn));
	}

	private void classFn(Map<String, Object> dict, String name, Builtin.Fn fn) {
		dict.put(name, new Builtin(name, fn));
	}

	private static void count(String name, Object[] a, int n) {
		if (a.length != n)
			throw PyOps.err(Types.TYPE_ERROR,
					name + "() takes no arguments (" + (a.length - 1) + " given)");
	}

	/** __eq__, __hash__ and the four ordering methods, on the key of the instance. */
	private void comparisons(Map<String, Object> dict) {
		method(dict, "__eq__", (in, a) -> {
			final PyInstance x = self(a[0]);
			if (a[1] instanceof PyInstance == false || ((PyInstance) a[1]).type != x.type)
				return PyNotImplemented.INSTANCE;
			return Boolean.valueOf(key(x) == key((PyInstance) a[1]));
		});
		method(dict, "__hash__", (in, a) -> {
			final long k = key(self(a[0]));
			return Integer.valueOf((int) (k ^ (k >>> 32)));
		});
		for (final String op : new String[] { "__lt__", "__le__", "__gt__", "__ge__" }) {
			method(dict, op, (in, a) -> {
				final PyInstance x = self(a[0]);
				if (a[1] instanceof PyInstance == false || ((PyInstance) a[1]).type != x.type)
					return PyNotImplemented.INSTANCE;
				final long p = key(x), q = key((PyInstance) a[1]);
				switch (op) {
				case "__lt__":
					return Boolean.valueOf(p < q);
				case "__le__":
					return Boolean.valueOf(p <= q);
				case "__gt__":
					return Boolean.valueOf(p > q);
				default:
					return Boolean.valueOf(p >= q);
				}
			});
		}
	}

	private void fillDate(Map<String, Object> d) {
		dateInit(d);
		comparisons(d);
		method(d, "__repr__", (in, a) -> repr(self(a[0])));
		method(d, "__str__", (in, a) -> isoDate(self(a[0])));
		method(d, "isoformat", (in, a) -> {
			count("isoformat", a, 1);
			return isoDate(self(a[0]));
		});
		method(d, "weekday", (in, a) -> {
			count("weekday", a, 1);
			return Integer.valueOf((int) ((ordinal(self(a[0])) + 6) % 7));
		});
		method(d, "isoweekday", (in, a) -> {
			count("isoweekday", a, 1);
			return Integer.valueOf((int) ((ordinal(self(a[0])) + 6) % 7) + 1);
		});
		method(d, "isocalendar", (in, a) -> {
			count("isocalendar", a, 1);
			final long[] t = tuple(self(a[0]));
			final long[] iso = TimeLib.isoWeek(t);
			return ints(iso[0], iso[1], t[6] + 1);
		});
		method(d, "toordinal", (in, a) -> {
			count("toordinal", a, 1);
			return Integer.valueOf((int) ordinal(self(a[0])));
		});
		method(d, "timetuple", (in, a) -> {
			count("timetuple", a, 1);
			return ints(tuple(self(a[0])));
		});
		method(d, "strftime", (in, a) -> {
			if (a.length != 2)
				throw PyOps.err(Types.TYPE_ERROR, "strftime() takes exactly one argument (" + (a.length - 1) + " given)");
			return strftime(self(a[0]), strArg(a[1], "strftime() argument 1"));
		});
		method(d, "ctime", (in, a) -> {
			count("ctime", a, 1);
			// as CPython, the year is on 4 digits (time.asctime does not pad it)
			final PyInstance x = self(a[0]);
			return strftime(x, "%a %b %e %H:%M:%S ") + pad(field(x, "year"), 4);
		});
		methodKw(d, "replace", (in, a, kw) -> {
			final PyInstance x = self(a[0]);
			final Object[] v = bind("replace", new String[] { "year", "month", "day" },
					new Object[] { Integer.valueOf(field(x, "year")), Integer.valueOf(field(x, "month")),
							Integer.valueOf(field(x, "day")) },
					a, 1, kw);
			return newDate(intArg(v[0]), intArg(v[1]), intArg(v[2]));
		});
		classFn(d, "today", (in, a) -> {
			if (a.length != 0)
				throw PyOps.err(Types.TYPE_ERROR, "today() takes no arguments (" + a.length + " given)");
			return dateOfSeconds(Math.floorDiv(in.currentTimeMillis(), 1000L));
		});
		classFn(d, "fromordinal", (in, a) -> {
			if (a.length != 1)
				throw PyOps.err(Types.TYPE_ERROR, "fromordinal() takes exactly one argument (" + a.length + " given)");
			final long n = intArg(a[0]);
			checkOrdinal(n);
			final long[] c = TimeLib.civilFromDays(n - ORDINAL_1970);
			return newDate(c[0], c[1], c[2]);
		});
		classFn(d, "fromtimestamp", (in, a) -> {
			if (a.length != 1)
				throw PyOps.err(Types.TYPE_ERROR, "fromtimestamp() takes exactly one argument (" + a.length + " given)");
			return dateOfSeconds(timestamp(a[0])[0]);
		});
		classFn(d, "fromisoformat", (in, a) -> {
			if (a.length != 1)
				throw PyOps.err(Types.TYPE_ERROR, "fromisoformat() takes exactly one argument (" + a.length + " given)");
			final String s = strArg(a[0], "fromisoformat: argument");
			final long[] ymd = parseDate(s, s);
			return newDate(ymd[0], ymd[1], ymd[2]);
		});
		d.put("min", newDate(1, 1, 1));
		d.put("max", newDate(9999, 12, 31));
	}

	private void dateInit(Map<String, Object> d) {
		methodKw(d, "__init__", (in, a, kw) -> {
			final Object[] v = bind("date", new String[] { "year", "month", "day" },
					new Object[] { REQUIRED, REQUIRED, REQUIRED }, a, 1, kw);
			final long y = intArg(v[0]), m = intArg(v[1]), day = intArg(v[2]);
			checkDate(y, m, day);
			fillDateFields((PyInstance) a[0], y, m, day);
			return PyNone.NONE;
		});
	}

	private void fillDatetime(Map<String, Object> d) {
		methodKw(d, "__init__", (in, a, kw) -> {
			final Object[] v = bind("datetime",
					new String[] { "year", "month", "day", "hour", "minute", "second", "microsecond", "tzinfo" },
					new Object[] { REQUIRED, REQUIRED, REQUIRED, Integer.valueOf(0), Integer.valueOf(0),
							Integer.valueOf(0), Integer.valueOf(0), PyNone.NONE },
					a, 1, kw);
			noTimezone(v[7]);
			final long y = intArg(v[0]), m = intArg(v[1]), day = intArg(v[2]);
			final long h = intArg(v[3]), mi = intArg(v[4]), s = intArg(v[5]), us = intArg(v[6]);
			checkDate(y, m, day);
			checkTime(h, mi, s, us);
			final PyInstance i = (PyInstance) a[0];
			fillDateFields(i, y, m, day);
			i.attrs.put("hour", Integer.valueOf((int) h));
			i.attrs.put("minute", Integer.valueOf((int) mi));
			i.attrs.put("second", Integer.valueOf((int) s));
			i.attrs.put("microsecond", Integer.valueOf((int) us));
			i.attrs.put("tzinfo", PyNone.NONE);
			return PyNone.NONE;
		});
		comparisons(d);
		method(d, "__repr__", (in, a) -> repr(self(a[0])));
		method(d, "__str__", (in, a) -> isoDatetime(self(a[0]), " ", "auto"));
		methodKw(d, "isoformat", (in, a, kw) -> {
			final Object[] v = bind("isoformat", new String[] { "sep", "timespec" },
					new Object[] { "T", "auto" }, a, 1, kw);
			return isoDatetime(self(a[0]), strArg(v[0], "isoformat() argument 1"),
					strArg(v[1], "isoformat() argument 2"));
		});
		method(d, "timestamp", (in, a) -> {
			count("timestamp", a, 1);
			final PyInstance x = self(a[0]);
			// a naive object is read as UTC here (CPython reads it as local time)
			final long secs = (ordinal(x) - ORDINAL_1970) * 86400L + field(x, "hour") * 3600L
					+ field(x, "minute") * 60L + field(x, "second");
			return Double.valueOf(secs + field(x, "microsecond") / 1000000.0);
		});
		method(d, "date", (in, a) -> {
			count("date", a, 1);
			final PyInstance x = self(a[0]);
			return newDate(field(x, "year"), field(x, "month"), field(x, "day"));
		});
		methodKw(d, "replace", (in, a, kw) -> {
			final PyInstance x = self(a[0]);
			final Object[] v = bind("replace",
					new String[] { "year", "month", "day", "hour", "minute", "second", "microsecond", "tzinfo" },
					new Object[] { Integer.valueOf(field(x, "year")), Integer.valueOf(field(x, "month")),
							Integer.valueOf(field(x, "day")), Integer.valueOf(field(x, "hour")),
							Integer.valueOf(field(x, "minute")), Integer.valueOf(field(x, "second")),
							Integer.valueOf(field(x, "microsecond")), PyNone.NONE },
					a, 1, kw);
			noTimezone(v[7]);
			return newDatetime(intArg(v[0]), intArg(v[1]), intArg(v[2]), intArg(v[3]), intArg(v[4]), intArg(v[5]),
					intArg(v[6]));
		});
		// the methods of date that do not change are inherited: weekday, isoweekday, isocalendar,
		// toordinal, timetuple, strftime, ctime
		classFn(d, "now", (in, a) -> {
			if (a.length > 1 || (a.length == 1 && a[0] != PyNone.NONE))
				throw PyOps.err(Types.TYPE_ERROR, "time zones are not supported: tz must be None");
			final long ms = in.currentTimeMillis();
			return datetimeOf(Math.floorDiv(ms, 1000L), Math.floorMod(ms, 1000L) * 1000L);
		});
		classFn(d, "today", (in, a) -> {
			if (a.length != 0)
				throw PyOps.err(Types.TYPE_ERROR, "today() takes no arguments (" + a.length + " given)");
			final long ms = in.currentTimeMillis();
			return datetimeOf(Math.floorDiv(ms, 1000L), Math.floorMod(ms, 1000L) * 1000L);
		});
		classFn(d, "fromordinal", (in, a) -> {
			if (a.length != 1)
				throw PyOps.err(Types.TYPE_ERROR, "fromordinal() takes exactly one argument (" + a.length + " given)");
			final long n = intArg(a[0]);
			checkOrdinal(n);
			return datetimeOf((n - ORDINAL_1970) * 86400L, 0);
		});
		classFn(d, "fromtimestamp", (in, a) -> {
			if (a.length < 1 || a.length > 2)
				throw PyOps.err(Types.TYPE_ERROR, "fromtimestamp() takes 1 or 2 arguments (" + a.length + " given)");
			if (a.length == 2)
				noTimezone(a[1]);
			final long[] st = timestamp(a[0]);
			return datetimeOf(st[0], st[1]);
		});
		classFn(d, "fromisoformat", (in, a) -> {
			if (a.length != 1)
				throw PyOps.err(Types.TYPE_ERROR, "fromisoformat() takes exactly one argument (" + a.length + " given)");
			return parseDatetime(strArg(a[0], "fromisoformat: argument"));
		});
		d.put("min", newDatetime(1, 1, 1, 0, 0, 0, 0));
		d.put("max", newDatetime(9999, 12, 31, 23, 59, 59, 999999));
	}

	// ------------------------------------------------------------ building from numbers and text

	private static void checkOrdinal(long n) {
		if (n < 1)
			throw PyOps.err(Types.VALUE_ERROR, "ordinal must be >= 1");
		if (n > MAX_ORDINAL)
			throw PyOps.err(Types.VALUE_ERROR, "year 10000 is out of range");
	}

	private PyInstance dateOfSeconds(long secs) {
		final long[] c = TimeLib.civilFromDays(Math.floorDiv(secs, 86400L));
		return newDate(c[0], c[1], c[2]);
	}

	private PyInstance datetimeOf(long secs, long microseconds) {
		final long days = Math.floorDiv(secs, 86400L);
		final long rem = Math.floorMod(secs, 86400L);
		final long[] c = TimeLib.civilFromDays(days);
		return newDatetime(c[0], c[1], c[2], rem / 3600, rem % 3600 / 60, rem % 60, microseconds);
	}

	/**
	 * The whole seconds and the microseconds of a timestamp, as CPython does it: the
	 * fraction is rounded to the nearest microsecond (half to even) and carried.
	 */
	private static long[] timestamp(Object o) {
		if (o instanceof Integer)
			return new long[] { ((Integer) o).longValue(), 0 };
		if (o instanceof Double == false)
			throw PyOps.err(Types.TYPE_ERROR, "'" + PyOps.typeName(o) + "' object cannot be interpreted as an integer");
		final double t = ((Double) o).doubleValue();
		if (t != t)
			throw PyOps.err(Types.VALUE_ERROR, "Invalid value NaN (not a number)");
		if (t >= 253402300800.0 || t < -62135596800.0)
			throw PyOps.err(Types.VALUE_ERROR, "year is out of range");
		final double whole = t < 0 ? Math.ceil(t) : Math.floor(t);
		long secs = (long) whole;
		long us = (long) Math.rint((t - whole) * 1e6);
		if (us >= 1000000L) {
			secs += 1;
			us -= 1000000L;
		} else if (us < 0) {
			secs -= 1;
			us += 1000000L;
		}
		return new long[] { secs, us };
	}

	private static PyError invalid(String whole) {
		return PyOps.err(Types.VALUE_ERROR, "Invalid isoformat string: " + Fmt.reprStr(whole));
	}

	private static boolean digits(String s, int from, int to) {
		if (from < 0 || to > s.length() || from >= to)
			return false;
		for (int k = from; k < to; k++)
			if (s.charAt(k) < '0' || s.charAt(k) > '9')
				return false;
		return true;
	}

	private static long number(String s, int from, int to) {
		return Long.parseLong(s.substring(from, to));
	}

	/** YYYY-MM-DD, nothing more: the first 10 characters of s. */
	private static long[] parseDate(String s, String whole) {
		if (s.length() != 10 || s.charAt(4) != '-' || s.charAt(7) != '-' || digits(s, 0, 4) == false
				|| digits(s, 5, 7) == false || digits(s, 8, 10) == false)
			throw invalid(whole);
		return new long[] { number(s, 0, 4), number(s, 5, 7), number(s, 8, 10) };
	}

	/** YYYY-MM-DD, then optionally one separator character and HH[:MM[:SS[.f...]]]; no offset, no Z. */
	private PyInstance parseDatetime(String s) {
		if (s.length() < 10)
			throw invalid(s);
		final long[] ymd = parseDate(s.substring(0, 10), s);
		long h = 0, mi = 0, sec = 0, us = 0;
		if (s.length() > 10) {
			// the separator is any one character, as in CPython 3.11
			final String t = s.substring(11);
			if (digits(t, 0, 2) == false)
				throw invalid(s);
			h = number(t, 0, 2);
			int p = 2;
			if (p < t.length()) {
				if (t.charAt(p) != ':' || digits(t, p + 1, p + 3) == false)
					throw invalid(s);
				mi = number(t, p + 1, p + 3);
				p += 3;
			}
			if (p < t.length()) {
				if (t.charAt(p) != ':' || digits(t, p + 1, p + 3) == false)
					throw invalid(s);
				sec = number(t, p + 1, p + 3);
				p += 3;
			}
			if (p < t.length()) {
				if (t.charAt(p) != '.' && t.charAt(p) != ',')
					throw invalid(s);
				int q = p + 1;
				while (q < t.length() && t.charAt(q) >= '0' && t.charAt(q) <= '9')
					q++;
				if (q == p + 1 || q != t.length())
					throw invalid(s);
				// at most 6 digits count, the others are cut; fewer are completed with zeros
				String f = t.substring(p + 1, q);
				f = f.length() > 6 ? f.substring(0, 6) : f;
				while (f.length() < 6)
					f += "0";
				us = Long.parseLong(f);
			}
		}
		// a field out of range raises the same ValueError as the constructor
		return newDatetime(ymd[0], ymd[1], ymd[2], h, mi, sec, us);
	}
}
