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
package net.sourceforge.plantuml.teavm.c;

import net.sourceforge.plantuml.klimt.drawing.raster.ArcsToCubics;
import net.sourceforge.plantuml.klimt.drawing.raster.RasterCanvas;
import net.sourceforge.plantuml.klimt.drawing.raster.RasterPath;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.font.UFontFace;

/**
 * The {@link RasterCanvas} of the TeaVM C build: drawn by plutovg (see
 * {@link NativeCanvas}), text with the fonts embedded in the executable
 * (Liberation Sans, whatever the family: the other families are not embedded
 * yet), never the fonts of the machine. The PNG is encoded in C, as PngIO does
 * on the JVM (src/teavm-c/native/plantuml_png.h).
 *
 * A canvas holds native memory: {@link #dispose} releases it.
 */
public final class PngCanvas implements RasterCanvas {
	// ::remove file when JAVA8

	private long handle;
	private final boolean opaque;
	private double scale = 1;

	// The clip set on the native canvas, which only changes it when it differs
	// (setting a clip saves and restores the native state)
	private boolean clipped;
	private double clipX;
	private double clipY;
	private double clipWidth;
	private double clipHeight;

	private final ArcsToCubics replay = new ArcsToCubics() {
		@Override
		protected void doMoveTo(double x, double y) {
			NativeCanvas.moveTo(handle, x, y);
		}

		@Override
		protected void doLineTo(double x, double y) {
			NativeCanvas.lineTo(handle, x, y);
		}

		@Override
		protected void doCubicTo(double x1, double y1, double x2, double y2, double x3, double y3) {
			NativeCanvas.cubicTo(handle, x1, y1, x2, y2, x3, y3);
		}

		@Override
		protected void doClosePath() {
			NativeCanvas.closePath(handle);
		}

		@Override
		public void rectangle(double x, double y, double width, double height, double arcWidth, double arcHeight) {
			// Arcs are diameters, the native rectangle takes radii
			NativeCanvas.rect(handle, x, y, width, height, arcWidth / 2, arcHeight / 2);
		}

		@Override
		public void ellipse(double x, double y, double width, double height) {
			NativeCanvas.ellipse(handle, x + width / 2, y + height / 2, width / 2, height / 2);
		}
	};

	/**
	 * @param opaque the image will get an opaque background, see
	 *               {@link RasterCanvas.Factory}
	 */
	public PngCanvas(int width, int height, boolean opaque) {
		this.opaque = opaque;
		if (width <= 0 || height <= 0)
			throw new IllegalArgumentException("Bad size " + width + "x" + height);
		this.handle = NativeCanvas.create(width, height);
		if (handle == 0)
			throw new IllegalStateException("Cannot create a " + width + "x" + height + " canvas");
	}

	private long handle() {
		if (handle == 0)
			throw new IllegalStateException("Canvas disposed");
		return handle;
	}

	@Override
	public void scale(double factor) {
		NativeCanvas.scale(handle(), factor);
		scale *= factor;
	}

	@Override
	public void setColor(int argb) {
		NativeCanvas.setColor(handle(), argb);
	}

	@Override
	public void setLinearGradient(double x1, double y1, double x2, double y2, double[] offsets, int[] colors,
			int spread) {
		NativeCanvas.setLinearGradient(handle(), x1, y1, x2, y2, offsets, colors, Math.min(offsets.length, 16),
				spread);
	}

	@Override
	public void setStroke(double thickness, double dashVisible, double dashSpace) {
		// Java2D draws a zero width as the thinnest line: one device pixel
		if (thickness <= 0)
			thickness = 1 / scale;
		NativeCanvas.setStroke(handle(), thickness, dashVisible, dashSpace);
	}

	@Override
	public void setClip(double x, double y, double width, double height) {
		if (clipped && x == clipX && y == clipY && width == clipWidth && height == clipHeight)
			return;
		NativeCanvas.setClip(handle(), x, y, width, height);
		clipped = true;
		clipX = x;
		clipY = y;
		clipWidth = width;
		clipHeight = height;
	}

	@Override
	public void resetClip() {
		if (clipped == false)
			return;
		NativeCanvas.resetClip(handle());
		clipped = false;
	}

	@Override
	public void fill(RasterPath path) {
		handle();
		path.visit(replay);
		NativeCanvas.fill(handle);
	}

	@Override
	public void stroke(RasterPath path) {
		handle();
		path.visit(replay);
		NativeCanvas.stroke(handle);
	}

	@Override
	public void drawString(String text, UFont font, double x, double y, double angle) {
		setFont(font);
		final char[] chars = text.toCharArray();
		NativeCanvas.fillText(handle, chars, chars.length, x, y, angle);
	}

	/**
	 * Advance width of the text, as the native canvas draws it.
	 */
	public double getTextWidth(String text, UFont font) {
		setFont(font);
		final char[] chars = text.toCharArray();
		return NativeCanvas.textWidth(handle, chars, chars.length);
	}

	/**
	 * @return ascent, descent (both positive) and line gap of the font
	 */
	public double[] getFontMetrics(UFont font) {
		setFont(font);
		final double[] metrics = new double[3];
		NativeCanvas.fontMetrics(handle, metrics);
		return metrics;
	}

	private void setFont(UFont font) {
		final UFontFace face = font.getFontFace();
		final int id;
		if (face.isBold())
			id = face.isItalic() ? NativeCanvas.FONT_SANS_BOLD_ITALIC : NativeCanvas.FONT_SANS_BOLD;
		else
			id = face.isItalic() ? NativeCanvas.FONT_SANS_ITALIC : NativeCanvas.FONT_SANS;
		if (NativeCanvas.setFont(handle(), id, font.getSize2D()) != 0)
			throw new IllegalStateException("No embedded font");
	}

	@Override
	public byte[] toPng() {
		final int size = NativeCanvas.encodePng(handle(), opaque ? 1 : 0);
		if (size < 0)
			throw new IllegalStateException("PNG encoding failed");
		final byte[] result = new byte[size];
		NativeCanvas.copyPng(handle, result);
		return result;
	}

	@Override
	public void dispose() {
		if (handle != 0) {
			NativeCanvas.destroy(handle);
			handle = 0;
		}
	}

}
