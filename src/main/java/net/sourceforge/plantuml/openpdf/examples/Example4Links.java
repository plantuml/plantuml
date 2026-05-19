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
 * Example 4 - hyperlinks.
 *
 * Draws three clickable boxes pointing to plantuml.com. Each box accumulates
 * its bounding rectangle between {@code openLink} and {@code closeLink};
 * everything drawn inside the pair becomes part of the clickable region.
 */
public class Example4Links {

	public static void main(String[] args) throws IOException {
		final PdfGraphics g = new PdfGraphics(new PdfOption()
				.withTitle("Example 4 - clickable links"));

		drawLinkBox(g, 30, 30, "https://plantuml.com", "Open plantuml.com",
				new Color(220, 230, 250), new Color(60, 90, 200), "plantuml.com");

		drawLinkBox(g, 30, 120, "https://github.com/plantuml/plantuml",
				"PlantUML on GitHub", new Color(230, 230, 230), new Color(40, 40, 40), "GitHub repo");

		drawLinkBox(g, 30, 210, "https://forum.plantuml.net",
				"PlantUML forum", new Color(240, 220, 220), new Color(180, 50, 50), "Forum");

		try (FileOutputStream out = new FileOutputStream("example4-links.pdf")) {
			g.createPdf(out);
		}
		System.out.println("Wrote example4-links.pdf (open it and click on the boxes)");
	}

	private static void drawLinkBox(PdfGraphics g, double x, double y, String url, String title,
			Color bg, Color fg, String label) {
		g.openLink(url, title);
		g.setFillColor(bg);
		g.setStrokeColor(fg);
		g.setStrokeWidth(1.5, null);
		g.pdfRectangle(x, y, 280, 60, 8, 8);
		g.setFillColor(fg);
		g.text(label, x + 16, y + 38, "Helvetica", 16, true, false, 0);
		g.closeLink();
	}
}
