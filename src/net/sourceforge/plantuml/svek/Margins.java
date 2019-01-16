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
package net.sourceforge.plantuml.svek;

public class Margins {

	private final double x1;
	private final double x2;
	private final double y1;
	private final double y2;

	static public Margins NONE = new Margins(0, 0, 0, 0);

	public static Margins uniform(double value) {
		return new Margins(value, value, value, value);
	}

	@Override
	public String toString() {
		return "MARGIN[" + x1 + "," + x2 + "," + y1 + "," + y2 + "]";
	}

	public Margins(double x1, double x2, double y1, double y2) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}

	public boolean isZero() {
		return x1 == 0 && x2 == 0 && y1 == 0 && y2 == 0;
	}

	public final double getX1() {
		return x1;
	}

	public final double getX2() {
		return x2;
	}

	public final double getY1() {
		return y1;
	}

	public final double getY2() {
		return y2;
	}

	public double getTotalWidth() {
		return x1 + x2;
	}

	public double getTotalHeight() {
		return y1 + y2;
	}

}