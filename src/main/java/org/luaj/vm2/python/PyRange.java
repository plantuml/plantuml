package org.luaj.vm2.python;

/** range(start, stop, step), all 32-bit. */
final class PyRange {
	final int start, stop, step;

	PyRange(int start, int stop, int step) {
		this.start = start;
		this.stop = stop;
		this.step = step;
	}

	int length() {
		long n;
		if (step > 0)
			n = stop > start ? ((long) stop - start + step - 1) / step : 0;
		else
			n = start > stop ? ((long) start - stop + (-(long) step) - 1) / (-(long) step) : 0;
		return (int) n;
	}

	int get(int i) {
		return (int) ((long) start + (long) i * step);
	}
}
