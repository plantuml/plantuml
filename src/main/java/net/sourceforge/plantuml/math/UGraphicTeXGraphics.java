/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
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
package net.sourceforge.plantuml.math;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import org.scilab.forge.jlatexmath.FontInfo;
import org.scilab.forge.jlatexmath.GlyphOutlines;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXGraphics;

import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.awt.XColor;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.geom.USegment;

/**
 * The canvas of JLaTeXMath (see PLAN.md, phase 2) drawn on a {@link UGraphic}:
 * a formula is only paths, so that every output (SVG, PNG, PDF, EMF, the C
 * build...) gets it the same way, with no font and no Batik.
 * <p>
 * UGraphic only knows translations, so this class keeps its own affine
 * transformation (scale, rotate, translate of the boxes) and gives the shapes
 * to the UGraphic with their points already transformed. A glyph is a filled
 * path made from glyphs.bin, a line is a stroked path.
 * <p>
 * The origin of the formula is the origin of the {@code UGraphic} given to the
 * constructor.
 * <p>
 * The color of the formula is an {@link HColor}, given as is to the UGraphic
 * (which applies its color mapper, as for any shape). A color set by the boxes
 * (an AWT color, from \color) replaces it until it is set back.
 */
public class UGraphicTeXGraphics implements TeXGraphics {
	// ::remove file when __MIT__ or __EPL__ or __BSD__ or __ASL__ or __LGPL__

	/** Control point of a cubic curve that approximates a quarter of circle. */
	private static final double KAPPA = 0.5522847498307936;

	private final UGraphic ug;

	// x' = a x + c y + e ; y' = b x + d y + f
	private double a = 1;
	private double b = 0;
	private double c = 0;
	private double d = 1;
	private double e = 0;
	private double f = 0;

	/** Stands for the foreground HColor in the colors that the boxes save and restore. */
	private final Color foregroundMark = new Color(0, 0, 0);
	private final HColor foreground;
	private Color color = foregroundMark;
	private float lineWidth = 1;

	/**
	 * The glyphs that follow each other with the same color are gathered in one
	 * path: the rasterizer then fills their union. Drawn one by one, the parts
	 * that overlap (the pieces of an extensible arrow or brace) are painted
	 * twice, and the edge pixels that they share get darker.
	 */
	private UPath pendingGlyphs;
	private HColor pendingColor;

	private final List<double[]> savedTransforms = new ArrayList<double[]>();
	private final List<Float> savedStrokes = new ArrayList<Float>();

	public UGraphicTeXGraphics(UGraphic ug, HColor foreground) {
		this.ug = ug;
		this.foreground = foreground == null ? HColors.BLACK : foreground;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		if (color != this.color)
			flush();
		this.color = color;
	}

	public void setLineWidth(float width) {
		this.lineWidth = width;
	}

	public void saveStroke() {
		savedStrokes.add(Float.valueOf(lineWidth));
	}

	public void restoreStroke() {
		lineWidth = savedStrokes.remove(savedStrokes.size() - 1).floatValue();
	}

	// ------------------------------------------------------------------ shapes

	public void fillRect(float x, float y, float width, float height) {
		final UPath path = UPath.none();
		rect(path, x, y, width, height);
		fill(path);
	}

	public void drawRect(float x, float y, float width, float height) {
		final UPath path = UPath.none();
		rect(path, x, y, width, height);
		stroke(path);
	}

	public void drawRoundRect(float x, float y, float width, float height, float arcWidth, float arcHeight) {
		// As java.awt.geom.RoundRectangle2D: the arcs are the diameters of the corners
		final double rx = Math.min(Math.max(arcWidth, 0) / 2, width / 2);
		final double ry = Math.min(Math.max(arcHeight, 0) / 2, height / 2);
		final double x2 = x + width;
		final double y2 = y + height;
		final double kx = rx * KAPPA;
		final double ky = ry * KAPPA;
		final UPath path = UPath.none();
		moveTo(path, x + rx, y);
		lineTo(path, x2 - rx, y);
		cubicTo(path, x2 - rx + kx, y, x2, y + ry - ky, x2, y + ry);
		lineTo(path, x2, y2 - ry);
		cubicTo(path, x2, y2 - ry + ky, x2 - rx + kx, y2, x2 - rx, y2);
		lineTo(path, x + rx, y2);
		cubicTo(path, x + rx - kx, y2, x, y2 - ry + ky, x, y2 - ry);
		lineTo(path, x, y + ry);
		cubicTo(path, x, y + ry - ky, x + rx - kx, y, x + rx, y);
		stroke(path);
	}

	public void drawLine(float x1, float y1, float x2, float y2) {
		final UPath path = UPath.none();
		moveTo(path, x1, y1);
		lineTo(path, x2, y2);
		stroke(path);
	}

	public void drawChar(int fontId, char ch) {
		final FontInfo info = FontInfo.getFontInfo(fontId);
		final GlyphOutlines.GlyphFont glyphs = info == null ? null : info.getGlyphs();
		if (glyphs == null)
			return;
		// The AWT font was at PIXELS_PER_POINT * FONT_SCALE_FACTOR, 1 em
		final double k = TeXFormula.PIXELS_PER_POINT * TeXFormula.FONT_SCALE_FACTOR / glyphs.getUnitsPerEm();
		final UPath path = UPath.none();
		glyphs.visit(ch, new GlyphOutlines.Sink() {

			private double px;
			private double py;

			public void moveTo(int x, int y) {
				px = x * k;
				py = -y * k;
				UGraphicTeXGraphics.this.moveTo(path, px, py);
			}

			public void lineTo(int x, int y) {
				final double nx = x * k;
				final double ny = -y * k;
				// some fonts repeat a point: nothing to draw, and a shorter path
				if (nx == px && ny == py)
					return;
				px = nx;
				py = ny;
				UGraphicTeXGraphics.this.lineTo(path, px, py);
			}

			public void quadTo(int cx, int cy, int x, int y) {
				final double qx = cx * k;
				final double qy = -cy * k;
				final double ex = x * k;
				final double ey = -y * k;
				// quadratic to cubic: control points at 2/3 of the way to the control point
				UGraphicTeXGraphics.this.cubicTo(path, px + 2.0 / 3.0 * (qx - px), py + 2.0 / 3.0 * (qy - py),
						ex + 2.0 / 3.0 * (qx - ex), ey + 2.0 / 3.0 * (qy - ey), ex, ey);
				px = ex;
				py = ey;
			}

			public void close() {
				path.closePath();
			}
		});
		if (path.isEmpty())
			return;
		final HColor fore = current();
		if (pendingGlyphs != null && pendingColor.equals(fore) == false)
			flush();
		if (pendingGlyphs == null) {
			pendingGlyphs = UPath.none();
			pendingColor = fore;
		}
		for (USegment segment : path)
			pendingGlyphs.add(segment.getCoord(), segment.getSegmentType());
	}

	/**
	 * Draws the glyphs that are waiting. Done before anything else is drawn, so
	 * that the order of the drawing is kept, and at the end of the formula.
	 */
	public void flush() {
		if (pendingGlyphs == null)
			return;
		final UPath path = pendingGlyphs;
		final HColor fore = pendingColor;
		pendingGlyphs = null;
		pendingColor = null;
		ug.apply(HColors.transparent()).apply(fore.bg()).apply(UStroke.withThickness(0)).draw(path);
	}

	// ------------------------------------------------------------------ transformation

	public void translate(double tx, double ty) {
		e += a * tx + c * ty;
		f += b * tx + d * ty;
	}

	public void scale(double sx, double sy) {
		a *= sx;
		b *= sx;
		c *= sy;
		d *= sy;
	}

	public void rotate(double theta) {
		// StrictMath: the same result on every platform, so that the output is deterministic
		final double cos = StrictMath.cos(theta);
		final double sin = StrictMath.sin(theta);
		final double na = a * cos + c * sin;
		final double nb = b * cos + d * sin;
		final double nc = -a * sin + c * cos;
		final double nd = -b * sin + d * cos;
		a = na;
		b = nb;
		c = nc;
		d = nd;
	}

	public void rotate(double theta, double x, double y) {
		translate(x, y);
		rotate(theta);
		translate(-x, -y);
	}

	public double getScaleX() {
		return a;
	}

	public void saveTransform() {
		savedTransforms.add(new double[] { a, b, c, d, e, f });
	}

	public void restoreTransform() {
		final double[] m = savedTransforms.remove(savedTransforms.size() - 1);
		a = m[0];
		b = m[1];
		c = m[2];
		d = m[3];
		e = m[4];
		f = m[5];
	}

	public Graphics2D getGraphics2D() {
		return null;
	}

	// ------------------------------------------------------------------ helpers

	private void rect(UPath path, double x, double y, double width, double height) {
		moveTo(path, x, y);
		lineTo(path, x + width, y);
		lineTo(path, x + width, y + height);
		lineTo(path, x, y + height);
		lineTo(path, x, y);
		path.closePath();
	}

	private double tx(double x, double y) {
		return a * x + c * y + e;
	}

	private double ty(double x, double y) {
		return b * x + d * y + f;
	}

	private void moveTo(UPath path, double x, double y) {
		path.moveTo(tx(x, y), ty(x, y));
	}

	private void lineTo(UPath path, double x, double y) {
		path.lineTo(tx(x, y), ty(x, y));
	}

	private void cubicTo(UPath path, double x1, double y1, double x2, double y2, double x, double y) {
		path.cubicTo(tx(x1, y1), ty(x1, y1), tx(x2, y2), ty(x2, y2), tx(x, y), ty(x, y));
	}

	private HColor current() {
		if (color == foregroundMark)
			return foreground;
		return HColors.simple(new XColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()));
	}

	/** Filled with the current color, no outline. */
	private void fill(UPath path) {
		flush();
		final HColor fore = current();
		ug.apply(HColors.transparent()).apply(fore.bg()).apply(UStroke.withThickness(0)).draw(path);
	}

	/** Outline only, with the current color and line width (scaled as the lines were). */
	private void stroke(UPath path) {
		flush();
		final double scale = Math.sqrt(Math.abs(a * d - b * c));
		ug.apply(current()).apply(HColors.transparent().bg()).apply(UStroke.withThickness(lineWidth * scale)).draw(path);
	}
}
