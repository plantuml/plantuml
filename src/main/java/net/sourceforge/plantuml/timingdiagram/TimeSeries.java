/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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
package net.sourceforge.plantuml.timingdiagram;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class TimeSeries {

	private final SortedMap<TimeTick, Double> values = new TreeMap<TimeTick, Double>();

	private Double minOverride;
	private Double maxOverride;

	private DecimalFormat format;

	public Collection<Double> values() {
		return values.values();
	}

	public Double get(TimeTick tick) {
		return values.get(tick);
	}

	public Set<Entry<TimeTick, Double>> entrySet() {
		return values.entrySet();
	}

	public void put(TimeTick now, double value) {
		values.put(now, value);

	}

	public double getMin() {
		if (minOverride != null)
			return minOverride;

		double min = 0;
		for (Double val : values())
			min = Math.min(min, val);

		return min;
	}

	public double getMax() {
		if (maxOverride != null)
			return maxOverride;

		double max = 0;
		for (Double val : values())
			max = Math.max(max, val);

		if (max == 0)
			return 10;

		return max;
	}

	public void setBounds(String min, String max) {
		this.minOverride = Double.parseDouble(min);
		this.maxOverride = Double.parseDouble(max);

		final DeduceFormat format1 = DeduceFormat.from(min);
		final DeduceFormat format2 = DeduceFormat.from(max);

		this.format = format1.mergeWith(format2).getDecimalFormat();
	}

	public String getDisplayValue(double value) {
		if (format != null)
			return format.format(value);

		return "" + value;
	}

	public double getValueAt(TimeTick tick) {
		final Double result = get(tick);
		if (result != null)
			return result;

		Entry<TimeTick, Double> last = null;
		for (Entry<TimeTick, Double> ent : entrySet()) {
			if (ent.getKey().compareTo(tick) > 0) {
				final double v2 = ent.getValue();
				if (last == null)
					return v2;

				final double t2 = ent.getKey().getTime().doubleValue();
				final double v1 = last.getValue();
				final double t1 = last.getKey().getTime().doubleValue();
				final double p = (tick.getTime().doubleValue() - t1) / (t2 - t1);
				return v1 + (v2 - v1) * p;
			}
			last = ent;
		}
		return last.getValue();
	}

}
