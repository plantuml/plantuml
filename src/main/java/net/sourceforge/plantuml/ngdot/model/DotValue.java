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
package net.sourceforge.plantuml.ngdot.model;

/**
 * The value of a DOT attribute.
 * <p>
 * DOT has four lexical forms for an ID (plain identifier, number, double-quoted
 * string, HTML string). Once parsed, only one distinction still matters: an
 * HTML string ({@code label=<...>}) must not be read as plain text. The
 * escapes of a double-quoted string are already resolved ({@code \"} and the
 * line continuation), the label escapes ({@code \n}, {@code \l}, {@code \N}...)
 * are kept verbatim.
 */
public final class DotValue {

	public static final DotValue TRUE = new DotValue("true", false);

	private final String text;
	private final boolean html;

	private DotValue(String text, boolean html) {
		if (text == null)
			throw new IllegalArgumentException();
		this.text = text;
		this.html = html;
	}

	public static DotValue of(String text) {
		return new DotValue(text, false);
	}

	public static DotValue html(String text) {
		return new DotValue(text, true);
	}

	public String getText() {
		return text;
	}

	public boolean isHtml() {
		return html;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DotValue == false)
			return false;
		final DotValue other = (DotValue) obj;
		return html == other.html && text.equals(other.text);
	}

	@Override
	public int hashCode() {
		return text.hashCode() + (html ? 1 : 0);
	}

	@Override
	public String toString() {
		if (html)
			return "<" + text + ">";
		return text;
	}

}
