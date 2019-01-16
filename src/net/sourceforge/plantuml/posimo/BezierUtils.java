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

import java.awt.Shape;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class BezierUtils {

	static public double getEndingAngle(final CubicCurve2D.Double left) {
		if (left.getCtrlP2().equals(left.getP2())) {
			return getAngle(left.getP1(), left.getP2());
		}
		return getAngle(left.getCtrlP2(), left.getP2());
	}

	static public double getStartingAngle(final CubicCurve2D.Double left) {
		if (left.getP1().equals(left.getCtrlP1())) {
			return getAngle(left.getP1(), left.getP2());
		}
		return getAngle(left.getP1(), left.getCtrlP1());
	}

	static double getAngle(Point2D p1, Point2D p2) {
		if (p1.equals(p2)) {
			throw new IllegalArgumentException();
		}
		return Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX());
	}

	static boolean isCutting(CubicCurve2D.Double bez, Shape shape) {
		final boolean contains1 = shape.contains(bez.x1, bez.y1);
		final boolean contains2 = shape.contains(bez.x2, bez.y2);
		return contains1 ^ contains2;
	}

	static void shorten(CubicCurve2D.Double bez, Shape shape) {
		final boolean contains1 = shape.contains(bez.x1, bez.y1);
		final boolean contains2 = shape.contains(bez.x2, bez.y2);
		if (contains1 ^ contains2 == false) {
			throw new IllegalArgumentException();
		}
		if (contains1 == false) {
			bez.setCurve(bez.x2, bez.y2, bez.ctrlx2, bez.ctrly2, bez.ctrlx1, bez.ctrly1, bez.x1, bez.y1);
		}
		assert shape.contains(bez.x1, bez.y1) && shape.contains(bez.x2, bez.y2) == false;
		final CubicCurve2D.Double left = new CubicCurve2D.Double();
		final CubicCurve2D.Double right = new CubicCurve2D.Double();
		subdivide(bez, left, right, 0.5);

		if (isCutting(left, shape) ^ isCutting(right, shape) == false) {
			throw new IllegalArgumentException();
		}

		if (isCutting(left, shape)) {
			bez.setCurve(left);
		} else {
			bez.setCurve(right);
		}

	}

	private static void subdivide(CubicCurve2D src, CubicCurve2D left, CubicCurve2D right, final double coef) {
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

	static double dist(CubicCurve2D.Double seg) {
		return Point2D.distance(seg.x1, seg.y1, seg.x2, seg.y2);
	}

	static double dist(Line2D.Double seg) {
		return Point2D.distance(seg.x1, seg.y1, seg.x2, seg.y2);
	}

	static public Point2D middle(Line2D.Double seg) {
		return new Point2D.Double((seg.x1 + seg.x2) / 2, (seg.y1 + seg.y2) / 2);
	}

	static public Point2D middle(Point2D p1, Point2D p2) {
		return new Point2D.Double((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2);
	}

	public static Point2D intersect(Line2D.Double orig, Shape shape) {
		final Line2D.Double copy = new Line2D.Double(orig.x1, orig.y1, orig.x2, orig.y2);
		final boolean contains1 = shape.contains(copy.x1, copy.y1);
		final boolean contains2 = shape.contains(copy.x2, copy.y2);
		if (contains1 ^ contains2 == false) {
			// return new Point2D.Double(orig.x2, orig.y2);
			throw new IllegalArgumentException();
		}
		while (true) {
			final double mx = (copy.x1 + copy.x2) / 2;
			final double my = (copy.y1 + copy.y2) / 2;
			final boolean containsMiddle = shape.contains(mx, my);
			if (contains1 == containsMiddle) {
				copy.x1 = mx;
				copy.y1 = my;
			} else {
				copy.x2 = mx;
				copy.y2 = my;
			}
			if (dist(copy) < 0.1) {
				if (contains1) {
					return new Point2D.Double(copy.x2, copy.y2);
				}
				if (contains2) {
					return new Point2D.Double(copy.x1, copy.y1);
				}
				throw new IllegalStateException();
			}
		}
	}

	static public Rectangle2D toRectangle(Positionable p) {
		final Point2D point = p.getPosition();
		final Dimension2D dim = p.getSize();
		return new Rectangle2D.Double(point.getX(), point.getY(), dim.getWidth(), dim.getHeight());
	}

	static public boolean intersect(Positionable p1, Positionable p2) {
		return toRectangle(p1).intersects(toRectangle(p2));
	}

	static public Point2D.Double getCenter(Positionable p) {
		return new Point2D.Double(toRectangle(p).getCenterX(), toRectangle(p).getCenterY());
	}

}
