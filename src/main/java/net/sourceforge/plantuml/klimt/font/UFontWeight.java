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
package net.sourceforge.plantuml.klimt.font;

/**
 * Immutable value object representing a CSS-compatible font weight (100-900).
 * <p>
 * Values are clamped to the 100-900 range and rounded to the nearest hundred,
 * following CSS {@code font-weight} conventions.
 */
public class UFontWeight {

	// Common CSS weight constants
	public static final UFontWeight THIN = new UFontWeight(100);
	public static final UFontWeight EXTRA_LIGHT = new UFontWeight(200);
	public static final UFontWeight LIGHT = new UFontWeight(300);
	public static final UFontWeight NORMAL = new UFontWeight(400);
	public static final UFontWeight MEDIUM = new UFontWeight(500);
	public static final UFontWeight SEMI_BOLD = new UFontWeight(600);
	public static final UFontWeight BOLD = new UFontWeight(700);
	public static final UFontWeight EXTRA_BOLD = new UFontWeight(800);
	public static final UFontWeight BLACK = new UFontWeight(900);

	private final int weight;

	private UFontWeight(int weight) {
		this.weight = weight;
	}

	/**
	 * Creates a {@code UFontWeight} from a CSS numeric weight. The value is clamped
	 * to 100-900 and rounded to the nearest hundred.
	 */
	public static UFontWeight fromCssValue(int weight) {
		final int clamped = clamp(weight);
		// Return a shared constant when possible
		switch (clamped) {
		case 100:
			return THIN;
		case 200:
			return EXTRA_LIGHT;
		case 300:
			return LIGHT;
		case 400:
			return NORMAL;
		case 500:
			return MEDIUM;
		case 600:
			return SEMI_BOLD;
		case 700:
			return BOLD;
		case 800:
			return EXTRA_BOLD;
		case 900:
			return BLACK;
		default:
			// Should not happen after clamping, but safe fallback
			return new UFontWeight(clamped);
		}
	}

	public int getWeight() {
		return weight;
	}

	public boolean isBold() {
		return weight >= 700;
	}

	private static int clamp(int value) {
		if (value < 100)
			return 100;
		if (value > 900)
			return 900;
		return ((value + 50) / 100) * 100;
	}

	@Override
	public int hashCode() {
		return weight;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof UFontWeight))
			return false;
		return this.weight == ((UFontWeight) obj).weight;
	}

	@Override
	public String toString() {
		return "UFontWeight[" + weight + "]";
	}

}
