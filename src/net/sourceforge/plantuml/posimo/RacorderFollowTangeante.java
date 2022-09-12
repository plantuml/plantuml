/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
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
import java.awt.geom.Rectangle2D;

import net.sourceforge.plantuml.awt.geom.XLine2D;
import net.sourceforge.plantuml.awt.geom.XPoint2D;

public class RacorderFollowTangeante extends RacorderAbstract implements Racorder {

	@Override
	public DotPath getRacordIn(Rectangle2D rect, XLine2D tangeante) {

// Log.println("rect x=" + rect.getX() + " y=" + rect.getY() + " w=" + rect.getWidth() + " h="
//				+ rect.getHeight());
// Log.println("tangeante (" + tangeante.getX1() + "," + tangeante.getY1() + ") (" + tangeante.getX2()
//				+ "," + tangeante.getY2() + ")");

		final DotPath result = new DotPath();

		// final XPoint2D inter = BezierUtils.intersect((Line2D.Double)
		// tangeante, rect);
		XPoint2D inter = new LineRectIntersection(tangeante, rect).getIntersection();
// Log.println("inter=" + inter);

		if (inter == null) {
			final XPoint2D p1 = new XPoint2D(rect.getMinX(), rect.getMinY());
			final XPoint2D p2 = new XPoint2D(rect.getMaxX(), rect.getMinY());
			final XPoint2D p3 = new XPoint2D(rect.getMaxX(), rect.getMaxY());
			final XPoint2D p4 = new XPoint2D(rect.getMinX(), rect.getMaxY());

			inter = LineRectIntersection.getCloser(tangeante.getP1(), p1, p2, p3, p4);
		}

		final CubicCurve2D.Double curv = new CubicCurve2D.Double(tangeante.getX1(), tangeante.getY1(),
				tangeante.getX1(), tangeante.getY1(), inter.getX(), inter.getY(), inter.getX(), inter.getY());
		return result.addAfter(curv);
	}

}
