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

	/**
	 * Incremented each time a key is added or removed: an iterator that sees
	 * another value than the one it started with knows the dict changed under it.
	 */
	int modCount;

	static Object norm(Object k) {
		return norm(k, 0);
	}

	/** The deepest tuple accepted as a key: the map hashes and compares it recursively. */
	private static final int MAX_KEY_DEPTH = 1000;

	/**
	 * Tuples are normalised inside, and then hashed and compared recursively by
	 * the map: the depth of a key is limited, so that neither recursion can
	 * overflow the stack.
	 */
	private static Object norm(Object k, int depth) {
		if (k instanceof String || k instanceof Integer || k == PyNone.NONE)
			return k;
		if (k instanceof Boolean)
			return Integer.valueOf(((Boolean) k).booleanValue() ? 1 : 0);
		if (k instanceof Double) {
			double d = ((Double) k).doubleValue();
			if (PyInt.isWholeInt(d))
				return Integer.valueOf((int) d);
			return k;
		}
		if (k instanceof PyTuple) {
			if (depth > MAX_KEY_DEPTH)
				throw PyOps.err(Types.RECURSION_ERROR, "maximum recursion depth exceeded");
			Object[] items = ((PyTuple) k).items;
			Object[] out = null;
			for (int i = 0; i < items.length; i++) {
				Object n = norm(items[i], depth + 1);
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
		if (PyOps.isNative(k) && ((PyInstance) k).type.lookup("__hash__") != null)
			return new PyHashKey((PyInstance) k);
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
		if (e == null) {
			map.put(n, new Entry(key, value));
			modCount++;
		} else
			e.value = value;
	}

	Object remove(Object key) {
		Entry e = map.remove(norm(key));
		if (e == null)
			return null;
		modCount++;
		return e.value;
	}

	void clear() {
		if (map.isEmpty())
			return;
		map.clear();
		modCount++;
	}

	boolean containsKey(Object key) {
		return map.containsKey(norm(key));
	}
}
