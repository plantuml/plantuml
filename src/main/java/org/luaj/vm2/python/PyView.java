package org.luaj.vm2.python;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

/** dict.keys(), dict.values() or dict.items(): a live view of the dict. */
final class PyView {
	static final int KEYS = 0;
	static final int VALUES = 1;
	static final int ITEMS = 2;

	final PyDict dict;
	final int kind;

	PyView(PyDict dict, int kind) {
		this.dict = dict;
		this.kind = kind;
	}

	PyType type() {
		return kind == KEYS ? Types.DICT_KEYS : kind == VALUES ? Types.DICT_VALUES : Types.DICT_ITEMS;
	}

	PyIter iterator() {
		final Iterator<PyDict.Entry> it = dict.map.values().iterator();
		final int size = dict.size();
		return new PyIter() {
			@Override
			Object next() {
				try {
					if (dict.size() != size)
						throw PyOps.err(Types.RUNTIME_ERROR, "dictionary changed size during iteration");
					if (!it.hasNext())
						return null;
					PyDict.Entry e = it.next();
					return kind == KEYS ? e.key : kind == VALUES ? e.value : new PyTuple(new Object[] { e.key, e.value });
				} catch (ConcurrentModificationException ex) {
					throw PyOps.err(Types.RUNTIME_ERROR, "dictionary changed size during iteration");
				}
			}
		};
	}
}
