package org.luaj.vm2.python;

/**
 * The value {@code NotImplemented}: what a comparison method returns to say that
 * it does not know the other operand, so that Python tries the other side.
 */
final class PyNotImplemented {
	static final PyNotImplemented INSTANCE = new PyNotImplemented();

	private PyNotImplemented() {
	}

	@Override
	public String toString() {
		return "NotImplemented";
	}
}
