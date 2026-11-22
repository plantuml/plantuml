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
 */
package net.sourceforge.plantuml.chart.command;

/**
 * Reads the numbers written in a chart.
 *
 * Double.parseDouble alone is not enough: it accepts "NaN", "Infinity" and
 * "-Infinity", and it reads a literal too large for a double ("1e999", a 400-digit
 * integer) as Infinity, all without throwing. Such a value has no place on an axis:
 * fitting an automatic range to it never ended (issue #3030), and drawing it crashed
 * or left NaN in the output. They are rejected here, as any other invalid number, so
 * that the caller reports the line with its usual message.
 */
final class ChartNumber {

	private ChartNumber() {
	}

	/**
	 * The finite value of text.
	 *
	 * @throws NumberFormatException when text is not a number, or is not a finite one
	 */
	static double parse(String text) {
		final double value = Double.parseDouble(text);
		if (Double.isNaN(value) || Double.isInfinite(value))
			throw new NumberFormatException("Not a finite number: " + text);
		return value;
	}

}
