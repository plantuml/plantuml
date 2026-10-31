package sanskrit;

/**
 * A method bound to its object: either a method of a builtin value, e.g.
 * {@code [].append}, or a function of a class reached through an instance.
 */
final class BoundMethod {
	final Object self;
	/** The builtin method, or null for a method of a class. */
	final Builtin method;
	/** The function of a class, or null for a builtin method. */
	final PyFunction function;

	BoundMethod(Object self, Builtin method) {
		this.self = self;
		this.method = method;
		this.function = null;
	}

	BoundMethod(Object self, PyFunction function) {
		this.self = self;
		this.method = null;
		this.function = function;
	}

	String name() {
		return function != null ? function.code.name : method.name;
	}
}
