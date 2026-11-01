/* TeXResourceProvider.java
 * =========================================================================
 * This file is NOT part of upstream JLaTeXMath, it was added for PLAN.md
 * (phase 3, step 3e). Where the library finds its resources when there is no class loader.
 * It is released under the same terms as the library: GNU GPL version 2 or
 * later, with the GNU linking exception (see the header of the other files of
 * this package).
 * =========================================================================
 */

package org.scilab.forge.jlatexmath;

import java.io.IOException;
import java.io.InputStream;

/**
 * Supplies the resources of the library (the XML files of the font metrics, the
 * symbols and the macros, and the glyph outlines) when there is no class loader
 * to find them, for instance in a native executable. Install it with
 * {@link TeXResources#setProvider(TeXResourceProvider)}.
 */
public interface TeXResourceProvider {

    /**
     * @param name the resource name, without leading slash, for instance
     *            {@code org/scilab/forge/jlatexmath/fonts/base/jlm_cmr10.xml}
     * @return the content, or {@code null} if the resource is unknown
     * @throws IOException on error
     */
    InputStream open(String name) throws IOException;
}
