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
package net.sourceforge.plantuml.svek;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class CircleAndArrow implements UDrawable {

	private final AffineTransform at;
	private final AffineTransform at2;
	private int radius;
	private final Point2D center;
	private final Point2D p1;
	private final Point2D p2;
	private Point2D p3;
	private Point2D p4;

	public CircleAndArrow(Point2D p1, Point2D p2) {
		this.center = new Point2D.Double((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2);
		at = AffineTransform.getTranslateInstance(-center.getX(), -center.getY());
		at2 = AffineTransform.getTranslateInstance(center.getX(), center.getY());
		radius = (int) (p1.distance(p2) / 2);
		if (radius % 2 == 0) {
			radius--;
		}
		this.p1 = putOnCircle(p1);
		this.p2 = putOnCircle(p2);

		this.p3 = at.transform(this.p1, null);
		this.p3 = new Point2D.Double(p3.getY(), -p3.getX());
		this.p3 = at2.transform(p3, null);

		this.p4 = at.transform(this.p2, null);
		this.p4 = new Point2D.Double(p4.getY(), -p4.getX());
		this.p4 = at2.transform(p4, null);
	}

	private Point2D putOnCircle(Point2D p) {
		p = at.transform(p, null);
		final double coef = p.distance(new Point2D.Double()) / radius;
		p = new Point2D.Double(p.getX() / coef, p.getY() / coef);
		return at2.transform(p, null);
	}

	public void drawU(UGraphic ug) {
		final UShape circle = new UEllipse(radius * 2, radius * 2);
		ug.apply(new UTranslate(center.getX() - radius, center.getY() - radius)).draw(circle);
		// drawLine(ug, x, y, p1, p2);
		// drawLine(ug, x, y, p3, p4);
	}

	static private void drawLine(UGraphic ug, double x, double y, Point2D p1, Point2D p2) {
		final double dx = p2.getX() - p1.getX();
		final double dy = p2.getY() - p1.getY();
		ug.apply(new UTranslate(x + p1.getX(), y + p1.getY())).draw(new ULine(dx, dy));

	}

}
