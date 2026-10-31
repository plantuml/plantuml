package sanskrit;

import java.util.ArrayList;
import java.util.List;

/** A Python exception travelling through Java code. It carries no Java stack trace. */
public final class PyError extends RuntimeException {
	private static final long serialVersionUID = 1L;

	final transient PyException exc;
	private final List<String> frames = new ArrayList<String>();

	private String file;
	private int line;

	/**
	 * True for the error of a bare raise, or of a handler that does not match: the frame it is in
	 * already has its entry in the traceback, which must not get another one.
	 */
	boolean reraised;

	/** An error that carries the exception, with the traceback the exception has if it was caught before. */
	PyError(PyException exc) {
		super(null, null, false, false);
		this.exc = exc;
		PyError before = exc.traceback;
		if (before != null) {
			frames.addAll(before.frames);
			file = before.file;
			line = before.line;
		}
	}

	/** The error of a raise that continues the traceback of the exception without a new entry for the frame. */
	static PyError reraise(PyException exc) {
		PyError e = new PyError(exc);
		e.reraised = true;
		return e;
	}

	/** The file name of the innermost Python frame (where the exception was raised), or null. */
	public String getFile() {
		return file;
	}

	/** The line, in {@link #getFile()}, of the innermost Python frame; 0 if there is none. */
	public int getLine() {
		return line;
	}

	void addFrame(String file, int line, String function) {
		if (frames.isEmpty()) {
			// frames are added from the innermost one outwards
			this.file = file;
			this.line = line;
		}
		if (frames.size() < 50)
			frames.add("  File \"" + file + "\", line " + line + ", in " + function);
	}

	/**
	 * The name of the exception class, as CPython's traceback prints it:
	 * "ValueError", "MyError" for a class of the diagram, "palette.MyError" for
	 * a class of a module.
	 */
	public String typeName() {
		return exc.type.reportName();
	}

	/** The text of the exception, as str(e) gives it. */
	public String text() {
		return PyOps.excText(exc);
	}

	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder();
		if (!frames.isEmpty()) {
			sb.append("Traceback (most recent call last):\n");
			for (int i = frames.size() - 1; i >= 0; i--)
				sb.append(frames.get(i)).append('\n');
		}
		sb.append(typeName());
		String t = text();
		if (!t.isEmpty())
			sb.append(": ").append(t);
		return sb.toString();
	}
}
