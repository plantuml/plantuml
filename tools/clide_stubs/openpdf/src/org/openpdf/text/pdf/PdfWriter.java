package org.openpdf.text.pdf;

import java.io.OutputStream;

import org.openpdf.text.Document;

/**
 * Compile-only stub of OpenPDF's {@code PdfWriter}. {@link #getInstance} is
 * the single factory PlantUML's PDF backend goes through, so making it throw
 * guarantees no real instance of this class ever exists - see
 * clide_stubs/openpdf/README.md.
 */
public class PdfWriter {

	private PdfWriter() {
	}

	public static PdfWriter getInstance(final Document document, final OutputStream os) {
		throw new UnsupportedOperationException("OpenPDF stub: compile-only, never meant to run.");
	}

	public PdfContentByte getDirectContent() {
		throw new UnsupportedOperationException("OpenPDF stub: compile-only, never meant to run.");
	}

	public void addAnnotation(final PdfAnnotation annotation) {
		throw new UnsupportedOperationException("OpenPDF stub: compile-only, never meant to run.");
	}

}
