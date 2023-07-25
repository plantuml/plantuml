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

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.shape.UPolygon;

public class ExtremityArrow extends Extremity {

	private UPolygon polygon = new UPolygon();
	private final ULine line;
	private final XPoint2D contact;

	@Override
	public XPoint2D somePoint() {
		return contact;
	}

	public ExtremityArrow(XPoint2D p1, double angle, XPoint2D center) {
		angle = manageround(angle);
		final int xContact = buildPolygon();
		polygon.rotate(angle + Math.PI / 2);
		polygon = polygon.translate(p1.getX(), p1.getY());
		contact = new XPoint2D(p1.getX() - xContact * Math.cos(angle + Math.PI / 2),
				p1.getY() - xContact * Math.sin(angle + Math.PI / 2));
		this.line = new ULine(center.getX() - contact.getX(), center.getY() - contact.getY());
	}

	public ExtremityArrow(XPoint2D p0, double angle) {
		this.line = null;
		angle = manageround(angle);
		buildPolygon();
		polygon.rotate(angle);
		polygon = polygon.translate(p0.getX(), p0.getY());
		contact = p0;
	}

	private int buildPolygon() {
		polygon.addPoint(0, 0);
		final int xWing = 9;
		final int yAperture = 4;
		polygon.addPoint(-xWing, -yAperture);
		final int xContact = 5;
		polygon.addPoint(-xContact, 0);
		polygon.addPoint(-xWing, yAperture);
		polygon.addPoint(0, 0);
		return xContact;
	}

	@Override
	public double getDecorationLength() {
		return 6;
	}

	public void drawU(UGraphic ug) {
		final HColor color = ug.getParam().getColor();
		if (color == null)
			ug = ug.apply(HColors.none().bg());
		else
			ug = ug.apply(color.bg());

		ug.draw(polygon);
		if (line != null && line.getLength() > 2)
			ug.apply(UTranslate.point(contact)).draw(line);

	}

	public void drawLineIfTransparent(UGraphic ug) {
		final XPoint2D pt1 = polygon.getPoint(0);
		final XPoint2D pt2 = polygon.getPoint(2);
		final ULine line = ULine.create(pt1, pt2);
		ug.apply(UTranslate.point(pt1)).draw(line);

	}

}
