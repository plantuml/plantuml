package org.openpdf.text;

import java.io.IOException;

/**
 * Compile-only stub of OpenPDF's {@code Image}. Both {@code getInstance}
 * overloads declare exactly the checked exceptions PlantUML's PdfGraphics
 * catches around each call site ({@code IOException} for the byte[] form,
 * {@code BadElementException} alone for the AWT-image form) - see
 * clide_stubs/openpdf/README.md.
 */
public class Image {

	private Image() {
	}

	public static Image getInstance(final byte[] imgb) throws IOException {
		throw new UnsupportedOperationException("OpenPDF stub: compile-only, never meant to run.");
	}

	public static Image getInstance(final java.awt.Image image, final java.awt.Color color, final boolean forceBW)
			throws BadElementException {
		throw new UnsupportedOperationException("OpenPDF stub: compile-only, never meant to run.");
	}

	public void scaleAbsolute(final float width, final float height) {
		throw new UnsupportedOperationException("OpenPDF stub: compile-only, never meant to run.");
	}

	public void setAbsolutePosition(final float x, final float y) {
		throw new UnsupportedOperationException("OpenPDF stub: compile-only, never meant to run.");
	}

}
