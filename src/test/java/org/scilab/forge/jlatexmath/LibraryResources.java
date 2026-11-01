package org.scilab.forge.jlatexmath;

/**
 * The library has no default source of resources (see TeXResources): a test that uses it
 * without going through PlantUML installs the class path as the JVM does.
 */
final class LibraryResources {

	private LibraryResources() {
	}

	static void install() {
		if (TeXResources.getProvider() == null)
			TeXResources.setProvider(new TeXClassLoaderResources());
	}

}
