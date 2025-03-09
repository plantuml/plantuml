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

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.utils.MathUtils;

public class RectangleArea {

	private final double minX;
	private final double minY;
	private final double maxX;
	private final double maxY;

	public RectangleArea(double minX, double minY, double maxX, double maxY) {
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
	}

	public static RectangleArea build(XPoint2D pt1, XPoint2D pt2) {
		return new RectangleArea(MathUtils.min(pt1.x, pt2.x), MathUtils.min(pt1.y, pt2.y), MathUtils.max(pt1.x, pt2.x),
				MathUtils.max(pt1.y, pt2.y));
	}

	public RectangleArea move(double deltaX, double deltaY) {
		return new RectangleArea(minX + deltaX, minY + deltaY, maxX + deltaX, maxY + deltaY);
	}

	public double getWidth() {
		return maxX - minX;
	}

	public double getHeight() {
		return maxY - minY;
	}

	public boolean contains(double x, double y) {
		return x >= minX && x < maxX && y >= minY && y < maxY;
	}

	public RectangleArea merge(RectangleArea other) {
		return new RectangleArea(Math.min(this.minX, other.minX), Math.min(this.minY, other.minY),
				Math.max(this.maxX, other.maxX), Math.max(this.maxY, other.maxY));
	}

	public RectangleArea merge(XPoint2D point) {
		final double x = point.getX();
		final double y = point.getY();
		return new RectangleArea(Math.min(this.minX, x), Math.min(this.minY, y), Math.max(this.maxX, x),
				Math.max(this.maxY, y));
	}

	public boolean contains(XPoint2D p) {
		return contains(p.getX(), p.getY());
	}

	@Override
	public String toString() {
		return "minX=" + minX + " maxX=" + maxX + " minY=" + minY + " maxY=" + maxY;
	}

	public final double getMinX() {
		return minX;
	}

	public final double getMinY() {
		return minY;
	}

	public final double getMaxX() {
		return maxX;
	}

	public final double getMaxY() {
		return maxY;
	}

	public PointDirected getIntersection(XCubicCurve2D bez) {
		if (contains(bez.x1, bez.y1) == contains(bez.x2, bez.y2))
			return null;

		final double dist = bez.getP1().distance(bez.getP2());
		if (dist < 2) {
			final double angle = BezierUtils.getStartingAngle(bez);
			return new PointDirected(bez.getP1(), angle);
		}
		final XCubicCurve2D left = XCubicCurve2D.none();
		final XCubicCurve2D right = XCubicCurve2D.none();
		bez.subdivide(left, right);
		final PointDirected int1 = getIntersection(left);
		if (int1 != null)
			return int1;

		final PointDirected int2 = getIntersection(right);
		if (int2 != null)
			return int2;

		throw new IllegalStateException();
	}

	public XPoint2D getPointCenter() {
		return new XPoint2D((minX + maxX) / 2, (minY + maxY) / 2);
	}

	public RectangleArea withMinX(double d) {
		return new RectangleArea(d, minY, maxX, maxY);
	}

	public RectangleArea withMaxX(double d) {
		return new RectangleArea(minX, minY, d, maxY);
	}

	public RectangleArea addMaxX(double d) {
		return new RectangleArea(minX, minY, maxX + d, maxY);
	}

	public RectangleArea addMaxY(double d) {
		return new RectangleArea(minX, minY, maxX, maxY + d);
	}

	public RectangleArea addMinX(double d) {
		return new RectangleArea(minX + d, minY, maxX, maxY);
	}

	public RectangleArea addMinY(double d) {
		return new RectangleArea(minX, minY + d, maxX, maxY);
	}

	public RectangleArea withMinY(double d) {
		return new RectangleArea(minX, d, maxX, maxY);
	}

	public RectangleArea withMaxY(double d) {
		return new RectangleArea(minX, minY, maxX, d);
	}

//	public XPoint2D getProjectionOnFrontier(XPoint2D pt) {
//		final double x = pt.getX();
//		final double y = pt.getY();
//		if (x > maxX && y >= minY && y <= maxY)
//			return new XPoint2D(maxX - 1, y);
//
//		if (x < minX && y >= minY && y <= maxY)
//			return new XPoint2D(minX + 1, y);
//
//		if (y > maxY && x >= minX && x <= maxX)
//			return new XPoint2D(x, maxY - 1);
//
//		if (y < minY && x >= minX && x <= maxX)
//			return new XPoint2D(x, minY + 1);
//
//		return new XPoint2D(x, y);
//	}

	public RectangleArea delta(double m1, double m2) {
		return new RectangleArea(minX, minY, maxX + m1, maxY + m2);
	}

	public XDimension2D getDimension() {
		return new XDimension2D(maxX - minX, maxY - minY);
	}

	public UTranslate getPosition() {
		return new UTranslate(getMinX(), getMinY());
	}

//	public boolean isPointJustUpper(XPoint2D pt) {
//		if (pt.getX() >= minX && pt.getX() <= maxX && pt.getY() <= minY) {
//			return true;
//		}
//		return false;
//	}

	public Side getClosestSide(XPoint2D pt) {
		final double distNorth = Math.abs(minY - pt.getY());
		final double distSouth = Math.abs(maxY - pt.getY());
		final double distWest = Math.abs(minX - pt.getX());
		final double distEast = Math.abs(maxX - pt.getX());
		if (isSmallerThan(distNorth, distWest, distEast, distSouth))
			return Side.NORTH;

		if (isSmallerThan(distSouth, distNorth, distWest, distEast))
			return Side.SOUTH;

		if (isSmallerThan(distEast, distNorth, distWest, distSouth))
			return Side.EAST;

		if (isSmallerThan(distWest, distNorth, distEast, distSouth))
			return Side.WEST;

		return null;
	}

	private boolean isSmallerThan(double value, double a, double b, double c) {
		return value <= a && value <= b && value <= c;
	}

}
