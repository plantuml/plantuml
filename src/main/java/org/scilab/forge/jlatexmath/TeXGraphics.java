/* TeXGraphics.java
 * =========================================================================
 * This file is NOT part of the upstream JLaTeXMath library: it was added by
 * PlantUML (phase 2 of PLAN.md). See readme.md in this directory.
 * =========================================================================
 */

package org.scilab.forge.jlatexmath;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * The small drawing surface that the boxes need, in place of
 * {@link java.awt.Graphics2D}.
 * <p>
 * It is exactly the subset of Graphics2D that {@link Box#draw} and its
 * subclasses use: rectangles, rounded rectangle, lines, one glyph at the origin,
 * the current color, the line width (butt caps, miter joins) and the
 * transformation (translate, scale, rotate, save and restore).
 */
public interface TeXGraphics {

	Color getColor();

	void setColor(Color color);

	/** Sets the width of the lines, with butt caps and miter joins. */
	void setLineWidth(float width);

	/** Saves the current line settings, to be given back by {@link #restoreStroke()}. */
	void saveStroke();

	void restoreStroke();

	void fillRect(float x, float y, float width, float height);

	void drawRect(float x, float y, float width, float height);

	void drawRoundRect(float x, float y, float width, float height, float arcWidth, float arcHeight);

	void drawLine(float x1, float y1, float x2, float y2);

	/**
	 * Draws one glyph with its origin (baseline, left) at the current origin.
	 * The size is given by the current scale: one unit is
	 * {@link TeXFormula#FONT_SCALE_FACTOR} font units.
	 */
	void drawChar(int fontId, char c);

	void translate(double tx, double ty);

	void scale(double sx, double sy);

	void rotate(double theta);

	void rotate(double theta, double x, double y);

	/** The horizontal scale factor of the current transformation. */
	double getScaleX();

	/** Saves the current transformation, to be given back by {@link #restoreTransform()}. */
	void saveTransform();

	void restoreTransform();

	/**
	 * The underlying AWT graphics, or null if this canvas is not made on AWT. The
	 * few boxes that cannot be drawn without AWT (images, Java font rendering,
	 * GeoGebra logo, Fcscore) draw nothing when it is null.
	 */
	Graphics2D getGraphics2D();

}
