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

public class CharClass {

	private final CharClassRaw charClassRaw;
	private final CharClassType type;

	public CharClass(CharClassRaw charClassRaw, CharClassType type) {
		this.charClassRaw = charClassRaw;
		this.type = type;
	}

	public boolean matches(char ch) {
		final boolean rawMatch = charClassRaw.internalMatches(ch);

		if (type == CharClassType.NORMAL)
			return rawMatch;
		else
			return !rawMatch;

	}

	public static CharClass fromDefinition(TextNavigator nav) {
		final CharClassType type = Character.isUpperCase(nav.charAt(0)) ? CharClassType.NEGATIVE : CharClassType.NORMAL;
		final CharClassRaw raw = CharClassRaw.fromDefinition(nav);

		return new CharClass(raw, type);
	}

	public String name() {
		return charClassRaw.name() + "-" + type.name();
	}

	public int getDefinitionLength() {
		return charClassRaw.getDefinitionLength();
	}

}
