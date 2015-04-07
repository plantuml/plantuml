/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 13970 $
 *
 */
package net.sourceforge.plantuml.graphic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.command.regex.MyPattern;

class ColorAndSizeChange implements FontChange {

	static final Pattern colorPattern = MyPattern.cmpile("(?i)color\\s*=\\s*[%g]?(#[0-9a-fA-F]{6}|\\w+)[%g]?");

	static final Pattern sizePattern = MyPattern.cmpile("(?i)size\\s*=\\s*[%g]?(\\d+)[%g]?");

	private final HtmlColor color;
	private final Integer size;

	ColorAndSizeChange(String s) {
		final Matcher matcherColor = colorPattern.matcher(s);
		if (matcherColor.find()) {
			color = HtmlColorSet.getInstance().getColorIfValid(matcherColor.group(1));
		} else {
			color = null;
		}
		final Matcher matcherSize = sizePattern.matcher(s);
		if (matcherSize.find()) {
			size = new Integer(matcherSize.group(1));
		} else {
			size = null;
		}
	}

	HtmlColor getColor() {
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
