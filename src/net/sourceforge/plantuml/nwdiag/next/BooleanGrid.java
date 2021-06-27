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
 */
package net.sourceforge.plantuml.nwdiag.next;

import java.util.HashSet;
import java.util.Set;

public class BooleanGrid {

	private final Set<Integer> burned = new HashSet<>();

	private int merge(int x, int y) {
		return x + (y << 16);
	}

	public void burn(int x, int y) {
		final boolean added = burned.add(merge(x, y));
		if (added == false) {
			throw new IllegalArgumentException("Already present");
		}
	}

	public boolean isBurned(int x, int y) {
		return burned.contains(merge(x, y));
	}

	public void burnRect(int x1, int y1, int x2, int y2) {
		check(x1, y1, x2, y2);
		for (int x = x1; x <= x2; x++)
			for (int y = y1; y <= y2; y++)
				burn(x, y);
	}

	public boolean isBurnRect(int x1, int y1, int x2, int y2) {
		check(x1, y1, x2, y2);
		for (int x = x1; x <= x2; x++)
			for (int y = y1; y <= y2; y++)
				if (isBurned(x, y))
					return true;
		return false;
	}

	private void check(int x1, int y1, int x2, int y2) {
		if (x1 < 0 || y1 < 0 || x2 < 0 || y2 < 0) {
			throw new IllegalArgumentException();
		}
		if (x2 < x1) {
			throw new IllegalArgumentException();
		}
		if (y2 < y1) {
			throw new IllegalArgumentException();
		}
	}

	// -----------------

	public boolean isSpaceAvailable(Staged element, int x) {
		if (isBurnRect(x, element.getStart().getNumber(), x + element.getNWidth() - 1, element.getEnd().getNumber())) {
			return false;
		}
		return true;
	}

	public void useSpace(Staged element, int x) {
		burnRect(x, element.getStart().getNumber(), x + element.getNWidth() - 1, element.getEnd().getNumber());
	}

}
