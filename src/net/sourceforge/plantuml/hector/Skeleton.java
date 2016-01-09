/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
 * Revision $Revision: 5079 $
 *
 */
package net.sourceforge.plantuml.hector;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class Skeleton {

	private final List<Pin> pins;
	private final List<PinLink> pinLinks;
	private final SortedSet<Integer> rows = new TreeSet<Integer>();

	public Skeleton(List<Pin> pins, List<PinLink> pinLinks) {
		this.pins = pins;
		this.pinLinks = pinLinks;
		int uid = 0;
		for (Pin pin : pins) {
			pin.setUid(uid++);
			rows.add(pin.getRow());
		}
	}

	public SortedSet<Integer> getRows() {
		return rows;
	}

	public List<Pin> getPins() {
		return pins;
	}

	public Collection<Pin> getPinsOfRow(int row) {
		final Set<Pin> result = new LinkedHashSet<Pin>();
		for (Pin pin : pins) {
			if (pin.getRow() == row) {
				result.add(pin);
			}
		}
		return result;
	}
	
	public List<PinLink> getPinLinks() {
		return pinLinks;
	}



}
