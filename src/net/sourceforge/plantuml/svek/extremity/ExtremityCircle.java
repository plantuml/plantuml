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
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

class ExtremityCircle extends Extremity {

	private static final double radius = 6;
	private final Point2D dest;
	private final boolean fill;

	@Override
	public Point2D somePoint() {
		return dest;
	}

	public static UDrawable create(Point2D center, boolean fill, double angle) {
		return new ExtremityCircle(center.getX(), center.getY(), fill, angle);
	}

	private ExtremityCircle(double x, double y, boolean fill, double angle) {
		this.dest = new Point2D.Double(x - radius * Math.cos(angle + Math.PI / 2), y - radius
				* Math.sin(angle + Math.PI / 2));
		this.fill = fill;
		// contact = new Point2D.Double(p1.getX() - xContact * Math.cos(angle + Math.PI / 2), p1.getY() - xContact
		// * Math.sin(angle + Math.PI / 2));
	}

	public void drawU(UGraphic ug) {

		ug = ug.apply(new UStroke(1.5));
		if (fill) {
			ug = ug.apply(HColorUtils.changeBack(ug));
		} else {
			ug = ug.apply(HColorUtils.WHITE.bg());
		}

		ug = ug.apply(new UTranslate(dest.getX() - radius, dest.getY() - radius));
		ug.draw(new UEllipse(radius * 2, radius * 2));
	}

}
