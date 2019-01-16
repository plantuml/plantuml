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
package net.sourceforge.plantuml.geom;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public abstract class AbstractLineSegment extends Line2D {

	@Override
	final public boolean equals(Object obj) {
		final AbstractLineSegment other = (AbstractLineSegment) obj;
		return this.getP1().equals(other.getP1()) && getP2().equals(other.getP2());
	}

	@Override
	final public int hashCode() {
		int result = 7;
		final int multiplier = 17;
		result = result * multiplier + getP1().hashCode();
		result = result * multiplier + getP2().hashCode();
		return result;
	}

	final public double getLength() {
		return Math.sqrt((getP2().getX() - getP1().getX()) * (getP2().getX() - getP1().getX())
				+ (getP2().getY() - getP1().getY()) * (getP2().getY() - getP1().getY()));
	}

	final protected Point2D.Double getPoint2D(double u) {
		final double x = getP1().getX() + u * (getP2().getX() - getP1().getX());
		final double y = getP1().getY() + u * (getP2().getY() - getP1().getY());
		return new Point2D.Double(x, y);
	}

	final public double getDistance(Point2D f) {
		return this.ptSegDist(f);
	}

	public Point2D getSegIntersection(AbstractLineSegment other) {
		final double u;
		if (other.isVertical()) {
			u = getIntersectionVertical(other.getP1().getX());
		} else if (other.isHorizontal()) {
			u = getIntersectionHorizontal(other.getP1().getY());
		} else {
			return getDichoIntersection(other);
		}
		if (java.lang.Double.isNaN(u) || u < 0 || u > 1) {
			return null;
		}
		final Point2D.Double result = getPoint2D(u);
		if (isBetween(result, other.getP1(), other.getP2())) {
			return result;
		}
		return null;
	}

	private Point2D getDichoIntersection(AbstractLineSegment other) {
		if (doesIntersect(other) == false) {
			return null;
		}
		if (other.getLength() < 0.01) {
			return other.getMiddle();
		}
		final LineSegmentDouble p1 = new LineSegmentDouble(other.getP1(), other.getMiddle());
		final LineSegmentDouble p2 = new LineSegmentDouble(other.getMiddle(), other.getP2());
		if (doesIntersect(p1)) {
			return getDichoIntersection(p1);
		}
		if (doesIntersect(p2)) {
			return getDichoIntersection(p2);
		}
		throw new IllegalStateException();
	}

	private Point2D.Double getMiddle() {
		return getPoint2D(0.5);
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

	public double getIntersectionVertical(double xOther) {
		final double coef = getP2().getX() - getP1().getX();
		if (coef == 0) {
			return java.lang.Double.NaN;
		}
		return (xOther - getP1().getX()) / coef;
	}

	public double getIntersectionHorizontal(double yOther) {
		final double coef = getP2().getY() - getP1().getY();
		if (coef == 0) {
			return java.lang.Double.NaN;
		}
		return (yOther - getP1().getY()) / coef;
	}

	// Line2D

	@Override
	final public void setLine(double x1, double y1, double x2, double y2) {
		throw new UnsupportedOperationException();
	}

	final public Rectangle2D getBounds2D() {
		final double x;
		final double w;
		if (getX1() < getX2()) {
			x = getX1();
			w = getX2() - getX1();
		} else {
			x = getX2();
			w = getX1() - getX2();
		}
		final double y;
		final double h;
		if (getY1() < getY2()) {
			y = getY1();
			h = getY2() - getY1();
		} else {
			y = getY2();
			h = getY1() - getY2();
		}
		return new Rectangle2D.Double(x, y, w, h);
	}

	final public boolean isHorizontal() {
		return getP1().getY() == getP2().getY();
	}

	final public boolean isVertical() {
		return getP1().getX() == getP2().getX();
	}

	final public double getDistance(AbstractLineSegment other) {
		final double result = getDistanceInternal(other);
		assert equals(result, other.getDistanceInternal(this));
		return result;
	}

	private boolean equals(double a1, double a2) {
		return Math.abs(a1 - a2) < 0.0001;
	}

	public boolean isPointOnSegment(Point2D pt) {
		return equals(pt.distance(getP1()) + pt.distance(getP2()), getLength());
	}

	private double getDistanceInternal(AbstractLineSegment other) {
		double result = this.getDistance(other.getP1());
		result += this.getDistance(other.getP2());
		result += other.getDistance(this.getP1());
		result += other.getDistance(this.getP2());
		return result;
	}

	final public double getAngle() {
		return Math.atan2(getP2().getY() - getP1().getY(), getP2().getX() - getP1().getX());
	}

	final public double getOppositeAngle() {
		return Math.atan2(getP1().getY() - getP2().getY(), getP1().getX() - getP2().getX());
	}

	final public Point2D.Double startTranslatedAsVector(double u) {
		final double pour = 1.0 * u / 100.0;
		final double x = getP1().getX() + pour * (getP2().getX() - getP1().getX());
		final double y = getP1().getY() + pour * (getP2().getY() - getP1().getY());
		return new Point2D.Double(x, y);
	}

	public boolean doesIntersect(AbstractLineSegment other) {
		final boolean result = doesIntersectInternal(other);
		assert result == other.doesIntersectInternal(this);
		return result;
	}

	private boolean doesIntersectInternal(AbstractLineSegment other) {
		final double d1 = direction(other.getP1(), other.getP2(), this.getP1());
		final double d2 = direction(other.getP1(), other.getP2(), this.getP2());
		final double d3 = direction(this.getP1(), this.getP2(), other.getP1());
		final double d4 = direction(this.getP1(), this.getP2(), other.getP2());

		if (d1 == 0 && isBetween(this.getP1(), other.getP1(), other.getP2())) {
			return true;
		}

		if (d2 == 0 && isBetween(this.getP2(), other.getP1(), other.getP2())) {
			return true;
		}

		if (d3 == 0 && isBetween(other.getP1(), this.getP1(), this.getP2())) {
			return true;
		}

		if (d4 == 0 && isBetween(other.getP2(), this.getP1(), this.getP2())) {
			return true;
		}

		final boolean result = signDiffers(d1, d2) && signDiffers(d3, d4);
		assert this.intersectsLine(other) == result;
		return result;
	}

	private static double direction(Point2D origin, Point2D point1, Point2D point2) {
		return determinant(point2.getX() - origin.getX(), point2.getY() - origin.getY(), point1.getX() - origin.getX(),
				point1.getY() - origin.getY());
	}

	private static boolean signDiffers(double a, double b) {
		if (a > 0 && b < 0) {
			return true;
		}
		if (a < 0 && b > 0) {
			return true;
		}
		return false;
	}

	public double determinant(AbstractLineSegment other) {
		return determinant(this.getP1().getX() - this.getP2().getX(), this.getP1().getY() - this.getP2().getY(), other
				.getP1().getX() - other.getP2().getX(), other.getP1().getY() - other.getP2().getY());
	}

	private static double determinant(double x1, double y1, double x2, double y2) {
		return x1 * y2 - x2 * y1;
	}

	public double side(Point2D point) {
		// assert Math.signum(direction(p1, p2, point)) ==
		// this.relativeCCW(point);
		return direction(getP1(), getP2(), point);
	}

}
