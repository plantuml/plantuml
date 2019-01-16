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
package net.sourceforge.plantuml.hector;

class UnlinearCompression {

	static enum Rounding {
		BORDER_1, CENTRAL, BORDER_2;
	}

	private final double inner;
	private final double outer;

	public UnlinearCompression(double inner, double outer) {
		this.inner = inner;
		this.outer = outer;
	}

	public double compress(double x) {
		final double pour = x / (inner + outer);
		final double pourInter = Math.floor(pour);
		x -= pourInter * (inner + outer);
		if (x < inner) {
			return pourInter * outer;
		}
		return x - inner + pourInter * outer;
	}

	public double uncompress(double x, Rounding rounding) {
		final int pourInter = nbOuterBefore(x);
		final boolean onBorder = equals(x, pourInter * outer);
		if (onBorder && rounding == Rounding.BORDER_1) {
			// Nothing
		} else if (onBorder && rounding == Rounding.CENTRAL) {
			x += inner / 2.0;
		} else {
			x += inner;
		}
		x += pourInter * inner;
		return x;
	}

	private static boolean equals(double d1, double d2) {
		return Math.abs(d1 - d2) < .001;
	}

	private int nbOuterBefore(double x) {
		final double pour = x / outer;
		final int pourInter = (int) Math.floor(pour);
		return pourInter;
	}

	public double[] encounteredSingularities(double from, double to) {
		final int outer1 = nbOuterBefore(from) + 1;
		int outer2 = nbOuterBefore(to) + 1;
		if (equals(to, (outer2 - 1) * outer)) {
			outer2--;
		}
		final double result[];
		if (from <= to) {
			result = new double[outer2 - outer1];
			for (int i = 0; i < result.length; i++) {
				result[i] = (outer1 + i) * outer;
			}
		} else {
			result = new double[outer1 - outer2];
			for (int i = 0; i < result.length; i++) {
				result[i] = (outer1 - 1 - i) * outer;
			}

		}
		return result;
	}

	public double innerSize() {
		return inner;
	}

}
