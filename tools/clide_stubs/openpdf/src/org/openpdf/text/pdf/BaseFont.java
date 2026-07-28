package org.openpdf.text.pdf;

import java.io.IOException;

import org.openpdf.text.DocumentException;

/**
 * Compile-only stub of OpenPDF's {@code BaseFont}. {@link #createFont} is the
 * single factory PlantUML's font resolution goes through, so making it throw
 * guarantees no real instance of this class ever exists - the instance
 * methods below are never reachable in practice, but still throw themselves
 * for consistency (see clide_stubs/openpdf/README.md).
 */
public class BaseFont {

	// Font-descriptor keys (arbitrary values - never meaningfully read, see above)
	public static final int ASCENT = 1;
	public static final int DESCENT = 2;

	// Encoding / embedding constants (arbitrary values)
	public static final String WINANSI = "Cp1252";
	public static final boolean NOT_EMBEDDED = false;

	// Standard 14 font names (arbitrary but realistic values)
	public static final String COURIER = "Courier";
	public static final String COURIER_BOLD = "Courier-Bold";
	public static final String COURIER_OBLIQUE = "Courier-Oblique";
	public static final String COURIER_BOLDOBLIQUE = "Courier-BoldOblique";
	public static final String TIMES_ROMAN = "Times-Roman";
	public static final String TIMES_BOLD = "Times-Bold";
	public static final String TIMES_ITALIC = "Times-Italic";
	public static final String TIMES_BOLDITALIC = "Times-BoldItalic";
	public static final String HELVETICA = "Helvetica";
	public static final String HELVETICA_BOLD = "Helvetica-Bold";
	public static final String HELVETICA_OBLIQUE = "Helvetica-Oblique";
	public static final String HELVETICA_BOLDOBLIQUE = "Helvetica-BoldOblique";

	private BaseFont() {
	}

	public static BaseFont createFont(final String name, final String encoding, final boolean embedded)
			throws DocumentException, IOException {
		throw new UnsupportedOperationException("OpenPDF stub: compile-only, never meant to run.");
	}

	public float getFontDescriptor(final int key, final float fontSize) {
		throw new UnsupportedOperationException("OpenPDF stub: compile-only, never meant to run.");
	}

	public float getWidthPoint(final String text, final float fontSize) {
		throw new UnsupportedOperationException("OpenPDF stub: compile-only, never meant to run.");
	}

}
