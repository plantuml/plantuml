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
package net.sourceforge.plantuml.decoration;

import java.util.Objects;

import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

public class HtmlColorAndStyle {

	private final HColor arrowHeadColor;
	private final HColor arrowColor;
	private final LinkStyle style;

	@Override
	public String toString() {
		return arrowColor + " " + style;
	}

	public HtmlColorAndStyle(HColor color, HColor arrowHeadColor) {
		this(color, LinkStyle.NORMAL(), arrowHeadColor);
	}

	public HtmlColorAndStyle(HColor arrowColor, LinkStyle style, HColor arrowHeadColor) {
		this.arrowColor = Objects.requireNonNull(arrowColor);
		this.arrowHeadColor = arrowHeadColor == null ? arrowColor : arrowHeadColor;
		this.style = style;
	}

	public HColor getArrowColor() {
		return arrowColor;
	}

	public HColor getArrowHeadColor() {
		return arrowHeadColor;
	}

	public LinkStyle getStyle() {
		return style;
	}

	static final public StyleSignatureBasic getDefaultStyleDefinitionArrow() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.arrow);
	}

	public static HtmlColorAndStyle build(ISkinParam skinParam, String definition) throws NoSuchColorException {

		final Style style = getDefaultStyleDefinitionArrow().getMergedStyle(skinParam.getCurrentStyleBuilder());
		HColor arrowColor = style.value(PName.LineColor).asColor(skinParam.getIHtmlColorSet());
		final HColor arrowHeadColor = null;

		LinkStyle linkStyle = LinkStyle.NORMAL();
		final HColorSet set = skinParam.getIHtmlColorSet();
		for (String s : definition.split(",")) {
			final LinkStyle tmpStyle = LinkStyle.fromString1(s);
			if (tmpStyle.isNormal() == false) {
				linkStyle = tmpStyle;
				continue;
			}
			final HColor tmpColor = s == null ? null : set.getColor(s);
			if (tmpColor != null)
				arrowColor = tmpColor;

		}
		return new HtmlColorAndStyle(arrowColor, linkStyle, arrowHeadColor);
	}

}
