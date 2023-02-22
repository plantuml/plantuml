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
 *
 */
package net.sourceforge.plantuml.asciiart;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.drawing.txt.UGraphicTxt;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Context2D;

public class ComponentTextReference extends AbstractComponentText {

	private final Display stringsToDisplay;
	private final FileFormat fileFormat;

	public ComponentTextReference(Display stringsToDisplay, FileFormat fileFormat) {
		this.stringsToDisplay = stringsToDisplay;
		this.fileFormat = fileFormat;
	}

	public void drawU(UGraphic ug, Area area, Context2D context) {
		final XDimension2D dimensionToUse = area.getDimensionToUse();
		final UmlCharArea charArea = ((UGraphicTxt) ug).getCharArea();
		final int width = (int) dimensionToUse.getWidth();
		final int height = (int) dimensionToUse.getHeight();

		final String header = "REF";

		if (fileFormat == FileFormat.UTXT) {
			charArea.drawHLine('\u2550', 0, 1, width - 1, '\u2502', '\u256a');
			charArea.drawStringLR(header + "  /", 2, 1);
			charArea.drawHLine('\u2500', 2, 1, header.length() + 4);
			charArea.drawVLine('\u2551', 0, 1, height - 1);
			charArea.drawVLine('\u2551', width - 1, 1, height - 1);
			charArea.drawChar('\u255f', 0, 2);
			charArea.drawStringTB("\u2564\u2502\u2518", header.length() + 4, 0);
			charArea.drawChar('\u2554', 0, 0);
			charArea.drawChar('\u2557', width - 1, 0);
			charArea.drawHLine('\u2550', height - 1, 1, width - 1, '\u2502', '\u256a');
			charArea.drawChar('\u255a', 0, height - 1);
			charArea.drawChar('\u255d', width - 1, height - 1);
		} else {
			charArea.drawHLine('_', 0, 0, width - 1);
			charArea.drawStringLR(header + "  /", 2, 1);
			charArea.drawHLine('_', 2, 1, header.length() + 3);
			charArea.drawChar('/', header.length() + 3, 2);
			charArea.drawVLine('!', 0, 1, height);
			charArea.drawVLine('!', width - 1, 1, height);
			charArea.drawHLine('~', height - 1, 1, width - 1);
		}

		final Display text = stringsToDisplay.subList(1, stringsToDisplay.size());

		charArea.drawStringsLRUnicode(text.asList(), 2, 3);
	}

	public double getPreferredHeight(StringBounder stringBounder) {
		return StringUtils.getHeight(stringsToDisplay) + 3;
	}

	public double getPreferredWidth(StringBounder stringBounder) {
		return StringUtils.getWcWidth(stringsToDisplay) + 4;
	}

}
