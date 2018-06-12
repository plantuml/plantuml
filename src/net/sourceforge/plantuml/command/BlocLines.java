/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
package net.sourceforge.plantuml.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.BackSlash;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.cucadiagram.Display;

public class BlocLines implements Iterable<CharSequence> {

	private List<CharSequence> lines;

	@Override
	public String toString() {
		return lines.toString();
	}

	private BlocLines(List<? extends CharSequence> lines) {
		this.lines = (List<CharSequence>) Collections.unmodifiableList(lines);
	}

	public Display toDisplay() {
		return Display.create(lines);
	}

	public static BlocLines single(CharSequence single) {
		return new BlocLines(Arrays.asList(single));
	}

	public static BlocLines getWithNewlines(CharSequence s) {
		return new BlocLines(BackSlash.getWithNewlines(s));
	}

	public BlocLines() {
		this(new ArrayList<CharSequence>());
	}

	public BlocLines add2(CharSequence s) {
		final List<CharSequence> copy = new ArrayList<CharSequence>(lines);
		copy.add(s);
		return new BlocLines(copy);
	}

	public List<CharSequence> getLines() {
		return lines;
	}

	public int size() {
		return lines.size();
	}

	public CharSequence get499(int i) {
		return lines.get(i);
	}

	public CharSequence getFirst499() {
		if (lines.size() == 0) {
			return null;
		}
		return lines.get(0);
	}

	public CharSequence getLast499() {
		return lines.get(lines.size() - 1);
	}

	public BlocLines cleanList2(MultilinesStrategy strategy) {
		final List<CharSequence> copy = new ArrayList<CharSequence>(lines);
		strategy.cleanList(copy);
		return new BlocLines(copy);
	}

	public BlocLines trim(boolean removeEmptyLines) {
		final List<CharSequence> copy = new ArrayList<CharSequence>(lines);
		for (int i = 0; i < copy.size(); i++) {
			final CharSequence s = copy.get(i);
			copy.set(i, StringUtils.trin(s));
		}
		if (removeEmptyLines) {
			for (final Iterator<CharSequence> it = copy.iterator(); it.hasNext();) {
				if (it.next().length() == 0) {
					it.remove();
				}
			}
		}
		return new BlocLines(copy);
	}

	public BlocLines removeEmptyColumns() {
		if (firstColumnRemovable(lines) == false) {
			return this;
		}
		final List<CharSequence> copy = new ArrayList<CharSequence>(lines);
		do {
			for (int i = 0; i < copy.size(); i++) {
				final CharSequence s = copy.get(i);
				if (s.length() > 0) {
					copy.set(i, s.subSequence(1, s.length()));
				}
			}
		} while (firstColumnRemovable(copy));
		return new BlocLines(copy);
	}

	private static boolean firstColumnRemovable(List<CharSequence> data) {
		boolean allEmpty = true;
		for (CharSequence s : data) {
			if (s.length() == 0) {
				continue;
			}
			allEmpty = false;
			final char c = s.charAt(0);
			if (c != ' ' && c != '\t') {
				return false;
			}
		}
		return allEmpty == false;
	}

	public char getLastChar() {
		final CharSequence s = lines.get(lines.size() - 1);
		return s.charAt(s.length() - 1);
	}

	public BlocLines removeStartingAndEnding2(String data) {
		if (lines.size() == 0) {
			return this;
		}
		final List<CharSequence> copy = new ArrayList<CharSequence>(lines);
		copy.set(0, data);
		final int n = copy.size() - 1;
		final CharSequence s = copy.get(n);
		copy.set(n, s.subSequence(0, s.length() - 1));
		return new BlocLines(copy);
	}

	public BlocLines toSingleLineWithHiddenNewLine() {
		final StringBuilder sb = new StringBuilder();
		for (CharSequence line : lines) {
			sb.append(line);
			sb.append(BackSlash.hiddenNewLine());
		}
		return single(sb.substring(0, sb.length() - 1));
	}

	public BlocLines trimSmart(int referenceLine) {
		if (lines.size() <= referenceLine) {
			return this;
		}
		final List<CharSequence> copy = new ArrayList<CharSequence>(lines);
		final int nbStartingSpace = nbStartingSpace(copy.get(referenceLine));
		for (int i = referenceLine; i < copy.size(); i++) {
			final CharSequence s = copy.get(i);
			copy.set(i, removeStartingSpaces(s, nbStartingSpace));
		}
		return new BlocLines(copy);
	}

	private static int nbStartingSpace(CharSequence s) {
		int nb = 0;
		while (nb < s.length() && isSpaceOrTab(s.charAt(nb))) {
			nb++;
		}
		return nb;
	}

	private static boolean isSpaceOrTab(char c) {
		return c == ' ' || c == '\t';
	}

	private static CharSequence removeStartingSpaces(CharSequence arg, int nbStartingSpace) {
		if (arg.length() == 0) {
			return arg;
		}
		int i = 0;
		while (i < nbStartingSpace && i < arg.length() && isSpaceOrTab(arg.charAt(i))) {
			i++;
		}
		if (i == 0) {
			return arg;
		}
		return arg.subSequence(i, arg.length());
	}

	public BlocLines subExtract(int start, int end) {
		List<CharSequence> copy = new ArrayList<CharSequence>(lines);
		copy = copy.subList(start, copy.size() - end);
		return new BlocLines(copy);
	}

	public Iterator<CharSequence> iterator() {
		return lines.iterator();
	}

	public BlocLines eventuallyMoveBracket() {
		if (size() < 2) {
			return this;
		}
		final String first = StringUtils.trin(getFirst499());
		final String second = StringUtils.trin(get499(1));
		if (first.endsWith("{") == false && second.equals("{")) {
			final String vline = first + " {";
			final List<CharSequence> result = new ArrayList<CharSequence>();
			result.add(vline);
			result.addAll(this.lines.subList(2, this.lines.size()));
			return new BlocLines(result);
		}
		return this;
	}
}
