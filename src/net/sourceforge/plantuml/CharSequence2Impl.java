/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 *
 * Revision $Revision: 3824 $
 *
 */
package net.sourceforge.plantuml;

public class CharSequence2Impl implements CharSequence2 {

	private final CharSequence s;
	private final LineLocation location;

	public CharSequence2Impl(CharSequence s, LineLocation location) {
		if (s == null) {
			throw new IllegalArgumentException();
		}
		this.s = s;
		this.location = location;
	}

	public static CharSequence2 errorPreprocessor(CharSequence s, LineLocation lineLocation) {
		return new CharSequence2Impl(s, lineLocation);
	}

	public int length() {
		return s.length();
	}

	public char charAt(int index) {
		return s.charAt(index);
	}

	public CharSequence2 subSequence(int start, int end) {
		return new CharSequence2Impl(s.subSequence(start, end), location);
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
		return new CharSequence2Impl(StringUtils.trin(s.toString()), location);
	}

	public boolean startsWith(String start) {
		return s.toString().startsWith(start);
	}

}
