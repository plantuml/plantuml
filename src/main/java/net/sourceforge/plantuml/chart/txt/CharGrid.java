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
package net.sourceforge.plantuml.chart.txt;

import java.util.ArrayList;
import java.util.List;

/**
 * A grid of characters growing as it is drawn on. Cells never written are spaces, and
 * the trailing spaces of each line are left out.
 */
final class CharGrid {

	private final List<StringBuilder> rows = new ArrayList<>();

	void put(int x, int y, char c) {
		if (x < 0 || y < 0)
			return;
		while (rows.size() <= y)
			rows.add(new StringBuilder());
		final StringBuilder row = rows.get(y);
		while (row.length() <= x)
			row.append(' ');
		row.setCharAt(x, c);
	}

	char get(int x, int y) {
		if (x < 0 || y < 0 || y >= rows.size() || x >= rows.get(y).length())
			return ' ';
		return rows.get(y).charAt(x);
	}

	void putString(int x, int y, String s) {
		for (int i = 0; i < s.length(); i++)
			put(x + i, y, s.charAt(i));
	}

	List<String> getLines() {
		final List<String> result = new ArrayList<>();
		for (StringBuilder row : rows)
			result.add(rtrim(row.toString()));
		return result;
	}

	static String rtrim(String s) {
		int end = s.length();
		while (end > 0 && s.charAt(end - 1) == ' ')
			end--;
		return s.substring(0, end);
	}
}
