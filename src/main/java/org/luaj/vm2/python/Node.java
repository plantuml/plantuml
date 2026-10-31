package org.luaj.vm2.python;

import java.util.List;

/**
 * A node of the syntax tree. The tree follows the shape of CPython's {@code ast}
 * module: same node names, same fields in the same order, so that a dump of it
 * can be compared with the dump of the tree CPython builds for the same source.
 *
 * <p>Field values are: another {@link Node}, a {@link List} of values, a
 * {@link String}, a {@link Long} (int), a {@link Double} (float), a
 * {@link Boolean}, or {@code null} for {@code None}. Contexts ({@code Load},
 * {@code Store}, {@code Del}) and operators ({@code Add}, {@code Not}...) are
 * nodes without fields.
 */
public final class Node {
	public final String type;
	public final int line;
	public final int col;
	private final String[] names;
	private final Object[] values;
	/** Free slot for the passes that annotate the tree (the resolver stores a scope here). */
	Object info;

	/** Creates a node from alternating field names and values. */
	public Node(String type, int line, int col, Object... nameValuePairs) {
		this.type = type;
		this.line = line;
		this.col = col;
		int n = nameValuePairs.length / 2;
		this.names = new String[n];
		this.values = new Object[n];
		for (int i = 0; i < n; i++) {
			names[i] = (String) nameValuePairs[2 * i];
			values[i] = nameValuePairs[2 * i + 1];
		}
	}

	/** A node without fields: a context or an operator. */
	public static Node bare(String type) {
		return new Node(type, 0, 0);
	}

	public int fieldCount() {
		return names.length;
	}

	public String fieldName(int i) {
		return names[i];
	}

	public Object fieldValue(int i) {
		return values[i];
	}

	public Object get(String name) {
		for (int i = 0; i < names.length; i++)
			if (names[i].equals(name))
				return values[i];
		throw new IllegalArgumentException(type + " has no field " + name);
	}

	public void set(String name, Object value) {
		for (int i = 0; i < names.length; i++)
			if (names[i].equals(name)) {
				values[i] = value;
				return;
			}
		throw new IllegalArgumentException(type + " has no field " + name);
	}

	public Node node(String name) {
		return (Node) get(name);
	}

	@SuppressWarnings("unchecked")
	public List<Node> nodes(String name) {
		return (List<Node>) get(name);
	}

	public String str(String name) {
		return (String) get(name);
	}

	public boolean is(String t) {
		return type.equals(t);
	}

	@Override
	public String toString() {
		return AstDump.dump(this);
	}
}
