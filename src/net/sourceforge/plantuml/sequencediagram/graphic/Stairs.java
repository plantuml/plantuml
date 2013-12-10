/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * Revision $Revision: 6054 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Stairs {

	private final List<Double> ys = new ArrayList<Double>();
	private final List<Integer> values = new ArrayList<Integer>();
	private final Map<Double, Integer> cache = new HashMap<Double, Integer>();

	public void addStep(double y, int value) {
		assert ys.size() == values.size();
		if (ys.size() > 0) {
			final double lastY = ys.get(ys.size() - 1);
			if (y < lastY) {
				throw new IllegalArgumentException();
			}
			if (lastY == y) {
				values.set(ys.size() - 1, value);
				cache.clear();
				return;
			}
		}
		ys.add(y);
		values.add(value);
		cache.clear();
	}

	public int getValue(double y) {
		Integer result = cache.get(y);
		if (result == null) {
			result = getValueSlow(y);
			cache.put(y, result);
		}
		return result;
	}

	private int getValueSlow(double y) {
		final int idx = Collections.binarySearch(ys, y);
		if (idx >= 0) {
			return values.get(idx);
		}
		final int insertPoint = -idx - 1;
		if (insertPoint == 0) {
			return 0;
		}
		return values.get(insertPoint - 1);
	}

	public int getLastValue() {
		final int size = values.size();
		if (size == 0) {
			return 0;
		}
		return values.get(size - 1);
	}

}
