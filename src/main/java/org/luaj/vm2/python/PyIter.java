package org.luaj.vm2.python;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

/** An iterator over a builtin value. {@link #next} returns Java null when it is exhausted. */
abstract class PyIter {
	abstract Object next();

	static final class ListIter extends PyIter {
		private final PyList list;
		private int i;

		ListIter(PyList list) {
			this.list = list;
		}

		@Override
		Object next() {
			return i < list.items.size() ? list.items.get(i++) : null;
		}
	}

	static final class ArrayIter extends PyIter {
		private final Object[] a;
		private int i;

		ArrayIter(Object[] a) {
			this.a = a;
		}

		@Override
		Object next() {
			return i < a.length ? a[i++] : null;
		}
	}

	static final class StrIter extends PyIter {
		private final String s;
		private int i;

		StrIter(String s) {
			this.s = s;
		}

		@Override
		Object next() {
			if (i >= s.length())
				return null;
			int cp = s.codePointAt(i);
			int n = Character.charCount(cp);
			String r = s.substring(i, i + n);
			i += n;
			return r;
		}
	}

	static final class RangeIter extends PyIter {
		private long cur;
		private final long stop;
		private final long step;

		RangeIter(PyRange r) {
			cur = r.start;
			stop = r.stop;
			step = r.step;
		}

		@Override
		Object next() {
			if (step > 0 ? cur >= stop : cur <= stop)
				return null;
			Object v = Integer.valueOf((int) cur);
			cur += step;
			return v;
		}
	}

	static final class DictIter extends PyIter {
		private final PyDict d;
		private final Iterator<PyDict.Entry> it;
		private final int size;

		DictIter(PyDict d) {
			this.d = d;
			this.it = d.map.values().iterator();
			this.size = d.size();
		}

		@Override
		Object next() {
			try {
				if (d.size() != size)
					throw PyOps.err(Types.RUNTIME_ERROR, "dictionary changed size during iteration");
				return it.hasNext() ? it.next().key : null;
			} catch (ConcurrentModificationException e) {
				throw PyOps.err(Types.RUNTIME_ERROR, "dictionary changed size during iteration");
			}
		}
	}
}
