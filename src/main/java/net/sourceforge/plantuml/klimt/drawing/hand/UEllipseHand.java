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
 * Original Author:  Adrian Vogt
 *
 */
package net.sourceforge.plantuml.klimt.drawing.hand;

import java.util.Random;

import net.sourceforge.plantuml.klimt.Shadowable;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.UEllipse;
import net.sourceforge.plantuml.klimt.shape.UPolygon;

public class UEllipseHand {

	private Shadowable poly;
	private final Random rnd;

	private double randomMe() {
		return rnd.nextDouble();
	}

	public UEllipseHand(UEllipse source, Random rnd) {
		this.rnd = rnd;
		if (source.getStart() != 0 || source.getExtend() != 0) {
			this.poly = source;
			return;
		}
		poly = new UPolygon();
		final double width = source.getWidth();
		final double height = source.getHeight();
		double angle = 0;
		if (width == height) {
			while (angle < Math.PI * 2) {
				angle += (10 + randomMe() * 10) * Math.PI / 180;
				final double variation = 1 + (randomMe() - 0.5) / 8;
				final double x = width / 2 + Math.cos(angle) * width * variation / 2;
				final double y = height / 2 + Math.sin(angle) * height * variation / 2;
				// final XPoint2D p = new XPoint2D(x, y);
				((UPolygon) poly).addPoint(x, y);
			}
		} else {
			while (angle < Math.PI * 2) {
				angle += Math.PI / 20;
				final XPoint2D pt = getPoint(width, height, angle);
				((UPolygon) poly).addPoint(pt.getX(), pt.getY());
			}

		}

		this.poly.setDeltaShadow(source.getDeltaShadow());
	}

	private XPoint2D getPoint(double width, double height, double angle) {
		final double x = width / 2 + Math.cos(angle) * width / 2;
		final double y = height / 2 + Math.sin(angle) * height / 2;
		final double variation = (randomMe() - 0.5) / 50;
		return new XPoint2D(x + variation * width, y + variation * height);

	}

	public Shadowable getHanddrawn() {
		return this.poly;
	}

}
