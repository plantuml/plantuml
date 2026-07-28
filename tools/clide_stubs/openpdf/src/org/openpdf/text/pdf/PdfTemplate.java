package org.openpdf.text.pdf;

import org.openpdf.text.Rectangle;

/**
 * Compile-only stub of OpenPDF's {@code PdfTemplate}, a form XObject that
 * (in the real API) is itself a {@link PdfContentByte}, since PlantUML
 * assigns a created template straight into a {@code PdfContentByte}-typed
 * field - see clide_stubs/openpdf/README.md.
 */
public class PdfTemplate extends PdfContentByte {

	PdfTemplate() {
		super();
	}

	public void setBoundingBox(final Rectangle boundingBox) {
		throw new UnsupportedOperationException("OpenPDF stub: compile-only, never meant to run.");
	}

}
