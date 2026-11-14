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

/**
 * A pie (or a donut) once resolved: one slice per value greater than 0, named by its
 * key or by the a-axis label at its index, with its share of the total. It is built by
 * {@link ChartDiagram#resolvePie()} and read by the renderers, graphic or text.
 */
public final class PieChart {

	private final ChartSeries series;
	private final ChartDiagram.LegendPosition legendPosition;
	private final List<Slice> slices = new ArrayList<>();
	private final double total;

	/**
	 * A slice: its name (null when it has none), its value, and the index of the value in
	 * the series, which gives its color.
	 */
	public static final class Slice {

		private final String name;
		private final double value;
		private final int index;

		Slice(String name, double value, int index) {
			this.name = name;
			this.value = value;
			this.index = index;
		}

		public String getName() {
			return name;
		}

		public double getValue() {
			return value;
		}

		public int getIndex() {
			return index;
		}
	}

	PieChart(ChartSeries series, List<String> angularLabels, ChartDiagram.LegendPosition legendPosition) {
		this.series = series;
		this.legendPosition = legendPosition;
		double sum = 0;
		final List<Double> values = series.getValues();
		for (int i = 0; i < values.size(); i++) {
			final Double value = values.get(i);
			if (value == null || value <= 0)
				continue;
			String name = null;
			if (series.hasKeys())
				name = series.getKeys().get(i);
			else if (i < angularLabels.size())
				name = angularLabels.get(i);
			slices.add(new Slice(name, value, i));
			sum += value;
		}
		this.total = sum;
	}

	public ChartSeries getSeries() {
		return series;
	}

	public ChartDiagram.LegendPosition getLegendPosition() {
		return legendPosition;
	}

	public List<Slice> getSlices() {
		return Collections.unmodifiableList(slices);
	}

	public double getTotal() {
		return total;
	}

	/**
	 * The share of a slice, from 0 to 100.
	 */
	public double getPercent(Slice slice) {
		return 100 * slice.value / total;
	}

	/**
	 * The label written on a slice, as asked by "labels" and "percent", or null when
	 * none is asked.
	 */
	public String getLabel(Slice slice) {
		if (series.isShowLabels() == false && series.isShowPercent() == false)
			return null;
		final String percent = formatPercent(getPercent(slice));
		if (series.isShowLabels() && series.isShowPercent())
			return ChartTicks.formatValue(slice.value) + " (" + percent + ")";
		if (series.isShowLabels())
			return ChartTicks.formatValue(slice.value);
		return percent;
	}

	/**
	 * A share written with one decimal at most: 33.3%, 50%, 0.4%
	 */
	public static String formatPercent(double percent) {
		final String s = String.format(Locale.ROOT, "%.1f", percent);
		return (s.endsWith(".0") ? s.substring(0, s.length() - 2) : s) + "%";
	}
}
