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
 *
 */
package net.sourceforge.plantuml.cucadiagram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import net.sourceforge.plantuml.abel.Entity;

abstract class BodierAbstract implements Bodier {

	protected final List<CharSequence> rawBody = new ArrayList<>();
	protected Entity leaf;

	@Override
	final public void setLeaf(Entity leaf) {
		this.leaf = Objects.requireNonNull(leaf);
	}

	@Override
	final public List<CharSequence> getRawBody() {
		return Collections.unmodifiableList(rawBody);
	}

	static private boolean isAlphanum(char c) {
		return Character.isAlphabetic(c) || Character.isDigit(c) || c == '_';
	}

	static private boolean isOnlyLetter(char c) {
		return Character.isAlphabetic(c);
	}

	@Override
	public final CharSequence getBestMatch(CharSequence candidate) {
		if (candidate == null || candidate.length() == 0)
			throw new IllegalArgumentException("candidate must not be empty");

		CharSequence best = null;
		long bestScore = Long.MAX_VALUE;
		for (CharSequence line : rawBody) {
			final long score = matchScore(line, candidate);
			if (score < bestScore) {
				best = line;
				bestScore = score;
				if (bestScore == 0)
					return best;
			}
		}

		return best;
	}

	private static final long WEIGHT_BEFORE_MATCH_STEP = 1L;
	private static final long WEIGHT_AFTER_SEPARATOR = 1_000L;
	private static final long WEIGHT_TRAILING_LETTERS = 1_000_000L;
	private static final long WEIGHT_BEFORE_MATCH_LETTER_STEP = 1000_000_000L;

	static long matchScore(CharSequence fullString, CharSequence candidate) {
		if (fullString == null || candidate == null)
			throw new IllegalArgumentException();
		if (candidate.length() == 0)
			throw new IllegalArgumentException("candidate must not be empty");

		final int lenFull = fullString.length();
		final int lenCand = candidate.length();

		long score = 0;
		for (int i = 0; i <= lenFull - lenCand; i++) {
			if (startsWith(fullString, i, candidate)) {

				boolean separatorSeen = false;
				for (int j = i + lenCand; j < lenFull; j++) {
					final char ch = fullString.charAt(j);
					if (separatorSeen == false && isAlphanum(ch)) {
						score += WEIGHT_TRAILING_LETTERS;
					} else {
						separatorSeen = true;
						score += WEIGHT_AFTER_SEPARATOR;
					}
				}
				return score;
			}

			final char ch = fullString.charAt(i);
			if (isOnlyLetter(ch))
				score += WEIGHT_BEFORE_MATCH_LETTER_STEP;
			else
				score += WEIGHT_BEFORE_MATCH_STEP;
		}

		return Long.MAX_VALUE;
	}

	static boolean startsWith(CharSequence fullString, int startIdx, CharSequence candidate) {
		if (fullString == null || candidate == null)
			return false;

		final int lenFull = fullString.length();
		final int lenCandidate = candidate.length();

		if (lenCandidate == 0)
			throw new IllegalArgumentException();

		if (startIdx < 0 || startIdx > lenFull)
			return false;

		if (startIdx + lenCandidate > lenFull)
			return false;

		for (int i = 0; i < lenCandidate; i++)
			if (fullString.charAt(startIdx + i) != candidate.charAt(i))
				return false;

		return true;
	}
}
