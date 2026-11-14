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
package net.sourceforge.plantuml.chart.txt;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.sourceforge.plantuml.chart.CartesianChart;
import net.sourceforge.plantuml.chart.ChartSeries;
import net.sourceforge.plantuml.chart.ChartTicks;
import net.sourceforge.plantuml.chart.PieChart;
import net.sourceforge.plantuml.chart.RadarChart;

/**
 * Charts written as tables of values, one row per value with a bar of its size: a pie
 * (the share of each slice), and the charts that are not drawn as text (areas, scatter,
 * radar, coordinate pairs, secondary axis), one table per series.
 */
final class ValueTableTxt {

	// Length of the longest bar
	private static final int BAR_LENGTH = 30;

	private final TxtCharset charset;

	ValueTableTxt(TxtCharset charset) {
		this.charset = charset;
	}

	// A row of a table: its name, the size of its bar and its value as written
	private static final class Row {
		private final String name;
		private final double size;
		private final String value;

		Row(String name, double size, String value) {
			this.name = name;
			this.size = size;
			this.value = value;
		}
	}

	/**
	 * A pie: the share of each slice.
	 */
	List<String> pie(PieChart pie) {
		final List<Row> rows = new ArrayList<>();
		for (PieChart.Slice slice : pie.getSlices()) {
			final String name = slice.getName() == null ? Integer.toString(slice.getIndex() + 1) : slice.getName();
			rows.add(new Row(name, slice.getValue(), PieChart.formatPercent(pie.getPercent(slice))));
		}
		final List<List<Row>> tables = new ArrayList<>();
		tables.add(rows);
		return format(null, tables);
	}

	/**
	 * A chart on cartesian axes: the values of each series, named by their category, or
	 * by their x when they are coordinate pairs.
	 */
	List<String> cartesian(CartesianChart chart) {
		final List<String> headers = new ArrayList<>();
		final List<List<Row>> tables = new ArrayList<>();
		final List<String> categories = chart.getCategories();
		for (ChartSeries s : chart.getSeries()) {
			final List<Row> rows = new ArrayList<>();
			final List<Double> values = s.getValues();
			for (int i = 0; i < values.size(); i++) {
				final Double value = values.get(i);
				if (value == null)
					continue;
				final String name;
				if (s.hasExplicitXValues())
					name = ChartTicks.formatValue(s.getXValues().get(i));
				else if (i < categories.size())
					name = categories.get(i);
				else
					name = Integer.toString(i + 1);
				rows.add(new Row(name, Math.abs(value), ChartTicks.formatValue(value)));
			}
			headers.add(header(s));
			tables.add(rows);
		}
		return format(headers, tables);
	}

	/**
	 * A radar: the value of each series for each category.
	 */
	List<String> radar(RadarChart radar) {
		final List<String> headers = new ArrayList<>();
		final List<List<Row>> tables = new ArrayList<>();
		final List<String> categories = radar.getCategories();
		for (int s = 0; s < radar.getSeries().size(); s++) {
			final List<Row> rows = new ArrayList<>();
			final List<Double> values = radar.getValues().get(s);
			for (int i = 0; i < values.size(); i++)
				if (values.get(i) != null)
					rows.add(new Row(categories.get(i), Math.abs(values.get(i)),
							ChartTicks.formatValue(values.get(i))));
			headers.add(header(radar.getSeries().get(s)));
			tables.add(rows);
		}
		return format(headers, tables);
	}

	private static String header(ChartSeries s) {
		final String type = s.getType().name().toLowerCase(Locale.ROOT);
		if (s.getName() == null || s.getName().isEmpty())
			return type;
		return s.getName() + " (" + type + ")";
	}

	// Tables one after the other, each under its header, with the same columns: the bars
	// are on the same scale, the longest being BAR_LENGTH characters
	private List<String> format(List<String> headers, List<List<Row>> tables) {
		int nameWidth = 0;
		int valueWidth = 0;
		double max = 0;
		for (List<Row> rows : tables)
			for (Row row : rows) {
				nameWidth = Math.max(nameWidth, row.name.length());
				valueWidth = Math.max(valueWidth, row.value.length());
				max = Math.max(max, row.size);
			}
		final List<String> result = new ArrayList<>();
		for (int t = 0; t < tables.size(); t++) {
			if (headers != null) {
				if (t > 0)
					result.add("");
				result.add(headers.get(t));
			}
			for (Row row : tables.get(t)) {
				final StringBuilder sb = new StringBuilder();
				sb.append(pad(row.name, nameWidth)).append("  ");
				int length = max > 0 ? (int) Math.round(BAR_LENGTH * row.size / max) : 0;
				if (length == 0 && row.size > 0)
					length = 1;
				for (int i = 0; i < BAR_LENGTH; i++)
					sb.append(i < length ? charset.fill(0) : ' ');
				sb.append("  ");
				for (int i = row.value.length(); i < valueWidth + 1; i++)
					sb.append(' ');
				sb.append(row.value);
				result.add(sb.toString());
			}
		}
		return result;
	}

	private static String pad(String s, int width) {
		final StringBuilder sb = new StringBuilder(s);
		while (sb.length() < width)
			sb.append(' ');
		return sb.toString();
	}
}
