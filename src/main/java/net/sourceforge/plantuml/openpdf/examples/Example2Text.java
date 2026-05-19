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
 * Example 2 - text rendering.
 *
 * Demonstrates the three standard PDF font families (Helvetica, Times,
 * Courier), all weight/style combinations, and the optional text-length
 * adjustment that mimics SVG's {@code lengthAdjust="spacingAndGlyphs"}.
 */
public class Example2Text {

	public static void main(String[] args) throws IOException {
		final PdfGraphics g = new PdfGraphics(new PdfOption()
				.withTitle("Example 2 - text"));

		g.setFillColor(Color.BLACK);

		// Three families, plain
		g.text("Helvetica plain - the quick brown fox", 20, 30, "Helvetica", 14, false, false, 0);
		g.text("Times plain    - the quick brown fox", 20, 50, "Times", 14, false, false, 0);
		g.text("Courier plain  - the quick brown fox", 20, 70, "Courier", 14, false, false, 0);

		// Weight and style variations
		g.text("Helvetica bold", 20, 110, "Helvetica", 14, true, false, 0);
		g.text("Helvetica italic", 20, 130, "Helvetica", 14, false, true, 0);
		g.text("Helvetica bold italic", 20, 150, "Helvetica", 14, true, true, 0);

		// A row of colored words
		g.setFillColor(new Color(200, 30, 30));
		g.text("RED", 20, 190, "Helvetica", 18, true, false, 0);
		g.setFillColor(new Color(30, 130, 30));
		g.text("GREEN", 80, 190, "Helvetica", 18, true, false, 0);
		g.setFillColor(new Color(30, 60, 200));
		g.text("BLUE", 170, 190, "Helvetica", 18, true, false, 0);

		// Stretched text - target width 300 pt regardless of natural width.
		// Useful for diagram labels that must fit a fixed box.
		g.setFillColor(Color.BLACK);
		g.text("stretched to fit", 20, 230, "Helvetica", 14, false, false, 300);
		g.setStrokeColor(Color.GRAY);
		g.setStrokeWidth(0.5, new double[] { 2, 2 });
		g.pdfLine(20, 234, 320, 234);
		g.pdfLine(20, 215, 20, 234);
		g.pdfLine(320, 215, 320, 234);

		try (FileOutputStream out = new FileOutputStream("example2-text.pdf")) {
			g.createPdf(out);
		}
		System.out.println("Wrote example2-text.pdf");
	}
}
