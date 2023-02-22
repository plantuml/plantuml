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
package net.sourceforge.plantuml.yaml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YamlLines implements Iterable<String> {

	public static final String KEY = "([^:]+)";

	private List<String> lines = new ArrayList<>();

	public YamlLines(List<String> rawLines) {
		for (String s : rawLines) {
			if (s.startsWith("#"))
				continue;

			s = removeDiese(s);
			if (s.trim().length() == 0)
				continue;
			lines.add(s);
		}
		mergeMultiline();
		manageSpaceTwoPoints();
		manageList();
		final int startingEmptyCols = startingEmptyCols();
		removeFirstCols(startingEmptyCols);
	}

	private void manageSpaceTwoPoints() {
		for (ListIterator<String> it = lines.listIterator(); it.hasNext();) {
			String s = it.next();
			if (s.contains("\"") == false && s.contains("'") == false && s.contains(":")
					&& s.indexOf(':') == s.lastIndexOf(':') && s.contains(": ") == false) {
				s = s.replace(":", ": ");
				it.set(s);
			}
		}

	}

	private String removeDiese(String s) {
		final int idx = s.indexOf(" #");
		if (idx == -1)
			return s;

		return s.substring(0, idx);
	}

	private void manageList() {
		final List<String> result = new ArrayList<>();
		for (String s : lines) {
			final Pattern p1 = Pattern.compile("^(\\s*[-])(\\s*\\S.*)$");
			final Matcher m1 = p1.matcher(s);
			if (s.contains(": ") && m1.matches()) {
				result.add(m1.group(1));
				result.add(s.replaceFirst("[-]", " "));
			} else if (m1.matches()) {
				result.add(" " + s);
			} else {
				result.add(s);
			}

		}
		this.lines = result;
	}

	private void removeFirstCols(int startingEmptyCols) {
		if (startingEmptyCols == 0)
			return;

		for (ListIterator<String> it = lines.listIterator(); it.hasNext();) {
			final String s = it.next().substring(startingEmptyCols);
			it.set(s);
		}
	}

	private int startingEmptyCols() {
		int result = Integer.MAX_VALUE;
		for (String s : lines) {
			result = Math.min(result, startingSpaces(s));
			if (result == 0)
				return 0;
		}
		return result;
	}

	private static int startingSpaces(String s) {
		final Pattern p1 = Pattern.compile("^(\\s*).*");
		final Matcher m1 = p1.matcher(s);
		if (m1.matches())
			return m1.group(1).length();

		return 0;
	}

	private void mergeMultiline() {
		final List<String> result = new ArrayList<>();
		for (int i = 0; i < lines.size(); i++) {
			final String init = isMultilineStart(i);
			if (init != null) {
				final StringBuilder sb = new StringBuilder(init);
				while (i + 1 < lines.size() && textOnly(lines.get(i + 1))) {
					sb.append(" " + lines.get(i + 1).trim());
					i++;
				}
				result.add(sb.toString());
			} else {
				result.add(lines.get(i));
			}
		}
		this.lines = result;
	}

	private String isMultilineStart(int i) {
		if (nameOnly(lines.get(i)) != null && textOnly(lines.get(i + 1))) {
			final int idx = lines.get(i).indexOf(':');
			return lines.get(i).substring(0, idx + 1);
		}
		return null;
	}

	public static String nameOnly(String s) {
		final Pattern p1 = Pattern.compile("^\\s*" + KEY + "\\s*:\\s*[|>]?\\s*$");
		final Matcher m1 = p1.matcher(s);
		if (m1.matches()) {
			final String name = m1.group(1);
			return name;
		}
		return null;
	}

	private boolean textOnly(String s) {
		if (isList(s))
			return false;
		return s.indexOf(':') == -1;
	}

	private boolean isList(String s) {
		return s.trim().startsWith("-");
	}

	public Iterator<String> iterator() {
		return Collections.unmodifiableList(lines).iterator();
	}

}