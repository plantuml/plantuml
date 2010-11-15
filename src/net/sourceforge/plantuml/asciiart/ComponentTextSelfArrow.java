/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
import java.util.List;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.txt.UGraphicTxt;

public class ComponentTextSelfArrow implements Component {

	private final ComponentType type;
	private final List<? extends CharSequence> stringsToDisplay;
	private final FileFormat fileFormat;

	public ComponentTextSelfArrow(ComponentType type, List<? extends CharSequence> stringsToDisplay,
			FileFormat fileFormat) {
		this.type = type;
		this.stringsToDisplay = stringsToDisplay;
		this.fileFormat = fileFormat;
	}

	public void drawU(UGraphic ug, Dimension2D dimensionToUse, Context2D context) {
		final UmlCharArea charArea = ((UGraphicTxt) ug).getCharArea();
		final int width = (int) dimensionToUse.getWidth();
		final int height = (int) dimensionToUse.getHeight() - 1;

		charArea.fillRect(' ', 0, 0, width, height);

		if (fileFormat == FileFormat.UTXT) {
			if (type == ComponentType.SELF_ARROW) {
				charArea.drawStringLR("\u2500\u2500\u2500\u2500\u2510", 0, 0);
				charArea.drawStringLR("\u2502", 4, 1);
				charArea.drawStringLR("<\u2500\u2500\u2500\u2518", 0, 2);
			} else if (type == ComponentType.DOTTED_SELF_ARROW) {
				charArea.drawStringLR("\u2500 \u2500 \u2510", 0, 0);
				charArea.drawStringLR("|", 4, 1);
				charArea.drawStringLR("< \u2500 \u2518", 0, 2);
			}
		} else {
			if (type == ComponentType.SELF_ARROW) {
				charArea.drawStringLR("----.", 0, 0);
				charArea.drawStringLR("|", 4, 1);
				charArea.drawStringLR("<---'", 0, 2);
			} else if (type == ComponentType.DOTTED_SELF_ARROW) {
				charArea.drawStringLR("- - .", 0, 0);
				charArea.drawStringLR("|", 4, 1);
				charArea.drawStringLR("< - '", 0, 2);
			}
		}

		charArea.drawStringsLR(stringsToDisplay, 6, 1);
	}

	public double getPreferredHeight(StringBounder stringBounder) {
		return StringUtils.getHeight(stringsToDisplay) + 3;
	}

	public double getPreferredWidth(StringBounder stringBounder) {
		return StringUtils.getWidth(stringsToDisplay) + 6;
	}

}
