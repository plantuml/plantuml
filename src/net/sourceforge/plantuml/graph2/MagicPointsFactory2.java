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
package net.sourceforge.plantuml.graph2;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class MagicPointsFactory2 {

	private final Point2D.Double p1;
	private final Point2D.Double p2;

	private final List<Point2D.Double> result = new ArrayList<Point2D.Double>();

	public MagicPointsFactory2(Point2D.Double p1, Point2D.Double p2) {
		this.p1 = p1;
		this.p2 = p2;
		final double dx = p2.x - p1.x;
		final double dy = p2.y - p1.y;

		final int interv = 5;
		final int intervAngle = 10;
		final double theta = Math.PI * 2 / intervAngle;
		for (int a = 0; a < 10; a++) {
			final AffineTransform at = AffineTransform.getRotateInstance(theta * a, p1.x, p1.y);
			for (int i = 0; i < interv * 2; i++) {
				final Point2D.Double p = new Point2D.Double(p1.x + dx * i / interv, p1.y + dy * i / interv);
				result.add((Point2D.Double) at.transform(p, null));
			}
		}

	}

	public List<Point2D.Double> get() {
		return result;
	}

}
