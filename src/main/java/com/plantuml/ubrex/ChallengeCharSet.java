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
package com.plantuml.ubrex;

import java.util.EnumSet;
import java.util.Set;

public class ChallengeCharSet implements Challenge {

	private final Set<CharClassRaw> charClasses = EnumSet.noneOf(CharClassRaw.class);
	private final CharSet charSet = new CharSet();
	private boolean reversed = false;

	public static ChallengeCharSet build(CharSequence pattern) {
		if (pattern.length() == 0)
			throw new IllegalArgumentException("Empty!");

		if (pattern.charAt(pattern.length() - 1) == '〜')
			throw new IllegalArgumentException("Range operator '〜' must be followed by a character.");

		final ChallengeCharSet result = new ChallengeCharSet();

		// for (int i = 0; i < pattern.length(); i++) {
		for (TextNavigator nav = TextNavigator.build(pattern); nav.length() > 0; nav.jump(1)) {
			final char ch = nav.charAt(0);
			if (/* i == 0 && */ ch == '〤') {
				result.reversed = true;
				continue;
			}
			if (ch == ' ')
				continue;

			if (ch == '〴') {
				nav.jump(1);
				final CharClassRaw charClass = CharClassRaw.fromDefinition(nav);
				result.charClasses.add(charClass);
				nav.jump(charClass.getDefinitionLength() - 1);
			} else if (nav.length() > 2 && nav.charAt(1) == '〜') {
				nav.jump(2);
				final char end = nav.charAt(0);
				result.addRange(ch, end);
			} else if (ch == '〃') {
				result.addChar('\"');
			} else {
				result.addChar(ch);
			}
		}

		return result;
	}

	public void addChar(char ch) {
		charSet.addChar(ch);
	}

	public void addRange(char start, char end) {
		charSet.addRange(start, end);
	}

	@Override
	public ChallengeResult runChallenge(TextNavigator string, int position) {
		if (string.length() == position)
			return new ChallengeResult(NO_MATCH);

		final char ch = string.charAt(position);
		if ((charSet.contains(ch) || CharClassRaw.internalMatchesAny(charClasses, ch)) != reversed)
			return new ChallengeResult(1);

		return new ChallengeResult(NO_MATCH);
	}

}
