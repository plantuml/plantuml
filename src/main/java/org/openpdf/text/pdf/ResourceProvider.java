/*
 * PlantUML fork of OpenPDF: how the resources (fonts metrics, glyph list, messages)
 * are found. See BaseFont#getResourceStream.
 */
package org.openpdf.text.pdf;

import java.io.IOException;
import java.io.InputStream;

/**
 * Supplies the resources OpenPDF needs (AFM font metrics, glyph list, messages) when
 * there is no class loader to find them, for instance in a native executable.
 * Install it with {@link BaseFont#setResourceProvider(ResourceProvider)}.
 */
public interface ResourceProvider {

    /**
     * @param name the resource name, without leading slash, for instance
     *             {@code org/openpdf/text/pdf/fonts/Helvetica.afm}
     * @return the content, or {@code null} if the resource is unknown
     * @throws IOException on error
     */
    InputStream open(String name) throws IOException;
}
