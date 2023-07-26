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
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.UEllipse;

class ExtremityCircleConnect extends Extremity {

	private final double px;
	private final double py;
	private final XPoint2D dest;
	private final double radius = 6;
	private final double radius2 = 10;
	private final double ortho;
	private final HColor backgroundColor;

	@Override
	public XPoint2D somePoint() {
		return dest;
	}

	public ExtremityCircleConnect(XPoint2D p1, double ortho, HColor backgroundColor) {
		this.px = p1.getX() - radius;
		this.py = p1.getY() - radius;
		this.dest = new XPoint2D(p1.getX(), p1.getY());
		this.ortho = ortho;
		this.backgroundColor = backgroundColor;
	}

	public void drawU(UGraphic ug) {
		ug = ug.apply(UStroke.withThickness(1.5)).apply(backgroundColor.bg());
		ug.apply(new UTranslate(dest.getX() - radius, dest.getY() - radius)).draw(UEllipse.build(radius * 2, radius * 2));

		final double deg = -ortho * 180 / Math.PI + 90 - 45;
		final UEllipse arc1 = new UEllipse(2 * radius2, 2 * radius2, deg, 90);
		ug.apply(new UTranslate(dest.getX() - radius2, dest.getY() - radius2)).draw(arc1);
	}
	
	@Override
	public double getDecorationLength() {
		return 10;
	}

	// private XPoint2D getPointOnCircle(double angle) {
	// final double x = px + radius + radius2 * Math.cos(angle);
	// final double y = py + radius + radius2 * Math.sin(angle);
	// return new XPoint2D(x, y);
	// }
	//
	// static private void drawLine(UGraphic ug, double x, double y, Point2D p1,
	// Point2D p2) {
	// final double dx = p2.getX() - p1.getX();
	// final double dy = p2.getY() - p1.getY();
	// ug.draw(x + p1.getX(), y + p1.getY(), new ULine(dx, dy));
	//
	// }

}