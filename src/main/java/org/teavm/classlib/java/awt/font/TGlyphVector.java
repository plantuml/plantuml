/*
 * TeaVM C build only (PDF through OpenPDF): minimal stand-in for java.awt.font.GlyphVector.
 * TeaVM compiles every reachable method, so classes referenced by OpenPDF must
 * exist even if never executed. Nothing here is meant to work at runtime.
 * Mapped by TeaVM's classlib naming rule (org.teavm.classlib.<package>.T<Name>).
 * See src/teavm-c/README.md.
 */
// ::remove file when JAVA8
package org.teavm.classlib.java.awt.font;

import org.teavm.classlib.java.awt.geom.TPoint2D;

public abstract class TGlyphVector {
	public abstract int getNumGlyphs();

	public abstract TPoint2D getGlyphPosition(int glyphIndex);

	public abstract TGlyphMetrics getGlyphMetrics(int glyphIndex);

	public abstract int getGlyphCode(int glyphIndex);

	public abstract int getGlyphCharIndex(int glyphIndex);

	public abstract int[] getGlyphCodes(int beginGlyphIndex, int numEntries, int[] codeReturn);
}
