/*
 * TeaVM C build only (PDF through OpenPDF): minimal stand-in for org.apache.fop.fonts.truetype.FontFileReader.
 * TeaVM compiles every reachable method, so classes referenced by OpenPDF must
 * exist even if never executed. Nothing here is meant to work at runtime.
 * Mapped by TeaVM's classlib naming rule (org.teavm.classlib.<package>.T<Name>).
 * See src/teavm-c/README.md.
 */
// ::remove file when JAVA8
package org.apache.fop.fonts.truetype;

import java.io.IOException;
import java.io.InputStream;

public class FontFileReader {
	public FontFileReader(InputStream in) throws IOException {
	}
}
