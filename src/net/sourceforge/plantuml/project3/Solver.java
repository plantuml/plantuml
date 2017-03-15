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
package net.sourceforge.plantuml.project3;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Solver {

	private final Map<TaskAttribute, Value> values = new LinkedHashMap<TaskAttribute, Value>();

	public void setData(TaskAttribute attribute, Value value) {
		values.remove(attribute);
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
			throw new UnsupportedOperationException(attribute.toString());
		}
		return result;
	}

	private Instant computeStart() {
		final Instant end = (Instant) values.get(TaskAttribute.END);
		final Duration duration = (Duration) values.get(TaskAttribute.DURATION);
		assert end != null && duration != null;
		return end.sub(duration).increment();
	}

	private Instant computeEnd() {
		final Instant start = (Instant) values.get(TaskAttribute.START);
		final Duration duration = (Duration) values.get(TaskAttribute.DURATION);
		assert start != null && duration != null;
		return start.add(duration).decrement();
	}

}
