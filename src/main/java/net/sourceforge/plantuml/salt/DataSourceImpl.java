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
package net.sourceforge.plantuml.salt;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.regex.Matcher2;
import net.sourceforge.plantuml.regex.Pattern2;

public class DataSourceImpl implements DataSource {

	private static final Pattern2 STRUCTURED_BLOCK_START_PATTERN = Pattern2.cmpile("\\{(?:[-+^#!*/]|S-|SI|S)?");

	// "|" and "}" are hard column/group delimiters for the tokenizer below, and
	// a bare "{" is always read as the start of a nested structured block - none
	// of that has any notion of escaping, so there was previously no way to get
	// a literal "|", "{" or "}" into a cell's content at all (see issues #2731,
	// #2732: a pipe inside a salt table cell always splits a new column, and a
	// brace - e.g. in a link's "[[url{tooltip}]]" - is always eaten as
	// structure). A backslash-escaped "\|", "\{" or "\}" is shielded from that
	// splitting by swapping it out for a placeholder from the Unicode Private
	// Use Area (so it can't collide with real diagram source) before
	// tokenizing, then swapped back to the literal character once splitting is
	// done. This intentionally does not also support escaping the backslash
	// itself ("\\|"); that is a separate, rarer need left alone here.
	private static final String ESCAPED_PIPE = "\uE000";
	private static final String ESCAPED_OPEN = "\uE001";
	private static final String ESCAPED_CLOSE = "\uE002";

	private int i = 0;
	private final List<Terminated<String>> data = new ArrayList<Terminated<String>>();

	public DataSourceImpl(List<String> data) {

		for (String rawLine : data) {
			final String s = rawLine.replace("\\|", ESCAPED_PIPE).replace("\\{", ESCAPED_OPEN).replace("\\}",
					ESCAPED_CLOSE);
			final StringTokenizer st = new StringTokenizer(s, "|}", true);
			while (st.hasMoreTokens()) {
				final String token = StringUtils.trin(st.nextToken());
				if (token.equals("|"))
					continue;

				final Terminator terminator = st.hasMoreTokens() ? Terminator.NEWCOL : Terminator.NEWLINE;
				final Matcher2 m = STRUCTURED_BLOCK_START_PATTERN.matcher(token, 0);
				final boolean found = m.find();
				if (found == false) {
					addInternal(token, terminator);
					continue;
				}

				int lastStart = 0;
				int end = 0;
				do {
					final int start = m.start();
					if (start > lastStart)
						addInternal(token.substring(lastStart, start), Terminator.NEWCOL);

					end = m.end();
					final Terminator t = end == token.length() ? terminator : Terminator.NEWCOL;
					addInternal(token.substring(start, end), t);
					lastStart = end;
				} while (m.find());

				if (end < token.length())
					addInternal(token.substring(end), terminator);

			}
		}
	}

	private void addInternal(String s, Terminator t) {
		s = StringUtils.trin(s);
		s = s.replace(ESCAPED_PIPE, "|").replace(ESCAPED_OPEN, "{").replace(ESCAPED_CLOSE, "}");
		if (s.length() > 0)
			data.add(new Terminated<>(s, t));

	}

	public Terminated<String> peek(int nb) {
		return data.get(i + nb);
	}

	public boolean hasNext() {
		return i < data.size();
	}

	public Terminated<String> next() {
		final Terminated<String> result = data.get(i);
		i++;
		return result;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	public String toString() {
		return super.toString() + " " + (hasNext() ? peek(0) : "$$$");
	}

}
