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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompositeList implements Challenge {

	private final List<Challenge> challenges = new ArrayList<>();

	public void addChallenge(Challenge p) {
		this.challenges.add(p);
	}

	public static CompositeList createEmpty() {
		return new CompositeList();
	}

	public static CompositeList parseAndBuild(CharSequence input) {
		if (input == null)
			throw new IllegalArgumentException();

		return new CompositeList(input);
	}

	@Override
	public String toString() {
		return challenges.toString();
	}

	private CompositeList() {

	}

	public static CompositeList parseAndBuildFromTextNavigator(TextNavigator input) {
		if (input == null)
			throw new IllegalArgumentException();
		final CompositeList result = new CompositeList();
		result.parseAndConsumeNow(input);
		return result;
	}

	private CompositeList(CharSequence definition) {
		if (definition.length() == 0)
			throw new UnsupportedOperationException();

		final TextNavigator input = TextNavigator.build(definition);
		parseAndConsumeNow(input);

	}

	private void parseAndConsumeNow(final TextNavigator input) {
		final AtomicParser builder = new AtomicParser();
		while (input.length() > 0) {
			final char ch = input.charAt(0);
			if (ch == ' ')
				input.jump(1);
			else
				for (Challenge c : builder.parse(input))
					this.addChallenge(c);

		}
	}

	@Override
	public ChallengeResult runChallenge(TextNavigator string, int position) {

		Capture capture = Capture.EMPTY;
		// Capture nonMergeableCapture = Capture.EMPTY;

		int current = position;
		for (Challenge challenge : challenges) {
			final ChallengeResult shallWePass = challenge.runChallenge(string, current);
			if (shallWePass.getFullCaptureLength() < 0)
				return new ChallengeResult(NO_MATCH, capture);

			current += shallWePass.getFullCaptureLength();
			capture = capture.merge(shallWePass.getCapture());
			capture = capture.merge(shallWePass.getNonMergeableCapture());
		}

		return new ChallengeResult(current - position, capture);
	}

	public List<Challenge> getInternalChallengesList() {
		return Collections.unmodifiableList(challenges);
	}

}
