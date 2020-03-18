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
package net.sourceforge.plantuml.graphic.color;

import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;

public class ColorParser {

	private static final String COLOR_REGEXP = "#\\w+[-\\\\|/]?\\w+";

	private static final String PART2 = "#(?:\\w+[-\\\\|/]?\\w+;)?(?:(?:text|back|header|line|line\\.dashed|line\\.dotted|line\\.bold|shadowing)(?::\\w+[-\\\\|/]?\\w+)?(?:;|(?![\\w;:.])))+";
	private static final String COLORS_REGEXP = "(?:" + PART2 + ")|(?:" + COLOR_REGEXP + ")";

	private final RegexLeaf regex;
	private final String name;
	private final ColorType mainType;

	private ColorParser(String name, RegexLeaf regex, ColorType mainType) {
		this.regex = regex;
		this.name = name;
		this.mainType = mainType;
	}

	public Colors getColor(RegexResult arg, HColorSet set) {
		if (mainType == null) {
			throw new IllegalStateException();
		}
		final String data = arg.getLazzy(name, 0);
		if (data == null) {
			return Colors.empty();
		}
		return new Colors(data, set, mainType);
		// return result.getColor(type);
	}

	// New Parsers
	public static ColorParser simpleColor(ColorType mainType) {
		return simpleColor(mainType, "COLOR");
	}

	public static ColorParser simpleColor(ColorType mainType, String id) {
		return new ColorParser(id, new RegexLeaf(id, "(" + COLORS_REGEXP + ")?"), mainType);
	}

	public static ColorParser mandatoryColor(ColorType mainType) {
		return new ColorParser("COLOR", new RegexLeaf("COLOR", "(" + COLORS_REGEXP + ")"), mainType);
	}

	public static ColorParser simpleColor(String optPrefix, ColorType mainType) {
		return new ColorParser("COLOR", new RegexLeaf("COLOR", "(?:" + optPrefix + " (" + COLORS_REGEXP + "))?"),
				mainType);
	}

	// Old Parsers

	public static RegexLeaf exp1() {
		return simpleColor(null).regex;
	}

	public static RegexLeaf exp2() {
		return new RegexLeaf("BACKCOLOR", "(" + COLOR_REGEXP + ")?");
	}

	public static RegexLeaf exp3() {
		return new RegexLeaf("BACKCOLOR2", "(" + COLOR_REGEXP + ")?");
	}

	public static RegexLeaf exp4() {
		return new RegexLeaf("COLOR", "(?:(" + COLOR_REGEXP + "):)?");
	}

	public static RegexLeaf exp6() {
		return new RegexLeaf("COLOR", "(?:(" + COLOR_REGEXP + ")\\|)?");
	}

	public static RegexLeaf exp7() {
		return new RegexLeaf("COLOR", "(?:(" + COLOR_REGEXP + "))?");
	}

	public RegexLeaf getRegex() {
		return regex;
	}

}
