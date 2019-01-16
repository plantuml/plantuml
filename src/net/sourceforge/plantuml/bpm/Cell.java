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
package net.sourceforge.plantuml.bpm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cell {

	private Placeable data;
	private final List<Placeable> destinations = new ArrayList<Placeable>();

	public final Placeable getData() {
		return data;
	}

	public final void setData(Placeable data) {
		this.data = data;
	}

	@Override
	public String toString() {
		if (data == null) {
			return super.toString();
		}
		return super.toString() + " " + data;
	}

	public void addConnectionTo2(Placeable other) {
		// Should be an assert
		if (other instanceof BpmElement == false) {
			throw new IllegalArgumentException();
		}
		this.destinations.add(other);
	}

	public List<Placeable> getDestinations2() {
		return Collections.unmodifiableList(destinations);
	}

}
