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

import net.sourceforge.plantuml.klimt.UParam;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.awt.XColor;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorGradient;
import net.sourceforge.plantuml.klimt.color.HColorLinearGradient;

/**
 * Paints and strokes of the raster drivers, as the Java2D drivers set them
 * (see {@code DriverRectangleG2d} and {@code DriverLineG2d}).
 */
final class RasterPaints {

	private RasterPaints() {
	}

	static int argb(HColor color, ColorMapper mapper) {
		return color.toColor(mapper).getRGB();
	}

	static boolean isGradient(HColor color) {
		return color instanceof HColorGradient || color instanceof HColorLinearGradient;
	}

	/**
	 * As {@code DriverLineG2d.manageStroke}.
	 */
	static void setStroke(UStroke stroke, RasterCanvas canvas) {
		if (stroke.getDashVisible() == 0)
			canvas.setStroke(stroke.getThickness(), 0, 0);
		else
			canvas.setStroke(stroke.getThickness(), stroke.getDashVisible(), stroke.getDashSpace());
	}

	/**
	 * As {@code DriverRectangleG2d.drawBorder}.
	 */
	static void drawBorder(UParam param, HColor color, ColorMapper mapper, double width, double height,
			RasterPath shape, RasterCanvas canvas, double x, double y) {
		if (color.isTransparent())
			return;

		if (isGradient(color))
			setGradient(canvas, x, y, mapper, width, height, color);
		else
			canvas.setColor(argb(color, mapper));

		setStroke(param.getStroke(), canvas);
		canvas.stroke(shape);
	}

	/**
	 * As {@code DriverRectangleG2d.getPaintGradient}, quirks included (the
	 * vertical and horizontal policies use (y + height) / 2 and (x + width) / 2).
	 */
	static void setGradient(RasterCanvas canvas, double x, double y, ColorMapper mapper, double width, double height,
			HColor back) {
		if (back instanceof HColorLinearGradient) {
			setLinearGradient(canvas, (HColorLinearGradient) back, mapper, x, y, width, height);
			return;
		}
		final HColorGradient gr = (HColorGradient) back;
		final char policy = gr.getPolicy();
		final int color1 = argb(gr.getColor1(), mapper);
		final int color2 = argb(gr.getColor2(), mapper);
		// java.awt.GradientPaint uses float coordinates
		final double x1;
		final double y1;
		final double x2;
		final double y2;
		if (policy == '|') {
			x1 = (float) x;
			y1 = (float) (y + height) / 2;
			x2 = (float) (x + width);
			y2 = (float) (y + height) / 2;
		} else if (policy == '\\') {
			x1 = (float) x;
			y1 = (float) (y + height);
			x2 = (float) (x + width);
			y2 = (float) y;
		} else if (policy == '-') {
			x1 = (float) (x + width) / 2;
			y1 = (float) y;
			x2 = (float) (x + width) / 2;
			y2 = (float) (y + height);
		} else {
			x1 = (float) x;
			y1 = (float) y;
			x2 = (float) (x + width);
			y2 = (float) (y + height);
		}
		canvas.setLinearGradient(x1, y1, x2, y2, new double[] { 0, 1 }, new int[] { color1, color2 },
				RasterCanvas.SPREAD_PAD);
	}

	private static void setLinearGradient(RasterCanvas canvas, HColorLinearGradient gr, ColorMapper mapper, double x,
			double y, double width, double height) {
		final int size = gr.getStops().size();
		final double[] offsets = new double[size];
		final int[] colors = new int[size];
		int i = 0;
		for (HColorLinearGradient.Stop stop : gr.getStops()) {
			offsets[i] = (float) stop.getOffset();
			final XColor base = stop.getColor().toColor(mapper);
			int alpha = (int) Math.round(base.getAlpha() * stop.getOpacity());
			if (alpha < 0)
				alpha = 0;
			if (alpha > 255)
				alpha = 255;
			colors[i] = new XColor(base.getRed(), base.getGreen(), base.getBlue(), alpha).getRGB();
			i++;
		}
		final double x1;
		final double y1;
		final double x2;
		final double y2;
		if (gr.isUserSpaceOnUse()) {
			x1 = (float) gr.getX1();
			y1 = (float) gr.getY1();
			x2 = (float) gr.getX2();
			y2 = (float) gr.getY2();
		} else {
			x1 = (float) (x + gr.getX1() * width);
			y1 = (float) (y + gr.getY1() * height);
			x2 = (float) (x + gr.getX2() * width);
			y2 = (float) (y + gr.getY2() * height);
		}
		final int spread;
		switch (gr.getSpreadMethod()) {
		case REFLECT:
			spread = RasterCanvas.SPREAD_REFLECT;
			break;
		case REPEAT:
			spread = RasterCanvas.SPREAD_REPEAT;
			break;
		case PAD:
		default:
			spread = RasterCanvas.SPREAD_PAD;
			break;
		}
		canvas.setLinearGradient(x1, y1, x2, y2, offsets, colors, spread);
	}

}
