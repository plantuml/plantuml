/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 8218 $
 *
 */
package net.sourceforge.plantuml.cucadiagram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;

public class Display implements Iterable<CharSequence> {

	private final List<CharSequence> display = new ArrayList<CharSequence>();

	private Display(Display other) {
		this.display.addAll(other.display);
	}

	public Display() {
	}

	public Display(List<? extends CharSequence> other) {
		this.display.addAll(other);
	}

	public Display underlined() {
		final List<CharSequence> result = new ArrayList<CharSequence>();
		for (CharSequence line : display) {
			result.add("<u>" + line);
		}
		return new Display(result);
	}

	@Override
	public String toString() {
		return display.toString();
	}

	@Override
	public int hashCode() {
		return display.hashCode();
	}

	@Override
	public boolean equals(Object other) {
		return this.display.equals(((Display) other).display);
	}

	public Display addAll(Display other) {
		final Display result = new Display(this);
		result.display.addAll(other.display);
		return result;
	}

	public Display addFirst(CharSequence s) {
		final Display result = new Display(this);
		result.display.add(0, s);
		return result;
	}

	public Display add(CharSequence s) {
		final Display result = new Display(this);
		result.display.add(s);
		return result;
	}

	private boolean firstColumnRemovable() {
		boolean allEmpty = true;
		for (CharSequence s : this) {
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

	public Display removeEmptyColumns() {
		if (firstColumnRemovable() == false) {
			return this;
		}
		final Display result = new Display(this);
		do {
			for (int i = 0; i < result.size(); i++) {
				final CharSequence s = result.get(i);
				if (s.length() > 0) {
					result.display.set(i, s.toString().substring(1));
				}
			}
		} while (result.firstColumnRemovable());
		return result;
	}

	public int size() {
		return display.size();
	}

	public CharSequence get(int i) {
		return display.get(i);
	}

	public Iterator<CharSequence> iterator() {
		return Collections.unmodifiableList(display).iterator();
	}

	public Display subList(int i, int size) {
		return new Display(display.subList(i, size));
	}

	public static Display asList(CharSequence... s) {
		return new Display(Arrays.asList(s));
	}

	public static Display emptyList() {
		return new Display();
	}

	public List<? extends CharSequence> as() {
		return Collections.unmodifiableList(display);
	}

	public static Display getWithNewlines(Code s) {
		return getWithNewlines(s.getCode());
	}

	public static Display getWithNewlines(String s) {
		if (s == null) {
			return null;
		}
		final Display result = new Display();
		final StringBuilder current = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			if (c == '\\' && i < s.length() - 1) {
				final char c2 = s.charAt(i + 1);
				i++;
				if (c2 == 'n') {
					result.display.add(current.toString());
					current.setLength(0);
				} else if (c2 == 't') {
					current.append('\t');
				} else if (c2 == '\\') {
					current.append(c2);
				} else {
					current.append(c);
					current.append(c2);
				}
			} else {
				current.append(c);
			}
		}
		result.display.add(current.toString());
		return result;
	}

	public Url initUrl() {
		if (this.size() == 0) {
			return null;
		}
		final UrlBuilder urlBuilder = new UrlBuilder(null, ModeUrl.AT_START);
		return urlBuilder.getUrl(this.get(0).toString().trim());
	}

	public Display removeUrl(Url url) {
		if (url == null) {
			return this;
		}
		final Display result = new Display();
		result.display.add(UrlBuilder.purgeUrl(this.get(0).toString()));
		result.display.addAll(this.subList(1, this.size()).display);
		return result;
	}

	public boolean hasUrl() {
		final UrlBuilder urlBuilder = new UrlBuilder(null, ModeUrl.ANYWHERE);
		for (CharSequence s : this) {
			if (urlBuilder.getUrl(s.toString()) != null) {
				return true;
			}
		}
		return false;
	}

}
