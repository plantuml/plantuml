/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
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
 */
package net.sourceforge.plantuml.klimt.drawing.raster;

import net.sourceforge.plantuml.klimt.font.UFont;

/**
 * A raster surface that {@link UGraphicRaster} draws on, and encodes as PNG.
 *
 * <p>
 * It follows the conventions of Java2D, which {@code UGraphicG2d} uses for the
 * PNG output of the JVM: coordinates in pixels with y going down, scaled by
 * {@link #scale}; colors as 0xAARRGGBB; strokes as {@code java.awt.BasicStroke};
 * text drawn with its baseline at y. Paint, stroke and clip are a state, kept
 * until changed, as in a {@code Graphics2D}.
 *
 * <p>
 * The TeaVM C build implements it with a native library (no AWT); the tests
 * implement it with Java2D, to check the drivers against {@code UGraphicG2d}.
 */
public interface RasterCanvas {

	/**
	 * Creates the canvas of a PNG image, transparent.
	 */
	public interface Factory {
		/**
		 * @param opaque the image will get an opaque background: PlantUML then
		 *               writes it without alpha channel (as EmptyImageBuilder
		 *               creates an RGB image)
		 */
		public RasterCanvas create(int width, int height, boolean opaque);
	}

	public static final int SPREAD_PAD = 0;
	public static final int SPREAD_REFLECT = 1;
	public static final int SPREAD_REPEAT = 2;

	/**
	 * Scales everything drawn afterwards. Called once, before drawing.
	 */
	public void scale(double factor);

	public void setColor(int argb);

	/**
	 * A linear gradient from (x1, y1) to (x2, y2), with one color (0xAARRGGBB)
	 * per offset (from 0 to 1, increasing), and a SPREAD_xxx method.
	 */
	public void setLinearGradient(double x1, double y1, double x2, double y2, double[] offsets, int[] colors,
			int spread);

	/**
	 * Like {@code java.awt.BasicStroke}: without dashes (dashVisible = 0), square
	 * caps; with dashes, butt caps; miter joins (limit 10) in both cases.
	 */
	public void setStroke(double thickness, double dashVisible, double dashSpace);

	public void setClip(double x, double y, double width, double height);

	public void resetClip();

	public void fill(RasterPath path);

	public void stroke(RasterPath path);

	/**
	 * Draws an image, given as 0xAARRGGBB pixels (not premultiplied) row by row,
	 * with its top left corner at (x, y), each of its pixels being {@code scale}
	 * units wide (1 copies the pixels when (x, y) is on the device grid). The color
	 * and the paint of the canvas are unchanged. As {@code Graphics2D.drawImage()}:
	 * the clip applies, the alpha channel is blended.
	 */
	public void drawImage(int[] argb, int width, int height, double x, double y, double scale);

	/**
	 * Draws the text with its baseline at (x, y), rotated by angle (radians,
	 * clockwise on screen) around that point, with the current color.
	 */
	public void drawString(String text, UFont font, double x, double y, double angle);

	/**
	 * Box of the ink of the text drawn with its baseline at (0, 0), without
	 * rotation (as {@code TextLayout.getBounds()}): x, y (of the top, so negative
	 * above the baseline), width, height.
	 */
	public double[] getInkBounds(String text, UFont font);

	/**
	 * Encodes the image as PNG, as PngIO does (see {@code PngIO}: colors reduced
	 * when possible), without any metadata (see {@link PngChunks}). Nothing can be
	 * drawn afterwards.
	 */
	public byte[] toPng();

	/**
	 * Releases the resources of the canvas.
	 */
	public void dispose();

}
