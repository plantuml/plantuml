package org.openpdf.text.pdf;

import org.openpdf.text.Rectangle;

/**
 * Compile-only stub of OpenPDF's {@code PdfAnnotation}. {@link #createLink}
 * is the only factory PlantUML's PDF backend uses, so it throws - see
 * clide_stubs/openpdf/README.md.
 */
public class PdfAnnotation {

	// Arbitrary value - never meaningfully read, see createLink below.
	public static final int HIGHLIGHT_INVERT = 2;

	private PdfAnnotation() {
	}

	public static PdfAnnotation createLink(final PdfWriter writer, final Rectangle rectangle, final int highlight,
			final PdfAction action) {
		throw new UnsupportedOperationException("OpenPDF stub: compile-only, never meant to run.");
	}

}
