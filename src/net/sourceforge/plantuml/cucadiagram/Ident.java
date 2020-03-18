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
package net.sourceforge.plantuml.cucadiagram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.cucadiagram.entity.EntityFactory;

public class Ident implements Code {

	private final List<String> parts;

	private Ident(List<String> parts) {
		this.parts = parts;
	}

	@Override
	public String toString() {
		return parts.toString();
	}

	public boolean startsWith(Ident other) {
		if (other.parts.size() > this.parts.size()) {
			return false;
		}
		for (int i = 0; i < other.parts.size(); i++) {
			if (other.parts.get(i).equals(this.parts.get(i)) == false) {
				return false;
			}
		}
		return true;
	}

	public String forXmi() {
		final StringBuilder sb = new StringBuilder();
		for (String s : parts) {
			if (sb.length() > 0) {
				sb.append(".");
			}
			sb.append(s);
		}
		return sb.toString();
	}

	public Ident add(Ident added) {
		final List<String> copy = new ArrayList<String>(parts);
		copy.addAll(added.parts);
		return new Ident(copy);
	}

	public static Ident empty() {
		return new Ident(Collections.<String>emptyList());
	}

	public String getLast() {
		if (parts.size() == 0) {
			return "";
		}
		return parts.get(parts.size() - 1);
	}

	public Code toCode(CucaDiagram diagram) {
		if (diagram.V1972())
			return this;
		return CodeImpl.of(getLast());
	}

	public Ident eventuallyRemoveStartingAndEndingDoubleQuote(String format) {
		final List<String> copy = new ArrayList<String>(parts);
		final int pos = copy.size() - 1;
		copy.set(pos, StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(copy.get(pos), format));
		return new Ident(copy);
	}

	public Ident removeStartingParenthesis() {
		final List<String> copy = new ArrayList<String>(parts);
		final int pos = copy.size() - 1;
		final String last = copy.get(pos);
		if (last.startsWith("()") == false) {
			throw new IllegalStateException();
		}
		copy.set(pos, StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(last.substring(2).trim()));
		return new Ident(copy);
	}

	public Ident addSuffix(String suffix) {
		final List<String> copy = new ArrayList<String>(parts);
		final int pos = copy.size() - 1;
		copy.set(pos, copy.get(pos) + suffix);
		return new Ident(copy);
		// return new Code(fullName + suffix);
	}

	public Ident removeMemberPart() {
		final String last = getLast();
		final int x = last.lastIndexOf("::");
		if (x == -1) {
			return null;
		}
		final List<String> copy = new ArrayList<String>(parts);
		final int pos = copy.size() - 1;
		copy.set(pos, last.substring(0, x));
		return new Ident(copy);
	}

	public String getPortMember() {
		final String last = getLast();
		final int x = last.lastIndexOf("::");
		if (x == -1) {
			return null;
		}
		return last.substring(x + 2);
	}

	static private Ident from(String full, String separator) {
		final Ident result = new Ident(new ArrayList<String>());
		if (isOdd(full, separator)) {
			result.parts.add(full);
			return result;
		}
		while (true) {
			int idx = full.indexOf(separator);
			if (idx == -1) {
				result.parts.add(full);
				result.checkResult(separator);
				return result;
			}
			if (idx > 0) {
				result.parts.add(full.substring(0, idx));
			}
			full = full.substring(idx + separator.length());
		}
	}

	private static boolean isOdd(String full, String separator) {
		return separator == null || full.contains(separator + separator) || full.endsWith(separator);
	}

	private void checkResult(String separator) {
		for (String s : this.parts) {
			if (s.length() == 0) {
				throw new IllegalStateException(toString());
			}
			if (separator != null && s.contains(separator) && s.endsWith(separator) == false
					&& s.contains(separator + separator) == false) {
				throw new IllegalStateException(toString());
			}
		}
	}

	public Ident add(String sup, String separator) {
		this.checkResult(separator);
		final Ident added = from(sup, separator);
		final List<String> list = new ArrayList<String>(this.parts.size() + added.parts.size());
		list.addAll(this.parts);
		list.addAll(added.parts);
		final Ident result = new Ident(list);
		result.checkResult(separator);
		return result;
	}

	public Ident parent() {
		if (parts.size() == 0) {
			throw new IllegalArgumentException();
		}
		return new Ident(parts.subList(0, parts.size() - 1));
	}

	@Override
	public boolean equals(Object obj) {
		final Ident other = (Ident) obj;
		return this.parts.equals(other.parts);
	}

	@Override
	public int hashCode() {
		return parts.hashCode();
	}

	public String toString(String sep) {
		if (sep == null) {
			sep = ".";
		}
		final StringBuilder sb = new StringBuilder();
		for (String s : parts) {
			if (sb.length() > 0) {
				sb.append(sep);
			}
			sb.append(s);
		}
		return sb.toString();
	}

	public void checkSameAs(Code code, String separator, CucaDiagram diagram) {
		if (diagram.V1972()) {
			return;
		}
		final String last = parts.get(parts.size() - 1);
		if (separator == null) {
			if (code.getName().equals(last) != true && code.getName().equals(toString(separator)) == false) {
				System.err.println("code1=" + code);
				System.err.println("this1=" + this);
				EntityFactory.bigError();
			}
		} else {
			if (getLastPart(code.getName(), separator).equals(last) != true
					&& code.getName().equals(toString(separator)) == false) {
				System.err.println("code2=" + code);
				System.err.println("this2=" + this);
				EntityFactory.bigError();
			}
		}
	}

	private String getLastPart(String fullName, String separator) {
		if (separator == null) {
			return fullName;
		}
		final int x = fullName.lastIndexOf(separator);
		if (x == -1) {
			return fullName;
		}
		return fullName.substring(x + separator.length());
	}

	// public int compareTo(Code o) {
	// throw new UnsupportedOperationException();
	// }

	public String getName() {
		return getLast();
	}

	public boolean isRoot() {
		return parts.size() == 0;
	}

	public Ident move(Ident from, Ident to) {
		if (this.startsWith(from) == false) {
			throw new IllegalArgumentException();
		}
		final List<String> result = new ArrayList<String>(to.parts);
		for (int i = from.parts.size(); i < this.parts.size(); i++) {
			result.add(this.parts.get(i));
		}
		return new Ident(result);
	}

	public int size() {
		return parts.size();
	}

	public Ident getPrefix(int toIndex) {
		return new Ident(this.parts.subList(0, toIndex));
	}

	public Ident getSuffix(int fromIndex) {
		return new Ident(this.parts.subList(fromIndex, this.parts.size()));
	}

}
