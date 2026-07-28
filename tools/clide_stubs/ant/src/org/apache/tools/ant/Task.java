package org.apache.tools.ant;

/**
 * Compile-only stub of Ant's {@code Task}, the base class PlantUML's
 * {@code CheckZipTask} and {@code PlantUmlTask} extend. Construction is
 * harmless; every method that would need a real running Ant project throws
 * (see clide_stubs/ant/README.md).
 */
public class Task {

	public Task() {
		// harmless: just a compile-only stub, see README.md
	}

	public Project getProject() {
		throw new UnsupportedOperationException("Ant stub: compile-only, never meant to run.");
	}

	public void log(final String msg) {
		throw new UnsupportedOperationException("Ant stub: compile-only, never meant to run.");
	}

	public void execute() throws BuildException {
		throw new UnsupportedOperationException("Ant stub: compile-only, never meant to run.");
	}

}
