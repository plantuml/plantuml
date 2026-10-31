package org.luaj.vm2.python;

/** A type that is a value: int, str, ... and the exception classes. */
final class PyType {
	final String name;
	final PyType parent;
	final boolean exception;
	/** Called when the type itself is called, e.g. int("3"). */
	Builtin ctor;

	PyType(String name, PyType parent, boolean exception) {
		this.name = name;
		this.parent = parent;
		this.exception = exception;
	}

	boolean isSubtypeOf(PyType t) {
		for (PyType p = this; p != null; p = p.parent)
			if (p == t)
				return true;
		return false;
	}
}
