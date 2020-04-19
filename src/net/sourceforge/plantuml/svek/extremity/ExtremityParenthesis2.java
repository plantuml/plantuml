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

import net.sourceforge.plantuml.svek.image.EntityImageLollipopInterfaceEye2;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class ExtremityParenthesis2 extends Extremity {

	private final Point2D contact;
	private final Point2D center;

	private final double ortho;
	private final double ang = 30;

	public ExtremityParenthesis2(Point2D contact, double ortho, Point2D p1) {
		this.contact = new Point2D.Double(contact.getX(), contact.getY());
		this.ortho = ortho;
		final double dx = p1.getX() - contact.getX();
		final double dy = p1.getY() - contact.getY();
		final double distance1 = Math.round(contact.distance(p1));
		// System.err.println("distance=" + distance1);
		final double len = Math.round(distance1 + EntityImageLollipopInterfaceEye2.SIZE / 2);
		this.center = new Point2D.Double(contact.getX() + dx / distance1 * len, contact.getY() + dy / distance1 * len);
	}
	
	@Override
	public Point2D somePoint() {
		return contact;
	}


	public void drawU(UGraphic ug) {
		final double deg = -ortho * 180 / Math.PI + 90 - ang;
		// final Point2D other = new Point2D.Double(contact.getX() + 10 * Math.cos(deg), contact.getY() + 10
		// * Math.sin(deg));
		// final ULine line = new ULine(1, 1);
		// ug.apply(UChangeColor.nnn(HtmlColorUtils.GREEN)).apply(new UTranslate(contact.getX(), contact.getY()))
		// .draw(line);
		// ug.apply(UChangeColor.nnn(HtmlColorUtils.BLACK)).apply(new UTranslate(center.getX(),
		// center.getY())).draw(line);
		// // final UEllipse arc1 = new UEllipse(2 * radius2, 2 * radius2, deg, 2 * ang);
		// // ug.apply(new UStroke(1.5)).apply(new UTranslate(dest.getX() - radius2, dest.getY() - radius2)).draw(arc1);
		//
		final double size = Math.round(contact.distance(center));
		// System.err.println("size=" + size);
		final UEllipse circle = new UEllipse(size * 2, size * 2, deg, 2 * ang);
		ug.apply(new UTranslate(center.getX() - size, center.getY() - size)).draw(circle);

	}
}
