package org.apache.tools.ant.types;

import java.io.File;

import org.apache.tools.ant.Project;

/**
 * Compile-only stub of Ant's {@code FileList}. Construction is harmless;
 * every method that would need real filesystem logic throws
 * (see clide_stubs/ant/README.md).
 */
public class FileList {

	public FileList() {
		// harmless: just a compile-only stub, see README.md
	}

	public File getDir(final Project project) {
		throw new UnsupportedOperationException("Ant stub: compile-only, never meant to run.");
	}

	public String[] getFiles(final Project project) {
		throw new UnsupportedOperationException("Ant stub: compile-only, never meant to run.");
	}

}
