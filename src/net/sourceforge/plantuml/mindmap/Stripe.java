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

public class Stripe implements Comparable<Stripe> {

	private final double x1;
	private final double x2;
	private final double value;

	@Override
	public String toString() {
		return "" + (int) x1 + "->" + (int) x2 + " (" + (int) value + ")";
	}

	public Stripe(double x1, double x2, double value) {
		if (x2 <= x1) {
			System.err.println("x1=" + x1);
			System.err.println("x2=" + x2);
			throw new IllegalArgumentException();
		}
		this.x1 = x1;
		this.x2 = x2;
		this.value = value;
	}

	public boolean contains(double x) {
		return x >= x1 && x <= x2;
	}

	public int compareTo(Stripe other) {
		return (int) Math.signum(this.x1 - other.x1);
	}

	public double getValue() {
		return value;
	}

	public final double getStart() {
		return x1;
	}

	public final double getEnd() {
		return x2;
	}

}
