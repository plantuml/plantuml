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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.EmbededDiagram;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;

public class Display implements Iterable<CharSequence> {

	private final List<CharSequence> display = new ArrayList<CharSequence>();
	private final HorizontalAlignment naturalHorizontalAlignment;

	public boolean isWhite() {
		return display.size() == 0 || (display.size() == 1 && display.get(0).toString().matches("\\s*"));
	}

	public static Display empty() {
		return new Display((HorizontalAlignment) null);
	}

	public static Display create(CharSequence... s) {
		return create(Arrays.asList(s));
	}

	public static Display create(List<? extends CharSequence> other) {
		return new Display(other, null);
	}

	public static Display getWithNewlines(Code s) {
		return getWithNewlines(s.getFullName());
	}

	public static Display getWithNewlines(String s) {
		if (s == null) {
			return null;
		}
		final List<String> result = new ArrayList<String>();
		final StringBuilder current = new StringBuilder();
		HorizontalAlignment naturalHorizontalAlignment = null;
		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			if (c == '\\' && i < s.length() - 1) {
				final char c2 = s.charAt(i + 1);
				i++;
				if (c2 == 'n' || c2 == 'r' || c2 == 'l') {
					if (c2 == 'r') {
						naturalHorizontalAlignment = HorizontalAlignment.RIGHT;
					} else if (c2 == 'l') {
						naturalHorizontalAlignment = HorizontalAlignment.LEFT;
					}
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
			} else if (c == StringUtils.hiddenNewLine()) {
				result.add(current.toString());
				current.setLength(0);
			} else {
				current.append(c);
			}
		}
		result.add(current.toString());
		return new Display(result, naturalHorizontalAlignment);
	}

	private Display(Display other) {
		this(other.naturalHorizontalAlignment);
		this.display.addAll(other.display);
	}

	private Display(HorizontalAlignment naturalHorizontalAlignment) {
		this.naturalHorizontalAlignment = naturalHorizontalAlignment;
	}

	private Display(List<? extends CharSequence> other, HorizontalAlignment naturalHorizontalAlignment) {
		this(naturalHorizontalAlignment);
		this.display.addAll(manageEmbededDiagrams2(other));
	}

	private static List<CharSequence> manageEmbededDiagrams2(final List<? extends CharSequence> strings) {
		final List<CharSequence> result = new ArrayList<CharSequence>();
		final Iterator<? extends CharSequence> it = strings.iterator();
		while (it.hasNext()) {
			CharSequence s = it.next();
			if (s != null && StringUtils.trin(s.toString()).equals("{{")) {
				final List<CharSequence> other = new ArrayList<CharSequence>();
				other.add("@startuml");
				while (it.hasNext()) {
					final CharSequence s2 = it.next();
					if (s2 != null && StringUtils.trin(s2.toString()).equals("}}")) {
						break;
					}
					other.add(s2);
				}
				other.add("@enduml");
				s = new EmbededDiagram(Display.create(other));
			}
			result.add(s);
		}
		return result;
	}

	public Display underlined() {
		final List<CharSequence> result = new ArrayList<CharSequence>();
		for (CharSequence line : display) {
			result.add("<u>" + line);
		}
		return new Display(result, this.naturalHorizontalAlignment);
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
		return new Display(display.subList(i, size), this.naturalHorizontalAlignment);
	}

	public List<? extends CharSequence> as() {
		return Collections.unmodifiableList(display);
	}

	public Url initUrl() {
		if (this.size() == 0) {
			return null;
		}
		final UrlBuilder urlBuilder = new UrlBuilder(null, ModeUrl.AT_START);
		return urlBuilder.getUrl(StringUtils.trin(this.get(0).toString()));
	}

	public Display removeUrl(Url url) {
		if (url == null) {
			return this;
		}
		final Display result = new Display(this.naturalHorizontalAlignment);
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

	public HorizontalAlignment getNaturalHorizontalAlignment() {
		return naturalHorizontalAlignment;
	}

	public List<Display> splitMultiline(Pattern separator) {
		final List<Display> result = new ArrayList<Display>();
		Display pending = new Display(this.naturalHorizontalAlignment);
		result.add(pending);
		for (CharSequence line : display) {
			final Matcher m = separator.matcher(line);
			if (m.find()) {
				final CharSequence s1 = line.subSequence(0, m.start());
				pending.display.add(s1);
				final CharSequence s2 = line.subSequence(m.end(), line.length());
				pending = new Display(this.naturalHorizontalAlignment);
				result.add(pending);
				pending.display.add(s2);
			} else {
				pending.display.add(line);
			}
		}
		return Collections.unmodifiableList(result);
	}

}
