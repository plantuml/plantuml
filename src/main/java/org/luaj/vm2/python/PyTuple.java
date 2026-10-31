package org.luaj.vm2.python;

import java.util.Arrays;

/** An immutable sequence; equality and hash code are structural. */
final class PyTuple {
	static final PyTuple EMPTY = new PyTuple(new Object[0]);

	final Object[] items;

	PyTuple(Object[] items) {
		this.items = items;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof PyTuple && Arrays.equals(items, ((PyTuple) o).items);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(items);
	}
}
