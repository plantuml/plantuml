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
package net.sourceforge.plantuml.yaml.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.annotation.DuplicateCode;

public class YamlLine {

	private final int indent;
	private final String key;
	private final String value;
	private final List<String> values;
	private final boolean listItem;
	private final YamlLineType type;

	public static YamlLine build(String line) {
		int count = 0;
		line = line.replaceAll("\t", "    ");
		while (count < line.length() && line.charAt(count) == ' ')
			count++;

		String trimmedLine = removeYamlComment(line.substring(count).trim());

		if (trimmedLine.isEmpty())
			return new YamlLine(YamlLineType.EMPTY_LINE, 0, null, null, null, false);

		if (trimmedLine.equals("-"))
			return new YamlLine(YamlLineType.PLAIN_DASH, count + 1, null, null, null, true);

		final boolean listItem = trimmedLine.startsWith("- ");

		if (listItem) {
			count += 2;
			trimmedLine = trimmedLine.substring(2);
		}

		final int colonIndex = trimmedLine.indexOf(':');
		if (colonIndex == -1)
			if (listItem)
				return new YamlLine(YamlLineType.PLAIN_ELEMENT_LIST, count, null, unquote(trimmedLine), null, listItem);
			else
				return new YamlLine(YamlLineType.NO_KEY_ONLY_TEXT, count, null, unquote(trimmedLine), null, false);

		final String rawKey = trimmedLine.substring(0, colonIndex).trim();
		final String rawValue = trimmedLine.substring(colonIndex + 1).trim();

		YamlLineType type = YamlLineType.KEY_AND_VALUE;

		if (rawValue.isEmpty())
			type = YamlLineType.KEY_ONLY;
		else if (rawValue.equals("|"))
			type = YamlLineType.KEY_AND_BLOCK_STYLE;
		else if (rawValue.equals(">"))
			type = YamlLineType.KEY_AND_FOLDED_STYLE;
		else if (rawValue.startsWith("[") && rawValue.endsWith("]"))
			return new YamlLine(YamlLineType.KEY_AND_FLOW_SEQUENCE, count, unquote(rawKey), null,
					toList(rawValue.substring(1, rawValue.length() - 1)), listItem);

		return new YamlLine(type, count, unquote(rawKey), unquote(rawValue), null, listItem);

	}

	private static List<String> toList(String rawValue) {
		final List<String> result = new ArrayList<>();

		final StringBuilder current = new StringBuilder();

		// Zero if we are not in a quoted state or
		// represents the quote character if we are in a quoted string
		char inQuotedString = '\0';

		// Indicates that the current field started with a quote
		boolean fieldStartWithQuote = false;

		for (int i = 0; i < rawValue.length(); i++) {
			final char c = rawValue.charAt(i);

			if (inQuotedString != '\0') {
				// Processing a quoted string
				if (c == '\\') {
					// Handle escaping: append the next character as is if it exists
					if (i + 1 < rawValue.length()) {
						current.append(rawValue.charAt(i + 1));
						i++; // Skip the escaped character
					}
				} else if (c == inQuotedString) {
					// End of the quoted string
					inQuotedString = '\0';
				} else {
					current.append(c);
				}
			} else {
				// We are not in a quoted string.
				// If the field contains only spaces and we encounter a quote,
				// we consider that the field actually starts with a quote.
				if (fieldStartWithQuote == false && current.toString().trim().isEmpty() && (c == '\'' || c == '"')) {
					inQuotedString = c;
					fieldStartWithQuote = true;
					current.setLength(0); // Clear any preliminary spaces
				} else if (c == ',') {
					// The field separator is encountered.
					// For a quoted field, keep the content as is,
					// otherwise apply trim.
					result.add(fieldStartWithQuote ? current.toString() : current.toString().trim());
					// Reset for the next field
					current.setLength(0);
					fieldStartWithQuote = false;
				} else if (c == '\\') {
					// Handle escaping outside of quotes
					if (i + 1 < rawValue.length()) {
						current.append(rawValue.charAt(i + 1));
						i++; // Skip the escaped character
					}
				} else {
					current.append(c);
				}
			}
		}
		// Add the last field (even if it's empty)
		result.add(fieldStartWithQuote ? current.toString() : current.toString().trim());

		return result;
	}

	private YamlLine(YamlLineType type, int indent, String key, String value, List<String> values, boolean listItem) {
		this.type = type;
		this.indent = indent;
		this.key = key;
		this.value = value;
		this.values = values;
		this.listItem = listItem;
	}

	private static String unquote(String str) {
		if (str == null || str.length() < 2)
			return str;

		final char first = str.charAt(0);
		final char last = str.charAt(str.length() - 1);
		if ((first == '"' && last == '"') || (first == '\'' && last == '\''))
			return str.substring(1, str.length() - 1);

		return str;
	}

	public int getIndent() {
		return indent;
	}

	@DuplicateCode(reference = "YamlLines")
	private static String removeYamlComment(String s) {
		if (s == null || s.isEmpty())
			return s;

		char inQuoteChar = '\0';

		if (s.charAt(0) == '#')
			return "";

		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);

			if (c == '\'' || c == '"')
				if (inQuoteChar == '\0')
					inQuoteChar = c;
				else if (c == inQuoteChar)
					inQuoteChar = '\0';

			if (inQuoteChar == '\0' && i < s.length() - 1 && c == ' ' && s.charAt(i + 1) == '#')
				return s.substring(0, i);

		}

		return s;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		if (type == YamlLineType.KEY_AND_VALUE || type == YamlLineType.KEY_AND_FLOW_SEQUENCE
				|| type == YamlLineType.PLAIN_ELEMENT_LIST || type == YamlLineType.NO_KEY_ONLY_TEXT)
			return value;
		throw new IllegalStateException(type.name());
	}

	public boolean isListItem() {
		return listItem;
	}

	public YamlLineType getType() {
		return type;
	}

	public List<String> getValues() {
		if (type == YamlLineType.KEY_AND_FLOW_SEQUENCE)
			return Collections.unmodifiableList(values);
		throw new IllegalStateException(type.name());
	}

	@Override
	public String toString() {
		return "YamlLine(" + type + " indent=" + indent + ", key=" + key + ", value=" + value + ")";
	}

}
