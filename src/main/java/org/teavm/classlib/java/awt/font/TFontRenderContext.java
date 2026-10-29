/*
 * TeaVM C build only (PDF through OpenPDF): minimal stand-in for java.awt.font.FontRenderContext.
 * TeaVM compiles every reachable method, so classes referenced by OpenPDF must
 * exist even if never executed. Nothing here is meant to work at runtime.
 * Mapped by TeaVM's classlib naming rule (org.teavm.classlib.<package>.T<Name>).
 * See src/teavm-c/README.md.
 */
// ::remove file when JAVA8
package org.teavm.classlib.java.awt.font;

import org.teavm.classlib.java.awt.geom.TAffineTransform;

public class TFontRenderContext {
	public TFontRenderContext(TAffineTransform tx, boolean isAntiAliased, boolean usesFractionalMetrics) {
	}
}
