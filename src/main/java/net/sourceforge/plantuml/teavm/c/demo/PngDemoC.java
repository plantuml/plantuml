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
package net.sourceforge.plantuml.teavm.c.demo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.klimt.drawing.raster.RasterPath;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.font.UFontFace;
import net.sourceforge.plantuml.klimt.font.UFontFactory;
import net.sourceforge.plantuml.teavm.c.PngCanvas;

/**
 * Entry point validating PNG output in the TeaVM C build, before any PlantUML
 * code is involved: Java calls a C facade over plutovg through TeaVM's
 * {@code @Import}, draws shapes and text with an embedded font, and gets the
 * PNG back as a byte[].
 *
 * Build it with
 * {@code gradlew :generateC -PteavmCMainClass=PngDemoC},
 * then run {@code plantuml [output.png]} (default: demo.png).
 *
 * The picture is a small mind map whose boxes are sized from the measured
 * text, plus a text sample with its ascent, baseline and descent lines: the
 * text must sit exactly on the red baseline. Measurements are printed, to be
 * compared with Java (Arial has the same metrics as Liberation Sans).
 */
public class PngDemoC {
	// ::remove folder when JAVA8

	private static final int WHITE = 0xFFFFFFFF;
	private static final int BLACK = 0xFF000000;
	private static final int BORDER = 0xFF181818;
	private static final int FILL = 0xFFF1F1F1;
	private static final int RED = 0xFFE00000;
	private static final int BLUE = 0xFF2060E0;

	private static final int FONT_SIZE = 14;
	private static final double MARGIN_X = 10;
	private static final double MARGIN_Y = 8;

	public static void main(String[] args) throws IOException {
		final String output = args.length > 0 ? args[0] : "demo.png";
		final UFont font = UFontFactory.build("SansSerif", UFontFace.normal(), FONT_SIZE);

		final byte[] png;
		final PngCanvas canvas = new PngCanvas(480, 260, true);
		try {
			canvas.setColor(WHITE);
			canvas.fill(RasterPath.rectangle(0, 0, 480, 260, 0, 0));

			final double[] metrics = canvas.getFontMetrics(font);
			System.out.println("Font size " + FONT_SIZE + ": ascent=" + metrics[0] + " descent=" + metrics[1]
					+ " lineGap=" + metrics[2]);
			for (String s : new String[] { "Hello world", "PlantUML", "Accents: \u00e9\u00e0\u00e7\u00f6 \u20ac" })
				System.out.println("Width of \"" + s + "\": " + canvas.getTextWidth(s, font));

			// A small mind map: boxes sized from the measured text
			final double[] root = box(canvas, font, metrics, "PlantUML", 30, 90);
			final double[] svg = box(canvas, font, metrics, "SVG", 250, 30);
			final double[] png2 = box(canvas, font, metrics, "PNG natif", 250, 150);
			link(canvas, root, svg);
			link(canvas, root, png2);

			// Text sample on its baseline, with the ascent and descent lines
			final String sample = "Accents: \u00e9\u00e0\u00e7\u00f6 \u20ac \u2013 Hxgjp";
			final double x = 30;
			final double baseline = 230;
			final double width = canvas.getTextWidth(sample, font);
			canvas.setStroke(1, 0, 0);
			canvas.setColor(BLUE);
			canvas.stroke(RasterPath.line(x, baseline - metrics[0], x + width, baseline - metrics[0]));
			canvas.stroke(RasterPath.line(x, baseline + metrics[1], x + width, baseline + metrics[1]));
			canvas.setColor(RED);
			canvas.stroke(RasterPath.line(x, baseline, x + width, baseline));
			canvas.setColor(BLACK);
			canvas.drawString(sample, font, x, baseline, 0);

			png = canvas.toPng();
		} finally {
			canvas.dispose();
		}

		try (OutputStream os = new FileOutputStream(output)) {
			os.write(png);
		}
		System.out.println("Written " + output + " (" + png.length + " bytes)");
	}

	/**
	 * Draws a rounded box around the text, its top left corner at (x, y).
	 *
	 * @return x, y, width, height of the box
	 */
	private static double[] box(PngCanvas canvas, UFont font, double[] metrics, String text, double x, double y) {
		final double width = canvas.getTextWidth(text, font) + 2 * MARGIN_X;
		final double height = metrics[0] + metrics[1] + 2 * MARGIN_Y;
		final RasterPath shape = RasterPath.rectangle(x, y, width, height, 12, 12);
		canvas.setColor(FILL);
		canvas.fill(shape);
		canvas.setColor(BORDER);
		canvas.setStroke(1.5, 0, 0);
		canvas.stroke(shape);
		canvas.setColor(BLACK);
		canvas.drawString(text, font, x + MARGIN_X, y + MARGIN_Y + metrics[0], 0);
		return new double[] { x, y, width, height };
	}

	private static void link(PngCanvas canvas, double[] from, double[] to) {
		final double x1 = from[0] + from[2];
		final double y1 = from[1] + from[3] / 2;
		final double x2 = to[0];
		final double y2 = to[1] + to[3] / 2;
		final double middle = (x1 + x2) / 2;
		canvas.setColor(BORDER);
		canvas.setStroke(1, 0, 0);
		canvas.stroke(new RasterPath().moveTo(x1, y1).cubicTo(middle, y1, middle, y2, x2, y2));
	}

}
