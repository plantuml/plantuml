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
package net.sourceforge.plantuml.klimt.shape;

//::comment when __HAXE__
import java.awt.geom.AffineTransform;
//::done

import net.sourceforge.plantuml.klimt.AbstractShadowable;
import net.sourceforge.plantuml.klimt.UShapeSized;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;

public class ULine extends AbstractShadowable implements UShapeSized {

	private final double dx;
	private final double dy;

	public static ULine create(XPoint2D p1, XPoint2D p2) {
		return new ULine(p2.getX() - p1.getX(), p2.getY() - p1.getY());
	}

	public ULine(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}

	// ::comment when __HAXE__
	public ULine rotate(double theta) {
		if (theta == 0)
			return this;
		final AffineTransform rot = AffineTransform.getRotateInstance(theta);
		final XPoint2D tmp = new XPoint2D(dx, dy).transform(rot);
		return new ULine(tmp.getX(), tmp.getY());
	}
	// ::done

	public static ULine hline(double dx) {
		return new ULine(dx, 0);
	}

	public static ULine vline(double dy) {
		return new ULine(0, dy);
	}

	@Override
	public String toString() {
		return "ULine dx=" + dx + " dy=" + dy;
	}

	public double getDX() {
		return dx;
	}

	public double getDY() {
		return dy;
	}

	public double getLength() {
		return Math.sqrt(dx * dx + dy * dy);
	}

	public double getWidth() {
		return dx;
	}

	public double getHeight() {
		return dy;
	}

}
