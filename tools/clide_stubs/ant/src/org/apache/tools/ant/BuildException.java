package org.apache.tools.ant;

/**
 * Compile-only stub of Ant's {@code BuildException}. Unchecked in the real
 * API (extends {@link RuntimeException}), so declaring {@code throws
 * BuildException} anywhere is just documentation, never a compiler
 * requirement - see clide_stubs/ant/README.md.
 */
public class BuildException extends RuntimeException {

	public BuildException() {
		super();
	}

	public BuildException(String message) {
		super(message);
	}

	public BuildException(Throwable cause) {
		super(cause);
	}

	public BuildException(String message, Throwable cause) {
		super(message, cause);
	}

}
