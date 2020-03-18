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
package net.sourceforge.plantuml.graphic;

import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;

class ColorAndSizeChange implements FontChange {

	static final Pattern2 colorPattern = MyPattern.cmpile("(?i)color\\s*=\\s*[%g]?(#[0-9a-fA-F]{6}|\\w+)[%g]?");

	static final Pattern2 sizePattern = MyPattern.cmpile("(?i)size\\s*=\\s*[%g]?(\\d+)[%g]?");

	private final HColor color;
	private final Integer size;

	ColorAndSizeChange(String s) {
		final Matcher2 matcherColor = colorPattern.matcher(s);
		if (matcherColor.find()) {
			color = HColorSet.instance().getColorIfValid(matcherColor.group(1));
		} else {
			color = null;
		}
		final Matcher2 matcherSize = sizePattern.matcher(s);
		if (matcherSize.find()) {
			size = new Integer(matcherSize.group(1));
		} else {
			size = null;
		}
	}

	HColor getColor() {
		return color;
	}

	Integer getSize() {
		return size;
	}

	public FontConfiguration apply(FontConfiguration initial) {
		FontConfiguration result = initial;
		if (color != null) {
			result = result.changeColor(color);
		}
		if (size != null) {
			result = result.changeSize(size);
		}
		return result;
	}

}
