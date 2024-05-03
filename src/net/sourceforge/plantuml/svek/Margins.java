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
package net.sourceforge.plantuml.svek;

import net.sourceforge.plantuml.activitydiagram3.ftile.RectangleCoordinates;

public class Margins {

	static public Margins NONE = new Margins(0, 0, 0, 0);
	private RectangleCoordinates rectangleCoordinates = new RectangleCoordinates(0.0, 0.0, 0.0, 0.0);

	public static Margins uniform(double value) {
		return new Margins(value, value, value, value);
	}

	@Override
	public String toString() {
		return "MARGIN[" + rectangleCoordinates.getX1() + "," + rectangleCoordinates.getX2() + "," + rectangleCoordinates.getY1() + "," + rectangleCoordinates.getY2() + "]";
	}

	public Margins merge(Margins other) {
		return new Margins(//
						Math.max(this.rectangleCoordinates.getX1(), other.rectangleCoordinates.getX1()), //
						Math.max(this.rectangleCoordinates.getX2(), other.rectangleCoordinates.getX2()), //
						Math.max(this.rectangleCoordinates.getY1(), other.rectangleCoordinates.getY1()), //
						Math.max(this.rectangleCoordinates.getY2(), other.rectangleCoordinates.getY2()));
	}

	public Margins(double x1, double x2, double y1, double y2) {
		this.rectangleCoordinates.setX1(x1);
		this.rectangleCoordinates.setX2(x2);
		this.rectangleCoordinates.setY1(y1);
		this.rectangleCoordinates.setY2(y2);
	}

	public boolean isZero() {
		return rectangleCoordinates.getX1() == 0 && rectangleCoordinates.getX2() == 0 && rectangleCoordinates.getY1() == 0 && rectangleCoordinates.getY2() == 0;
	}

	public final double getX1() {
		return rectangleCoordinates.getX1();
	}

	public final double getX2() {
		return rectangleCoordinates.getX2();
	}

	public final double getY1() {
		return rectangleCoordinates.getY1();
	}

	public final double getY2() {
		return rectangleCoordinates.getY2();
	}

	public double getTotalWidth() {
		return rectangleCoordinates.getX1() + rectangleCoordinates.getX2();
	}

	public double getTotalHeight() {
		return rectangleCoordinates.getY1() + rectangleCoordinates.getY2();
	}

}