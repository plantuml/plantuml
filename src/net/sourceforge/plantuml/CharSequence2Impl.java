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
package net.sourceforge.plantuml;

public class CharSequence2Impl implements CharSequence2 {

	private final CharSequence s;
	private final LineLocation location;
	private String preprocessorError;

	public CharSequence2Impl(CharSequence s, LineLocation location) {
		this(s, location, null);
	}

	public CharSequence2Impl(CharSequence s, LineLocation location, String preprocessorError) {
		if (s == null) {
			throw new IllegalArgumentException();
		}
		this.s = s;
		this.location = location;
		this.preprocessorError = preprocessorError;
	}

	// public static CharSequence2 errorPreprocessor(CharSequence s, String preprocessorError) {
	// return new CharSequence2Impl("FOO4242", null, preprocessorError);
	// }

	public CharSequence2 withErrorPreprocessor(String preprocessorError) {
		return new CharSequence2Impl(s, location, preprocessorError);
	}

	public int length() {
		return s.length();
	}

	public char charAt(int index) {
		return s.charAt(index);
	}

	public CharSequence2 subSequence(int start, int end) {
		return new CharSequence2Impl(s.subSequence(start, end), location, preprocessorError);
	}

	public CharSequence toCharSequence() {
		return s;
	}

	@Override
	public String toString() {
		return s.toString();
	}

	public String toString2() {
		return s.toString();
	}

	public LineLocation getLocation() {
		return location;
	}

	public CharSequence2 trin() {
		return new CharSequence2Impl(StringUtils.trin(s.toString()), location, preprocessorError);
	}

	public boolean startsWith(String start) {
		return s.toString().startsWith(start);
	}

	public String getPreprocessorError() {
		return preprocessorError;
	}

	public CharSequence2 removeInnerComment() {
		final String string = s.toString();
		final String trim = string.replace('\t', ' ').trim();
		if (trim.startsWith("/'")) {
			final int idx = string.indexOf("'/");
			if (idx != -1) {
				return new CharSequence2Impl(removeSpecialInnerComment(s.subSequence(idx + 2, s.length())), location,
						preprocessorError);
			}
		}
		if (trim.endsWith("'/")) {
			final int idx = string.lastIndexOf("/'");
			if (idx != -1) {
				return new CharSequence2Impl(removeSpecialInnerComment(s.subSequence(0, idx)), location,
						preprocessorError);
			}
		}
		if (trim.contains("/'''") && trim.contains("'''/")) {
			return new CharSequence2Impl(removeSpecialInnerComment(s), location, preprocessorError);
		}
		return this;
	}

	private CharSequence removeSpecialInnerComment(CharSequence cs) {
		final String s = cs.toString();
		if (s.contains("/'''") && s.contains("'''/")) {
			return s.replaceAll("/'''[-\\w]*'''/", "");

		}
		return cs;
	}
}
