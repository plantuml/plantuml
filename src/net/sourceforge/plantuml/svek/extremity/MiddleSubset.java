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
package net.sourceforge.plantuml.svek.extremity;

import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.UEllipse;
import net.sourceforge.plantuml.klimt.shape.ULine;

class MiddleSubset extends Extremity {

	private final double radius = 6;
	private final double length = 10;

	private final double angle;
	private final boolean reverse;

	public MiddleSubset(double angle, boolean reverse) {
		this.angle = angle;
		this.reverse = reverse;
	}

	@Override
	public XPoint2D somePoint() {
		return null;
	}

	public void drawU(UGraphic ug) {
		ug = ug.apply(UStroke.withThickness(1.5));

		final double rotate = reverse ? -45 : 135;

		ug.apply(new UTranslate(-radius, -radius)).draw(new UEllipse(2 * radius, 2 * radius, angle + rotate, 180));

		final double sin = Math.sin(Math.toRadians(angle + rotate));
		final double cos = Math.cos(Math.toRadians(angle + rotate));

		ug.apply(new UTranslate(radius * cos, radius * -sin)).draw(new ULine(length * sin, length * cos));
		ug.apply(new UTranslate(radius * -cos, radius * sin)).draw(new ULine(length * sin, length * cos));
	}

	@Override
	public double getDecorationLength() {
		return radius;
	}

}
