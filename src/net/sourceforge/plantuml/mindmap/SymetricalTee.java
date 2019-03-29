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

public class SymetricalTee {

	private final double thickness1;
	private final double elongation1;
	private final double thickness2;
	private final double elongation2;

	@Override
	public String toString() {
		return "t1=" + thickness1 + " e1=" + elongation1 + " t2=" + thickness2 + " e2=" + elongation2;
	}

	public SymetricalTee(double thickness1, double elongation1, double thickness2, double elongation2) {
		this.thickness1 = thickness1;
		this.elongation1 = elongation1;
		this.thickness2 = thickness2;
		this.elongation2 = elongation2;
	}

	public double getThickness1() {
		return thickness1;
	}

	public double getElongation1() {
		return elongation1;
	}

	public double getThickness2() {
		return thickness2;
	}

	public double getElongation2() {
		return elongation2;
	}

	public double getFullElongation() {
		return elongation1 + elongation2;
	}

	public double getFullThickness() {
		return Math.max(thickness1, thickness2);
	}

}
