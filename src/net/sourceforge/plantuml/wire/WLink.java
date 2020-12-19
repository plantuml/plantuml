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
package net.sourceforge.plantuml.wire;

import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class WLink {

	private final UTranslate pos1;
	private final double pos2x;
	private final WLinkType type;
	private final HColor color;

	public WLink(WBlock block1, String x1, String y1, WBlock block2, WLinkType type, HColor color) {
		pos1 = block1.getNextOut(x1, y1, type);
		pos2x = block2.getAbsolutePosition("0", "0").getDx();
		this.type = type;
		this.color = color == null ? HColorUtils.BLACK : color;
	}

	public void drawMe(UGraphic ug) {

		final double dx = pos2x - pos1.getDx() - 2;

		ug = ug.apply(color).apply(color.bg());

		if (type == WLinkType.NORMAL) {
			final UPath path = new UPath();
			path.moveTo(0, 0);
			path.lineTo(-5, -5);
			path.lineTo(-5, 5);
			path.lineTo(0, 0);
			path.closePath();
			ug.apply(pos1.compose(UTranslate.dx(dx))).draw(path);
			ug.apply(pos1.compose(UTranslate.dx(1))).draw(ULine.hline(dx));

		} else if (type == WLinkType.BUS) {
			final UPath path = new UPath();
			path.moveTo(0, 0);
			path.lineTo(dx - 15, 0);
			path.lineTo(dx - 15, -5);
			path.lineTo(dx, 5);
			path.lineTo(dx - 15, 15);
			path.lineTo(dx - 15, 10);
			path.lineTo(0, 10);
			path.lineTo(0, 0);
			path.closePath();
			ug.apply(pos1.compose(UTranslate.dx(1))).draw(path);
		}

	}

}
