package org.luaj.vm2.python;

import java.util.HashMap;
import java.util.Map;

/** A builtin module such as math. */
final class PyModule {
	final String name;
	final Map<String, Object> members = new HashMap<String, Object>();

	PyModule(String name) {
		this.name = name;
	}
}
