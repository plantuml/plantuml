/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 *
 * Revision $Revision: 4236 $
 * 
 */
package net.sourceforge.plantuml.posimo;

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.cucadiagram.LinkStyle;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class DecorInterfaceProvider implements Decor {

	private final double radius = 5;
	private final double radius2 = 9;
	private final LinkStyle style;

	// private final double distanceCircle = 16;

	public DecorInterfaceProvider(LinkStyle style) {
		if (style != LinkStyle.INTERFACE_PROVIDER && style != LinkStyle.INTERFACE_USER) {
			throw new IllegalArgumentException();
		}
		this.style = style;
	}

	public void drawDecor(UGraphic ug, Point2D start, double direction) {
		final double cornerX = start.getX() - radius;
		final double cornerY = start.getY() - radius;
		final double cornerX2 = start.getX() - radius2 - 0 * Math.sin(direction * Math.PI / 180.0);
		final double cornerY2 = start.getY() - radius2 - 0 * Math.cos(direction * Math.PI / 180.0);

		if (style == LinkStyle.INTERFACE_USER) {
			direction += 180;
		}
		if (direction >= 360) {
			direction -= 360;
		}

		final UEllipse arc = new UEllipse(2 * radius2, 2 * radius2, direction + 15, 180 - 30);
		final UStroke old = ug.getParam().getStroke();
		ug.getParam().setStroke(new UStroke(1.5));
		ug.draw(cornerX2, cornerY2, arc);
		ug.draw(cornerX, cornerY, new UEllipse(2 * radius, 2 * radius));
		ug.getParam().setStroke(old);
	}

}
