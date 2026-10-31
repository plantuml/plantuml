package sanskrit;

import java.util.LinkedHashMap;
import java.util.Map;

/** An exception instance, as seen by the program. */
final class PyException {
	final PyType type;
	/** Set again by BaseException.__init__, which a class may call explicitly. */
	Object[] args;
	/** Attributes set by the program, e.g. self.code = 3 in an __init__; created on first use. */
	Map<String, Object> attrs;
	/**
	 * The error that carried this exception to the handler that caught it, with the frames it went through:
	 * the traceback of the exception, which a raise of the same exception continues. Null until caught.
	 */
	PyError traceback;

	PyException(PyType type, Object[] args) {
		this.type = type;
		this.args = args;
	}

	Map<String, Object> attrs() {
		if (attrs == null)
			attrs = new LinkedHashMap<String, Object>();
		return attrs;
	}
}
