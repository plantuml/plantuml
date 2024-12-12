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
package net.sourceforge.plantuml.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.jaws.JawsStrange;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.text.BackSlash;
import net.sourceforge.plantuml.text.StringLocated;

public class BlocLines implements Iterable<StringLocated> {
	// ::remove file when __HAXE__

	private List<StringLocated> lines;

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		for (StringLocated line : lines) {
			sb.append("[");
			sb.append(line);
			sb.append("]");
		}
		return sb.toString();
	}

	public BlocLines expandsJaws5() {
		final List<StringLocated> copy = new ArrayList<>();
		for (StringLocated sl : lines)
			copy.addAll(sl.expandsJaws5());

		return new BlocLines(copy);
	}

	public static BlocLines load(SFile f, LineLocation location) throws IOException {
		final BufferedReader br = f.openBufferedReader();
		if (br == null)
			return null;

		return loadInternal(br, location);
	}

	public static BlocLines load(InputStream is, LineLocation location) throws IOException {
		final BufferedReader br = new BufferedReader(new InputStreamReader(is));
		return loadInternal(br, location);
	}

	public static BlocLines from(List<StringLocated> lines) {
		return new BlocLines(lines);
	}

	private static BlocLines loadInternal(final BufferedReader br, LineLocation location) throws IOException {
		final List<StringLocated> result = new ArrayList<>();
		String s;
		try {
			while ((s = br.readLine()) != null)
				result.add(new StringLocated(s, location));
		} finally {
			br.close();
		}
		return new BlocLines(result);
	}

	private BlocLines(List<StringLocated> lines) {
		this.lines = Collections.unmodifiableList(lines);
	}

	public Display toDisplay() throws NoSuchColorException {
		return Display.createFoo(lines);
	}

	public static BlocLines single(StringLocated single) {
		final List<StringLocated> result = new ArrayList<>();
		result.add(single);
		return new BlocLines(result);
	}

	public static BlocLines singleString(String single) {
		final List<StringLocated> result = new ArrayList<>();
		result.add(new StringLocated(single, null));
		return new BlocLines(result);
	}

	public static BlocLines fromArray(String[] array) {
		final List<StringLocated> result = new ArrayList<>();
		for (String single : array)
			result.add(new StringLocated(single, null));

		return new BlocLines(result);
	}

	public static BlocLines getWithNewlines(String s) {
		final List<StringLocated> result = new ArrayList<>();
		for (String cs : Display.getWithNewlines3(s))
			result.add(new StringLocated(cs, null));

		return new BlocLines(result);
	}

	public static BlocLines create() {
		return new BlocLines(new ArrayList<StringLocated>());
	}

	public BlocLines add(StringLocated s) {
		final List<StringLocated> copy = new ArrayList<>(lines);
		copy.add(s);
		return new BlocLines(copy);
	}

	public BlocLines addString(String s) {
		final List<StringLocated> copy = new ArrayList<>(lines);
		copy.add(new StringLocated(s, null));
		return new BlocLines(copy);
	}

	public List<String> getLinesAsStringForSprite() {
		final List<String> result = new ArrayList<>();
		for (StringLocated s : lines)
			result.add(s.getString());

		return result;
	}

	public int size() {
		return lines.size();
	}

	public StringLocated getAt(int i) {
		return lines.get(i);
	}

	public StringLocated getFirst() {
		if (lines.size() == 0)
			return null;

		return lines.get(0);
	}

	public StringLocated getLast() {
		return lines.get(lines.size() - 1);
	}

	public BlocLines cleanList(MultilinesStrategy strategy) {
		final List<StringLocated> copy = new ArrayList<>(lines);
		strategy.cleanList(copy);
		return new BlocLines(copy);
	}

	public BlocLines trim() {
		final List<StringLocated> copy = new ArrayList<>(lines);
		for (int i = 0; i < copy.size(); i++) {
			final StringLocated s = copy.get(i);
			copy.set(i, s.getTrimmed());
		}
		return new BlocLines(copy);
	}

	public BlocLines removeEmptyLines() {
		final List<StringLocated> copy = new ArrayList<>(lines);
		for (final Iterator<StringLocated> it = copy.iterator(); it.hasNext();)
			if (it.next().getString().length() == 0)
				it.remove();

		return new BlocLines(copy);
	}

//	public BlocLines trimRight() {
//		final List<StringLocated> copy = new ArrayList<>(lines);
//		for (int i = 0; i < copy.size(); i++) {
//			final StringLocated s = copy.get(i);
//			copy.set(i, s.getTrimmedRight());
//		}
//		return new BlocLines(copy);
//	}

	public BlocLines removeEmptyColumns() {
		if (firstColumnRemovable(lines) == false)
			return this;

		final List<StringLocated> copy = new ArrayList<>(lines);
		do {
			for (int i = 0; i < copy.size(); i++) {
				final StringLocated s = copy.get(i);
				if (s.getString().length() > 0)
					copy.set(i, s.substring(1, s.getString().length()));

			}
		} while (firstColumnRemovable(copy));
		return new BlocLines(copy);
	}

	@JawsStrange
	private static boolean firstColumnRemovable(List<StringLocated> data) {
		boolean allEmpty = true;
		for (StringLocated s : data) {
			if (s.getString().length() == 0)
				continue;

			allEmpty = false;
			final char c = s.getString().charAt(0);
			if (c != ' ' && c != '\t')
				return false;

		}
		return allEmpty == false;
	}

	public char getLastChar() {
		final StringLocated s = lines.get(lines.size() - 1);
		return s.getString().charAt(s.getString().length() - 1);
	}

	public BlocLines removeStartingAndEnding(String data, int removeAtEnd) {
		if (lines.size() == 0)
			return this;

		final List<StringLocated> copy = new ArrayList<>(lines);
		copy.set(0, new StringLocated(data, null));
		final int n = copy.size() - 1;
		final StringLocated s = copy.get(n);
		copy.set(n, s.substring(0, s.getString().length() - removeAtEnd));
		return new BlocLines(copy);
	}

	public BlocLines overrideLastLine(String last) {
		if (lines.size() == 0)
			return this;

		final List<StringLocated> copy = new ArrayList<>(lines);
		final int n = copy.size() - 1;
		final StringLocated currentLast = copy.get(n);
		copy.set(n, new StringLocated(last, currentLast.getLocation()));
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
		if (lines.size() <= referenceLine)
			return this;

		final List<StringLocated> copy = new ArrayList<>(lines);
		final int nbStartingSpace = nbStartingSpace(copy.get(referenceLine).getString());
		for (int i = referenceLine; i < copy.size(); i++) {
			final StringLocated s = copy.get(i);
			copy.set(i, removeStartingSpaces(s, nbStartingSpace));
		}
		return new BlocLines(copy);
	}

	private static int nbStartingSpace(CharSequence s) {
		int nb = 0;
		while (nb < s.length() && isSpaceOrTab(s.charAt(nb)))
			nb++;

		return nb;
	}

	private static boolean isSpaceOrTab(char c) {
		return c == ' ' || c == '\t';
	}

	private static StringLocated removeStartingSpaces(StringLocated arg, int nbStartingSpace) {
		if (arg.getString().length() == 0)
			return arg;

		int i = 0;
		while (i < nbStartingSpace && i < arg.getString().length() && isSpaceOrTab(arg.getString().charAt(i)))
			i++;

		if (i == 0)
			return arg;

		return arg.substring(i, arg.getString().length());
	}

	public BlocLines subExtract(int margeStart, int margeEnd) {
		List<StringLocated> copy = new ArrayList<>(lines);
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
		if (size() < 2)
			return this;

		final String first = getFirst().getTrimmed().getString();
		final String second = getAt(1).getTrimmed().getString();
		if (first.endsWith("{") == false && second.equals("{")) {
			final StringLocated vline = getFirst().append(" {");
			final List<StringLocated> result = new ArrayList<>();
			result.add(vline);
			result.addAll(this.lines.subList(2, this.lines.size()));
			return new BlocLines(result);
		}
		return this;
	}

	public BlocLines eventuallyMoveAllEmptyBracket() {
		final List<StringLocated> result = new ArrayList<>();
		for (StringLocated line : lines)
			if (line.getTrimmed().toString().equals("{")) {
				if (result.size() > 0) {
					final int pos = result.size() - 1;
					StringLocated last = result.get(pos);
					result.set(pos, last.append(" {"));
				}
			} else {
				result.add(line);
			}

		return new BlocLines(result);
	}

	public BlocLines removeFewChars(int nb) {
		final List<StringLocated> result = new ArrayList<>();
		result.add(getFirst().substring(nb).getTrimmed());
		result.addAll(this.lines.subList(1, this.lines.size() - 1));
		final StringLocated last = getLast();
		result.add(last.substring(0, last.length() - nb).getTrimmed());
		return new BlocLines(result);
	}

	public CharInspector inspector() {
		return new CharInspectorImpl(this, false);
	}

	public CharInspector inspectorWithNewlines() {
		return new CharInspectorImpl(this, true);
	}

}
