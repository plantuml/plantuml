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

import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.UPolygon;

class ExtremityTriangle extends Extremity {

	private UPolygon polygon = new UPolygon();
	private final boolean fill;
	private final HColor backgroundColor;
	private final XPoint2D contact;
	private final int decorationLength;

	@Override
	public XPoint2D somePoint() {
		return contact;
	}

	public ExtremityTriangle(XPoint2D p1, double angle, boolean fill, HColor backgroundColor, int xWing, int yAperture,
			int decorationLength) {
		this.backgroundColor = backgroundColor;
		this.fill = fill;
		this.contact = new XPoint2D(p1.getX(), p1.getY());
		this.decorationLength = decorationLength;
		angle = manageround(angle);
		polygon.addPoint(0, 0);

		polygon.addPoint(-xWing, -yAperture);
		polygon.addPoint(-xWing, yAperture);
		polygon.addPoint(0, 0);
		polygon.rotate(angle + Math.PI / 2);
		polygon = polygon.translate(p1.getX(), p1.getY());
	}

	public void drawU(UGraphic ug) {
		if (backgroundColor != null)
			ug = ug.apply(backgroundColor.bg());
		else if (fill)
			ug = ug.apply(HColors.changeBack(ug));
		ug.draw(polygon);
	}

	@Override
	public double getDecorationLength() {
		return decorationLength;
	}

}
