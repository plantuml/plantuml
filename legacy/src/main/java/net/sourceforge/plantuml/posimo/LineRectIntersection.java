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
package net.sourceforge.plantuml.posimo;

import net.sourceforge.plantuml.klimt.geom.XLine2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.geom.XRectangle2D;

public class LineRectIntersection {

	private final XPoint2D inter;

	public LineRectIntersection(XLine2D line, XRectangle2D rect) {
		final XPoint2D p1 = new XPoint2D(rect.getMinX(), rect.getMinY());
		final XPoint2D p2 = new XPoint2D(rect.getMaxX(), rect.getMinY());
		final XPoint2D p3 = new XPoint2D(rect.getMaxX(), rect.getMaxY());
		final XPoint2D p4 = new XPoint2D(rect.getMinX(), rect.getMaxY());

		final XPoint2D inter1 = new LineSegmentIntersection(XLine2D.line(p1, p2), line).getIntersection();
		final XPoint2D inter2 = new LineSegmentIntersection(XLine2D.line(p2, p3), line).getIntersection();
		final XPoint2D inter3 = new LineSegmentIntersection(XLine2D.line(p3, p4), line).getIntersection();
		final XPoint2D inter4 = new LineSegmentIntersection(XLine2D.line(p4, p1), line).getIntersection();

		final XPoint2D o = line.getP1();
		inter = getCloser(o, inter1, inter2, inter3, inter4);

	}

	public static XPoint2D getCloser(final XPoint2D o, final XPoint2D... other) {
		double minDist = Double.MAX_VALUE;
		XPoint2D result = null;

		for (XPoint2D pt : other)
			if (pt != null) {
				final double dist = pt.distanceSq(o);
				if (dist < minDist) {
					minDist = dist;
					result = pt;
				}
			}

		return result;
	}

	public final XPoint2D getIntersection() {
		return inter;
	}

}
