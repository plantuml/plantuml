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

import java.util.List;

public interface UnicodeBracketedExpression {

	public default UMatcher match(String string) {
		return match(TextNavigator.build(string), 0);
	}

	public default UMatcher match(TextNavigator string) {
		return match(string, 0);
	}

	public UMatcher match(TextNavigator string, int position);

	public static UnicodeBracketedExpression build(String ubrex) {
		return from(CompositeList.parseAndBuild(ubrex));
	}

	public static UnicodeBracketedExpression from(final Challenge challenge) {
		return new UnicodeBracketedExpression() {

			@Override
			public UMatcher match(final TextNavigator string, final int position) {

				final ChallengeResult shallWePass = challenge.runChallenge(string, position);
				final CharSequence acceptepMatch;

				if (shallWePass.getFullCaptureLength() < 0)
					acceptepMatch = "";
				else
					acceptepMatch = string.subSequence(position, position + shallWePass.getFullCaptureLength());

				return new UMatcher() {

					@Override
					public String toString() {
						return acceptepMatch + " " + shallWePass.toString();
					}

					@Override
					public boolean exactMatch() {
						if (startMatch() == false)
							return false;

						return position + shallWePass.getFullCaptureLength() == string.length();

					}

					@Override
					public String getAcceptedMatch() {
						return acceptepMatch.toString();
					}

					@Override
					public boolean startMatch() {
						return shallWePass.getFullCaptureLength() >= 0;
					}

					@Override
					public List<String> getCapture(String path) {
						return shallWePass.findValuesByKey(path);
					}

					@Override
					public List<String> getKeysToBeRefactored() {
						return shallWePass.getKeysToBeRefactored();
					}

				};
			}
		};
	}

}
