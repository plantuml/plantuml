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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AtomicParser {

	private static int getClosingBracket(TextNavigator input, char open, char close) {
		int level = 0;
		for (int i = 1; i < input.length(); i++) {
			final char ch = input.charAt(i);
			if (ch == open)
				level++;
			else if (ch == close)
				if (level == 0)
					return i;
				else if (--level < 0)
					throw new IllegalArgumentException();
		}
		return -1;
	}

	private Challenge parseSingle(TextNavigator input) {
		final List<Challenge> result = parse(input);
		if (result.size() != 1)
			throw new UnsupportedOperationException();
		return result.get(0);
	}

	public List<Challenge> parse(TextNavigator input) {
		switch (input.charAt(0)) {
		case '┇':
			throw new IllegalArgumentException();
		case '〒':
			return Collections.singletonList(manageLookAround(input));
		case '【':
			return Collections.singletonList(manageAlternative(input));
		case '〇':
			return Collections.singletonList(manageQuantifier(input));
		case '〄':
			return manageUpTo(input);
		case '〘':
			return Collections.singletonList(manageGroup(input));
		case '「':
			return Collections.singletonList(manageCharacterSet(input));
		case '〴':
			return Collections.singletonList(manageClass(input));
		case '〶':
			return Collections.singletonList(manageNamed(input));
		case '〃':
			return Collections.singletonList(manageDoubleQuote(input));
		default:
			return Collections.singletonList(manageRegularCharacter(input));
		}
	}

	private Challenge manageLookAround(TextNavigator input) {
		input.jump(1);
		final LookAround look = LookAround.from(input);
		if (look == null)
			throw new UnsupportedOperationException("Syntax error");
		input.jump(look.getDefinitionSize());

		final Challenge result;
		if (look == LookAround.END_OF_TEXT) {
			result = new ChallengeEndOfText();
		} else {
			final Challenge p1 = parseSingle(input);

			if (look.isLookBehind())
				result = new ChallengeLookBehind(p1, look);
			else if (look.isLookAhead())
				result = new ChallengeLookAhead(p1, look);
			else
				throw new UnsupportedOperationException();
		}

		return result;
	}

	private Challenge manageClass(TextNavigator input) {
		input.jump(1);
		final CharClass result = CharClass.fromDefinition(input);
		input.jump(result.getDefinitionLength());
		return new ChallengeCharClass(result);
	}

	private Challenge manageQuantifier(TextNavigator input) {
		final char operator = input.charAt(1);
		input.jump(2);
		if (operator == '{')
			return manageQuantifierBracket(input);
		else if (operator == 'l')
			return manageQuantifierLazzy(input);

		final Challenge origin = parseSingle(input);
		switch (operator) {
		case '+':
			return new ChallengeOneOrMore(origin);
		case '*':
			return new ChallengeZeroOrMore(origin);
		case '?':
			return new ChallengeOptional(origin);
		default:
			throw new UnsupportedOperationException("wip01");
		}
	}

	private Challenge manageQuantifierLazzy(TextNavigator input) {
		input.jump(1);
		final Challenge origin = parseSingle(input);
		final CompositeList remaining = CompositeList.parseAndBuildFromTextNavigator(input);

		return new ChallengeLazzyOneOrMore(origin, remaining);
	}

	private Challenge manageQuantifierBracket(TextNavigator input) {
		final Repetition repetition = Repetition.parse(input);
		final Challenge origin = parseSingle(input);
		return new ChallengeRepetition(repetition, origin);
	}

	private List<Challenge> manageUpTo(TextNavigator input) {
		final char operator = input.charAt(1);
		if (operator == '>') {
			input.jump(2);
			final Challenge p2 = parseSingle(input);
			// This is a terrible hack, because if this is used with named group, we do not
			// want p2 to be captured
			return Arrays.asList(new ChallengeUpTo(p2), p2);
		}
		if (operator != '+')
			throw new UnsupportedOperationException("manageQuantifierUpTo1");

		input.jump(2);
		skipSpaces(input);

		final Challenge p1 = parseSingle(input);

		skipSpaces(input);

		if (input.charAt(0) != '-')
			throw new UnsupportedOperationException("manageQuantifierUpTo2");

		if (input.charAt(1) != '>')
			throw new UnsupportedOperationException("manageQuantifierUpTo2");

		input.jump(2);
		skipSpaces(input);

		final Challenge p2 = parseSingle(input);
		// This is a terrible hack, because if this is used with named group, we do not
		// want p2 to be captured
		return Arrays.asList(new ChallengeOneOrMoreUpToOldVersion(p1, p2), p2);
	}

	private void skipSpaces(TextNavigator input) {
		while (input.charAt(0) == ' ')
			input.jump(1);
	}

	private Challenge manageGroup(TextNavigator input) {
		final int end = getClosingBracket(input, '〘', '〙');
		if (end == -1)
			throw new UnsupportedOperationException("wip99");

		final CompositeList result = CompositeList.parseAndBuild(input.subSequence(1, end));
		input.jump(end + 1);
		return result;
	}

	private Challenge manageAlternative(TextNavigator input) {
		final ChallengeAlternative result = new ChallengeAlternative();

		int start = 1;
		int level = 0;
		for (int i = 1; i < input.length(); i++) {
			final char ch = input.charAt(i);
			if (ch == '【')
				level++;
			else if (level == 0 && ch == '┇') {
				final CompositeList part = CompositeList.parseAndBuild(input.subSequence(start, i));
				result.addAlternative(part);
				start = i + 1;
			} else if (ch == '】')
				if (level == 0) {
					final CompositeList part = CompositeList.parseAndBuild(input.subSequence(start, i));
					result.addAlternative(part);
					input.jump(i + 1);
					return result;
				} else if (--level < 0)
					throw new IllegalArgumentException();

		}
		throw new UnsupportedOperationException("wip32");
	}

	private Challenge manageNamed(TextNavigator input) {
		final StringBuilder name = new StringBuilder();
		if (input.charAt(1) != '$')
			throw new UnsupportedOperationException("varname must have a $");

		input.jump(2);
		while (true) {
			final char ch = input.charAt(0);
			input.jump(1);
			if (ch == '=') {
				if (name.length() == 0)
					throw new UnsupportedOperationException("no name!");
				final CompositeNamed result = new CompositeNamed(name.toString(), parse(input));

				return result;
			}
			if (Character.isJavaIdentifierPart(ch))
				name.append(ch);
			else
				throw new UnsupportedOperationException("Unsupported name!");
		}
	}

	private Challenge manageCharacterSet(TextNavigator input) {
		final int end = input.indexOf('」');
		if (end == -1)
			throw new UnsupportedOperationException("wip80");
		final ChallengeCharSet result = ChallengeCharSet.build(input.subSequence(1, end));
		input.jump(end + 1);
		return result;
	}

	private Challenge manageRegularCharacter(TextNavigator input) {
		char ch = input.charAt(0);
		if (ch == ' ')
			throw new IllegalStateException("no space allowed");
		if (ch == '∙')
			ch = ' ';
		input.jump(1);
		return new ChallengeSingleChar(ch);
	}

	private Challenge manageDoubleQuote(TextNavigator input) {
		input.jump(1);
		return new ChallengeSingleChar('\"');
	}

}
