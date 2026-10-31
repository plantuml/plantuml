package sanskrit;

import java.util.LinkedHashMap;
import java.util.Map;

/** An instance of a class defined by the program (not of an exception class: those are PyException). */
final class PyInstance {
	final PyType type;
	/** In insertion order, as CPython's __dict__, so that a future vars() is the same on every edition. */
	final Map<String, Object> attrs = new LinkedHashMap<String, Object>();

	PyInstance(PyType type) {
		this.type = type;
	}
}
