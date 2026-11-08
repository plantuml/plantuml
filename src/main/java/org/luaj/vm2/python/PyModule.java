package org.luaj.vm2.python;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
		members.put(constant, stringDict(values));
		return this;
	}

	/** A new {@code dict} of strings, in the order of the map: the value a function returns. */
	public static Object stringDict(Map<String, String> values) {
		final PyDict dict = new PyDict();
		for (Map.Entry<String, String> entry : values.entrySet())
			dict.put(entry.getKey(), entry.getValue());
		return dict;
	}

	/**
	 * Adds a constant built from plain Java values: a {@code Map} with String keys
	 * becomes a {@code dict} (in the order of the map), a {@code List} a
	 * {@code list}, a {@code String} a {@code str}. Nothing else is accepted: no Java
	 * object is ever exposed to a script.
	 */
	public PyModule putPlain(String constant, Object value) {
		members.put(constant, plain(value));
		return this;
	}

	private static Object plain(Object value) {
		if (value instanceof String)
			return value;
		if (value instanceof Map) {
			final PyDict dict = new PyDict();
			for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
				if (entry.getKey() instanceof String == false)
					throw new IllegalArgumentException("dict keys must be strings");
				dict.put(entry.getKey(), plain(entry.getValue()));
			}
			return dict;
		}
		if (value instanceof List) {
			final ArrayList<Object> items = new ArrayList<Object>();
			for (Object item : (List<?>) value)
				items.add(plain(item));
			return new PyList(items);
		}
		throw new IllegalArgumentException("unsupported value: " + (value == null ? "null" : value.getClass()));
	}

	/** The member called {@code fn} (a function, for instance), or null. */
	public Object get(String fn) {
		return members.get(fn);
	}
}
