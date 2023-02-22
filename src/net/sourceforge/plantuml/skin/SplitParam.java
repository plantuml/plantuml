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
package net.sourceforge.plantuml.skin;

import java.awt.Color;

import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSimple;

public class SplitParam {

	private final HColor borderColor;
	private final HColor externalColor;
	private final int externalMargin;

	private static SplitParam empty() {
		return new SplitParam(null, null, 0);
	}

	public SplitParam(HColor borderColor, HColor externalColor, int externalMargin) {
		if (borderColor != null && externalMargin == 0) {
			externalMargin = 1;
		}
		this.borderColor = borderColor;
		this.externalColor = externalColor;
		this.externalMargin = externalMargin;
	}

	public boolean isSet() {
		return externalMargin > 0;
	}

	public int getExternalMargin() {
		return externalMargin;
	}

	public Color getBorderColor() {
		if (borderColor == null)
			return null;

		return ((HColorSimple) borderColor).getAwtColor();
	}

	public Color getExternalColor() {
		if (externalColor == null)
			return null;

		return ((HColorSimple) externalColor).getAwtColor();
	}

}