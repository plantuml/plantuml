package org.luaj.vm2.python;

import java.util.LinkedHashMap;

/**
 * A Python dict, in insertion order. Keys are normalised before they reach the
 * map (True is 1, 2.0 is 2, tuples are normalised inside), and each entry
 * remembers the key as it was first given.
 */
final class PyDict {
	static final class Entry {
		final Object key;
		Object value;

		Entry(Object key, Object value) {
			this.key = key;
			this.value = value;
		}
	}

	final LinkedHashMap<Object, Entry> map = new LinkedHashMap<Object, Entry>();

	static Object norm(Object k) {
		if (k instanceof String || k instanceof Integer || k == PyNone.NONE)
			return k;
		if (k instanceof Boolean)
			return Integer.valueOf(((Boolean) k).booleanValue() ? 1 : 0);
		if (k instanceof Double) {
			double d = ((Double) k).doubleValue();
			if (d == Math.rint(d) && d >= Integer.MIN_VALUE && d <= Integer.MAX_VALUE)
				return Integer.valueOf((int) d);
			return k;
		}
		if (k instanceof PyTuple) {
			Object[] items = ((PyTuple) k).items;
			Object[] out = null;
			for (int i = 0; i < items.length; i++) {
				Object n = norm(items[i]);
				if (n != items[i]) {
					if (out == null)
						out = items.clone();
					out[i] = n;
				}
			}
			return out == null ? k : new PyTuple(out);
		}
		if (k instanceof PyList || k instanceof PyDict)
			throw PyOps.err(Types.TYPE_ERROR, "unhashable type: '" + PyOps.typeName(k) + "'");
		return k;
	}

	int size() {
		return map.size();
	}

	Object get(Object key) {
		Entry e = map.get(norm(key));
		return e == null ? null : e.value;
	}

	void put(Object key, Object value) {
		Object n = norm(key);
		Entry e = map.get(n);
		if (e == null)
			map.put(n, new Entry(key, value));
		else
			e.value = value;
	}

	Object remove(Object key) {
		Entry e = map.remove(norm(key));
		return e == null ? null : e.value;
	}

	boolean containsKey(Object key) {
		return map.containsKey(norm(key));
	}
}
