/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2026, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 *
 * 
 */
package net.sourceforge.plantuml.openpdf;

import java.awt.Color;
import java.awt.geom.PathIterator;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;

import org.openpdf.text.Document;
import org.openpdf.text.Image;
import org.openpdf.text.Rectangle;
import org.openpdf.text.pdf.BaseFont;
import org.openpdf.text.pdf.PdfAction;
import org.openpdf.text.pdf.PdfAnnotation;
import org.openpdf.text.pdf.PdfContentByte;
import org.openpdf.text.pdf.PdfTemplate;
import org.openpdf.text.pdf.PdfWriter;

import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.color.HColor.TransparentFillBehavior;
import net.sourceforge.plantuml.klimt.geom.USegment;
import net.sourceforge.plantuml.klimt.geom.USegmentType;

/**
 * PDF backend that mirrors the public API of
 * {@code net.sourceforge.plantuml.klimt.drawing.svg.SvgGraphics}.
 *
 * <h2>Coordinate convention</h2>
 *
 * PDF natively has Y growing upwards from a bottom-left origin; SVG and
 * PlantUML have Y growing downwards from a top-left origin. Rather than
 * flipping the CTM (which forces every text run and image to compensate with a
 * local flip), this driver keeps PDF's native Y-up convention and simply
 * <strong>negates Y in input</strong>: a caller-supplied SVG-coord
 * {@code (x, y)} is drawn at PDF-coord {@code (x, -y)}.
 *
 * <p>
 * For shapes that take a top-left corner plus a size (rectangles, raster
 * images), the bottom-left in PDF coords becomes {@code (x, -(y + height))}.
 * For point-like primitives (line endpoints, polygon vertices, ellipse centers,
 * text baselines, path segments) it is simply {@code (x, -y)}.
 *
 * <h2>From negative-Y back to a normal page</h2>
 *
 * The content is buffered into a {@link PdfTemplate} (a PDF form XObject) while
 * we draw. The template is unbounded - it can hold negative-Y content without
 * complaining. At {@link #createPdf(OutputStream)} time we know the final
 * extent ({@code maxX}, {@code maxY} in SVG coords): we open a page of size
 * {@code maxX x maxY} and place the template translated by {@code (0, maxY)} so
 * the content lands inside {@code [0, 0, maxX, maxY]}.
 *
 * <p>
 * Net result for callers: SVG-style coords on the way in, a standard-shape page
 * on the way out, no CTM flip anywhere, no per-call compensation for text or
 * images.
 *
 * <h2>Helper {@link #ny(double)}</h2>
 *
 * All internal conversions go through {@link #ny(double)} (read it as "negate
 * y"). If anything ever looks upside-down or misplaced in a generated PDF, the
 * one place to look is whether a coordinate took the {@code ny()} route or
 * leaked through as a positive PDF coord.
 *
 * <h2>Deliberately omitted in v1</h2>
 *
 * Drop shadows, embedded SVG images (stdlib icons), gradients, text-background
 * filters, rotated text, CSS classes, interactive scripts, dark mode.
 *
 * <h2>Not for TeaVM</h2>
 *
 * OpenPDF depends on {@code java.awt} and {@code java.util.zip}; this driver is
 * server-side only and is excluded from the TeaVM build path.
 *
 * <h2>OpenPDF version</h2>
 *
 * Targets OpenPDF 3.0+ which uses the {@code org.openpdf} package namespace.
 * The legacy {@code com.lowagie} packages have been removed in 3.0 and are not
 * available anymore.
 */
public class PdfGraphics {
	// ::remove folder when JAVA8

	// ---------- output buffering ----------

	private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	private final Document document;
	private final PdfWriter writer;
	private PdfTemplate template;
	private PdfContentByte cb;
	private boolean documentOpened = false;

	// ---------- options ----------

	private final PdfOption option;

	// ---------- bounding box tracking (in caller-facing SVG coords, Y > 0) ----

	private double maxX;
	private double maxY;

	// ---------- current graphics state ----------

	private Color fillColor = Color.BLACK;
	private Color strokeColor = Color.BLACK;
	private double strokeWidth = 1.0;
	private float[] strokeDash = null;
	private boolean hidden = false;
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

		// We do not know the final page size yet. Open with an arbitrary
		// placeholder (we'll fix the actual page size in createPdf, before
		// document.open()). All drawing goes into a PdfTemplate which is
		// unbounded - it can hold negative-Y content without truncation.
		this.document = new Document(new Rectangle(10, 10));
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

		// Page size will be patched in createPdf. Open document with the
		// placeholder; we draw exclusively into the template so the page's
		// own size does not constrain us.
		//
		// IMPORTANT: the template has a *bounding box* and content outside
		// that box is clipped by the viewer. Since we draw in negative Y,
		// the bbox must extend into negative Y too. We set it to a generous
		// 10000 x 10000 region covering [0, 10000] x [-10000, 0]; createPdf
		// later tightens it to the actual content bounds.
		document.open();
		final PdfContentByte direct = writer.getDirectContent();
		this.template = direct.createTemplate(10000, 10000);
		this.template.setBoundingBox(new Rectangle(0f, -10000f, 10000f, 0f));
		this.cb = template;
	}

	// ----------------------------------------------------------------------
	// the one-line "negate Y" helper
	// ----------------------------------------------------------------------

	/**
	 * Convert a caller-facing SVG Y (positive, grows downward) into the PDF Y we
	 * actually emit (negative, drawn below the y=0 axis).
	 */
	private static float ny(double svgY) {
		return (float) -svgY;
	}

	// ----------------------------------------------------------------------
	// bounding box
	// ----------------------------------------------------------------------

	/**
	 * Tracked in SVG coords (positive Y). {@code maxX} and {@code maxY} are the
	 * largest values the caller has drawn at; the final page will be a
	 * {@code maxX x maxY} rectangle in standard PDF coords.
	 */
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

	public final void setFillColor(Color fill, TransparentFillBehavior transparentFillBehaviour) {
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

	private void paintPath() {
		final boolean f = hasFill();
		final boolean s = hasStroke();
		if (f && s)
			cb.fillStroke();
		else if (f)
			cb.fill();
		else if (s)
			cb.stroke();
		else
			cb.newPath();
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
		// PDF rectangle takes its lower-left corner. In our (Y-negated) world,
		// the SVG top-left (x, y) becomes the PDF *upper*-left at (x, -y),
		// which means the lower-left is at (x, -y - height) = (x, -(y+height)).
		if (rx > 0 && ry > 0)
			cb.roundRectangle((float) x, ny(y + height), (float) width, (float) height, (float) Math.min(rx, ry));
		else
			cb.rectangle((float) x, ny(y + height), (float) width, (float) height);
		paintPath();
		ensureVisible(x + width, y + height);
	}

	public void pdfLine(double x1, double y1, double x2, double y2) {
		if (hidden)
			return;
		openDocumentIfNeeded();
		applyStroke();
		cb.moveTo((float) x1, ny(y1));
		cb.lineTo((float) x2, ny(y2));
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
		// PdfContentByte.ellipse takes a bounding box (x1, y1, x2, y2). With
		// Y negated, the box runs from (cx - rx, -cy - ry) to (cx + rx, -cy + ry).
		cb.ellipse((float) (cx - rx), ny(cy + ry), (float) (cx + rx), ny(cy - ry));
		paintPath();
		ensureVisible(cx + rx, cy + ry);
	}

	public void pdfArcEllipse(double rx, double ry, double x1, double y1, double x2, double y2) {
		if (hidden)
			return;
		openDocumentIfNeeded();

		applyStroke();
		applyFill();

		// Mirrors the SVG path "M x1,y1 A rx,ry 0 0 0 x2,y2" emitted by the
		// original SvgGraphics.svgArcEllipse: xAxisRotation=0, largeArc=0,
		// sweep=0. DriverEllipsePdf has already picked which endpoint goes
		// first based on the sign of `extend`, so we can keep flags fixed.
		cb.moveTo((float) x1, ny(y1));
		arcToCubics(x1, y1, rx, ry, /* phiDeg */ 0, /* largeArc */ false, /* sweep */ false, x2, y2);
		paintPath();

		ensureVisible(x1, y1);
		ensureVisible(x2, y2);
	}

	public void pdfPolygon(double... points) {
		if (points.length < 4 || (points.length & 1) != 0)
			return;
		if (hidden)
			return;
		openDocumentIfNeeded();
		applyStroke();
		applyFill();
		cb.moveTo((float) points[0], ny(points[1]));
		for (int i = 2; i < points.length; i += 2)
			cb.lineTo((float) points[i], ny(points[i + 1]));
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
		cb.moveTo((float) x, ny(y));
		ensureVisible(x, y);
	}

	public void lineto(double x, double y) {
		cb.lineTo((float) x, ny(y));
		ensureVisible(x, y);
	}

	public void closepath() {
		cb.closePath();
	}

	public void curveto(double x1, double y1, double x2, double y2, double x3, double y3) {
		cb.curveTo((float) x1, ny(y1), (float) x2, ny(y2), (float) x3, ny(y3));
		ensureVisible(x1, y1);
		ensureVisible(x2, y2);
		ensureVisible(x3, y3);
	}

	/**
	 * Quadratic bezier. PDF does not have a native quad operator, so we convert to
	 * a cubic on the fly using the standard formula
	 * {@code C1 = P0 + 2/3*(P1-P0), C2 = P2 + 2/3*(P1-P2)}. The previous current
	 * point on the path is needed - we accept it as input because we do not track
	 * the path cursor ourselves.
	 *
	 * Note: the conversion is done in SVG coords for clarity, then Y is negated at
	 * the very last moment when calling curveTo. Negating before or after the
	 * convex combination gives the same result mathematically, but doing it last
	 * keeps the formula readable.
	 */
	public void quadto(double x0, double y0, double x1, double y1, double x2, double y2) {
		final double c1x = x0 + 2.0 / 3.0 * (x1 - x0);
		final double c1y = y0 + 2.0 / 3.0 * (y1 - y0);
		final double c2x = x2 + 2.0 / 3.0 * (x1 - x2);
		final double c2y = y2 + 2.0 / 3.0 * (y1 - y2);
		cb.curveTo((float) c1x, ny(c1y), (float) c2x, ny(c2y), (float) x2, ny(y2));
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
	 * Replay a Java2D {@link PathIterator} into the current PDF stream. Used by the
	 * higher-level driver to handle arbitrary AWT shapes (including arcs already
	 * flattened to bezier by Java2D).
	 *
	 * All coordinates go through the regular path API ({@link #moveto},
	 * {@link #lineto}, {@link #quadto}, {@link #curveto}), so Y negation happens
	 * transparently there.
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
	 * With Y negation, the default PDF text matrix (which renders glyphs
	 * right-side-up) is what we want - no per-call flip is needed. We just use
	 * {@link PdfContentByte#setTextMatrix(float, float)} to place the baseline at
	 * {@code (x, -y)}.
	 */
	public void pdfText(String text, double x, double y, BaseFont font, int fontSize) {
		if (hidden)
			return;
		if (text == null || text.isEmpty())
			return;
		openDocumentIfNeeded();

		cb.beginText();
		cb.setColorFill(fillColor != null ? fillColor : Color.BLACK);
		cb.setFontAndSize(font, fontSize);

		// Default text matrix (identity, glyphs upright). Just place the
		// baseline at (x, -y).
		cb.setTextMatrix((float) x, ny(y));

		cb.showText(text);
		cb.endText();

		ensureVisible(x + font.getWidthPoint(text, fontSize), y);
	}

	// ----------------------------------------------------------------------
	// images (PNG / JPEG)
	// ----------------------------------------------------------------------

	/**
	 * Insert a raster image (PNG/JPEG bytes) at position (x, y).
	 *
	 * With Y negation, the image's bottom-left in PDF coords lands at
	 * {@code (x, -(y + height))} - same formula as for rectangles. No local matrix
	 * flip is needed; the image stays right-side-up.
	 *
	 * @param pngOrJpegBytes encoded image data.
	 * @param x              top-left X (SVG convention).
	 * @param y              top-left Y (SVG convention).
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
			img.setAbsolutePosition((float) x, ny(y + height));
			cb.addImage(img);
		} catch (Exception e) {
			throw new IllegalStateException("Cannot embed image", e);
		}
		ensureVisible(x + width, y + height);
	}

	// ----------------------------------------------------------------------
	// links
	// ----------------------------------------------------------------------

	/**
	 * A pending link region. Bounds are tracked in <em>PDF</em> coords (Y already
	 * negated) because the {@link PdfAnnotation} we eventually emit must match the
	 * page coordinate system.
	 */
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

		void expand(double x, double pdfY) {
			if (x < llx)
				llx = x;
			if (pdfY < lly)
				lly = pdfY;
			if (x > urx)
				urx = x;
			if (pdfY > ury)
				ury = pdfY;
		}
	}

	/**
	 * Begin a clickable region. Currently the bounding box of the link is driven
	 * externally (callers must drive {@link #ensureVisible} - the higher-level
	 * UGraphic driver does that anyway). A future iteration may auto-track inside
	 * {@code openLink/closeLink} by intercepting every drawing primitive between
	 * the two calls.
	 *
	 * Unlike SVG, PDF link annotations are flat rectangles - we cannot nest them.
	 * Nested calls do behave correctly because we keep a stack of
	 * {@link PendingLink}, but they will produce sibling rectangles rather than
	 * truly nested clickable regions.
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
	 * Pipeline:
	 * <ol>
	 * <li>Compute the final page size from {@code maxX} and {@code maxY}.</li>
	 * <li>Switch the document's page size to {@code maxX x maxY} and start a new
	 * page (the placeholder page we opened with is replaced).</li>
	 * <li>Place the {@link PdfTemplate} on that page with a translation of
	 * {@code (0, maxY)}: our negative-Y content moves into
	 * {@code [0, 0, maxX, maxY]} in page coords.</li>
	 * </ol>
	 *
	 * After this call the instance must not be used anymore.
	 */
	public void createPdf(OutputStream os) throws IOException {
		openDocumentIfNeeded();

		final float scaledW = (float) (maxX * option.getScale());
		final float scaledH = (float) (maxY * option.getScale());

		// Tighten the template's bounding box to the actual content extent.
		// Our content lives in [0, maxX] x [-maxY, 0].
		template.setBoundingBox(new Rectangle(0f, -scaledH, scaledW, 0f));

		// Switch to the real page size. setPageSize affects subsequent pages,
		// so we call newPage() to flush the placeholder page and start a new
		// one with the proper dimensions.
		final Rectangle pageSize = new Rectangle(scaledW, scaledH);
		document.setPageSize(pageSize);
		document.newPage();

		// Place the template, translated up by maxY so the content (which
		// lives in negative Y inside the template) lands in [0, 0, w, h].
		final PdfContentByte pageContent = writer.getDirectContent();
		pageContent.addTemplate(template, 0, scaledH);

		document.close();
		buffer.writeTo(os);
	}

	// ----------------------------------------------------------------------
	// raw access (escape hatch for callers that need PdfContentByte)
	// ----------------------------------------------------------------------

	/**
	 * Direct access to the underlying OpenPDF content stream. Intended for advanced
	 * cases (custom shading patterns, form XObjects, ...). Most callers should
	 * never need this.
	 *
	 * <strong>Coordinate warning:</strong> raw drawing through this stream uses
	 * native PDF coords (Y-up). Either work entirely in PDF coords, or negate Y
	 * yourself with the same convention this class uses ({@code pdfY = -svgY}).
	 */
	public PdfContentByte getRawContent() {
		openDocumentIfNeeded();
		return cb;
	}

	public PdfWriter getRawWriter() {
		openDocumentIfNeeded();
		return writer;
	}

	// ----------------------------------------------------------------------
	// UPath rendering
	// ----------------------------------------------------------------------

	/**
	 * Render a {@link UPath} (PlantUML's SVG-flavored path representation)
	 * translated by {@code (x, y)}.
	 *
	 * Each {@link USegment} maps to a native PDF path operator, except
	 * {@link USegmentType#SEG_ARCTO} which is converted on the fly to a sequence of
	 * cubic Beziers (PDF has no arc operator). The conversion follows the SVG 1.1
	 * Appendix F.6 endpoint-to-center parametrization and approximates each ~90
	 * degree slice with one cubic.
	 *
	 * <p>
	 * Path metadata ({@code comment}, {@code codeLine}) is currently ignored: PDF
	 * has no clean place to expose it (no element id like SVG). A future iteration
	 * could emit it as a marked-content tag if useful for accessibility or tooling.
	 */
	public void pdfPath(double x, double y, UPath path) {
		if (hidden)
			return;
		openDocumentIfNeeded();
		ensureVisible(x, y);

		applyStroke();
		applyFill();

		// Track the current point so we can feed quad-to-cubic conversion and
		// the arc converter, which both need the previous endpoint.
		double curX = x;
		double curY = y;

		for (USegment seg : path) {
			final USegmentType type = seg.getSegmentType();
			final double[] c = seg.getCoord();

			if (type == USegmentType.SEG_MOVETO) {
				final double px = c[0] + x;
				final double py = c[1] + y;
				cb.moveTo((float) px, ny(py));
				ensureVisible(px, py);
				curX = px;
				curY = py;

			} else if (type == USegmentType.SEG_LINETO) {
				final double px = c[0] + x;
				final double py = c[1] + y;
				cb.lineTo((float) px, ny(py));
				ensureVisible(px, py);
				curX = px;
				curY = py;

			} else if (type == USegmentType.SEG_QUADTO) {
				// UPath.quadTo stores the control point twice; we use one pair
				// as the quadratic control and convert to a cubic for PDF.
				final double cx1 = c[0] + x;
				final double cy1 = c[1] + y;
				final double px = c[4] + x;
				final double py = c[5] + y;
				quadToInternal(curX, curY, cx1, cy1, px, py);
				ensureVisible(px, py);
				curX = px;
				curY = py;

			} else if (type == USegmentType.SEG_CUBICTO) {
				final double cx1 = c[0] + x;
				final double cy1 = c[1] + y;
				final double cx2 = c[2] + x;
				final double cy2 = c[3] + y;
				final double px = c[4] + x;
				final double py = c[5] + y;
				cb.curveTo((float) cx1, ny(cy1), (float) cx2, ny(cy2), (float) px, ny(py));
				ensureVisible(px, py);
				curX = px;
				curY = py;

			} else if (type == USegmentType.SEG_ARCTO) {
				// SVG arc: [rx, ry, xAxisRotation(deg), largeArcFlag, sweepFlag, x, y]
				final double rx = c[0];
				final double ry = c[1];
				final double xAxisRotDeg = c[2];
				final boolean largeArc = c[3] != 0;
				final boolean sweep = c[4] != 0;
				final double endX = c[5] + x;
				final double endY = c[6] + y;
				arcToCubics(curX, curY, rx, ry, xAxisRotDeg, largeArc, sweep, endX, endY);
				ensureVisible(endX, endY);
				curX = endX;
				curY = endY;

			} else if (type == USegmentType.SEG_CLOSE) {
				cb.closePath();
			}
			// Unknown segment types are silently ignored - UPath's enum is
			// closed, so we should never get here in practice.
		}

		paintPath();
	}

	/**
	 * Internal helper: emit a quadratic as a cubic. Same math as the public
	 * {@link #quadto} method, kept private here to avoid disturbing the
	 * path-building state machine (no {@code newpath}/{@code fill} dance).
	 */
	private void quadToInternal(double x0, double y0, double cx, double cy, double x2, double y2) {
		final double c1x = x0 + 2.0 / 3.0 * (cx - x0);
		final double c1y = y0 + 2.0 / 3.0 * (cy - y0);
		final double c2x = x2 + 2.0 / 3.0 * (cx - x2);
		final double c2y = y2 + 2.0 / 3.0 * (cy - y2);
		cb.curveTo((float) c1x, ny(c1y), (float) c2x, ny(c2y), (float) x2, ny(y2));
	}

	// ----------------------------------------------------------------------
	// SVG arc -> cubic Bezier conversion
	// ----------------------------------------------------------------------

	/**
	 * Convert an SVG-style elliptical arc into a sequence of cubic Beziers and emit
	 * them via {@code cb.curveTo}. Follows SVG 1.1 Appendix F.6 (endpoint-to-center
	 * parametrization) and approximates each ~90 degree slice of the sweep with a
	 * single cubic using the standard 4/3*tan(a/4) control-point distance.
	 *
	 * @param x1           start point X (already translated, in caller coords)
	 * @param y1           start point Y
	 * @param rxIn         radius along the X axis of the ellipse (before rotation)
	 * @param ryIn         radius along the Y axis
	 * @param phiDeg       rotation of the ellipse's X axis, in degrees
	 * @param largeArcFlag SVG large-arc flag
	 * @param sweepFlag    SVG sweep flag
	 * @param x2           end point X
	 * @param y2           end point Y
	 */
	private void arcToCubics(double x1, double y1, double rxIn, double ryIn, double phiDeg, boolean largeArcFlag,
			boolean sweepFlag, double x2, double y2) {

		// Degenerate cases per SVG spec F.6.2:
		// - identical endpoints: do nothing
		// - either radius zero: straight line
		if (x1 == x2 && y1 == y2)
			return;
		if (rxIn == 0 || ryIn == 0) {
			cb.lineTo((float) x2, ny(y2));
			return;
		}

		double rx = Math.abs(rxIn);
		double ry = Math.abs(ryIn);
		final double phi = Math.toRadians(phiDeg);
		final double cosPhi = Math.cos(phi);
		final double sinPhi = Math.sin(phi);

		// Step 1: compute (x1', y1') in the rotated coordinate system.
		final double dx = (x1 - x2) / 2.0;
		final double dy = (y1 - y2) / 2.0;
		final double x1p = cosPhi * dx + sinPhi * dy;
		final double y1p = -sinPhi * dx + cosPhi * dy;

		// Step 2: if the radii are too small to reach both endpoints, scale them
		// up uniformly per SVG F.6.6.2.
		final double lambda = (x1p * x1p) / (rx * rx) + (y1p * y1p) / (ry * ry);
		if (lambda > 1.0) {
			final double s = Math.sqrt(lambda);
			rx *= s;
			ry *= s;
		}

		// Step 3: center (cx', cy') in the rotated system.
		final double rx2 = rx * rx;
		final double ry2 = ry * ry;
		final double x1p2 = x1p * x1p;
		final double y1p2 = y1p * y1p;

		double numerator = rx2 * ry2 - rx2 * y1p2 - ry2 * x1p2;
		final double denominator = rx2 * y1p2 + ry2 * x1p2;
		if (numerator < 0)
			numerator = 0;
		double coef = Math.sqrt(numerator / denominator);
		if (largeArcFlag == sweepFlag)
			coef = -coef;
		final double cxp = coef * (rx * y1p) / ry;
		final double cyp = coef * -(ry * x1p) / rx;

		// Step 4: center (cx, cy) in the original system.
		final double mx = (x1 + x2) / 2.0;
		final double my = (y1 + y2) / 2.0;
		final double cx = cosPhi * cxp - sinPhi * cyp + mx;
		final double cy = sinPhi * cxp + cosPhi * cyp + my;

		// Step 5: start angle theta1 and sweep deltaTheta.
		final double ux = (x1p - cxp) / rx;
		final double uy = (y1p - cyp) / ry;
		final double vx = (-x1p - cxp) / rx;
		final double vy = (-y1p - cyp) / ry;

		final double theta1 = angleBetween(1, 0, ux, uy);
		double deltaTheta = angleBetween(ux, uy, vx, vy);
		if (!sweepFlag && deltaTheta > 0)
			deltaTheta -= 2 * Math.PI;
		else if (sweepFlag && deltaTheta < 0)
			deltaTheta += 2 * Math.PI;

		// Step 6: split sweep into <= 90 degree slices and approximate each
		// with one cubic Bezier. The standard approximation uses control
		// distance alpha = (4/3) * tan(slice/4).
		final int nSlices = (int) Math.ceil(Math.abs(deltaTheta) / (Math.PI / 2.0));
		final double slice = deltaTheta / nSlices;
		final double alpha = (4.0 / 3.0) * Math.tan(slice / 4.0);

		double t1 = theta1;
		double cos1 = Math.cos(t1);
		double sin1 = Math.sin(t1);
		double curX = x1;
		double curY = y1;

		for (int i = 0; i < nSlices; i++) {
			final double t2 = t1 + slice;
			final double cos2 = Math.cos(t2);
			final double sin2 = Math.sin(t2);

			// Endpoint of this slice on the unit-circle parametrization,
			// then mapped back to the rotated ellipse.
			final double ex = cosPhi * (rx * cos2) - sinPhi * (ry * sin2) + cx;
			final double ey = sinPhi * (rx * cos2) + cosPhi * (ry * sin2) + cy;

			// Control points: tangents at t1 and t2, scaled by alpha.
			final double c1ux = -rx * sin1 * alpha;
			final double c1uy = ry * cos1 * alpha;
			final double c2ux = rx * sin2 * alpha;
			final double c2uy = -ry * cos2 * alpha;

			final double c1x = curX + (cosPhi * c1ux - sinPhi * c1uy);
			final double c1y = curY + (sinPhi * c1ux + cosPhi * c1uy);
			final double c2x = ex + (cosPhi * c2ux - sinPhi * c2uy);
			final double c2y = ey + (sinPhi * c2ux + cosPhi * c2uy);

			cb.curveTo((float) c1x, ny(c1y), (float) c2x, ny(c2y), (float) ex, ny(ey));

			t1 = t2;
			cos1 = cos2;
			sin1 = sin2;
			curX = ex;
			curY = ey;
		}
	}

	/**
	 * Signed angle between two 2D vectors, in radians, in {@code (-pi, pi]}. Used
	 * internally by {@link #arcToCubics}.
	 */
	private static double angleBetween(double ux, double uy, double vx, double vy) {
		final double dot = ux * vx + uy * vy;
		final double lenU = Math.sqrt(ux * ux + uy * uy);
		final double lenV = Math.sqrt(vx * vx + vy * vy);
		double cos = dot / (lenU * lenV);
		if (cos < -1)
			cos = -1;
		else if (cos > 1)
			cos = 1;
		final double sign = (ux * vy - uy * vx) < 0 ? -1 : 1;
		return sign * Math.acos(cos);
	}

}
