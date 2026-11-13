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
 */
package net.sourceforge.plantuml.chart.command;

import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOr;
import net.sourceforge.plantuml.regex.RegexResult;

/**
 * The optional name of a series (bar, line, area, scatter), shown in the legend.
 *
 * It is either quoted, and may then hold spaces ("Revenue 2024"), or a single word of
 * letters, digits, '_', '.' and '-' (Revenue_2024), see #2632. An unquoted name must be
 * separated from the keyword by a blank: "barfoo [1, 2]" must not be read as a bar named
 * "foo", since the keyword is followed by zero or more spaces.
 *
 * That blank is part of the pattern, and not a lookbehind, on purpose: the first tokens of a
 * command are read from its pattern (see FirstTokens), which skips lookbehinds and cannot
 * enumerate {@code \p{L}}. With a blank that closes the keyword, the token stays "bar".
 */
final class ChartSeriesName {

	private ChartSeriesName() {
	}

	static IRegex regex() {
		return new RegexOr(//
				new RegexLeaf(1, "NAME", "\"([^\"]+)\""), //
				new RegexConcat(//
						RegexLeaf.spaceOneOrMore(), //
						new RegexLeaf(1, "NAMEWORD", "([\\p{L}0-9_][\\p{L}0-9_.\\-]*)")));
	}

	static String get(RegexResult arg) {
		final String quoted = arg.get("NAME", 0);
		return quoted != null ? quoted : arg.get("NAMEWORD", 0);
	}
}
