/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
package net.sourceforge.plantuml.bpm;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.SortedSet;

public class CleanerInterleavingLines implements GridCleaner {

	public boolean clean(Grid grid) {
		boolean result = false;
		Line previous = null;
		for (Line line : grid.lines().toList()) {
			if (previous != null) {
				final Collection<Col> cols1 = grid.usedColsOf(previous);
				final Collection<Col> cols2 = grid.usedColsOf(line);
				if (Collections.disjoint(cols1, cols2)) {
//					final SortedSet<Col> used1 = grid.colsConnectedTo(previous);
//					final SortedSet<Col> used2 = grid.colsConnectedTo(line);
//					if (mergeable(used1, used2)) {
//						System.err.println("CAN BE MERGE!");
//						grid.mergeLines(previous, line);
//						result = true;
//					}
				}
			}
			previous = line;
		}
		return result;
	}

	private boolean mergeable(SortedSet<Col> used1, SortedSet<Col> used2) {
		final Comparator<? super Col> s = used1.comparator();
		assert s == used2.comparator();
		if (s.compare(used1.last(), used2.first()) <= 0) {
			return true;
		}
		if (s.compare(used2.last(), used1.first()) <= 0) {
			return true;
		}
		return false;
	}
}
