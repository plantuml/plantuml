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

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class ExtremityCircleCrowfoot extends Extremity {

	private final Point2D contact;
	private final double angle;
	private final double radius = 4;
	

	@Override
	public Point2D somePoint() {
		return contact;
	}

	public ExtremityCircleCrowfoot(Point2D p1, double angle) {
		this.contact = new Point2D.Double(p1.getX(), p1.getY());
		this.angle = manageround(angle + Math.PI / 2);
	}

	public void drawU(UGraphic ug) {
		final int xWing = 8;
		final int yAperture = 6;
		final AffineTransform rotate = AffineTransform.getRotateInstance(this.angle);
		Point2D middle = new Point2D.Double(0, 0);
		Point2D left = new Point2D.Double(0, -yAperture);
		Point2D base = new Point2D.Double(-xWing, 0);
		Point2D right = new Point2D.Double(0, yAperture);
		Point2D circleBase = new Point2D.Double(-xWing-radius-2, 0);
		rotate.transform(left, left);
		rotate.transform(base, base);
		rotate.transform(right, right);
		rotate.transform(circleBase, circleBase);

		drawLine(ug, contact.getX(), contact.getY(), base, left);
		drawLine(ug, contact.getX(), contact.getY(), base, right);
		drawLine(ug, contact.getX(), contact.getY(), base, middle);
		ug.apply(new UTranslate(contact.getX()+circleBase.getX()-radius, contact.getY()+circleBase.getY()-radius)).draw(new UEllipse(2*radius, 2*radius));
	}

	static private void drawLine(UGraphic ug, double x, double y, Point2D p1, Point2D p2) {
		final double dx = p2.getX() - p1.getX();
		final double dy = p2.getY() - p1.getY();
		ug.apply(new UTranslate(x + p1.getX(), y + p1.getY())).draw(new ULine(dx, dy));
	}
	
}
