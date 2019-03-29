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
package net.sourceforge.plantuml.mindmap;

import java.awt.geom.Line2D;

public class SymetricalTeePositioned {

	private final SymetricalTee tee;
	private double y;

	@Override
	public String toString() {
		return "y=" + y + " " + tee;
	}

	public SymetricalTeePositioned(SymetricalTee tee) {
		this(tee, 0);
	}

	private SymetricalTeePositioned(SymetricalTee tee, double y) {
		this.tee = tee;
		this.y = y;
	}

	public void moveSoThatSegmentA1isOn(double newY) {
		final double current = getSegmentA1().getY1();
		y += newY - current;
	}

	public void moveSoThatSegmentA2isOn(double newY) {
		final double current = getSegmentA2().getY1();
		y += newY - current;
	}

	public void move(double delta) {
		y += delta;
	}

	public Line2D getSegmentA1() {
		return new Line2D.Double(0, y - tee.getThickness1() / 2, tee.getElongation1(), y - tee.getThickness1() / 2);
	}

	public Line2D getSegmentB1() {
		return new Line2D.Double(0, y + tee.getThickness1() / 2, tee.getElongation1(), y + tee.getThickness1() / 2);
	}

	public Line2D getSegmentA2() {
		return new Line2D.Double(tee.getElongation1(), y - tee.getThickness2() / 2, tee.getElongation1()
				+ tee.getElongation2(), y - tee.getThickness2() / 2);
	}

	public Line2D getSegmentB2() {
		return new Line2D.Double(tee.getElongation1(), y + tee.getThickness2() / 2, tee.getElongation1()
				+ tee.getElongation2(), y + tee.getThickness2() / 2);
	}

	public double getMaxX() {
		return tee.getElongation1() + tee.getElongation2();
	}

	public double getMaxY() {
		return y + Math.max(tee.getThickness1() / 2, tee.getThickness2() / 2);
	}

	public double getMinY() {
		return y - Math.max(tee.getThickness1() / 2, tee.getThickness2() / 2);
	}

	public final double getY() {
		return y;
	}

	public SymetricalTeePositioned getMax(SymetricalTeePositioned other) {
		if (this.tee != other.tee) {
			throw new IllegalArgumentException();
		}
		if (other.y > this.y) {
			return other;
		}
		return this;
	}

}
