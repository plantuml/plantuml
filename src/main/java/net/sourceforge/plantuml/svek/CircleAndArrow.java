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
package net.sourceforge.plantuml.svek;

import java.awt.geom.AffineTransform;

import net.sourceforge.plantuml.klimt.UShape;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.klimt.shape.UEllipse;

class CircleAndArrow implements UDrawable {

	private final AffineTransform at;
	private final AffineTransform at2;
	private int radius;
	private final XPoint2D center;
	private final XPoint2D p1;
	private final XPoint2D p2;
	private XPoint2D p3;
	private XPoint2D p4;

	public CircleAndArrow(XPoint2D p1, XPoint2D p2) {
		this.center = new XPoint2D((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2);
		at = AffineTransform.getTranslateInstance(-center.getX(), -center.getY());
		at2 = AffineTransform.getTranslateInstance(center.getX(), center.getY());
		radius = (int) (p1.distance(p2) / 2);
		if (radius % 2 == 0) {
			radius--;
		}
		this.p1 = putOnCircle(p1);
		this.p2 = putOnCircle(p2);

		this.p3 = this.p1.transform(at);
		this.p3 = new XPoint2D(p3.getY(), -p3.getX());
		this.p3 = this.p3.transform(at2);

		this.p4 = this.p2.transform(at);
		this.p4 = new XPoint2D(p4.getY(), -p4.getX());
		this.p4 = this.p4.transform(at2);
	}

	private XPoint2D putOnCircle(XPoint2D p) {
		p = p.transform(at);
		final double coef = p.distance(new XPoint2D(0, 0)) / radius;
		p = new XPoint2D(p.getX() / coef, p.getY() / coef);
		return p.transform(at2);
	}

	public void drawU(UGraphic ug) {
		final UShape circle = UEllipse.build(radius * 2, radius * 2);
		ug.apply(new UTranslate(center.getX() - radius, center.getY() - radius)).draw(circle);
		// drawLine(ug, x, y, p1, p2);
		// drawLine(ug, x, y, p3, p4);
	}

}
