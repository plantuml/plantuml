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
package net.sourceforge.plantuml;

import java.util.StringTokenizer;
import java.util.regex.Matcher;

public class Guillemet {

	public static final Guillemet NONE = new Guillemet("", "");
	public static final Guillemet DOUBLE_COMPARATOR = new Guillemet("<<", ">>");
	public static final Guillemet GUILLEMET = new Guillemet("\u00AB", "\u00BB");

	private final String start;
	private final String end;

	public Guillemet fromDescription(String value) {
		if (value != null) {
			if ("false".equalsIgnoreCase(value)) {
				return Guillemet.DOUBLE_COMPARATOR;
			}
			if ("<< >>".equalsIgnoreCase(value)) {
				return Guillemet.DOUBLE_COMPARATOR;
			}
			if ("none".equalsIgnoreCase(value)) {
				return Guillemet.NONE;
			}
			if (value.contains(" ")) {
				final StringTokenizer st = new StringTokenizer(value, " ");
				return new Guillemet(st.nextToken(), st.nextToken());
			}
		}
		return Guillemet.GUILLEMET;
	}

	private Guillemet(String start, String end) {
		this.start = start;
		this.end = end;

	}

	public String manageGuillemet(String st) {
		if (this == DOUBLE_COMPARATOR) {
			return st;
		}
		return st.replaceAll("\\<\\<\\s?((?:\\<&\\w+\\>|[^<>])+?)\\s?\\>\\>", Matcher.quoteReplacement(start) + "$1"
				+ Matcher.quoteReplacement(end));
	}

	public String manageGuillemetStrict(String st) {
		if (this == DOUBLE_COMPARATOR) {
			return st;
		}
		if (st.startsWith("<< ")) {
			st = start + st.substring(3);
		} else if (st.startsWith("<<")) {
			st = start + st.substring(2);
		}
		if (st.endsWith(" >>")) {
			st = st.substring(0, st.length() - 3) + end;
		} else if (st.endsWith(">>")) {
			st = st.substring(0, st.length() - 2) + end;
		}
		return st;
	}

}