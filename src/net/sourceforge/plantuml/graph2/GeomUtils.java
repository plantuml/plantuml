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

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class GeomUtils {

	public static Point2D translate(Point2D pt, double deltaX, double deltaY) {
		return new Point2D.Double(pt.getX() + deltaX, pt.getY() + deltaY);
	}

	static public boolean isHorizontal(Line2D.Double seg) {
		return seg.getP1().getY() == seg.getP2().getY();
	}

	static public boolean isVertical(Line2D.Double seg) {
		return seg.getP1().getX() == seg.getP2().getX();
	}

	static public double getMinX(Line2D.Double seg) {
		return Math.min(seg.x1, seg.x2);
	}

	static public double getMaxX(Line2D.Double seg) {
		return Math.max(seg.x1, seg.x2);
	}

	static public double getMinY(Line2D.Double seg) {
		return Math.min(seg.y1, seg.y2);
	}

	static public double getMaxY(Line2D.Double seg) {
		return Math.max(seg.y1, seg.y2);
	}

	static public Point2D.Double getPoint2D(Line2D.Double line, double u) {
		final double x = line.x1 + u * (line.x2 - line.x1);
		final double y = line.y1 + u * (line.y2 - line.y1);
		return new Point2D.Double(x, y);
	}

	private static boolean isBetween(double value, double v1, double v2) {
		if (v1 < v2) {
			return value >= v1 && value <= v2;
		}
		assert v2 <= v1;
		return value >= v2 && value <= v1;

	}

	static boolean isBetween(Point2D toTest, Point2D pos1, Point2D pos2) {
		return isBetween(toTest.getX(), pos1.getX(), pos2.getX()) && isBetween(toTest.getY(), pos1.getY(), pos2.getY());
	}

	static private double getIntersectionVertical(Line2D.Double line, double xOther) {
		final double coef = line.x2 - line.x1;
		if (coef == 0) {
			return java.lang.Double.NaN;
		}
		return (xOther - line.x1) / coef;
	}

	static private double getIntersectionHorizontal(Line2D.Double line, double yOther) {
		final double coef = line.y2 - line.y1;
		if (coef == 0) {
			return java.lang.Double.NaN;
		}
		return (yOther - line.y1) / coef;
	}

	static public Point2D.Double getSegIntersection(Line2D.Double line1, Line2D.Double line2) {
		final double u;
		if (isVertical(line2)) {
			u = getIntersectionVertical(line1, line2.getP1().getX());
		} else if (isHorizontal(line2)) {
			u = getIntersectionHorizontal(line1, line2.getP1().getY());
		} else {
			throw new UnsupportedOperationException();
		}
		if (java.lang.Double.isNaN(u) || u < 0 || u > 1) {
			return null;
		}
		final Point2D.Double result = getPoint2D(line1, u);
		if (isBetween(result, line2.getP1(), line2.getP2())) {
			return result;
		}
		return null;
	}

	public static String toString(Line2D line) {
		// return line.getP1() + "-" + line.getP2();
		return toString(line.getP1()) + "-" + toString(line.getP2());
	}

	public static String toString(Point2D pt) {
		return "[" + pt.getX() + "," + pt.getY() + "]";
	}

	public static Point2D.Double getCenter(Line2D.Double l) {
		final double x = (l.getX1() + l.getX2()) / 2;
		final double y = (l.getY1() + l.getY2()) / 2;
		return new Point2D.Double(x, y);
	}

	public static void fillPoint2D(Graphics2D g2d, Point2D pt) {
		final int x = (int) pt.getX() - 1;
		final int y = (int) pt.getY() - 1;
		g2d.fillOval(x, y, 3, 3);
	}
	
	public static double getOrthoDistance(Line2D.Double seg, Point2D pt) {
		if (isHorizontal(seg)) {
			return Math.abs(seg.getP1().getY() - pt.getY());
		}
		if (isVertical(seg)) {
			return Math.abs(seg.getP1().getX() - pt.getX());
		}
		throw new IllegalArgumentException();
	}

}
