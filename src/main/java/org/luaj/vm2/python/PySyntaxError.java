package org.luaj.vm2.python;

/** A syntax error found while reading Python source. */
public class PySyntaxError extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public final int line;
	public final int col;

	public PySyntaxError(String message, int line, int col) {
		super(message + " (line " + line + ", column " + (col + 1) + ")");
		this.line = line;
		this.col = col;
	}
}
