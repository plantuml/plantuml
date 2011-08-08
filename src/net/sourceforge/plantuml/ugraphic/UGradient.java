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
 */
package net.sourceforge.plantuml.ugraphic;

import java.awt.Color;

import net.sourceforge.plantuml.graphic.HtmlColor;

public class UGradient {

	private final HtmlColor color1;
	private final HtmlColor color2;

	public UGradient(HtmlColor color1, HtmlColor color2) {
		if (color1 == null || color2 == null) {
			throw new IllegalArgumentException();
		}
		this.color1 = color1;
		this.color2 = color2;
	}

	public final HtmlColor getColor1() {
		return color1;
	}

	public final HtmlColor getColor2() {
		return color2;
	}

	public final Color getColor(ColorMapper mapper, double coeff) {
		if (coeff > 1 || coeff < 0) {
			throw new IllegalArgumentException("c=" + coeff);
		}
		final Color c1 = mapper.getMappedColor(color1);
		final Color c2 = mapper.getMappedColor(color2);
		final int vred = c2.getRed() - c1.getRed();
		final int vgreen = c2.getGreen() - c1.getGreen();
		final int vblue = c2.getBlue() - c1.getBlue();

		final int red = c1.getRed() + (int) (coeff * vred);
		final int green = c1.getGreen() + (int) (coeff * vgreen);
		final int blue = c1.getBlue() + (int) (coeff * vblue);

		return new Color(red, green, blue);

	}

}
