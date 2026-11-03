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
package net.sourceforge.plantuml.tim;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.text.TLineType;

/**
 * Groups the Python lines of a diagram into chunks, before the legacy
 * preprocessor looks at them (one single pass afterwards).
 * <ul>
 * <li>A <code>!python</code> ... <code>!endpy</code> block is taken as it is:
 * the legacy preprocessor is not applied inside, so <code>'</code> is a Python
 * string delimiter, not a PlantUML comment.</li>
 * <li>A run of consecutive <code>!</code> lines of type
 * {@link TLineType#PYTHON} is one chunk; the <code>!</code> is a one-column
 * gutter.</li>
 * </ul>
 * A chunk is one {@link StringLocated} whose text is
 * {@link TLineType#PYTHON_MARKER} followed by the Python code.
 */
public final class PythonChunks {

	private static final Pattern START = Pattern.compile("^\\s*!python\\s*$");
	private static final Pattern END = Pattern.compile("^\\s*!endpy\\s*$");

	private PythonChunks() {
	}

	public static List<StringLocated> collapse(List<StringLocated> body) {
		// Fast path: a diagram without Python pays nothing.
		boolean found = false;
		for (StringLocated sl : body)
			if (sl.containsExclamationMark()) {
				found = true;
				break;
			}
		if (found == false)
			return body;

		final List<StringLocated> result = new ArrayList<>();
		int i = 0;
		while (i < body.size()) {
			final StringLocated first = body.get(i);
			final String s = first.getString();
			if (START.matcher(s).matches()) {
				final StringBuilder code = new StringBuilder();
				i++;
				while (i < body.size() && END.matcher(body.get(i).getString()).matches() == false) {
					code.append(body.get(i).getString()).append('\n');
					i++;
				}
				i++; // skips !endpy (a missing one ends the block at the end of the text)
				result.add(chunk(first, code));
			} else if (isGutterLine(first)) {
				final StringBuilder code = new StringBuilder();
				while (i < body.size() && (isGutterLine(body.get(i)) || isIndentedGutterLine(body.get(i)))) {
					code.append(gutter(body.get(i).getString())).append('\n');
					i++;
				}
				result.add(chunk(first, code));
			} else {
				result.add(first);
				i++;
			}
		}
		return result;
	}

	private static boolean isGutterLine(StringLocated sl) {
		return sl.getString().indexOf('\n') == -1 && sl.getType() == TLineType.PYTHON;
	}

	/** "!    return x": inside a run, an indented line after the gutter continues the block. */
	private static boolean isIndentedGutterLine(StringLocated sl) {
		final String s = sl.getString();
		if (s.indexOf('\n') != -1 || s.length() < 2 || s.charAt(0) != '!' || s.charAt(1) != ' ')
			return false;
		return sl.getType() == TLineType.PLAIN;
	}

	/** Removes the one-column gutter: the text after the first <code>!</code>. */
	private static String gutter(String s) {
		return s.substring(s.indexOf('!') + 1);
	}

	private static StringLocated chunk(StringLocated first, StringBuilder code) {
		return new StringLocated(TLineType.PYTHON_MARKER + code, first.getLocation(), first.getPreprocessorError());
	}

	/** The Python code of a chunk. */
	public static String code(StringLocated chunk) {
		final String s = chunk.getString();
		if (s.startsWith(TLineType.PYTHON_MARKER))
			return s.substring(TLineType.PYTHON_MARKER.length());
		return gutter(s) + "\n";
	}
}
