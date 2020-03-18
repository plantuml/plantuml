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
package net.sourceforge.plantuml.ugraphic.color;

import java.awt.Color;

public enum ColorOrder {
	RGB, RBG, GRB, GBR, BRG, BGR;

	public Color getColor(Color color) {
		if (this == RGB) {
			return new Color(color.getRed(), color.getGreen(), color.getBlue());
		}
		if (this == RBG) {
			return new Color(color.getRed(), color.getBlue(), color.getGreen());
		}
		if (this == GRB) {
			return new Color(color.getGreen(), color.getRed(), color.getBlue());
		}
		if (this == GBR) {
			return new Color(color.getGreen(), color.getBlue(), color.getRed());
		}
		if (this == BRG) {
			return new Color(color.getBlue(), color.getRed(), color.getGreen());
		}
		if (this == BGR) {
			return new Color(color.getBlue(), color.getGreen(), color.getRed());
		}
		throw new IllegalStateException();
	}

	public Color getReverse(Color color) {
		color = this.getColor(color);
		return new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
	}

	public static ColorOrder fromString(String order) {
		try {
			return ColorOrder.valueOf(order.toUpperCase());
		} catch (Exception e) {
			return null;
		}
	}

}
