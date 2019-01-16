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
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Stairs {

	private final List<Double> ys = new ArrayList<Double>();
	private final List<Integer> values = new ArrayList<Integer>();
	private final Map<Double, Integer> cache = new HashMap<Double, Integer>();

	@Override
	public String toString() {
		final List<Double> copy = new ArrayList<Double>(ys);
		Collections.sort(copy);
		final StringBuilder sb = new StringBuilder("[");
		for (Double y : copy) {
			sb.append(y + "=" + getValue(y) + " ");
		}
		sb.append("]");
		return sb.toString();
	}

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

	public int getMaxValue() {
		int max = Integer.MIN_VALUE;
		for (Integer v : values) {
			if (v > max) {
				max = v;
			}
		}
		return max;
	}

	public List<Double> getYs() {
		return Collections.unmodifiableList(ys);
	}

	private double getLastY() {
		if (ys.size() == 0) {
			return 0;
		}
		return ys.get(ys.size() - 1);
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
