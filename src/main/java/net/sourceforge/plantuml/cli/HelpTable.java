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
package net.sourceforge.plantuml.cli;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class HelpTable {

	private final List<String[]> lines = new ArrayList<>();

	public void newLine(String... cells) {
		if (lines.size() > 0 && cells.length != nbCols())
			throw new IllegalArgumentException();

		lines.add(cells);
	}

	public int size(int cols) {
		int result = 0;
		for (String[] line : lines)
			result = Math.max(result, line[cols].length());
		return result;
	}

	public int nbCols() {
		if (lines.size() == 0)
			throw new IllegalStateException();
		return lines.get(0).length;
	}

	private static String format(String s, int size, char format) {
		final StringBuilder result = new StringBuilder(s);
		while (result.length() < size)
			result.append(format);
		return result.toString();
	}

	public void printMe(PrintStream out) {
		final int size0 = size(0);
		int i = 0;
		for (String[] line : lines)
			if (line[0].length() == 0) {
				out.println();
				out.println(line[1] + ":");
				// out.println();

			} else
				out.println(format(line[0] + " ", size0 + 2, '.') + " " + line[1]);

	}

}
