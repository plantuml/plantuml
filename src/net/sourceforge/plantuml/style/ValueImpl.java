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

import java.awt.Font;

import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;

public class ValueImpl implements Value {

	private final String value;
	private final int priority;

	public ValueImpl(String value, AutomaticCounter counter) {
		this.value = value;
		this.priority = counter.getNextInt();
	}

	@Override
	public String toString() {
		return value + " (" + priority + ")";
	}

	public String asString() {
		return value;
	}

	public HColor asColor(HColorSet set) {
		if ("none".equalsIgnoreCase(value) || "transparent".equalsIgnoreCase(value)) {
			return null;
		}
		return set.getColorIfValid(value);
	}

	public boolean asBoolean() {
		return "true".equalsIgnoreCase(value);
	}

	public int asInt() {
		return Integer.parseInt(value);
	}

	public double asDouble() {
		return Double.parseDouble(value);
	}

	public int asFontStyle() {
		if (value.equalsIgnoreCase("bold")) {
			return Font.BOLD;
		}
		if (value.equalsIgnoreCase("italic")) {
			return Font.ITALIC;
		}
		return Font.PLAIN;
	}

	public HorizontalAlignment asHorizontalAlignment() {
		return HorizontalAlignment.fromString(asString());
	}

	public int getPriority() {
		return priority;
	}

}