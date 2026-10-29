/*
 * TeaVM C build only (PDF through OpenPDF): minimal stand-in for org.apache.fop.fonts.truetype.TTFFile.
 * TeaVM compiles every reachable method, so classes referenced by OpenPDF must
 * exist even if never executed. Nothing here is meant to work at runtime.
 * Mapped by TeaVM's classlib naming rule (org.teavm.classlib.<package>.T<Name>).
 * See src/teavm-c/README.md.
 */
// ::remove file when JAVA8
package org.apache.fop.fonts.truetype;

import org.apache.fop.complexscripts.fonts.GlyphSubstitutionTable;

public class TTFFile {
	public TTFFile(boolean useKerning, boolean useAdvanced) {
	}

	public boolean isCFF() {
		return false;
	}

	public GlyphSubstitutionTable getGSUB() {
		return null;
	}
}
