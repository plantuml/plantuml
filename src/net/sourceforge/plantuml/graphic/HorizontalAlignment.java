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
package net.sourceforge.plantuml.graphic;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public enum HorizontalAlignment {

	LEFT, CENTER, RIGHT;

	public static HorizontalAlignment fromString(String s) {
		if (LEFT.name().equalsIgnoreCase(s)) {
			return LEFT;
		}
		if (CENTER.name().equalsIgnoreCase(s)) {
			return CENTER;
		}
		if (RIGHT.name().equalsIgnoreCase(s)) {
			return RIGHT;
		}
		return null;
	}

	public static HorizontalAlignment fromString(String s, HorizontalAlignment defaultValue) {
		if (defaultValue == null) {
			throw new IllegalArgumentException();
		}
		if (s == null) {
			return defaultValue;
		}
		s = StringUtils.goUpperCase(s);
		final HorizontalAlignment result = fromString(s);
		if (result == null) {
			return defaultValue;
		}
		return result;
	}

	public String getGraphVizValue() {
		return toString().substring(0, 1).toLowerCase();
	}

	public void draw(UGraphic ug, TextBlock tb, double padding, Dimension2D dimTotal) {
		if (this == HorizontalAlignment.LEFT) {
			tb.drawU(ug.apply(new UTranslate(padding, padding)));
		} else if (this == HorizontalAlignment.RIGHT) {
			final Dimension2D dimTb = tb.calculateDimension(ug.getStringBounder());
			tb.drawU(ug.apply(new UTranslate(dimTotal.getWidth() - dimTb.getWidth() - padding, padding)));
		} else if (this == HorizontalAlignment.CENTER) {
			final Dimension2D dimTb = tb.calculateDimension(ug.getStringBounder());
			tb.drawU(ug.apply(new UTranslate((dimTotal.getWidth() - dimTb.getWidth()) / 2, padding)));
		}

	}

}