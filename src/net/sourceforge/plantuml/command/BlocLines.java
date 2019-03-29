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
package net.sourceforge.plantuml.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.BackSlash;
import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.cucadiagram.Display;

public class BlocLines implements Iterable<StringLocated> {

	private List<StringLocated> lines;

	private void check() {
		// for (CharSequence s : lines) {
		// // if (s == null) {
		// // continue;
		// // }
		// if (s instanceof String) {
		// continue;
		// }
		// // if (s instanceof Stereotype) {
		// // continue;
		// // }
		// if (s instanceof CharSequence2) {
		// continue;
		// }
		// // if (s instanceof MessageNumber) {
		// // continue;
		// // }
		// // if (s instanceof EmbeddedDiagram) {
		// // continue;
		// // }
		// System.err.println("PB2=" + s);
		// System.err.println("PB=" + s.getClass());
		// Thread.dumpStack();
		// System.exit(0);
		// }
	}

	@Override
	public String toString() {
		return lines.toString();
	}

	public static BlocLines load(File f, LineLocation location) throws IOException {
		final List<StringLocated> result = new ArrayList<StringLocated>();
		final BufferedReader br = new BufferedReader(new FileReader(f));
		String s;
		while ((s = br.readLine()) != null) {
			result.add(new StringLocated(s, location));
		}
		br.close();
		return new BlocLines(result);
	}

	private BlocLines(List<StringLocated> lines) {
		this.lines = Collections.unmodifiableList(lines);
		this.check();
	}

	public Display toDisplay() {
		return Display.createFoo(lines);
	}

	public static BlocLines single2(StringLocated single) {
		final List<StringLocated> result = new ArrayList<StringLocated>();
		result.add(single);
		return new BlocLines(result);
	}

	public static BlocLines singleString(String single) {
		final List<StringLocated> result = new ArrayList<StringLocated>();
		result.add(new StringLocated(single, null));
		return new BlocLines(result);
	}

	public static BlocLines getWithNewlines(String s) {
		final List<StringLocated> result = new ArrayList<StringLocated>();
		for (String cs : BackSlash.getWithNewlines(s)) {
			result.add(new StringLocated(cs, null));
		}
		return new BlocLines(result);
	}

	public BlocLines() {
		this(new ArrayList<StringLocated>());
	}

	public BlocLines add2(StringLocated s) {
		final List<StringLocated> copy = new ArrayList<StringLocated>(lines);
		copy.add(s);
		return new BlocLines(copy);
	}

	public BlocLines addString(String s) {
		final List<StringLocated> copy = new ArrayList<StringLocated>(lines);
		copy.add(new StringLocated(s, null));
		return new BlocLines(copy);
	}

	// public List<CharSequence2> getLines() {
	// return lines2;
	// }

	public List<String> getLinesAsStringForSprite() {
		final List<String> result = new ArrayList<String>();
		for (StringLocated s : lines) {
			result.add(s.getString());
		}
		return result;
	}

	public int size() {
		return lines.size();
	}

	public StringLocated get499(int i) {
		return lines.get(i);
	}

	public StringLocated getFirst499() {
		if (lines.size() == 0) {
			return null;
		}
		return lines.get(0);
	}

	public StringLocated getLast499() {
		return lines.get(lines.size() - 1);
	}

	public BlocLines cleanList2(MultilinesStrategy strategy) {
		final List<StringLocated> copy = new ArrayList<StringLocated>(lines);
		strategy.cleanList(copy);
		return new BlocLines(copy);
	}

	public BlocLines trim(boolean removeEmptyLines) {
		final List<StringLocated> copy = new ArrayList<StringLocated>(lines);
		for (int i = 0; i < copy.size(); i++) {
			final StringLocated s = copy.get(i);
			copy.set(i, new StringLocated(s.getStringTrimmed(), s.getLocation()));
		}
		if (removeEmptyLines) {
			for (final Iterator<StringLocated> it = copy.iterator(); it.hasNext();) {
				if (it.next().getString().length() == 0) {
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
		final List<StringLocated> copy = new ArrayList<StringLocated>(lines);
		do {
			for (int i = 0; i < copy.size(); i++) {
				final StringLocated s = copy.get(i);
				if (s.getString().length() > 0) {
					copy.set(i, s.sub(1, s.getString().length()));
				}
			}
		} while (firstColumnRemovable(copy));
		return new BlocLines(copy);
	}

	private static boolean firstColumnRemovable(List<StringLocated> data) {
		boolean allEmpty = true;
		for (StringLocated s : data) {
			if (s.getString().length() == 0) {
				continue;
			}
			allEmpty = false;
			final char c = s.getString().charAt(0);
			if (c != ' ' && c != '\t') {
				return false;
			}
		}
		return allEmpty == false;
	}

	public char getLastChar() {
		final StringLocated s = lines.get(lines.size() - 1);
		return s.getString().charAt(s.getString().length() - 1);
	}

	public BlocLines removeStartingAndEnding2(String data) {
		if (lines.size() == 0) {
			return this;
		}
		final List<StringLocated> copy = new ArrayList<StringLocated>(lines);
		copy.set(0, new StringLocated(data, null));
		final int n = copy.size() - 1;
		final StringLocated s = copy.get(n);
		copy.set(n, s.sub(0, s.getString().length() - 1));
		return new BlocLines(copy);
	}

	public BlocLines toSingleLineWithHiddenNewLine() {
		final StringBuilder sb = new StringBuilder();
		for (StringLocated line : lines) {
			sb.append(line.getString());
			sb.append(BackSlash.hiddenNewLine());
		}
		return singleString(sb.substring(0, sb.length() - 1).toString());
	}

	public BlocLines trimSmart(int referenceLine) {
		if (lines.size() <= referenceLine) {
			return this;
		}
		final List<StringLocated> copy = new ArrayList<StringLocated>(lines);
		final int nbStartingSpace = nbStartingSpace(copy.get(referenceLine).getString());
		for (int i = referenceLine; i < copy.size(); i++) {
			final StringLocated s = copy.get(i);
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

	private static StringLocated removeStartingSpaces(StringLocated arg, int nbStartingSpace) {
		if (arg.getString().length() == 0) {
			return arg;
		}
		int i = 0;
		while (i < nbStartingSpace && i < arg.getString().length() && isSpaceOrTab(arg.getString().charAt(i))) {
			i++;
		}
		if (i == 0) {
			return arg;
		}
		return arg.sub(i, arg.getString().length());
	}

	public BlocLines subExtract(int margeStart, int margeEnd) {
		List<StringLocated> copy = new ArrayList<StringLocated>(lines);
		copy = copy.subList(margeStart, copy.size() - margeEnd);
		return new BlocLines(copy);
	}

	public BlocLines subList(int start, int end) {
		return new BlocLines(lines.subList(start, end));
	}

	public Iterator<StringLocated> iterator() {
		return lines.iterator();
	}

	public BlocLines eventuallyMoveBracket() {
		if (size() < 2) {
			return this;
		}
		final String first = getFirst499().getStringTrimmed();
		final String second = get499(1).getStringTrimmed();
		if (first.endsWith("{") == false && second.equals("{")) {
			final String vline = first + " {";
			final List<StringLocated> result = new ArrayList<StringLocated>();
			result.add(new StringLocated(vline, null));
			result.addAll(this.lines.subList(2, this.lines.size()));
			return new BlocLines(result);
		}
		return this;
	}

}
