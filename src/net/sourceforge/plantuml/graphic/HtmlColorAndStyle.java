/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
 * Revision $Revision: 19109 $
 *
 */
package net.sourceforge.plantuml.graphic;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.cucadiagram.LinkStyle;
import net.sourceforge.plantuml.skin.rose.Rose;

public class HtmlColorAndStyle {

	private final static Rose rose = new Rose();

	private final HtmlColor color;
	private final LinkStyle style;

	public static Rainbow fromColor(HtmlColor color) {
		if (color == null) {
			return Rainbow.none();
		}
		return Rainbow.build(new HtmlColorAndStyle(color));
	}

	public static Rainbow build(ISkinParam skinParam) {
		return fromColor(rose.getHtmlColor(skinParam, ColorParam.activityArrow));
	}

	private HtmlColorAndStyle(HtmlColor color) {
		this(color, LinkStyle.NORMAL);
	}

	public HtmlColorAndStyle(HtmlColor color, LinkStyle style) {
		if (color == null) {
			throw new IllegalArgumentException();
		}
		this.color = color;
		this.style = style;
	}

	public HtmlColor getColor() {
		return color;
	}

	public LinkStyle getStyle() {
		return style;
	}

	public static HtmlColorAndStyle build(ISkinParam skinParam, String definition) {
		HtmlColor color = build(skinParam).getColors().get(0).color;
		LinkStyle style = LinkStyle.NORMAL;
		final IHtmlColorSet set = skinParam.getIHtmlColorSet();
		for (String s : definition.split(",")) {
			final LinkStyle tmpStyle = LinkStyle.fromString(s);
			if (tmpStyle != LinkStyle.NORMAL) {
				style = tmpStyle;
				continue;
			}
			final HtmlColor tmpColor = set.getColorIfValid(s);
			if (tmpColor != null) {
				color = tmpColor;
			}
		}
		return new HtmlColorAndStyle(color, style);
	}

}
