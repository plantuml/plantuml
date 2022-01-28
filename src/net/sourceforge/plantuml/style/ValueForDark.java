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
package net.sourceforge.plantuml.style;

import net.sourceforge.plantuml.ThemeStyle;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;
import net.sourceforge.plantuml.ugraphic.color.HColorSimple;

public class ValueForDark implements Value {

	private final Value regular;
	private final Value dark;

	public ValueForDark(Value regular, Value dark) {
		this.regular = regular;
		this.dark = dark;
	}

	@Override
	public String asString() {
		throw new UnsupportedOperationException();
	}

	@Override
	public HColor asColor(ThemeStyle themeStyle, HColorSet set) {
		final HColor result = regular.asColor(themeStyle, set);
		if (result instanceof HColorSimple)
			return ((HColorSimple) result).withDark(dark.asColor(themeStyle, set));
		return result;
	}

	@Override
	public int asInt() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double asDouble() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean asBoolean() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int asFontStyle() {
		throw new UnsupportedOperationException();
	}

	@Override
	public HorizontalAlignment asHorizontalAlignment() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getPriority() {
		throw new UnsupportedOperationException();
	}

}