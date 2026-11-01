/* Graphics2DTeXGraphics.java
 * =========================================================================
 * This file is NOT part of the upstream JLaTeXMath library: it was added by
 * PlantUML (phase 2 of PLAN.md). See readme.md in this directory.
 * =========================================================================
 */

package org.scilab.forge.jlatexmath;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link TeXGraphics} on top of an AWT {@link Graphics2D}: each method is the
 * call that the boxes used to make directly.
 */
public class Graphics2DTeXGraphics implements TeXGraphics {

	private final Graphics2D g2;
	private final List<AffineTransform> saved = new ArrayList<AffineTransform>();
	private final List<Stroke> savedStrokes = new ArrayList<Stroke>();
	private final char[] arr = new char[1];

	public Graphics2DTeXGraphics(Graphics2D g2) {
		this.g2 = g2;
	}

	public Color getColor() {
		return g2.getColor();
	}

	public void setColor(Color color) {
		g2.setColor(color);
	}

	public void setLineWidth(float width) {
		g2.setStroke(new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
	}

	public void saveStroke() {
		savedStrokes.add(g2.getStroke());
	}

	public void restoreStroke() {
		g2.setStroke(savedStrokes.remove(savedStrokes.size() - 1));
	}

	public void fillRect(float x, float y, float width, float height) {
		g2.fill(new Rectangle2D.Float(x, y, width, height));
	}

	public void drawRect(float x, float y, float width, float height) {
		g2.draw(new Rectangle2D.Float(x, y, width, height));
	}

	public void drawRoundRect(float x, float y, float width, float height, float arcWidth, float arcHeight) {
		g2.draw(new RoundRectangle2D.Float(x, y, width, height, arcWidth, arcHeight));
	}

	public void drawLine(float x1, float y1, float x2, float y2) {
		g2.draw(new Line2D.Float(x1, y1, x2, y2));
	}

	public void drawChar(int fontId, char c) {
		final Font font = FontInfo.getFont(fontId);
		if (g2.getFont() != font)
			g2.setFont(font);
		arr[0] = c;
		g2.drawChars(arr, 0, 1, 0, 0);
	}

	public void translate(double tx, double ty) {
		g2.translate(tx, ty);
	}

	public void scale(double sx, double sy) {
		g2.scale(sx, sy);
	}

	public void rotate(double theta) {
		g2.rotate(theta);
	}

	public void rotate(double theta, double x, double y) {
		g2.rotate(theta, x, y);
	}

	public double getScaleX() {
		return g2.getTransform().getScaleX();
	}

	public void saveTransform() {
		saved.add(g2.getTransform());
	}

	public void restoreTransform() {
		g2.setTransform(saved.remove(saved.size() - 1));
	}

	public Graphics2D getGraphics2D() {
		return g2;
	}

}
