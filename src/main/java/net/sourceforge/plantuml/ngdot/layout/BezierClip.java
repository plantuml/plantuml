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
package net.sourceforge.plantuml.ngdot.layout;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.klimt.geom.XPoint2D;

/**
 * Shortens a piecewise cubic Bezier curve at one end, so that an arrow of a
 * given length fits between the new end and the original one. This is what
 * Graphviz does in {@code arrowEndClip} / {@code arrowStartClip}: the curve is
 * cut where it leaves a circle centered on its end.
 * <p>
 * It is done here rather than by Smetana because Smetana only knows the
 * {@code normal} arrow, and takes a single {@code arrowsize} for both ends.
 */
final class BezierClip {

	private BezierClip() {
	}

	/**
	 * Cuts the end of the curve.
	 *
	 * @param points {@code 1 + 3n} points
	 * @param length the length to remove, measured as a straight distance from
	 *               the original end
	 * @return the new points; the original end is the arrow tip
	 */
	static List<XPoint2D> clipEnd(List<XPoint2D> points, double length) {
		final int n = points.size();
		if (n < 4 || length <= 0)
			return points;

		final XPoint2D end = points.get(n - 1);
		// Find the last segment that starts outside the circle
		int start = n - 4;
		while (start > 0 && points.get(start).distance(end) < length)
			start -= 3;

		final XPoint2D p0 = points.get(start);
		final XPoint2D p1 = points.get(start + 1);
		final XPoint2D p2 = points.get(start + 2);
		final XPoint2D p3 = points.get(start + 3);

		double t = 0;
		if (p0.distance(end) > length) {
			double lo = 0;
			double hi = 1;
			for (int i = 0; i < 50; i++) {
				final double mid = (lo + hi) / 2;
				if (eval(p0, p1, p2, p3, mid).distance(end) > length)
					lo = mid;
				else
					hi = mid;
			}
			t = lo;
		}

		final List<XPoint2D> result = new ArrayList<XPoint2D>(points.subList(0, start));
		final XPoint2D[] left = splitLeft(p0, p1, p2, p3, t);
		result.add(left[0]);
		result.add(left[1]);
		result.add(left[2]);
		result.add(left[3]);
		return result;
	}

	/**
	 * Cuts the start of the curve.
	 *
	 * @see #clipEnd(List, double)
	 */
	static List<XPoint2D> clipStart(List<XPoint2D> points, double length) {
		return reverse(clipEnd(reverse(points), length));
	}

	/**
	 * Cuts the end of the curve where it enters the contour of a node.
	 *
	 * @param center the center of the node
	 */
	static List<XPoint2D> clipEndToContour(List<XPoint2D> points, NodeContour contour, XPoint2D center) {
		final int n = points.size();
		if (n < 4)
			return points;
		// The last segment that starts outside the node
		int start = n - 4;
		while (start >= 0 && inside(contour, center, points.get(start)))
			start -= 3;
		if (start < 0)
			return points;

		final XPoint2D p0 = points.get(start);
		final XPoint2D p1 = points.get(start + 1);
		final XPoint2D p2 = points.get(start + 2);
		final XPoint2D p3 = points.get(start + 3);
		if (inside(contour, center, p3) == false)
			return points;

		double lo = 0;
		double hi = 1;
		for (int i = 0; i < 50; i++) {
			final double mid = (lo + hi) / 2;
			if (inside(contour, center, eval(p0, p1, p2, p3, mid)))
				hi = mid;
			else
				lo = mid;
		}
		final List<XPoint2D> result = new ArrayList<XPoint2D>(points.subList(0, start));
		final XPoint2D[] left = splitLeft(p0, p1, p2, p3, hi);
		result.add(left[0]);
		result.add(left[1]);
		result.add(left[2]);
		result.add(left[3]);
		return result;
	}

	/**
	 * Cuts the start of the curve where it leaves the contour of a node.
	 */
	static List<XPoint2D> clipStartToContour(List<XPoint2D> points, NodeContour contour, XPoint2D center) {
		return reverse(clipEndToContour(reverse(points), contour, center));
	}

	private static boolean inside(NodeContour contour, XPoint2D center, XPoint2D pt) {
		return contour.contains(pt.getX() - center.getX(), pt.getY() - center.getY());
	}

	private static List<XPoint2D> reverse(List<XPoint2D> points) {
		final List<XPoint2D> result = new ArrayList<XPoint2D>(points.size());
		for (int i = points.size() - 1; i >= 0; i--)
			result.add(points.get(i));
		return result;
	}

	static XPoint2D eval(XPoint2D p0, XPoint2D p1, XPoint2D p2, XPoint2D p3, double t) {
		final double u = 1 - t;
		final double a = u * u * u;
		final double b = 3 * u * u * t;
		final double c = 3 * u * t * t;
		final double d = t * t * t;
		return new XPoint2D(a * p0.getX() + b * p1.getX() + c * p2.getX() + d * p3.getX(),
				a * p0.getY() + b * p1.getY() + c * p2.getY() + d * p3.getY());
	}

	private static XPoint2D lerp(XPoint2D a, XPoint2D b, double t) {
		return new XPoint2D(a.getX() + (b.getX() - a.getX()) * t, a.getY() + (b.getY() - a.getY()) * t);
	}

	/**
	 * The part [0, t] of a cubic Bezier curve (de Casteljau).
	 */
	private static XPoint2D[] splitLeft(XPoint2D p0, XPoint2D p1, XPoint2D p2, XPoint2D p3, double t) {
		final XPoint2D p01 = lerp(p0, p1, t);
		final XPoint2D p12 = lerp(p1, p2, t);
		final XPoint2D p23 = lerp(p2, p3, t);
		final XPoint2D p012 = lerp(p01, p12, t);
		final XPoint2D p123 = lerp(p12, p23, t);
		final XPoint2D p0123 = lerp(p012, p123, t);
		return new XPoint2D[] { p0, p01, p012, p0123 };
	}

}
