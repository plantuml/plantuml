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
package net.sourceforge.plantuml.svek.extremity;

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

class ExtremityDiamond extends Extremity {

	private UPolygon polygon = new UPolygon();
	private final boolean fill;
	private final Point2D contact;
	private final HColor backgroundColor;

	@Override
	public Point2D somePoint() {
		return contact;
	}

	public ExtremityDiamond(Point2D p1, double angle, boolean fill, HColor backgroundColor) {
		this.fill = fill;
		this.backgroundColor = backgroundColor;
		this.contact = new Point2D.Double(p1.getX(), p1.getY());
		angle = manageround(angle);
		polygon.addPoint(0, 0);
		final int xWing = 6;
		final int yAperture = 4;
		polygon.addPoint(-xWing, -yAperture);
		polygon.addPoint(-xWing * 2, 0);
		polygon.addPoint(-xWing, yAperture);
		polygon.addPoint(0, 0);
		polygon.rotate(angle + Math.PI / 2);
		polygon = polygon.translate(p1.getX(), p1.getY());
	}

	public void drawU(UGraphic ug) {
		if (fill) {
			ug = ug.apply(HColorUtils.changeBack(ug));
		} else {
			ug = ug.apply(backgroundColor.bg());
		}
		ug.draw(polygon);
	}

	@Override
	public Point2D isTooSmallSoGiveThePointCloserToThisOne(Point2D pt) {
		Point2D result = null;
		for (Point2D p : polygon.getPoints()) {
			if (result == null || p.distance(pt) < result.distance(pt)) {
				result = p;
			}
		}
		return result;
	}

}
