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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class NTetris<S extends Staged> {

	private final Map<S, Integer> all = new LinkedHashMap<>();
	private final BooleanGrid grid = new BooleanGrid();
	
	@Override
	public String toString() {
		return all.toString();
	}

	public void add(S element) {
		int x = 0;
		while (true) {
			if (grid.isSpaceAvailable(element, x)) {
				all.put(element, x);
				grid.useSpace(element, x);
				return;
			}
			x++;
			if (x > 100) {
				throw new IllegalStateException();
			}
		}
	}

	public final Map<S, Integer> getPositions() {
		return Collections.unmodifiableMap(all);
	}

	public int getNWidth() {
		int max = 0;
		for (Entry<S, Integer> ent : all.entrySet()) {
			max = Math.max(max, ent.getValue() + ent.getKey().getNWidth());
		}
		return max;
	}

}
