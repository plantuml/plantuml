package org.luaj.vm2.python;

import java.util.HashMap;
import java.util.Map;

/** An instance of a class defined by the program (not of an exception class: those are PyException). */
final class PyInstance {
	final PyType type;
	final Map<String, Object> attrs = new HashMap<String, Object>();

	PyInstance(PyType type) {
		this.type = type;
	}
}
