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
package net.sourceforge.plantuml.klimt.geom;

public class BezierUtils {
	static public double getEndingAngle(final XCubicCurve2D left) {
		if (left.getCtrlP2().equals(left.getP2()))
			return getAngle(left.getP1(), left.getP2());

		return getAngle(left.getCtrlP2(), left.getP2());
	}

	static public double getStartingAngle(final XCubicCurve2D left) {
		if (left.getP1().equals(left.getCtrlP1()))
			return getAngle(left.getP1(), left.getP2());

		return getAngle(left.getP1(), left.getCtrlP1());
	}

	public static double getAngle(XPoint2D p1, XPoint2D p2) {
		if (p1.equals(p2))
			throw new IllegalArgumentException();

		return Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX());
	}

	private static boolean isCutting(XCubicCurve2D bez, XRectangle2D shape) {
		final boolean contains1 = shape.contains(bez.x1, bez.y1);
		final boolean contains2 = shape.contains(bez.x2, bez.y2);
		return contains1 ^ contains2;
	}

	private static void shorten(XCubicCurve2D bez, XRectangle2D shape) {
		final boolean contains1 = shape.contains(bez.x1, bez.y1);
		final boolean contains2 = shape.contains(bez.x2, bez.y2);
		if (contains1 ^ contains2 == false)
			throw new IllegalArgumentException();

		if (contains1 == false)
			bez.setCurve(bez.x2, bez.y2, bez.ctrlx2, bez.ctrly2, bez.ctrlx1, bez.ctrly1, bez.x1, bez.y1);

		assert shape.contains(bez.x1, bez.y1) && shape.contains(bez.x2, bez.y2) == false;
		final XCubicCurve2D left = XCubicCurve2D.none();
		final XCubicCurve2D right = XCubicCurve2D.none();
		subdivide(bez, left, right, 0.5);

		if (isCutting(left, shape) ^ isCutting(right, shape) == false)
			throw new IllegalArgumentException();

		if (isCutting(left, shape))
			bez.setCurve(left);
		else
			bez.setCurve(right);

	}

	private static void subdivide(XCubicCurve2D src, XCubicCurve2D left, XCubicCurve2D right, final double coef) {
		final double coef1 = coef;
		final double coef2 = 1 - coef;
		final double centerxA = src.getCtrlX1() * coef1 + src.getCtrlX2() * coef2;
		final double centeryA = src.getCtrlY1() * coef1 + src.getCtrlY2() * coef2;

		final double x1 = src.getX1();
		final double y1 = src.getY1();
		final double x2 = src.getX2();
		final double y2 = src.getY2();
		final double ctrlx1 = x1 * coef1 + src.getCtrlX1() * coef1;
		final double ctrly1 = y1 * coef1 + src.getCtrlY1() * coef1;
		final double ctrlx2 = x2 * coef1 + src.getCtrlX2() * coef1;
		final double ctrly2 = y2 * coef1 + src.getCtrlY2() * coef1;

		final double ctrlx12 = ctrlx1 * coef1 + centerxA * coef1;
		final double ctrly12 = ctrly1 * coef1 + centeryA * coef1;
		final double ctrlx21 = ctrlx2 * coef1 + centerxA * coef1;
		final double ctrly21 = ctrly2 * coef1 + centeryA * coef1;
		final double centerxB = ctrlx12 * coef1 + ctrlx21 * coef1;
		final double centeryB = ctrly12 * coef1 + ctrly21 * coef1;
		left.setCurve(x1, y1, ctrlx1, ctrly1, ctrlx12, ctrly12, centerxB, centeryB);
		right.setCurve(centerxB, centeryB, ctrlx21, ctrly21, ctrlx2, ctrly2, x2, y2);
	}

	static double dist(XCubicCurve2D seg) {
		return XPoint2D.distance(seg.x1, seg.y1, seg.x2, seg.y2);
	}

	static double dist(XLine2D seg) {
		return XPoint2D.distance(seg.x1, seg.y1, seg.x2, seg.y2);
	}

	static public XPoint2D middle(XLine2D seg) {
		return new XPoint2D((seg.x1 + seg.x2) / 2, (seg.y1 + seg.y2) / 2);
	}

	static public XPoint2D middle(XPoint2D p1, XPoint2D p2) {
		return new XPoint2D((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2);
	}

	public static XPoint2D intersect(XLine2D orig, XRectangle2D shape) {
		XLine2D copy = new XLine2D(orig.x1, orig.y1, orig.x2, orig.y2);
		final boolean contains1 = shape.contains(copy.x1, copy.y1);
		final boolean contains2 = shape.contains(copy.x2, copy.y2);
		if (contains1 ^ contains2 == false) {
			// return new XPoint2D(orig.x2, orig.y2);
			throw new IllegalArgumentException();
		}
		while (true) {
			final XPoint2D m = copy.getMiddle();
			final boolean containsMiddle = shape.contains(m.x, m.y);
			if (contains1 == containsMiddle)
				copy = copy.withPoint1(m);
			else
				copy = copy.withPoint2(m);

			if (dist(copy) < 0.1) {
				if (contains1)
					return copy.getP2();

				if (contains2)
					return copy.getP1();

				throw new IllegalStateException();
			}
		}
	}

	static private XRectangle2D toRectangle(Positionable p) {
		final XPoint2D point = p.getPosition();
		final XDimension2D dim = p.getSize();
		return new XRectangle2D(point.getX(), point.getY(), dim.getWidth(), dim.getHeight());
	}

	static public boolean intersect(Positionable p1, Positionable p2) {
		return toRectangle(p1).intersects(toRectangle(p2));
	}

	static public XPoint2D getCenter(Positionable p) {
		return new XPoint2D(toRectangle(p).getCenterX(), toRectangle(p).getCenterY());
	}

}
