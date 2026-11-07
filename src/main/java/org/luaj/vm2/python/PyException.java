package org.luaj.vm2.python;

import java.util.HashMap;
import java.util.Map;

/** An exception instance, as seen by the program. */
final class PyException {
	final PyType type;
	/** Set again by BaseException.__init__, which a class may call explicitly. */
	Object[] args;
	/** Attributes set by the program, e.g. self.code = 3 in an __init__; created on first use. */
	Map<String, Object> attrs;

	PyException(PyType type, Object[] args) {
		this.type = type;
		this.args = args;
	}

	Map<String, Object> attrs() {
		if (attrs == null)
			attrs = new HashMap<String, Object>();
		return attrs;
	}
}
