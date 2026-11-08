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
	/**
	 * CPython's __module__ of a class defined by the program: the __name__ of
	 * the globals it was defined in ("__main__" for a diagram); null for a
	 * builtin type.
	 */
	final String module;
	/**
	 * The interpreter that ran the class statement: a class belongs to one
	 * interpreter state and never appears in another one, so an instance can ask
	 * its class to run a special method (__str__, __repr__, ...) from code that
	 * has no interpreter at hand, such as the static methods of PyOps. Null for a
	 * builtin type.
	 */
	Interp interp;

	PyType(String name, PyType parent, boolean exception) {
		this(name, parent, exception, null, null, null);
	}

	PyType(String name, PyType parent, boolean exception, Map<String, Object> dict, String qualname,
			String module) {
		this.name = name;
		this.parent = parent;
		this.exception = exception;
		this.dict = dict;
		this.qualname = qualname;
		this.module = module;
	}

	/** The name used by repr: "int", or "palette.Box" for a class of the program. */
	String fullName() {
		return dict == null ? name : module + "." + qualname;
	}

	/**
	 * The name used in error reports, as CPython's traceback prints it: the
	 * module is left out for a builtin type and for a class of "__main__".
	 */
	String reportName() {
		if (dict == null)
			return name;
		return module.equals("__main__") ? qualname : module + "." + qualname;
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
