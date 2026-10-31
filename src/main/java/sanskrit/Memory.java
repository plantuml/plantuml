package sanskrit;

import java.util.ArrayDeque;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * The estimate of the memory held by the values of a program, in units (about
 * a byte): one per character of a str, {@link #SLOT} per item of a list or a
 * tuple, {@link #ENTRY} per entry of a dict, {@link #ATTR} per attribute of an
 * object, and {@link #OBJECT} for the value itself. The numbers are not
 * counted: they are in the slots.
 * <p>
 * MAX_LIST and MAX_STRING bound one value; this bounds their sum. The
 * interpreter counts what the program creates ({@link Interp#allocated}); when
 * the count passes {@link #MAX}, it measures what the program still holds
 * ({@link #live}): above {@link #MAX_LIVE} it raises MemoryError, else it counts
 * again from there. So the values of a program never hold more than MAX units,
 * and a program that holds less than MAX_LIVE is never refused.
 */
final class Memory {
	private Memory() {
	}

	static final long MAX = 1L << 28;
	/** A list of MAX_LIST items is half of MAX: the program may hold one, and more. */
	static final long MAX_LIVE = MAX / 8 * 5;
	static final int SLOT = 8;
	static final int ENTRY = 128;
	static final int ATTR = 64;
	static final int OBJECT = 40;
	/** A str shorter than this is counted each time it is met: looking for the ones already seen costs more. */
	private static final int SHARED = 16;

	/** The units of one value, without the values it holds. */
	static long weight(Object v) {
		if (v instanceof String)
			return OBJECT + ((String) v).length();
		if (v instanceof PyStr)
			return OBJECT + ((PyStr) v).s.length();
		if (v instanceof PyList)
			return OBJECT + (long) SLOT * ((PyList) v).items.size();
		if (v instanceof PyTuple)
			return OBJECT + (long) SLOT * ((PyTuple) v).items.length;
		if (v instanceof PyDict)
			return OBJECT + (long) ENTRY * ((PyDict) v).size();
		if (v instanceof PySet)
			return OBJECT + (long) ENTRY * ((PySet) v).size();
		return 0;
	}

	/**
	 * For a builtin that builds a list of new values (split, zip, json.loads...):
	 * MemoryError if what it has built so far is above what a program may hold.
	 * The values a builtin is building are not seen by {@link #live}.
	 */
	static void checkBuilt(long units) {
		if (units > MAX_LIVE)
			throw PyOps.err(Types.MEMORY_ERROR, "out of memory");
	}

	/** {@link #checkBuilt} for a list of {@code count} strs cut from a text of {@code length} characters. */
	static void checkParts(long count, long length) {
		PyOps.checkList(count);
		checkBuilt(count * (OBJECT + SLOT) + length);
	}

	/** What is being measured: the units so far, the values still to visit and the ones seen. */
	private long total;
	private long visited;
	private final ArrayDeque<Object> todo = new ArrayDeque<Object>();
	private final IdentityHashMap<Object, Boolean> seen = new IdentityHashMap<Object, Boolean>();

	/** The units held by what the program can reach: its globals, its modules and the frames of its calls. */
	static long live(Interp in) {
		final Memory m = new Memory();
		m.map(in.globals);
		for (PyModule module : in.modules.values())
			m.todo.push(module);
		for (Object[] frame : in.frames)
			m.array(frame);
		for (PyException e : in.handled)
			m.todo.push(e);
		while (!m.todo.isEmpty())
			m.visit(m.todo.pop());
		in.work(m.visited);
		return m.total;
	}

	private void array(Object[] items) {
		visited += items.length;
		for (Object o : items)
			if (o != null && !(o instanceof Integer))
				todo.push(o);
	}

	private void map(Map<String, Object> map) {
		if (map == null || seen.put(map, Boolean.TRUE) != null)
			return;
		total += (long) ATTR * map.size();
		visited += map.size();
		for (Object o : map.values())
			todo.push(o);
	}

	private void visit(Object o) {
		if (o instanceof Integer || o instanceof Double || o instanceof Boolean || o == PyNone.NONE)
			return;
		if (o instanceof PyStr)
			o = ((PyStr) o).s;
		if (o instanceof String) {
			final int n = ((String) o).length();
			if (n < SHARED || seen.put(o, Boolean.TRUE) == null)
				total += OBJECT + n;
			return;
		}
		if (seen.put(o, Boolean.TRUE) != null)
			return;
		total += weight(o);
		if (o instanceof PyList) {
			array(((PyList) o).items.toArray());
		} else if (o instanceof PyTuple) {
			array(((PyTuple) o).items);
		} else if (o instanceof PyDict) {
			visited += ((PyDict) o).size();
			for (PyDict.Entry e : ((PyDict) o).map.values()) {
				todo.push(e.key);
				todo.push(e.value);
			}
		} else if (o instanceof PySet) {
			visited += ((PySet) o).size();
			for (Object item : ((PySet) o).map.values())
				todo.push(item);
		} else if (o instanceof PyInstance) {
			total += ATTR;
			map(((PyInstance) o).attrs);
			todo.push(((PyInstance) o).type);
		} else if (o instanceof PyException) {
			array(((PyException) o).args);
			map(((PyException) o).attrs);
		} else if (o instanceof PyFunction) {
			final PyFunction fn = (PyFunction) o;
			map(fn.globals);
			map(fn.kwdefaults);
			if (fn.defaults != null)
				array(fn.defaults);
			if (fn.closure != null)
				for (Cell c : fn.closure)
					todo.push(c);
		} else if (o instanceof Cell) {
			if (((Cell) o).value != null)
				todo.push(((Cell) o).value);
		} else if (o instanceof BoundMethod) {
			todo.push(((BoundMethod) o).self);
			if (((BoundMethod) o).function != null)
				todo.push(((BoundMethod) o).function);
		} else if (o instanceof PyType) {
			map(((PyType) o).dict);
			if (((PyType) o).parent != null)
				todo.push(((PyType) o).parent);
		} else if (o instanceof PyModule) {
			map(((PyModule) o).members);
		} else if (o instanceof PyView) {
			todo.push(((PyView) o).dict);
		}
	}
}
