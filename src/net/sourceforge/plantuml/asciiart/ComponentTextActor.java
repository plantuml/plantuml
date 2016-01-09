/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 *
 * Revision $Revision: 4169 $
 *
 */
package net.sourceforge.plantuml.asciiart;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.txt.UGraphicTxt;
import net.sourceforge.plantuml.StringUtils;

public class ComponentTextActor extends AbstractComponentText {

	private final ComponentType type;
	private final Display stringsToDisplay;
	private final FileFormat fileFormat;

	public ComponentTextActor(ComponentType type, Display stringsToDisplay, FileFormat fileFormat) {
		this.type = type;
		this.stringsToDisplay = stringsToDisplay;
		this.fileFormat = fileFormat;
	}

	public void drawU(UGraphic ug, Area area, Context2D context) {
		final Dimension2D dimensionToUse = area.getDimensionToUse();
		final UmlCharArea charArea = ((UGraphicTxt) ug).getCharArea();
		final int width = (int) dimensionToUse.getWidth();
		// final int textWidth = StringUtils.getWidth(stringsToDisplay);
		final int height = (int) dimensionToUse.getHeight();
		charArea.fillRect(' ', 0, 0, width, height);

		final int xman = width / 2 - 1;
		if (type == ComponentType.ACTOR_HEAD) {
			charArea.drawStringsLR(stringsToDisplay.as(), 1, getStickManHeight());
			if (fileFormat == FileFormat.UTXT) {
				charArea.drawStickManUnicode(xman, 0);
			} else {
				charArea.drawStickMan(xman, 0);
			}
		} else if (type == ComponentType.ACTOR_TAIL) {
			charArea.drawStringsLR(stringsToDisplay.as(), 1, 0);
			if (fileFormat == FileFormat.UTXT) {
				charArea.drawStickManUnicode(xman, 1);
			} else {
				charArea.drawStickMan(xman, 1);
			}
		} else {
			assert false;
		}
	}

	private int getStickManHeight() {
		if (fileFormat == FileFormat.UTXT) {
			return UmlCharArea.STICKMAN_UNICODE_HEIGHT;
		}
		return UmlCharArea.STICKMAN_HEIGHT;
	}

	public double getPreferredHeight(StringBounder stringBounder) {
		return StringUtils.getHeight(stringsToDisplay) + getStickManHeight();
	}

	public double getPreferredWidth(StringBounder stringBounder) {
		return StringUtils.getWidth(stringsToDisplay) + 2;
	}

}
