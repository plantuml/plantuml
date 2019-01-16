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

import java.awt.geom.Point2D;
import java.util.Locale;

public class LineSegmentInt extends AbstractLineSegment {

	private final Point2DInt p1;
	private final Point2DInt p2;

	@Override
	public String toString() {
		return String.format(Locale.US, "( %d,%d - %d,%d )", p1.getXint(), p1.getYint(), p2.getXint(), p2.getYint());
	}

	public LineSegmentInt(int x1, int y1, int x2, int y2) {
		this(new Point2DInt(x1, y1), new Point2DInt(x2, y2));
	}

	public LineSegmentInt(Point2DInt p1, Point2DInt p2) {
		this.p1 = p1;
		this.p2 = p2;
		if (p1.equals(p2)) {
			throw new IllegalArgumentException();
		}
		assert p1 != null && p2 != null;
		assert getLength() > 0;
		assert this.getDistance(this) == 0;
	}

	public boolean containsPoint(Point2D point) {
		return side(point) == 0 && isBetween(point, p1, p2);
	}

	public double side(Box box) {
		final Point2DInt corners[] = box.getCorners();
		final double s0 = side(corners[0]);
		final double s1 = side(corners[1]);
		final double s2 = side(corners[2]);
		final double s3 = side(corners[3]);
		if (s0 > 0 && s1 > 0 && s2 > 0 && s3 > 0) {
			return 1;
		}
		if (s0 < 0 && s1 < 0 && s2 < 0 && s3 < 0) {
			return -1;
		}
		return 0;
	}

	public boolean doesIntersectButNotSameExtremity(LineSegmentInt other) {
		// assert sameExtremities(other) == false;
		if (doesIntersect(other) == false) {
			return false;
		}
		if (atLeastOneCommonExtremities(other)) {
			return false;
		}
		return true;
	}

	public boolean sameExtremities(LineSegmentInt other) {
		if (p1.equals(other.p1) && p2.equals(other.p2)) {
			return true;
		}
		if (p1.equals(other.p2) && p2.equals(other.p1)) {
			return true;
		}
		return false;
	}

	public boolean atLeastOneCommonExtremities(LineSegmentInt other) {
		if (p1.equals(other.p1)) {
			return true;
		}
		if (p1.equals(other.p2)) {
			return true;
		}
		if (p2.equals(other.p1)) {
			return true;
		}
		if (p2.equals(other.p2)) {
			return true;
		}
		return false;
	}

	public Point2DInt getCommonExtremities(LineSegmentInt other) {
		if (p1.equals(other.p1)) {
			return p1;
		}
		if (p1.equals(other.p2)) {
			return p1;
		}
		if (p2.equals(other.p1)) {
			return p2;
		}
		if (p2.equals(other.p2)) {
			return p2;
		}
		return null;
	}

	public Point2DInt getOtherExtremity(Point2DInt extremity1) {
		if (extremity1 == null) {
			throw new IllegalArgumentException();
		}
		if (extremity1.equals(p1)) {
			return p2;
		}
		if (extremity1.equals(p2)) {
			return p1;
		}
		throw new IllegalArgumentException();
	}

	// Line2D

	@Override
	public Point2DInt getP1() {
		return p1;
	}

	@Override
	public Point2DInt getP2() {
		return p2;
	}

	@Override
	public double getX1() {
		return p1.getXint();
	}

	@Override
	public double getX2() {
		return p2.getXint();
	}

	@Override
	public double getY1() {
		return p1.getYint();
	}

	@Override
	public double getY2() {
		return p2.getYint();
	}

	public Point2DInt getTranslatedPoint(Point2DInt pointToBeTranslated) {
		final int x = p2.getXint() - p1.getXint();
		final int y = p2.getYint() - p1.getYint();
		return new Point2DInt(pointToBeTranslated.getXint() + x, pointToBeTranslated.getYint() + y);
	}

	public Point2DInt getCenter() {
		return new Point2DInt((p1.getXint() + p2.getXint()) / 2, (p1.getYint() + p2.getYint()) / 2);
	}

	public int getMinX() {
		return Math.min(p1.getXint(), p2.getXint());
	}

	public int getMaxX() {
		return Math.max(p1.getXint(), p2.getXint());
	}

	public int getMinY() {
		return Math.min(p1.getYint(), p2.getYint());
	}

	public int getMaxY() {
		return Math.max(p1.getYint(), p2.getYint());
	}

	public Point2DInt ortho(Point2D.Double orig, double d) {
		final double vectX = p2.getY() - p1.getY();
		final double vectY = -(p2.getX() - p1.getX());
		final double pour = 1.0 * d / 100.0;
		final double x = orig.x + vectX * pour;
		final double y = orig.y + vectY * pour;
		return new Point2DInt((int) Math.round(x), (int) Math.round(y));
	}

	public LineSegmentInt translate(int deltaX, int deltaY) {
		return new LineSegmentInt(p1.translate(deltaX, deltaY), p2.translate(deltaX, deltaY));
	}

	public LineSegmentInt inflateXAlpha(InflateData inflateData) {

		final int xpos = inflateData.getPos();
		final int inflation = inflateData.getInflation();
		if (isHorizontal()) {
			return new LineSegmentInt(p1.inflateX(inflateData), p2.inflateX(inflateData));
		}
		if (getP1().getXint() == xpos && getP2().getXint() == xpos) {
			return this.translate(inflation / 2, 0);
		}
		if (getP1().getXint() <= xpos && getP2().getXint() <= xpos) {
			return this;
		}
		if (getP1().getXint() >= xpos && getP2().getXint() >= xpos) {
			return this.translate(inflation, 0);
		}
		throw new UnsupportedOperationException(toString() + " " + inflateData);
	}

	public LineSegmentInt inflateYAlpha(InflateData inflateData) {
		final int ypos = inflateData.getPos();
		final int inflation = inflateData.getInflation();
		if (isVertical()) {
			return new LineSegmentInt(p1.inflateY(inflateData), p2.inflateY(inflateData));
		}
		if (getP1().getYint() == ypos && getP2().getYint() == ypos) {
			return this.translate(0, inflation / 2);
		}
		if (getP1().getYint() <= ypos && getP2().getYint() <= ypos) {
			return this;
		}
		if (getP1().getYint() >= ypos && getP2().getYint() >= ypos) {
			return this.translate(0, inflation);
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public Point2DInt getSegIntersection(AbstractLineSegment other) {
		final Point2D result = super.getSegIntersection(other);
		if (result == null) {
			return null;
		}
		return new Point2DInt((int) Math.round(result.getX()), (int) Math.round(result.getY()));
	}

}
