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
package net.sourceforge.plantuml.cucadiagram;

import net.sourceforge.plantuml.StringUtils;

public class Code implements Comparable<Code> {

	private final String fullName;
	private final String separator;

	private Code(String fullName, String separator) {
		if (fullName == null) {
			throw new IllegalArgumentException();
		}
		this.fullName = fullName;
		this.separator = separator;
	}

	public Code removeMemberPart() {
		final int x = fullName.lastIndexOf("::");
		if (x == -1) {
			return null;
		}
		return new Code(fullName.substring(0, x), separator);
	}

	public String getPortMember() {
		final int x = fullName.lastIndexOf("::");
		if (x == -1) {
			return null;
		}
		return fullName.substring(x + 2);
	}

	public Code withSeparator(String separator) {
		if (separator == null) {
			throw new IllegalArgumentException();
		}
		if (this.separator != null && this.separator.equals(separator) == false) {
			throw new IllegalStateException();
		}
		return new Code(fullName, separator);
	}

	public static Code of(String code) {
		return of(code, null);
	}

	public static Code of(String code, String separator) {
		if (code == null) {
			return null;
		}
		return new Code(code, separator);
	}

	public final String getFullName() {
		return fullName;
	}

	@Override
	public String toString() {
		return fullName + "(" + separator + ")";
	}

	@Override
	public int hashCode() {
		return fullName.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		final Code other = (Code) obj;
		return this.fullName.equals(other.fullName);
	}

	public Code addSuffix(String suffix) {
		return new Code(fullName + suffix, separator);
	}

	public int compareTo(Code other) {
		return this.fullName.compareTo(other.fullName);
	}

	public Code eventuallyRemoveStartingAndEndingDoubleQuote(String format) {
		return Code.of(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(fullName, format), separator);
	}

	public final String getSeparator() {
		return separator;
	}

}
