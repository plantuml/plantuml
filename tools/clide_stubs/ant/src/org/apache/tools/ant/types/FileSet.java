package org.apache.tools.ant.types;

import java.io.File;

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;

/**
 * Compile-only stub of Ant's {@code FileSet}. Construction is harmless;
 * every method that would need real filesystem-scanning logic throws
 * (see clide_stubs/ant/README.md).
 */
public class FileSet {

	public FileSet() {
		// harmless: just a compile-only stub, see README.md
	}

	public DirectoryScanner getDirectoryScanner(final Project project) {
		throw new UnsupportedOperationException("Ant stub: compile-only, never meant to run.");
	}

	public File getDir(final Project project) {
		throw new UnsupportedOperationException("Ant stub: compile-only, never meant to run.");
	}

}
