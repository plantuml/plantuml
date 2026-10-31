package org.luaj.vm2.python;

import java.util.ArrayList;

/** A Python list. Not hashable; equality is {@link PyOps#eq}, not {@code equals}. */
final class PyList {
	final ArrayList<Object> items;

	PyList() {
		items = new ArrayList<Object>();
	}

	PyList(ArrayList<Object> items) {
		this.items = items;
	}

	PyList(Object[] a) {
		items = new ArrayList<Object>(a.length);
		for (Object o : a)
			items.add(o);
	}
}
