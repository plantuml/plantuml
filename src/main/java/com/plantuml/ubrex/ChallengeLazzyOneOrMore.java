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
	private final Challenge stopCondition;

	public ChallengeLazzyOneOrMore(Challenge origin, Challenge stopCondition) {
		this.origin = origin;
		this.stopCondition = stopCondition;
	}

	@Override
	public ChallengeResult runChallenge(TextNavigator string, int position) {
		Capture capture = Capture.EMPTY;

		int currentPos = position;
		while (true) {
			final ChallengeResult match1 = origin.runChallenge(string, currentPos);
			if (match1.getFullCaptureLength() < 0)
				return ChallengeResult.NO_MATCH;
			if (match1.getFullCaptureLength() == 0)
				throw new IllegalStateException("infinite loop");
			capture = capture.merge(match1.getCapture());
			currentPos += match1.getFullCaptureLength();

			// Only peek at the stop condition, don't consume it.
			// The stop condition challenges will be matched as siblings
			// by the parent CompositeList/CompositeNamed.
			final ChallengeResult match2 = stopCondition.runChallenge(string, currentPos);
			if (match2.getFullCaptureLength() >= 0)
				return new ChallengeResult(currentPos - position, capture);

		}
	}

}
