package sanskrit;

/** range(start, stop, step), all 32-bit. */
final class PyRange {
	final int start, stop, step;

	PyRange(int start, int stop, int step) {
		this.start = start;
		this.stop = stop;
		this.step = step;
	}

	/** The length as an int: OverflowError if it does not fit (CPython has no such limit). */
	int length() {
		final long n = longLength();
		if (n > Integer.MAX_VALUE)
			throw PyInt.overflow();
		return (int) n;
	}

	long longLength() {
		long n;
		if (step > 0)
			n = stop > start ? ((long) stop - start + step - 1) / step : 0;
		else
			n = start > stop ? ((long) start - stop + (-(long) step) - 1) / (-(long) step) : 0;
		return n;
	}

	int get(long i) {
		return (int) ((long) start + (long) i * step);
	}
}
