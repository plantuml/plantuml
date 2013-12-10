/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 *
 * Revision $Revision: 4236 $
 * 
 */
package net.sourceforge.plantuml.svek;

import java.awt.geom.CubicCurve2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.posimo.BezierUtils;

public class ClusterPosition {

	private final double minX;
	private final double minY;
	private final double maxX;
	private final double maxY;

	public ClusterPosition(double minX, double minY, double maxX, double maxY) {
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
	}

	public boolean contains(double x, double y) {
		return x >= minX && x < maxX && y >= minY && y < maxY;
	}

	public ClusterPosition merge(ClusterPosition other) {
		return new ClusterPosition(Math.min(this.minX, other.minX), Math.min(this.minY, other.minY), Math.max(
				this.maxX, other.maxX), Math.max(this.maxY, other.maxY));
	}

	public ClusterPosition merge(Point2D point) {
		final double x = point.getX();
		final double y = point.getY();
		return new ClusterPosition(Math.min(this.minX, x), Math.min(this.minY, y), Math.max(this.maxX, x), Math.max(
				this.maxY, y));
	}

	public boolean contains(Point2D p) {
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

	public PointDirected getIntersection(CubicCurve2D.Double bez) {
		if (contains(bez.x1, bez.y1) == contains(bez.x2, bez.y2)) {
			return null;
		}
		final double dist = bez.getP1().distance(bez.getP2());
		if (dist < 2) {
			final double angle = BezierUtils.getStartingAngle(bez);
			return new PointDirected(bez.getP1(), angle);
		}
		final CubicCurve2D.Double left = new CubicCurve2D.Double();
		final CubicCurve2D.Double right = new CubicCurve2D.Double();
		bez.subdivide(left, right);
		final PointDirected int1 = getIntersection(left);
		if (int1 != null) {
			return int1;
		}
		final PointDirected int2 = getIntersection(right);
		if (int2 != null) {
			return int2;
		}
		throw new IllegalStateException();
	}

	public Point2D getPointCenter() {
		return new Point2D.Double((minX + maxX) / 2, (minY + maxY) / 2);
	}

	public ClusterPosition withMinX(double d) {
		return new ClusterPosition(d, minY, maxX, maxY);
	}

	public ClusterPosition withMaxX(double d) {
		return new ClusterPosition(minX, minY, d, maxY);
	}

	public ClusterPosition addMaxX(double d) {
		return new ClusterPosition(minX, minY, maxX + d, maxY);
	}

	public ClusterPosition addMaxY(double d) {
		return new ClusterPosition(minX, minY, maxX, maxY + d);
	}

	public ClusterPosition addMinX(double d) {
		return new ClusterPosition(minX + d, minY, maxX, maxY);
	}

	public ClusterPosition addMinY(double d) {
		return new ClusterPosition(minX, minY + d, maxX, maxY);
	}

	public ClusterPosition withMinY(double d) {
		return new ClusterPosition(minX, d, maxX, maxY);
	}

	public ClusterPosition withMaxY(double d) {
		return new ClusterPosition(minX, minY, maxX, d);
	}

	public Point2D getProjectionOnFrontier(Point2D pt) {
		final double x = pt.getX();
		final double y = pt.getY();
		if (x > maxX && y >= minY && y <= maxY) {
			return new Point2D.Double(maxX - 1, y);
		}
		if (x < minX && y >= minY && y <= maxY) {
			return new Point2D.Double(minX + 1, y);
		}
		if (y > maxY && x >= minX && x <= maxX) {
			return new Point2D.Double(x, maxY - 1);
		}
		if (y < minY && x >= minX && x <= maxX) {
			return new Point2D.Double(x, minY + 1);
		}
		return new Point2D.Double(x, y);
	}

	public ClusterPosition delta(double m1, double m2) {
		return new ClusterPosition(minX, minY, maxX + m1, maxY + m2);
	}

	public Dimension2D getDimension() {
		return new Dimension2DDouble(maxX - minX, maxY - minY);
	}

}
