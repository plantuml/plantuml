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
package net.sourceforge.plantuml.cute;

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class Balloon implements UDrawable {

	private final Point2D center;
	private final double radius;

	public Balloon(Point2D center, double radius) {
		if (radius < 0) {
			throw new IllegalArgumentException();
		}
		this.center = center;
		this.radius = radius;
	}

	public static Balloon fromRadiusSegment(Segment centerToContact) {
		throw new UnsupportedOperationException();
	}

	public Point2D getPointOnCircle(double a) {
		return new Point2D.Double(center.getX() + radius * Math.cos(a), center.getY() + radius * Math.sin(a));
	}

	public Segment getSegmentCenterToPointOnCircle(double a) {
		return new Segment(center, getPointOnCircle(a));
	}

	public Balloon translate(UTranslate translate) {
		return new Balloon(translate.getTranslated(center), radius);
	}

	public Balloon rotate(RotationZoom rotationZoom) {
		return new Balloon(rotationZoom.getPoint(center), rotationZoom.applyZoom(radius));
	}

	@Override
	public String toString() {
		return "Balloon(" + center + "," + radius + ")";
	}

	public Point2D getCenter() {
		return center;
	}

	public double getRadius() {
		return radius;
	}

	public void drawU(UGraphic ug) {
		UEllipse circle = new UEllipse(2 * radius, 2 * radius);
		ug.apply(new UTranslate(center.getX() - circle.getWidth() / 2, center.getY() - circle.getHeight() / 2)).draw(
				circle);
	}

	public Balloon getInsideTangentBalloon1(double angle, double curvation) {
		final double f = radius - curvation;
		final double e = (radius * radius - f * f) / 2 / radius;
		final RotationZoom rotation = RotationZoom.rotationInRadians(angle);
		final Point2D p1 = rotation.getPoint(f, e);
		final Point2D newCenter = new Point2D.Double(center.getX() + p1.getX(), center.getY() + p1.getY());
		return new Balloon(newCenter, e);
	}

	public Balloon getInsideTangentBalloon2(double angle, double curvation) {
		final double f = radius - curvation;
		final double e = (radius * radius - f * f) / 2 / radius;
		final RotationZoom rotation = RotationZoom.rotationInRadians(angle);
		final Point2D p1 = rotation.getPoint(f, -e);
		final Point2D newCenter = new Point2D.Double(center.getX() + p1.getX(), center.getY() + p1.getY());
		return new Balloon(newCenter, e);
	}

	public Point2D getPointOnCirclePassingByThisPoint(Point2D passingBy) {
		final Segment seg = new Segment(center, passingBy);
		return seg.getFromAtoB(radius);
	}

	public Point2D getPointOnCircleOppositeToThisPoint(Point2D passingBy) {
		final Segment seg = new Segment(center, passingBy);
		return seg.getFromAtoB(-radius);
	}

}
