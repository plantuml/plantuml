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

public class Arc {

	private final Segment segment;
	private final Tension tension;

	public Tension getTension() {
		return tension;
	}

	public Arc(final MyPoint2D a, final MyPoint2D b) {
		this(a, b, Tension.none());
	}

	private Arc(final MyPoint2D a, final MyPoint2D b, Tension tension) {
		this.segment = new Segment(a, b);
		this.tension = tension;
	}

	public MyPoint2D getA() {
		return (MyPoint2D) segment.getA();
	}

	public MyPoint2D getB() {
		return (MyPoint2D) segment.getB();
	}

	public Arc withNoTension() {
		return new Arc(getA(), getB(), Tension.none());
	}

	public Arc withTension(String tensionString) {
		if (tensionString == null) {
			return this;
		}
		final double newTension = Double.parseDouble(tensionString);
		return new Arc(getA(), getB(), new Tension(newTension));
	}

	public Arc rotateZoom(RotationZoom rotationZoom) {
		return new Arc(getA().rotateZoom(rotationZoom), getB().rotateZoom(rotationZoom),
				tension.rotateZoom(rotationZoom));
	}

//	public void appendTo(UPath path) {
//		if (tension.isNone()) {
//			path.lineTo(getB());
//		} else {
//			final double a = segment.getLength() / 2;
//			final double b = getTension().getValue();
//			final double radius = (a * a + b * b) / 2 / b;
//			final int sweep_flag = 1;
//			path.arcTo(getB(), radius, 0, sweep_flag);
//		}
//	}

	public Point2D getTensionPoint() {
		if (tension.isNone()) {
			throw new IllegalArgumentException();
		}
		return segment.getOrthoPoint(-tension.getValue());
	}

	// public void appendTo(UPath path) {
	// if (path.isEmpty()) {
	// path.moveTo(getA());
	// }
	// path.lineTo(getB());
	// }

	public double getLength() {
		return segment.getLength();
	}

}
