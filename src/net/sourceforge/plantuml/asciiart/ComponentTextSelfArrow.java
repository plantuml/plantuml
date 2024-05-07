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
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.ArrowComponent;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;

public class ComponentTextSelfArrow extends AbstractComponentText implements ArrowComponent {

	private final ComponentType type;
	private final Display stringsToDisplay;
	private final FileFormat fileFormat;
	private final ArrowConfiguration config;

	public ComponentTextSelfArrow(ComponentType type, ArrowConfiguration config, Display stringsToDisplay,
			FileFormat fileFormat) {
		this.type = type;
		this.stringsToDisplay = ComponentTextArrow.cleanAndManageBoldNumber(stringsToDisplay, fileFormat);
		this.fileFormat = fileFormat;
		this.config = config;
	}

	public void drawU(UGraphic ug, Area area, Context2D context) {
		if (config.isHidden()) {
			return;
		}
		final XDimension2D dimensionToUse = area.getDimensionToUse();
		final UmlCharArea charArea = ((UGraphicTxt) ug).getCharArea();
		final int width = (int) dimensionToUse.getWidth();
		final int height = (int) dimensionToUse.getHeight() - 1;

		charArea.fillRect(' ', 1, 0, width-2, height);

		if (fileFormat == FileFormat.UTXT) {
			if (config.isDotted()) {
				charArea.drawStringLR("\u2500 \u2500 \u2510", 1, 0);
				charArea.drawStringLR("|", 5, 1);
				charArea.drawStringLR("< \u2500 \u2518", 1, 2);
			} else {
				charArea.drawStringLR("\u2500\u2500\u2500\u2500\u2510", 1, 0);
				charArea.drawStringLR("\u2502", 5, 1);
				charArea.drawStringLR("<\u2500\u2500\u2500\u2518", 1, 2);
			}
		} else if (config.isDotted()) {
			charArea.drawStringLR("- - .", 1, 0);
			charArea.drawStringLR("|", 5, 1);
			charArea.drawStringLR("< - '", 1, 2);
		} else {
			charArea.drawStringLR("----.", 1, 0);
			charArea.drawStringLR("|", 5, 1);
			charArea.drawStringLR("<---'", 1, 2);
		}

		if (fileFormat == FileFormat.UTXT) {
			charArea.drawStringsLRUnicode(stringsToDisplay.asList(), 7, 1);
		} else {
			charArea.drawStringsLRSimple(stringsToDisplay.asList(), 7, 1);
		}
	}

	public double getPreferredHeight(StringBounder stringBounder) {
		return StringUtils.getHeight(stringsToDisplay) + 3;
	}

	public double getPreferredWidth(StringBounder stringBounder) {
		return StringUtils.getWcWidth(stringsToDisplay) + 8;
	}

	public XPoint2D getStartPoint(StringBounder stringBounder, XDimension2D dimensionToUse) {
		return new XPoint2D(0, 0);
	}

	public XPoint2D getEndPoint(StringBounder stringBounder, XDimension2D dimensionToUse) {
		return new XPoint2D(0, 0);
	}

	public double getPaddingY() {
		throw new UnsupportedOperationException();
	}

	public double getYPoint(StringBounder stringBounder) {
		throw new UnsupportedOperationException();
	}

	public double getPosArrow(StringBounder stringBounder) {
		throw new UnsupportedOperationException();
	}

}
