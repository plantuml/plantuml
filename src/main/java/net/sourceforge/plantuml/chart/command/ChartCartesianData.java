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

/**
 * The data of a bar, a line, an area or a scatter, written between brackets.
 *
 * Each item is a value, optionally preceded by a key, or a coordinate pair (x, y) for a line
 * or a scatter:
 * <pre>
 * [10, 20, , 40]
 * [Jan: 10, Feb: 20, "Mar": 40]
 * [(1, 2), (3, 4)]
 * </pre>
 * An empty value, as in [4, , 5], is a missing value. The keys are categories of the h-axis
 * (see ChartDiagram): either every value has a key, or none has. As for a pie, the syntax is lenient
 * enough to accept JSON-like data: the items may be enclosed in brackets, braces or
 * parentheses, values may be quoted with double or single quotes, and a coordinate pair may
 * be written between brackets, braces or parentheses:
 * <pre>
 * {"10", '20', 30}
 * [[1, 2], [3, "4"]]
 * </pre>
 */
final class ChartCartesianData {

	private final List<Double> values = new ArrayList<>();
	private final List<Double> xValues = new ArrayList<>();
	private final List<String> keys = new ArrayList<>();
	private boolean pairs;
	private boolean keyed;
	private String error;

	private ChartCartesianData() {
	}

	static ChartCartesianData parse(String data, boolean pairsAllowed) {
		final ChartCartesianData result = new ChartCartesianData();
		final String mismatch = ChartDataItems.checkDelimiters(data);
		if (mismatch != null)
			return result.fail(mismatch);
		data = ChartDataItems.stripDelimiters(data);
		if (data == null || data.trim().isEmpty())
			return result;

		final List<String> items = split(data, ',');
		if (items == null)
			return result.fail("Unbalanced quotes or brackets in data");

		for (int i = 0; i < items.size(); i++) {
			String item = items.get(i).trim();
			final List<String> parts = split(item, ':');
			if (parts.size() > 2)
				return result.fail("Too many ':' in \"" + item + "\": quote the key");
			final String key = parts.size() == 2 ? ChartDataItems.unquote(parts.get(0).trim()) : null;
			if (key != null) {
				item = parts.get(1).trim();
				if (key.isEmpty())
					return result.fail("Empty key before the value " + item);
			}
			if (i == 0)
				result.keyed = key != null;
			else if ((key != null) != result.keyed)
				return result.fail("Either every value has a key (key: value), or none has");
			result.keys.add(key);
			final boolean pair = item.isEmpty() == false && ChartDataItems.isOpening(item.charAt(0));
			if (pair && key != null)
				return result.fail("A coordinate pair cannot have a key: \"" + items.get(i).trim() + "\"");
			if (pair && pairsAllowed == false)
				return result.fail("Coordinate pairs are not allowed here: \"" + item + "\"");
			if (i == 0)
				result.pairs = pair;
			else if (pair != result.pairs)
				return result.fail("Either every item is a coordinate pair (x, y), or none is");
			final String error = pair ? result.addPair(item) : result.addValue(item);
			if (error != null)
				return result.fail(error);
		}
		return result;
	}

	private ChartCartesianData fail(String message) {
		this.error = message;
		return this;
	}

	private String addValue(String item) {
		final String text = ChartDataItems.unquote(item).trim();
		if (text.isEmpty()) {
			values.add(null);
			return null;
		}
		final Double value = parseNumber(text);
		if (value == null)
			return "Invalid number \"" + text + "\"";
		values.add(value);
		return null;
	}

	private String addPair(String item) {
		final String mismatch = ChartDataItems.checkDelimiters(item);
		if (mismatch != null)
			return mismatch.replace("around data", "around a coordinate pair");
		final List<String> coords = split(ChartDataItems.stripDelimiters(item), ',');
		if (coords == null || coords.size() != 2)
			return "Invalid coordinate pair \"" + item + "\"";
		final Double x = parseNumber(ChartDataItems.unquote(coords.get(0).trim()).trim());
		final Double y = parseNumber(ChartDataItems.unquote(coords.get(1).trim()).trim());
		if (x == null || y == null)
			return "Invalid coordinate pair \"" + item + "\"";
		xValues.add(x);
		values.add(y);
		return null;
	}

	private static Double parseNumber(String text) {
		try {
			return Double.parseDouble(text);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	// Splits on a separator outside quotes and outside nested brackets; null when unbalanced
	private static List<String> split(String s, char separator) {
		final List<String> result = new ArrayList<>();
		final StringBuilder current = new StringBuilder();
		char quote = 0;
		int depth = 0;
		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			if (quote != 0) {
				if (c == quote)
					quote = 0;
			} else if (c == '"' || (c == '\'' && startsText(current))) {
				quote = c;
			} else if (ChartDataItems.isOpening(c)) {
				depth++;
			} else if (c == ']' || c == '}' || c == ')') {
				depth--;
				if (depth < 0)
					return null;
			} else if (c == separator && depth == 0) {
				result.add(current.toString());
				current.setLength(0);
				continue;
			}
			current.append(c);
		}
		if (quote != 0 || depth != 0)
			return null;
		result.add(current.toString());
		return result;
	}

	// A single quote only opens a quoted text where a text starts, so that an apostrophe, as
	// in Driver's: 5, needs no quoting
	private static boolean startsText(StringBuilder current) {
		final String before = current.toString().trim();
		if (before.isEmpty())
			return true;
		final char last = before.charAt(before.length() - 1);
		return last == ':' || last == ',' || ChartDataItems.isOpening(last);
	}

		String getError() {
		return error;
	}

	// True when the items are coordinate pairs: the y are then in getValues()
	boolean isPairs() {
		return pairs;
	}

	// True when every value has a key: see getKeys()
	boolean isKeyed() {
		return keyed;
	}

	List<String> getKeys() {
		return keys;
	}

	List<Double> getValues() {
		return values;
	}

	List<Double> getXValues() {
		return xValues;
	}

}
