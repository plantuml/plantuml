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
package net.sourceforge.plantuml.svek;

import java.awt.geom.Point2D;

public class Oscillator {

	private int n = 3;
	private int i = 0;
	private char seg = 'A';

	private int x = -1;
	private int y = -1;

	public Point2D.Double nextPosition() {
		assert n % 2 == 1;
		final int halfN = (n - 1) / 2;
		final Point2D.Double result = new Point2D.Double(x, y);
		i++;
		if (seg == 'A') {
			x++;
			if (x > halfN) {
				seg = 'B';
				x = halfN;
				y = -halfN + 1;
			}
		} else if (seg == 'B') {
			y++;
			if (y > halfN) {
				seg = 'C';
				x = halfN - 1;
				y = halfN;
			}
		} else if (seg == 'C') {
			x--;
			if (x < -halfN) {
				seg = 'D';
				x = -halfN;
				y = halfN - 1;
			}
		} else if (seg == 'D') {
			y--;
			if (y == -halfN) {
				n += 2;
				i = 0;
				x = -((n - 1) / 2);
				y = x;
				seg = 'A';
			}
		} else {
			throw new UnsupportedOperationException();
		}
		return result;
	}
}
