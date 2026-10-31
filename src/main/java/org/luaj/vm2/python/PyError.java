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

	void addFrame(String file, int line, String function) {
		if (frames.size() < 50)
			frames.add("  File \"" + file + "\", line " + line + ", in " + function);
	}

	/** The name of the exception class, e.g. "ValueError". */
	public String typeName() {
		return exc.type.name;
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
		sb.append(exc.type.name);
		String t = text();
		if (!t.isEmpty())
			sb.append(": ").append(t);
		return sb.toString();
	}
}
