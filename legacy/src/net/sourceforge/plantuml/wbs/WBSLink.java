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
package net.sourceforge.plantuml.wbs;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XLine2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.geom.XRectangle2D;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.svek.extremity.ExtremityArrow;

class WBSLink implements UDrawable {

	private final WElement element1;
	private final WElement element2;
	private final HColor color;

	public WBSLink(WElement element1, WElement element2, HColor color) {
		this.element1 = element1;
		this.element2 = element2;
		this.color = color;
		if (color == null)
			throw new IllegalArgumentException();
	}

	public final WElement getElement1() {
		return element1;
	}

	public final WElement getElement2() {
		return element2;
	}

	public void drawU(UGraphic ug) {
		final WElement element1 = getElement1();
		final WElement element2 = getElement2();
		final UTranslate position1 = element1.getPosition();
		final UTranslate position2 = element2.getPosition();
		final XDimension2D dim1 = element1.getDimension();
		final XDimension2D dim2 = element2.getDimension();
		if (position1 != null && position2 != null) {

			final XRectangle2D rect1 = new XRectangle2D(position1.getDx(), position1.getDy(), dim1.getWidth(),
					dim1.getHeight());
			final XRectangle2D rect2 = new XRectangle2D(position2.getDx(), position2.getDy(), dim2.getWidth(),
					dim2.getHeight());

			XLine2D line = new XLine2D(rect1.getCenterX(), rect1.getCenterY(), rect2.getCenterX(), rect2.getCenterY());

			final XPoint2D c1 = rect1.intersect(line);
			final XPoint2D c2 = rect2.intersect(line);

			line = XLine2D.line(c1, c2);
			ug = ug.apply(color);
			line.drawU(ug);

			final double angle = line.getAngle();
			final ExtremityArrow arrow = new ExtremityArrow(c2, angle);
			arrow.drawU(ug);

		}

	}

}
