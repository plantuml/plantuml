/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
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
package net.sourceforge.plantuml.klimt.drawing.svg;

import java.awt.font.TextLayout;
import java.util.HashMap;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.klimt.UParam;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UDriver;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.font.UnusedSpace;
import net.sourceforge.plantuml.klimt.shape.UCenteredCharacter;

public class DriverCenteredCharacterSvg implements UDriver<UCenteredCharacter, SvgGraphics> {

	private final FileFormat fileFormat;

	public DriverCenteredCharacterSvg(FileFormat fileFormat) {
		this.fileFormat = fileFormat;
	}

	public void draw(UCenteredCharacter characterCircled, double x, double y, ColorMapper mapper, UParam param,
			SvgGraphics svg) {
		final char c = characterCircled.getChar();
		final HColor textColor = param.getColor();

		// For deterministic SVG output (used by tests), avoid rendering the glyph as a
		// vector path derived from the system font (which differs across OS/JVM).
		// Instead, emit a simple <text> element with a fixed font-family.
		if (fileFormat == FileFormat.SVG_DETERMINISTIC) {
			svg.setFillColor(textColor.toSvg(mapper));
			svg.text(String.valueOf(c), x - 5, y + 5, "monospace", 14, null, null, null, 0,
					new HashMap<String, String>(), null);
			return;
		}

		final UFont font = characterCircled.getFont();
		final UnusedSpace unusedSpace = UnusedSpace.getUnusedSpace(font, c);

		final double xpos = x - unusedSpace.getCenterX() - 0.5;
		final double ypos = y - unusedSpace.getCenterY() - 0.5;

		final TextLayout t = font.createTextLayout("" + c);
		svg.setFillColor(textColor.toSvg(mapper));

		svg.drawPathIterator(xpos, ypos, t.getOutline(null).getPathIterator(null));
	}
}
