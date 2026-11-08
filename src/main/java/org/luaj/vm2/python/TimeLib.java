package org.luaj.vm2.python;

/**
 * The time module: the functions of CPython's <code>time</code> that a diagram
 * can use, with the same names and the same results, in UTC and in English.
 * <p>
 * Everything is computed here, from the number of seconds since the epoch: no
 * <code>java.time</code>, no <code>SimpleDateFormat</code>, no locale and no
 * time zone database, so the result is the same on every edition and on every
 * machine. The only thing that is not deterministic is the clock itself
 * (<code>time()</code>, and the functions called without argument).
 * <ul>
 * <li><code>time()</code>: seconds since the epoch, a float.</li>
 * <li><code>gmtime([secs])</code>, <code>localtime([secs])</code>: the 9-tuple
 * (year, month, day, hour, minute, second, weekday with Monday 0, day of the
 * year with January 1st 1, 0). <code>localtime</code> is <code>gmtime</code>:
 * there is no local time zone (<code>timezone</code> is 0 and
 * <code>tzname</code> is <code>("UTC", "UTC")</code>).</li>
 * <li><code>mktime(t)</code>: the inverse, as if t were UTC; the fields out of
 * range are carried over (month 13 is January of the next year).</li>
 * <li><code>strftime(fmt[, t])</code>, <code>asctime([t])</code>,
 * <code>ctime([secs])</code>: the formatting of the C locale, see
 * {@link #format}.</li>
 * </ul>
 * Not provided: <code>strptime</code>, <code>sleep</code>,
 * <code>monotonic</code>, <code>perf_counter</code>, <code>time_ns</code>
 * (64-bit integer), <code>struct_time</code> (the tuples are plain tuples).
 */
final class TimeLib {
	/** 9999-12-31 23:59:59: the largest timestamp accepted (and the year 1 for the smallest). */
	private static final long MAX_SECONDS = 253402300799L;
	private static final long MIN_SECONDS = -62135596800L;

	private static final String[] DAYS = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday",
			"Sunday" };
	private static final String[] MONTHS = { "January", "February", "March", "April", "May", "June", "July",
			"August", "September", "October", "November", "December" };

	private TimeLib() {
	}

	static PyModule create() {
		final PyModule m = new PyModule("time");
		m.put("time", (in, a) -> {
			count("time", a, 0, 0);
			return Double.valueOf(System.currentTimeMillis() / 1000.0);
		});
		m.put("gmtime", (in, a) -> {
			count("gmtime", a, 0, 1);
			return tuple(fields(a.length == 0 ? now() : seconds("gmtime", a[0])));
		});
		m.put("localtime", (in, a) -> {
			count("localtime", a, 0, 1);
			return tuple(fields(a.length == 0 ? now() : seconds("localtime", a[0])));
		});
		m.put("mktime", (in, a) -> {
			count("mktime", a, 1, 1);
			return Double.valueOf(mktime(items("mktime", a[0])));
		});
		m.put("strftime", (in, a) -> {
			count("strftime", a, 1, 2);
			if (a[0] instanceof String == false)
				throw PyOps.err(Types.TYPE_ERROR, "strftime() argument 1 must be str, not " + PyOps.typeName(a[0]));
			final long[] t = a.length == 2 ? check(items("strftime", a[1])) : fields(now());
			return format((String) a[0], t);
		});
		m.put("asctime", (in, a) -> {
			count("asctime", a, 0, 1);
			final long[] t = a.length == 1 ? check(items("asctime", a[0])) : fields(now());
			return format("%a %b %e %H:%M:%S %Y", t);
		});
		m.put("ctime", (in, a) -> {
			count("ctime", a, 0, 1);
			return format("%a %b %e %H:%M:%S %Y", fields(a.length == 0 ? now() : seconds("ctime", a[0])));
		});
		m.members.put("timezone", Integer.valueOf(0));
		m.members.put("altzone", Integer.valueOf(0));
		m.members.put("daylight", Integer.valueOf(0));
		m.members.put("tzname", new PyTuple(new Object[] { "UTC", "UTC" }));
		return m;
	}

	// ------------------------------------------------------------ arguments

	private static void count(String name, Object[] a, int min, int max) {
		if (a.length < min || a.length > max) {
			final String expected = min == max ? "exactly " + min : "at most " + max;
			throw PyOps.err(Types.TYPE_ERROR,
					name + "() takes " + expected + " argument" + (max == 1 ? "" : "s") + " (" + a.length + " given)");
		}
	}

	private static long now() {
		return Math.floorDiv(System.currentTimeMillis(), 1000L);
	}

	/** An int or a float of seconds, as the whole seconds before it (CPython rounds towards minus infinity). */
	private static long seconds(String name, Object o) {
		final long secs;
		if (o instanceof Integer)
			secs = ((Integer) o).longValue();
		else if (o instanceof Double) {
			final double d = ((Double) o).doubleValue();
			if (d != d)
				throw PyOps.err(Types.VALUE_ERROR, "Invalid value NaN (not a number)");
			if (d >= MAX_SECONDS + 1.0 || d < MIN_SECONDS)
				throw PyOps.err(Types.OVERFLOW, "timestamp out of range for platform time_t");
			secs = (long) Math.floor(d);
		} else
			throw PyOps.err(Types.TYPE_ERROR,
					"'" + PyOps.typeName(o) + "' object cannot be interpreted as an integer");
		if (secs > MAX_SECONDS || secs < MIN_SECONDS)
			throw PyOps.err(Types.OVERFLOW, "timestamp out of range for platform time_t");
		return secs;
	}

	/** The 9 integers of a time tuple. */
	private static long[] items(String name, Object o) {
		if (o instanceof PyTuple == false)
			throw PyOps.err(Types.TYPE_ERROR, "Tuple or struct_time argument required");
		final Object[] items = ((PyTuple) o).items;
		if (items.length != 9)
			throw PyOps.err(Types.TYPE_ERROR, name + "(): illegal time tuple argument");
		final long[] t = new long[9];
		for (int i = 0; i < 9; i++) {
			if (items[i] instanceof Integer == false)
				throw PyOps.err(Types.TYPE_ERROR, "'" + PyOps.typeName(items[i]) + "' object cannot be interpreted as an integer");
			t[i] = ((Integer) items[i]).longValue();
		}
		return t;
	}

	private static PyTuple tuple(long[] t) {
		final Object[] items = new Object[9];
		for (int i = 0; i < 9; i++)
			items[i] = Integer.valueOf((int) t[i]);
		return new PyTuple(items);
	}

	// ------------------------------------------------------------ calendar

	/** Days since 1970-01-01 of a day of the proleptic Gregorian calendar (month 1 to 12). */
	static long daysFromCivil(long y, int m, int d) {
		y -= m <= 2 ? 1 : 0;
		final long era = Math.floorDiv(y, 400L);
		final long yoe = y - era * 400;
		final long doy = (153 * (m + (m > 2 ? -3 : 9)) + 2) / 5 + d - 1;
		final long doe = yoe * 365 + yoe / 4 - yoe / 100 + doy;
		return era * 146097 + doe - 719468;
	}

	/** year, month (1 to 12), day of the month, of a number of days since 1970-01-01. */
	static long[] civilFromDays(long z) {
		z += 719468;
		final long era = Math.floorDiv(z, 146097L);
		final long doe = z - era * 146097;
		final long yoe = (doe - doe / 1460 + doe / 36524 - doe / 146096) / 365;
		final long doy = doe - (365 * yoe + yoe / 4 - yoe / 100);
		final long mp = (5 * doy + 2) / 153;
		final long d = doy - (153 * mp + 2) / 5 + 1;
		final long m = mp < 10 ? mp + 3 : mp - 9;
		final long y = yoe + era * 400 + (m <= 2 ? 1 : 0);
		return new long[] { y, m, d };
	}

	/** The 9 fields of a number of seconds since the epoch. */
	static long[] fields(long secs) {
		final long days = Math.floorDiv(secs, 86400L);
		final long rem = Math.floorMod(secs, 86400L);
		final long[] ymd = civilFromDays(days);
		final long wday = Math.floorMod(days + 3, 7L); // 1970-01-01 was a Thursday: Monday is 0
		final long yday = days - daysFromCivil(ymd[0], 1, 1) + 1;
		return new long[] { ymd[0], ymd[1], ymd[2], rem / 3600, rem % 3600 / 60, rem % 60, wday, yday, 0 };
	}

	private static double mktime(long[] t) {
		// the months are carried first, then everything else is plain arithmetic
		final long months = t[0] * 12 + (t[1] - 1);
		final long year = Math.floorDiv(months, 12L);
		final int month = (int) Math.floorMod(months, 12L) + 1;
		final long days = daysFromCivil(year, month, 1) + (t[2] - 1);
		final long secs = days * 86400 + t[3] * 3600 + t[4] * 60 + t[5];
		if (secs > MAX_SECONDS || secs < MIN_SECONDS)
			throw PyOps.err(Types.OVERFLOW, "mktime argument out of range");
		return secs;
	}

	// ------------------------------------------------------------ strftime

	/** The checks CPython makes before it formats a tuple. */
	private static long[] check(long[] t) {
		if (t[1] < 1 || t[1] > 12)
			throw PyOps.err(Types.VALUE_ERROR, "month out of range");
		if (t[2] < 1 || t[2] > 31)
			throw PyOps.err(Types.VALUE_ERROR, "day of month out of range");
		if (t[3] < 0 || t[3] > 23)
			throw PyOps.err(Types.VALUE_ERROR, "hour out of range");
		if (t[4] < 0 || t[4] > 59)
			throw PyOps.err(Types.VALUE_ERROR, "minute out of range");
		if (t[5] < 0 || t[5] > 61)
			throw PyOps.err(Types.VALUE_ERROR, "seconds out of range");
		if (t[6] < 0 || t[6] > 6)
			throw PyOps.err(Types.VALUE_ERROR, "day of week out of range");
		if (t[7] < 1 || t[7] > 366)
			throw PyOps.err(Types.VALUE_ERROR, "day of year out of range");
		return t;
	}

	/**
	 * strftime of the C locale, on the fields of the tuple as they are (the weekday
	 * and the day of the year are not recomputed, as in C). The directives are
	 * those of C99: %a %A %b %B %c %C %d %D %e %F %g %G %h %H %I %j %m %M %n %p
	 * %r %R %S %t %T %u %U %V %w %W %x %X %y %Y %z %Z %%; %Z is UTC and %z is
	 * +0000. %Y is not padded (as glibc does). Another directive is a ValueError.
	 */
	static String format(String fmt, long[] t) {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < fmt.length(); i++) {
			final char c = fmt.charAt(i);
			if (c != '%') {
				sb.append(c);
				continue;
			}
			if (++i >= fmt.length())
				throw PyOps.err(Types.VALUE_ERROR, "Invalid format string");
			directive(fmt.charAt(i), t, sb);
		}
		return sb.toString();
	}

	private static void directive(char d, long[] t, StringBuilder sb) {
		final int wdaySun = (int) ((t[6] + 1) % 7); // Sunday is 0
		final long yday0 = t[7] - 1;
		switch (d) {
		case 'a':
			sb.append(DAYS[(int) t[6]], 0, 3);
			break;
		case 'A':
			sb.append(DAYS[(int) t[6]]);
			break;
		case 'b':
		case 'h':
			sb.append(MONTHS[(int) t[1] - 1], 0, 3);
			break;
		case 'B':
			sb.append(MONTHS[(int) t[1] - 1]);
			break;
		case 'c':
			sb.append(format("%a %b %e %H:%M:%S %Y", t));
			break;
		case 'C':
			pad(sb, Math.floorDiv(t[0], 100L), 2, '0');
			break;
		case 'd':
			pad(sb, t[2], 2, '0');
			break;
		case 'D':
			sb.append(format("%m/%d/%y", t));
			break;
		case 'e':
			pad(sb, t[2], 2, ' ');
			break;
		case 'F':
			sb.append(format("%Y-%m-%d", t));
			break;
		case 'g':
			pad(sb, Math.floorMod(isoWeek(t)[0], 100L), 2, '0');
			break;
		case 'G':
			sb.append(isoWeek(t)[0]);
			break;
		case 'H':
			pad(sb, t[3], 2, '0');
			break;
		case 'I':
			pad(sb, t[3] % 12 == 0 ? 12 : t[3] % 12, 2, '0');
			break;
		case 'j':
			pad(sb, t[7], 3, '0');
			break;
		case 'm':
			pad(sb, t[1], 2, '0');
			break;
		case 'M':
			pad(sb, t[4], 2, '0');
			break;
		case 'n':
			sb.append('\n');
			break;
		case 'p':
			sb.append(t[3] < 12 ? "AM" : "PM");
			break;
		case 'r':
			sb.append(format("%I:%M:%S %p", t));
			break;
		case 'R':
			sb.append(format("%H:%M", t));
			break;
		case 'S':
			pad(sb, t[5], 2, '0');
			break;
		case 't':
			sb.append('\t');
			break;
		case 'T':
		case 'X':
			sb.append(format("%H:%M:%S", t));
			break;
		case 'u':
			sb.append(t[6] + 1);
			break;
		case 'U':
			pad(sb, (yday0 + 7 - wdaySun) / 7, 2, '0');
			break;
		case 'V':
			pad(sb, isoWeek(t)[1], 2, '0');
			break;
		case 'w':
			sb.append(wdaySun);
			break;
		case 'W':
			pad(sb, (yday0 + 7 - (wdaySun + 6) % 7) / 7, 2, '0');
			break;
		case 'x':
			sb.append(format("%m/%d/%y", t));
			break;
		case 'y':
			pad(sb, Math.floorMod(t[0], 100L), 2, '0');
			break;
		case 'Y':
			sb.append(t[0]);
			break;
		case 'z':
			sb.append("+0000");
			break;
		case 'Z':
			sb.append("UTC");
			break;
		case '%':
			sb.append('%');
			break;
		default:
			throw PyOps.err(Types.VALUE_ERROR, "Invalid format directive %" + d);
		}
	}

	private static void pad(StringBuilder sb, long value, int width, char fill) {
		final String s = Long.toString(value);
		for (int i = s.length(); i < width; i++)
			sb.append(fill);
		sb.append(s);
	}

	/** The year and the number of the ISO 8601 week of the day. */
	private static long[] isoWeek(long[] t) {
		final long year = t[0];
		long week = (t[7] - (t[6] + 1) + 10) / 7;
		long isoYear = year;
		if (week < 1) {
			isoYear = year - 1;
			week = weeksInYear(isoYear);
		} else if (week > weeksInYear(year)) {
			isoYear = year + 1;
			week = 1;
		}
		return new long[] { isoYear, week };
	}

	private static long weeksInYear(long y) {
		return p(y) == 4 || p(y - 1) == 3 ? 53 : 52;
	}

	private static long p(long y) {
		return Math.floorMod(y + Math.floorDiv(y, 4L) - Math.floorDiv(y, 100L) + Math.floorDiv(y, 400L), 7L);
	}
}
