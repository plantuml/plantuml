/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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

public class Display2 implements Iterable<CharSequence> {

	private final List<CharSequence> display = new ArrayList<CharSequence>();

	public static Display2 empty() {
		return new Display2();
	}

	public static Display2 create(CharSequence... s) {
		if (s.length==1 && s[0]==null) {
			return empty();
		}
		return new Display2(Arrays.asList(s));
	}

	public static Display2 create(List<? extends CharSequence> other) {
		return new Display2(other);
	}

	public static Display2 getWithNewlines(Code s) {
		return getWithNewlines(s.getFullName());
	}

	public static Display2 getWithNewlines(String s) {
		if (s == null) {
			return null;
		}
		final Display2 result = new Display2();
		result.display.addAll(getWithNewlinesInternal(s));
		return result;
	}

	private Display2(List<? extends CharSequence> other) {
		for (CharSequence s : other) {
			this.display.addAll(getWithNewlinesInternal(s));
		}
	}

	private Display2(Display2 other) {
		this.display.addAll(other.display);
	}

	private Display2() {
	}

	public Display2 underlined() {
		final List<CharSequence> result = new ArrayList<CharSequence>();
		for (CharSequence line : display) {
			result.add("<u>" + line);
		}
		return new Display2(result);
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
		return this.display.equals(((Display2) other).display);
	}

	public Display2 addAll(Display2 other) {
		final Display2 result = new Display2(this);
		result.display.addAll(other.display);
		return result;
	}

	public Display2 addFirst(CharSequence s) {
		final Display2 result = new Display2(this);
		result.display.addAll(0, getWithNewlinesInternal(s));
		return result;
	}

	public Display2 add(CharSequence s) {
		final Display2 result = new Display2(this);
		result.display.addAll(getWithNewlinesInternal(s));
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

	public Display2 removeEmptyColumns() {
		if (firstColumnRemovable() == false) {
			return this;
		}
		final Display2 result = new Display2(this);
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

	public Display2 subList(int i, int size) {
		final Display2 result = new Display2();
		result.display.addAll(display.subList(i, size));
		return result;
	}

	public List<? extends CharSequence> as() {
		return Collections.unmodifiableList(display);
	}

	private static List<String> getWithNewlinesInternal(CharSequence s) {
		final List<String> result = new ArrayList<String>();
		final StringBuilder current = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			if (c == '\\' && i < s.length() - 1) {
				final char c2 = s.charAt(i + 1);
				i++;
				if (c2 == 'n') {
					result.add(current.toString());
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
		result.add(current.toString());
		return result;
	}

	public Url initUrl() {
		if (this.size() == 0) {
			return null;
		}
		final UrlBuilder urlBuilder = new UrlBuilder(null, ModeUrl.AT_START);
		return urlBuilder.getUrl(this.get(0).toString().trim());
	}

	public Display2 removeUrl(Url url) {
		if (url == null) {
			return this;
		}
		final Display2 result = new Display2();
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
