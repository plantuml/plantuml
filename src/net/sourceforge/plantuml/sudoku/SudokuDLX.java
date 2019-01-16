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
package net.sourceforge.plantuml.sudoku;

import java.util.Random;

import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.StringUtils;

public class SudokuDLX implements ISudoku {

	private final String tab[];
	private final long seed;
	private final long rate;

	public SudokuDLX(Long seed) {
		if (seed == null) {
			this.seed = Math.abs(new Random().nextLong());
		} else {
			this.seed = Math.abs(seed.longValue());
		}
		final DLXEngine engine = new DLXEngine(new Random(this.seed));
		final String s = engine.generate(10000, 100000);
		rate = engine.rate(s.replace("\n", "").trim());
		tab = s.split("\\s");
	}

	public long getRatting() {
		return rate;
	}

	public long getSeed() {
		return seed;
	}

	public int getGiven(int x, int y) {
		final char c = tab[x].charAt(y);
		if (c == '.') {
			return 0;
		}
		return c - '0';
	}

	public void print() {
		for (String s : tab) {
			Log.println(s);
		}
		Log.println("Rate=" + rate);
		Log.println("Seed=" + StringUtils.goUpperCase(Long.toString(seed, 36)));
	}

	public static void main(String[] args) {
		for (int i = 0; i < 1; i++) {
			final SudokuDLX sudoku = new SudokuDLX(null);
			sudoku.print();
		}
	}

}
