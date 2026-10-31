package sanskrit;

/** The value {@code None}. */
final class PyNone {
	static final PyNone NONE = new PyNone();

	private PyNone() {
	}

	@Override
	public String toString() {
		return "None";
	}
}
