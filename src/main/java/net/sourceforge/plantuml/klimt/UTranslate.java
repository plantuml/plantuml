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
package net.sourceforge.plantuml.klimt;

import java.awt.geom.AffineTransform;

import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.geom.XRectangle2D;

public class UTranslate implements UChange {
	private final double dx;
	private final double dy;

	@Override
	public String toString() {
		return "translate dx=" + dx + " dy=" + dy;
	}

	public static UTranslate none() {
		return new UTranslate(0, 0);
	}

	public UTranslate(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public static UTranslate dx(double dx) {
		return new UTranslate(dx, 0);
	}

	public static UTranslate dy(double dy) {
		return new UTranslate(0, dy);
	}

	public static UTranslate point(XPoint2D p) {
		return new UTranslate(p.getX(), p.getY());
	}

	public double getDx() {
		return dx;
	}

	public double getDy() {
		return dy;
	}

	public boolean isAlmostSame(UTranslate other) {
		return this.dx == other.dx || this.dy == other.dy;
	}

	public XPoint2D getTranslated(XPoint2D p) {
		if (p == null)
			return null;

		return new XPoint2D(p.getX() + dx, p.getY() + dy);
	}

	public UTranslate scaled(double scale) {
		return new UTranslate(dx * scale, dy * scale);
	}

	public UTranslate compose(UTranslate other) {
		return new UTranslate(dx + other.dx, dy + other.dy);
	}

	public UTranslate reverse() {
		return new UTranslate(-dx, -dy);
	}

	public XRectangle2D apply(XRectangle2D rect) {
		return new XRectangle2D(rect.getX() + dx, rect.getY() + dy, rect.getWidth(), rect.getHeight());
	}

	public UTranslate multiplyBy(double v) {
		return new UTranslate(dx * v, dy * v);
	}

	public UTranslate sym() {
		return new UTranslate(dy, dx);
	}

	public XPoint2D getPosition() {
		return new XPoint2D(dx, dy);
	}

	public UTranslate rotate(double angle) {
		final AffineTransform rotate = AffineTransform.getRotateInstance(angle);
		return UTranslate.point(new XPoint2D(dx, dy).transform(rotate));

	}

}
