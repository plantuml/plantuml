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
 * Original Author:  David Fyfe
 *
 */
package net.sourceforge.plantuml.chart.command;

import java.util.ArrayList;
import java.util.List;

final class ChartDataParser {

	private ChartDataParser() {
	}

	static List<Double> parseValues(String data) {
		final List<Double> result = new ArrayList<>();
		if (data == null || data.trim().isEmpty())
			return result;

		final String[] parts = data.split(",");
		for (String part : parts) {
			try {
				result.add(Double.parseDouble(part.trim()));
			} catch (NumberFormatException e) {
				return null;
			}
		}
		return result;
	}

	static boolean parseCoordinatePairs(String data, List<Double> xValues, List<Double> yValues) {
		if (data == null || data.trim().isEmpty())
			return false;

		// Match pattern: (x,y) or (x, y)
		// Split by closing paren followed by comma and opening paren
		final String cleaned = data.replaceAll("\\s+", ""); // Remove all whitespace
		final String[] pairs = cleaned.split("\\),\\(");

		for (String pair : pairs) {
			// Clean up the pair - remove leading/trailing parens and brackets
			String trimmedPair = pair.trim();
			trimmedPair = trimmedPair.replaceAll("^[\\[\\(]+", ""); // Remove leading [ or (
			trimmedPair = trimmedPair.replaceAll("[\\]\\)]+$", ""); // Remove trailing ] or )

			final String[] coords = trimmedPair.split(",");
			if (coords.length != 2)
				return false;

			try {
				final double x = Double.parseDouble(coords[0].trim());
				final double y = Double.parseDouble(coords[1].trim());
				xValues.add(x);
				yValues.add(y);
			} catch (NumberFormatException e) {
				return false;
			}
		}
		return true;
	}

}
