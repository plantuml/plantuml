package org.luaj.vm2.python;

/** A method of a builtin value, e.g. {@code [].append}. */
final class BoundMethod {
	final Object self;
	final Builtin method;

	BoundMethod(Object self, Builtin method) {
		this.self = self;
		this.method = method;
	}
}
