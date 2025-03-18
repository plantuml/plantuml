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

public class ChallengeSingleChar implements Challenge {

	private final CaseMode mode = CaseMode.CASE_INSENSITIVE;

	private final char ch;

	public ChallengeSingleChar(char ch) {
		switch (ch) {
		case '┇':
		case '〴':
		case '「':
		case '」':
		case '〤':
		case '〜':
		case '〇':
		case '〄':
		case '〶':
		case '〘':
		case '〙':
		case '【':
		case '】':
			throw new IllegalArgumentException();
		}

		if (mode == CaseMode.CASE_INSENSITIVE)
			ch = CaseMode.ensureLowercase(ch);

		this.ch = ch;

	}

	@Override
	public String toString() {
		return "[" + ch + "]";
	}

	@Override
	public ChallengeResult runChallenge(TextNavigator string, int position) {
		if (string.length() == position)
			return new ChallengeResult(NO_MATCH);
		char extract = string.charAt(position);
		if (mode == CaseMode.CASE_INSENSITIVE)
			extract = CaseMode.ensureLowercase(extract);

		if (extract == ch)
			return new ChallengeResult(1);
		return new ChallengeResult(NO_MATCH);
	}

}
