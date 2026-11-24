package net.sourceforge.plantuml.preproc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.preproc.spm.SpmChannel;

/**
 * Stdlib.retrieve follows the link of an alias library (awslib, bootstrap,
 * gcp, material...). It used to do it inside the computeIfAbsent of its static
 * cache, which a ConcurrentHashMap does not allow: when the nested insertion
 * made the table grow, it threw "IllegalStateException: Recursive update". It
 * happened or not depending on how many libraries were already cached, so
 * "stdlib" failed in a test suite only after some other tests.
 * <p>
 * Each attempt loads Stdlib in a new class loader, so that it starts with an
 * empty cache: the result does not depend on the tests that ran before, nor on
 * the sizes at which a ConcurrentHashMap grows. Every number of libraries
 * already cached is tried.
 */
class StdlibRetrieveTest {

	private static final String CLASS = "net/sourceforge/plantuml/preproc/Stdlib.class";
	private static final String HOME = "stdlib/home.spm";

	@Test
	void aliasesAreFollowedWhateverIsAlreadyCached() throws Exception {
		final List<String> aliases = new ArrayList<>();
		final List<String> others = new ArrayList<>();
		for (String name : Stdlib.getAllFolderNames())
			if (isAlias(name))
				aliases.add(name);
			else
				others.add(name);

		assertFalse(aliases.isEmpty(), "no alias library: nothing is tested");

		final URL[] classpath = { root(Stdlib.class.getClassLoader().getResource(CLASS), CLASS),
				root(Stdlib.class.getClassLoader().getResource(HOME), HOME) };

		for (int cached = 0; cached <= others.size(); cached++)
			try (URLClassLoader loader = new URLClassLoader(classpath, null)) {
				final Method retrieve = loader.loadClass(Stdlib.class.getName()).getMethod("retrieve",
						String.class);
				try {
					for (String name : others.subList(0, cached))
						retrieve.invoke(null, name);

					for (String alias : aliases) {
						final Object result = retrieve.invoke(null, alias);
						assertSame(result, retrieve.invoke(null, alias), alias + " is cached");
						final Object name = result.getClass().getMethod("getName").invoke(result);
						assertTrue(name.equals(alias) == false, alias + " is the library it links to, not " + name);
					}
				} catch (InvocationTargetException e) {
					throw new AssertionError(
							"after " + cached + " libraries " + others.subList(0, cached) + ": " + e.getCause(),
							e.getCause());
				}
			}
	}

	private static boolean isAlias(String name) throws IOException {
		try (InputStream is = SpmChannel.INFO.getInternalInputStream(name);
				BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
			String s;
			while ((s = br.readLine()) != null)
				if (s.startsWith("link="))
					return true;
		}
		return false;
	}

	/**
	 * The jar or the folder that holds a resource: plantuml.jar with ant, the
	 * classes and the resources folders with gradle.
	 */
	private static URL root(URL resource, String path) throws MalformedURLException {
		String s = resource.toString();
		s = s.substring(0, s.length() - path.length());
		if (s.startsWith("jar:"))
			s = s.substring("jar:".length(), s.length() - "!/".length());
		return URI.create(s).toURL();
	}

}
