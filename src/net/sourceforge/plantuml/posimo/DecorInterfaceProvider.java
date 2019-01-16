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
package net.sourceforge.plantuml.posimo;

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.cucadiagram.LinkStyle;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class DecorInterfaceProvider implements Decor {

	private final double radius = 5;
	private final double radius2 = 9;
	private final LinkStyle style;

	// private final double distanceCircle = 16;

	public DecorInterfaceProvider(LinkStyle style) {
//		if (style != LinkStyle.__toremove_INTERFACE_PROVIDER && style != LinkStyle.__toremove_INTERFACE_USER) {
//			throw new IllegalArgumentException();
//		}
		this.style = style;
	}

	public void drawDecor(UGraphic ug, Point2D start, double direction) {
		final double cornerX = start.getX() - radius;
		final double cornerY = start.getY() - radius;
		final double cornerX2 = start.getX() - radius2 - 0 * Math.sin(direction * Math.PI / 180.0);
		final double cornerY2 = start.getY() - radius2 - 0 * Math.cos(direction * Math.PI / 180.0);

//		if (style == LinkStyle.__toremove_INTERFACE_USER) {
//			direction += 180;
//		}
		if (direction >= 360) {
			direction -= 360;
		}

		final UEllipse arc = new UEllipse(2 * radius2, 2 * radius2, direction + 15, 180 - 30);
		ug = ug.apply(new UStroke(1.5));
		ug.apply(new UTranslate(cornerX2, cornerY2)).draw(arc);
		ug.apply(new UTranslate(cornerX, cornerY)).draw(new UEllipse(2 * radius, 2 * radius));
	}

}
