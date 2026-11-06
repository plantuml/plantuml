package org.luaj.vm2.python;

import java.util.HashMap;
import java.util.Map;

/** A builtin module such as math. */
public final class PyModule {
	final String name;
	final Map<String, Object> members = new HashMap<String, Object>();

	public PyModule(String name) {
		this.name = name;
	}

	/** Adds a function written in Java, callable as {@code name.fn(...)} from Python. */
	public PyModule put(String fn, Builtin.Fn body) {
		members.put(fn, new Builtin(fn, body));
		return this;
	}

	/** Adds a constant string, readable as {@code name.constant}. */
	public PyModule putString(String constant, String value) {
		members.put(constant, value);
		return this;
	}

	/** Adds a constant {@code dict} of strings, in the order of the map. */
	public PyModule putStringMap(String constant, Map<String, String> values) {
		final PyDict dict = new PyDict();
		for (Map.Entry<String, String> entry : values.entrySet())
			dict.put(entry.getKey(), entry.getValue());
		members.put(constant, dict);
		return this;
	}

	/** The member called {@code fn} (a function, for instance), or null. */
	public Object get(String fn) {
		return members.get(fn);
	}
}
