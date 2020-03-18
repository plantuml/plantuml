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
package net.sourceforge.plantuml;

import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class SkinParamColors extends SkinParamDelegator {

	public final Colors getColors() {
		return colors;
	}

	final private Colors colors;

	public SkinParamColors(ISkinParam skinParam, Colors colors) {
		super(skinParam);
		this.colors = colors;
	}

	@Override
	public String toString() {
		return super.toString() + colors;
	}

	@Override
	public boolean shadowing(Stereotype stereotype) {
		if (colors.getShadowing() == null) {
			return super.shadowing(stereotype);
		}
		return colors.getShadowing();
	}

	@Override
	public HColor getFontHtmlColor(Stereotype stereotype, FontParam... param) {
		final HColor value = colors.getColor(ColorType.TEXT);
		if (value == null) {
			return super.getFontHtmlColor(stereotype, param);
		}
		return value;
	}

	@Override
	public HColor getHtmlColor(ColorParam param, Stereotype stereotype, boolean clickable) {
		final ColorType type = param.getColorType();
		if (type == null) {
			return super.getHtmlColor(param, stereotype, clickable);
		}
		final HColor value = colors.getColor(type);
		if (value != null) {
			return value;
		}
		assert value == null;
		return super.getHtmlColor(param, stereotype, clickable);
	}

}
