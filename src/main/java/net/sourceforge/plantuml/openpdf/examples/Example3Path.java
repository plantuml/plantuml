/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
 *
 * This file is part of PlantUML.
 *
 */
package net.sourceforge.plantuml.openpdf.examples;

import java.awt.Color;
import java.awt.geom.Path2D;
import java.io.FileOutputStream;
import java.io.IOException;

import net.sourceforge.plantuml.openpdf.PdfGraphics;
import net.sourceforge.plantuml.openpdf.PdfOption;

/**
 * Example 3 - complex paths.
 *
 * Builds a path with all four segment types (moveto, lineto, quadto, curveto,
 * closepath) using both the direct API and via a Java2D {@link Path2D}
 * replayed through {@link PdfGraphics#drawPathIterator}.
 */
public class Example3Path {

	public static void main(String[] args) throws IOException {
		final PdfGraphics g = new PdfGraphics(new PdfOption()
				.withTitle("Example 3 - paths"));

		// ----- Direct path API: a leaf-like shape with quad and cubic curves -----
		g.setFillColor(new Color(120, 200, 130));
		g.setStrokeColor(new Color(40, 90, 50));
		g.setStrokeWidth(2, null);
		g.newpath();
		g.moveto(80, 50);
		// quadto wants the current point as first 2 args (we manage it ourselves
		// because PdfContentByte does not track it for us).
		g.quadto(80, 50, 180, 30, 220, 100);
		g.curveto(260, 130, 200, 200, 140, 180);
		g.lineto(80, 150);
		g.closepath();
		g.fill(Path2D.WIND_NON_ZERO);

		// ----- Java2D Path2D replayed via drawPathIterator -----
		// A 5-pointed star centered at (360, 110), radius 60.
		final Path2D.Double star = new Path2D.Double();
		final double cx = 360, cy = 110, rOuter = 60, rInner = 26;
		for (int i = 0; i < 10; i++) {
			final double angle = -Math.PI / 2 + i * Math.PI / 5;
			final double r = (i % 2 == 0) ? rOuter : rInner;
			final double x = cx + r * Math.cos(angle);
			final double y = cy + r * Math.sin(angle);
			if (i == 0)
				star.moveTo(x, y);
			else
				star.lineTo(x, y);
		}
		star.closePath();
		g.setFillColor(new Color(250, 200, 50));
		g.setStrokeColor(new Color(150, 100, 20));
		g.drawPathIterator(0, 0, star.getPathIterator(null));

		// ----- A wavy line using only the path API (no fill, stroke only) -----
		g.setFillColor(new Color(0, 0, 0, 0)); // transparent fill
		g.setStrokeColor(new Color(60, 80, 200));
		g.setStrokeWidth(2.5, null);
		g.newpath();
		g.moveto(40, 260);
		double prevX = 40;
		double prevY = 260;
		for (int i = 0; i < 8; i++) {
			final double x1 = prevX + 25;
			final double y1 = (i % 2 == 0) ? 230 : 290;
			final double x2 = prevX + 50;
			final double y2 = 260;
			g.quadto(prevX, prevY, x1, y1, x2, y2);
			prevX = x2;
			prevY = y2;
		}
		g.fill(Path2D.WIND_NON_ZERO);

		try (FileOutputStream out = new FileOutputStream("example3-path.pdf")) {
			g.createPdf(out);
		}
		System.out.println("Wrote example3-path.pdf");
	}
}
