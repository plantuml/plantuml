/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 3837 $
 *
 */
package net.sourceforge.plantuml.sudoku;

import java.util.Random;

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
			System.err.println(s);
		}
		System.err.println("Rate=" + rate);
		System.err.println("Seed=" + Long.toString(seed, 36).toUpperCase());
	}

	public static void main(String[] args) {
		for (int i = 0; i < 1; i++) {
			final SudokuDLX sudoku = new SudokuDLX(null);
			sudoku.print();
		}
	}

}
