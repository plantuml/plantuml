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
package net.sourceforge.plantuml.asciiverse;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.security.SecurityUtils;

public class InfinitePlan {

	private final List<InfiniteString> plan = new ArrayList<>();

	public void drawChar(char c, int x, int y) {
		ensureSize(y);
		plan.get(y).setCharAt(x, c);
	}

	public void fillRect(char c, int x, int y, int width, int height) {
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				drawChar(c, x + i, y + j);
	}

	private void ensureSize(int y) {
		while (y >= plan.size())
			plan.add(new InfiniteString());
	}

	public char getCharAt(int x, int y) {
		if (y < 0 || y >= plan.size())
			return ' ';

		return plan.get(y).getCharAt(x);
	}

	public void exportTxt(OutputStream os) {
		final PrintStream ps = SecurityUtils.createPrintStream(os);
		for (InfiniteString line : plan) {
			ps.println(line.toString());
		}
	}

}
