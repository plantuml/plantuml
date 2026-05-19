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
import java.io.FileOutputStream;
import java.io.IOException;

import net.sourceforge.plantuml.openpdf.PdfGraphics;
import net.sourceforge.plantuml.openpdf.PdfOption;

/**
 * Example 1 - basic shapes.
 *
 * Draws a few rectangles, lines, ellipses and a polygon. Useful to validate
 * the coordinate system (origin top-left, Y growing downward as in SVG).
 *
 * Run from the repo root with the OpenPDF jars on the classpath:
 *
 * <pre>
 *   java -cp "build/classes/java/main;lib/openpdf-3.0.4.jar" \
 *        net.sourceforge.plantuml.openpdf.examples.Example1Basic
 * </pre>
 */
public class Example1Basic {

	public static void main(String[] args) throws IOException {
		final PdfGraphics g = new PdfGraphics(new PdfOption()
				.withTitle("Example 1 - basic shapes")
				.withAuthor("PlantUML"));

		// A red square
		g.setFillColor(new Color(220, 60, 60));
		g.setStrokeColor(Color.BLACK);
		g.setStrokeWidth(1.5, null);
		g.pdfRectangle(20, 20, 100, 60, 0, 0);

		// A blue rounded rectangle
		g.setFillColor(new Color(60, 120, 220));
		g.pdfRectangle(140, 20, 100, 60, 10, 10);

		// A green ellipse
		g.setFillColor(new Color(60, 200, 90));
		g.pdfEllipse(310, 50, 50, 30);

		// A diagonal line (stroke only)
		g.setStrokeColor(Color.DARK_GRAY);
		g.setStrokeWidth(2, null);
		g.pdfLine(20, 120, 360, 120);

		// A dashed line just below
		g.setStrokeWidth(1.2, new double[] { 6, 4 });
		g.pdfLine(20, 140, 360, 140);

		// A yellow triangle (polygon)
		g.setStrokeWidth(1, null);
		g.setFillColor(new Color(240, 210, 60));
		g.setStrokeColor(Color.BLACK);
		g.pdfPolygon(60, 200, 180, 200, 120, 280);

		try (FileOutputStream out = new FileOutputStream("example1-basic.pdf")) {
			g.createPdf(out);
		}
		System.out.println("Wrote example1-basic.pdf");
	}
}
