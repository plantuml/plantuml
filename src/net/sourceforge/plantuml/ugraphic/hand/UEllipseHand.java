/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
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
 * Original Author:  Adrian Vogt
 *
 */
package net.sourceforge.plantuml.ugraphic.hand;

import net.sourceforge.plantuml.ugraphic.Shadowable;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UPolygon;

public class UEllipseHand {

	private final Shadowable poly;

	public UEllipseHand(UEllipse source) {

		if (source.getStart() != 0 || source.getExtend() != 0) {
			this.poly = source;
			return;
		}
		final UPolygon result = new UPolygon();
		final double width = source.getWidth();
		final double height = source.getHeight();
		double angle = 0;
		while (angle < Math.PI * 2) {
			angle += (10 + Math.random() * 10) * Math.PI / 180;
			final double variation = 1 + (Math.random() - 0.5) / 8;
			final double x = width / 2 + Math.cos(angle) * width * variation / 2;
			final double y = height / 2 + Math.sin(angle) * height * variation / 2;
			// final Point2D.Double p = new Point2D.Double(x, y);
			result.addPoint(x, y);
		}

		this.poly = result;
		this.poly.setDeltaShadow(source.getDeltaShadow());
	}

	public Shadowable getHanddrawn() {
		return this.poly;
	}

}
