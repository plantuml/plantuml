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

import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

class ExtremityHalfArrow extends Extremity {

	private final ULine line;
	private final ULine otherLine;
	private final Point2D contact;

	@Override
	public Point2D somePoint() {
		return contact;
	}

	public ExtremityHalfArrow(Point2D p1, double angle, Point2D center) {
		angle = manageround(angle);
		final AffineTransform rotate = AffineTransform.getRotateInstance(angle + Math.PI / 2);
		final int xWing = 9;
		final int yAperture = 4;
		final Point2D other = new Point2D.Double(-xWing, -yAperture);
		rotate.transform(other, other);
		this.contact = p1;
		this.line = new ULine(center.getX() - contact.getX(), center.getY() - contact.getY());
		this.otherLine = new ULine(other.getX(), other.getY());
	}

	public ExtremityHalfArrow(Point2D p0, double angle) {
		throw new UnsupportedOperationException();
	}

	public void drawU(UGraphic ug) {
		ug = ug.apply(HColorUtils.changeBack(ug));
		if (line != null && line.getLength() > 2) {
			ug.apply(new UTranslate(contact.getX(), contact.getY())).draw(line);
			ug.apply(new UTranslate(contact.getX(), contact.getY())).draw(otherLine);
		}
	}

}
