/* TeXClassLoaderResources.java
 * =========================================================================
 * This file is NOT part of upstream JLaTeXMath, it was added for PLAN.md
 * (phase 3, step 3e). The resources of the library found through the class loaders (the JVM).
 * It is released under the same terms as the library: GNU GPL version 2 or
 * later, with the GNU linking exception (see the header of the other files of
 * this package).
 * =========================================================================
 */

package org.scilab.forge.jlatexmath;

import java.io.IOException;
import java.io.InputStream;

/**
 * A {@link TeXResourceProvider} that finds the resources in the class path: the
 * files of {@code src/main/resources/org/scilab/forge/jlatexmath} in the jar. It is what
 * the JVM uses; it is a class of its own, and not the default of
 * {@link TeXResources}, so that the C build never reaches the code of the class
 * loaders (TeaVM compiles all it can reach, and its support of the resources
 * does not work there).
 */
public final class TeXClassLoaderResources implements TeXResourceProvider {

    public TeXClassLoaderResources() {
    }

    public InputStream open(String name) throws IOException {
        InputStream in = null;
        try {
            final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if (contextClassLoader != null) {
                in = contextClassLoader.getResourceAsStream(name);
            }
        } catch (SecurityException e) {
            // try the others
        }
        if (in == null) {
            in = TeXClassLoaderResources.class.getResourceAsStream("/" + name);
        }
        if (in == null) {
            in = ClassLoader.getSystemResourceAsStream(name);
        }
        return in;
    }
}
