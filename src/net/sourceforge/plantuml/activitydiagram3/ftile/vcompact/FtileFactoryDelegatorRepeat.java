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
 *
 * Original Author:  Arnaud Roques
 *
 *
 */
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactoryDelegator;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorAndStyle;
import net.sourceforge.plantuml.graphic.Rainbow;
import net.sourceforge.plantuml.svek.ConditionStyle;

public class FtileFactoryDelegatorRepeat extends FtileFactoryDelegator {

	public FtileFactoryDelegatorRepeat(FtileFactory factory) {
		super(factory);
	}

	@Override
	public Ftile repeat(Swimlane swimlane, Swimlane swimlaneOut, Ftile repeat, Display test, Display yes, Display out,
			HtmlColor color, LinkRendering backRepeatLinkRendering) {
		final ConditionStyle conditionStyle = skinParam().getConditionStyle();

		final HtmlColor borderColor = getRose().getHtmlColor(skinParam(), ColorParam.activityBorder);
		final HtmlColor backColor = color == null ? getRose().getHtmlColor(skinParam(), ColorParam.activityBackground)
				: color;
		final Rainbow arrowColor = HtmlColorAndStyle.build(skinParam());

		final LinkRendering endRepeatLinkRendering = repeat.getOutLinkRendering();
		final Rainbow endRepeatLinkColor = endRepeatLinkRendering == null ? null : endRepeatLinkRendering.getRainbow();

		final FontConfiguration fcDiamond = new FontConfiguration(skinParam(), FontParam.ACTIVITY_DIAMOND, null);
		final FontConfiguration fcArrow = new FontConfiguration(skinParam(), FontParam.ACTIVITY_ARROW, null);

		return FtileRepeat.create(backRepeatLinkRendering, swimlane, swimlaneOut, repeat, test, yes, out, borderColor,
				backColor, arrowColor, endRepeatLinkColor, conditionStyle, this.skinParam(), fcDiamond, fcArrow);
	}
}
