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

import java.awt.geom.AffineTransform;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.ULine;

class ExtremityDoubleLine extends Extremity {

	private final XPoint2D contact;
	private final double angle;
	private final double lineHeight = 4;

	@Override
	public XPoint2D somePoint() {
		return contact;
	}

	public ExtremityDoubleLine(XPoint2D p1, double angle) {
		this.contact = new XPoint2D(p1.getX(), p1.getY());
		this.angle = manageround(angle + Math.PI / 2);
	}

	public void drawU(UGraphic ug) {
		final int xWing = 4;
		final AffineTransform rotate = AffineTransform.getRotateInstance(this.angle);
		XPoint2D firstLineTop = new XPoint2D(-xWing, -lineHeight);
		XPoint2D firstLineBottom = new XPoint2D(-xWing, lineHeight);
		XPoint2D secondLineTop = new XPoint2D(-xWing - 3, -lineHeight);
		XPoint2D secondLineBottom = new XPoint2D(-xWing - 3, lineHeight);

		XPoint2D middle = new XPoint2D(0, 0);
		XPoint2D base = new XPoint2D(-xWing - 4, 0);

		middle = middle.transform(rotate);
		base = base.transform(rotate);
		firstLineTop = firstLineTop.transform(rotate);
		firstLineBottom = firstLineBottom.transform(rotate);
		secondLineTop = secondLineTop.transform(rotate);
		secondLineBottom = secondLineBottom.transform(rotate);

		drawLine(ug, contact.getX(), contact.getY(), firstLineTop, firstLineBottom);
		drawLine(ug, contact.getX(), contact.getY(), secondLineTop, secondLineBottom);
		drawLine(ug, contact.getX(), contact.getY(), base, middle);
	}
	
	@Override
	public double getDecorationLength() {
		return 8;
	}

	static private void drawLine(UGraphic ug, double x, double y, XPoint2D p1, XPoint2D p2) {
		final double dx = p2.getX() - p1.getX();
		final double dy = p2.getY() - p1.getY();
		ug.apply(new UTranslate(x + p1.getX(), y + p1.getY())).draw(new ULine(dx, dy));
	}

}
