package org.luaj.vm2.python;

/** An exception instance, as seen by the program. */
final class PyException {
	final PyType type;
	final Object[] args;

	PyException(PyType type, Object[] args) {
		this.type = type;
		this.args = args;
	}
}
