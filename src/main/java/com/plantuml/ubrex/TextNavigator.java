/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
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
package com.plantuml.ubrex;

public class TextNavigator implements CharSequence {

	private final CharSequence content;
	private int p1;
	private int p2;
	private boolean reversed;

	public static TextNavigator build(CharSequence content) {
		return new TextNavigator(content, 0, content.length(), false);
	}

	public TextNavigator reverse(int pos) {
		if (reversed)
			throw new IllegalStateException();

		return new TextNavigator(content, 0, p1 + pos, true);
	}

	@Override
	public String toString() {
		final int length = length();
		final StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++)
			sb.append(this.charAt(i));
		return sb.toString();
	}

	private TextNavigator(CharSequence content, int p1, int p2, boolean reversed) {
		if (p2 < p1)
			throw new IllegalArgumentException();
		this.content = content;
		this.p1 = p1;
		this.p2 = p2;
		this.reversed = reversed;
	}

	public int indexOf(char ch) {
		for (int i = 0; i < length(); i++)
			if (this.charAt(i) == ch)
				return i;
		return -1;
	}

	@Override
	public TextNavigator subSequence(int beginIndex, int endIndex) {
		if (beginIndex < 0 || endIndex > length() || beginIndex > endIndex)
			throw new IndexOutOfBoundsException(
					"subSequence(" + beginIndex + ", " + endIndex + ") out of bounds for length " + length());
		if (reversed)
			return new TextNavigator(content, p2 - endIndex, p2 - beginIndex, reversed);

		return new TextNavigator(content, p1 + beginIndex, p1 + endIndex, reversed);
	}

	@Override
	public int length() {
		return p2 - p1;
	}

	@Override
	public char charAt(int index) {
		if (index < 0 || index >= length())
			throw new IndexOutOfBoundsException("Index " + index + " out of bounds for length " + length());

		if (reversed)
			return this.content.charAt(p2 - index - 1);

		return this.content.charAt(p1 + index);
	}

	public void jump(int step) {
		if (reversed)
			p2 -= step;
		else
			p1 += step;
	}

	public boolean startsWith(String searched, int ahead) {
		for (int i = 0; i < searched.length(); i++)
			if (searched.charAt(i) != this.charAt(ahead + i))
				return false;

		return true;
	}

	public int search(String searched, int ahead) {
		for (int i = ahead; i < this.length() - searched.length() + 1; i++)
			if (startsWith(searched, i))
				return i;
		return -1;
	}

	public int searchPattern(Challenge pattern, int ahead) {
		for (int i = ahead; i < this.length(); i++)
			if (pattern.runChallenge(this, i).getFullCaptureLength() >= 0)
				return i;
		return Challenge.NO_MATCH;
	}

}
