package org.luaj.vm2.python;

/** The program used up its instruction budget. A try/except cannot catch it. */
public final class ExecutionLimitError extends RuntimeException {
	private static final long serialVersionUID = 1L;

	ExecutionLimitError(String message) {
		super(message, null, false, false);
	}
}
