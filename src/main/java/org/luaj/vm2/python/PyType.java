package org.luaj.vm2.python;

import java.util.Map;

/** A type that is a value: int, str, ..., the exception classes and the classes defined by the program. */
final class PyType {
	final String name;
	final PyType parent;
	final boolean exception;
	/** Called when the type itself is called, e.g. int("3"). */
	Builtin ctor;
	/** The attributes of a class defined by the program; null for a builtin type. */
	final Map<String, Object> dict;
	/** CPython's __qualname__ of a class defined by the program; null for a builtin type. */
	final String qualname;

	PyType(String name, PyType parent, boolean exception) {
		this(name, parent, exception, null, null);
	}

	PyType(String name, PyType parent, boolean exception, Map<String, Object> dict, String qualname) {
		this.name = name;
		this.parent = parent;
		this.exception = exception;
		this.dict = dict;
		this.qualname = qualname;
	}

	boolean isSubtypeOf(PyType t) {
		for (PyType p = this; p != null; p = p.parent)
			if (p == t)
				return true;
		return false;
	}

	/** True for a class defined by the program. */
	boolean isClass() {
		return dict != null;
	}

	/**
	 * Looks a name up in the class and its bases, as CPython's MRO does for
	 * single inheritance. Builtin bases only provide __init__ (object and the
	 * exception classes). Returns null when the name is not found.
	 */
	Object lookup(String n) {
		for (PyType p = this; p != null; p = p.parent) {
			if (p.dict != null) {
				Object v = p.dict.get(n);
				if (v != null)
					return v;
			} else if (n.equals("__init__")) {
				if (p.exception)
					return Builtins.EXCEPTION_INIT;
				if (p == Types.OBJECT)
					return Builtins.OBJECT_INIT;
			}
		}
		return null;
	}
}
