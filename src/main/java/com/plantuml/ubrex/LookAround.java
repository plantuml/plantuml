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

public enum LookAround {
	LOOK_AHEAD_POSITIVE("(=)"), //
	LOOK_AHEAD_NEGATIVE("(!)"), //
	LOOK_BEHIND_POSITIVE("(<=)"), //
	LOOK_BEHIND_NEGATIVE("(<!)"), //
	END_OF_TEXT("($)");

	private final String definition;

	LookAround(String definition) {
		this.definition = definition;
	}

	public static LookAround from(CharSequence input) {
		final char ch0 = input.charAt(0);
		final char ch1 = input.charAt(1);
		if (ch0 != '(')
			return null;

		if (ch1 == '$')
			return END_OF_TEXT;
		if (ch1 == '=')
			return LOOK_AHEAD_POSITIVE;
		if (ch1 == '!')
			return LOOK_AHEAD_NEGATIVE;

		if (ch1 == '<') {
			final char ch2 = input.charAt(2);
			if (ch2 == '=')
				return LOOK_BEHIND_POSITIVE;
			if (ch2 == '!')
				return LOOK_BEHIND_NEGATIVE;
		}

		return null;
	}

	public boolean isLookBehind() {
		switch (this) {
		case LOOK_BEHIND_POSITIVE:
		case LOOK_BEHIND_NEGATIVE:
			return true;
		}
		return false;
	}

	public boolean isLookAhead() {
		switch (this) {
		case LOOK_AHEAD_POSITIVE:
		case LOOK_AHEAD_NEGATIVE:
			return true;
		}
		return false;
	}

	public int getDefinitionSize() {
		return definition.length();
	}

}
