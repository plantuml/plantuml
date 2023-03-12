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
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.UEllipse;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.shape.UPolygon;

class ExtremityStateLine1 extends Extremity {

	private UPolygon polygon = new UPolygon();
	private final XPoint2D dest;
	private final double radius = 7;
	private final double angle;

	@Override
	public XPoint2D somePoint() {
		return dest;
	}

	public ExtremityStateLine1(double angle, XPoint2D center) {
		this.angle = manageround(angle);
		polygon.addPoint(0, 0);
		this.dest = new XPoint2D(center.getX(), center.getY());
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
		ug.apply(ug.getParam().getColor().bg())
				.apply(new UTranslate(-radius * Math.cos(angle), -radius * Math.sin(angle))).draw(polygon);
		ug = ug.apply(HColors.WHITE.bg());
		ug.apply(UStroke.withThickness(1.5)).apply(new UTranslate(dest.getX() - radius, dest.getY() - radius))
				.draw(UEllipse.build(radius * 2, radius * 2));
		drawLine(ug, getPointOnCircle(dest.getX(), dest.getY(), Math.PI / 4),
				getPointOnCircle(dest.getX(), dest.getY(), Math.PI + Math.PI / 4));
		drawLine(ug, getPointOnCircle(dest.getX(), dest.getY(), -Math.PI / 4),
				getPointOnCircle(dest.getX(), dest.getY(), Math.PI - Math.PI / 4));
	}

	private XPoint2D getPointOnCircle(double centerX, double centerY, double angle) {
		final double x = centerX + radius * Math.cos(angle);
		final double y = centerY + radius * Math.sin(angle);
		return new XPoint2D(x, y);
	}

	static private void drawLine(UGraphic ug, XPoint2D p1, XPoint2D p2) {
		final double dx = p2.getX() - p1.getX();
		final double dy = p2.getY() - p1.getY();
		ug.apply(new UTranslate(p1.getX(), p1.getY())).draw(new ULine(dx, dy));

	}

}
