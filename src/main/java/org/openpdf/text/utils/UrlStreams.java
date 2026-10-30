/*
 * PlantUML fork of OpenPDF: URL streams are not available in the native (TeaVM C)
 * build.
 */
package org.openpdf.text.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.openpdf.text.pdf.NativePlatform;

/**
 * Opens the content of an {@link URL}. In the TeaVM C build, {@code URL.openStream()} is an
 * asynchronous method of TeaVM (XHR connection), and one single asynchronous method
 * turns every method that can reach it into a coroutine, which TeaVM's C backend cannot
 * transform in every case. So the call is pruned there, and this method throws.
 */
public final class UrlStreams {

    private UrlStreams() {
    }

    /**
     * @param url the URL to read
     * @return the stream of the content of the URL
     * @throws IOException on error, always in the native build
     */
    public static InputStream open(URL url) throws IOException {
        // The marker is tested here, so that TeaVM prunes the call below
        if (NativePlatform.isC()) {
            throw new IOException("URL streams are not available in the native build");
        }
        return url.openStream();
    }
}
