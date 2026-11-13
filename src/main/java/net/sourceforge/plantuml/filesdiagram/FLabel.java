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
package net.sourceforge.plantuml.filesdiagram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.klimt.color.HColorSet;

/**
 * The text of one entry once its tree prefix (indentation, box-drawing
 * characters...) has been removed: a name followed by optional annotations, in
 * any order.
 *
 * <pre>
 * App.java :::highlight &lt;&lt;main&gt;&gt; #yellow icon(&lt;:coffee:&gt;) ## the entry point
 * "my file ## not a description.txt" ## a description
 * </pre>
 *
 * <ul>
 * <li>{@code :::cls} or {@code <<cls>>}: a stereotype, styled by
 * {@code .cls} in {@code <style>}</li>
 * <li>{@code #color}: a background color for the row</li>
 * <li>{@code icon(...)}: an explicit icon, see {@link FilesIcons}</li>
 * <li>{@code ## text}: an inline description</li>
 * </ul>
 * The name may be quoted, so that it can contain one of these markers.
 */
final class FLabel {

	private static final Pattern STEREO1 = Pattern.compile("^:::([^\\s:]+)");
	private static final Pattern STEREO2 = Pattern.compile("^<<([^<>]+)>>");
	private static final Pattern ICON = Pattern.compile("^icon\\(([^)]*)\\)");
	private static final Pattern COLOR = Pattern.compile("^(#[0-9a-zA-Z]+)(?=\\s|$)");
	private static final Pattern TRAILING = Pattern
			.compile("\\s+(:::[^\\s:]+|<<[^<>]+>>|icon\\([^)]*\\)|#[0-9a-zA-Z]+)$");

	/**
	 * Where an annotation may start, when the name is not quoted. {@code :::} and
	 * {@code <<} may stick to the name, the others need a blank before them.
	 */
	private static final Pattern ANNOTATION_START = Pattern
			.compile(":::[^\\s:]|<<[^<>]+>>|\\s##|\\sicon\\(|\\s#[0-9a-zA-Z]+(?=\\s|$)");

	private final String name;
	private final String description;
	private final List<String> stereotypes;
	private final String color;
	private final String icon;

	private FLabel(String name, String description, List<String> stereotypes, String color, String icon) {
		this.name = name;
		this.description = description;
		this.stereotypes = Collections.unmodifiableList(stereotypes);
		this.color = color;
		this.icon = icon;
	}

	public static FLabel parse(String text) {
		text = text.trim();
		String name;
		String rest;
		if (text.startsWith("\"") && text.indexOf('"', 1) > 0) {
			final int end = text.indexOf('"', 1);
			name = text.substring(1, end);
			rest = text.substring(end + 1);
		} else {
			final int start = annotationStart(" " + text);
			if (start == -1) {
				name = text;
				rest = "";
			} else {
				name = text.substring(0, start).trim();
				rest = text.substring(start);
			}
		}

		String description = null;
		String color = null;
		String icon = null;
		final List<String> stereotypes = new ArrayList<>();
		final StringBuilder unknown = new StringBuilder();

		while (true) {
			rest = rest.trim();
			if (rest.length() == 0)
				break;
			if (rest.startsWith("##")) {
				// The description runs to the end of the line, but annotations written after
				// it still count, as in "App.java ## the entry point :::highlight"
				String desc = rest.substring(2).trim();
				Matcher m;
				while ((m = TRAILING.matcher(desc)).find()) {
					final String tail = m.group(1);
					if (tail.startsWith("#") && isColor(tail) == false)
						break;
					desc = desc.substring(0, m.start()).trim();
					if (tail.startsWith(":::"))
						stereotypes.add(0, tail.substring(3));
					else if (tail.startsWith("<<"))
						stereotypes.add(0, tail.substring(2, tail.length() - 2).trim());
					else if (tail.startsWith("icon("))
						icon = tail.substring(5, tail.length() - 1).trim();
					else
						color = tail;
				}
				description = desc;
				break;
			}
			Matcher m = STEREO1.matcher(rest);
			if (m.find()) {
				stereotypes.add(m.group(1));
				rest = rest.substring(m.end());
				continue;
			}
			m = STEREO2.matcher(rest);
			if (m.find()) {
				stereotypes.add(m.group(1).trim());
				rest = rest.substring(m.end());
				continue;
			}
			m = ICON.matcher(rest);
			if (m.find()) {
				icon = m.group(1).trim();
				rest = rest.substring(m.end());
				continue;
			}
			m = COLOR.matcher(rest);
			if (m.find() && isColor(m.group(1))) {
				color = m.group(1);
				rest = rest.substring(m.end());
				continue;
			}
			// Not an annotation after all: it belongs to the name
			final int space = firstBlank(rest);
			final String word = space == -1 ? rest : rest.substring(0, space);
			if (unknown.length() > 0)
				unknown.append(' ');
			unknown.append(word);
			rest = space == -1 ? "" : rest.substring(space);
		}
		if (unknown.length() > 0)
			name = (name + " " + unknown).trim();

		return new FLabel(name, description, stereotypes, color, icon);
	}

	private static int firstBlank(String s) {
		for (int i = 0; i < s.length(); i++)
			if (Character.isWhitespace(s.charAt(i)))
				return i;
		return -1;
	}

	/**
	 * Position of the first annotation in {@code " " + text}, given back as a
	 * position in {@code text}, or -1.
	 */
	private static int annotationStart(String spaced) {
		final Matcher m = ANNOTATION_START.matcher(spaced);
		int from = 0;
		while (m.find(from)) {
			final String found = m.group();
			final int start = Character.isWhitespace(found.charAt(0)) ? m.start() + 1 : m.start();
			if (found.trim().startsWith("#") && found.trim().startsWith("##") == false
					&& isColor(found.trim()) == false) {
				from = m.start() + 1;
				continue;
			}
			// "start" counts the extra leading blank
			return Math.max(0, start - 1);
		}
		return -1;
	}

	/**
	 * True for {@code #red} or {@code #FFC107}, but not for {@code #12} or
	 * {@code #todo}: a word that only looks like a color stays in the name.
	 */
	static boolean isColor(String s) {
		if (s.startsWith("#") == false || s.length() < 2)
			return false;
		final String value = s.substring(1);
		if (value.matches("[0-9a-fA-F]{3}|[0-9a-fA-F]{4}|[0-9a-fA-F]{6}|[0-9a-fA-F]{8}"))
			return true;
		if (value.matches("[a-zA-Z]+"))
			return HColorSet.instance().getColorOrNull(value) != null;
		return false;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public List<String> getStereotypes() {
		return stereotypes;
	}

	public String getColor() {
		return color;
	}

	public String getIcon() {
		return icon;
	}

}
