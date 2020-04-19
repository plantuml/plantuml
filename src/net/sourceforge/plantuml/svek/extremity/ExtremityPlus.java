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

import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

class ExtremityPlus extends Extremity {

	private final UEllipse circle;
	private final double px;
	private final double py;
	private static final double radius = 8;
	private final double angle;

	private ExtremityPlus(double x, double y, double angle) {
		this.angle = angle;
		this.circle = new UEllipse(2 * radius, 2 * radius);
		this.px = x;
		this.py = y;
	}
	
	@Override
	public Point2D somePoint() {
		return new Point2D.Double(px, py);
	}


	public static UDrawable create(Point2D p1, double angle) {
		final double x = p1.getX() - radius + radius * Math.sin(angle);
		final double y = p1.getY() - radius - radius * Math.cos(angle);
		return new ExtremityPlus(x, y, angle);
	}

	public void drawU(UGraphic ug) {
		ug.apply(HColorUtils.WHITE.bg()).apply(new UTranslate(px + 0, py + 0)).draw(circle);
		drawLine(ug, 0, 0, getPointOnCircle(angle - Math.PI / 2), getPointOnCircle(angle + Math.PI / 2));
		drawLine(ug, 0, 0, getPointOnCircle(angle), getPointOnCircle(angle + Math.PI));
	}

	private Point2D getPointOnCircle(double angle) {
		final double x = px + radius + radius * Math.cos(angle);
		final double y = py + radius + radius * Math.sin(angle);
		return new Point2D.Double(x, y);
	}

	static private void drawLine(UGraphic ug, double x, double y, Point2D p1, Point2D p2) {
		final double dx = p2.getX() - p1.getX();
		final double dy = p2.getY() - p1.getY();
		ug.apply(new UTranslate(x + p1.getX(), y + p1.getY())).draw(new ULine(dx, dy));

	}

}
