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
 * 
 */
package net.sourceforge.plantuml.teavm;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.utils.LineLocationImpl;

/**
 * Turns the raw content of the browser editor into the source lines given to
 * the preprocessor, with the same tolerance as editor.plantuml.com (#2907).
 *
 * <ul>
 * <li>Trailing blank lines are removed.</li>
 * <li>If a line starts with {@code @startxxx}, everything before it is
 * ignored (blank lines, but also any text pasted before the diagram, as when
 * reading a file), and the {@code @startxxx} line itself is trimmed. If no
 * {@code @endxxx} line follows, the matching one is added.</li>
 * <li>If there is no {@code @startxxx} line at all, leading blank lines are
 * removed and the text is wrapped in {@code @startuml} / {@code @enduml}
 * (the {@code @enduml} is not added if the text already has one).</li>
 * </ul>
 *
 * Each kept line keeps its position in the editor, so that error messages
 * still point to the right line. Added lines are placed just before the first
 * line or just after the last line.
 */
public class TextareaSource {

	private static final Pattern START = Pattern.compile("^@start([A-Za-z0-9]*)");

	private TextareaSource() {
	}

	public static List<StringLocated> create(String[] lines, String description) {
		final LineLocationImpl beforeFirst = new LineLocationImpl(description, null);
		final List<StringLocated> all = new ArrayList<>();
		final List<LineLocationImpl> locations = new ArrayList<>();
		LineLocationImpl location = beforeFirst;
		for (String s : lines) {
			location = location.oneLineRead();
			all.add(new StringLocated(s, location));
			locations.add(location);
		}

		while (all.size() > 0 && isBlank(all.get(all.size() - 1))) {
			all.remove(all.size() - 1);
			locations.remove(locations.size() - 1);
		}

		final int start = indexOfStart(all);
		final String type;
		final List<StringLocated> result = new ArrayList<>();
		if (start == -1) {
			type = "uml";
			int first = 0;
			while (first < all.size() && isBlank(all.get(first)))
				first++;

			result.add(new StringLocated("@startuml", beforeFirst));
			result.addAll(all.subList(first, all.size()));
		} else {
			final Matcher m = START.matcher(trim(all.get(start)));
			m.find();
			type = m.group(1).length() == 0 ? "uml" : m.group(1);
			// The @start line itself is trimmed: UmlSource does not accept a
			// non-breaking space before it (pasted from a web page)
			result.add(all.get(start).getTrimmed());
			result.addAll(all.subList(start + 1, all.size()));
		}

		if (hasEnd(result) == false) {
			final LineLocationImpl last = locations.isEmpty() ? beforeFirst : locations.get(locations.size() - 1);
			result.add(new StringLocated("@end" + type, last.oneLineRead()));
		}

		return result;
	}

	private static int indexOfStart(List<StringLocated> lines) {
		for (int i = 0; i < lines.size(); i++)
			if (trim(lines.get(i)).startsWith("@start"))
				return i;

		return -1;
	}

	private static boolean hasEnd(List<StringLocated> lines) {
		for (StringLocated s : lines)
			if (trim(s).startsWith("@end"))
				return true;

		return false;
	}

	private static boolean isBlank(StringLocated s) {
		return trim(s).isEmpty();
	}

	private static String trim(StringLocated s) {
		return s.getTrimmed().getString();
	}

}
