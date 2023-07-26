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

import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.klimt.shape.UEllipse;

class ExtremityCircle extends Extremity {

	private static final double radius = 6;
	private final XPoint2D dest;
	private final boolean fill;
	private final HColor backgroundColor;

	@Override
	public XPoint2D somePoint() {
		return dest;
	}

	public static UDrawable create(XPoint2D center, boolean fill, double angle, HColor backgroundColor) {
		return new ExtremityCircle(center.getX(), center.getY(), fill, angle, backgroundColor);
	}

	private ExtremityCircle(double x, double y, boolean fill, double angle, HColor backgroundColor) {
		this.dest = new XPoint2D(x - radius * Math.cos(angle + Math.PI / 2),
				y - radius * Math.sin(angle + Math.PI / 2));
		this.backgroundColor = backgroundColor;
		this.fill = fill;
		// contact = new XPoint2D(p1.getX() - xContact * Math.cos(angle + Math.PI / 2),
		// p1.getY() - xContact
		// * Math.sin(angle + Math.PI / 2));
	}

	public void drawU(UGraphic ug) {

		ug = ug.apply(UStroke.withThickness(1.5));
		if (fill) {
			ug = ug.apply(HColors.changeBack(ug));
		} else {
			ug = ug.apply(backgroundColor.bg());
		}

		ug = ug.apply(new UTranslate(dest.getX() - radius, dest.getY() - radius));
		ug.draw(UEllipse.build(radius * 2, radius * 2));
	}
	
	@Override
	public double getDecorationLength() {
		return 12;
	}

}