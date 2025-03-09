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

import net.sourceforge.plantuml.klimt.UPath;

public class Zigzag {

	private final double width;
	private final double height;
	private final double ctrl;

	public Zigzag(double ctrl, double width, double height) {
		this.ctrl = ctrl;
		this.width = width;
		this.height = height;
	}

	public UPath pathDown() {
		final UPath path = UPath.none();
		final double xm = width / 2;
		final double ym = height / 2;

		path.moveTo(0, 0);

		final double cp1x1 = ctrl;
		final double cp1y1 = 0;
		final double cp1x2 = xm;
		final double cp1y2 = ym - ctrl;
		path.cubicTo(cp1x1, cp1y1, cp1x2, cp1y2, xm, ym);

		final double cp2x1 = xm;
		final double cp2y1 = ym + ctrl;
		final double cp2x2 = width - ctrl;
		final double cp2y2 = height;
		path.cubicTo(cp2x1, cp2y1, cp2x2, cp2y2, width, height);

		return path;
	}

	public UPath pathUp() {
		final UPath path = UPath.none();
		final double xm = width / 2;
		final double ym = height / 2;

		path.moveTo(0, height);

		final double cp1x1 = ctrl;
		final double cp1y1 = height;
		final double cp1x2 = xm;
		final double cp1y2 = ym + ctrl;
		path.cubicTo(cp1x1, cp1y1, cp1x2, cp1y2, xm, ym);

		final double cp2x1 = xm;
		final double cp2y1 = ym - ctrl;
		final double cp2x2 = width - ctrl;
		final double cp2y2 = 0;
		path.cubicTo(cp2x1, cp2y1, cp2x2, cp2y2, width, 0);
		
		return path;
	}

}
