package org.luaj.vm2.python;

import java.util.Map;

/** A function implemented in Java. */
final class Builtin {
	interface Fn {
		Object call(Interp in, Object[] args);
	}

	interface KwFn {
		Object call(Interp in, Object[] args, Map<String, Object> kw);
	}

	final String name;
	final Fn fn;
	final KwFn kwFn;

	Builtin(String name, Fn fn) {
		this(name, fn, null);
	}

	Builtin(String name, Fn fn, KwFn kwFn) {
		this.name = name;
		this.fn = fn;
		this.kwFn = kwFn;
	}

	Object call(Interp in, Object[] args) {
		return fn.call(in, args);
	}

	Object callKw(Interp in, Object[] args, Map<String, Object> kw) {
		if (kwFn == null)
			throw PyOps.err(Types.TYPE_ERROR, name + "() takes no keyword arguments");
		return kwFn.call(in, args, kw);
	}
}
