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

public class ChallengeLazzyOneOrMore implements Challenge {

	private final Challenge origin;
	private final Challenge end;

	public ChallengeLazzyOneOrMore(Challenge origin, Challenge end) {
		this.origin = origin;
		this.end = end;
	}

	@Override
	public ChallengeResult runChallenge(TextNavigator string, int position) {
		Capture capture = Capture.EMPTY;

		int currentPos = position;
		while (true) {
			final ChallengeResult match1 = origin.runChallenge(string, currentPos);
			if (match1.getFullCaptureLength() < 0)
				return new ChallengeResult(NO_MATCH);
			if (match1.getFullCaptureLength() == 0)
				throw new IllegalStateException("infinite loop");
			capture = capture.merge(match1.getCapture());
			currentPos += match1.getFullCaptureLength();

			final ChallengeResult match2 = end.runChallenge(string, currentPos);
			if (match2.getFullCaptureLength() >= 0) {
				final int result = currentPos + match2.getFullCaptureLength() - position;
				final int nameLength = currentPos - position;
				// capture = capture.merge(match2.getCapture());
				return new ChallengeResult(result, capture).withNameLength(nameLength, match2.getCapture());
			}

		}
	}

}
