package org.luaj.vm2.python;

import java.util.Map;

/** A function implemented in Java. */
public final class Builtin {
	/** The body of a function written in Java: the interpreter and the positional arguments (Python values). */
	public interface Fn {
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

	/** A function written in Java, to be given to Python by the host (a decorator, for instance). */
	public static Builtin of(String name, Fn fn) {
		return new Builtin(name, fn);
	}

	/** The Python value {@code None}, for a function that returns nothing. */
	public static Object none() {
		return PyNone.NONE;
	}

	/** A Python {@code TypeError}, to be thrown from a {@link Fn}. */
	public static PyError typeError(String message) {
		return PyOps.err(Types.TYPE_ERROR, message);
	}

	/** A Python {@code RuntimeError}, to be thrown from a {@link Fn}. */
	public static PyError runtimeError(String message) {
		return PyOps.err(Types.RUNTIME_ERROR, message);
	}

	/** A Python {@code ValueError}, to be thrown from a {@link Fn}. */
	public static PyError valueError(String message) {
		return PyOps.err(Types.VALUE_ERROR, message);
	}
}
