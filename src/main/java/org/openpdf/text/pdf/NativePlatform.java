/*
 * PlantUML fork of OpenPDF: how the resources (fonts metrics, glyph list, messages)
 * are found. See BaseFont#getResourceStream.
 */
package org.openpdf.text.pdf;

import org.teavm.interop.PlatformMarker;
import org.teavm.interop.Platforms;

/**
 * Platform marker for the TeaVM C backend. TeaVM resolves {@link #isC()} to
 * {@code true} at compile time and prunes the code guarded by it, in the method
 * that tests it (the test must not be hidden behind another method). On the JVM it
 * simply returns {@code false}.
 */
public final class NativePlatform {

    private NativePlatform() {
    }

    @PlatformMarker(Platforms.C)
    public static boolean isC() {
        return false;
    }
}
