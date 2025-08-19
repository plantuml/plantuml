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
package net.sourceforge.plantuml.style;

import java.awt.Font;
import java.util.Objects;

import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;

public class ValueImpl implements Value {
	// ::remove file when __HAXE__

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

	public HColor asColor(HColorSet set) {
		final String value1 = value.getValue1();
		if ("none".equalsIgnoreCase(value1))
			return HColors.transparent();

		if ("transparent".equalsIgnoreCase(value1))
			return HColors.transparent();

		if (value1 == null)
			throw new IllegalArgumentException(value.toString());

		final HColor result = set.getColorOrWhite(value1);
		if (value.getValue2() != null) {
			final HColor dark = set.getColorOrWhite(value.getValue2());
			return result.withDark(dark);
		}
		return result;
	}

	public boolean asBoolean() {
		return "true".equalsIgnoreCase(value.getValue1());
	}

	public int asInt(boolean minusOneIfError) {
		final String s = extractDigits(value.getValue1());
		if (s.length() == 0)
			return minusOneIfError ? -1 : 0;
		return Integer.parseInt(s);
	}

	private String extractDigits(String s) {
		final StringBuilder sb = new StringBuilder(s.length());
		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			if (c >= '0' && c <= '9')
				sb.append(c);

		}
		return sb.toString();
	}

	private String extractDigitsAndDot(String s) {
		final StringBuilder sb = new StringBuilder(s.length());
		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			if (c >= '0' && c <= '9' || c == '.')
				sb.append(c);

		}
		return sb.toString();
	}

	public double asDouble() {
		final String s = extractDigitsAndDot(value.getValue1());
		return Double.parseDouble(s);
	}

	public double asDoubleDefaultTo(double defaultValue) {
		final String s = extractDigitsAndDot(value.getValue1());
		if (s.length() > 0)
			try {
				return Double.parseDouble(s);
			} catch (NumberFormatException e) {
			}
		return defaultValue;
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