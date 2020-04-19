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

import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

class ExtremityStateLine2 extends Extremity {

	private UPolygon polygon = new UPolygon();
	private final Point2D dest;
	private final double radius = 5;
	private final double angle;
	
	@Override
	public Point2D somePoint() {
		return dest;
	}


	public ExtremityStateLine2(double angle, Point2D center) {
		this.angle = manageround(angle);
		polygon.addPoint(0, 0);
		this.dest = new Point2D.Double(center.getX(), center.getY());
		final int xWing = 9;
		final int yAperture = 4;
		polygon.addPoint(-xWing, -yAperture);
		final int xContact = 5;
		polygon.addPoint(-xContact, 0);
		polygon.addPoint(-xWing, yAperture);
		polygon.addPoint(0, 0);
		polygon.rotate(this.angle);
		polygon = polygon.translate(center.getX(), center.getY());
	}

	public void drawU(UGraphic ug) {
		ug.apply(ug.getParam().getColor().bg()).apply(new UTranslate(-radius * Math.cos(angle), -radius * Math.sin(angle))).draw(polygon);
		ug.apply(new UStroke(1.5)).apply(HColorUtils.WHITE.bg()).apply(new UTranslate(dest.getX() - radius, dest.getY() - radius)).draw(new UEllipse(radius * 2, radius * 2));
	}

}
