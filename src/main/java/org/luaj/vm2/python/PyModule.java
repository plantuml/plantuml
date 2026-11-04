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

	/** The member called {@code fn} (a function, for instance), or null. */
	public Object get(String fn) {
		return members.get(fn);
	}
}
