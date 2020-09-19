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

public class TeethRange {

	private final List<Tooth> list = new ArrayList<Tooth>();

	public void add(Tooth tooth) {
		if (list.size() > 0) {
			final Tooth last = list.get(list.size() - 1);
			if (tooth.getStart() <= last.getEnd()) {
				throw new IllegalArgumentException();
			}
		}
		list.add(tooth);
	}

	@Override
	public String toString() {
		return list.toString();
	}

	public long getStart() {
		return list.get(0).getStart();
	}

	public long getEnd() {
		return list.get(list.size() - 1).getEnd();
	}

	public long getDuration() {
		return getEnd() - getStart();
	}

}
