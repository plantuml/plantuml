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
import java.util.List;

/**
 * Where each value of stacked series lies, in values (not in pixels): for the i-th
 * value of the s-th series, the segment from its base to its top.
 * <p>
 * Stacked bars: positive values are piled up from 0 upwards, negative ones from 0
 * downwards, apart. Areas: each value is put on top of the values of the previous
 * series at the same index, whatever their sign.
 * <p>
 * A missing value (null) has no segment and does not count in the stack. The ranges
 * of the axes (see {@code ChartDiagram}) and the renderers use the same layout, so
 * that a stack always fits in its axis.
 */
public final class StackLayout {

	// bases.get(s).get(i) is null when the i-th value of the s-th series is missing
	private final List<List<Double>> bases = new ArrayList<>();
	private final List<List<Double>> tops = new ArrayList<>();
	// Areas only: whether a series before the s-th one has a value at index i
	private final List<List<Boolean>> onPrevious = new ArrayList<>();

	private StackLayout() {
	}

	/**
	 * The layout of stacked bars: positive and negative values grow apart from 0.
	 */
	public static StackLayout bars(List<ChartSeries> series) {
		final StackLayout result = new StackLayout();
		final List<Double> up = new ArrayList<>();
		final List<Double> down = new ArrayList<>();
		for (ChartSeries s : series) {
			final List<Double> base = new ArrayList<>();
			final List<Double> top = new ArrayList<>();
			final List<Double> values = s.getValues();
			for (int i = 0; i < values.size(); i++) {
				final Double value = values.get(i);
				if (value == null) {
					base.add(null);
					top.add(null);
					continue;
				}
				final List<Double> totals = value >= 0 ? up : down;
				final double from = get(totals, i);
				final double to = addAt(totals, i, value);
				base.add(from);
				top.add(to);
			}
			result.bases.add(base);
			result.tops.add(top);
		}
		return result;
	}

	/**
	 * The layout of areas: each one is on top of the previous ones.
	 */
	public static StackLayout areas(List<ChartSeries> series) {
		final StackLayout result = new StackLayout();
		final List<Double> totals = new ArrayList<>();
		final List<Boolean> reached = new ArrayList<>();
		for (ChartSeries s : series) {
			final List<Double> base = new ArrayList<>();
			final List<Double> top = new ArrayList<>();
			final List<Boolean> previous = new ArrayList<>();
			final List<Double> values = s.getValues();
			for (int i = 0; i < values.size(); i++) {
				previous.add(i < reached.size() && reached.get(i));
				final Double value = values.get(i);
				if (value == null) {
					base.add(null);
					top.add(null);
					continue;
				}
				final double from = get(totals, i);
				final double to = addAt(totals, i, value);
				base.add(from);
				top.add(to);
			}
			for (int i = 0; i < values.size(); i++)
				if (values.get(i) != null) {
					while (reached.size() <= i)
						reached.add(false);
					reached.set(i, true);
				}
			result.bases.add(base);
			result.tops.add(top);
			result.onPrevious.add(previous);
		}
		return result;
	}

	/**
	 * Whether the i-th value of the s-th series is there.
	 */
	public boolean has(int s, int i) {
		return i < tops.get(s).size() && tops.get(s).get(i) != null;
	}

	/**
	 * The value where the segment of the i-th value of the s-th series starts.
	 */
	public double getBase(int s, int i) {
		return bases.get(s).get(i);
	}

	/**
	 * The value where the segment of the i-th value of the s-th series ends: its base
	 * plus its value.
	 */
	public double getTop(int s, int i) {
		return tops.get(s).get(i);
	}

	/**
	 * Areas only: the base of each value of the s-th series, or null at an index where
	 * no previous series has a value (the area then starts from the bottom of the plot).
	 * Null for the first series, which lies on the bottom of the plot.
	 */
	public List<Double> getAreaBaseline(int s) {
		if (s == 0)
			return null;
		final List<Double> result = new ArrayList<>();
		for (int i = 0; i < onPrevious.get(s).size(); i++)
			result.add(onPrevious.get(s).get(i) ? get(bases.get(s), i) : null);
		return result;
	}

	private static double get(List<Double> totals, int index) {
		if (index >= totals.size() || totals.get(index) == null)
			return 0;
		return totals.get(index);
	}

	private static double addAt(List<Double> totals, int index, double value) {
		while (totals.size() <= index)
			totals.add(0.0);
		final double result = totals.get(index) + value;
		totals.set(index, result);
		return result;
	}
}
