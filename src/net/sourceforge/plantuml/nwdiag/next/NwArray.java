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
package net.sourceforge.plantuml.nwdiag.next;

public class NwArray {
    // ::remove folder when __HAXE__

	private final NServerDraw data[][];

	public NwArray(int lines, int cols) {
		this.data = new NServerDraw[lines][cols];
	}

	@Override
	public String toString() {
		return "lines=" + getNbLines() + " cols=" + getNbCols();
	}

	public int getNbLines() {
		return data.length;
	}

	public int getNbCols() {
		return data[0].length;
	}

	public NServerDraw get(int i, int j) {
		return data[i][j];
	}

	public NServerDraw[] getLine(int i) {
		return data[i];
	}

	public void set(int i, int j, NServerDraw value) {
		data[i][j] = value;
	}

}
