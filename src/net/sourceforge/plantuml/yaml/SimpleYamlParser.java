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
 */
package net.sourceforge.plantuml.yaml;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.json.JsonArray;
import net.sourceforge.plantuml.json.JsonObject;
import net.sourceforge.plantuml.json.JsonValue;

public class SimpleYamlParser {

	private static final String KEY = "([_0-9\\w][- _0-9\\w./]*)";

	private JsonObject result;
	private final List<Integer> pendingIndents = new ArrayList<Integer>();

	public JsonObject parse(List<String> lines) {
		List<String> tmp = new ArrayList<String>();
		for (String s : lines) {
			if (s.trim().length() == 0)
				continue;
			if (s.trim().startsWith("#"))
				continue;
			tmp.add(s);
		}
		tmp = mergeMultiline(tmp);
		result = new JsonObject();
		pendingIndents.clear();
		pendingIndents.add(0);
		for (String s : tmp) {
			parseSingleLine(s);
		}
		return result;

	}

	private List<String> mergeMultiline(List<String> tmp) {
		final List<String> result = new ArrayList<String>();
		for (int i = 0; i < tmp.size(); i++) {
			if (nameOnly(tmp.get(i)) != null && textOnly(tmp.get(i + 1))) {
				final StringBuilder sb = new StringBuilder(tmp.get(i));
				while (textOnly(tmp.get(i + 1))) {
					sb.append(" " + tmp.get(i + 1).trim());
					i++;
				}
				result.add(sb.toString());
			} else {
				result.add(tmp.get(i));
			}
		}
		return result;
	}

	private String[] dashNameAndValue(String s) {
		final Pattern p1 = Pattern.compile("^\\s*[-]\\s*" + KEY + "\\s*:\\s*(\\S.*)$");
		final Matcher m1 = p1.matcher(s);
		if (m1.matches()) {
			final String name = m1.group(1);
			final String data = m1.group(2).trim();
			return new String[] { name, data };
		}
		return null;
	}

	private String[] nameAndValue(String s) {
		final Pattern p1 = Pattern.compile("^\\s*" + KEY + "\\s*:\\s*(\\S.*)$");
		final Matcher m1 = p1.matcher(s);
		if (m1.matches()) {
			final String name = m1.group(1);
			final String data = m1.group(2).trim();
			return new String[] { name, data };
		}
		return null;
	}

	private String nameOnly(String s) {
		final Pattern p1 = Pattern.compile("^\\s*" + KEY + "\\s*:\\s*\\|?\\s*$");
		final Matcher m1 = p1.matcher(s);
		if (m1.matches()) {
			final String name = m1.group(1);
			return name;
		}
		return null;
	}

	private String listedValue(String s) {
		final Pattern p1 = Pattern.compile("^\\s*[-]\\s*(\\S.*)$");
		final Matcher m1 = p1.matcher(s);
		if (m1.matches()) {
			final String name = m1.group(1).trim();
			return name;
		}
		return null;
	}

	private boolean textOnly(String s) {
		if (isList(s))
			return false;
		return s.indexOf(':') == -1;
	}

	private void parseSingleLine(String s) {
		// System.err.println("s=" + s);
		final int indent = getIndent(s);

		if (isListStrict(s)) {
			muteToArray(indent);
			return;
		}

		final String[] dashNameAndValue = dashNameAndValue(s);
		if (dashNameAndValue != null) {
			muteToArray(indent);
			parseSingleLine(s.replaceFirst("[-]", " "));
			return;
		}

		final String listedValue = listedValue(s);
		if (listedValue != null) {
			final JsonArray array = getForceArray(indent);
			array.add(listedValue);
			return;
		}

		final JsonObject working = getWorking(indent);
		if (working == null) {
			System.err.println("ERROR: ignoring " + s);
			return;
		}

		final String[] nameAndValue = nameAndValue(s);
		if (nameAndValue != null) {
			final String name = nameAndValue[0];
			final String data = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(nameAndValue[1], "\"");
			working.add(name, data);
			return;
		}

		final String nameOnly = nameOnly(s);
		if (nameOnly != null) {
			working.add(nameOnly, new JsonObject());
			return;
		}

		throw new UnsupportedOperationException(s);
	}

	private JsonArray getForceArray(int indent) {
		while (getLastIndent() > indent - 1)
			pendingIndents.remove(pendingIndents.size() - 1);

		final JsonObject last = search(result, pendingIndents.size());
		final String field = last.names().get(last.size() - 1);
		if (last.get(field) instanceof JsonArray == false) {
			last.set(field, new JsonArray());
		}
		return (JsonArray) last.get(field);
	}

	private void muteToArray(int indent) {
		while (getLastIndent() > indent)
			pendingIndents.remove(pendingIndents.size() - 1);

		final JsonObject last = search(result, pendingIndents.size());
		final String field = last.names().get(last.size() - 1);
		if (last.get(field) instanceof JsonArray == false) {
			last.set(field, new JsonArray());
		} else {
			((JsonArray) last.get(field)).add(new JsonObject());
		}
	}

	private boolean isList(String s) {
		return s.trim().startsWith("-");
	}

	private boolean isListStrict(String s) {
		return s.trim().equals("-");
	}

	private int getLastIndent() {
		return pendingIndents.get(pendingIndents.size() - 1);
	}

	private JsonObject getWorking(int indent) {
		if (indent > getLastIndent()) {
			pendingIndents.add(indent);
			return search(result, pendingIndents.size());
		}
		if (indent == getLastIndent()) {
			return search(result, pendingIndents.size());
		}
		final int idx = pendingIndents.indexOf(indent);
		if (idx == -1) {
			return null;
		}

		while (pendingIndents.size() > idx + 1)
			pendingIndents.remove(pendingIndents.size() - 1);

		return search(result, pendingIndents.size());
	}

	private static JsonObject search(JsonObject current, int size) {
		if (size <= 1) {
			return current;
		}
		final String last = current.names().get(current.size() - 1);
		// System.err.println("last=" + last);
		JsonValue tmp = current.get(last);
		if (tmp instanceof JsonArray) {
			JsonArray array = (JsonArray) tmp;
			if (array.size() == 0) {
				tmp = new JsonObject();
				array.add(tmp);
			} else {
				tmp = array.get(array.size() - 1);
			}
		}
		return search((JsonObject) tmp, size - 1);
	}

	private int getIndent(String s) {
		int indent = 0;
		for (int i = 0; i < s.length(); i++) {
			final char ch = s.charAt(i);
			if (ch == ' ' || ch == '\t') {
				indent++;
			} else {
				return indent;
			}
		}
		return 0;
	}

}