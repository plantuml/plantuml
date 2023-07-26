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
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.ULine;

class ExtremityHalfArrow extends Extremity {

	private final ULine line;
	private final ULine otherLine;
	private final XPoint2D contact;

	@Override
	public XPoint2D somePoint() {
		return contact;
	}

	public ExtremityHalfArrow(XPoint2D p1, double angle, XPoint2D center) {
		angle = manageround(angle);
		final AffineTransform rotate = AffineTransform.getRotateInstance(angle + Math.PI / 2);
		final int xWing = 9;
		final int yAperture = 4;
		XPoint2D other = new XPoint2D(-xWing, -yAperture);
		other = other.transform(rotate);

		this.contact = p1;
		this.line = new ULine(center.getX() - contact.getX(), center.getY() - contact.getY());
		this.otherLine = new ULine(other.getX(), other.getY());
	}

	public ExtremityHalfArrow(XPoint2D p0, double angle) {
		angle = manageround(angle);
		final int xWing = 9;
		final int yAperture = 4;

		this.contact = p0;

		final XPoint2D other = new XPoint2D(-xWing, -yAperture).transform(AffineTransform.getRotateInstance(angle));
		final XPoint2D other2 = new XPoint2D(-8, 0).transform(AffineTransform.getRotateInstance(angle));

		this.line = new ULine(other.getX(), other.getY());
		this.otherLine = new ULine(other2.getX(), other2.getY());
	}

	public void drawU(UGraphic ug) {
		ug = ug.apply(HColors.changeBack(ug));
		if (line != null && line.getLength() > 2) {
			final UTranslate position = UTranslate.point(contact);
			ug.apply(position).draw(line);
			ug.apply(position).draw(otherLine);
		}
	}

}
