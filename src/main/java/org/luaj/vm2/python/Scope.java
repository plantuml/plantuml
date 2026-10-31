package org.luaj.vm2.python;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** The names of one module, function, lambda or comprehension, and how each is stored. */
final class Scope {
	static final int MODULE = 0;
	static final int FUNCTION = 1;
	static final int COMPREHENSION = 2;

	// symbol flags
	static final int BOUND = 1;
	static final int USED = 2;
	static final int GLOBAL = 4;
	static final int NONLOCAL = 8;
	static final int PARAM = 16;
	/** Set by the resolver: a local that an inner scope refers to. */
	static final int CELL = 32;

	// result of a lookup
	static final int K_GLOBAL = 0;
	static final int K_LOCAL = 1;
	static final int K_CELL = 2;
	static final int K_FREE = 3;

	final int kind;
	final String name;
	final Scope parent;
	final Node node;
	final Map<String, Integer> symbols = new LinkedHashMap<String, Integer>();
	/** Parameter names in slot order: positional, keyword-only, *args, **kwargs. */
	final List<String> params = new ArrayList<String>();
	int argcount;
	int kwonlycount;
	String varargs;
	String kwargs;
	final Set<String> freeNames = new LinkedHashSet<String>();

	// layout, filled by layout()
	final Map<String, Integer> slots = new LinkedHashMap<String, Integer>();
	int firstFree;
	int nslots;
	final List<String> slotNames = new ArrayList<String>();

	Scope(int kind, String name, Scope parent, Node node) {
		this.kind = kind;
		this.name = name;
		this.parent = parent;
		this.node = node;
	}

	int flags(String n) {
		Integer f = symbols.get(n);
		return f == null ? 0 : f.intValue();
	}

	void addFlags(String n, int f) {
		symbols.put(n, Integer.valueOf(flags(n) | f));
	}

	/** True if the name is a local variable of this scope. */
	boolean isLocal(String n) {
		int f = flags(n);
		return (f & (BOUND | PARAM)) != 0 && (f & (GLOBAL | NONLOCAL)) == 0;
	}

	void layout() {
		for (String p : params) {
			slots.put(p, Integer.valueOf(slotNames.size()));
			slotNames.add(p);
		}
		if (kind != MODULE) {
			for (Map.Entry<String, Integer> e : symbols.entrySet()) {
				String n = e.getKey();
				if (!slots.containsKey(n) && isLocal(n)) {
					slots.put(n, Integer.valueOf(slotNames.size()));
					slotNames.add(n);
				}
			}
		}
		firstFree = slotNames.size();
		for (String n : freeNames) {
			slots.put(n, Integer.valueOf(slotNames.size()));
			slotNames.add(n);
		}
		nslots = slotNames.size();
	}

	int kindOf(String n) {
		if (kind == MODULE)
			return K_GLOBAL;
		if (freeNames.contains(n))
			return K_FREE;
		if (isLocal(n))
			return (flags(n) & CELL) != 0 ? K_CELL : K_LOCAL;
		return K_GLOBAL;
	}

	int slotOf(String n) {
		return slots.get(n).intValue();
	}
}
