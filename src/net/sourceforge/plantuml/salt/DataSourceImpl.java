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
 *
 */
package net.sourceforge.plantuml.salt;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;

public class DataSourceImpl implements DataSource {

	private int i = 0;
	private final List<Terminated<String>> data = new ArrayList<Terminated<String>>();

	public DataSourceImpl(List<String> data) {
		final Pattern2 p = MyPattern.cmpile("\\{(?:[-+^#!*/]|S-|SI|S)?");

		for (String s : data) {
			final StringTokenizer st = new StringTokenizer(s, "|}", true);
			while (st.hasMoreTokens()) {
				final String token = StringUtils.trin(st.nextToken());
				if (token.equals("|")) {
					continue;
				}
				final Terminator terminator = st.hasMoreTokens() ? Terminator.NEWCOL : Terminator.NEWLINE;
				final Matcher2 m = p.matcher(token);
				final boolean found = m.find();
				if (found == false) {
					addInternal(token, terminator);
					continue;
				}
				int lastStart = 0;
				int end = 0;
				do {
					final int start = m.start();
					if (start > lastStart) {
						addInternal(token.substring(lastStart, start), Terminator.NEWCOL);
					}
					end = m.end();
					final Terminator t = end == token.length() ? terminator : Terminator.NEWCOL;
					addInternal(token.substring(start, end), t);
					lastStart = end;
				} while (m.find());
				if (end < token.length()) {
					addInternal(token.substring(end), terminator);
				}
			}
		}
	}


	private void addInternal(String s, Terminator t) {
		s = StringUtils.trin(s);
		if (s.length() > 0) {
			data.add(new Terminated<String>(s, t));
		}
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
