/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.sequencediagram.MessageNumber;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.ArrowComponent;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.skin.ArrowDirection;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.txt.UGraphicTxt;

public class ComponentTextArrow extends AbstractComponentText implements ArrowComponent {

	private final ComponentType type;
	private final Display stringsToDisplay;
	private final FileFormat fileFormat;
	private final ArrowConfiguration config;
	private final int maxAsciiMessageLength;

	public ComponentTextArrow(ComponentType type, ArrowConfiguration config, Display stringsToDisplay,
			FileFormat fileFormat, int maxAsciiMessageLength) {
		this.maxAsciiMessageLength = maxAsciiMessageLength;
		this.type = type;
		this.config = config;
		this.stringsToDisplay = clean(stringsToDisplay);
		this.fileFormat = fileFormat;
	}

	private static Display clean(Display orig) {
		if (orig.size() == 0 || orig.get(0) instanceof MessageNumber == false) {
			return orig;
		}
		Display result = Display.empty();
		for (int i = 0; i < orig.size(); i++) {
			CharSequence element = orig.get(i);
			if (i == 1) {
				element = removeTag(orig.get(0).toString()) + " " + element;
			}
			if (i != 0) {
				result = result.add(element);
			}
		}
		return result;
	}

	private static String removeTag(String s) {
		return s.replaceAll("\\<[^<>]+\\>", "");
	}

	public void drawU(UGraphic ug, Area area, Context2D context) {
		if (config.isHidden()) {
			return;
		}
		final Dimension2D dimensionToUse = area.getDimensionToUse();
		final UmlCharArea charArea = ((UGraphicTxt) ug).getCharArea();
		final int width = (int) dimensionToUse.getWidth();
		final int height = (int) dimensionToUse.getHeight();
		final int textWidth = StringUtils.getWcWidth(stringsToDisplay);

		final int yarrow = height - 2;
		charArea.drawHLine(fileFormat == FileFormat.UTXT ? '\u2500' : '-', yarrow, 1, width);
		if (config.isDotted()) {
			for (int i = 1; i < width; i += 2) {
				charArea.drawChar(' ', i, yarrow);
			}
		}

		if (config.getArrowDirection() == ArrowDirection.LEFT_TO_RIGHT_NORMAL) {
			charArea.drawChar('>', width - 1, yarrow);
		} else if (config.getArrowDirection() == ArrowDirection.RIGHT_TO_LEFT_REVERSE) {
			charArea.drawChar('<', 1, yarrow);
		} else if (config.getArrowDirection() == ArrowDirection.BOTH_DIRECTION) {
			charArea.drawChar('>', width - 1, yarrow);
			charArea.drawChar('<', 1, yarrow);
		} else {
			throw new UnsupportedOperationException();
		}
		// final int position = Math.max(0, (width - textWidth) / 2);
		charArea.drawStringsLR(stringsToDisplay.as(), (width - textWidth) / 2, 0);
	}

	public double getPreferredHeight(StringBounder stringBounder) {
		return StringUtils.getHeight(stringsToDisplay) + 2;
	}

	public double getPreferredWidth(StringBounder stringBounder) {
		final int width = StringUtils.getWcWidth(stringsToDisplay) + 2;
		if (maxAsciiMessageLength > 0) {
			return Math.min(maxAsciiMessageLength, width);
		}
		return width;
	}

	public Point2D getStartPoint(StringBounder stringBounder, Dimension2D dimensionToUse) {
		return new Point2D.Double(0, 0);
	}

	public Point2D getEndPoint(StringBounder stringBounder, Dimension2D dimensionToUse) {
		return new Point2D.Double(0, 0);
	}

	public double getPaddingY() {
		throw new UnsupportedOperationException();
	}

	public double getYPoint(StringBounder stringBounder) {
		throw new UnsupportedOperationException();
	}

}
