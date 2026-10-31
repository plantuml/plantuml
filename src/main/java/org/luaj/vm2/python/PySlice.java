package org.luaj.vm2.python;

/** slice(lower, upper, step); each part is an Integer or None. */
final class PySlice {
	final Object lower, upper, step;

	PySlice(Object lower, Object upper, Object step) {
		this.lower = lower;
		this.upper = upper;
		this.step = step;
	}
}
