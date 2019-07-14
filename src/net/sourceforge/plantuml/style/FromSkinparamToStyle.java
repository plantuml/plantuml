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

import java.util.EnumMap;
import java.util.Map;

public class FromSkinparamToStyle {

	private Style style;

	public FromSkinparamToStyle(String key, String value, AutomaticCounter counter) {
		final Map<PName, Value> map = new EnumMap<PName, Value>(PName.class);
		SName styleName = null;
		if (key.equalsIgnoreCase("participantBackgroundColor")) {
			styleName = SName.participant;
			map.put(PName.BackGroundColor, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("SequenceLifeLineBorderColor")) {
			styleName = SName.lifeLine;
			map.put(PName.LineColor, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("noteBackgroundColor")) {
			styleName = SName.note;
			map.put(PName.BackGroundColor, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("arrowColor")) {
			styleName = SName.message;
			map.put(PName.LineColor, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("arrowFontColor")) {
			styleName = SName.message;
			map.put(PName.FontColor, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("noteFontColor")) {
			styleName = SName.note;
			map.put(PName.FontColor, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("defaulttextalignment")) {
			styleName = SName.root;
			map.put(PName.HorizontalAlignment, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("defaultFontName")) {
			styleName = SName.root;
			map.put(PName.FontName, new ValueImpl(value, counter));
		}
		if (styleName != null && map.size() > 0) {
			style = new Style(StyleKind.STYLE, styleName.name(), map);
		}
	}

	public Style getStyle() {
		return style;
	}

}