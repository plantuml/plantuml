package org.luaj.vm2.python;

import java.util.Map;

/** A function defined by the program. */
final class PyFunction {
	final Code code;
	final Map<String, Object> globals;
	final Object[] defaults;
	final Map<String, Object> kwdefaults;
	final Cell[] closure;

	PyFunction(Code code, Map<String, Object> globals, Object[] defaults, Map<String, Object> kwdefaults,
			Cell[] closure) {
		this.code = code;
		this.globals = globals;
		this.defaults = defaults;
		this.kwdefaults = kwdefaults;
		this.closure = closure;
	}
}
