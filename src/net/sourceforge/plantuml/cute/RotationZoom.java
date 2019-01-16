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

public class RotationZoom {

	private final double angle;
	private final double zoom;

	private RotationZoom(double angle, double zoom) {
		if (zoom < 0) {
			throw new IllegalArgumentException();
		}
		this.angle = angle;
		this.zoom = zoom;
	}

	public RotationZoom compose(RotationZoom other) {
		return new RotationZoom(this.angle + other.angle, this.zoom * other.zoom);
	}

	@Override
	public String toString() {
		return "Rotation=" + Math.toDegrees(angle) + " Zoom=" + zoom;
	}

	public static RotationZoom fromVarArgs(VarArgs varArgs) {
		final double radians = Math.toRadians(varArgs.getAsDouble("rotation", 0));
		final double scale = varArgs.getAsDouble("scale", 1);
		return new RotationZoom(radians, scale);
	}

	public static RotationZoom rotationInDegrees(double angle) {
		return new RotationZoom(Math.toRadians(angle), 1);
	}

	public static RotationZoom rotationInRadians(double angle) {
		return new RotationZoom(angle, 1);
	}

	public static RotationZoom zoom(double zoom) {
		return new RotationZoom(0, zoom);
	}

	public RotationZoom inverse() {
		return new RotationZoom(-angle, 1 / zoom);
	}

	public double getAngleDegree() {
		return Math.toDegrees(angle);
	}

	static public RotationZoom builtRotationOnYaxis(Point2D toRotate) {
		final double a = Math.atan2(toRotate.getX(), toRotate.getY());
		return new RotationZoom(a, 1);
	}

	public Point2D.Double getPoint(double x, double y) {
		if (angle == 0) {
			return new Point2D.Double(x * zoom, y * zoom);
		}
		final double x1 = Math.cos(angle) * x - Math.sin(angle) * y;
		final double y1 = Math.sin(angle) * x + Math.cos(angle) * y;
		return new Point2D.Double(x1 * zoom, y1 * zoom);
	}

	public Point2D getPoint(Point2D p) {
		return getPoint(p.getX(), p.getY());
	}

	public UTranslate getUTranslate(UTranslate translate) {
		return new UTranslate(getPoint(translate.getDx(), translate.getDy()));

	}

	public static RotationZoom none() {
		return new RotationZoom(0, 1);
	}

	public boolean isNone() {
		return angle == 0 && zoom == 1;
	}

	public double applyZoom(double value) {
		return value * zoom;
	}

	public double applyRotation(double alpha) {
		return angle + alpha;
	}

}
