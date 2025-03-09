/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
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
package net.sourceforge.plantuml.ebnf;

import net.sourceforge.plantuml.klimt.CopyForegroundColorToBackgroundColor;
import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.shape.UDrawable;

enum CornerType {
	NW, NE, SE, SW;
}

public class CornerCurved implements UDrawable {

	private final double delta;
	private final CornerType type;
	private final boolean withArrow;

	private CornerCurved(CornerType type, double delta, boolean withArrow) {
		this.delta = delta;
		this.type = type;
		this.withArrow = withArrow;
		if (delta <= 0)
			throw new IllegalArgumentException();
	}

	public static UDrawable createSW(double delta) {
		return new CornerCurved(CornerType.SW, delta, false);
	}

	public static UDrawable createSE(double delta) {
		return new CornerCurved(CornerType.SE, delta, false);
	}

	public static UDrawable createNE(double delta) {
		return new CornerCurved(CornerType.NE, delta, false);
	}

	public static UDrawable createNE_arrow(double delta) {
		return new CornerCurved(CornerType.NE, delta, true);
	}

	public static UDrawable createNW(double delta) {
		return new CornerCurved(CornerType.NW, delta, false);
	}

	public static UDrawable createNW_arrow(double delta) {
		return new CornerCurved(CornerType.NW, delta, true);
	}

	@Override
	public void drawU(UGraphic ug) {
		final UPath path = UPath.none();
		final double a = delta / 4;

		switch (type) {
		case SW:
			path.moveTo(0, -delta);
			path.cubicTo(0, -a, a, 0, delta, 0);
			break;
		case SE:
			path.moveTo(0, -delta);
			path.cubicTo(0, -a, -a, 0, -delta, 0);
			break;
		case NE:
			path.moveTo(-delta, 0);
			path.cubicTo(-a, 0, 0, a, 0, delta);
			if (withArrow)
				ug.apply(new CopyForegroundColorToBackgroundColor()).apply(UTranslate.dy(delta - 5))
						.draw(ETile.getArrowToBottom());
			break;
		case NW:
			path.moveTo(0, delta);
			path.cubicTo(0, a, a, 0, delta, 0);
			if (withArrow)
				ug.apply(new CopyForegroundColorToBackgroundColor()).apply(UTranslate.dy(delta))
						.draw(ETile.getArrowToTop());
			break;
		}

		ug.draw(path);
	}

}
