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
package net.sourceforge.plantuml.klimt.color;

import java.awt.Color;

public abstract class ColorMapper {

	private ColorMapper() {
	}

	public abstract Color fromColorSimple(HColorSimple simple);

	public final static ColorMapper IDENTITY = new ColorMapper() {
		@Override
		public Color fromColorSimple(HColorSimple simple) {
			return simple.getAwtColor();
		}
	};
	// ::comment when __HAXE__
	public final static ColorMapper DARK_MODE = new ColorMapper() {
		@Override
		public Color fromColorSimple(HColorSimple simple) {
			return ((HColorSimple) simple.darkSchemeTheme()).getAwtColor();
		}
	};
	public final static ColorMapper LIGTHNESS_INVERSE = new ColorMapper() {
		@Override
		public Color fromColorSimple(HColorSimple simple) {
			return ColorUtils.getReversed(simple.getAwtColor());
		}
	};
	public static final ColorMapper MONOCHROME = new ColorMapper() {
		@Override
		public Color fromColorSimple(HColorSimple simple) {
			return ColorUtils.getGrayScaleColor(simple.getAwtColor());
		}
	};
	public static final ColorMapper MONOCHROME_REVERSE = new ColorMapper() {
		@Override
		public Color fromColorSimple(HColorSimple simple) {
			return ColorUtils.getGrayScaleColorReverse(simple.getAwtColor());
		}
	};

	public static ColorMapper reverse(final ColorOrder order) {
		return new ColorMapper() {
			@Override
			public Color fromColorSimple(HColorSimple simple) {
				return order.getReverse(simple.getAwtColor());
			}
		};
	}

	// ::done

}
