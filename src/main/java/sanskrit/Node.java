package sanskrit;

/**
 * A node of the syntax tree. The tree follows the shape of CPython's {@code ast}
 * module: same node names, same fields in the same order, so that a dump of it
 * can be compared with the dump of the tree CPython builds for the same source.
 *
 * <p>Every kind of node is a class of {@link Ast} with typed fields; passes
 * discriminate them with {@code instanceof}.
 */
public abstract class Node {
	public final int line;
	public final int col;

	Node(int line, int col) {
		this.line = line;
		this.col = col;
	}

	/** The name of the node in CPython's {@code ast} module ("Name", "BinOp"...). */
	public abstract String type();

	/** The fields in CPython's order, as alternating names and values. Only {@link AstDump} uses it. */
	abstract Object[] fields();

	@Override
	public String toString() {
		return AstDump.dump(this);
	}
}
