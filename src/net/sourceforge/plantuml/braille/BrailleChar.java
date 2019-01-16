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
package net.sourceforge.plantuml.braille;

public class BrailleChar {

	private final int id;

	private BrailleChar(int id) {
		this.id = id;
	}

	public void draw(BrailleGrid grid, double xv, double yv) {
		final int x = grid.toInt(xv);
		final int y = grid.toInt(yv);
		drawOneSpot(grid, x + 0, y + 0, 1);
		drawOneSpot(grid, x + 0, y + 1, 2);
		drawOneSpot(grid, x + 0, y + 2, 4);
		drawOneSpot(grid, x + 1, y + 0, 8);
		drawOneSpot(grid, x + 1, y + 1, 16);
		drawOneSpot(grid, x + 1, y + 2, 32);
	}

	private void drawOneSpot(BrailleGrid grid, int x, int y, int check) {
		if ((id & check) != 0) {
			grid.setState(x, y, true);
		}

	}

	public static BrailleChar fromChar(char c) {
		if (c == 'a' || c == 'A' || c == '1') {
			return new BrailleChar(1);
		}
		if (c == 'b' || c == 'B' || c == '2') {
			return new BrailleChar(1 + 2);
		}
		if (c == 'c' || c == 'C' || c == '3') {
			return new BrailleChar(1 + 8);
		}
		if (c == 'd' || c == 'D' || c == '4') {
			return new BrailleChar(1 + 8 + 16);
		}
		if (c == 'e' || c == 'E' || c == '5') {
			return new BrailleChar(1 + 16);
		}
		if (c == 'f' || c == 'F' || c == '6') {
			return new BrailleChar(1 + 8 + 2);
		}
		if (c == 'g' || c == 'G' || c == '7') {
			return new BrailleChar(1 + 2 + 8 + 16);
		}
		if (c == 'h' || c == 'H' || c == '8') {
			return new BrailleChar(1 + 2 + 16);
		}
		if (c == 'i' || c == 'I' || c == '9') {
			return new BrailleChar(2 + 8);
		}
		if (c == 'j' || c == 'J' || c == '0') {
			return new BrailleChar(2 + 8 + 16);
		}
		if (c == 'k' || c == 'K') {
			return new BrailleChar(1 + 4);
		}
		if (c == 'l' || c == 'L') {
			return new BrailleChar(1 + 2 + 4);
		}
		if (c == 'm' || c == 'M') {
			return new BrailleChar(1 + 4 + 8);
		}
		if (c == 'n' || c == 'N') {
			return new BrailleChar(1 + 4 + 8 + 16);
		}
		if (c == 'o' || c == 'O') {
			return new BrailleChar(1 + 4 + 16);
		}
		if (c == 'p' || c == 'P') {
			return new BrailleChar(1 + 2 + 4 + 8);
		}
		if (c == 'q' || c == 'Q') {
			return new BrailleChar(1 + 2 + 4 + 8 + 16);
		}
		if (c == 'r' || c == 'R') {
			return new BrailleChar(1 + 2 + 4 + 16);
		}
		if (c == 's' || c == 'S') {
			return new BrailleChar(2 + 4 + 8);
		}
		if (c == 't' || c == 'T') {
			return new BrailleChar(2 + 4 + 8 + 16);
		}
		if (c == 'u' || c == 'U') {
			return new BrailleChar(1 + 4 + 32);
		}
		if (c == 'v' || c == 'V') {
			return new BrailleChar(1 + 2 + 4 + 32);
		}
		if (c == 'w' || c == 'W') {
			return new BrailleChar(2 + 8 + 16 + 32);
		}
		if (c == 'x' || c == 'X') {
			return new BrailleChar(1 + 4 + 8 + 32);
		}
		if (c == 'y' || c == 'Y') {
			return new BrailleChar(1 + 4 + 8 + 16 + 32);
		}
		if (c == 'z' || c == 'Z') {
			return new BrailleChar(1 + 4 + 16 + 32);
		}
		if (c == ' ') {
			return new BrailleChar(0);
		}
		if (c == '\'') {
			return new BrailleChar(2);
		}
		if (c == ';') {
			return new BrailleChar(2 + 4);
		}
		if (c == ':') {
			return new BrailleChar(2 + 16);
		}
		if (c == '!') {
			return new BrailleChar(2 + 4 + 16);
		}
		if (c == '(' || c == ')') {
			return new BrailleChar(2 + 4 + 16 + 32);
		}
		if (c == '?' || c == '.' || c == '\"') {
			return new BrailleChar(2 + 4 + 32);
		}
		if (c == ',') {
			return new BrailleChar(4);
		}
		if (c == '-') {
			return new BrailleChar(4 + 32);
		}
		return new BrailleChar(63);
	}

}
