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

import net.sourceforge.plantuml.ugraphic.UTranslate;

public class TriangleCorner {

	private final Point2D o;
	private final Point2D a;
	private final Point2D b;
	private final TriangleCornerSimple simple;
	private final UTranslate translateO;
	private final UTranslate translateOreverse;

	private final RotationZoom rotation;
	private final RotationZoom rotationInverse;

	@Override
	public String toString() {
		return "Corner " + o + " a=" + a + " b=" + b;
	}

	public boolean hasCurvation() {
		return ((MyPoint2D) o).hasCurvation();
	}

	public double getCurvation() {
		if (hasCurvation() == false) {
			throw new IllegalStateException();
		}
		return ((MyPoint2D) o).getCurvation(0);
	}

	public TriangleCorner(Point2D o, Point2D a, Point2D b) {
		this.o = o;
		this.a = a;
		this.b = b;
		this.translateO = new UTranslate(o);
		this.translateOreverse = translateO.reverse();
		final Point2D a2 = translateOreverse.getTranslated(a);
		final Point2D b2 = translateOreverse.getTranslated((b));

		final Point2D a3, b3;
		if (a2.getX() == 0) {
			a3 = a2;
			b3 = b2;
			this.rotation = RotationZoom.none();
			this.rotationInverse = RotationZoom.none();
		} else {
			this.rotation = RotationZoom.builtRotationOnYaxis(a2);
			this.rotationInverse = rotation.inverse();
			a3 = rotation.getPoint(a2);
			b3 = rotation.getPoint(b2);
		}

		this.simple = new TriangleCornerSimple(a3, b3);
	}

	public Point2D getOnSegmentA(double dist) {
		final Segment seg = new Segment(o, a);
		return seg.getFromAtoB(dist);
	}

	public Point2D getOnSegmentB(double dist) {
		final Segment seg = new Segment(o, b);
		return seg.getFromAtoB(dist);
	}

	public Balloon getCenterWithFixedRadius(double radius) {
		final Point2D centerSimple = simple.getCenterWithFixedRadius(radius);
		return new Balloon(rotationInverse.getPoint(translateO.getTranslated(centerSimple)), radius);
	}

	private Balloon getBalloonWithFixedY(double y) {
		Balloon result = simple.getBalloonWithFixedY(y);
		result = result.rotate(rotationInverse);
		result = result.translate(translateO);
		return result;
	}

	public Point2D getCornerOrBalloonCenter() {
		if (hasCurvation()) {
			return getBalloonInside().getCenter();
		}
		return getO();
	}

	public double determinant() {
		final double ux = a.getX() - o.getX();
		final double uy = a.getY() - o.getY();
		final double vx = b.getX() - o.getX();
		final double vy = b.getY() - o.getY();
		return ux * vy - uy * vx;
	}

	public Point2D getO() {
		return o;
	}

	public Balloon getBalloonInside() {
		if (hasCurvation() == false) {
			throw new IllegalStateException();
		}
		return getBalloonWithFixedY(getCurvation());
	}

}
