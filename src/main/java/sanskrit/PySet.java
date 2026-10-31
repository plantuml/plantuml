package sanskrit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A Python set. Its items are normalised as the keys of a {@link PyDict} (True
 * is 1, 2.0 is 2...), and each one is kept as it was first given. It iterates
 * in insertion order, where CPython follows the hashes of the items: the two
 * orders differ, and a program that must give the same output on both sorts
 * the items first.
 */
final class PySet {
	/** The normalised item, and the item as it was first given. */
	final LinkedHashMap<Object, Object> map = new LinkedHashMap<Object, Object>();

	/**
	 * Incremented each time an item is added or removed: an iterator that sees
	 * another value than the one it started with knows the set changed under it.
	 */
	int modCount;

	int size() {
		return map.size();
	}

	void add(Object item) {
		Object n = PyDict.norm(item);
		if (!map.containsKey(n)) {
			map.put(n, item);
			modCount++;
		}
	}

	/** Removes an item; false if it is absent. */
	boolean remove(Object item) {
		if (map.remove(PyDict.norm(item)) == null)
			return false;
		modCount++;
		return true;
	}

	boolean contains(Object item) {
		return map.containsKey(PyDict.norm(item));
	}

	void clear() {
		if (map.isEmpty())
			return;
		map.clear();
		modCount++;
	}

	/** The items, in a new list. */
	ArrayList<Object> items() {
		return new ArrayList<Object>(map.values());
	}

	PySet copy() {
		PySet r = new PySet();
		r.map.putAll(map);
		return r;
	}

	/** The iterator of a set: it may not change size while it is iterated. */
	static final class SetIter implements PyIter {
		private final PySet s;
		private final Iterator<Object> it;
		private final int size;
		private final int modCount;

		SetIter(PySet s) {
			this.s = s;
			this.it = s.map.values().iterator();
			this.size = s.size();
			this.modCount = s.modCount;
		}

		@Override
		public Object next() {
			if (s.size() != size)
				throw PyOps.err(Types.RUNTIME_ERROR, "Set changed size during iteration");
			if (it.hasNext() == false)
				return null;
			if (s.modCount != modCount)
				throw PyOps.err(Types.RUNTIME_ERROR, "Set changed size during iteration");
			return it.next();
		}
	}

	// ------------------------------------------------------------ operations

	/** True if every item of x is in y. */
	static boolean isSubset(PySet x, PySet y) {
		if (x.size() > y.size())
			return false;
		for (Object n : x.map.keySet())
			if (!y.map.containsKey(n))
				return false;
		return true;
	}

	/** The equality of two sets of normalised keys: those of a set, or of dict.keys(). */
	static boolean keysEqual(java.util.Set<Object> x, java.util.Set<Object> y) {
		if (x.size() != y.size())
			return false;
		for (Object n : x)
			if (!y.contains(n))
				return false;
		return true;
	}

	/**
	 * a | b, a & b, a - b and a ^ b: a new set, the items of a first, in their
	 * order; null for the other operators.
	 */
	static PySet binary(int op, PySet a, PySet b) {
		PySet r = new PySet();
		switch (op) {
		case Op.BIN_OR:
			r = a.copy();
			for (Map.Entry<Object, Object> e : b.map.entrySet())
				r.map.putIfAbsent(e.getKey(), e.getValue());
			return r;
		case Op.BIN_AND:
			for (Map.Entry<Object, Object> e : a.map.entrySet())
				if (b.map.containsKey(e.getKey()))
					r.map.put(e.getKey(), e.getValue());
			return r;
		case Op.BIN_SUB:
			for (Map.Entry<Object, Object> e : a.map.entrySet())
				if (!b.map.containsKey(e.getKey()))
					r.map.put(e.getKey(), e.getValue());
			return r;
		case Op.BIN_XOR:
			for (Map.Entry<Object, Object> e : a.map.entrySet())
				if (!b.map.containsKey(e.getKey()))
					r.map.put(e.getKey(), e.getValue());
			for (Map.Entry<Object, Object> e : b.map.entrySet())
				if (!a.map.containsKey(e.getKey()))
					r.map.put(e.getKey(), e.getValue());
			return r;
		default:
			return null;
		}
	}

	/**
	 * s |= o, s &= o, s -= o and s ^= o: s is changed; false for the other
	 * operators. o may be s itself.
	 */
	static boolean inplace(int op, PySet s, PySet o) {
		switch (op) {
		case Op.BIN_OR:
			s.update(o.items());
			return true;
		case Op.BIN_AND:
			s.keep(o, true);
			return true;
		case Op.BIN_SUB:
			if (s == o)
				s.clear();
			else
				for (Object item : o.items())
					s.remove(item);
			return true;
		case Op.BIN_XOR:
			if (s == o)
				s.clear();
			else
				s.symmetricUpdate(o.items());
			return true;
		default:
			return false;
		}
	}

	void update(Iterable<Object> items) {
		for (Object item : items)
			add(item);
	}

	/** Removes the items that are not in o (inside true) or that are in o (inside false). */
	void keep(PySet o, boolean inside) {
		if (map.keySet().removeIf(n -> o.map.containsKey(n) != inside))
			modCount++;
	}

	void symmetricUpdate(ArrayList<Object> items) {
		// the items of the argument are made a set first: one that is there twice
		// is still added or removed once
		PySet o = new PySet();
		o.update(items);
		for (Object item : o.map.values())
			if (!remove(item))
				add(item);
	}

	/** An argument of a method of set: a set, or the items of an iterable made a set. */
	static PySet asSet(Interp in, Object o) {
		if (o instanceof PySet)
			return (PySet) o;
		ArrayList<Object> items = PyOps.toArrayList(o);
		in.work(items.size());
		Memory.checkBuilt(Memory.OBJECT + (long) Memory.ENTRY * items.size());
		in.allocated(Memory.OBJECT + (long) Memory.ENTRY * items.size());
		PySet r = new PySet();
		r.update(items);
		return r;
	}

	/** s.update(o): the items of an iterable added to s, with their work and their memory. */
	static void addItems(Interp in, PySet s, Object o) {
		// src may be s
		ArrayList<Object> items = o instanceof PySet ? ((PySet) o).items() : PyOps.toArrayList(o);
		// counted before the items are added: at most one entry for each; the
		// set being built may not be seen by the measure of what is held yet
		in.work(items.size());
		Memory.checkBuilt((long) Memory.ENTRY * items.size());
		in.allocated((long) Memory.ENTRY * items.size());
		s.update(items);
	}

	/** set() and set(iterable). */
	static PySet make(Interp in, Object[] a) {
		Builtins.count("set", a, 0, 1);
		PySet s = new PySet();
		if (a.length == 1) {
			in.allocated(Memory.OBJECT);
			addItems(in, s, a[0]);
		}
		return s;
	}

	// ------------------------------------------------------------ methods

	static Map<String, Builtin> methods() {
		final Map<String, Builtin> t = new HashMap<String, Builtin>();
		def(t, "add", (in, a) -> {
			Methods.need(a, 1, 1, "add");
			PySet s = (PySet) a[0];
			if (!s.contains(a[1])) {
				in.allocated(Memory.ENTRY);
				s.add(a[1]);
			}
			return PyNone.NONE;
		});
		def(t, "remove", (in, a) -> {
			Methods.need(a, 1, 1, "remove");
			if (!((PySet) a[0]).remove(a[1]))
				throw new PyError(new PyException(Types.KEY_ERROR, new Object[] { a[1] }));
			return PyNone.NONE;
		});
		def(t, "discard", (in, a) -> {
			Methods.need(a, 1, 1, "discard");
			((PySet) a[0]).remove(a[1]);
			return PyNone.NONE;
		});
		def(t, "pop", (in, a) -> {
			Methods.need(a, 0, 0, "pop");
			PySet s = (PySet) a[0];
			if (s.size() == 0)
				throw new PyError(new PyException(Types.KEY_ERROR, new Object[] { "pop from an empty set" }));
			Iterator<Object> it = s.map.values().iterator();
			Object first = it.next();
			it.remove();
			s.modCount++;
			return first;
		});
		def(t, "clear", (in, a) -> {
			Methods.need(a, 0, 0, "clear");
			((PySet) a[0]).clear();
			return PyNone.NONE;
		});
		def(t, "copy", (in, a) -> {
			Methods.need(a, 0, 0, "copy");
			return in.made(((PySet) a[0]).copy());
		});
		def(t, "update", (in, a) -> {
			for (int i = 1; i < a.length; i++)
				addItems(in, (PySet) a[0], a[i]);
			return PyNone.NONE;
		});
		def(t, "union", (in, a) -> {
			PySet r = in.made(((PySet) a[0]).copy());
			for (int i = 1; i < a.length; i++)
				addItems(in, r, a[i]);
			return r;
		});
		// intersection, difference and their _update: the set keeps the items that
		// are in every argument, or in none of them
		def(t, "intersection", (in, a) -> {
			PySet r = in.made(((PySet) a[0]).copy());
			keep(in, r, a, true);
			return r;
		});
		def(t, "intersection_update", (in, a) -> {
			keep(in, (PySet) a[0], a, true);
			return PyNone.NONE;
		});
		def(t, "difference", (in, a) -> {
			PySet r = in.made(((PySet) a[0]).copy());
			keep(in, r, a, false);
			return r;
		});
		def(t, "difference_update", (in, a) -> {
			PySet s = (PySet) a[0];
			for (int i = 1; i < a.length; i++)
				if (a[i] == s)
					s.clear();
			keep(in, s, a, false);
			return PyNone.NONE;
		});
		def(t, "symmetric_difference", (in, a) -> {
			Methods.need(a, 1, 1, "symmetric_difference");
			PySet s = (PySet) a[0];
			PySet o = asSet(in, a[1]);
			in.work(s.size() + o.size());
			return in.made(binary(Op.BIN_XOR, s, o));
		});
		def(t, "symmetric_difference_update", (in, a) -> {
			Methods.need(a, 1, 1, "symmetric_difference_update");
			PySet s = (PySet) a[0];
			if (a[1] == s) {
				s.clear();
				return PyNone.NONE;
			}
			ArrayList<Object> items = PyOps.toArrayList(a[1]);
			in.work(items.size());
			Memory.checkBuilt((long) Memory.ENTRY * items.size());
			in.allocated((long) Memory.ENTRY * items.size());
			s.symmetricUpdate(items);
			return PyNone.NONE;
		});
		def(t, "issubset", (in, a) -> {
			Methods.need(a, 1, 1, "issubset");
			in.work(((PySet) a[0]).size());
			return Boolean.valueOf(isSubset((PySet) a[0], asSet(in, a[1])));
		});
		def(t, "issuperset", (in, a) -> {
			Methods.need(a, 1, 1, "issuperset");
			PySet o = asSet(in, a[1]);
			in.work(o.size());
			return Boolean.valueOf(isSubset(o, (PySet) a[0]));
		});
		def(t, "isdisjoint", (in, a) -> {
			Methods.need(a, 1, 1, "isdisjoint");
			PySet s = (PySet) a[0];
			ArrayList<Object> items = PyOps.toArrayList(a[1]);
			in.work(items.size());
			for (Object item : items)
				if (s.contains(item))
					return Boolean.FALSE;
			return Boolean.TRUE;
		});
		return t;
	}

	private static void keep(Interp in, PySet s, Object[] a, boolean inside) {
		for (int i = 1; i < a.length; i++) {
			in.work(s.size());
			s.keep(asSet(in, a[i]), inside);
		}
	}

	private static void def(Map<String, Builtin> table, String name, Builtin.Fn fn) {
		table.put(name, new Builtin(name, fn));
	}
}
