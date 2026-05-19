/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 *
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *
 * Original Author:  Arnaud Roques
 *
 */
package net.sourceforge.plantuml.openpdf;

import java.awt.Color;
import java.awt.geom.PathIterator;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Image;
import org.openpdf.text.Rectangle;
import org.openpdf.text.pdf.BaseFont;
import org.openpdf.text.pdf.PdfAction;
import org.openpdf.text.pdf.PdfAnnotation;
import org.openpdf.text.pdf.PdfContentByte;
import org.openpdf.text.pdf.PdfWriter;

/**
 * PDF backend that mirrors the public API of
 * {@code net.sourceforge.plantuml.klimt.drawing.svg.SvgGraphics}.
 *
 * <h2>Design notes</h2>
 *
 * <ul>
 * <li>PDF has Y growing upwards; SVG/PlantUML have Y growing downwards. We flip
 * the coordinate system once at start with {@code concatCTM(1, 0, 0, -1, 0, H)}
 * so that callers can keep using SVG-style coordinates throughout.</li>
 *
 * <li>The flip means text rendered with the default text matrix would appear
 * upside-down. We compensate by issuing {@code setTextMatrix(1, 0, 0, -1, x, y)}
 * for every text run, which flips it back locally.</li>
 *
 * <li>Page size is not known up-front (callers draw, then we close). We buffer
 * the drawing into a {@link PdfContentByte} obtained from the writer; the page
 * size is finalized in {@link #createPdf(OutputStream)} based on {@code maxX} /
 * {@code maxY} accumulated through {@link #ensureVisible(double, double)} -
 * exactly like {@code SvgGraphics} does for the SVG viewBox.</li>
 *
 * <li>Things deliberately left out in this first iteration: drop shadows,
 * inlined/embedded SVG images, gradients (background only is supported as a
 * solid color), interactive scripts, dark mode CSS.</li>
 * </ul>
 *
 * <h2>Not for TeaVM</h2>
 *
 * OpenPDF relies on {@code java.awt} (Color, fonts, images) and on java.util.zip
 * for deflate streams. It is not meant to be compiled to JavaScript via TeaVM.
 * This driver therefore lives outside the TeaVM build path.
 *
 * <h2>OpenPDF version</h2>
 *
 * Targets OpenPDF 3.0+ which uses the {@code org.openpdf} package namespace.
 * The legacy {@code com.lowagie} packages have been removed in 3.0 and are
 * not available anymore.
 */
public class PdfGraphics {

	// ---------- output buffering ----------

	private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	private final Document document;
	private final PdfWriter writer;
	private PdfContentByte cb;
	private boolean documentOpened = false;

	// ---------- options ----------

	private final PdfOption option;

	// ---------- bounding box tracking ----------

	private double maxX;
	private double maxY;

	// ---------- current graphics state ----------

	private Color fillColor = Color.BLACK;
	private Color strokeColor = Color.BLACK;
	private double strokeWidth = 1.0;
	private float[] strokeDash = null;
	private boolean hidden = false;

	// ---------- text state ----------

	private BaseFont currentFont;
	private final Map<String, BaseFont> fontCache = new HashMap<>();

	// ---------- current path (newpath/moveto/lineto/.../fill) ----------

	private boolean pathOpen = false;

	// ---------- pending links (for openLink/closeLink) ----------

	private final LinkedList<PendingLink> activeLinks = new LinkedList<>();

	// ----------------------------------------------------------------------
	// construction
	// ----------------------------------------------------------------------

	public PdfGraphics(PdfOption option) {
		this.option = option == null ? new PdfOption() : option;
		this.maxX = this.option.getMinWidth();
		this.maxY = this.option.getMinHeight();

		// We do not know the final page size yet. We open a placeholder page
		// (A0 - plenty of room) and clip later via the Rectangle in createPdf.
		// PDF coordinates are absolute; clipping the page size at close time
		// is the standard trick.
		this.document = new Document(new Rectangle(2400, 2400));
		this.writer = PdfWriter.getInstance(document, buffer);
	}

	private void openDocumentIfNeeded() {
		if (documentOpened)
			return;
		documentOpened = true;

		if (option.getTitle() != null)
			document.addTitle(option.getTitle());
		if (option.getAuthor() != null)
			document.addAuthor(option.getAuthor());
		if (option.getSubject() != null)
			document.addSubject(option.getSubject());

		document.open();
		this.cb = writer.getDirectContent();
	}

	// ----------------------------------------------------------------------
	// bounding box
	// ----------------------------------------------------------------------

	protected final void ensureVisible(double x, double y) {
		if (x > maxX)
			maxX = x + 1;
		if (y > maxY)
			maxY = y + 1;
	}

	// ----------------------------------------------------------------------
	// state setters (mirror SvgGraphics)
	// ----------------------------------------------------------------------

	public final void setFillColor(Color fill) {
		this.fillColor = fill;
	}

	public final void setStrokeColor(Color stroke) {
		this.strokeColor = stroke;
	}

	public final void setStrokeWidth(double width, double[] dash) {
		this.strokeWidth = width;
		if (dash == null) {
			this.strokeDash = null;
		} else {
			this.strokeDash = new float[dash.length];
			for (int i = 0; i < dash.length; i++)
				this.strokeDash[i] = (float) dash[i];
		}
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	// ----------------------------------------------------------------------
	// internal helpers
	// ----------------------------------------------------------------------

	private void applyStroke() {
		cb.setLineWidth((float) strokeWidth);
		if (strokeColor != null)
			cb.setColorStroke(strokeColor);
		if (strokeDash == null)
			cb.setLineDash(0);
		else if (strokeDash.length >= 2)
			cb.setLineDash(strokeDash[0], strokeDash[1], 0);
		else
			cb.setLineDash(strokeDash[0], strokeDash[0], 0);
	}

	private void applyFill() {
		if (fillColor != null)
			cb.setColorFill(fillColor);
	}

	private boolean hasFill() {
		return fillColor != null && fillColor.getAlpha() > 0;
	}

	private boolean hasStroke() {
		return strokeColor != null && strokeColor.getAlpha() > 0 && strokeWidth > 0;
	}

	/**
	 * Apply the appropriate paint operator depending on what is set.
	 */
	private void paintPath() {
		final boolean f = hasFill();
		final boolean s = hasStroke();
		if (f && s) {
			cb.fillStroke();
		} else if (f) {
			cb.fill();
		} else if (s) {
			cb.stroke();
		} else {
			// Nothing to do, just discard the path.
			cb.newPath();
		}
	}

	// ----------------------------------------------------------------------
	// shape primitives (mirror SvgGraphics)
	// ----------------------------------------------------------------------

	public void pdfRectangle(double x, double y, double width, double height, double rx, double ry) {
		if (width <= 0 || height <= 0)
			return;
		if (hidden)
			return;
		openDocumentIfNeeded();
		applyStroke();
		applyFill();
		if (rx > 0 && ry > 0)
			cb.roundRectangle((float) x, (float) y, (float) width, (float) height, (float) Math.min(rx, ry));
		else
			cb.rectangle((float) x, (float) y, (float) width, (float) height);
		paintPath();
		ensureVisible(x + width, y + height);
	}

	public void pdfLine(double x1, double y1, double x2, double y2) {
		if (hidden)
			return;
		openDocumentIfNeeded();
		applyStroke();
		cb.moveTo((float) x1, (float) y1);
		cb.lineTo((float) x2, (float) y2);
		cb.stroke();
		ensureVisible(x1, y1);
		ensureVisible(x2, y2);
	}

	public void pdfEllipse(double cx, double cy, double rx, double ry) {
		if (hidden)
			return;
		openDocumentIfNeeded();
		applyStroke();
		applyFill();
		// PdfContentByte.ellipse takes the bounding box, not center+radius.
		cb.ellipse((float) (cx - rx), (float) (cy - ry), (float) (cx + rx), (float) (cy + ry));
		paintPath();
		ensureVisible(cx + rx, cy + ry);
	}

	public void pdfPolygon(double... points) {
		if (points.length < 4 || (points.length & 1) != 0)
			return;
		if (hidden)
			return;
		openDocumentIfNeeded();
		applyStroke();
		applyFill();
		cb.moveTo((float) points[0], (float) points[1]);
		for (int i = 2; i < points.length; i += 2)
			cb.lineTo((float) points[i], (float) points[i + 1]);
		cb.closePath();
		paintPath();
		for (int i = 0; i < points.length; i += 2)
			ensureVisible(points[i], points[i + 1]);
	}

	// ----------------------------------------------------------------------
	// path drawing (newpath/moveto/.../fill)
	// ----------------------------------------------------------------------

	public void newpath() {
		openDocumentIfNeeded();
		applyStroke();
		applyFill();
		pathOpen = true;
	}

	public void moveto(double x, double y) {
		cb.moveTo((float) x, (float) y);
		ensureVisible(x, y);
	}

	public void lineto(double x, double y) {
		cb.lineTo((float) x, (float) y);
		ensureVisible(x, y);
	}

	public void closepath() {
		cb.closePath();
	}

	public void curveto(double x1, double y1, double x2, double y2, double x3, double y3) {
		cb.curveTo((float) x1, (float) y1, (float) x2, (float) y2, (float) x3, (float) y3);
		ensureVisible(x1, y1);
		ensureVisible(x2, y2);
		ensureVisible(x3, y3);
	}

	/**
	 * Quadratic bezier. PDF does not have a native quad operator, so we convert
	 * to a cubic on the fly using the standard formula
	 * {@code C1 = P0 + 2/3*(P1-P0), C2 = P2 + 2/3*(P1-P2)}.
	 * The previous current point on the path is needed - we accept it as input
	 * because we do not track the path cursor ourselves.
	 */
	public void quadto(double x0, double y0, double x1, double y1, double x2, double y2) {
		final double c1x = x0 + 2.0 / 3.0 * (x1 - x0);
		final double c1y = y0 + 2.0 / 3.0 * (y1 - y0);
		final double c2x = x2 + 2.0 / 3.0 * (x1 - x2);
		final double c2y = y2 + 2.0 / 3.0 * (y1 - y2);
		cb.curveTo((float) c1x, (float) c1y, (float) c2x, (float) c2y, (float) x2, (float) y2);
		ensureVisible(x1, y1);
		ensureVisible(x2, y2);
	}

	public void fill(int windingRule) {
		if (!pathOpen)
			return;
		pathOpen = false;
		final boolean f = hasFill();
		final boolean s = hasStroke();
		if (windingRule == PathIterator.WIND_EVEN_ODD) {
			if (f && s)
				cb.eoFillStroke();
			else if (f)
				cb.eoFill();
			else if (s)
				cb.stroke();
			else
				cb.newPath();
		} else {
			if (f && s)
				cb.fillStroke();
			else if (f)
				cb.fill();
			else if (s)
				cb.stroke();
			else
				cb.newPath();
		}
	}

	/**
	 * Replay a Java2D {@link PathIterator} into the current PDF stream.
	 * Used by the higher-level driver to handle arbitrary AWT shapes
	 * (including arcs already flattened to bezier by Java2D).
	 */
	public void drawPathIterator(double x, double y, PathIterator path) {
		newpath();
		final double[] coord = new double[6];
		double curX = 0, curY = 0;
		while (!path.isDone()) {
			final int code = path.currentSegment(coord);
			switch (code) {
			case PathIterator.SEG_MOVETO:
				moveto(coord[0] + x, coord[1] + y);
				curX = coord[0] + x;
				curY = coord[1] + y;
				break;
			case PathIterator.SEG_LINETO:
				lineto(coord[0] + x, coord[1] + y);
				curX = coord[0] + x;
				curY = coord[1] + y;
				break;
			case PathIterator.SEG_QUADTO:
				quadto(curX, curY, coord[0] + x, coord[1] + y, coord[2] + x, coord[3] + y);
				curX = coord[2] + x;
				curY = coord[3] + y;
				break;
			case PathIterator.SEG_CUBICTO:
				curveto(coord[0] + x, coord[1] + y, coord[2] + x, coord[3] + y, coord[4] + x, coord[5] + y);
				curX = coord[4] + x;
				curY = coord[5] + y;
				break;
			case PathIterator.SEG_CLOSE:
				closepath();
				break;
			default:
				throw new UnsupportedOperationException("code=" + code);
			}
			path.next();
		}
		fill(path.getWindingRule());
	}

	// ----------------------------------------------------------------------
	// text
	// ----------------------------------------------------------------------

	/**
	 * Render a single line of text at the (x, y) baseline position.
	 *
	 * @param fontFamily one of "Helvetica", "Times", "Courier" (case-insensitive)
	 *                   or a font name previously registered.
	 * @param fontSize   point size.
	 * @param bold       use bold weight.
	 * @param italic     use italic style.
	 * @param textLength target rendered width, or 0 for natural width. If &gt; 0
	 *                   we adjust the horizontal scaling to match exactly, which
	 *                   is the closest PDF equivalent to SVG's
	 *                   {@code lengthAdjust="spacingAndGlyphs"}.
	 */
	public void text(String text, double x, double y, String fontFamily, int fontSize, boolean bold, boolean italic,
			double textLength) {
		if (hidden)
			return;
		if (text == null || text.isEmpty())
			return;
		openDocumentIfNeeded();

		final BaseFont font = resolveFont(fontFamily, bold, italic);

		cb.beginText();
		cb.setColorFill(fillColor != null ? fillColor : Color.BLACK);
		cb.setFontAndSize(font, fontSize);

		// We previously flipped the global CTM so that Y grows downward like in
		// SVG. That makes the default text matrix render glyphs upside-down -
		// so we flip *back* in the text matrix only, using (1, 0, 0, -1, x, y).
		cb.setTextMatrix(1, 0, 0, -1, (float) x, (float) y);

		if (textLength > 0) {
			final float naturalWidth = font.getWidthPoint(text, fontSize);
			if (naturalWidth > 0) {
				final float scale = (float) (textLength / naturalWidth * 100.0);
				cb.setHorizontalScaling(scale);
			}
		}

		cb.showText(text);
		cb.endText();

		// Reset horizontal scaling for subsequent text runs.
		if (textLength > 0)
			cb.setHorizontalScaling(100);

		ensureVisible(x + (textLength > 0 ? textLength : font.getWidthPoint(text, fontSize)), y);
	}

	private BaseFont resolveFont(String family, boolean bold, boolean italic) {
		if (family == null)
			family = "Helvetica";

		final String key = family.toLowerCase() + (bold ? "-b" : "") + (italic ? "-i" : "");
		final BaseFont cached = fontCache.get(key);
		if (cached != null)
			return cached;

		final String baseName;
		final String lower = family.toLowerCase();
		if (lower.contains("courier") || lower.contains("mono")) {
			if (bold && italic)
				baseName = BaseFont.COURIER_BOLDOBLIQUE;
			else if (bold)
				baseName = BaseFont.COURIER_BOLD;
			else if (italic)
				baseName = BaseFont.COURIER_OBLIQUE;
			else
				baseName = BaseFont.COURIER;
		} else if (lower.contains("times") || lower.contains("serif")) {
			if (bold && italic)
				baseName = BaseFont.TIMES_BOLDITALIC;
			else if (bold)
				baseName = BaseFont.TIMES_BOLD;
			else if (italic)
				baseName = BaseFont.TIMES_ITALIC;
			else
				baseName = BaseFont.TIMES_ROMAN;
		} else {
			if (bold && italic)
				baseName = BaseFont.HELVETICA_BOLDOBLIQUE;
			else if (bold)
				baseName = BaseFont.HELVETICA_BOLD;
			else if (italic)
				baseName = BaseFont.HELVETICA_OBLIQUE;
			else
				baseName = BaseFont.HELVETICA;
		}

		try {
			final BaseFont bf = BaseFont.createFont(baseName, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
			fontCache.put(key, bf);
			currentFont = bf;
			return bf;
		} catch (DocumentException | IOException e) {
			throw new IllegalStateException("Cannot load font " + baseName, e);
		}
	}

	// ----------------------------------------------------------------------
	// images (PNG only in this first iteration)
	// ----------------------------------------------------------------------

	/**
	 * Insert a raster image (PNG/JPEG bytes) at position (x, y).
	 *
	 * @param pngOrJpegBytes encoded image data.
	 * @param x              top-left X.
	 * @param y              top-left Y.
	 * @param width          target render width.
	 * @param height         target render height.
	 */
	public void pdfImage(byte[] pngOrJpegBytes, double x, double y, double width, double height) {
		if (hidden)
			return;
		openDocumentIfNeeded();
		try {
			final Image img = Image.getInstance(pngOrJpegBytes);
			img.scaleAbsolute((float) width, (float) height);

			// Our CTM flips Y; an image placed via setAbsolutePosition would
			// land mirrored. We compensate by flipping the image cell locally:
			// translate to (x, y+height), then scale Y by -1.
			cb.saveState();
			cb.concatCTM(1, 0, 0, -1, (float) x, (float) (y + height));
			img.setAbsolutePosition(0, 0);
			cb.addImage(img);
			cb.restoreState();
		} catch (Exception e) {
			throw new IllegalStateException("Cannot embed image", e);
		}
		ensureVisible(x + width, y + height);
	}

	// ----------------------------------------------------------------------
	// links
	// ----------------------------------------------------------------------

	private static final class PendingLink {
		final String url;
		final String title;
		double llx = Double.POSITIVE_INFINITY;
		double lly = Double.POSITIVE_INFINITY;
		double urx = Double.NEGATIVE_INFINITY;
		double ury = Double.NEGATIVE_INFINITY;

		PendingLink(String url, String title) {
			this.url = url;
			this.title = title;
		}

		void expand(double x, double y) {
			if (x < llx)
				llx = x;
			if (y < lly)
				lly = y;
			if (x > urx)
				urx = x;
			if (y > ury)
				ury = y;
		}
	}

	/**
	 * Begin a clickable region. Everything drawn until the matching
	 * {@link #closeLink()} will be tracked, and a PDF link annotation will be
	 * emitted covering the union of all drawn bounding boxes.
	 *
	 * Unlike SVG, PDF link annotations are flat rectangles - we cannot nest
	 * them. Nested calls do behave correctly because we keep a stack of
	 * {@link PendingLink}, but they will produce sibling rectangles rather
	 * than truly nested clickable regions.
	 */
	public void openLink(String url, String title) {
		openDocumentIfNeeded();
		activeLinks.push(new PendingLink(url, title));
	}

	public void closeLink() {
		if (activeLinks.isEmpty())
			throw new IllegalStateException("closeLink() with no matching openLink()");
		final PendingLink link = activeLinks.pop();
		if (link.urx <= link.llx || link.ury <= link.lly)
			return; // nothing was drawn in this region
		final PdfAction action = new PdfAction(link.url);
		final PdfAnnotation annot = PdfAnnotation.createLink(writer,
				new Rectangle((float) link.llx, (float) link.lly, (float) link.urx, (float) link.ury),
				PdfAnnotation.HIGHLIGHT_INVERT, action);
		writer.addAnnotation(annot);
	}

	// ----------------------------------------------------------------------
	// output
	// ----------------------------------------------------------------------

	/**
	 * Flush the buffered drawing to the supplied stream as a complete PDF.
	 *
	 * The page size is computed from the union of every {@code ensureVisible}
	 * call, scaled by {@link PdfOption#getScale()}. After this call the
	 * instance must not be used anymore.
	 */
	public void createPdf(OutputStream os) throws IOException {
		openDocumentIfNeeded();

		final double scaledW = maxX * option.getScale();
		final double scaledH = maxY * option.getScale();

		writer.setCropBoxSize(new Rectangle((float) scaledW, (float) scaledH));
		document.setPageSize(new Rectangle((float) scaledW, (float) scaledH));

		document.close();
		buffer.writeTo(os);
	}

	// ----------------------------------------------------------------------
	// raw access (escape hatch for callers that need PdfContentByte)
	// ----------------------------------------------------------------------

	/**
	 * Direct access to the underlying OpenPDF content stream. Intended for
	 * advanced cases (custom shading patterns, form XObjects, ...).
	 * Most callers should never need this.
	 */
	public PdfContentByte getRawContent() {
		openDocumentIfNeeded();
		return cb;
	}

	public PdfWriter getRawWriter() {
		openDocumentIfNeeded();
		return writer;
	}

}
