/*
 * TeaVM C build only (PDF through OpenPDF): minimal stand-in for org.apache.fop.fonts.apps.TTFReader.
 * TeaVM compiles every reachable method, so classes referenced by OpenPDF must
 * exist even if never executed. Nothing here is meant to work at runtime.
 * Mapped by TeaVM's classlib naming rule (org.teavm.classlib.<package>.T<Name>).
 * See src/teavm-c/README.md.
 */
// ::remove file when JAVA8
package org.apache.fop.fonts.apps;

import java.io.IOException;

import org.apache.fop.fonts.truetype.TTFFile;

public class TTFReader {
	public static TTFFile loadTTF(String fileName, String fontName, boolean useKerning, boolean useAdvanced)
			throws IOException {
		return null;
	}
}
