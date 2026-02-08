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
package net.sourceforge.plantuml.cli;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeSet;

public class HelpGroup implements Iterable<CliFlag> {

	// ::remove file when __CORE__ or __TEAVM__
	// ::remove file when __HAXE__

	private static final Comparator<CliFlag> COMPARATOR = new Comparator<CliFlag>() {

		private String clean(String s) {
			// Just because we what "-v, --verbose" and "--version" to be sorted %-)
			final String onlyLetters = s.replaceAll("\\W", "").toLowerCase(Locale.US);
			if (s.trim().startsWith("--"))
				return onlyLetters.charAt(0) + onlyLetters;
			return onlyLetters;
		}

		@Override
		public int compare(CliFlag flag1, CliFlag flag2) {
			final String usage1 = clean(flag1.getUsage());
			final String usage2 = clean(flag2.getUsage());
			return usage1.compareTo(usage2);
		}
	};

	private final String title;
	private final Collection<CliFlag> flags = new TreeSet<>(COMPARATOR);

	public HelpGroup(String title) {
		this.title = title;
	}

	public void append(CliFlag flag) {
		this.flags.add(flag);

	}

	public String getTitle() {
		return title;
	}

	@Override
	public Iterator<CliFlag> iterator() {
		return flags.iterator();
	}

}
