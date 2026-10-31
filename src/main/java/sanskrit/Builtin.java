package sanskrit;

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
	/**
	 * True for a method of a native class (a class written in Java, such as
	 * datetime.date): read through an instance it is bound to it, as the
	 * function of a class of the program is, and the instance is its first
	 * argument. A builtin function stored in a class is not bound otherwise.
	 */
	boolean isMethod;

	Builtin(String name, Fn fn) {
		this(name, fn, null);
	}

	Builtin(String name, Fn fn, KwFn kwFn) {
		this.name = name;
		this.fn = fn;
		this.kwFn = kwFn;
	}

	/** A method of a native class that takes only positional arguments. */
	static Builtin method(String name, Fn fn) {
		final Builtin b = new Builtin(name, fn);
		b.isMethod = true;
		return b;
	}

	/** A method of a native class that also takes keyword arguments (kw is null when there are none). */
	static Builtin methodKw(String name, KwFn fn) {
		final Builtin b = new Builtin(name, (in, a) -> fn.call(in, a, null), fn);
		b.isMethod = true;
		return b;
	}

	/** The result of a call: a String a function gives is a str of the program, a PyStr. */
	private static Object result(Object r) {
		return r instanceof String ? PyStr.of((String) r) : r;
	}

	Object call(Interp in, Object[] args) {
		return result(fn.call(in, args));
	}

	Object callKw(Interp in, Object[] args, Map<String, Object> kw) {
		if (kwFn == null)
			throw PyOps.err(Types.TYPE_ERROR, name + "() takes no keyword arguments");
		return result(kwFn.call(in, args, kw));
	}

	/**
	 * A function written by the host: the str it is given are Strings, whatever
	 * form they have inside the interpreter (a str inside a list or a dict is not
	 * converted).
	 */
	static Fn forHost(Fn body) {
		return (in, a) -> body.call(in, PyStr.unwrapArgs(a));
	}

	/** A function written in Java, to be given to Python by the host (a decorator, for instance). */
	public static Builtin of(String name, Fn fn) {
		return new Builtin(name, forHost(fn));
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
