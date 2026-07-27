package org.openpdf.text.pdf;

import java.awt.Color;

import org.openpdf.text.Image;

/**
 * Compile-only stub of OpenPDF's {@code PdfContentByte}, covering only the
 * drawing operators PlantUML's PDF backend calls. Every method throws - see
 * clide_stubs/openpdf/README.md. Package-visible constructor: never
 * instantiated directly by PlantUML code, only obtained (in the real API)
 * via {@link PdfWriter#getDirectContent()} or produced as a
 * {@link PdfTemplate}.
 */
public class PdfContentByte {

	PdfContentByte() {
	}

	public void setLineWidth(final float width) {
		throw new UnsupportedOperationException("OpenPDF stub: compile-only, never meant to run.");
	}

	public void setColorStroke(final Color color) {
		throw new UnsupportedOperationException("OpenPDF stub: compile-only, never meant to run.");
	}

	public void setColorFill(final Color color) {
		throw new UnsupportedOperationException("OpenPDF stub: compile-only, never meant to run.");
	}

	public void setLineDash(final float phase) {
		throw new UnsupportedOperationException("OpenPDF stub: compile-only, never meant to run.");
	}

	public void setLineDash(final float unitsOn, final float unitsOff, final float phase) {
		throw new UnsupportedOperationException("OpenPDF stub: compile-only, never meant to run.");
	}

	public void fillStroke() {
		throw new UnsupportedOperationException("OpenPDF stub: compile-only, never meant to run.");
	}

	public void fill() {
		throw new UnsupportedOperationException("OpenPDF stub: compile-only, never meant to run.");
	}

	public void stroke() {
		throw new UnsupportedOperationException("OpenPDF stub: compile-only, never meant to run.");
	}

	public void newPath() {
		throw new UnsupportedOperationException("OpenPDF stub: compile-only, never meant to run.");
	}

	public void eoFillStroke() {
		throw new UnsupportedOperationException("OpenPDF stub: compile-only, never meant to run.");
	}

	public void eoFill() {
		throw new UnsupportedOperationException("OpenPDF stub: compile-only, never meant to run.");
	}

	public void roundRectangle(final float x, final float y, final float w, final float h, final float radius) {
		throw new UnsupportedOperationException("OpenPDF stub: compile-only, never meant to run.");
	}

	public void rectangle(final float x, final float y, final float w, final float h) {
		throw new UnsupportedOperationException("OpenPDF stub: compile-only, never meant to run.");
	}

	public void moveTo(final float x, final float y) {
		throw new UnsupportedOperationException("OpenPDF stub: compile-only, never meant to run.");
	}

	public void lineTo(final float x, final float y) {
		throw new UnsupportedOperationException("OpenPDF stub: compile-only, never meant to run.");
	}

	public void curveTo(final float x1, final float y1, final float x2, final float y2, final float x3,
			final float y3) {
		throw new UnsupportedOperationException("OpenPDF stub: compile-only, never meant to run.");
	}

	public void ellipse(final float x1, final float y1, final float x2, final float y2) {
		throw new UnsupportedOperationException("OpenPDF stub: compile-only, never meant to run.");
	}

	public void closePath() {
		throw new UnsupportedOperationException("OpenPDF stub: compile-only, never meant to run.");
	}

	public void beginText() {
		throw new UnsupportedOperationException("OpenPDF stub: compile-only, never meant to run.");
	}

	public void endText() {
		throw new UnsupportedOperationException("OpenPDF stub: compile-only, never meant to run.");
	}

	public void setFontAndSize(final BaseFont font, final float size) {
		throw new UnsupportedOperationException("OpenPDF stub: compile-only, never meant to run.");
	}

	public void setTextMatrix(final float x, final float y) {
		throw new UnsupportedOperationException("OpenPDF stub: compile-only, never meant to run.");
	}

	public void showText(final String text) {
		throw new UnsupportedOperationException("OpenPDF stub: compile-only, never meant to run.");
	}

	public void addImage(final Image image) {
		throw new UnsupportedOperationException("OpenPDF stub: compile-only, never meant to run.");
	}

	public void addTemplate(final PdfTemplate template, final float x, final float y) {
		throw new UnsupportedOperationException("OpenPDF stub: compile-only, never meant to run.");
	}

	public PdfTemplate createTemplate(final float width, final float height) {
		throw new UnsupportedOperationException("OpenPDF stub: compile-only, never meant to run.");
	}

}
