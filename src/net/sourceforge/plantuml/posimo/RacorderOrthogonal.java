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

import java.awt.geom.CubicCurve2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class RacorderOrthogonal extends RacorderAbstract implements Racorder {

	public DotPath getRacordIn(Rectangle2D rect, Line2D tangeante) {

		final Point2D in = tangeante.getP1();

		final DotPath result = new DotPath();
		Point2D inter = null;

		if (in.getX() > rect.getMinX() && in.getX() < rect.getMaxX()) {
			if (in.getY() < rect.getMinY()) {
				inter = new Point2D.Double(in.getX(), rect.getMinY());
			} else if (in.getY() > rect.getMaxY()) {
				inter = new Point2D.Double(in.getX(), rect.getMaxY());
			} else {
				throw new IllegalArgumentException();
			}
		} else if (in.getY() > rect.getMinY() && in.getY() < rect.getMaxY()) {
			if (in.getX() < rect.getMinX()) {
				inter = new Point2D.Double(rect.getMinX(), in.getY());
			} else if (in.getX() > rect.getMaxX()) {
				inter = new Point2D.Double(rect.getMaxX(), in.getY());
			} else {
				throw new IllegalArgumentException();
			}
		} else {
			final Point2D p1 = new Point2D.Double(rect.getMinX(), rect.getMinY());
			final Point2D p2 = new Point2D.Double(rect.getMaxX(), rect.getMinY());
			final Point2D p3 = new Point2D.Double(rect.getMaxX(), rect.getMaxY());
			final Point2D p4 = new Point2D.Double(rect.getMinX(), rect.getMaxY());

			inter = LineRectIntersection.getCloser(tangeante.getP1(), p1, p2, p3, p4);

		}

		final CubicCurve2D.Double curv = new CubicCurve2D.Double(tangeante.getX1(), tangeante.getY1(),
				tangeante.getX1(), tangeante.getY1(), inter.getX(), inter.getY(), inter.getX(), inter.getY());
		return result.addAfter(curv);
	}

}
