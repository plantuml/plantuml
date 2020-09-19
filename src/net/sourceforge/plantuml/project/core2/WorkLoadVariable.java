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
package net.sourceforge.plantuml.project.core2;

import java.util.ArrayList;
import java.util.List;

public class WorkLoadVariable implements WorkLoad {

	private final List<Slice> slices = new ArrayList<Slice>();

	public void add(Slice slice) {
		if (slices.size() > 0) {
			final Slice last = slices.get(slices.size() - 1);
			if (slice.getStart() <= last.getEnd()) {
				throw new IllegalArgumentException();
			}
		}
		slices.add(slice);
	}

	public IteratorSlice slices(long timeBiggerThan) {
		for (int i = 0; i < slices.size(); i++) {
			final Slice current = slices.get(i);
			if (current.getEnd() <= timeBiggerThan) {
				continue;
			}
			assert current.getEnd() > timeBiggerThan;
			assert current.getStart() >= timeBiggerThan;
			final List<Slice> tmp = slices.subList(i, slices.size());
			assert tmp.get(0).getStart() >= timeBiggerThan;
			return new ListIteratorSlice(tmp);
		}
		throw new UnsupportedOperationException();
	}

}
