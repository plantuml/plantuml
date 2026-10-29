/*
 * TeaVM C build only (PDF through OpenPDF): minimal stand-in for org.apache.fop.complexscripts.fonts.GlyphSubstitutionTable.
 * TeaVM compiles every reachable method, so classes referenced by OpenPDF must
 * exist even if never executed. Nothing here is meant to work at runtime.
 * Mapped by TeaVM's classlib naming rule (org.teavm.classlib.<package>.T<Name>).
 * See src/teavm-c/README.md.
 */
// ::remove file when JAVA8
package org.apache.fop.complexscripts.fonts;

import org.apache.fop.complexscripts.util.GlyphSequence;

public class GlyphSubstitutionTable {
	public GlyphSequence substitute(GlyphSequence gs, String script, String language) {
		return gs;
	}
}
