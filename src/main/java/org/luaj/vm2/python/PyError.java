package org.luaj.vm2.python;

import java.util.ArrayList;
import java.util.List;

/** A Python exception travelling through Java code. It carries no Java stack trace. */
public final class PyError extends RuntimeException {
	private static final long serialVersionUID = 1L;

	final transient PyException exc;
	private final List<String> frames = new ArrayList<String>();

	PyError(PyException exc) {
		super(null, null, false, false);
		this.exc = exc;
	}

	private String file;
	private int line;

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
