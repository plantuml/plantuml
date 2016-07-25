/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 *
 * Revision $Revision: 6170 $
 *
 */
package net.sourceforge.plantuml.braille;

import java.util.HashSet;
import java.util.Set;

public class BrailleGrid {

	private int minX;
	private int minY;
	private int maxX;
	private int maxY;

	private final double quanta;
	private final Set<Coords> on = new HashSet<Coords>();

	public BrailleGrid(double quanta) {
		this.quanta = quanta;
	}

	public boolean getState(int x, int y) {
		final Coords coords = new Coords(x, y);
		return on.contains(coords);
	}

	public void setState(int x, int y, boolean state) {
		final Coords coords = new Coords(x, y);
		if (state) {
			on.add(coords);
		} else {
			on.remove(coords);
		}
		minX = Math.min(minX, x);
		maxX = Math.max(maxX, x);
		minY = Math.min(minY, y);
		maxY = Math.max(maxY, y);
	}

	public int getMinX() {
		return minX;
	}

	public int getMinY() {
		return minY;
	}

	public int getMaxX() {
		return maxX;
	}

	public int getMaxY() {
		return maxY;
	}

	public void rectangle(double x, double y, double width, double height) {
		hline(y, x, x + width);
		hline(y + height, x, x + width);
		vline(x, y, y + height);
		vline(x + width, y, y + height);

	}

	private void vline(double x, double y1, double y2) {
		final int i = toInt(x);
		final int j1 = toInt(y1);
		final int j2 = toInt(y2);
		for (int j = j1; j <= j2; j++) {
			setState(i, j, true);
		}
	}

	private void hline(double y, double x1, double x2) {
		final int j = toInt(y);
		final int i1 = toInt(x1);
		final int i2 = toInt(x2);
		for (int i = i1; i <= i2; i++) {
			setState(i, j, true);
		}
	}

	private int toInt(double value) {
		return (int) Math.round(value / quanta);
	}

	public void line(double x1, double y1, double x2, double y2) {
		if (x1 == x2) {
			vline(x1, y1, y2);
		} else if (y1 == y2) {
			hline(y1, x1, x2);
		} else {
			System.err.println("warning line");
		}

	}
}
