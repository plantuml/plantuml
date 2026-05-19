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
 * Example 5 - a mini class diagram.
 *
 * Hand-drawn UML-ish class diagram to validate that the API holds up in a
 * realistic PlantUML-like scenario: three boxes, separators, methods, and
 * connecting arrows.
 */
public class Example5MiniDiagram {

	public static void main(String[] args) throws IOException {
		final PdfGraphics g = new PdfGraphics(new PdfOption()
				.withTitle("Example 5 - mini class diagram"));

		final Color boxFill = new Color(255, 250, 220);
		final Color boxStroke = new Color(120, 80, 0);
		final Color textColor = new Color(40, 30, 0);

		drawClassBox(g, 40, 40, "Animal", new String[] { "+ name : String", "+ age : int" },
				new String[] { "+ eat()", "+ sleep()" }, boxFill, boxStroke, textColor);

		drawClassBox(g, 300, 40, "Dog", new String[] { "+ breed : String" },
				new String[] { "+ bark()" }, boxFill, boxStroke, textColor);

		drawClassBox(g, 300, 220, "Cat", new String[] { "+ indoor : boolean" },
				new String[] { "+ purr()", "+ scratch()" }, boxFill, boxStroke, textColor);

		// Inheritance arrows: open triangle pointing to Animal
		drawInheritance(g, 300, 80, 200, 80, boxStroke);
		drawInheritance(g, 300, 260, 200, 100, boxStroke);

		try (FileOutputStream out = new FileOutputStream("example5-diagram.pdf")) {
			g.createPdf(out);
		}
		System.out.println("Wrote example5-diagram.pdf");
	}

	private static void drawClassBox(PdfGraphics g, double x, double y, String name, String[] fields,
			String[] methods, Color fill, Color stroke, Color text) {
		final double width = 180;
		final double headerH = 28;
		final double rowH = 18;
		final double height = headerH + fields.length * rowH + methods.length * rowH + 10;

		g.setFillColor(fill);
		g.setStrokeColor(stroke);
		g.setStrokeWidth(1.2, null);
		g.pdfRectangle(x, y, width, height, 0, 0);

		// Header separator
		g.pdfLine(x, y + headerH, x + width, y + headerH);

		// Fields/methods separator
		g.pdfLine(x, y + headerH + fields.length * rowH + 5,
				x + width, y + headerH + fields.length * rowH + 5);

		// Class name (centered-ish, bold)
		g.setFillColor(text);
		g.text(name, x + width / 2 - name.length() * 4, y + 19, "Helvetica", 14, true, false, 0);

		// Fields
		for (int i = 0; i < fields.length; i++)
			g.text(fields[i], x + 8, y + headerH + (i + 1) * rowH - 4,
					"Helvetica", 10, false, false, 0);

		// Methods
		for (int i = 0; i < methods.length; i++)
			g.text(methods[i], x + 8,
					y + headerH + fields.length * rowH + 5 + (i + 1) * rowH - 4,
					"Helvetica", 10, false, false, 0);
	}

	private static void drawInheritance(PdfGraphics g, double fromX, double fromY, double toX, double toY,
			Color color) {
		g.setStrokeColor(color);
		g.setFillColor(Color.WHITE);
		g.setStrokeWidth(1.2, null);
		g.pdfLine(fromX, fromY, toX + 12, toY);

		// Open triangle arrowhead at (toX, toY)
		final double size = 10;
		g.pdfPolygon(toX, toY, toX + size, toY - size / 2, toX + size, toY + size / 2);
	}
}
