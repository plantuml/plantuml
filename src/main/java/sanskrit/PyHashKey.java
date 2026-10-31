package sanskrit;

/**
 * The key of a dict for an instance of a native class that has __hash__ and
 * __eq__ (datetime.date...): two keys are the same if the hashes are and the
 * instances are equal, as in CPython. The entry of the dict keeps the instance
 * itself, so the keys and the items come back as they were given.
 */
final class PyHashKey {
	final PyInstance instance;
	private final int hash;

	PyHashKey(PyInstance instance) {
		this.instance = instance;
		final Object h = PyOps.callNative(instance, "__hash__");
		if (h instanceof Integer == false)
			throw PyOps.err(Types.TYPE_ERROR, "__hash__ method should return an integer");
		this.hash = ((Integer) h).intValue();
	}

	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof PyHashKey && PyOps.eq(instance, ((PyHashKey) o).instance);
	}
}
