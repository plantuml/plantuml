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
package net.sourceforge.plantuml.chart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * The ticks of the axes of a chart, and how their values are written. They depend
 * only on the axes, not on the way the chart is drawn: the graphic renderers and the
 * text one place their ticks at the same values, with the same labels.
 */
public final class ChartTicks {

	// Number of intervals drawn by the automatic ticks of a value axis
	private static final int TICK_INTERVALS = 5;

	private ChartTicks() {
	}

	/**
	 * A tick: its value, its position along the axis from 0 (min) to 1 (max), and its
	 * label.
	 */
	public static final class Tick {

		private final double value;
		private final double ratio;
		private final String label;

		Tick(double value, double ratio, String label) {
			this.value = value;
			this.ratio = ratio;
			this.label = label;
		}

		public double getValue() {
			return value;
		}

		public double getRatio() {
			return ratio;
		}

		public String getLabel() {
			return label;
		}
	}

	/**
	 * The ticks of a vertical value axis (and of the radial scale of a radar): custom
	 * ticks if defined, else ticks every "spacing", else five automatic intervals.
	 */
	public static List<Tick> valueTicks(ChartAxis axis) {
		final List<Tick> result = new ArrayList<>();
		final double range = axis.getMax() - axis.getMin();
		if (axis.hasCustomTicks()) {
			for (Map.Entry<Double, String> entry : axis.getCustomTicks().entrySet()) {
				final double ratio = (entry.getKey() - axis.getMin()) / range;
				// Skip if outside axis range
				if (ratio >= 0 && ratio <= 1)
					result.add(new Tick(entry.getKey(), ratio, entry.getValue()));
			}
		} else if (axis.hasTickSpacing()) {
			final double spacing = axis.getTickSpacing();
			// Calculate starting value (round up to nearest spacing interval)
			final double startValue = Math.ceil(axis.getMin() / spacing) * spacing;
			for (double value = startValue; value <= axis.getMax(); value += spacing) {
				// Avoid floating point precision issues
				if (value > axis.getMax() + spacing * 0.01)
					break;
				result.add(new Tick(value, (value - axis.getMin()) / range, formatValue(value)));
			}
		} else {
			for (int i = 0; i <= TICK_INTERVALS; i++) {
				final double value = axis.getMin() + range * i / TICK_INTERVALS;
				result.add(new Tick(value, (double) i / TICK_INTERVALS, formatValue(value)));
			}
		}
		return result;
	}

	/**
	 * The ticks of the value axis of a horizontal chart, drawn along the bottom: always
	 * the five automatic intervals, written as integers.
	 */
	public static List<Tick> horizontalValueTicks(ChartAxis axis) {
		final List<Tick> result = new ArrayList<>();
		final double range = axis.getMax() - axis.getMin();
		for (int i = 0; i <= TICK_INTERVALS; i++) {
			final double value = axis.getMin() + (i * range / TICK_INTERVALS);
			result.add(new Tick(value, (double) i / TICK_INTERVALS, String.format(Locale.ROOT, "%.0f", value)));
		}
		return result;
	}

	/**
	 * The ticks of a numeric h-axis (coordinate pairs, or an h-axis range): one every
	 * "spacing" when given, else about ten, starting at a multiple of the interval.
	 * They are placed by their value, as the points of the series.
	 */
	public static List<Tick> numericTicks(ChartAxis axis, Integer spacing) {
		if (axis == null)
			return Collections.emptyList();
		final List<Tick> result = new ArrayList<>();
		final double range = axis.getMax() - axis.getMin();
		final double tickInterval = spacing != null && spacing > 0 ? spacing : range / 10.0;

		// Find the starting tick value (round down to nearest multiple of tickInterval)
		final double startValue = Math.floor(axis.getMin() / tickInterval) * tickInterval;
		for (double value = startValue; value <= axis.getMax() + tickInterval * 0.01; value += tickInterval) {
			// Skip if outside axis range
			if (value < axis.getMin() - tickInterval * 0.01 || value > axis.getMax() + tickInterval * 0.01)
				continue;
			result.add(new Tick(value, (value - axis.getMin()) / range, formatNumericValue(value)));
		}
		return result;
	}

	/**
	 * A value on a value axis, or on a point label: 0.80 is written 0.8, as the
	 * automatic range gives ticks like 0, 0.8, 1.6 ...
	 */
	public static String formatValue(double value) {
		if (Math.abs(value) < 0.01 && value != 0)
			return String.format(Locale.ROOT, "%.2e", value);
		if (value == (long) value)
			return String.format(Locale.ROOT, "%d", (long) value);
		return String.format(Locale.ROOT, "%.2f", value).replaceAll("0+$", "");
	}

	// A value on a numeric h-axis: one decimal at most
	private static String formatNumericValue(double value) {
		if (Math.abs(value) < 0.01 && value != 0)
			return String.format(Locale.ROOT, "%.2e", value);
		if (value == (long) value)
			return String.format(Locale.ROOT, "%d", (long) value);
		return String.format(Locale.ROOT, "%.1f", value);
	}
}
