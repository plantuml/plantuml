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
package net.sourceforge.plantuml.salt.element;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;

public class ElementLine extends AbstractElement {

	private final char separator;

	public ElementLine(char separator) {
		this.separator = separator;
	}

	public Dimension2D getPreferredDimension(StringBounder stringBounder, double x, double y) {
		return new Dimension2DDouble(10, 6);
	}

	public void drawU(UGraphic ug, int zIndex, Dimension2D dimToUse) {
		if (zIndex != 0) {
			return;
		}
		ug = ug.apply(HColorSet.instance().getColorIfValid("#AAAAAA"));
		double y2 = dimToUse.getHeight() / 2;
		if (separator == '=') {
			y2 = y2 - 1;
		}
		drawLine(ug, 0, y2, dimToUse.getWidth(), separator);
	}

	private static void drawLine(UGraphic ug, double x, double y, double widthToUse, char separator) {
		if (separator == '=') {
			ug.apply(new UStroke()).apply(new UTranslate(x, y)).draw(ULine.hline(widthToUse));
			ug.apply(new UStroke()).apply(new UTranslate(x, y + 2)).draw(ULine.hline(widthToUse));
		} else if (separator == '.') {
			ug.apply(new UStroke(1, 2, 1)).apply(new UTranslate(x, y)).draw(ULine.hline(widthToUse));
		} else if (separator == '-') {
			ug.apply(new UStroke()).apply(new UTranslate(x, y)).draw(ULine.hline(widthToUse));
		} else {
			ug.apply(new UStroke(1.5)).apply(new UTranslate(x, y)).draw(ULine.hline(widthToUse));
		}
	}

}
