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
package net.sourceforge.plantuml.project;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.sourceforge.plantuml.project.core.TaskAttribute;
import net.sourceforge.plantuml.project.time.Wink;

public class Solver3 {

	private final Map<TaskAttribute, Value> values = new LinkedHashMap<TaskAttribute, Value>();

	private final LoadPlanable loadPlanable;

	public Solver3(LoadPlanable loadPlanable) {
		this.loadPlanable = loadPlanable;
	}

	public void setData(TaskAttribute attribute, Value value) {
		final Value previous = values.remove(attribute);
		if (previous != null && attribute == TaskAttribute.START) {
			final Wink previousInstant = (Wink) previous;
			if (previousInstant.compareTo((Wink) value) > 0) {
				value = previous;
			}
		}
		values.put(attribute, value);
		if (values.size() > 2) {
			removeFirstElement();
		}
		assert values.size() <= 2;

	}

	private void removeFirstElement() {
		final Iterator<Entry<TaskAttribute, Value>> it = values.entrySet().iterator();
		it.next();
		it.remove();
	}

	public Value getData(TaskAttribute attribute) {
		Value result = values.get(attribute);
		if (result == null) {
			if (attribute == TaskAttribute.END) {
				return computeEnd();
			}
			if (attribute == TaskAttribute.START) {
				return computeStart();
			}
			return Load.inWinks(1);
			// throw new UnsupportedOperationException(attribute.toString());
		}
		return result;
	}

	private Wink computeEnd() {
		Wink current = (Wink) values.get(TaskAttribute.START);
		int fullLoad = ((Load) values.get(TaskAttribute.LOAD)).getFullLoad();
		while (fullLoad > 0) {
			fullLoad -= loadPlanable.getLoadAt(current);
			current = current.increment();
		}
		return current.decrement();
	}

	private Wink computeStart() {
		Wink current = (Wink) values.get(TaskAttribute.END);
		int fullLoad = ((Load) values.get(TaskAttribute.LOAD)).getFullLoad();
		while (fullLoad > 0) {
			fullLoad -= loadPlanable.getLoadAt(current);
			current = current.decrement();
			if (current.getWink() <= 0) {
				return current;
			}
		}
		return current.increment();
	}

}
