/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
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

import java.awt.Font;
import java.util.Objects;

import net.sourceforge.plantuml.api.ThemeStyle;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;
import net.sourceforge.plantuml.ugraphic.color.HColorSimple;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class ValueImpl implements Value {

	private final DarkString value;

	public static ValueImpl dark(String value, AutomaticCounter counter) {
		return new ValueImpl(new DarkString(null, Objects.requireNonNull(value), counter.getNextInt()));
	}

	public static ValueImpl regular(String value, AutomaticCounter counter) {
		return new ValueImpl(new DarkString(Objects.requireNonNull(value), null, counter.getNextInt()));
	}

	public static ValueImpl regular(String value, int priority) {
		return new ValueImpl(new DarkString(Objects.requireNonNull(value), null, priority));
	}

	public Value mergeWith(Value other) {
		if (other == null)
			return this;
		if (other instanceof ValueImpl)
			return new ValueImpl(value.mergeWith(((ValueImpl) other).value));
		if (other instanceof ValueColor) {
			if (other.getPriority() > getPriority())
				return other;
			return this;
		}
		throw new UnsupportedOperationException();
	}

	private ValueImpl(DarkString value) {
		this.value = value;
	}

	public Value addPriority(int delta) {
		return new ValueImpl(value.addPriority(delta));
	}

	@Override
	public String toString() {
		return value.toString();
	}

	public String asString() {
		return value.getValue1();
	}

	public HColor asColor(ThemeStyle themeStyle, HColorSet set) {
		final String value1 = value.getValue1();
		if ("none".equalsIgnoreCase(value1))
			return null;

		if ("transparent".equalsIgnoreCase(value1))
			return HColorUtils.transparent();

		if (value1 == null)
			throw new IllegalArgumentException(value.toString());

		final HColor result = set.getColorOrWhite(themeStyle, value1);
		if (value.getValue2() != null) {
			final HColor dark = set.getColorOrWhite(themeStyle, value.getValue2());
			return ((HColorSimple) result).withDark(dark);
		}
		return result;
	}

	public boolean asBoolean() {
		return "true".equalsIgnoreCase(value.getValue1());
	}

	public int asInt() {
		return Integer.parseInt(value.getValue1());
	}

	public double asDouble() {
		return Double.parseDouble(value.getValue1());
	}

	public int asFontStyle() {
		if (value.getValue1().equalsIgnoreCase("bold"))
			return Font.BOLD;

		if (value.getValue1().equalsIgnoreCase("italic"))
			return Font.ITALIC;

		return Font.PLAIN;
	}

	public HorizontalAlignment asHorizontalAlignment() {
		return HorizontalAlignment.fromString(asString());
	}

	public int getPriority() {
		return value.getPriority();
	}

}