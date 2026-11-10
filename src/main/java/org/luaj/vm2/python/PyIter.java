package org.luaj.vm2.python;

import java.util.Iterator;

/** An iterator over a builtin value. {@link #next} returns Java null when it is exhausted. */
interface PyIter {
	Object next();

	final class ListIter implements PyIter {
		private final PyList list;
		private int i;

		ListIter(PyList list) {
			this.list = list;
		}

		@Override
		public Object next() {
			return i < list.items.size() ? list.items.get(i++) : null;
		}
	}

	final class ArrayIter implements PyIter {
		private final Object[] a;
		private int i;

		ArrayIter(Object[] a) {
			this.a = a;
		}

		@Override
		public Object next() {
			return i < a.length ? a[i++] : null;
		}
	}

	final class StrIter implements PyIter {
		private final String s;
		private int i;

		StrIter(String s) {
			this.s = s;
		}

		@Override
		public Object next() {
			if (i >= s.length())
				return null;
			int cp = s.codePointAt(i);
			int n = Character.charCount(cp);
			String r = s.substring(i, i + n);
			i += n;
			return r;
		}
	}

	final class RangeIter implements PyIter {
		private long cur;
		private final long stop;
		private final long step;

		RangeIter(PyRange r) {
			cur = r.start;
			stop = r.stop;
			step = r.step;
		}

		@Override
		public Object next() {
			if (step > 0 ? cur >= stop : cur <= stop)
				return null;
			Object v = Integer.valueOf((int) cur);
			cur += step;
			return v;
		}
	}

	final class DictIter implements PyIter {
		private final PyDict d;
		private final Iterator<PyDict.Entry> it;
		private final int size;
		private final int modCount;

		DictIter(PyDict d) {
			this.d = d;
			this.it = d.map.values().iterator();
			this.size = d.size();
			this.modCount = d.modCount;
		}

		@Override
		public Object next() {
			if (d.size() != size)
				throw PyOps.err(Types.RUNTIME_ERROR, "dictionary changed size during iteration");
			if (it.hasNext() == false)
				return null;
			if (d.modCount != modCount)
				throw PyOps.err(Types.RUNTIME_ERROR, "dictionary changed size during iteration");
			return it.next().key;
		}
	}
}
