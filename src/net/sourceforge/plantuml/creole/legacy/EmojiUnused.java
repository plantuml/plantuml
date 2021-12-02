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
package net.sourceforge.plantuml.creole.legacy;

import java.util.regex.Pattern;

// Syntax  from https://github.com/ikatyang/emoji-cheat-sheet/blob/master/README.md
// Unicode from https://unicode.org/emoji/charts/full-emoji-list.html

public enum EmojiUnused {

	wave(0x1F44B), //
	grinning(0x1F600), //
	yum(0x1F60B), //
	// smile(0x1F642), //
	disappointed(0x2639), //
	smile(0x263A), //
	innocentfoo(0x1F607); //

	static private final Pattern ALL = allPattern();

	private final int[] codePoints;

	private EmojiUnused(int... codePoints) {
		this.codePoints = codePoints;
	}

	private static Pattern allPattern() {
		final StringBuilder result = new StringBuilder();
		result.append("\\<:(");
		for (EmojiUnused emoji : EmojiUnused.values()) {
			if (result.toString().endsWith("(") == false)
				result.append("|");
			result.append(emoji.name());
		}
		result.append("):\\>");
		return Pattern.compile(result.toString());
	}

	private String toJavaString() {
		final StringBuilder result = new StringBuilder();

		for (Integer codePoint : codePoints)
			for (char ch : Character.toChars(codePoint))
				result.append(ch);

		return result.toString();
	}

	public static String replace(String s) {
		if (ALL.matcher(s).find())
			for (EmojiUnused emoji : EmojiUnused.values())
				s = s.replace("<:" + emoji.name() + ":>", emoji.toJavaString());

		return s;
	}

}
