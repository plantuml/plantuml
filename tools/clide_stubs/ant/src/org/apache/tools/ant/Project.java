package org.apache.tools.ant;

/**
 * Compile-only stub of Ant's {@code Project}. PlantUML's two Ant tasks
 * (CheckZipTask, PlantUmlTask) only ever pass this around as an opaque
 * handle ({@code getProject()} then straight into
 * {@code fileSet.getDir(project)} etc.) - they never call a method on it
 * directly, so unlike Task/FileSet/FileList/DirectoryScanner there is no
 * "real work" to prevent here: this is left as a harmless empty class
 * rather than a throwing stub (see clide_stubs/ant/README.md).
 */
public class Project {
}
