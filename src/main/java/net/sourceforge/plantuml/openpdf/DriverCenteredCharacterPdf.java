/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2026, Arnaud Roques
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
 * Original Author:  kolulu23
 *
 * 
 */
package net.sourceforge.plantuml.openpdf;

import org.openpdf.text.pdf.BaseFont;

import net.sourceforge.plantuml.klimt.UParam;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UDriver;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.shape.UCenteredCharacter;

/**
 * Draws the character of a circled character (the "C" of a class) as text, with
 * the font of the PDF, centered on (x, y) by the bounding box of its glyph (AFM
 * metrics). No AWT text outline: it is the same on the JVM and in the C build.
 */
public class DriverCenteredCharacterPdf implements UDriver<UCenteredCharacter, PdfGraphics> {

	public void draw(UCenteredCharacter characterCircled, double x, double y, ColorMapper mapper, UParam param,
			PdfGraphics pdf) {
		final char c = characterCircled.getChar();
		final HColor textColor = param.getColor();

		final UFont font = characterCircled.getFont();
		final int size = font.getSize();
		final BaseFont baseFont = StringBounderOpenPdf.resolveFont(font);
		final String text = "" + c;

		// Center of the glyph, from the baseline origin, in points (the bounding box is in 1/1000 em)
		final double centerX;
		final double centerY;
		final int[] bbox = baseFont.getCharBBox(c);
		if (bbox == null) {
			centerX = baseFont.getWidthPoint(text, size) / 2;
			centerY = 0.35 * size;
		} else {
			centerX = (bbox[0] + bbox[2]) / 2000.0 * size;
			centerY = (bbox[1] + bbox[3]) / 2000.0 * size;
		}

		pdf.setFillColor(textColor.toColor(mapper).toAwtColor());
		// y grows downward: the baseline is below the center by centerY
		pdf.pdfText(text, x - centerX, y + centerY, baseFont, size);
	}
}
