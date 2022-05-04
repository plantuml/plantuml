/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
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
package net.sourceforge.plantuml.svek;

import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public final class RoundedNorth implements UDrawable {

	private final double width;
	private final double height;
	private final HColor backColor;
	private final double rounded;

	public RoundedNorth(double width, double height, HColor backColor, double rounded) {
		if (width == 0)
			throw new IllegalArgumentException();
		if (height == 0)
			throw new IllegalArgumentException();

		this.width = width;
		this.height = height;
		this.rounded = rounded;
		this.backColor = backColor;
	}

	public void drawU(UGraphic ug) {
		if (HColorUtils.isTransparent(backColor))
			return;

		final UShape header;
		if (rounded == 0) {
			header = new URectangle(width, height);
		} else {
			final UPath path = new UPath();
			path.moveTo(rounded / 2, 0);
			path.lineTo(width - rounded / 2, 0);
			path.arcTo(rounded / 2, rounded / 2, 0, 0, 1, width, rounded / 2);
			path.lineTo(width, height);
			path.lineTo(0, height);
			path.lineTo(0, rounded / 2);
			path.arcTo(rounded / 2, rounded / 2, 0, 0, 1, rounded / 2, 0);
			path.closePath();
			header = path;
		}
		ug.apply(new UStroke()).apply(backColor).apply(backColor.bg()).draw(header);

	}
}
