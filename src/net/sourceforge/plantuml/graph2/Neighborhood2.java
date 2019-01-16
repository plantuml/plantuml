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

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import net.sourceforge.plantuml.geom.Orientation;

public class Neighborhood2 {

	final private double angle1;
	final private double angle2;
	final private Point2D.Double center;

	public Neighborhood2(Point2D.Double center) {
		this(center, 0, 0);
	}

	public boolean is360() {
		return angle1 == angle2;
	}

	public Neighborhood2(Point2D.Double center, double angle1, double angle2) {
		this.center = center;
		this.angle1 = angle1;
		this.angle2 = angle2;
	}

	@Override
	public boolean equals(Object obj) {
		final Neighborhood2 other = (Neighborhood2) obj;
		return angle1 == other.angle1 && angle2 == other.angle2 && center.equals(other.center);
	}

	@Override
	public int hashCode() {
		return center.hashCode() * 17 + new Point2D.Double(angle1, angle2).hashCode();
	}

	@Override
	public String toString() {
		final int a1 = (int) (angle1 * 180 / Math.PI);
		final int a2 = (int) (angle2 * 180 / Math.PI);
		return center + " " + a1 + " " + a2;
	}

	public final Point2D.Double getCenter() {
		return center;
	}

	// private double getMiddle() {
	// if (is360()) {
	// return angle1 + 2 * Math.PI / 3;
	// }
	// double result = (angle1 + angle2) / 2;
	// if (angle2 < angle1) {
	// result += Math.PI;
	// }
	// return result;
	// }
	//
	public Point2D.Double getPointInNeighborhood(double dist, Point2D p1, Point2D p2) {
		if (p1 == null || p2 == null) {
			throw new IllegalArgumentException();
		}
		if (dist <= 0) {
			throw new IllegalArgumentException();
		}
		final double v1 = Singularity2.convertAngle(Singularity2.getAngle(new Line2D.Double(center, p1)) - angle1);
		final double v2 = Singularity2.convertAngle(Singularity2.getAngle(new Line2D.Double(center, p2)) - angle1);
		if (v1 < 0) {
			throw new IllegalStateException();
		}
		if (v2 < 0) {
			throw new IllegalStateException();
		}
		final double middle = (v1 + v2) / 2 + angle1;
		return new Point2D.Double(center.x + dist * Math.cos(middle), center.y + dist * Math.sin(middle));
	}

	public boolean isInAngleStrict(double angle) {
		if (angle < 0) {
			throw new IllegalArgumentException();
		}
		if (angle2 > angle1) {
			return angle > angle1 && angle < angle2;
		}
		return angle > angle1 || angle < angle2;
	}

	public boolean isInAngleLarge(double angle) {
		if (angle < 0) {
			throw new IllegalArgumentException();
		}
		if (angle2 > angle1) {
			return angle >= angle1 && angle <= angle2;
		}
		return angle >= angle1 || angle <= angle2;
	}

	public boolean isAngleLimit(double angle) {
		return angle == angle1 || angle == angle2;
	}

	public Orientation getOrientationFrom(double angle) {
		if (angle1 == angle2) {
			throw new IllegalStateException();
		}
		if (angle != angle1 && angle != angle2) {
			throw new IllegalArgumentException("this=" + this + " angle=" + (int) (angle * 180 / Math.PI));
		}
		assert angle == angle1 || angle == angle2;

		if (angle == angle1) {
			return Orientation.MATH;
		}
		return Orientation.CLOCK;
	}

	public boolean isConnectable(Neighborhood2 other) {
		assert isConnectableInternal(other) == other.isConnectableInternal(this);
		return isConnectableInternal(other);

	}

	private boolean isConnectableInternal(Neighborhood2 other) {
		if (getCenter().equals(other.getCenter())) {
			throw new IllegalArgumentException("Same center");
		}
		final Line2D.Double seg1 = new Line2D.Double(getCenter(), other.getCenter());

		final double angle1 = Singularity2.convertAngle(Singularity2.getAngle(seg1));
		final double angle2 = Singularity2.convertAngle(Singularity2.getOppositeAngle(seg1));
		assert angle2 == Singularity2.convertAngle(Singularity2.getAngle(new Line2D.Double(other.getCenter(),
				getCenter())));
		if (isInAngleStrict(angle1) && other.isInAngleStrict(angle2)) {
			return true;
		}
		if (isInAngleStrict(angle1) && other.isInAngleLarge(angle2)) {
			return true;
		}
		if (isInAngleLarge(angle1) && other.isInAngleStrict(angle2)) {
			return true;
		}
		if (isAngleLimit(angle1) && other.isAngleLimit(angle2)) {
			if (is360() || other.is360()) {
				return true;
			}
			final Orientation o1 = getOrientationFrom(angle1);
			final Orientation o2 = other.getOrientationFrom(angle2);
			return o1 != o2;
		}
		return false;
	}

}
