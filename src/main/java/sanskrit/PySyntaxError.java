package sanskrit;

/** A syntax error found while reading Python source. */
public class PySyntaxError extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public final int line;
	public final int col;
	/** The message without the position. */
	public final String detail;

	public PySyntaxError(String message, int line, int col) {
		super(message + " (line " + line + ", column " + (col + 1) + ")");
		this.detail = message;
		this.line = line;
		this.col = col;
	}
}
