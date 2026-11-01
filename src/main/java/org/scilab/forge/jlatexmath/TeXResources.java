/* TeXResources.java
 * =========================================================================
 * This file is NOT part of upstream JLaTeXMath, it was added for PLAN.md
 * (phase 3, step 3e). Every resource of the library is read here.
 * It is released under the same terms as the library: GNU GPL version 2 or
 * later, with the GNU linking exception (see the header of the other files of
 * this package).
 * =========================================================================
 */

package org.scilab.forge.jlatexmath;

import java.io.IOException;
import java.io.InputStream;

/**
 * Where the library reads its resources: the {@link TeXResourceProvider} that is
 * installed with {@link #setProvider(TeXResourceProvider)}, and nothing else (as
 * {@code BaseFont.setResourceProvider} of OpenPDF). The JVM installs
 * {@link TeXClassLoaderResources}, which reads the class path; the C build has no
 * class loader and installs a provider that reads the files embedded in the
 * executable. Without a provider, there is no resource.
 * <p>
 * The name of a resource is the one that {@code Class.getResourceAsStream} takes:
 * relative to the package of the class that asks, or from the root of the class
 * path when it starts with a slash. The provider is given the whole name, without
 * slash.
 */
public final class TeXResources {

    private static volatile TeXResourceProvider provider;

    private TeXResources() {
    }

    /** Null removes it: the library then finds no resource. */
    public static void setProvider(TeXResourceProvider provider) {
        TeXResources.provider = provider;
    }

    public static TeXResourceProvider getProvider() {
        return provider;
    }

    /**
     * @param anchor the class that asks (its package is where a relative name starts)
     * @return the content, or null if there is no such resource
     */
    public static InputStream open(Class<?> anchor, String name) {
        final TeXResourceProvider current = provider;
        if (current == null) {
            return null;
        }
        try {
            return current.open(absoluteName(anchor, name));
        } catch (IOException e) {
            return null;
        }
    }

    /** The name from the root, without slash: "fonts/x.xml" asked by org.a.B is "org/a/fonts/x.xml". */
    static String absoluteName(Class<?> anchor, String name) {
        if (name.startsWith("/")) {
            return name.substring(1);
        }
        final String className = anchor.getName();
        final int dot = className.lastIndexOf('.');
        if (dot < 0) {
            return name;
        }
        return className.substring(0, dot).replace('.', '/') + "/" + name;
    }
}
