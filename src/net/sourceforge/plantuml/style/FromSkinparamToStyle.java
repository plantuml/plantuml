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
import java.util.StringTokenizer;

public class FromSkinparamToStyle {

	private Style style;

	public FromSkinparamToStyle(String key, String value, AutomaticCounter counter) {
		final Map<PName, Value> map = new EnumMap<PName, Value>(PName.class);
		String stereo = null;
		if (key.contains("<<")) {
			final StringTokenizer st = new StringTokenizer(key, "<>");
			key = st.nextToken();
			stereo = st.nextToken();
		}
		SName styleName = null;
		if (key.equalsIgnoreCase("participantBackgroundColor")) {
			styleName = SName.participant;
			map.put(PName.BackGroundColor, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("footerFontColor")) {
			styleName = SName.footer;
			map.put(PName.FontColor, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("footerFontSize")) {
			styleName = SName.footer;
			map.put(PName.FontSize, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("SequenceGroupBorderColor")) {
			styleName = SName.group;
			map.put(PName.LineColor, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("SequenceBoxBorderColor")) {
			styleName = SName.box;
			map.put(PName.LineColor, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("SequenceBoxBackgroundColor")) {
			styleName = SName.box;
			map.put(PName.BackGroundColor, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("SequenceLifeLineBorderColor")) {
			styleName = SName.lifeLine;
			map.put(PName.LineColor, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("noteFontColor")) {
			styleName = SName.note;
			map.put(PName.FontColor, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("noteBackgroundColor")) {
			styleName = SName.note;
			map.put(PName.BackGroundColor, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("noteBackgroundColor")) {
			styleName = SName.note;
			map.put(PName.BackGroundColor, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("packageBackgroundColor")) {
			styleName = SName.group;
			map.put(PName.BackGroundColor, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("packageBorderColor")) {
			styleName = SName.group;
			map.put(PName.LineColor, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("PartitionBorderColor")) {
			styleName = SName.partition;
			map.put(PName.LineColor, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("PartitionBackgroundColor")) {
			styleName = SName.partition;
			map.put(PName.BackGroundColor, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("PartitionFontColor")) {
			styleName = SName.partition;
			map.put(PName.FontColor, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("PartitionFontSize")) {
			styleName = SName.partition;
			map.put(PName.FontSize, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("hyperlinkColor")) {
			styleName = SName.root;
			map.put(PName.HyperLinkColor, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("activityStartColor")) {
			styleName = SName.circle;
			map.put(PName.LineColor, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("activityBarColor")) {
			styleName = SName.activityBar;
			map.put(PName.LineColor, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("activityBorderColor")) {
			styleName = SName.activity;
			map.put(PName.LineColor, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("activityBorderThickness")) {
			styleName = SName.activity;
			map.put(PName.LineThickness, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("activityBackgroundColor")) {
			styleName = SName.activity;
			map.put(PName.BackGroundColor, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("activityFontColor")) {
			styleName = SName.activity;
			map.put(PName.FontColor, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("activityFontSize")) {
			styleName = SName.activity;
			map.put(PName.FontSize, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("activityFontName")) {
			styleName = SName.activity;
			map.put(PName.FontName, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("activityDiamondBackgroundColor")) {
			styleName = SName.diamond;
			map.put(PName.BackGroundColor, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("activityDiamondBorderColor")) {
			styleName = SName.diamond;
			map.put(PName.LineColor, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("activityDiamondFontSize")) {
			styleName = SName.diamond;
			map.put(PName.FontSize, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("activityDiamondFontColor")) {
			styleName = SName.diamond;
			map.put(PName.FontColor, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("arrowColor")) {
			styleName = SName.arrow;
			map.put(PName.LineColor, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("arrowFontColor")) {
			styleName = SName.arrow;
			map.put(PName.FontColor, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("defaulttextalignment")) {
			styleName = SName.root;
			map.put(PName.HorizontalAlignment, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("defaultFontName")) {
			styleName = SName.root;
			map.put(PName.FontName, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("SwimlaneTitleFontColor")) {
			styleName = SName.swimlane;
			map.put(PName.FontColor, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("SwimlaneTitleFontSize")) {
			styleName = SName.swimlane;
			map.put(PName.FontSize, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("SwimlaneTitleBackgroundColor")) {
			styleName = SName.swimlane;
			map.put(PName.BackGroundColor, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("SwimlaneBorderColor")) {
			styleName = SName.swimlane;
			map.put(PName.LineColor, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("SwimlaneBorderThickness")) {
			styleName = SName.swimlane;
			map.put(PName.LineThickness, new ValueImpl(value, counter));
		} else if (key.equalsIgnoreCase("shadowing")) {
			styleName = SName.root;
			if (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("no")) {
				map.put(PName.Shadowing, new ValueImpl("0", counter));
			}
		}
		if (styleName != null && map.size() > 0) {
			String tmp = styleName.name();
			if (stereo != null) {
				tmp = tmp + "&" + stereo;
			}
			style = new Style(tmp, map);
		}
	}

	public Style getStyle() {
		return style;
	}

}