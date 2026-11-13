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
package net.sourceforge.plantuml.chart.command;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.chart.ChartSeries;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;

/**
 * The data of a pie or a radar, written between brackets.
 *
 * Each item is a value, optionally preceded by a key and followed by a color:
 * <pre>
 * [78, 21, 1]
 * [Nitrogen: 78, Oxygen: 21 #DC3912, "Rare gases, others": 1]
 * </pre>
 * A key holding a comma, a colon or a '#' must be quoted. Either every item has a key, or
 * none has. An empty value, as in [4, , 5], is a missing value.
 */
final class ChartDataItems {

	private final List<String> keys = new ArrayList<>();
	private final List<Double> values = new ArrayList<>();
	private final List<HColor> colors = new ArrayList<>();
	private boolean keyed;
	private boolean hasColor;
	private String error;

	private ChartDataItems() {
	}

	static ChartDataItems parse(String data, HColorSet colorSet) {
		final ChartDataItems result = new ChartDataItems();
		if (data == null || data.trim().isEmpty())
			return result;

		final List<String> items = split(data, ',');
		if (items == null)
			return result.fail("Unbalanced quotes in data");

		for (int i = 0; i < items.size(); i++) {
			final String error = result.add(items.get(i).trim(), colorSet);
			if (error != null)
				return result.fail(error);
			final boolean itemKeyed = result.keys.get(i) != null;
			if (i == 0)
				result.keyed = itemKeyed;
			else if (itemKeyed != result.keyed)
				return result.fail("Either every value has a key (key: value), or none has");
		}
		return result;
	}

	private ChartDataItems fail(String message) {
		this.error = message;
		return this;
	}

	private String add(String item, HColorSet colorSet) {
		String key = null;
		final List<String> parts = split(item, ':');
		if (parts.size() > 2)
			return "Too many ':' in \"" + item + "\": quote the key";
		if (parts.size() == 2) {
			key = unquote(parts.get(0).trim());
			if (key.isEmpty())
				return "Empty key in \"" + item + "\"";
			item = parts.get(1).trim();
		}

		HColor color = null;
		final int sharp = item.indexOf('#');
		if (sharp >= 0) {
			final String colorName = item.substring(sharp).trim();
			item = item.substring(0, sharp).trim();
			try {
				color = colorSet.getColor(colorName);
			} catch (NoSuchColorException e) {
				return "Unknown color " + colorName;
			}
			hasColor = true;
		}

		Double value = null;
		if (item.isEmpty() == false)
			try {
				value = Double.parseDouble(item);
			} catch (NumberFormatException e) {
				return "Invalid number \"" + item + "\"";
			}

		keys.add(key);
		values.add(value);
		colors.add(color);
		return null;
	}

	// Splits on a separator outside double quotes; null when a quote is not closed
	private static List<String> split(String s, char separator) {
		final List<String> result = new ArrayList<>();
		final StringBuilder current = new StringBuilder();
		boolean quoted = false;
		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			if (c == '"')
				quoted = quoted == false;
			if (c == separator && quoted == false) {
				result.add(current.toString());
				current.setLength(0);
			} else {
				current.append(c);
			}
		}
		if (quoted)
			return null;
		result.add(current.toString());
		return result;
	}

	static String unquote(String s) {
		if (s.length() >= 2 && s.startsWith("\"") && s.endsWith("\""))
			return s.substring(1, s.length() - 1);
		return s;
	}

	// A list of labels, as in a-axis [Dev, "Ops, Run", QA]
	static List<String> parseLabels(String data) {
		final List<String> result = new ArrayList<>();
		if (data == null || data.trim().isEmpty())
			return result;
		final List<String> parts = split(data, ',');
		if (parts == null)
			return null;
		for (String part : parts)
			result.add(unquote(part.trim()));
		return result;
	}

	String getError() {
		return error;
	}

	List<Double> getValues() {
		return values;
	}

	// Gives its keys and colors to a series built from these values
	void applyTo(ChartSeries series) {
		if (keyed)
			series.setKeys(keys);
		if (hasColor)
			series.setItemColors(colors);
	}

}
