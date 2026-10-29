/*
 * TeaVM C build only (PDF through OpenPDF): minimal stand-in for org.apache.fop.complexscripts.util.GlyphSequence.
 * TeaVM compiles every reachable method, so classes referenced by OpenPDF must
 * exist even if never executed. Nothing here is meant to work at runtime.
 * Mapped by TeaVM's classlib naming rule (org.teavm.classlib.<package>.T<Name>).
 * See src/teavm-c/README.md.
 */
// ::remove file when JAVA8
package org.apache.fop.complexscripts.util;

import java.nio.IntBuffer;
import java.util.List;

public class GlyphSequence {
	public GlyphSequence(IntBuffer characters, IntBuffer glyphs, List<?> associations) {
	}

	public IntBuffer getGlyphs() {
		return null;
	}

	public List<?> getAssociations() {
		return null;
	}
}
